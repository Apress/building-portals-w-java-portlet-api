package com.portalbook.sso;

import java.io.*;
import java.net.*;
import javax.security.auth.Subject;
import org.ietf.jgss.*;

public class AuthServer {

    public AuthServer(Subject subject, String serverPrincipalName) {
        this.subject = subject;
        this.serverPrincipalName = serverPrincipalName;
    }

    private Subject subject;
    private String serverPrincipalName;
    
    public static void main(String[] argv) throws Exception {
        String serverPrincipalName = "login/java";
        String serverPassword = "myPassword";

        System.out.println("Config");
        Authentication.setKerberosSystemProperties();

        Subject subject =
            Authentication.jaasLogin(
                "ServerContext",
                serverPrincipalName,
                serverPassword);
        System.out.println("Logged in " + subject);

        System.out.println("Setting up GSSContexts");
        GSSContext serverContext =
            GSSContextUtil.createIncomingGSSContext(
                subject,
                serverPrincipalName,
                serverPrincipalName);

        System.out.println("Server: " + serverContext);

        System.out.println("Waiting for client connection");
        ServerSocket serverSocket = new ServerSocket(3000);
        Socket socket = serverSocket.accept();

        System.out.println("Connection established");
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();

        try {
            System.out.println("Establishing client context");
            while (!serverContext.isEstablished()) {
                System.out.println("Exchanging tokens");
                serverContext.acceptSecContext(input, output);
                output.flush();
            }
            System.out.println("Client context established !");

            String clientName = serverContext.getTargName().toString();
            String serverName = serverContext.getSrcName().toString();

            System.out.println(
                "Context established by " + serverName + " to " + clientName);

            // Test to see if the client has been authenticated - this
            // should always pass at this point !
            if (serverContext.isEstablished()) {
                System.out.println("OK: Client is authenticated");
            } else {
                System.out.println("ERROR: Client is NOT authenticated");
            }

        } catch (GSSException e) {
            System.out.println("ERROR: Client is NOT authenticated");

            System.out.println("MajorString:" + e.getMajorString());
            System.out.println("MinorString:" + e.getMinorString());
            e.printStackTrace();
        }

        // Close socket.
        socket.close();
    }
}
