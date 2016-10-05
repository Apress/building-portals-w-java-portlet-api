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

import com.Yasna.forum.ForumPermissions;
import com.Yasna.forum.Group;
import com.Yasna.forum.GroupNotFoundException;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>account</b>, used to modify a Yazd User account.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve User data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * If optional tag attribute <b>confirm</b>="true" and the
 * user enters a password, they must also enter the password
 * a second time to confirm their password change.
 * <p>
 * Requires that the User be logged in to modify their account.
 * <p>
 * Can be nested inside the <b>admin</b> tag so that a user
 * can be modified using the permissions of the admin user.
 * <p>
 * The authorize tag must be used in the JSP prior to using this tag
 * so that YazdState and YazdRequest are initialized, and to verify
 * that a real user is logged in.
 * <p>
 * Uses the the following HTTP input parameters
 * <p><ul>
 * <li><b>modify</b> - name of submit button to use with HTML input form
 * <li><b>email</b> - users email address
 * <li><b>password</b> - new password
 * <li><b>confirm</b> - confirm password if changing to new password
 * <li><b>name</b> - users real name
 * <li><b>nameVisible</b> - check box returning "checked" if real name should be visible
 * <li><b>emailVisible</b> - check box returning "checked" if email address should be visible
 * </ul>
 * <p>
 * Plus any additional parameters as specified in yazd.user.properties file.
 * If a property in yazd.user.properties is named "password", that User
 * property is only changed if a user's password gets changed and they
 * are not a System, Forum, Group, or User Admin.
 * <p>
 * Sets one or more user errors if account modifications fail.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;account&lt;/name&gt;
 * &lt;tagclass&gt;com.Yasna.forum.tags.AccountTag&lt;/tagclass&gt;
 * &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 * &lt;info&gt;Includes body of tag if account modifications are successful.&lt;/info&gt;
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
 * </pre>
 *
 * @see AdminUserTag
 * @see TagPropertyManager
 * @see YazdProperty
 * @see GetYazdPropertyTag
 * @see SetYazdPropertyTag
 * @see YazdState
 * @see YazdRequest
 * @see ErrorTag
 * @see ErrorLoopTag
 *
 * @author Glenn Nielsen
 */

public class AccountTag extends TagSupport implements YazdProperty
{
  private YazdState js = null;
  private YazdRequest jr = null;
  private User user = null;
  // Static user data, can't change username after account created.
  private String username = null;
  // Required input parameters
  private String email = null;
  // Optional input parameters
  private String nameVisible = null;
  private String emailVisible = null;
  private String password = null;
  private String confirm = null;
  private String name = null;
  // Extended properties of User as configured in yazd.user.properties
  private Map properties = new HashMap();
  // Required extended properties of User as configured in yazd.user.properties
  private Map required = new HashMap();
  // Flag that we must confirm password if user is setting new password
  // Set by the account tag confirm attribute
  private boolean confirm_password = false;

  /**
   * Initializes data for account tag User data and modifies User account
   * if this is an HTML form submission.
   *
   * @throws JspException on system level error
   *
   * @return EVAL_BODY_INCLUDE
   */

