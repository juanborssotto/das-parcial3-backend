package ar.edu.ubp.das.resources;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ar.edu.ubp.das.beans.ReclamoBean;
import ar.edu.ubp.das.config.Config;

@Path("/reclamos")
@Produces(MediaType.APPLICATION_JSON)
public class ReclamosResource {

	@GET
	public Response getReclamos() {
		LinkedList <ReclamoBean> reclamos = new LinkedList();
		try {        	
        	Connection conn;
	        CallableStatement st;
            ResultSet result;
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
	        conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=das", Config.getInstance().getDbUser(), Config.getInstance().getDbPass());
            
            st = conn.prepareCall("{CALL dbo.get_reclamos}");
            st.execute();

	        result = st.getResultSet();
	        while(result.next()) {
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
		} 
		catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			ex.printStackTrace();
		    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error interno del servidor").build();
		}
	}
	
}
