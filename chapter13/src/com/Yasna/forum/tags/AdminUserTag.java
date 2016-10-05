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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.AuthorizationFactory;
import com.Yasna.forum.ForumFactory;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;

/**
 * JSP Tag <b>admin</b>, used to perform user admin level functions
 * on a user that is not logged in or not authorized for the function.
 * <p>
 * Uses the following yazd.tag.properties
 * <p><ul>
 * <li><b>yazd.admin.username</b> - username of admin user
 * <li><b>yazd.admin.password</b> - password for admin user
 * </ul>
 * <p>
 * Uses the following HTTP input parameter:
 * <li><b>username</b> - username of user to administer
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;admin&lt;/name&gt;
 *   &lt;tagclass&gt;com.Yasna.forum.tags.AdminUserTag&lt;/tagclass&gt;
 *   &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *   &lt;info&gt;An account tag can be nested within this tag when you need permission to do something with a user who is not logged in.&lt;/info&gt;
 * </pre>
 *
 * @see YazdState
 * @see YazdRequest
 * @see AccountTag
 *
 * @author Glenn Nielsen
 */
public class AdminUserTag extends TagSupport
{
  private Authorization auth = null;
  private YazdState js = null;
  private YazdRequest jr = null;
  private User user = null;

  /**
   * Login as the admin user so that a yazd user account can be administered.
   * <p>
   * Includes body of tag if admin user was authorized based on the yazd.tag.properties
   * yazd.admin.username and yazd.admin.password, and a valid yazd username was found
   * in the HTTP input parameter "username".
   *
   * @throws JspException on system level error
   *
   * @return <b>SKIP_BODY</b> on failure to authorize as admin user or user to administer could not be found, <b>EVAL_BODY_INCLUDE</b> if admin succeeded
   */
  public final int doStartTag() throws JspException
  {
    // Initialize YazdState
    js = (YazdState)pageContext.getAttribute("yazdUserState",
		PageContext.SESSION_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd admin tag could not get yazd state.");
    }

    // Initialize YazdRequest
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null ) {
      throw new JspException("Yazd admin tag could not get yazd request.");
    }

    // Get username and password of admin
    String username = TagPropertyManager.getTagProperty("yazd.admin.username");
    String password = TagPropertyManager.getTagProperty("yazd.admin.password");
    if( username == null || username.length() == 0 ||
	password == null || password.length() == 0 )
      return SKIP_BODY;

    // Get admin user authorization
    try {
      auth = AuthorizationFactory.getAuthorization( username, password );
    }
    catch( UnauthorizedException ue ) {
      return SKIP_BODY;
    }

    // Get the user to administer from the "username" HTTP input parameter
    ServletRequest req = pageContext.getRequest();
    String tmp;
    if( (tmp = req.getParameter("username")) != null )
      return SKIP_BODY;

    try {
      ForumFactory ff = ForumFactory.getInstance(auth);
      ProfileManager pm = ff.getProfileManager();
      user = pm.getUser(tmp);
    }
    catch( Exception e) { }

    if( user == null ) {
      jr.addError("User \"" + tmp + "\" not found");
      return SKIP_BODY;
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
   * For use by account tag to get the user to administer
   *
   * @return User - Yazd user to administer
   */
  public final User getUser()
  {
    return user;
  }
}
