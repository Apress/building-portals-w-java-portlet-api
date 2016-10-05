package com.portalbook.sso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.security.auth.Subject;

import org.ietf.jgss.GSSContext;

public class AuthClient {

    public AuthClient(String server, int port) throws IOException {
        socket = new Socket(server, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void establishContext(
        String clientPrincipal,
        String clientPassword,
        String serverPrincipal)
        throws Exception {
        Authentication.setKerberosSystemProperties();

        Subject subject =
            Authentication.jaasLogin(
                "ClientContext",
                clientPrincipal,
                clientPassword);
        System.out.println("Logged in " + subject);

        serverContext =
            GSSContextUtil.createOutgoingGSSContext(
                subject,
                clientPrincipal,
                serverPrincipal);
        System.out.println(
            "Server context established:" + serverContext.isEstablished());
    }

    public GSSContext handshake() throws Exception {
        System.out.println("Performing handshake");

        // Set mode flags for our secure context
        serverContext.requestConf(true);
        serverContext.requestMutualAuth(true);
        serverContext.requestReplayDet(true);
        serverContext.requestSequenceDet(true);

        while (!serverContext.isEstablished()) {
            System.out.println("Exchanging tokens");
            serverContext.initSecContext(input, output);
            output.flush();
        }

        System.out.println("Handshake completed");
        return serverContext;
    }

    private Socket socket;
    private GSSContext serverContext;
    private InputStream input;
    private OutputStream output;
    
    public static void main(String[] argv) throws Exception {

       // Creating a client to connect to the server on
        // an appropriate socket. Note that establishing a
        // line of communication is our responsibility.
        AuthClient client = new AuthClient("192.168.0.5", 3000);

        // Create context objects
        System.out.println("Establish server context");
        client.establishContext("dave", "myPassword", "login/java");

        // Create a secure context by handshaking with the server
        GSSContext ctx = client.handshake();
        System.out.println("Server context established:" + ctx.isEstablished());

        // Close the socket and terminate
        client.close();
        System.out.println("OK");
    }
}
