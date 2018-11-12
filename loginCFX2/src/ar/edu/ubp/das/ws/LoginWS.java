package ar.edu.ubp.das.ws;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.cxf.interceptor.Fault;

import ar.edu.ubp.das.beans.UsuarioBean;
import ar.edu.ubp.das.config.Config;

@XmlSeeAlso({UsuarioBean.class})
@WebService(targetNamespace = "http://ws.das.ubp.edu.ar/", portName = "LoginWSPort", serviceName = "LoginWSService")
public class LoginWS {

	@WebMethod(operationName = "getUsuario", action = "urn:GetUsuario")
	public UsuarioBean getUsuario(@WebParam(name = "arg0") String usuario, @WebParam(name = "arg1") String clave) {
		Connection conn;
		CallableStatement stmt;
		ResultSet result;
		UsuarioBean usr;
		usr = new UsuarioBean();
		
		System.out.println("usuario --> " + usuario + " clave --> " + clave);		
		usuario = usuario.replace("\"", "");
		clave = clave.replace("\"", "");

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=das",
					Config.getInstance().getDbUser(), Config.getInstance().getDbPass());
			conn.setAutoCommit(true);

			stmt = conn.prepareCall("{CALL dbo.login_usuario(?,?) }");
			stmt.setString(1, usuario);
			stmt.setString(2, clave);
			result = stmt.executeQuery();

			if (result.next()) {
				usr.setApellido(result.getString("apellido"));
				usr.setNombre(result.getString("nombre"));
				usr.setNroDocumento(result.getString("nro_documento"));
				usr.setTipoDocumento(result.getString("tipo_documento"));
				usr.setNroPersona(result.getInt("nro_persona"));
			}

			stmt.close();
			conn.close();

		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error!!! --> " + e.getMessage());
			Fault fault = new Fault(new Exception("El usuario no existe"));
			fault.setStatusCode(409);
			throw fault;
		}
		return usr;
	}

}
