package ar.edu.ubp.das.resources;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ar.edu.ubp.das.beans.ReclamoBean;
import ar.edu.ubp.das.config.Config;

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
				reclamo.setNroReclamo(result.getInt("nro_reclamo"));
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

		Integer nroReclamo = null;
		String respuesta = null;
		Integer respRespuesta = null;

		try {
			JsonObject jobj = new Gson().fromJson(body.toString(), JsonObject.class);
			nroReclamo = Integer.valueOf(jobj.get("nroReclamo").toString());
			respuesta = jobj.get("respuesta").toString();
			respRespuesta = Integer.valueOf(jobj.get("respRespuesta").toString());

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

			try {

				st = conn.prepareCall("{CALL dbo.act_respuesta_reclamo(?,?,?)}");

				st.setInt(1, nroReclamo);
				st.setString(2, respuesta);
				st.setInt(3, respRespuesta);

				int result = st.executeUpdate();

				if (result < 1) {
					return Response.status(Response.Status.BAD_REQUEST).entity("El nro reclamo esta mal").build();
				}

				conn.commit();
				st.close();

				return Response.status(Response.Status.OK).build();

			} catch (SQLException ex) {
				conn.rollback();
				ex.printStackTrace();
				throw new SQLException("Error al actualizar los datos");

			} finally {
				conn.setAutoCommit(true);
				conn.close();
			}

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
		}
	}

}
