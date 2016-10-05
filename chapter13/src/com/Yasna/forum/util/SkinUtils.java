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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.AuthorizationFactory;
import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumFactory;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumPermissions;
import com.Yasna.forum.Group;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.UserNotFoundException;
import com.Yasna.util.StringUtils;

/**
 * A collection of utility methods for use in Yazd WebSkins. Because these
 * methods make skin development much easier, skin authors should study them
 * carefully.<p>
 *
 * Three major areas of funtionality are provided:<p><ol>
 *      <li> Methods that simplify Authorization tasks:
 *          <ul>
 *              <li>{@link #getUserAuthorization(HttpServletRequest, HttpServletResponse)}
 *              <li>{@link #getUserAuthorization(HttpServletRequest, HttpServletResponse, boolean)}
 *              <li>{@link #setUserAuthorization(HttpServletRequest, HttpServletResponse, String, String, boolean)}
 *              <li>{@link #removeUserAuthorization(HttpServletRequest, HttpServletResponse)}
 *              <li>{@link #isSystemAdmin(Authorization)}
 *              <li>{@link #isForumAdmin(Authorization)}
 *              <li>{@link #isForumAdmin(Authorization, Forum)}
 *              <li>{@link #isGroupAdmin(Authorization)}
 *              <li>{@link #isGroupAdmin(Authorization, Group)}
 *          </ul>
 *          <p>
 *      <li> Methods that get and set Session and cookie values.
 *          <ul>
 *              <li>{@link #getCookie(HttpServletRequest, String)}
 *              <li>{@link #getCookieValue(HttpServletRequest, String)}
 *              <li>{@link #invalidateCookie(HttpServletRequest, HttpServletResponse, String)}
 *              <li>{@link #remove(HttpServletRequest, HttpServletResponse, String)}
 *              <li>{@link #retrieve(HttpServletRequest, HttpServletResponse, String)}
 *              <li>{@link #retrieve(HttpServletRequest, HttpServletResponse, String, boolean)}
 *              <li>{@link #store(HttpServletRequest, HttpServletResponse, String, String)}
 *              <li>{@link #store(HttpServletRequest, HttpServletResponse, String, String, int)}
 *              <li>{@link #store(HttpServletRequest, HttpServletResponse, String, String, int boolean)}
 *          </ul>
 *          <p>
 *      <li> Other methods.
 *          <ul>
 *              <li>{@link #dateToText(Date)}
 *              <li>(@link #getLastVisisted(HttpServletRequest, HttpServletResponse)}
 *              <li>(@link #getLastVisisted(HttpServletRequest, HttpServletResponse, boolean)}
 *              <li>{@link #isNewMessage(ForumMessage, long)}
 *              <li>(@link #quoteOriginal(ForumMessage, String, int)}
 *          </ul>
 * </ol>
 *
 * All methods conform to the Servlet 1.1 and JSP 1.0 specs for maximum
 * compatibility with application servers. This may yield deprecation warnings
 * if you compile with a newer Servlet/JSP spec; these should be ignored. This
 * class will periodically be updated to the newer specs as app servers mature.
 */
public class SkinUtils {

    /** Name of the authentication token (is stored in the user's session) */
    public static final String YAZD_AUTH_TOKEN = "yazdAuthorization";

    /** Name of the cookie used to store user info for auto-login purposes */
    public static final String YAZD_AUTOLOGIN_COOKIE = "yazdAutoLogin";

    /** Name of the last visited token (is stored in the user's session) */
    public static final String YAZD_LASTVISITED_TOKEN = "yazdLastVisited";

    /** Name of the cookie used to store last visited timestamp */
    public static final String YAZD_LASTVISITED_COOKIE = "yazdLastVisited";

    // XXX keep this ?
    /** Name of the "use last visited" property (is stored in yazd.properties) */
    public static final String YAZD_LASTVISITED_PROP = "Site.useLastVisited";

    //Time constants (in milliseconds)
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR   = 60 * MINUTE;
    private static final long DAY    = 24 * HOUR;
    private static final long WEEK   = 7 * DAY;

