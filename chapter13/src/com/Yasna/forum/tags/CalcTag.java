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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * JSP Tag <b>calc</b>, used to calculate and return an integer value.
 * <p>
 * Calculates an integer value based on the integer values and operators
 * found in the body of the calc tag, then returns the value calculated
 * to the JSP page.
 * <p>
 * Valid operators are <b>+</b>, <b>-</b>, <b>*</b>, and <b>/</b>.
 * <p>
 * There must be white space between integers and operators.
 * <p>
 * Any JSP tags embedded in the body of the calc tag are processed
 * before the calculation is made.
 * <p>
 * Example:
 * <p>
 * &lt;jf:calc&gt; &lt;jf:current_depth/&gt; * 4 &lt;/jf:calc&gt;
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;calc&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.CalcTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Calculate an expression&lt;/info&gt;
 * </pre>
 *
 * @author Glenn Nielsen
 */

public class CalcTag extends BodyTagSupport
{
  // Result of integer calculation
  long res = 0;

  /**
   * Allow the body of the calc tag to be processed.
   *
   * @return EVAL_BODY_TAG
   */
  public final int doStartTag() throws JspException
  {
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Read the body of the calc tag to obtain the integer values and operators,
   * then perform calculation.
   *
   * @return SKIP_BODY
   */
  public final int doAfterBody() throws JspException
  {
    // Use the body of the tag as input for the calculation
    BodyContent body = getBodyContent();
    String s = body.getString();
    // Clear the body since we will output only the result of the calculation
    body.clearBody();

    // Create a list of integers and operators
    List l = new LinkedList();
    int beg = 0,end=0;
    int len = s.length();
    char v;
    boolean digit = false;
    boolean operator = false;
    boolean negated = false;

    // Parse the body extracting integers and operators
    for( end=0; end < len; end++ ) {
      v = s.charAt(end);
//      System.out.println("calc char \"" + v + "\" negated=" + negated);
      if( Character.isWhitespace(v) ) {
        if( digit ) {
//          System.out.println("Calc digit \"" + s.substring(beg,end) + "\"");
          l.add((Object)Long.valueOf(s.substring(beg,end)));
        } else if( operator ) {
//          System.out.println("Calc op \"" + s.substring(beg,end) + "\"");
          l.add((Object)new StringBuffer(s.substring(beg,end)));
        }
        digit = negated = operator = false;
        beg = end;
      } else if( Character.isDigit(v) ) {
        if( !operator || negated ) {
          if( !digit && !negated )beg = end;
          digit = true;
          operator = negated = false;
        }
      } else {
        if( !operator && !digit) {
          beg = end;
          if( v == '-' )
            negated = true;
        } else {
          negated = false;
        }
        operator = true;
        digit = false;
      }
    }

    if( (end - beg) > 0 ) {
      if( digit ) {
//	System.out.println("Calc digit \"" + s.substring(beg,end) + "\"");
        l.add((Object)Long.valueOf(s.substring(beg,end)));
      } else if( operator ) {
//	System.out.println("Calc op \"" + s.substring(beg,end) + "\"");
        l.add((Object)new StringBuffer(s.substring(beg,end)));
      }
    }
    // Calculate the result
    res = calculate((ListIterator)l.iterator());
//    System.out.println("calc result: " + res);
    return SKIP_BODY;
  }

  /**
   * Output the result of the calculation
   *
   * @return EVAL_PAGE
   */

  public final int doEndTag() throws JspException
  {
    try {
      pageContext.getOut().write("" + res);
    } catch(Exception e) {
      throw new JspException("IO Error: " + e.getMessage());
    }
    return EVAL_PAGE;
  }

  /**
   * Do the calculation
   *
   * @return String - result of the calculation as an integer
   */
  private long calculate(ListIterator li) throws JspException
  {
    long a = 0;
    long res = 0;
    StringBuffer sb = null;
    Object o;
    Long v;
    boolean aset = false;

    // Loop through the list of integers and operators
    // performing calculations
    while(li.hasNext()) {
      o = li.next();
      if( o.getClass().getName().equals("java.lang.Long") ) {
	v = (Long)o;
//	System.out.println("Calc value: " + v.longValue());
	if( aset ) {
	  if( sb == null ) {
	    throw new JspException("Jive tag calc missing operator.");
	  }
//	  System.out.println("Calc operator: " + sb);
	  if( sb.toString().equals("*") )
	    a *= v.longValue();
	  else if( sb.toString().equals("+") )
	    a += v.longValue();
          else if( sb.toString().equals("/") )
            a /= v.longValue();
          else if( sb.toString().equals("-") )
            a -= v.longValue();
	  else
	    throw new JspException("Jive tag calc invalid operator \"" +
		sb + "\"");
	} else {
	  aset = true;
	  a = v.longValue();
	}
      } else {
	sb = (StringBuffer)o;
        if( !aset ) {
	  throw new JspException("Jive tag calc must start with a number.");
	}
      }
    }
    return a;
  }

}
