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
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.Query;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * JSP Tag <b>message_loop</b>, used to loop through messages
 * returned by a query in the current forum.
 * <p>
 * Required attribute <b>query</b> is set to the script
 * variable id of a <b>query</b> tag to search for messages
 * based on a query.
 * <p>
 * Must be nested inside a <b>forum</b> tag.
 * <p>
 * During each loop the body of the message_loop tag is processed.
 * <p>
 * The <b>on_entry</b> tag can be used to include content if this is
 * the first time through the loop.
 * <p>
 * The <b>on_exit</b> tag can be used to include content if this is
 * the last time through the loop.
 * <p>
 * The message_loop continues looping until there are no more messages
 * or the users items_per_page limit has been reached. The <b>next_page</b>
 * tag can be used to determine if the message loop ended due to the
 * users items_per_page limit.  Then the <b>next_item</b> tag can be
 * used to get the query portion of an HTML href so that you can setup
 * an href to page to the next list of messages.
 * <p>
 * The message_loop can loop back to the previous list of messages
 * after the message_loop has been paged. The <b>prev_page</b>
 * tag can be used to determine if the message loop has a previous page.
 * Then the <b>prev_item</b> tag can be used to get the query portion
 * of an HTML href so that you can setup an href to page to the previous
 * list of messages.
 * <p>
 * Uses the the following HTTP input parameter
 * <p><ul>
 * <li><b>begin</b> - index of message in current forum query results to start this message_loop at
 * </ul>
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;message_loop&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.MessageLoopTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Implement a loop for a list of messages in a forum&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;query&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see ForumTag
 * @see QueryTag
 * @see OnEntryTag
 * @see NextPageTag
 * @see NextItemTag
 * @see PrevPageTag
 * @see PrevItemTag
 * @see OnExitTag
 * @see YazdRequest
 *
 * @author Glenn Nielsen
 */
