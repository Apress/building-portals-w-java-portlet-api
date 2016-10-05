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

import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>user</b>, used to access account information for
 * a user who posted a message.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve User data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * If the current yazd user is not authorized to view an account
 * property an empty string "" will be returned.
 * <p>
 * Uses the following HTTP input parameter:
 * <li><b>username</b> - username of user to view public account information
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;user&lt;/name&gt;
 * &lt;tagclass&gt;com.Yasna.forum.tags.UserTag&lt;/tagclass&gt;
 * &lt;teiclass&gt;com.Yasna.forum.tags.UserTEI&lt;/teiclass&gt;
 * &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 * &lt;info&gt;User information for the user who posted the current message being displayed.&lt;/info&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;id&lt;/name&gt;
 *     &lt;required&gt;true&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 * </pre>
 *
 * @see MessageTag
 * @see YazdProperty
 * @see GetYazdPropertyTag
 * @see SetYazdPropertyTag
 * @see YazdState
 * @see YazdRequest
 *
 * @author Glenn Nielsen
 */
public class UserTag extends TagSupport implements YazdProperty
{
  private YazdRequest jr = null;
  private YazdState js = null;
  private User ui = null;

  /**
   * Method used at start of user Tag to get User data for
   * person who posted a message or the user set by the
   * HTML form input parameter "username".
   *
   * @throws JspException on system level error
   *
   * @return EVAL_BODY_INCLUDE if user is found, SKIP_BODY if user could no be found
   */
  public final int doStartTag() throws JspException
  {
    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Yazd user tag, could not find request");

    // Get the parent message tag
    MessageTag mt = null;
    try {
      mt = (MessageTag)TagSupport.findAncestorWithClass(this,MessageTag.class);
    } catch(Exception e) {
    }

    if( mt != null ) {
      ui = mt.getUser();
    }

    // Not inside an admin tag, so get a public instance of user
    if( ui == null ) {
     // See if we should get the user based on HTML input parameter
      // username
      ServletRequest req = pageContext.getRequest();
      String tmp;
      if( (tmp = req.getParameter("username")) != null ) {
        ProfileManager pm = jr.getProfileManager();
        try {
          ui = pm.getUser(tmp);
        } catch(UserNotFoundException e) {}
      }
    }

    if( ui == null ) {
      return SKIP_BODY;
    }

    // Save the script variable so JSP author can access user data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);
    return EVAL_BODY_INCLUDE;
  }

  /**
   * User Email address property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="email"/&gt;
   *
   * @return String - user email address if authorized to view it
   */
  public final String getEmail()
  {
    String email;

    email =  ui.getEmail();
    if( email == null )
      return "";
    return email;
  }

  /**
   * User Name (real name) property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="name"/&gt;
   *
   * @return String - user name (real name) if authorized to view it
   */
  public final String getName()
  {
    String name;

    name =  ui.getName();
    if( name == null )
      return "";
    return name;
  }

  /**
   * User Username (userid) property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="username"/&gt;
   *
   * @return String - user username (forum userid)
   */
  public final String getUsername()
  {
    return ui.getUsername();
  }

  /**
   * Method used by the getYazdProperty tag to get an extended User
   * property from the user tag script variable.
   *
   * @return String - value of the property
   */
  public final String getProperty(String name)
  {
    // Don't allow the special property named "password" to be returned
    if( name.equals("password") )
      return "";
    String tmp = ui.getProperty(name);
    if( tmp != null )return tmp;
    return "";
  }

  /**
   * Method required by the setYazdProperty tag to set an extended User
   * property, disabled in user tag.
   */
  public final void setProperty(String name, String value)
  {
  }

  /**
   * Remove the script variable after forum tag closed out
   */
  public final void release()
  {
    if( id != null && id.length() > 0 )
      pageContext.removeAttribute(id,PageContext.PAGE_SCOPE);
  }
}
