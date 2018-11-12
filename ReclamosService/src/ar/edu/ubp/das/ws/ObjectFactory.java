
package ar.edu.ubp.das.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ar.edu.ubp.das.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetUsuario_QNAME = new QName("http://ws.das.ubp.edu.ar/", "getUsuario");
    private final static QName _GetUsuarioResponse_QNAME = new QName("http://ws.das.ubp.edu.ar/", "getUsuarioResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ar.edu.ubp.das.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUsuario }
     * 
     */
    public GetUsuario createGetUsuario() {
        return new GetUsuario();
    }

    /**
     * Create an instance of {@link GetUsuarioResponse }
     * 
     */
    public GetUsuarioResponse createGetUsuarioResponse() {
        return new GetUsuarioResponse();
    }

    /**
     * Create an instance of {@link UsuarioBean }
     * 
     */
    public UsuarioBean createUsuarioBean() {
        return new UsuarioBean();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsuario }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.das.ubp.edu.ar/", name = "getUsuario")
    public JAXBElement<GetUsuario> createGetUsuario(GetUsuario value) {
        return new JAXBElement<GetUsuario>(_GetUsuario_QNAME, GetUsuario.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsuarioResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.das.ubp.edu.ar/", name = "getUsuarioResponse")
    public JAXBElement<GetUsuarioResponse> createGetUsuarioResponse(GetUsuarioResponse value) {
        return new JAXBElement<GetUsuarioResponse>(_GetUsuarioResponse_QNAME, GetUsuarioResponse.class, null, value);
    }

}
