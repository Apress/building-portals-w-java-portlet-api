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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumFactory;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumNotFoundException;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.ForumThreadNotFoundException;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.util.StringUtils;

 /**
 * Maintains Yazd state information for the current request.
 * <p>
 * Created by the <b>authorize</b> tag and is the class that is tied
 * to the <b>authorize</b> script variable to provide access by JSP
 * page authors to request data using &lt;jsp:getProperty/&gt;.
 * <p>
 * Provides methods required for maintaining the request state information
 * for use by other tags.

 * @see AuthorizeTag
 * @see YazdState
 *
 * @author Glenn Nielsen
 */

public class YazdRequest
{
  private YazdState js = null;
  private ForumFactory ff = null;
  private PageContext pageContext = null;
  private ProfileManager pm = null;
  private List forums = null;
  private List error = new java.util.LinkedList();

  /**
   * YazdRequest frequently needs to use the YazdState,
   * so just set it once.
   *
   * @param YazdState
   */
  public final void setYazdState(YazdState j)
  {
    js = j;
  }

  /**
   * Get the list of Forums user is authorized to view.
   * Used by the <b>forum</b> and <b>forum_loop</b> tags.
   *
   * @return a List of Forums
   */

  public final List getForums(Map match) throws JspException
  {
    checkYazdState();
    if( forums == null ) {
      forums = new java.util.LinkedList();
      Iterator vf = ff.forums();
      while( vf.hasNext() ) {
        Forum forum = (Forum)vf.next();
        boolean match_made = true;
        String prop;
        String value;
        for(Iterator it=match.keySet().iterator();it.hasNext();) {
          prop = (String)it.next();
          if( (value = forum.getProperty(prop)) == null ) {
            match_made = false;
            break;
          }
          if( !value.equals((String)match.get(prop)) ) {
            match_made = false;
            break;
          }
        }
        if( match_made ) {
	  forums.add( forum );
        }
      }
    }
    return forums;
  }
/*
  public final List getForums(Map match) throws JspException
  {
    checkYazdState();
    if( forums == null ) {
      forums = new java.util.LinkedList();
      Iterator vf = ff.forums(match); Fix Me
      while( vf.hasNext() ) {
           forums.add( vf.next() );
      }
    }
    return forums;
  }
*/


  /**
   * Gets the current forum user is viewing by current thread or
   * defaults to first forum.
   *
   * Used by the <b>forum</b> and <b>thread</b> tags.
   *
   * @return currently selected Forum
   */
  public final Forum getForum() throws JspException
  {
    Forum cf = null;
    checkYazdState();

    int threadid = js.getForumID();
    if( threadid < 0 ) {
      java.util.LinkedList forums = (java.util.LinkedList)getForums(new HashMap());
      if( forums.size() > 0 ) {
        cf = (Forum)forums.get(0);
        js.setForumID(cf.getID());
      }
    } else {
      try {
        cf = ff.getForum(threadid);
      } catch(ForumNotFoundException e) {
        throw new JspException("Yazd forum tag, could not find forum with ID:" +
          threadid);
      } catch(UnauthorizedException e) {
        throw new JspException("Yazd forum tag, user not authorized for forum with ID:" +
          threadid);
      }
    }
    if( cf == null ) {
      throw new JspException("Yazd forum tag, no forums available for user.");
    }

    return cf;
  }

  /**
   * Gets the current forum user is viewing by current thread or
   * defaults to first thread in current forum.
   *
   * Used by the <b>thread</b> tag.
   *
   * @return currently selected Thread
   */
  public final ForumThread getThread() throws JspException
  {
    Forum cf = getForum();
    ForumThread ct = null;

    int threadid = js.getThreadID();
    if( threadid < 0 ) {
      Iterator threads = cf.threads(0,1);
      if( threads.hasNext() ) {
        ct = (ForumThread)threads.next();
      }
    } else {
      try {
        ct = cf.getThread(threadid);
      } catch(ForumThreadNotFoundException e) {
        throw new JspException("Could not find thread with ID:" + threadid);
      }
    }
    if( ct == null ) {
      throw new JspException("Yazd thread tag, no threads available for user.");
    }

    return ct;
  }

  /**
   * Gets the current message user is viewing by current message or
   * defaults to first message in current thread.
   *
   * Used by the <b>message</b> tag.
   *
   * @return currently selected Message
   */
  public final ForumMessage getMessage() throws JspException
  {
    ForumThread ct = getThread();
    ForumMessage cm = null;

    int messageid = js.getMessageID();
    try {
      if( messageid < 0 ) {
        cm = ct.getRootMessage();
      } else {
        cm = ct.getMessage(messageid);
      }
    } catch(ForumMessageNotFoundException e) {
        throw new JspException("Could not find message with messageID: " +
           messageid);
    }
    if( cm == null ) {
      throw new JspException("Yazd message tag, no messages available for user.");
    }

    return cm;
  }

  /**
   * NumberOfForums property which can be obtained by the JSP page
   * using the <b>authorize</b> script variable with
   * &lt;jsp:getProperty name=<i>"id"</i> property="numberOfForums"/&gt;
   *
   * @return number of Forums user is authorized to view
   */
  public final String getNumberOfForums() throws JspException
  {
    return "" + getForumFactory().getForumCount();
  }

