package ar.edu.ubp.das.resources;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import ar.edu.ubp.das.beans.ReclamoBean;
import ar.edu.ubp.das.config.Config;
import ar.edu.ubp.das.ws.LoginWS;
import ar.edu.ubp.das.ws.LoginWSService;
import ar.edu.ubp.das.ws.UsuarioBean;

@Path("/reclamos")
@Produces(MediaType.APPLICATION_JSON)
public class ReclamosResource {

	private final int CONSTRAINT_ERROR = 547;

	@GET
	public Response getReclamos() {
		System.out.println("getReclamos");
		LinkedList<ReclamoBean> reclamos = new LinkedList();
		try {
			Connection conn;
			CallableStatement st;
			ResultSet result;

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=das",
					Config.getInstance().getDbUser(), Config.getInstance().getDbPass());

			st = conn.prepareCall("{CALL dbo.get_reclamos}");
			st.execute();

			result = st.getResultSet();
			while (result.next()) {
				ReclamoBean reclamo = new ReclamoBean();
				reclamo.setFechaHora(result.getString("fecha_hora"));
				reclamo.setNroChasis(result.getString("nro_chasis"));
				reclamo.setDominio(result.getString("dominio"));
				reclamo.setKm(result.getInt("km"));
				reclamo.setApellido(result.getString("apellido"));
				reclamo.setNombre(result.getString("nombre"));
				reclamo.setEmail(result.getString("email"));
				reclamo.setTelefono(result.getString("telefono"));
				reclamo.setContactar(result.getString("contactar"));
				reclamo.setLocalidad(result.getString("localidad"));
				reclamo.setReclamo(result.getString("reclamo"));
				reclamos.add(reclamo);

			}
			st.close();
			conn.close();
			return Response.status(Response.Status.OK).entity(reclamos).build();
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			ex.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReclamo(String body) throws SQLException {

		Integer nro_reclamo;
		String respuesta;
		Integer resp_respuesta;

		try {
			JsonObject jobj = new Gson().fromJson(body.toString(), JsonObject.class);
			nro_reclamo = Integer.valueOf(jobj.get("nro_reclamo").toString());
			respuesta = jobj.get("respuesta").toString();
			resp_respuesta = Integer.valueOf(jobj.get("resp_respuesta").toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity("Body incorrecto").build();
		}

		Connection conn = null;
		try {
			CallableStatement st;

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=das",
					Config.getInstance().getDbUser(), Config.getInstance().getDbPass());

			conn.setAutoCommit(false);

			st = conn.prepareCall("{CALL dbo.act_respuesta_reclamo(?,?,?)}");

			st.setInt(1, nro_reclamo);
			st.setString(2, respuesta);
			st.setInt(3, resp_respuesta);

			int result = st.executeUpdate();

			if (result < 1) {
				return Response.status(Response.Status.BAD_REQUEST).entity("nro reclamo mal").build();
			}

			conn.commit();

			return Response.status(Response.Status.OK).build();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			conn.rollback();
			ex.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();

		} catch (SQLServerException ex) {
			ex.printStackTrace();
			if (ex.getErrorCode() == CONSTRAINT_ERROR) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Constraint").build();
			}

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();

		} finally {
			conn.close();
		}
	}

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUsuario(Object body) {
		JsonObject jobj = new Gson().fromJson(body.toString(), JsonObject.class);

		String usuario, clave;
		try {
			usuario = jobj.get("usuario").toString();
			clave = jobj.get("clave").toString();
		} catch (NullPointerException ex) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Body incorrecto").build();
		}

		LoginWSService service = new LoginWSService();
		LoginWS login = service.getLoginWSPort();
		try {
			UsuarioBean usr = login.getUsuario(usuario, clave);
			return Response.status(Response.Status.OK).entity(usr).build();
		} catch (WebServiceException e) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity("{\"error\":\"Password o usuario incorrectos\"}").type("application/json").build();
		}
	}
}
