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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import com.Yasna.forum.UserAlreadyExistsException;

/**
 * JSP Tag <b>create</b>, used to create a new Yazd User account.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve User data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * If optional tag attribute <b>confirm</b>="true" users must also enter
 * the password a second time to confirm their password change.
 * <p>
 * If optional tag attribute <b>password</b>="true" users will have a
 * password generated for them automatically.
 * <p>
 * If optional tag attribute <b>login</b>="true" users will automatically
 * be logged in if account is created.
 * <p>
 * If the create succeeds, includes the body of the create tag and
 * user is authorized as if they had logged in.
 * <p>
 * The authorize tag must be used in the JSP prior to using this tag
 * so that YazdState and YazdRequest are initialized.
 * <p>
 * Uses the the following HTTP input parameters
 * <p><ul>
 * <li><b>create</b> - name of submit button to use with HTML input form
 * <li><b>email</b> - users email address
 * <li><b>username</b> - users username (userid)
 * <li><b>password</b> - password
 * <li><b>confirm</b> - confirm password
 * <li><b>name</b> - users real name
 * <li><b>nameVisible</b> - check box returning a value if real name should be visi
ble
 * <li><b>emailVisible</b> - check box returning a value if email address should be
 visible
 * </ul>
 * <p>
 * Plus any additional parameters as specified in yazd.user.properties file.
 * <p>
 * Sets one or more user errors if account creation fail.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;create&lt;/name&gt;
 * &lt;tagclass&gt;com.Yasna.forum.tags.CreateTag&lt;/tagclass&gt;
 * &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 * &lt;info&gt;Includes body of tag if create is successful.&lt;/info&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;id&lt;/name&gt;
 *     &lt;required&gt;true&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;confirm&lt;/name&gt;
 *     &lt;required&gt;false&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;password&lt;/name&gt;
 *     &lt;required&gt;false&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;login&lt;/name&gt;
 *     &lt;required&gt;false&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
 * </pre>
 *
 * @see AuthorizeTag
 * @see TagPropertyManager
 * @see YazdProperty
 * @see GetYazdPropertyTag
 * @see SetYazdPropertyTag
 * @see YazdState
 * @see YazdRequest
 *
 * @see ErrorTag
 * @see ErrorLoopTag
 *
 * @author Glenn Nielsen
 */

public class CreateTag extends TagSupport implements YazdProperty
{
  private Authorization auth = null;
  private YazdState js = null;
  private YazdRequest jr = null;
  private User user = null;
  // Required input Parameters
  private String email = null;
  private String username = null;
  private String password = null;
  // Optional input parameters
  private String confirm = null;
  private String name = null;
  private String nameVisible = null;
  private String emailVisible = null;
  // Extended properties of User as configured in yazd.user.properties
  private Map properties = new HashMap();
  // Required extended properties of User as configured in yazd.user.properties
  private Map required = new HashMap();
  // Flag that we must confirm password if user is setting new password
  // Set by the account tag confirm attribute
  private boolean confirm_password = false;
  // Flag that password will be generated for user
  // Set by the account tag password attribute
  private boolean generate_password = false;
  // Flag that logs in user if account created
  // Set by the account tag login attribute
  private boolean auto_login = false;
  // Flag indicating that account was created
  private boolean created = false;

  /**
   * Creates User account if this is an HTML form submission.
   *
   * @throws JspException on system level error
   *
   * @return <b>SKIP_BODY</b> if a create form was not submitted or create account failed, <b>EVAL_BODY_INCLUDE</b> if account creation succeeded
   */

