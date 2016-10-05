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

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>preview_message</b>, used with <b>message</b> tag to
 * preview a message before posting to a forum.
 * <p>
 * Uses the the following HTTP input parameters
 * <p><ul>
 * <li><b>subject</b> - subject of message
 * <li><b>body</b> - body of message
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;preview_message&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.PostMessageTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Saves the message to the forum.&lt;/info&gt;
 * </pre>
 *
 * @see MessageTag
 *
 * @author Glenn Nielsen
 */

public class PreviewMessageTag extends TagSupport implements GetNestedMessage
{
  private YazdState js = null;
  private YazdRequest jr = null;
  private String subject;
  private String body;
  private ForumMessage message;

  /**
   * Method called at start of tag, previews the message, includes
   * body of tag if body parameter has content.
   *
   * @return EVAL_BODY_INCLUDE on success, SKIP_BODY on failure
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null )
      throw new JspException("Yazd preview_message tag, could not find yazdUserState");

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Could not find request");

    // Get HTTP input parameters
    ServletRequest req = pageContext.getRequest();
    subject = req.getParameter("subject");
    if( subject == null || subject.length() == 0)
      subject = " ";
    body = req.getParameter("body");
    if( body == null || body.length() == 0 )
      body = " ";

    Forum cf = jr.getForum();
    User user = null;

    try {
      user = jr.getProfileManager().getUser( js.getAuthorization().getUserID() );
    } catch( UserNotFoundException e ) {
      return SKIP_BODY;
    }
    try {
      ForumMessage nm = cf.createMessage( user );
      nm.setSubject( subject );
      nm.setBody( body.toString() );
      message = cf.applyFilters(nm);
    } catch( UnauthorizedException ue ) {
      return SKIP_BODY;
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
   * Used by <b>message</b> tag to get the current ForumMessage
   *  
   * @return ForumMessage
   */   
  public final ForumMessage getMessage()
  { 
    return message;
  }          
  
  /**
   * Return total number of messages in thread, required by
   * GetNestedMessage.      
   *                 
   * @return String - thread MessageCount
   */                       
  public final int getTotal()
  {                         
    return 0;
  }

}
