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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.ForumThreadNotFoundException;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>post</b>, used to setup for posting a new message or replying
 * to an existing message using <b>post_message</b> tag.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for use in tag body to get message
 * properties using &lt;jsp:getProperty/&gt;, get extended message propertes
 * using the <b>yazdGetProperty</b> tag, or set extended message properties
 * using the <b>yazdSetProperty</b> tag.
 * <p>
 * Uses the the following HTTP input parameters
 * <p><ul>
 * <li><b>subject</b> - subject of message
 * <li><b>body</b> - body of message
 * <li><b>post</b> - this is a new message post
 * <li><b>reply</b> - this is a reply to a message
 * </ul>
 * <p>
 * The <b>change_forum</b> tag can be used nested within
 * the <b>post</b> tag to set what forum the message is
 * to be posted to.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;post&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.PostTag&lt;/tagclass&gt;
 *  &lt;teiclass&gt;com.Yasna.forum.tags.PostTEI&lt;/teiclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Used to setup for posting a new message.&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;id&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see ChangeForumTag
 * @see PostMessageTag
 *
 * @author Glenn Nielsen
 */

public class PostTag extends TagSupport implements YazdProperty, ChangeForum
{
  // HTTP input parameters
  private String subject = null;
  private StringBuffer body = new StringBuffer();
  private boolean isreply = false;
  private boolean submit = false;
  // Internal
  private YazdRequest jr = null;
  private YazdState js = null;
  private Forum pf = null;
  private ForumMessage message = null;
  private Map properties = new HashMap();

  /**
   * Sets up to post a new message or reply to a message if this is an
   * HTML form submission with values set for the parameters body, subject,
   * and post.
   *
   * @throws JspException on system level error
   *
   * @return EVAL_BODY_INCLUDE
   */
  public final int doStartTag() throws JspException
  {
    // Initialize YazdState
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null )
      throw new JspException("Yazd post tag, could not find yazdUserState");

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Yazd post tag, could not find request");

    // Save the script variable so JSP author can access post message data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);

    // Get HTTP input parameters
    ServletRequest req = pageContext.getRequest();
    String tmp;
    tmp = req.getParameter("post");
    if( tmp != null && tmp.length() > 0 )
      submit=true;
    tmp = req.getParameter("reply");
    if( tmp != null && tmp.length() > 0 ) {
      submit=true;
      isreply = true;
    }

    subject =  req.getParameter("subject");
    body.append( req.getParameter("body") );

    // Have the input to post a message
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Message Subject entered in previous HTML form submisson which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="subject"/&gt;
   *
   * @return String - message subject the user entered in the HTML form submitted
   */
  public final String getSubject()
  {
    if( subject == null )
      return "";
    return subject;
  }

  /**
   * Method used by the getYazdProperty tag to get an extended Message
   * property from the post or message tag script variable.
   *
   * @return String - value of the property
   */
  public final String getProperty(String name)
  {
    String tmp = (String)properties.get(name);
    if( tmp != null )return tmp;
    return "";
  }

  /**
   * Method used by the setYazdProperty tag to set an extended Message
   * property from the post or message tag script variable.
   */
  public final void setProperty(String name, String value)
  {
    properties.put(name,value);
  }

  /**
   * Message Body entered in previous HTML form submisson which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="body"/&gt;
   *
   * @return String - message body the user entered in the HTML form submitted
   */
  public final String getBody()
  {
    return body.toString();
  }

  /**
   * Append a string to the body of the message,
   * used by <b>post_append</b> tag. Only appends
   * the string if the body of message already has
   * content.
   *
   * @param String text to append to message body
   */
  public final void appendBody(String str)
  {
    if( body.length() > 0 )
      body.append(str);
  }

  /**
   * Create a new message for a forum post or reply.
   *
   * @return true if message creation succeeded
   */
  public final boolean postMessage() throws JspException
  {
    ForumMessage parentMessage = null;
    ForumThread thread = null;
    Forum cf = jr.getForum();
    User user = null;

    // Make sure post is triggered from a form submit
    if( !submit )
      return false;

    if( body.length() == 0 ||
      subject == null || subject.length() == 0 ) {
      jr.addError("A message must have both a Subject and a Message.");
      return false;
    }

    if( pf == null ) {
      pf = cf;
    }

    try {
      user = jr.getProfileManager().getUser( js.getAuthorization().getUserID() );
    } catch( UserNotFoundException e ) {
      throw new JspException("Post Message, user account not found");
    }
    try {
      message = pf.createMessage( user );
      message.setSubject( subject );
      message.setBody( body.toString() );
      String tmp;
      for( Iterator it=properties.keySet().iterator(); it.hasNext(); ) {
        tmp = (String)it.next();
        message.setProperty(tmp,(String)properties.get(tmp));
      }
      if( isreply ) {
        try {
          thread = cf.getThread( js.getThreadID() );
          parentMessage = thread.getMessage( js.getMessageID() );
        } catch( ForumThreadNotFoundException e ) {
          throw new JspException("Post Message, thread not found with id: " +
                js.getThreadID() );
        } catch( ForumMessageNotFoundException e ) {
          throw new JspException("Post Message, parent not found with id: " +
                js.getMessageID() );
        }
        thread.addMessage( parentMessage, message );
      } else {
        thread = pf.createThread( message );
        pf.addThread( thread );
      }
    } catch( UnauthorizedException ue ) {
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.post.authorize.failed"));
      return false;
    }
    return true;
  }

  /**
   *  Set an alternate forum where message is to be posted.
   */
  public final void changeForum(Forum apf)
  {
    pf = apf;
  }

}
