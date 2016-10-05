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
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.Yasna.forum.Forum;

/**
 * JSP Tag <b>forum_loop</b>, used to loop through available forums.
 * <p>
 * Optional attribute <b>begin</b> can be set to the number at
 * which the forum_loop should start.
 * <p>
 * Optional attribute <b>end</b> can be set to the number at
 * which the forum_loop should end.
 * <p>
 * Optional attribute <b>match</b> can be set to the <b>id</b> of
 * a <b>match</b> tag to select only those forums that match the
 * forum extended properties specified by the <b>match</b> tag.
 * <p>
 * During each loop the body of the forum_loop tag is processed.
 * <p>
 * The <b>on_entry</b> tag can be used to include content if this is
 * the first time through the loop.
 * <p>
 * The <b>on_exit</b> tag can be used to include content if this is
 * the last time through the loop.
 * <p>
 * The forum_loop continues looping until there are no more forums
 * or the users items_per_page limit has been reached. The <b>next_page</b>
 * tag can be used to determine if the forum loop ended due to the
 * users items_per_page limit.  Then the <b>next_item</b> tag can be
 * used to get the query portion of an HTML href so that you can setup
 * an href to page to the next list of forums.
 * <p>
 * The forum_loop can loop back to the previous list of forums
 * after the forum_loop has been paged. The <b>prev_page</b>
 * tag can be used to determine if the forum loop has a previous page.
 * Then the <b>prev_item</b> tag can be used to get the query portion
 * of an HTML href so that you can setup an href to page to the previous
 * list of forums.
 * <p>
 * Uses the the following HTTP input parameter
 * <p><ul>
 * <li><b>begin</b> - index of forum to start this forum_loop at
 * </ul>
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;forum_loop&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.ForumLoopTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Implement a loop for a list of forums&lt;/info&gt;
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
 *  &lt;attribute&gt;
 *    &lt;name&gt;match&lt;/name&gt;
 *    &lt;required&gt;false&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see ForumTag
 * @see MatchTag
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
public class ForumLoopTag extends BodyTagSupport implements Paging
{

  private YazdState js = null;
  private YazdRequest jr = null;
  // int's to keep track of our looping
  private int begin = 0;
  private int end = -1;
  private int index = 0;
  // Number of forums being displayed on this page
  private int forum_num = 0;
  // Do we have more forums to display after current page?
  private boolean next_page = false;
  // Do we have a previous list of forums that could be displayed
  private boolean prev_page = false;
  // Flag whether this is the first iteration of loop
  private boolean is_entry = true;
  // Flag whether this is the last iteration of loop
  private boolean is_exit = false;
  // Our list of forums to iterate through
  private List forums = null;
  // Current forum to display inside forum_loop
  private Forum cf = null;
  // Hash of forum extended properties to match on when selecting forums
  private Map match = new HashMap();

  /**
   * Method called at start of forum_loop Tag
   *
   * @return an EVAL_BODY_TAG if forum_loop should continue, or a SKIP_BODY if forum_loop is completed.
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null )
      throw new JspException("Yazd forum_loop tag, could not find yazdUserState");

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Could not find request");

    // Get the HTML input parameter begin to setup where forum_loop
    // should start
    ServletRequest req = pageContext.getRequest();
    String tmp = req.getParameter("begin");
    if( tmp != null && tmp.length() > 0 ) {
      try {
        begin = index = Integer.valueOf(tmp).intValue();
      } catch(NumberFormatException e) {
      }
    }

    // Get the list of forums and setup counters for the forum_loop
    forums = jr.getForums(match);
    int size = forums.size();
    if( end < 0 )
      end = size-1;
    if( index > end )
      return SKIP_BODY;
    cf = (Forum)forums.get(index);
    if( cf == null )
      return SKIP_BODY;
    if( begin > 0 )
      prev_page = true;
    if( index < end && (forum_num+1) >= js.getItemsPerPage() )
      next_page = true;
    if( next_page || index >= end )
      is_exit = true;
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Method called at end of each forum_loop Tag Body
   *
   * @return an EVAL_BODY_TAG if forum_loop should continue, or a SKIP_BODY if f
orum_loop is completed.
   */
  public final int doAfterBody() throws JspException
  {
    is_entry = prev_page = false;
    index++;
    forum_num++;
    if( index > end )
      return SKIP_BODY;
    if( forum_num >= js.getItemsPerPage() )
      return SKIP_BODY;
    cf = (Forum)forums.get(index);
    if( cf == null )
      return SKIP_BODY;
    // See if we need to page next time
    if( index < end && (forum_num+1) >= js.getItemsPerPage() )
      next_page = true;
    if( next_page || index >= end )
      is_exit = true;
    return EVAL_BODY_BUFFERED;
  }

  /**
   * Method called at end of forum_loop Tag
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
    try {
      index = begin = Integer.valueOf(beg).intValue();
    } catch(NumberFormatException e) {
    }
  }

  /**
   * Set the ending index into an enumerated list
   */
  public final void setEnd(String last)
  {
    end = Integer.valueOf(last).intValue();
  }

  /**
   * Set the <b>id</b> of a <b>match</b> tag
   */
  public final void setMatch(String mtch)
  {
    MatchTag fmt =
      (MatchTag)pageContext.getAttribute(mtch,PageContext.PAGE_SCOPE);
    if( fmt != null && fmt.getMatch() != null )
      match = fmt.getMatch();
  }

  /**
   * Used by the <b>forum</b> tag to get any match criteria
   *
   * @return Map of forum extended property/value pairs
   */
  public final Map getMatch()
  {
    return match;
  }

  /**
   * Used by <b>forum</b> tag to get the current Forum
   *
   * @return Current Forum
   */
  public final Forum getForum()
  {
    return cf;
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
   * Used by <b>next_page</b> tag to detect if forum listing could continue
   * on another page.
   *
   * @return true or false
   */
  public final boolean isNextPage()
  {
    return next_page;
  }

  /**
   * Used by <b>prev_page</b> tag to detect if forum listing
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
   * for paging to next list of forums.
   *
   * @return String - query portion of an HTML GET href
   */
  public final String nextItem()
  {
    return "begin=" + (index+1);
  }

  /**
   * Used by <b>prev_item</b> tag to return query portion of an HTML GET href
   * for paging to previous list of forums.
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
