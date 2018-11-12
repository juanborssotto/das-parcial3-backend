package ar.edu.ubp.das.beans;


public class UsuarioBean {

	private int nroPersona;
	private String apellido;
	private String nombre;
	private String tipoDocumento;
	private String nroDocumento;

	public int getNroPersona() {
		return nroPersona;
	}

	public void setNroPersona(int nroPersona) {
		this.nroPersona = nroPersona;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

}
