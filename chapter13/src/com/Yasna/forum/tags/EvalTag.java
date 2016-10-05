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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * JSP Tag <b>eval</b>, used to evaluate a comparison of two strings or
 * integers.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use by the <b>true</b> or <b>false</b> tags.
 * <p>
 * Valid comparison operators for integers are <b>==</b> (equal), <b>!=</b>
 * (not equal), <b>>=</b> (greater than or equal),
 * <b><=</b> (less than or equal), <b><</b> (less than),
 * and <b>></b> (greater than).
 * <p>
 * Valid comparison operators for strings include all those for an integer
 * plus <b>|=</b> (first string is a prefix of second string),
 * <b>|!</b> (first string is not a prefix of second string),
 * <b>=|</b> (first string is a suffix of second string),
 * and <b>!|</b> (first string is not a suffix of second string).
 * <p>
 * When comparing strings that might include white space, use the forward
 * slash <b>\</b> at the start and end of the string.
 * <p>
 * The values being compared must be separated from the comparison operator
 * by white space.
 * <p>
 * Any JSP tags embedded in the body of the eval tag are processed
 * before the comparison is made.
 * <p>
 * The <b>true</b> and <b>false</b> tags are used to take action based on the
 * result of the eval comparison.
 * <p>
 * Examples:
 * <p>
 * Compare the number of forums to 0.
 * <p><pre>
 * &lt;jf:eval id="num_forums"&gt;
 *   &lt;jsp:getProperty name="req" property="numberOfForums"/&gt; == 0
 * &lt;/jf:eval&gt;
 * <p></pre>
 * Compares the prefix "Re: " to a message subject line.
 * <p><pre>
 * &lt;jf:eval id="sub"&gt;
 *   \Re: \ |= \&lt;jsp:getProperty name="cm" property="subject"/&gt;\
 * &lt;/jf:eval&gt;
 * <p></pre>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;eval&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.EvalTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Compares two values, use the true and false tags to test result of eval. The values can be strings or integers.&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;id&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see TrueTag
 * @see FalseTag
 *
 * @author Glenn Nielsen
 */

public class EvalTag extends BodyTagSupport
{
  // Result of comparison
  private Boolean result = new Boolean(false);

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
   * Read the body of the eval tag to obtain the integer or string values
   * comparison operator, then perform eval comparison.
   *
   * @return SKIP_BODY
   */
  public final int doAfterBody() throws JspException
  {
    // Use the body of the tag as input for the comparison
    BodyContent body = getBodyContent();
    String s = body.getString();
    // Clear the body since we only used it as input for the comparison
    body.clearBody();

    // Parse the body extracting integers or strings and an operator
    List l = new LinkedList();
    int beg = 0,end;
    int len = s.length();
    char v;
    boolean digit = false;
    boolean operator = false;
    boolean slashed = false;
    boolean was_slashed = false;
    boolean negated = false;

    for( end=0; end < len; end++ ) {
      v = s.charAt(end);
//      System.out.println("eval char \"" + v + "\" negated=" + negated);
      if( !slashed && Character.isWhitespace(v) ) {
        if( digit ) {
//          System.out.println("Eval digit \"" + s.substring(beg,end) + "\"");
          l.add((Object)Long.valueOf(s.substring(beg,end)));
        } else if( operator ) {
//          System.out.println("Eval op \"" + s.substring(beg,end) + "\"");
          if( was_slashed ) {
            beg++;
            char ec = s.charAt(end-1);
            if( ec == '\\' )
              end--;
          }
          l.add((Object)new StringBuffer(s.substring(beg,end)));
        }
	was_slashed = digit = negated = operator = false;
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
	  if( v == '\\' )
	    was_slashed = slashed = true;
        } else {
          negated = false;
	  if( v == '\\' )
	    slashed = false;
        }
        operator = true;
	digit = false;
      }
    }

    if( (end - beg) > 0 ) {
      if( digit ) {
//      System.out.println("Eval digit \"" + s.substring(beg,end) + "\"");
        l.add((Object)Long.valueOf(s.substring(beg,end)));
      } else if( operator ) {
//      System.out.println("Eval op \"" + s.substring(beg,end) + "\"");
        if( was_slashed ) {
          beg++;
          char ec = s.charAt(end-1);
          if( ec == '\\' )
            end--;
        }
        l.add((Object)new StringBuffer(s.substring(beg,end)));
      }
    }

    if( l.size() != 3 ) {
      throw new JspException("Jive tag eval must have two values and an operator.");
    }
    // Evaluate the comparison
    result = new Boolean(evaluate(l));
    // Save the script variable for later use by true or false tags
    pageContext.setAttribute(id,result,PageContext.PAGE_SCOPE);
    return SKIP_BODY;
  }

  /**
   * Do the comparison
   *
   * @return String - result of the calculation as a boolean
   */
  private boolean evaluate(List l) throws JspException
  {
    boolean res = false;
    StringBuffer op = null;
    Long v;
    boolean aset = false;
    boolean bset = false;

//    System.out.println("EvalTag a \"" + l.get(0) + "\"");
//    System.out.println("EvalTag op \"" + l.get(1) + "\"");
//    System.out.println("EvalTag b \"" + l.get(2) + "\"");

    op = (StringBuffer)l.get(1);
    if( l.get(0).getClass().getName().equals("java.lang.Long") &&
	l.get(2).getClass().getName().equals("java.lang.Long") ) {
      // Do an integer comparison
      long a = ((Long)l.get(0)).longValue();
      long b = ((Long)l.get(2)).longValue();
      if( op.toString().equals("<") ) {
        res = (a < b);
      } else if( op.toString().equals(">") ) {
        res = (a > b);
      } else if( op.toString().equals("==") ) {
        res = (a == b);
      } else if( op.toString().equals("<=") ) {
        res = (a <= b);
      } else if( op.toString().equals(">=") ) {
        res = (a >= b);
      } else if( op.toString().equals("!=") ) {
        res = (a != b);
      } else {
        throw new JspException("Jive tag eval invalid comparison operator \"" +
	  op + "\".");
      }
    } else {
      // Do a string comparison
      String a = "" + l.get(0);
      String b = "" + l.get(2);
      int comp = a.compareTo(b);
      if( op.toString().equals("<") ) {
        if( comp < 0 )res=true;
      } else if( op.toString().equals(">") ) {
        if( comp > 0 )res=true;
      } else if( op.toString().equals("==") ) {
        if( comp == 0 )res=true;
      } else if( op.toString().equals("<=") ) {
        if( comp <= 0 )res=true;
      } else if( op.toString().equals(">=") ) {
	if( comp >= 0 )res=true;
      } else if( op.toString().equals("!=") ) {
	if( comp != 0 )res=true;
      } else if( op.toString().equals("|=") ) {
        res = b.startsWith(a);
      } else if( op.toString().equals("|!") ) {
        res = !b.startsWith(a);
      } else if( op.toString().equals("=|") ) {
        res = b.endsWith(a);
      } else if( op.toString().equals("!|") ) {
        res = !b.endsWith(a);
      } else {
        throw new JspException("Jive tag eval invalid comparison operator \"" +
          op + "\".");
      }
    }
    return res;
  }

}
