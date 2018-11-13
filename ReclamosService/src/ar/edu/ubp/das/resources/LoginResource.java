package ar.edu.ubp.das.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ar.edu.ubp.das.ws.LoginWS;
import ar.edu.ubp.das.ws.LoginWSService;
import ar.edu.ubp.das.ws.UsuarioBean;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {
	@POST
	public Response getUsuario(Object body) {		
		JsonObject jobj = new Gson().fromJson(body.toString(), JsonObject.class);
		
		String usuario, clave;		
		try {
			usuario = jobj.get("usuario").toString();
			clave = jobj.get("clave").toString();
		}catch (NullPointerException ex){
			return Response.status(Response.Status.BAD_REQUEST).entity("Body incorrecto").build();
		}
		
		LoginWSService service = new LoginWSService();
		LoginWS login = service.getLoginWSPort();
		try {
			UsuarioBean usr = login.getUsuario(usuario, clave);
			return Response.status(Response.Status.OK).entity(usr).build();
		}catch(WebServiceException e) {			
			return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Password o usuario incorrectos\"}").type("application/json").build();
		}
	}
}
