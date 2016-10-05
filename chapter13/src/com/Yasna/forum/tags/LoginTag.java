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

package com.Yasna.forum.tags;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.AuthorizationFactory;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>login</b>, used to login a Yazd user.
 * <p>
 * Uses the following HTTP input parameters
 * <p><ul>
 * <li><b>login</b> - indicates user is submitting the login form
 * <li><b>username</b> - Yazd user name (userid) to login
 * <li><b>password</b> - users password
 * </ul>
 * If login fails a user error is set, if login succeeds the body of the
 * login tag is included and the remainder of the page is skipped.
 * <p>
 * Initializes YazdState and YazdRequest if necessary.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;login&lt;/name&gt;
 *   &lt;tagclass&gt;com.Yasna.forum.tags.LoginTag&lt;/tagclass&gt;
 *   &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *   &lt;info&gt;Login user using username and password.  Includes body of tag if login succeeded.&lt;/info&gt;
 * </pre>
 *
 * @see YazdState
 * @see YazdRequest
 *
 * @see ErrorTag
 * @see ErrorLoopTag
 *
 * @author Glenn Nielsen
 */
public class LoginTag extends TagSupport
{
  private Authorization auth = null;
  private YazdState js = null;
  private YazdRequest jr = null;

  /**
   * Attempts a user login if this is an HTML form submission.
   * <p>
   * If login succeeds, reads in user data and initializes state information
   * <p>
   * Includes body of tag if user is already logged in, or a new login
   * is successful.
   *
   * @throws JspException on system level error
   *
   * @return <b>SKIP_BODY</b> if a login form was not submitted or login failed, <b>EVAL_BODY_INCLUDE</b> if login succeeded
   */
  public final int doStartTag() throws JspException
  {
    // Initialize YazdState
    js = (YazdState)pageContext.getAttribute("yazdUserState",
		PageContext.SESSION_SCOPE);
    if( js == null ) {
      js = new YazdState();
      pageContext.setAttribute("yazdUserState",js,PageContext.SESSION_SCOPE);
    }

    if( js.getLoggedIn() ) {
      return EVAL_BODY_INCLUDE;
    }

    // Initialize YazdRequest
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null ) {
      jr = new YazdRequest();
      jr.setYazdState(js);
      pageContext.setAttribute("yazdUserRequest",jr,PageContext.REQUEST_SCOPE);
    }

    // Get username and password
    ServletRequest req = pageContext.getRequest();
    String tmp = req.getParameter("login");
    if( tmp == null || tmp.length() == 0 )
      return SKIP_BODY;
    String u = req.getParameter("username");
    String p = req.getParameter("password");
    if( p.length() == 0 || u.length() == 0) {
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.login.failed"));
      return SKIP_BODY;
    }
    try {
      auth = AuthorizationFactory.getAuthorization( u, p );
    }
    catch( UnauthorizedException ue ) {
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.login.failed"));
      return SKIP_BODY;
    }

    // Set user message_depth, thread_depth, and items_per_page
    ProfileManager pm = jr.getProfileManager();
    User user = null;
    try {
      user = pm.getUser(auth.getUserID());
    } catch( UserNotFoundException ex ) {
      throw new JspException("Yazd login tag could not find user.");
    }

    // Get the users display preferences
    long val;
    tmp = user.getProperty(YazdState.MESSAGE_DEPTH);
    if( tmp != null ) {
      try {
        val = Long.valueOf(tmp).longValue();
        js.setMessageDepth((int)val);
      } catch(NumberFormatException e) {
      }
    }
    tmp = user.getProperty(YazdState.THREAD_DEPTH);
    if( tmp != null ) {
      try {
        val = Long.valueOf(tmp).longValue();
        js.setThreadDepth((int)val);
      } catch(NumberFormatException e) {
      }
    }
    tmp = user.getProperty(YazdState.ITEMS_PER_PAGE);
    if( tmp != null ) {
      try {
        val = Long.valueOf(tmp).longValue();
        js.setItemsPerPage((int)val);
      } catch(NumberFormatException e) {
      }
    }

    // Reset the users last visit to Yazd
    tmp = user.getProperty(YazdState.LAST_VISIT);
    if( tmp != null ) {
      try {
        val = Long.valueOf(tmp).longValue();
        js.setLastVisit(new Date(val));
      } catch(NumberFormatException e) {
      }
    }

    // Save the user state information
    js.setAuthorization(auth);

    // Reset the users lastForumVisit from user extended properties
    js.resetLastForumVisitDate(jr);

    js.setLoggedIn(true);
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Determine if login was successful.
   *
   * @return EVAL_PAGE if login failed, SKIP_PAGE if login succeeded
   */
  public final int doEndTag() throws JspException
  {
    if( js.getLoggedIn() )
      return SKIP_PAGE;
    return EVAL_PAGE;
  }
}
