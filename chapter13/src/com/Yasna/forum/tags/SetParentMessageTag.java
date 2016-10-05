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
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * JSP Tag <b>set_parent_message</b>, used with <b>thread</b> tag to set
 * the parent message id to move message to.
 * <p>
 * Must be nested within a <b>thread</b> tag.
 * <p>
 * The parent messageID to move message to is obtained from the body of the tag.
 * <p>
 * This tag is required to move a message and must precede the
 * <b>move_message</b> tag.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;set_parent_message&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.SetParentMessageTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Sets the thread where message should be moved.&lt;/info&gt;
 * </pre>
 *
 * @see ThreadTag
 * @see MoveMessageTag
 *
 * @author Glenn Nielsen
 */

public class SetParentMessageTag extends BodyTagSupport
{

  /**
   * Method called at start of tag, just returns EVAL_BODY_TAG
   *
   * @return EVAL_BODY_TAG
   */
  public final int doStartTag() throws JspException
  {
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Read the body of the set_parent_message tag to obtain the value
   * of the parent messageID to move message to.
   *
   * @return SKIP_BODY
   */
  public final int doAfterBody() throws JspException
  {
    // Use the body of the tag as input for the parent messageID
    BodyContent body = getBodyContent();
    String s = body.getString();
    // Clear the body since we only used it to get the parent messageID
    body.clearBody();

    int mid = 0;
    try {
      mid = Integer.valueOf(s).intValue();
    } catch(NumberFormatException e) {
      throw new JspException(
	"Jive set_parent_message tag, body of tag is not a parent messageID integer");
    }

    // Get the parent tag
    ThreadTag tt = null;
    try {
      tt = (ThreadTag)TagSupport.findAncestorWithClass(this,ThreadTag.class);
    } catch(Exception e) {
    }

    // Set the parent messageID
    if( tt == null ) {
      throw new JspException(
	"Jive set_parent_message tag must be nested inside a thread tag");
    }
    tt.setParentMessage(mid);

    return SKIP_BODY;
  }

}
