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

package com.Yasna.forum.util;

import javax.servlet.http.HttpServletRequest;

/**
 *  This class assists skin writers in getting parameters.
 */
public class ParamUtils {

    /**
     *  Gets a parameter as a string.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param paramName The name of the parameter you want to get
     *  @return The value of the parameter or null if the parameter was not
     *  found or if the parameter is a zero-length string.
     */
        public static String getParameter( HttpServletRequest request, String paramName ) {
                return getParameter( request, paramName, false );
        }

    /**
     *  Gets a parameter as a string.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param paramName The name of the parameter you want to get
     *  @param emptyStringsOK Return the parameter values even if it is an empty string.
     *  @return The value of the parameter or null if the parameter was not
     *  found.
     */
        public static String getParameter( HttpServletRequest request, String paramName, boolean emptyStringsOK ) {
                String temp = request.getParameter(paramName);
        if( temp != null ) {
            if( temp.equals("") && !emptyStringsOK ) {
                return null;
            }
            else {
                return temp;
            }
        }
        else {
            return null;
        }
    }

    /**
     *  Gets a parameter as a boolean.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param paramName The name of the parameter you want to get
     *  @return True if the value of the parameter was "true", false otherwise.
     */
        public static boolean getBooleanParameter( HttpServletRequest request, String paramName ) {
                String temp = request.getParameter(paramName);
                if( temp != null && temp.equals("true") ) {
                        return true;
                } else {
                        return false;
                }
        }

    /**
     *  Gets a parameter as a int.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param paramName The name of the parameter you want to get
     *  @return The int value of the parameter specified or the default value if
     *  the parameter is not found.
     */
        public static int getIntParameter( HttpServletRequest request, String paramName, int defaultNum ) {
                String temp = request.getParameter(paramName);
                if( temp != null && !temp.equals("") ) {
            int num = defaultNum;
            try {
                num = Integer.parseInt(temp);
            }
            catch( Exception ignored ) {}
                        return num;
                } else {
                        return defaultNum;
                }
        }

    /**
     *  Gets a checkbox parameter value as a boolean.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param paramName The name of the parameter you want to get
     *  @return True if the value of the checkbox is "on", false otherwise.
     */
    public static boolean getCheckboxParameter( HttpServletRequest request, String paramName ) {
        String temp = request.getParameter(paramName);
        if( temp != null && temp.equals("on") ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Gets a parameter as a string.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param attribName The name of the parameter you want to get
     *  @return The value of the parameter or null if the parameter was not
     *  found or if the parameter is a zero-length string.
     */
        public static String getAttribute( HttpServletRequest request, String attribName ) {
                return getAttribute( request, attribName, false );
        }

    /**
     *  Gets a parameter as a string.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param attribName The name of the parameter you want to get
     *  @param emptyStringsOK Return the parameter values even if it is an empty string.
     *  @return The value of the parameter or null if the parameter was not
     *  found.
     */
        public static String getAttribute( HttpServletRequest request, String attribName, boolean emptyStringsOK ) {
                String temp = (String)request.getAttribute(attribName);
        if( temp != null ) {
            if( temp.equals("") && !emptyStringsOK ) {
                return null;
            }
            else {
                return temp;
            }
        }
        else {
            return null;
        }
    }

    /**
     *  Gets an attribute as a boolean.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param attribName The name of the attribute you want to get
     *  @return True if the value of the attribute is "true", false otherwise.
     */
        public static boolean getBooleanAttribute( HttpServletRequest request, String attribName ) {
                String temp = (String)request.getAttribute(attribName);
                if( temp != null && temp.equals("true") ) {
                        return true;
                } else {
                        return false;
                }
        }

    /**
     *  Gets an attribute as a int.
     *  @param request The HttpServletRequest object, known as "request" in a
     *  JSP page.
     *  @param attribName The name of the attribute you want to get
     *  @return The int value of the attribute or the default value if the attribute is not
     *  found or is a zero length string.
     */
        public static int getIntAttribute( HttpServletRequest request, String attribName, int defaultNum ) {
                String temp = (String)request.getAttribute(attribName);
                if( temp != null && !temp.equals("") ) {
            int num = defaultNum;
            try {
                num = Integer.parseInt(temp);
            }
            catch( Exception ignored ) {}
                        return num;
                } else {
                        return defaultNum;
                }
        }

}
