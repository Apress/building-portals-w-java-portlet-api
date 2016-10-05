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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumPermissions;
import com.Yasna.forum.UnauthorizedException;

/**
 * JSP Tag <b>forum</b>, used to get information about current forum.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve Forum data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * If optional attribute <b>trackVisits</b>="true", the date of the
 * users last visit to the forum will be saved when the user logs out
 * or their current session ends.
 * <p>
 * Optional attribute <b>match</b> can be set to the <b>id</b> of
 * a <b>match</b> tag to select only those forums that match the
 * forum extended properties specified by the <b>match</b> tag.
 * <p>
 * Gets the current forum from within a <b>forum_loop</b> tag or
 * the current forum from the user state information.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;forum&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.ForumTag&lt;/tagclass&gt;
 *  &lt;teiclass&gt;com.Yasna.forum.tags.ForumTEI&lt;/teiclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Get the current forum data&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;id&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;trackVisits&lt;/name&gt;
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
 * @see ForumLoopTag
 * @see MatchTag
 * @see YazdProperty
 * @see GetYazdPropertyTag
 * @see SetYazdPropertyTag
 * @see YazdRequest
 *
 * @author Glenn Nielsen
 */

public class ForumTag extends TagSupport implements YazdProperty,
	ChangeForum, NewMessages
{
  private YazdState js = null;
  private YazdRequest jr = null;
  private Forum cf = null;
  // ID's of previous and next forum for use as a navigation aide
  private int prevForumID = -1;
  private int nextForumID = -1;
  // Flag whether date and time of last visit to this forum should be saved
  private boolean trackVisits = false;
  // Hash of forum extended properties to match on when selecting forums
  private Map match = new HashMap();

  /**
   * Method called at start of forum Tag to get Forum
   *
   * @return EVAL_BODY_INCLUDE if there is a forum or SKIP_BODY if there is not a forum to view
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd forum tag could not get yazd state.");
    }

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Yazd forum tag, could not find request");

   // See if we are nested inside a forum_loop tag
   ForumLoopTag fl = null;
    try {
      fl = (ForumLoopTag)TagSupport.findAncestorWithClass(this,ForumLoopTag.class);
    } catch(Exception e) {
    }

    if( fl != null ) {
      cf = fl.getForum();
      if( fl.getMatch() != null )
        match = fl.getMatch();
    } else {
      // Get the forum from user state information
      cf = jr.getForum();
    }

    if( cf == null || !cf.hasPermission(ForumPermissions.READ) )
      return SKIP_BODY;

    // Get the next and previous forum ID's just in case they are needed
    List forums = jr.getForums(match);
    int nextid;
    int previd = -1;
    int size = forums.size();
    for( int i = 0; i < size; i++ ) {
      nextid = ((Forum)forums.get(i)).getID();
      if( nextid == cf.getID() ) {
        prevForumID = previd;
	if( (i+1) < size )
	  nextForumID = ((Forum)forums.get(i+1)).getID();
	break;
      }
      previd = nextid;
    }

    // Set the date and time of last visit to this forum
    if( trackVisits ) {
      js.setNextForumVisitDate(pageContext,cf.getID());
    }

    // Save the script variable so JSP author can access forum data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Set a flag indicating whether date and time of users last visit
   * to this forum should be saved (Optional attribute).
   */
  public final void setTrackVisits(String a)
  {
    if( a.equals("true") )
      trackVisits=true;
  }

  /**
   * Forum ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="forumid"/&gt;
   *
   * @return String - forum ID
   */
  public final String getForumid()
  {
    return "" + cf.getID();
  }

  /**
   * Forum Description property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="description"/&gt;
   *
   * @return String - forum Description
   */
  public final String getDescription()
  {
    return cf.getDescription();
  }

  /**
   * Forum MessageCount property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="messagecount"/&gt;
   *
   * @return String - forum MessageCount
   */
  public final String getMessagecount()
  {
    return "" + cf.getMessageCount();
  }

  /**
   * Forum ThreadCount property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="threadcount"/&gt;
   *
   * @return String - forum ThreadCount
   */
  public final String getThreadcount()
  {
    return "" + cf.getThreadCount();
  }

  /**
   * Forum Name property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="name"/&gt;
   *
   * @return String - forum Name
   */
  public final String getName()
  {
    return cf.getName();
  }

  /**
   * Date and time of Forum CreationDate (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="creationDate"/&gt;
   *
   * @return date and time of Forum CreationDate as an integer
   */
  public final String getCreationDate()
  {
    return "" + cf.getCreationDate().getTime();
  }

  /**
   * Date and time of Forum ModifiedDate (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="modifiedDate"/&gt;
   *
   * @return date and time of Forum ModifiedDate as an integer
   */
  public final String getModifiedDate()
  {
    return "" + cf.getModifiedDate().getTime();
  }

  /**
   * Date and time Forum was last visited (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="lastForumVisitDate"/&gt;
   *
   * @return date and time of lastForumVisitDate as an integer
   */
  public final String getLastForumVisitDate()
  {
    return "" + js.getLastForumVisitDate(cf,jr).getTime();
  }

  /**
   * Next Forum ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="nextForumid"/&gt;
   *
   * @return String - next forum ID
   */
  public final String getNextForumid()
  {
    return "" + nextForumID;
  }

  /**
   * Previous Forum ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="prevForumid"/&gt;
   *
   * @return String - previous forum ID
   */
  public final String getPrevForumid()
  {
    return "" + prevForumID;
  }

  /**
   * Used by ThreadLoopTag and ThreadTag to get current Forum
   *
   * @return Forum
   */
  public final Forum getForum()
  {
    return cf;
  }

  /**
   * Method used by the getYazdProperty tag to get an extended Forum
   * property from the forum tag script variable.
   *
   * @return String - value of the property
   */
  public final String getProperty(String name)
  {
    String tmp = cf.getProperty(name);
    if( tmp != null )return tmp;
    return "";
  }

  /**
   * Method used by the setYazdProperty tag to set an extended Forum
   * property from the forum tag script variable.
   */
  public final void setProperty(String name, String value)
  {
    try {
      cf.setProperty(name,value);
    } catch(UnauthorizedException ue) {
    }
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
   *  Set a different forum
   */
  public final void changeForum(Forum apf)
  {
    cf = apf;
  }

  /**
   * Determine if forum has new messages since users last visit.
   *
   * @return boolean - true or false
   */
  public final boolean newMessages()
  {
    if( js.getLastForumVisitDate(cf,jr).getTime() <
        cf.getModifiedDate().getTime() )
		return true;
    return false;
  }

  /**
   * Determine if there is a forum preceding this one
   *
   * @return boolean - true or false
   */
  public final boolean prevForum()
  {
    if( prevForumID > 0 )
      return true;
    return false;
  }

  /**
   * Determine if there is a forum following this one
   *
   * @return boolean - true or false
   */
  public final boolean nextForum()
  {
    if( nextForumID > 0 )
      return true;
    return false;
  }

  /**
   * Remove the script variable after forum tag closed out
   */
  public final void release()
  {
    if( id != null && id.length() > 0 )
      pageContext.removeAttribute(id,PageContext.PAGE_SCOPE);
  }

}