public class MessageLoopTag extends BodyTagSupport
  implements Paging, GetNestedMessage
{
  private YazdState js = null;
  private YazdRequest jr = null;
  // int's to keep track of our looping
  private int begin = 0;
  private String query = null;

  private int size = 0;
  // Number of messages being displayed on this page
  private int message_num = 0;
  // Do we have more messages to display after current page?
  private boolean next_page = false;
  // Do we have a previous list of messages to display before current page?
  private boolean prev_page = false;
  // Flag whether this is the first iteration of loop
  private boolean is_entry = true;
  // Flag whether this is the last iteration of loop
  private boolean is_exit = false;
  // Our list of messages to iterate through
  private Iterator mit = null;
  // Current message to display inside message_loop
  private ForumMessage cm = null;

  /**
   * Method called at start of message_loop Tag
   *
   * @return an EVAL_BODY_TAG if message_loop should continue, or a SKIP_BODY if message_loop is completed.
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null )
      throw new JspException("Yazd message_loop tag, could not find yazdUserState");

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Could not find request");

    // Get the Forum this message_loop tag is nested within
    ForumTag ft = null;
    try {
      ft = (ForumTag)TagSupport.findAncestorWithClass(this,ForumTag.class);
    } catch(Exception e) {
    }

    if( ft == null ) {
      throw new JspException("Yazd message_loop tag could not find forum.");
    }

    // Get the HTML input parameter begin to setup where message_loop
    // should start
    ServletRequest req = pageContext.getRequest();
    String tmp = req.getParameter("begin");
    if( tmp != null && tmp.length() > 0 ) {
      try {
        begin = Integer.valueOf(tmp).intValue();
      } catch(NumberFormatException e) {
      }
    }

    QueryTag qt = null;
    try {
      qt = (QueryTag)pageContext.getAttribute(query,PageContext.PAGE_SCOPE);
    } catch(Exception e) {
      throw new JspException(
	"Yazd message tag, could not find query tag with id: " + query);
    }
    Map properties = qt.getQuery();
    Query q = ft.getForum().createQuery();
    if( properties.get("beforeDate") != null ) {
      tmp = (String)properties.get("beforeDate");
      try {
        if( tmp != null && tmp.length() > 0 ) {
          Date date = new Date(Long.valueOf(tmp).longValue());
          q.setBeforeDate(date);
        }
      } catch(Exception e) {
	throw new JspException("Yazd message tag, bad query tag beforeDate");
      }
    }
    if( properties.get("afterDate") != null ) {
      tmp = (String)properties.get("afterDate");
      try {
	if( tmp != null && tmp.length() > 0 ) {
          Date date = new Date(Long.valueOf(tmp).longValue());
          q.setAfterDate(date);
        }
      } catch(Exception e) {
        throw new JspException("Yazd message tag, bad query tag afterDate");
      }
    }
    if( properties.get("queryString") != null ) {
      tmp = (String)properties.get("queryString");
      if( tmp == null ) {
	throw new JspException("Yazd message tag, bad query tag queryString");
      }
      if( tmp.length() > 0 )
        q.setQueryString(tmp);
    }
    if( properties.get("userID") != null ) {
      tmp = (String)properties.get("userID");
      try {
        ProfileManager pm = jr.getProfileManager();
        User user = pm.getUser(tmp);
        q.filterOnUser(user);
      } catch(UserNotFoundException ue) {
//	System.out.println("Search Query User: " + tmp + " not found.");
      } catch(Exception e) {
        throw new JspException("Yazd message tag, bad query tag userID:" +
	  e.getMessage());
      }
    }
    mit = q.results(begin,js.getItemsPerPage());

    if( mit == null ) {
      throw new JspException("Yazd message_loop tag could not get Iterator for forum messages.");
    }

    if( !mit.hasNext() )
      return SKIP_BODY;
    cm = (ForumMessage)mit.next();
    message_num++;
    if( !mit.hasNext() ) {
      is_exit = true;
      if( message_num >= js.getItemsPerPage() )
        next_page = true;
    }
    if( begin > 0 )
      prev_page = true;

    return EVAL_BODY_BUFFERED;
  }

  /**
   * Method called at end of each message_loop Tag Body
   *
   * @return an EVAL_BODY_TAG if message_loop should continue, or a SKIP_BODY if message_loop is completed.
   */
  public final int doAfterBody() throws JspException
  {
    is_entry = prev_page = false;
    message_num++;
    if( !mit.hasNext() ) {
      return SKIP_BODY;
    }
    cm = (ForumMessage)mit.next();
    if( !mit.hasNext() ) {
      is_exit = true;
      if( message_num >= js.getItemsPerPage() )
        next_page = true;
    }
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Method called at end of message_loop Tag
   * @return EVAL_PAGE
   */
  public final int doEndTag() throws JspException
  {
    try
    {
      if(bodyContent != null)
        bodyContent.writeOut(bodyContent.getEnclosingWriter());
    } catch(java.io.IOException e)
    {
      throw new JspException("IO Error: " + e.getMessage());
    }
    return EVAL_PAGE;
  }

  /**
   * Set the forum message query to use
   */
  public final void setQuery(String qry)
  {
    query = qry;
  }

  /**
   * Used by <b>message</b> tag to get the current ForumMessage
   *
   * @return ForumMessage
   */
  public final ForumMessage getMessage()
  {
    return cm;
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

  /**
   * Used by <b>on_entry</b> tag to detect if this is the first iteration
   * of the loop.
   *
   * @return true or false
   */
  public final boolean isEntry()
  {
    return is_entry;
  }

  /**
   * Used by <b>on_exit</b> tag to detect if this is the last iteration
   * of the loop.
   *
   * @return true or false
   */
  public final boolean isExit()
  {
    return is_exit;
  }

  /**
   * Used by <b>next_page</b> tag to detect if message listing could continue
   * on another page.
   *
   * @return true or false
   */
  public final boolean isNextPage()
  {
    return next_page;
  }

  /**
   * Used by <b>prev_page</b> tag to detect if message listing
   * has a previous page.
   *
   * @return true or false
   */
  public final boolean isPrevPage()
  {
    return prev_page;
  }

  /**
   * Used by <b>next_item</b> tag to return query portion of an HTML GET href
   * for paging to next list of messages.
   *
   * @return String - query portion of an HTML GET href
   */
  public final String nextItem()
  {
    return "begin=" + (begin+message_num);
  }

  /**
   * Used by <b>prev_item</b> tag to return query portion of an HTML GET href
   * for paging to previous list of messages.
   *
   * @return String - query portion of an HTML GET href
   */
  public final String prevItem()
  {
    int prev = begin - js.getItemsPerPage();
    if( prev < 0 )
      prev = 0;
    return "begin=" + prev;
  }

}
