
package ar.edu.ubp.das.ws;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 3.2.6
 * 2018-11-08T13:21:16.180-03:00
 * Generated source version: 3.2.6
 *
 */

public class LoginWS_PortTypeServer{

    protected LoginWS_PortTypeServer() throws Exception {
        System.out.println("Starting Server");
        Object implementor = new ar.edu.ubp.das.ws.LoginWS();
        String address = "http://localhost:9090/LoginWSPort";
        Endpoint.publish(address, implementor);
    }

    public static void main(String args[]) throws Exception {
        new LoginWS_PortTypeServer();
        System.out.println("Server ready...");

//        Thread.sleep(5 * 60 * 1000);
//        System.out.println("Server exiting");
//        System.exit(0);
    }
}

