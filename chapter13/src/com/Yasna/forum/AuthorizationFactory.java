/**
 * Copyright (C) 2001 Yasna.com. All rights reserved.
 *
 * ===================================================================
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        Yasna.com (http://www.yasna.com)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Yazd" and "Yasna.com" must not be used to
 *    endorse or promote products derived from this software without
 *    prior written permission. For written permission, please
 *    contact yazd@yasna.com.
 *
 * 5. Products derived from this software may not be called "Yazd",
 *    nor may "Yazd" appear in their name, without prior written
 *    permission of Yasna.com.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL YASNA.COM OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of Yasna.com. For more information
 * on Yasna.com, please see <http://www.yasna.com>.
 */

/**
 * Copyright (C) 2000 CoolServlets.com. All rights reserved.
 *
 * ===================================================================
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        CoolServlets.com (http://www.coolservlets.com)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Jive" and "CoolServlets.com" must not be used to
 *    endorse or promote products derived from this software without
 *    prior written permission. For written permission, please
 *    contact webmaster@coolservlets.com.
 *
 * 5. Products derived from this software may not be called "Jive",
 *    nor may "Jive" appear in their name, without prior written
 *    permission of CoolServlets.com.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL COOLSERVLETS.COM OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of CoolServlets.com. For more information
 * on CoolServlets.com, please see <http://www.coolservlets.com>.
 */

package com.Yasna.forum;

/**
 * An abstract class that defines a framework for providing authorization
 * services in Yazd. The static getAuthorization(String,String) and
 * getAnonymousAuthorization() methods should be called directly from
 * applications using Yazd in order to obtain Authorization tokens.<p>
 *
 * Users of Yazd that wish to change the AuthorizationFactory implementation
 * used to generate tokens can set the <code>AuthorizationFactory.className</code>
 * Yazd property. For example, if you have altered Yazd to use LDAP for user
 * information, you'd want to write a custom implementation of
 * AuthorizationFactory to make LDAP authorization queries. After changing the
 * <code>AuthorizationFactory.className</code> Yazd property, you must restart
 * your application server.
 */
public abstract class AuthorizationFactory {

    /**
     * The default class to instantiate is database implementation.
     */
    private static String className =
        "com.Yasna.forum.database.DbAuthorizationFactory";

    private static AuthorizationFactory factory = null;

    /**
     * Returns the Authorization token associated with the specified username
     * and password. If the username and password do not match the record of
     * any user in the system, the method throws an UnauthorizedException.<p>
     *
     * When using most implementations of this class, authorization tokens
     * should be cached. A convenient place to store a token is often in the
     * HttpSession.
     *
     * @param username the username to create an Authorization with.
     * @param password the password to create an Authorization with.
     * @return an Authorization token if the username and password are correct.
     * @throws UnauthorizedException if the username and password do not match
     *      any existing user.
     */
    public static Authorization getAuthorization(String username,
            String password) throws UnauthorizedException
    {
        loadAuthorizationFactory();
        return factory.createAuthorization(username, password);
    }

    /**
     * Returns the anonymous user Authorization.
     *
     * @return an anonymous Authorization token.
     */
    public static Authorization getAnonymousAuthorization() {
        loadAuthorizationFactory();
        return factory.createAnonymousAuthorization();
    }

    /**
     * Creates Authorization tokens for users. This method is implemented by
     * concrete subclasses of AuthorizationFactory.
     *
     * @param username the username to create an Authorization with.
     * @param password the password to create an Authorization with.
     * @return an Authorization token if the username and password are correct.
     * @throws UnauthorizedException if the username and password do not match
     *      any existing user.
     */
    public abstract Authorization createAuthorization(String username,
            String password) throws UnauthorizedException;

    /**
     * Creates anonymous Authorization tokens. This method is implemented by
     * concrete subclasses AuthorizationFactory.
     *
     * @return an anonymous Authorization token.
     */
    public abstract Authorization createAnonymousAuthorization();

    /**
     * Loads a concrete AuthorizationFactory that can be used generate
     * Authorization tokens for authorized users.<p>
     *
     * By default, the implementation used will be an instance of
     * DbAuthorizationFactory -- the standard database implementation that uses
     * the Yazd user table. A different factory can be specified by setting the
     * Yazd property "AuthorizationFactory.className". However, you must
     * restart Yazd for any change to take effect.
     */
    private static void loadAuthorizationFactory() {
        if (factory == null) {
            //Use className as a convenient object to get a lock on.
            synchronized(className) {
                if (factory == null) {
                    //See if the classname has been set as a Yazd property.
                    String classNameProp = PropertyManager.getProperty(
                        "AuthorizationFactory.className"
                    );
                    if (classNameProp != null) {
                        className = classNameProp;
                    }
                    try {
                        Class c = Class.forName(className);
                        factory = (AuthorizationFactory)c.newInstance();
                    }
                    catch (Exception e) {
                        System.err.println("Exception loading class: " + e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
