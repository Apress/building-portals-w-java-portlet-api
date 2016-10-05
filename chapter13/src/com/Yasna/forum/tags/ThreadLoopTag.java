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

import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.ForumThread;

/**
 * JSP Tag <b>thread_loop</b>, used to loop through available threads
 * in the current forum.
 * <p>
 * Optional attribute <b>begin</b> can be set to the number at
 * which the thread_loop should start.
 * <p>
 * Optional attribute <b>end</b> can be set to the number at
 * which the thread_loop should end.
 * <p>
 * Must be nested inside a <b>forum</b> tag.
 * <p>
 * During each loop the body of the thread_loop tag is processed.
 * <p>
 * The <b>on_entry</b> tag can be used to include content if this is
 * the first time through the loop.
 * <p>
 * The <b>on_exit</b> tag can be used to include content if this is
 * the last time through the loop.
 * <p>
 * The thread_loop continues looping until there are no more threads
 * or the users items_per_page limit has been reached. The <b>next_page</b>
 * tag can be used to determine if the thread loop ended due to the
 * users items_per_page limit.  Then the <b>next_item</b> tag can be
 * used to get the query portion of an HTML href so that you can setup
 * an href to page to the next list of threads.
 * <p>
 * The thread_loop can loop back to the previous list of threads
 * after the thread_loop has been paged. The <b>prev_page</b>
 * tag can be used to determine if the thread loop has a previous page.
 * Then the <b>prev_item</b> tag can be used to get the query portion
 * of an HTML href so that you can setup an href to page to the previous
 * list of threads.
 * <p>
 * Uses the the following HTTP input parameter
 * <p><ul>
 * <li><b>begin</b> - index of thread in current forum to start this thread_loop at
 * </ul>
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;thread_loop&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.ThreadLoopTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Implement a loop for a list of threads in a forum&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;begin&lt;/name&gt;
 *    &lt;required&gt;false&lt;/required&gt;
 *    &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;end&lt;/name&gt;
 *    &lt;required&gt;false&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see ForumTag
 * @see ThreadTag
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
public class ThreadLoopTag extends BodyTagSupport implements Paging
{
  private YazdState js = null;
  private YazdRequest jr = null;
  // int's to keep track of our looping
  private int begin = 0;
  private int end = -1;

  private int size = 0;
  // Number of threads being displayed on this page
  private int thread_num = 0;
  // Do we have more threads to display after current page?
  private boolean next_page = false;
  // Do we have a previous list of threads to display before current page?
  private boolean prev_page = false;
  // Flag whether this is the first iteration of loop
  private boolean is_entry = true;
  // Flag whether this is the last iteration of loop
  private boolean is_exit = false;
  // Our list of threads to iterate through
  private Iterator thrit = null;
  // Current thread to display inside thread_loop
  private ForumThread thr = null;

  /**
   * Method called at start of thread_loop Tag
   *
   * @return an EVAL_BODY_TAG if thread_loop should continue, or a SKIP_BODY if thread_loop is completed.
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null )
      throw new JspException("Yazd thread_loop tag, could not find yazdUserState");

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Could not find request");

    // Get the Forum this thread_loop tag is nested within
    ForumTag ft = null;
    try {
      ft = (ForumTag)TagSupport.findAncestorWithClass(this,ForumTag.class);
    } catch(Exception e) {
    }

    if( ft == null ) {
      throw new JspException("Yazd thread_loop tag could not find forum.");
    }

    // Get the HTML input parameter begin to setup where thread_loop
    // should start
    ServletRequest req = pageContext.getRequest();
    String tmp = req.getParameter("begin");
    if( tmp != null && tmp.length() > 0 ) {
      try {
        begin = Integer.valueOf(tmp).intValue();
      } catch(NumberFormatException e) {
      }
    }

    // Get the list of threads and setup counters for the thread_loop
    size = ft.getForum().getThreadCount();

    if( end < 0 || end > size )
      end = size;
    if( (begin >= end) )
      return SKIP_BODY;
    if( (end - begin) > js.getItemsPerPage() )
      end = begin + js.getItemsPerPage();

    thrit = ft.getForum().threads(begin,end-begin);
    if( thrit == null ) {
      throw new JspException("Yazd thread_loop tag could not get Iterator for forum.");
    }

    if( !thrit.hasNext() )
      return SKIP_BODY;
    thr = (ForumThread)thrit.next();
    thread_num++;
    if( !thrit.hasNext() ) {
      is_exit = true;
      if( end < size )
        next_page = true;
    }
    if( begin > 0 )
      prev_page = true;

    return EVAL_BODY_BUFFERED;
  }

  /**
   * Method called at end of each thread_loop Tag Body
   *
   * @return an EVAL_BODY_TAG if thread_loop should continue, or a SKIP_BODY if thread_loop is completed.
   */
  public final int doAfterBody() throws JspException
  {
    is_entry = prev_page = false;
    thread_num++;
    if( !thrit.hasNext() ) {
      return SKIP_BODY;
    }
    thr = (ForumThread)thrit.next();
    if( !thrit.hasNext() ) {
      is_exit = true;
      if( end < size )
        next_page = true;
    }
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Method called at end of thread_loop Tag
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
   * Set the beginning index into an enumerated list
   */
  public final void setBegin(String beg)
  {
    begin = Integer.valueOf(beg).intValue();
  }

  /**
   * Set the ending index into an enumerated list
   */
  public final void setEnd(String last)
  {
    end = Integer.valueOf(last).intValue();
  }

  /**
   * Used by <b>thread</b> tag to get the current ForumThread
   *
   * @return Current ForumThread
   */
  public final ForumThread getThread()
  {
    return thr;
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
   * Used by <b>next_page</b> tag to detect if thread listing could continue
   * on another page.
   *
   * @return true or false
   */
  public final boolean isNextPage()
  {
    return next_page;
  }

  /**
   * Used by <b>prev_page</b> tag to detect if thread listing
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
   * for paging to next list of threads.
   *
   * @return String - query portion of an HTML GET href
   */
  public final String nextItem()
  {
    return "begin=" + (begin+thread_num);
  }

  /**
   * Used by <b>prev_item</b> tag to return query portion of an HTML GET href
   * for paging to previous list of threads.
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