  public final int doStartTag() throws JspException
  {
    boolean extended_required = true;

    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
		PageContext.SESSION_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd create tag could not get yazd state.");
    }

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd create tag could not get yazd request.");
    }

    // Save the script variable so JSP author can access user data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);

    // Get names and default values for any extended user properties defined
    // in yazd.user.properties
    ServletRequest req = pageContext.getRequest();
    Enumeration enum = TagPropertyManager.getUserPropertyNames();
    String tmp;
    String prop;
    String propname;
    while( enum.hasMoreElements() ) {
      prop = (String)enum.nextElement();
      // Set the default value
      if( prop.startsWith( "required." ) ) {
        propname = prop.substring(9);
        required.put(propname,TagPropertyManager.getUserProperty(prop));
      } else {
        properties.put(prop,TagPropertyManager.getUserProperty(prop));
      }
    }

    // See if this is a form submission
    tmp = req.getParameter("create");
    if( tmp == null || tmp.length() == 0 ) {
      // User didn't submit a form, and we are done initializing data
      // Skip body of create tag since account hasn't been created yet
      return SKIP_BODY;
    }

    // User has submitted a page requesting an account be created

    // Harvest the standard user data input parameters from page
    email = req.getParameter("email");
    username = req.getParameter("username");
    password = req.getParameter("password");
    confirm = req.getParameter("confirm");
    name = req.getParameter("name");
    nameVisible = req.getParameter("nameVisible");
    emailVisible = req.getParameter("emailVisible");

    // Harvest the input parameters for any required extended properties
    for( Iterator it=required.keySet().iterator(); it.hasNext(); ) {
      prop = (String)it.next();
      if( (tmp = req.getParameter(prop)) != null  && tmp.length() > 0 ) {
        required.put(prop,tmp);
      } else {
        required.put(prop,"");
        extended_required = false;
      }
    }

    // Harvest the input parameters for any optional extended properties
    for( Iterator it=properties.keySet().iterator(); it.hasNext(); ) {
      prop = (String)it.next();
      if( (tmp = req.getParameter(prop)) != null ) {
        properties.put(prop,tmp);
      }
    }

    // Validate required fields, error string comes from yazd.tag.properties
    if( email == null || email.length() == 0 || !extended_required ||
	username == null || username.length() == 0 ||
        ((password == null || password.length() == 0) && !generate_password) ) {
        jr.addError(TagPropertyManager.getTagProperty("yazd.tag.create.required"));
        return SKIP_BODY;
    }

    // Validate password confirmation if confirm is true.
    // Error string comes from yazd.tag.properties
    if( confirm_password && (confirm == null || !password.equals(confirm)) ) {
        password = null;
        confirm = null;
        jr.addError(TagPropertyManager.getTagProperty("yazd.tag.create.confirm_password"));
        return SKIP_BODY;
    }

    // Generate a password for user if requested
    if( generate_password ) {
      password = jr.GeneratePassword();
      properties.put("password",password);
    }

    // Try to create the new user account
    ForumFactory ff = jr.getForumFactory();
    ProfileManager pm = jr.getProfileManager();
    try {
      user = pm.createUser( username, password, email );
    } catch( UserAlreadyExistsException e ) {
      // userid is already in use, user has to try again
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.create.userexists"));
      username = null;
      return SKIP_BODY;
    }

    // new user account created, save user data
    try {
      // Save standard user data
      if( name != null && name.length() > 0 ) {
        user.setName( name );
      }
      if( nameVisible != null && nameVisible.length() > 0) {
        user.setNameVisible( true );
      } else {
        user.setNameVisible( false );
      }
      if( emailVisible != null && emailVisible.length() > 0 ) {
        user.setEmailVisible( true );
      } else {
        user.setEmailVisible( false );
      }

      // Save any extended user properties
      // thread_depth, message_depth, and items_per_page are special
      // extended properties used for display preferences
      for( Iterator it=properties.keySet().iterator(); it.hasNext(); ) {
	tmp = (String)it.next();
        user.setProperty( tmp, (String)properties.get(tmp));
        if( tmp.equals(YazdState.MESSAGE_DEPTH) )
	  js.setMessageDepth(Integer.valueOf((String)properties.get(tmp)).intValue());
        if( tmp.equals(YazdState.THREAD_DEPTH) )
          js.setThreadDepth(Integer.valueOf((String)properties.get(tmp)).intValue());
        if( tmp.equals(YazdState.ITEMS_PER_PAGE) )
          js.setItemsPerPage(Integer.valueOf((String)properties.get(tmp)).intValue());
      }
       
      // Save any required extended user properties
      // thread_depth, message_depth, and items_per_page are special
      // extended properties used for display preferences
      for( Iterator it=required.keySet().iterator(); it.hasNext(); ) {
        tmp = (String)it.next();
        user.setProperty( tmp, (String)required.get(tmp));
        if( tmp.equals(YazdState.MESSAGE_DEPTH) )
          js.setMessageDepth(Integer.valueOf((String)required.get(tmp)).intValue());
        if( tmp.equals(YazdState.THREAD_DEPTH) )
          js.setThreadDepth(Integer.valueOf((String)required.get(tmp)).intValue());
        if( tmp.equals(YazdState.ITEMS_PER_PAGE) )
          js.setItemsPerPage(Integer.valueOf((String)required.get(tmp)).intValue());
      }
      // Finally, get authorization for new user account
      auth = AuthorizationFactory.getAuthorization( username, password );
    } catch(UnauthorizedException e) {
      throw new JspException("Create tag not authorized to set user values.");
    }

    // Account is now created
    created = true;

    // See if user should be automatically logged in
    if( auto_login ) {
      // User is now authorized as if they had logged in
      js.setAuthorization(auth);
      js.setLoggedIn(true);
    }

    // All done, the body of the create tag can now redirect the
    // user some where else if needed.
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Method called at end of create Tag
   *
   * @return <b>SKIP_PAGE</b> if new user account created, <b>EVAL_PAGE</b> if user account has not been created yet.
   */

  public final int doEndTag() throws JspException
  {
    if( created )
      return SKIP_PAGE;
    return EVAL_PAGE;
  }

  /**
   * Set a flag indicating that password changes should be confirmed
   * (Optional attribute).
   */
  public final void setConfirm(String name)
  {
    if( name.equals("true") )
      confirm_password = true;
  }

  /**
   * Set a flag indicating that password will be generated for user
   * (Optional attribute).
   */
  public final void setPassword(String name)
  {
    if( name.equals("true") )
      generate_password = true;
  }

  /**
   * Set a flag indicating that user should be logged in once account
   * is created
   * (Optional attribute).
   */
  public final void setLogin(String name)
  {
    if( name.equals("true") )
      auto_login = true;
  }

  // Get methods for standard user data which is made
  // availabe to JSP author via a script variable

  /**
   * User Email address property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="email"/&gt;
   *
   * @return String - user email address
   */
  public final String getEmail()
  {
    if( email == null )
      return "";
    return email;
  }

  /**
   * User Username (userid) property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="username"/&gt;
   *
   * @return String - user username (forum userid)
   */
  public final String getUsername()
  {
    if( username == null )
      return "";
    return username;
  }

  /**
   * Password entered in previous HTML form submisson which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="password"/&gt;
   *
   * @return String - password the user entered in the HTML form submitted
   */
  public final String getPassword()
  {
    if( password == null )
      return "";
    return password;
  }

  /**
   * Confirm Password entered in previous HTML form submisson which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="confirm"/&gt;
   *
   * @return String - confirm password the user entered in the HTML form submitted
   */
  public final String getConfirm()
  {
    if( confirm == null )
      return "";
    return confirm;
  }

  /**
   * User Name (real name) property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="name"/&gt;
   *
   * @return String - user name (real name)
   */
  public final String getName()
  {
    if( name == null )
      return "";
    return name;
  }

  /**
   * User NameVisible (real name) property which can be obtained by the
   * JSP Page using &lt;jsp:getProperty name=<i>"id"</i> property="nameVisible"/&gt;
   *
   * @return checkbox value if user real name should be visible.
   */
  public final String getNameVisible()
  {
    if( nameVisible != null )
      return "checked";
    return "";
  }

  /**
   * User EmailVisible (email address) property which can be obtained by the
   * JSP Page using &lt;jsp:getProperty name=<i>"id"</i> property="emailVisible"/&gt;
   *
   * @return checkbox value if user email address should be visible.
   */
  public final String getEmailVisible()
  {
    if( emailVisible != null )
      return "checked";
    return "";
  }

  /**
   * Method used by the getYazdProperty tag to get an extended User
   * property from the create tag script variable.
   *
   * @return String - value of the property
   */
  public final String getProperty(String name)
  {
    String tmp = (String)properties.get(name);
    if( tmp != null )
      return tmp;
    tmp = (String)required.get(name);
    if( tmp != null )    
      return tmp;
    return "";
  }

  /**
   * Method used by the setYazdProperty tag to set an extended User
   * property from the create tag script variable.
   */
  public final void setProperty(String name, String value)
  {
    user.setProperty(name,value);
    if( properties.containsKey(name) )
      properties.put(name,value);
    else if( required.containsKey(name) )
      properties.put(name,value);
  }
}