  public final int doStartTag() throws JspException
  {
    boolean extended_required = true;

    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
		PageContext.SESSION_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd account tag could not get yazd state.");
    }

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null ) {
      throw new JspException("Yazd account tag could not get yazd request.");
    }

    // If we are nested inside an admin tag, get user to administer
    try {
      AdminUserTag at = (AdminUserTag)TagSupport.findAncestorWithClass(this,AdminUserTag.class);
      user = at.getUser();
    } catch(Exception e) {
    }

    if( user == null ) {
      // Get the User data
      ProfileManager pm = jr.getProfileManager();
      try {
        user = pm.getUser(js.getAuthorization().getUserID());
      } catch( UserNotFoundException ex ) {
        throw new JspException("Yazd account tag could not find user.");
      }
    }

    // Make sure user isn't anonymous
    if( user.isAnonymous() ) {
      throw new JspException("Yazd account tag can not be used by an anonymous user.");
    }

    // Get user board name
    if( user.getUsername() == null )
      throw new JspException("Yazd account tag, user with id " +
	user.getID() + " has no name.");
    username = user.getUsername();

    // Save the script variable so JSP author can access user data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);

    // Get the User data and any extended properties 
    Enumeration enum = TagPropertyManager.getUserPropertyNames();
    String tmp;
    String prop;
    ServletRequest req = pageContext.getRequest();
    if( user.getEmail() != null )
      email = user.getEmail();
    if( user.getName() != null )
      name = user.getName();
    if( user.isEmailVisible() )
      emailVisible = "checked";
    if( user.isNameVisible() )
      nameVisible = "checked";
    // Get values of any extended user properties defined
    // in yazd.user.properties
    while( enum.hasMoreElements() ) {
      prop = (String)enum.nextElement();
      if( prop.startsWith( "required." ) ) {
        prop = prop.substring(9);
        if( (tmp = user.getProperty(prop)) != null ) {
          required.put(prop,tmp);
        } else {
          required.put(prop,"");
        }
      } else {
        if( (tmp = user.getProperty(prop)) != null ) {
	  properties.put(prop,tmp);
	} else {
          properties.put(prop,"");
	}
      }
    }

    if( (tmp = req.getParameter("modify")) == null ) {
      // First time account info display, so continue on with rest of page
      return EVAL_BODY_INCLUDE;
    }

    // User has submitted a page requesting changes to their user data

    // Harvest the standard user data input parameters from page
    email = req.getParameter("email");
    password = req.getParameter("password");
    confirm = req.getParameter("confirm");
    name = req.getParameter("name");
    nameVisible = req.getParameter("nameVisible");
    emailVisible = req.getParameter("emailVisible");

    // Harvest the input parameters for any required extended properties
    for( Iterator it=required.keySet().iterator(); it.hasNext(); ) {
      prop = (String)it.next(); 
      if( (tmp = req.getParameter(prop)) != null && tmp.length() > 0 ) {
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

    // Validate email address, error string comes from yazd.tag.properties
    if( email == null || email.length() == 0 || !extended_required ) {
        jr.addError(TagPropertyManager.getTagProperty("yazd.tag.account.required"));
        return EVAL_BODY_INCLUDE;
    }

    // Validate password confirmation if a password change is being made,
    // and confirm is true. Error string comes from yazd.tag.properties
    if( password != null && password.length() > 0 ) {
      if( confirm_password && (confirm == null || !password.equals(confirm)) ) {
        password = null;
        confirm = null;
        jr.addError(TagPropertyManager.getTagProperty("yazd.tag.account.confirm_password"));
        return EVAL_BODY_INCLUDE;
      }
    }

    try {
      // Save standard user data if changed
      if( password != null && password.length() > 0 ) {
	user.setPassword(password);
	if( properties.get("password") != null ) {
	  ForumPermissions perms =
	    jr.getForumFactory().getPermissions(js.getAuthorization());
          if( !perms.get(ForumPermissions.SYSTEM_ADMIN) &&
              !perms.get(ForumPermissions.FORUM_ADMIN) &&
              !perms.get(ForumPermissions.GROUP_ADMIN) &&
              !perms.get(ForumPermissions.USER_ADMIN) ) {
                user.setProperty( "password", (String)properties.get("password"));
	  }
	}
      }
      if( name != null && name.length() > 0 ) {
        user.setName( name );
      }
      if( nameVisible != null && nameVisible.length() > 0) {
        user.setNameVisible( true );
      } else {
        user.setNameVisible( false );
      }
      if( email != null && email.length() > 0 ) {
        user.setEmail( email );
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
        if( !tmp.equals("password") )
          user.setProperty( tmp, (String)properties.get(tmp));
        if( tmp.equals(YazdState.MESSAGE_DEPTH) )
          js.setMessageDepth(Integer.valueOf((String)properties.get(tmp)).intValue());
        if( tmp.equals(YazdState.THREAD_DEPTH) )
          js.setThreadDepth(Integer.valueOf((String)properties.get(tmp)).intValue());
        if( tmp.equals(YazdState.ITEMS_PER_PAGE) )
          js.setItemsPerPage(Integer.valueOf((String)properties.get(tmp)).intValue());
      }

      // Save any extended required user properties
      // thread_depth, message_depth, and items_per_page are special
      // extended properties used for display preferences
      for( Iterator it=required.keySet().iterator(); it.hasNext(); ) {
        tmp = (String)it.next();
        if( !tmp.equals("password") )
          user.setProperty( tmp, (String)required.get(tmp));
        if( tmp.equals(YazdState.MESSAGE_DEPTH) )
          js.setMessageDepth(Integer.valueOf((String)required.get(tmp)).intValue());
        if( tmp.equals(YazdState.THREAD_DEPTH) )
          js.setThreadDepth(Integer.valueOf((String)required.get(tmp)).intValue());
        if( tmp.equals(YazdState.ITEMS_PER_PAGE) )
          js.setItemsPerPage(Integer.valueOf((String)required.get(tmp)).intValue());
      }

    } catch(UnauthorizedException e) {
      throw new JspException("Yazd account tag, user with id " +
	user.getID() + " is not authorized to set user values.");
    }

    // All done, the body of the account tag can now redirect the
    // user some where else if needed.
    return EVAL_BODY_INCLUDE;
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
   * @return <b>checked</b> if user real name should be visible.
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
   * @return <b>checked</b> if user email address should be visible.
   */
  public final String getEmailVisible()
  {
    if( emailVisible != null )
      return "checked";
    return "";
  }

  /**
   * Method used by the getYazdProperty tag to get an extended User
   * property from the account tag script variable.
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
   * property from the account tag script variable.
   */
  public final void setProperty(String name, String value)
  {
    user.setProperty(name,value);
    if( properties.containsKey(name) )
      properties.put(name,value);
    else if( required.containsKey(name) )  
      properties.put(name,value);
  }

  /**
   * Method used to determine if user is a member of a yazd Group.
   *
   * @return true if a member, false if not a member
   */
  public final boolean isMember(String name) throws JspException
  {
    // Get the group data
    ProfileManager pm = jr.getProfileManager();
    try {
      Group group = pm.getGroup(name);
      return group.isMember(user);
    } catch( GroupNotFoundException ex ) {
    }
    return false;
  }

}