    //Default cookie time to live (in seconds).
    private static final int MAX_COOKIE_AGE = (int)(WEEK / 1000) * 8;

    //Days of the week
    private static final String[] DAYS_OF_WEEK =
        { "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday" };

    // SimpleDateFormat objects for use in the dateToText method
    private static final SimpleDateFormat dateFormatter =
        new SimpleDateFormat("EEEE, MMM d 'at' h:mm a");
    private static final SimpleDateFormat yesterdayFormatter =
        new SimpleDateFormat("'Yesterday at' h:mm a");

    //"Tweakable" parameters for the cookie encoding. NOTE: changing these
    //and recompiling this class will essentially invalidate old cookies.
    private final static int    ENCODE_XORMASK = 0x5A;
    private final static char	ENCODE_DELIMETER = '\002';
    private final static char	ENCODE_CHAR_OFFSET1 = 'A';
    private final static char	ENCODE_CHAR_OFFSET2 = 'h';

    /**
     * Returns an Authorization token for the user. The following steps are
     * performed to determine the token:<ol>
     *
     * <li>Check the session for the existence of a Yazd authorization token.
     *     If one is found, it is returned as we assume that the user has logged
     *     in and is authorized.
     * <li>Check the Yazd authorization cookie for a username and password. If found,
     *     attempt to create a Yazd authorization token using that data. If
     *     successful, save the token to the session and return it.
     *     NOTE: This check can be skipped by setting
     *     <code>checkYazdCookie</code> to false.
     * </ol><p>
     *
     * @param request the HttpServletRequest object, known as "request" in a
     *      JSP page.
     * @param response the HttpServletResponse object, known as "response" in
     *      a JSP page.
     * @param checkYazdCookie a boolean that indicates whether or not we want
     *      to use a cookie for authorization.
     * @return the authorization token if authenticated, otherwise
     *      <code>null</code>.
     * @see Authorization
     */
    public static Authorization getUserAuthorization(HttpServletRequest request,
            HttpServletResponse response, boolean checkYazdCookie)
    {
        // we can get the session object from the request object:
        HttpSession session = request.getSession();

        // Check 1: check for the yazd authentication token in the user's session.
        Authorization authToken = (Authorization)session.getAttribute(YAZD_AUTH_TOKEN);
        if (authToken != null) {
            return authToken;
        }

        // Check 2: check the yazd cookie for username and password, if we're allowing that
        if( checkYazdCookie ) {
            Cookie cookie = getCookie(request, YAZD_AUTOLOGIN_COOKIE);
            try {
                if( cookie != null ) {
                    // at this point, we found a cookie so grab the username & password
                    // from it, create an authorization token and store that in the session
                    String[] values = decodePasswordCookie(cookie.getValue());
                    String username = values[0];
                    String password = values[1];
                    // try to validate the user based on the info from the cookie
                    authToken = AuthorizationFactory.getAuthorization(username,password);

                    // put that token in the user's session:
                    session.setAttribute( YAZD_AUTH_TOKEN, authToken );

                    // return the authorization token
                    return authToken;
                }
            }
            catch( Exception e ) {
                //We want any exceptions in this block to be caught so that an
                //anonymous authorization token can be returned. The
                //getAuthorzation(username,password) method above throws an
                //UnauthorizedException. In the case of this exception or others,
                //the cookie holds invalid login info, so we should remove it:
                cookie = new Cookie(YAZD_AUTOLOGIN_COOKIE,null);
                cookie.setMaxAge(0); // zero value causes cookie to be deleted
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        //Got this far, so return null.
        return null;
    }

    /**
     * Returns an Authorization token for the user. This is a convenience method
     * that that calls the other getUserAuthorization method with
     * <code>checkYazdCookie</code set to true.
     *
     * @param request the HttpServletRequest object, known as "request" in a
     *      JSP page.
     * @param response The HttpServletResponse object, known as "response" in
     *      a JSP page.
     * @return The authorization token if authenticated, otherwise
     *      <code>null</code>.
     * @see SkinUtils#getUserAuthorization(HttpServletRequest,HttpServletResponse,boolean)
     */
    public static Authorization getUserAuthorization
            ( HttpServletRequest request, HttpServletResponse response )
    {
        return getUserAuthorization(request, response, true);
    }

    /**
     * Validates the user and optionally enables auto-login by creating an
     * auto-login cookie.
     *
     * @param request the HttpServletRequest object, known as "request" in a JSP page.
     * @param response the HttpServletResponse object, known as "response" in a JSP page.
     * @param username the username.
     * @param password the password.
     * @param autoLogin if <code>true</code> create a cookie that enables auto-login.
     * @throws UserNotFoundException
     * @throws UnauthorizedException
     * @return The authorization token if authenticated, otherwise
     *      <code>null</code>
     */
    public static Authorization setUserAuthorization(HttpServletRequest request,
            HttpServletResponse response, String username, String password,
            boolean autoLogin) throws UserNotFoundException, UnauthorizedException
    {
        HttpSession session = request.getSession();
        Authorization authToken = AuthorizationFactory.getAuthorization(username, password);
        session.setAttribute(YAZD_AUTH_TOKEN, authToken);

        if (autoLogin) {
            Cookie cookie = new Cookie(YAZD_AUTOLOGIN_COOKIE, encodePasswordCookie(username, password));
            cookie.setMaxAge(MAX_COOKIE_AGE);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return authToken;
    }

    /**
     *  Invalidates the cookie that otherwise lets a user auto-login.
     *
     *  @param request The HttpServletRequest object, known as "request" in a JSP page.
     *  @param response The HttpServletResponse object, known as "response" in a JSP page.
     */
    public static void removeUserAuthorization( HttpServletRequest request, HttpServletResponse response )
    {
        HttpSession session = request.getSession();
        session.removeAttribute(YAZD_AUTH_TOKEN);
        Cookie cookie = new Cookie(YAZD_AUTOLOGIN_COOKIE, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * Invalidate the specified cookie and delete it from the response object.
     *
     * @param request The HttpServletRequest object, known as "request" in a JSP page.
     * @param response The HttpServletResponse object, known as "response" in a JSP page.
     * @param cookieName The name of the cookie you want to delete.
     */
    public static void invalidateCookie( HttpServletRequest request, HttpServletResponse response, String cookieName ) {
        Cookie cookie = new Cookie( cookieName, null ); // invalidate cookie
        cookie.setMaxAge(0); // deletes cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * Persists a value for the length of the user's session.
     *
     * @see SkinUtils#store(HttpServletRequest,HttpServletResponse,String,String,int) store
     */
    public static void store( HttpServletRequest request, HttpServletResponse response,
            String id, String value )
    {
        store( request,response,id,value,0,false );
    }

    /**
     * Persists a value for the time (in seconds) specified
     *
     * @see SkinUtils#store(HttpServletRequest,HttpServletResponse,String,String,int) store
     */
    public static void store( HttpServletRequest request, HttpServletResponse response,
            String id, String value, int secsToLive )
    {
        store( request,response,id,value,secsToLive,false );
    }

    /**
     *  This method should be used in a jsp skin to store an arbritary value.
     *  For example, we could persist the name of a user so that on a form page
     *  where they enter their name, that field could be auto-filled in with
     *  the stored value.
     *  <p>
     *  To indicate that the data should only be persisted for a session, pass
     *  in 0 as the <code>timeToLive</code>.
     *
     *  @param request The HttpServletRequest object, known as "request" on a JSP page.
     *  @param response The HttpServletRequest object, known as "response" on a JSP page.
     *  @param id The name or identifier of the data you want to persist.
     *  @param value The value you wish to store.
     *  @param secsToLive The length (in seconds) this value will persist. Any value of 0 or
     *  less indicates this data should only persist for a session.
     */
    public static void store( HttpServletRequest request, HttpServletResponse response,
            String id, String value, int secsToLive, boolean restoreInSession )
    {
        // This method uses sessions and cookies to persist data. We always store
        // it in the user's session. We'll only set it in a cookie if the
        // 'timeToLive' parameter is > 0.

        // If the id is null, return
        if( id == null ) {
            return;
        }

        // Get the session object:
        HttpSession session = request.getSession();

        // check to see if the value already exists in the session -- if it does,
        // don't restore it unless specified
        if( ((String)session.getAttribute(id)) != null && !restoreInSession ) {
            return;
        }

        // At this point, restore (or store for the first time) the value in the session
        // Servlet API 2.1 call. Used to preserve compatibility with older app
        // servers. You might get deprecation warnings.
        session.setAttribute(id,value);

        // if the timeToLive param is > 0, store to the cookie:
        if( secsToLive > 0 ) {
            Cookie cookie = new Cookie(id,value);
            cookie.setMaxAge(secsToLive);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    /**
     * Retrieves a user stored value. Values are set using the <code>store(...)</code>
     * methods.
     *
     * @param request The HttpServletRequest object, known as "request" on a JSP page.
     * @param response The HttpServletRequest object, known as "response" on a JSP page.
     * @param id The id or name of the stored value.
     * @return The value of the specified id, otherwise <code>null</code>.
     */
    public static String retrieve( HttpServletRequest request, HttpServletResponse response, String id ) {
        // just retrieve the value, don't remove it from persistence
        return( retrieve( request,response,id,false ) );
    }

    /**
     * Retrieves a user stored value. Values are set using the <code>store(...)</code>
     * methods. If <code>remove</code> is true, the value is also removed
     * from persistence.
     *
     * @param request The HttpServletRequest object, known as "request" on a JSP page.
     * @param response The HttpServletRequest object, known as "response" on a JSP page.
     * @param id The id or name of the stored value.
     * @return The value of the specified id, otherwise <code>null</code>.
     */
    public static String retrieve( HttpServletRequest request,
            HttpServletResponse response, String id, boolean remove )
    {
        // First, check the session.
        HttpSession session = request.getSession();
        String value = (String)session.getAttribute(id);

        // if it's not found, check the cookies
        if( value == null ) {
            value = getCookieValue(request,id);
        }

        // remove it from persistence if indicated
        if( remove ) {
            remove( request,response,id );
        }

        return value;
    }

    /**
     * Removes a user stored value. Values are set using the <code>store(...)</code>
     * methods.
     *
     * @param request the HttpServletRequest object, known as "request" on a JSP page.
     * @param response the HttpServletRequest object, known as "response" on a JSP page.
     * @param id the id or name of the stored value you wish to remove from persistence.
     */
    public static void remove( HttpServletRequest request, HttpServletResponse response, String id ) {
        // First, remove it from the session:
        HttpSession session = request.getSession();
        session.removeAttribute(id);

        // Invalidate the cookie by setting a null expired cookie in its place
        Cookie cookie = new Cookie( id, null );
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * Returns the time in milliseconds that the user last visited Yazd.
     *
     * @param request the HttpServletRequest object, known as "request" on a JSP page.
     * @param response the HttpServletRequest object, known as "response" on a JSP page.
     * @see SkinUtils#getLastVisited(HttpServletRequest,HttpServletResponse,boolean) getLastVisited
     * @return The time (in milliseconds) that the suer last visited Yazd.
     */
    public static long getLastVisited(HttpServletRequest request,
            HttpServletResponse response)
    {
        return getLastVisited(request,response,true);
    }

    /**
     * Returns the time in milliseconds that the user last visited the Yazd system.
     *
     * @param request the HttpServletRequest object, known as "request" on a JSP page.
     * @param response the HttpServletRequest object, known as "response" on a JSP page.
     * @param updateLastVisitedTime Set to <code>true</code> if you wish to update
     * the user's last visited time to the current time; set to <code>false</code> otherwise.
     * @return The time (in milliseconds) that the suer last visited Yazd.
     */
    public static long getLastVisited(HttpServletRequest request,
            HttpServletResponse response, boolean updateLastVisitedTime)
    {
        //Get session object
        HttpSession session = request.getSession();

        //The current instant in time.
        long now = System.currentTimeMillis();

        //First, try to retrieve the value from the session
        String lastTime = (String)session.getAttribute(YAZD_LASTVISITED_TOKEN);

        //Found a value in the session, so return it
        if(lastTime != null) {
            try {
                long time = Long.parseLong(lastTime);
                // update the last visited time to now, but don't update the
                // last visited time in the session:
                Cookie cookie = new Cookie(YAZD_LASTVISITED_TOKEN, Long.toString(now));
                cookie.setMaxAge(60*60*24*30);
                cookie.setPath("/");
                response.addCookie(cookie);
                // return the time value
                return time;
            }
            catch(NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // getting to this point means no time value was found in the session,
        // so look for it in the cookie:
        long time = now;
        lastTime = getCookieValue(request,YAZD_LASTVISITED_TOKEN);
        if( lastTime != null ) {
            try {
                time = Long.parseLong(lastTime);
            } catch( NumberFormatException e ) {}
        }

         // set the value in the cookie, return the time
        session.setAttribute(YAZD_LASTVISITED_TOKEN, Long.toString(time));
        Cookie cookie = new Cookie(YAZD_LASTVISITED_TOKEN, Long.toString(now));
        cookie.setMaxAge(60*60*24*30);
        cookie.setPath("/");
        response.addCookie(cookie);

        return time;
    }

    /**
     * Returns true if the message has been created or updated since
     * the last time the user visisted.
     *
     * @param message the message to check.
     * @param lastVisted the time the user last visisted the forum.
     * @return true if the message has been created or updated since the user's
     *      last visit.
     */
    public static boolean isNewMessage(ForumMessage message, long lastVisited)
    {
        if (message.getModifiedDate().getTime() > lastVisited) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the specified Cookie object, or null if the cookie does not exist.
     *
     * @param request The HttpServletRequest object, known as "request" in a
     *      JSP page.
     * @param name the name of the cookie.
     * @return the Cookie object if it exists, otherwise null.
     */
    public static Cookie getCookie( HttpServletRequest request, String name ) {
        Cookie cookies[] = request.getCookies();
        if(cookies == null || name == null || name.length() == 0) {
            return null;
        }
        //Otherwise, we have to do a linear scan for the cookie.
        for( int i = 0; i < cookies.length; i++ ) {
            if(cookies[i].getName().equals(name) ) {
                return cookies[i];
            }
        }
        return null;
    }

    /**
     * Returns the value of the specified cookie as a String. If the cookie
     * does not exist, the method returns null.
     *
     * @param request the HttpServletRequest object, known as "request" in a
     *      JSP page.
     * @param name the name of the cookie
     * @return the value of the cookie, or null if the cookie does not exist.
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request,name);
        if(cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * Formats the unfiltered body of a message to make it appear in the "quote
     * original" format. This is simply the body of the message with the
     * delimiter appended to the beginning of each line. The delimiter
     * is most often "> " by convention. A desired length for each line in the
     * returned String can be specified to aid in formatting.<p>
     *
     * This method uses message.getUnfilteredBody() in order to get the body of
     * the message. This usually yields better results for the formatting
     * required by this method. However, it also has the potential of being
     * a security risk if malicious HTML code is embedded in the body. Therefore,
     * you should always filter HTML from the result of this method before
     * showing it in an environment where HTML is interpreted. If you are
     * showing the results of this method in an HTML &lt;textarea&gt;, there is
     * no need to worry about malicious HTML.
     *
     * @param message the message to quote.
     * @param delimiter a String that will start each line of the quoted
     *      message. For example, "> ";
     * @param lineLength the desired length of each line in the quoted message.
     * @return the unfiltered body of the message in the "quote original" format.
     */
    public static String quoteOriginal(String body, String delimiter,
            int lineLength)
    {
        if (body == null || body.length() == 0) {
            return "";
        }
        int length = body.length();
        //Create a StringBuffer to hold the quoted body; approximate size.
        StringBuffer buf = new StringBuffer(body.length());
        //i maintains the current position in the String.
        for (int i=0; i<length; ) {
            String partialString =
                StringUtils.chopAtWord(
                    body.substring(i),
                    lineLength
                );
            //System.out.println("--" + partialString);
            i += partialString.length()+1;
            buf.append(delimiter).append(partialString.trim()).append("\\n");
        }
        return buf.toString();
    }

    /**
     * Returns a String describing the amount of time between now (current
     * system time) and the passed in date time. Example output is "5 hours
     * ago" or "Yesterday at 3:30 pm"
     *
     * @param date the Date to compare the current time with.
     * @return a description of the difference in time, ie: "5 hours ago"
     *      or "Yesterday at 3:30pm"
     */
    public static String dateToText( Date date ) {
        if( date == null ) {
            return "";
        }

        long delta = System.currentTimeMillis() - date.getTime();

        // within the last hour
        if( (delta / HOUR) < 1 ) {
            long minutes = (delta/MINUTE);
            if( minutes == 0 ) {
                return "Less than 1 min ago";
            }
            else if( minutes == 1 ) {
                return "1 minute ago";
            }
            else {
                return ( minutes + " minutes ago" );
            }
        }

        // sometime today
        if( (delta / DAY) < 1 ) {
            long hours = (delta/HOUR);
            if( hours <= 1 ) {
                return "1 hour ago";
            }
            else {
                return ( hours + " hours ago" );
            }
        }

        // within the last week
        if( (delta / WEEK) < 1 ) {
            double days = ((double)delta/(double)DAY);
            if( days <= 1.0 ) {
                return yesterdayFormatter.format(date);
            }
            else {
                return dateFormatter.format(date);
            }
        }

        // before a week ago
        else {
            return dateFormatter.format(date);
        }
    }

    /**
     * Returns true if the user is a system administrator.
     *
     * @param authToken the authentication token of the user
     * @return true if the user is a system administrator, false otherwise.
     */
    public static boolean isSystemAdmin( Authorization authToken ) {
        ForumFactory forumFactory = ForumFactory.getInstance(authToken);
        ForumPermissions permissions = forumFactory.getPermissions(authToken);
        return permissions.get(ForumPermissions.SYSTEM_ADMIN);
    }

    /**
     * Returns true if the user is a forum adminstrator of any forum in the
     * system. For example, if there are 3 forums in the system and the user
     * is an adminstrator of any one or more of them, this method will return
     * true.<p>
     *
     * Use the method <code>isForumAdmin( Authorization, Forum)</code> to
     * check an individual forum for administrator status.)
     *
     * @param authToken the authentication token of the user
     * @return true if the user is a forum administrator of any forum in the system.
     * @see SkinUtils#isForumAdmin(Authorization, Forum)
     */
    public static boolean isForumAdmin( Authorization authToken ) {
        ForumFactory forumFactory = ForumFactory.getInstance(authToken);
        Iterator forumIterator = forumFactory.forums();
        if( !forumIterator.hasNext() ) {
            return false;
        }
        while( forumIterator.hasNext() ) {
            Forum forum = (Forum)forumIterator.next();
            if( forum.hasPermission(ForumPermissions.FORUM_ADMIN) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the user is a forum moderator of any forum in the
     * system. For example, if there are 3 forums in the system and the user
     * is a moderator of any one or more of them, this method will return
     * true.<p>
     *
     * Use the method <code>isForumModerator( Authorization, Forum)</code> to
     * check an individual forum for moderator status.)
     *
     * @param authToken the authentication token of the user
     * @return true if the user is a forum moderator of any forum in the system.
     * @see SkinUtils#isForumModerator(Authorization, Forum)
     */
    public static boolean isForumModerator( Authorization authToken ) {
        ForumFactory forumFactory = ForumFactory.getInstance(authToken);
        Iterator forumIterator = forumFactory.forums();
        if( !forumIterator.hasNext() ) {
            return false;
        }
        while( forumIterator.hasNext() ) {
            Forum forum = (Forum)forumIterator.next();
            if( forum.hasPermission(ForumPermissions.MODERATOR) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the user is a forum adminstrator of the given forum.
     *
     * @param authToken the authentication token of the user
     * @param forum the forum to check administrator status on.
     * @return true if the user is a forum administrator of the given forum.
     */
    public static boolean isForumAdmin( Authorization authToken, Forum forum ) {
        return( forum.hasPermission(ForumPermissions.FORUM_ADMIN) );
    }
    /**
     * Returns true if the user is a forum moderator of the given forum.
     *
     * @param authToken the authentication token of the user
     * @param forum the forum to check moderator status on.
     * @return true if the user is a forum moderator of the given forum.
     */
    public static boolean isForumModerator( Authorization authToken, Forum forum ) {
        return( forum.hasPermission(ForumPermissions.MODERATOR) );
    }

    /**
     * Returns true if the user is a group administrator of any group in the
     * system. For example, if there are 3 groups in the system and the user
     * is an adminstrator of any one or more of them, this method will return
     * true.<p>
     *
     * Use the method <code>isGroupAdmin( Authorization, Group)</code> to check
     * an individual group for administrator status.)
     *
     * @see SkinUtils#isGroupAdmin(Authorization, Group)
     */
    public static boolean isGroupAdmin( Authorization authToken ) {
        ForumFactory forumFactory = ForumFactory.getInstance(authToken);
        ProfileManager manager = forumFactory.getProfileManager();
        Iterator groupIterator = manager.groups();
        if( !groupIterator.hasNext() ) {
            return false;
        }
        while( groupIterator.hasNext() ) {
            Group group = (Group)groupIterator.next();
            if( group.hasPermission(ForumPermissions.GROUP_ADMIN) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the user is a group administrator of the given group.
     *
     * @param authToken the authentication token of the user
     * @param group the group to check administrator status on.
     * @return true if the user is a group administrator of the given group.
     */
    public static boolean isGroupAdmin( Authorization authToken, Group group ) {
        return( group.hasPermission(ForumPermissions.GROUP_ADMIN) );
    }

    /**
     * Builds a cookie string containing a username and password.<p>
     *
     * Note: with open source this is not really secure, but it prevents users
     * from snooping the cookie file of others and by changing the XOR mask and
     * character offsets, you can easily tweak results.
     *
     * @param username The username.
     * @param password The password.
     * @return String encoding the input parameters, an empty string if one of
     *      the arguments equals <code>null</code>.
     */
    private static String encodePasswordCookie (String username, String password)
    {
        StringBuffer buf = new StringBuffer();
        if (username != null && password != null) {
            byte[] bytes = (username + ENCODE_DELIMETER + password).getBytes();
            int    b;

            for (int n = 0; n < bytes.length; n++) {
                b = bytes[n] ^ (ENCODE_XORMASK + n);
                buf.append((char)(ENCODE_CHAR_OFFSET1 + (b & 0x0F)));
                buf.append((char)(ENCODE_CHAR_OFFSET2 + ((b >> 4) & 0x0F)));
            }
        }
        return buf.toString();
    }

    /**
     * Unrafels a cookie string containing a username and password.
     * @param value The cookie value.
     * @return String[] containing the username at index 0 and the password at
     *      index 1, or <code>{ null, null }</code> if cookieVal equals
     *      <code>null</code> or the empty string.
     */
    private static String[] decodePasswordCookie( String cookieVal ) {

        // check that the cookie value isn't null or zero-length
        if( cookieVal == null || cookieVal.length() <= 0 ) {
            return null;
        }

        // unrafel the cookie value
        char[] chars = cookieVal.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int b;
        for (int n = 0, m = 0; n < bytes.length; n++) {
            b = chars[m++] - ENCODE_CHAR_OFFSET1;
            b |= (chars[m++] - ENCODE_CHAR_OFFSET2) << 4;
            bytes[n] = (byte)(b ^ (ENCODE_XORMASK + n));
        }
        cookieVal = new String(bytes);
        int	pos = cookieVal.indexOf(ENCODE_DELIMETER);
        String username = (pos < 0) ? "" : cookieVal.substring(0, pos);
        String password = (pos < 0) ? "" : cookieVal.substring(pos + 1);

        return new String[] { username, password };
    }

    /**
     *  test method for this class
     */
     /*
    public static void main( String args[] ) {
        Calendar cal = Calendar.getInstance();
        System.out.println( "now:\t" + dateToText(cal.getTime()) );
        for( int i=0; i<122; i++ ) {
            cal.setTime( new Date(cal.getTime().getTime() - (30*MINUTE) ) );
            System.out.println( (i+1) + " min ago:\t" + dateToText(cal.getTime()) + "\t" + cal.getTime() );
        }
    }
    */

}
