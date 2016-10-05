package com.portalbook.sso;

import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;
import org.ietf.jgss.*;

public class GSSContextUtil {
    public static GSSContext createIncomingGSSContext(
        Subject subject,
        String credentialPrincipal,
        String contextPrincipal)
        throws Exception {
        GSSContext context =
            (GSSContext) Subject.doAs(
                subject,
                new CreateGSSContextPrivilegedAction(
                    credentialPrincipal,
                    contextPrincipal,
                    GSSCredential.ACCEPT_ONLY));
        return context;
    }

    public static GSSContext createOutgoingGSSContext(
        Subject subject,
        String credentialPrincipal,
        String contextPrincipal)
        throws Exception {
        GSSContext context =
            (GSSContext) Subject.doAs(
                subject,
                new CreateGSSContextPrivilegedAction(
                    credentialPrincipal,
                    contextPrincipal,
                    GSSCredential.INITIATE_ONLY));
        return context;
    }

    public static class CreateGSSContextPrivilegedAction
        implements PrivilegedExceptionAction {

        public CreateGSSContextPrivilegedAction(
            String clientPrincipal,
            String serverPrincipal,
            int usage) {
            this.credentialPrincipal = clientPrincipal;
            this.contextPrincipal = serverPrincipal;
            this.usage = usage;
        }

        public Object run() throws Exception {
            GSSManager manager = GSSManager.getInstance();

            // Declare OID for Kerberos mechanisms
            Oid krb5Mechanism = new Oid("1.2.840.113554.1.2.2");

            // Identify who the client wishes to be
            GSSName clientName =
                manager.createName(credentialPrincipal, GSSName.NT_USER_NAME);

            // Identify the name of the server.
            GSSName serverName =
                manager.createName(contextPrincipal, GSSName.NT_USER_NAME);

            // Acquire credentials for the user
            GSSCredential userCreds =
                manager.createCredential(
                    clientName,
                    GSSCredential.DEFAULT_LIFETIME,
                    krb5Mechanism,
                    usage);

            // Instantiate and initialize a security context 
            // that will be established with the server
            GSSContext clientContext =
                manager.createContext(
                    serverName,
                    krb5Mechanism,
                    userCreds,
                    GSSContext.DEFAULT_LIFETIME);

            return clientContext;
        }

        private String credentialPrincipal;
        private String contextPrincipal;
        private int usage;
    }

    // This utility class should not be instantiated
    private GSSContextUtil() {
    }
}
