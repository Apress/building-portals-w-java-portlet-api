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
 * JSP Tag <b>rotate</b>, used to rotate output within a loop.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable so that the <b>rotate</b> tag can save state
 * information about its rotations which it can reuse for the
 * next loop.
 * <p>
 * This tag is designed to be nested within some other looping
 * tag set.
 * <p>
 * <b>rotate_selection</b> tags are nested within the <b>rotate</b>
 * tag to specify what items to select from for each rotation
 * within some loop tag set.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;rotate&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.RotateTag&lt;/tagclass&gt;
 *  &lt;teiclass&gt;com.Yasna.forum.tags.RotateTEI&lt;/teiclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Rotates ouput within a loop.&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;id&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see RotateSelectionTag
 *
 * @author Glenn Nielsen
 */
public class RotateTag extends TagSupport
{
  // Previously saved RotateTag
  private RotateTag rt = null;
  // Variables to keep track of rotation selection
  private int index = 0;
  private int size = 0;
  private int total = 0;
  private boolean total_set = false;

  /**
   * Method called at start of rotate Tag
   *
   * @return EVAL_BODY_INCLUDE
   */
  public final int doStartTag() throws JspException
  {
    Boolean res;

    // Try to get rotate state information from previous rotation.
    rt = (RotateTag)pageContext.getAttribute(id,
		PageContext.PAGE_SCOPE);
    if( rt == null ) {
      // First time rotation encountered in a loop, save state information.
      rt = this;
      pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);
    } else {
      // Retrieve state information from original RotateTag
      index = rt.getIndex();
      total = rt.getTotal();
    }
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Used when rotate tag is closed to increment to next
   * rotate_selection.
   *
   * @return EVAL_PAGE
   */
  public final int doEndTag() throws JspException
  {
    index++;
    if( index >= total )
      index = 0;
    total_set = true;
    rt.setIndex(index);
    return EVAL_PAGE;
  }

  /**
   * Used by a nested rotate_selection tag to determine if
   * it is its turn to display its body.
   *
   * @return true if rotate_selection body should be displayed, false if it isn't its turn
   */
  public final boolean myTurn()
  {
    if( !rt.equals(this) )return rt.myTurn();
    size++;
    if( !total_set ) {
      total++;
      if( total == 1 )
        return true;
    } else {
      if( index == ((size-1)%total) )
        return true;
    }
    return false;
  }

  // Remaining methods are invoked on the original RotateTag
  // which was saved as a script variable

  /**
   * Used by current rotate tag to get rotation state information
   * from a previous iteration of enclosing loop.
   *
   * @return total number of rotate_selection tags found
   */
  public final int getTotal()
  {
    return total;
  }

  /**
   * Used by current rotate tag to get rotation state information
   * from a previous iteration of enclosing loop.
   *
   * @return current index of item to rotate from rotate selections
   */
  public final int getIndex()
  {
    return index;
  }

  /**
   * Used to set the next index to use for rotating selections in
   * the RotateTag script variable.
   */
  public final void setIndex(int v)
  {
    index = v;
  }
}
