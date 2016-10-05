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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.User;

/**
 * JSP Tag <b>message</b>, used to get information about current message.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve ForumMessage data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * Gets the current message from the user state information.
 * <p>
 * If optional attribute <b>nested</b>="true" the message is obtained
 * from the closest enclosing <b>walk</b> or <b>thread</b> tag.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;message&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.MessageTag&lt;/tagclass&gt;
 *  &lt;teiclass&gt;com.Yasna.forum.tags.MessageTEI&lt;/teiclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Get the current message data&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;id&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;nested&lt;/name&gt;
 *    &lt;required&gt;false&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see WalkTag
 * @see ThreadTag
 * @see YazdRequest
 * @see YazdState
 * @see YazdProperty
 * @see NewMessages
 *
 * @author Glenn Nielsen
 */
public class MessageTag extends TagSupport implements YazdProperty,
	NewMessages
{
  private YazdState js = null;
  private YazdRequest jr = null;
  private ForumMessage cm = null;
  private GetNestedMessage gnm = null;
  // Flag to indicate the message is from a parent walk or thread tag
  private boolean nested = false;

  /**
   * Method called at start of message Tag to get ForumMessage
   *
   * @return EVAL_BODY_INCLUDE if there is a message or SKIP_BODY if there is not a message to view
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd message tag could not get yazd state.");
    }

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Yazd message tag, could not find request");

    // See if we are nested inside a walk or thread tag
    if( !nested ) {
        // Get the message from user state information
        cm = jr.getMessage();
    } else {
      // Get the closest enclosing walk or thread tag.
      try {
        gnm = (GetNestedMessage)TagSupport.findAncestorWithClass(this,GetNestedMessage.class);
	cm = gnm.getMessage();
      } catch(Exception e) {
      }
    }

    if( cm == null )
      return SKIP_BODY;

    // Save the script variable so JSP author can access message data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Set a flag indicating whether message is nested within a walk or thread tag
   * Optional attribute).
   */
  public final void setNested(String a)
  {
    if( a.equals("true") )nested=true;
  }

  /**
   * Used by UserTag to get the User data for the person
   * who posted this message.
   *
   * @return User who posted message being viewed
   */
  public final User getUser()
  {
    return cm.getUser();
  }

  /**
   * Message ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="messageid"/&gt;
   *
   * @return String - message ID
   */
  public final String getMessageid()
  {
    return "" + cm.getID();
  }

  /**
   * Message Body property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="body"/&gt;
   *
   * @return String - message body
   */
  public final String getBody()
  {
    return cm.getBody();
  }

  /**
   * Message Subject property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="subject"/&gt;
   *
   * @return String - message subject
   */
  public final String getSubject()
  {
    return cm.getSubject();
  }

  /**
   * Date and time of Message CreationDate (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="creationDate"/&gt;
   *
   * @return date and time of Message CreationDate as an integer
   */
  public final String getCreationDate()
  {
    return "" + cm.getCreationDate().getTime();
  }

  /**
   * Date and time of Message ModifiedDate (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="modifiedDate"/&gt;
   *
   * @return date and time of Message ModifiedDate as an integer
   */
  public final String getModifiedDate()
  {
    return "" + cm.getModifiedDate().getTime();
  }

  /**
   * Get the total number of replies to this message
   *
   * @return number of replies to this message
   */
  public final String getMessagecount()
  {
    if( gnm != null ) {
      return "" + gnm.getTotal();
    }
    return "";
  }

  /**
   * Get the ID of the thread this message is in
   *
   * @return threadID
   */
  public final String getThreadid()
  {
    return "" + cm.getForumThread().getID();
  }

  /**
   * Method used by the getYazdProperty tag to get an extended Message
   * property from the message tag script variable.
   *
   * @return String - value of the property
   */
  public final String getProperty(String name)
  {
    String tmp = cm.getProperty(name);
    if( tmp != null )return tmp;
    return "";
  }

  /**
   * Method used by the setYazdProperty tag to set an extended Message
   * property from the message tag script variable.
   */
  public final void setProperty(String name, String value)
  {
    cm.setProperty(name,value);
  }

  /**
   * Determine if message was modified since users last visit.
   *
   * @return boolean - true or false
   */
  public final boolean newMessages()
  {
    if( js.getLastForumVisitDate(cm.getForumThread().getForum(),jr).getTime() <
        cm.getModifiedDate().getTime() )
                return true;
    return false;
  }

  /**
   * Determine if message was posted by an anonymous user.
   *
   * @return boolean - true or false
   */
  public final boolean isAnonymous()
  {
    return cm.isAnonymous();
  }

  /**
   * Remove the script variable after message tag closed out
   */
  public final void release()
  {
    if( id != null && id.length() > 0 )
      pageContext.removeAttribute(id,PageContext.PAGE_SCOPE);
  }
}
