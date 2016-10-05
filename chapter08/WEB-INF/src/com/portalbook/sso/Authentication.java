package com.portalbook.sso;

import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;

public class Authentication {

    public static class KerberosCallbackHandler implements CallbackHandler {
        public KerberosCallbackHandler(String principal, String password) {
            this.principal = principal;
            this.password = password;
        }

        public void handle(Callback[] callbacks) {
            for (int i = 0; i < callbacks.length; i++) {
                Callback callback = callbacks[i];
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(principal);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(
                        password.toCharArray());
                }
            }
        }

        private String principal;
        private String password;
    }

    public static void setKerberosSystemProperties() {
        System.setProperty(
            "java.security.auth.login.config",
            "./Kerberos.config");

        System.setProperty("java.security.krb5.realm", "JETSPEED");
        System.setProperty("java.security.krb5.kdc", "jetspeed");
        System.setProperty("sun.security.krb5.debug", "false");
    }

    public static Subject jaasLogin(
        String context,
        String principalName,
        String password)
        throws LoginException {
        System.out.println("Acquiring context");
        LoginContext lc =
            new LoginContext(
                context,
                new KerberosCallbackHandler(principalName, password));

        System.out.println("Logging in...");
        try {
            lc.login();
            System.out.println("OK, Logged in.");

            System.out.println(
                "Retrieving and returning the Subject from the login context.");
            return lc.getSubject();
        } catch (Exception e) {
            throw new LoginException("Login failed: " + e);
        }
    }
    
    public static void main(String[] argv) {
        String principalName = "dave/staff";
        String password = "Coriolanus";

        setKerberosSystemProperties();

        System.out.println("Attempting jaasLogin");
        try {
            Subject subject =
                jaasLogin("ClientContext", principalName, password);
            System.out.println("Logged in " + subject);
        } catch (Exception e) {
            System.out.println("JaasLogin Failed: " + e);
            e.printStackTrace();
        }
        System.out.println("OK, Done.");
    }
}