  /**
   * Date and time of users last visit (integer) property which can be
   * obtained by the JSP page using the <b>authorize</b> script variable with
   * &lt;jsp:getProperty name=<i>"id"</i> property="lastVisit"/&gt;
   *
   * @return date and time of users last visit
   */
  public final String getLastVisit()
  {
    return "" + js.getLastVisit().getTime();
  }

  /**
   * Method used by many tags to add an error to the list of errors
   * which can be returned using the <b>error_loop</b> tag.
   */
  public final void addError(String err)
  {
    error.add((Object)new StringBuffer(err));
  }

  /**
   * Method used by <b>if_error</b> and <b>error_loop</b> tags to
   * get the list of user errors.
   *
   * @return a List of user errors
   */
  public final List getErrorList()
  {
    return error;
  }

  /**
   * Used by the JSP page to set the error for the current request
   * using the <b>authorize</b> script variable with
   * &lt;jsp:setProperty name=<i>"id"</i> property="error" value="<i>string</i>"/&gt;
   */
  public final void setError(String err)
  {
    error.clear();
    error.add((Object)new StringBuffer(err));
  }

  /**
   * Test whether current user is anonymous.
   *
   * @return boolean - true or false
   */
  public final boolean isAnonymous()
  {
    if( js.getLoggedIn() )
      return false;
    return true;
  }

  /**
   * MessageDepth property which can be obtained by the JSP page
   * using the <b>authorize</b> script variable with
   * &lt;jsp:getProperty name=<i>"id"</i> property="messageDepth"/&gt;
   *
   * @return user message_depth display preference
   */
  public final String getMessageDepth()
  {
    return "" + js.getMessageDepth();
  }

  /**
   * Used by the JSP page to set the message_depth display preferences of
   * user for current session using the <b>authorize</b> script variable with
   * &lt;jsp:setProperty name=<i>"id"</i> property="messageDepth" value="<i>integer</i>"/&gt;
   */
  public final void setMessageDepth(String s)
  {
    try {
      js.setMessageDepth(Integer.valueOf(s).intValue());
    } catch(NumberFormatException e) {
    }
  }

  /**
   * ThreadDepth property which can be obtained by the JSP page
   * using the <b>authorize</b> script variable with
   * &lt;jsp:getProperty name=<i>"id"</i> property="threadDepth"/&gt;
   *
   * @return user thread_depth display preference
   */
  public final String getThreadDepth()
  {
    return "" + js.getThreadDepth();
  }

  /**
   * Used by the JSP page to set the thread_depth display preferences of
   * user for current session using the <b>authorize</b> script variable with
   * &lt;jsp:setProperty name=<i>"id"</i> property="theadDepth" value="<i>integer</i>"/&gt;
   */
  public final void setThreadDepth(String s)
  {
    try {
      js.setThreadDepth(Integer.valueOf(s).intValue());
    } catch(NumberFormatException e) {
    }
  }

  /**
   * ItemsPerPage property which can be obtained by the JSP page
   * using the <b>authorize</b> script variable with
   * &lt;jsp:getProperty name=<i>"id"</i> property="itemsPerPage"/&gt;
   *
   * @return user items_per_page display preference
   */
  public final String getItemsPerPage()
  {
    return "" + js.getItemsPerPage();
  }

  /**
   * Used by the JSP page to set the items_per_page display preferences of
   * user for current session using the <b>authorize</b> script variable with
   * &lt;jsp:setProperty name=<i>"id"</i> property="itemsPerPage" value="<i>integer</i>"/&gt;
   */
  public final void setItemsPerPage(String s)
  {
    try {
      js.setItemsPerPage(Integer.valueOf(s).intValue());
    } catch(NumberFormatException e) {
    }
  }

  /**
   * Generate a random password.
   *
   * @return an 8 character password
   */
  public final String GeneratePassword()
  {
    return StringUtils.randomString(8);
  }

  /**
   * Method used by many tags to get the ForumFactory.
   *
   * @return ForumFactory
   */
  public final ForumFactory getForumFactory() throws JspException
  {
    checkYazdState();
    return ff;
  }

  /**
   * Method used by many tags to get the User ProfileManager.
   *
   * @return User ProfileManager
   */
  public final ProfileManager getProfileManager() throws JspException
  {
    checkYazdState();
    if( pm == null )
      pm = ff.getProfileManager();
    if( pm == null )
      throw new JspException("Could not get ProfileManager.");
    return pm;
  }

  /**
   * Method used by many YazdRequest methods to make sure
   * the necessary resources are available.
   *
   * @throws JspException if a resource can not be found
   */
  private void checkYazdState() throws JspException
  {
    if( js == null || js.getAuthorization() == null )
      throw new JspException("YazdState not set for YazdRequest");
    if( ff == null )
      ff = ForumFactory.getInstance(js.getAuthorization());
    if( ff == null )
      throw new JspException("Could not get ForumFactory.");
  }
}
