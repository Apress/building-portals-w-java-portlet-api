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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.AuthorizationFactory;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>authorize</b>, used to authorize a Yazd user session.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve YazdRequest data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * If optional attribute <b>anonymous</b>="true" anonymous users
 * are allowed to use the JSP page.
 * <p>
 * If authorize fails a user error is set, the body of the authorize tag is
 * included, and the remainder of the page is skipped.
 * <p>
 * Retrieves session information for user and will create session information
 * for a new anonymous user.
 * <p>
 * Updates state information about the users session from the following
 * HTTP input parameters
 * <p><ul>
 * <li><b>forum</b> - current forum user is viewing
 * <li><b>thread</b> - current thread user is viewing
 * <li><b>message</b> - current message user is viewing
 * </ul>
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 * &lt;name&gt;authorize&lt;/name&gt;
 *   &lt;tagclass&gt;com.Yasna.forum.tags.AuthorizeTag&lt;/tagclass&gt;
 *   &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *   &lt;info&gt;Authorize Yazd user and initialize forum, thread, and message parameters.&lt;/info&gt;
 *   &lt;attribute&gt;
 *     &lt;name&gt;anonymous&lt;/name&gt;
 *     &lt;required&gt;false&lt;/required&gt;
 *     &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *   &lt;/attribute&gt;
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

public class AuthorizeTag extends TagSupport
{
  private YazdState js = null;
  private YazdRequest jr = null;
  private Authorization auth = null;
  // Flag indicating anonymous user can view page
  boolean anonymous = false;
  // Flag indicating that authorization failed
  private boolean not_authorized = false;

  /**
   * Retrieves session information for user and will create session information
   * for a new anonymous user.
   *
   * @throws JspException on system level error
   *
   * @return <b>SKIP_BODY</b> if user is authorized for page, <b>EVAL_BODY_INCLUDE</b> if user is not authorized.
   */

  public final int doStartTag() throws JspException
  {
    String tmp;

    // Get user state information, create new if needed
    js = (YazdState)pageContext.getAttribute("yazdUserState",
		PageContext.SESSION_SCOPE);
    if( js == null ) {
      js = new YazdState();
      pageContext.setAttribute("yazdUserState",js,PageContext.SESSION_SCOPE);
    }

    // Initialize user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null ) {
      jr = new YazdRequest();
      pageContext.setAttribute("yazdUserRequest",jr,PageContext.REQUEST_SCOPE);
    }
    pageContext.setAttribute(id,jr,PageContext.PAGE_SCOPE);
    jr.setYazdState(js);

    auth = (Authorization)js.getAuthorization();
    if( auth == null ) {
      // Create new authorization for an anonymous user
      auth = AuthorizationFactory.getAnonymousAuthorization();
      js.setAuthorization(auth);
      // Initialize default settings for anonymous user from yazd.tag.properties
      int val;
      tmp = TagPropertyManager.getTagProperty(YazdState.MESSAGE_DEPTH);
      if( tmp != null ) {
        try {
          val = Integer.valueOf(tmp).intValue();
	  js.setMessageDepth(val);
	} catch(NumberFormatException e) {
	}
      }
      tmp = TagPropertyManager.getTagProperty(YazdState.THREAD_DEPTH);
      if( tmp != null ) {
        try {
          val = Integer.valueOf(tmp).intValue();
          js.setThreadDepth(val);
        } catch(NumberFormatException e) {
        }
      }
      tmp = TagPropertyManager.getTagProperty(YazdState.ITEMS_PER_PAGE);
      if( tmp != null ) {
        try {
          val = Integer.valueOf(tmp).intValue();
          js.setItemsPerPage(val);
        } catch(NumberFormatException e) {
        }
      }
      // See if user has any lastVisit cookies
      Cookie [] cookies = ((HttpServletRequest)pageContext.getRequest()).getCookies();
      if( cookies != null && cookies.length > 0 ) {
	String name;
	String value;
        for( int i = 0; i < cookies.length; i++ ) {
	  name = cookies[i].getName();
	  value = cookies[i].getValue();
	  if( name.equals(YazdState.LAST_VISIT) ) {
	    js.setLastVisit(new Date(Long.valueOf(value).longValue()));
	  } else if( name.startsWith(YazdState.LAST_FORUM_VISIT) ) {
	    js.setLastForumVisitDate(name,new Date(Long.valueOf(value).longValue()));
	  }
	}
      }
    }

    // For a logged in user, set the Date of the next LastVisit
    js.setNextVisit(pageContext);

    // Set the YazdState based on the HTTP parameters
    ServletRequest req = pageContext.getRequest();
    tmp = req.getParameter("forum");
    if( tmp != null && tmp.length() > 0 )
      js.setForumID(Integer.valueOf(tmp).intValue());
    tmp = req.getParameter("thread");
    if( tmp != null && tmp.length() > 0 )
      js.setThreadID(Integer.valueOf(tmp).intValue());
    tmp = req.getParameter("message");
    if( tmp != null && tmp.length() > 0 )
      js.setMessageID(Integer.valueOf(tmp).intValue());
    tmp = req.getParameter("parent");
    if( tmp != null && tmp.length() > 0 )
      js.setParentID(Integer.valueOf(tmp).intValue());

    // Make sure user is authorized
    User user;
    try {
      user = jr.getProfileManager().getUser(auth.getUserID());
    } catch( UserNotFoundException ex ) {
      throw new JspException("authorize tag could not find user with ID: " +
	auth.getUserID());
    }
    if( !anonymous && user.isAnonymous() ) {
      // User is not authorized to view page
      not_authorized = true;
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.authorize.failed"));
      return EVAL_BODY_INCLUDE;
    }
    // User is authorized for page
    return SKIP_BODY;
  }

  /**
   * Method called at end of authorize Tag
   *
   * @return <b>EVAL_PAGE</b> if user authorized to view page, <b>SKIP_PAGE</b> if user is not authorized.
   */
  public final int doEndTag() throws JspException
  {
    if( not_authorized )
      return SKIP_PAGE;
    return EVAL_PAGE;
  }

  /**
   * Set a flag indicating whether an anonymous user is authorized to
   * view page (Optional attribute).
   */
  public final void setAnonymous(String a)
  {
    if( a.equals("true") )anonymous=true;
  }
}
