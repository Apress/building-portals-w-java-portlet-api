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

/**
 * JSP Tag <b>true</b>, used to test the result of an <b>eval</b> tag
 * for true.
 * <p>
 * Requires that attribute <b>eval</b> be set to the name of a
 * script variable <b>id</b> of a previous <b>eval</b> tag.
 * <p>
 * Includes body of tag if result of <b>eval</b> tag was true.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;true&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.TrueTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Evaluate an expression&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;eval&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see EvalTag
 *
 * @author Glenn Nielsen
 */
public class TrueTag extends TagSupport
{
  // id of eval script variable which contains the boolean result
  private String eval = null;

  /**
   * Gets the result of a previous eval
   *
   * @return EVAL_BODY_INCLUDE if result of eval was true, or SKIP_BODY if the result was false
   */
  public final int doStartTag() throws JspException
  {
    Boolean res;

    res = (Boolean)pageContext.getAttribute(eval,PageContext.PAGE_SCOPE);
    if( res == null )
      throw new JspException("Jive true tag could not find eval \"" +
	eval + "\"");
    if( res.booleanValue() )
      return EVAL_BODY_INCLUDE;
    return SKIP_BODY;
  }

  /**
   * Set the name of the eval tag script variable id
   */
  public final void setEval(String nam)
  {
    eval = nam;
  }

}
