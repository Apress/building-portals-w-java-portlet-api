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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumFactory;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

 /**
 * Maintains Yazd state information for the user session.
 * Provides methods required for maintaining the
 * user session state information.
 *
 * @see YazdRequest
 *
 * @author Glenn Nielsen
 */

public class YazdState implements HttpSessionBindingListener
{
  private Authorization authorization = null;
  // Keep track of user display preferences
  private int message_depth = 1;
  private int thread_depth = 3;
  private int items_per_page = 25;
  // Keep track of currently selected forum, thread, and message
  // for forum navigation
  private int forumID = -1;
  private int threadID = -1;
  private int messageID = -1;
  private int parentID = -1;
  // Flag whether the user is logged in
  private boolean logged_in = false;
  // Date of users last session
  private Date lastVisit = null;
  // Date of users last use of current session
  private Date nextVisit = null;
  // Variables for tracking last visist to a Forum
  private Map nextForumVisit = new HashMap();
  private Map lastForumVisit = new HashMap();
  private Map lastForumVisitCookie = new HashMap();
  // Variables for tracking previous messages page
  private Map prevMessages = new HashMap();
  // And some constants for use with extended user properties
  public final static String LAST_VISIT = "lastVisitDate";
  public final static String THREAD_DEPTH = "threadDepth";
  public final static String MESSAGE_DEPTH = "messageDepth";
  public final static String ITEMS_PER_PAGE = "itemsPerPage";
  public final static String LAST_FORUM_VISIT = "lastForumVisitDate_";
  public final static int COOKIE_EXPIRE = 60*60*24*365;

  /**
   * Required for implementing interface
   * HttpSessionBindingListener
   */
  public final void valueBound(HttpSessionBindingEvent e)
  {
  }

  /**
   * Required for implementing interface
   * HttpSessionBindingListener, used to save the
   * date and time of users lastVisit to Yazd.
   * If forums were visited, save nextForumVisit date
   * and time as an extended user property.
   */
  public final void valueUnbound(HttpSessionBindingEvent e)
  {
    saveLastVisit();
  }

  /**
   * If user is logged in save the
   * date and time user last used current session
   * as their lastVisist to Yazd.
   * If forums were visited, save nextForumVisit date
   * and time as an extended user property.
   */
  public final void saveLastVisit()
  {
    if( nextVisit == null )return;
    User user;
    ForumFactory ff = ForumFactory.getInstance(authorization);
    if( ff == null )return;
    ProfileManager pm = ff.getProfileManager();
    try {
      user = pm.getUser(authorization.getUserID());
    } catch( UserNotFoundException ex ) {
      return;
    }
    if( user.isAnonymous() ) {
      return;
    }

    user.setProperty(LAST_VISIT,"" + nextVisit.getTime());
    // Save next lastForumVisit dates to extended user properties
    String tmp;
    Date last;
    for( Iterator it=nextForumVisit.keySet().iterator(); it.hasNext(); ) {
      tmp = (String)it.next();
      last = (Date)nextForumVisit.get(tmp);
      user.setProperty( tmp, "" + last.getTime());
    }
  }

  /**
   * Update users Authorization state information
   * for current session.
   */
  public final void setAuthorization(Authorization auth)
  {
    authorization = auth;
  }

  /**
   * Get users Authorization state information
   * for current session.
   *
   * @return users Authorization
   */
  public final Authorization getAuthorization()
  {
    return authorization;
  }

  /**
   * Set the users currently selected forum
   * as session state information
   */
  public final void setForumID(int id)
  {
    forumID=id;
  }

  /**
   * Get the users currently selected forum
   * from session state information
   *
   * @return forumID
   */
  public final int getForumID()
  {
    return forumID;
  }

  /**
   * Set the users currently selected thread
   * as session state information
   */
  public final void setThreadID(int id)
  {
    threadID=id;
  }

  /**
   * Get the users currently selected thread
   * from session state information
   *
   * @return threadID
   */
  public final int getThreadID()
  {
    return threadID;
  }

  /**
   * Set the users currently selected message
   * as session state information
   */
  public final void setMessageID(int id)
  {
    messageID=id;
  }

  /**
   * Get the users currently selected message
   * from session state information
   *
   * @return messageID
   */
  public final int getMessageID()
  {
    return messageID;
  }

  /**
   * Under construction.
   */
  public final void setParentID(int id)
  {
    parentID=id;
  }

  /**
   * Under construction.
   */
  public final int getParentID()
  {
    return parentID;
  }

  /**
   * Set the Date of the users last visit to Yazd
   */
  public final void setLastVisit(Date d)
  {
    lastVisit = d;
  }

  /**
   * Get the Date of the users last visit to Yazd
   *
   * @return Date of users last Yazd visit
   */
  public final Date getLastVisit()
  {
    return lastVisit;
  }

  /**
   * Used by <b>forum</b> tag to set the Date of the users last visit to a forum.
   * This is saved as an extended user property when the
   * user logs out or the users session times out.  If the
   * users nextForumVisit setting is new, send a cookie.
   *
   * @param PageContext for request
   */
  public final void setNextForumVisitDate(PageContext pc, int forumid)
  {
    StringBuffer fid = new StringBuffer();
    fid.append(LAST_FORUM_VISIT + forumid);
    if( nextForumVisit.get(fid.toString()) == null ) {
      Date now = new Date();
      HttpServletRequest req = (HttpServletRequest)pc.getRequest();
      Cookie cookie = new Cookie(fid.toString(), "" + now.getTime());
      cookie.setPath(req.getContextPath());
      cookie.setMaxAge(COOKIE_EXPIRE);
      HttpServletResponse res = (HttpServletResponse)pc.getResponse();
      res.addCookie(cookie);
      nextForumVisit.put(fid.toString(), now);
    }
  }

  /**
   * Set the Date of the users last visit to a forum in the users
   * YazdState information.
   *
   * Used by <b>authorize</b> and <b>login</b> tag.
   *
   * @param String lastForumVisit property name
   * @param Date of last visit to forum
   */
  public final void setLastForumVisitDate(String prop, Date date)
  {
    lastForumVisit.put(prop,date);
  }

  /**
   * Used by <b>login</b> tag to reset the users lastForumVisit
   * based on their user extended properties.
   */
  public final void resetLastForumVisitDate(YazdRequest jr)
  {
    // Get the User data
    try {
      ProfileManager pm = jr.getProfileManager();
      User user = null;
      try {
        user = pm.getUser(getAuthorization().getUserID());
      } catch( UserNotFoundException ex ) {
      }
      if( user != null && !user.isAnonymous() ) {
        String tmp;
        String value;
	Date last;
	Enumeration enum = user.propertyNames();
	while( enum.hasMoreElements() ) {
	  tmp = (String)enum.nextElement();
	  if( tmp.startsWith(LAST_FORUM_VISIT) ) {
            value = user.getProperty(tmp);
	    if( value != null ) {
	      long last_time = Long.valueOf(value).longValue();
              last = new Date(last_time);
              lastForumVisit.put(tmp,last);
	    }
	  }
        }
      }
    } catch(JspException e) {}
  }

  /**
   * Used by forum tag to get the Date of the users last visit to a forum.
   *
   * @return Date of users last forum visit
   */
  public final Date getLastForumVisitDate(Forum cf, YazdRequest jr)
  {
    StringBuffer fid = new StringBuffer();
    fid.append(LAST_FORUM_VISIT + cf.getID());
    Date last = (Date)lastForumVisit.get(fid.toString());
    if( last == null ) {
      // If all else fails, use the forum creation date
      last = cf.getCreationDate();
    }
    return last;
  }

  /**
   * Set the Date of the users next lastVisit to the forum.
   * This is saved as an extended user property when the
   * user logs out or the users session times out.  If the
   * users nextVisit setting is new, send a cookie.
   *
   * Used by the <b>authorize</b> and <b>logout</b> tags.
   *
   * @param PageContext for request
   */
  public final void setNextVisit(PageContext pc)
  {
    if( nextVisit == null ) {
      nextVisit = new Date();
      HttpServletRequest req = (HttpServletRequest)pc.getRequest();
      Cookie cookie = new Cookie(LAST_VISIT,"" + nextVisit.getTime());
      cookie.setPath(req.getContextPath());
      cookie.setMaxAge(COOKIE_EXPIRE);
      HttpServletResponse res = (HttpServletResponse)pc.getResponse();
      res.addCookie(cookie);
    }
  }

  /**
   * Set the users login status flag.
   */
  public final void setLoggedIn(boolean l)
  {
    logged_in = l;
  }

  /**
   * Get the users login status flag.
   *
   * @return true if user is logged in, false if user is anonymous
   */
  public final boolean getLoggedIn()
  {
    return logged_in;
  }

  /**
   * Get the users current session messageDepth display preference.
   *
   * @return messageDepth
   */
  public final int getMessageDepth()
  {
    return message_depth;
  }

  /**
   * Set the users current session messageDepth display preference.
   */
  public final void setMessageDepth(int d)
  {
    message_depth = d;
  }

  /**
   * Get the users current session threadDepth display preference.
   *
   * @return threadDepth
   */
  public final int getThreadDepth()
  {
    if( thread_depth < message_depth )
      return message_depth;
    return thread_depth;
  }

  /**
   * Set the users current session threadDepth display preference.
   */
  public final void setThreadDepth(int d)
  {
    thread_depth = d;
  }

  /**
   * Get the users current session itemsPerPage display preference.
   *
   * @return itemsPerPage
   */
  public final int getItemsPerPage()
  {
    return items_per_page;
  }

  /**
   * Set the users current session itemsPerPage display preference.
   */
  public final void setItemsPerPage(int d)
  {
    items_per_page = d;
  }

  /**
   * Add an HREF to the message paging stack for a forum/thread.
   *
   */
  public final void addMessagePage(String currentPage, String nextPage)
  {
    String key = "" + forumID + "_" + threadID;
    Map map = (Map)prevMessages.get(key);
    if( map == null ) {
      map = new HashMap();
      prevMessages.put(key,map);
    }
    map.put(nextPage,currentPage);
  }

  /**
   * Get the HREF of a previous message page list.
   *
   * @return an HREF string
   */
  public final String getMessagePage(String currentPage)
  {
    String key = "" + forumID + "_" + threadID;
    Map map = (Map)prevMessages.get(key);
    if( map != null ) {
      String tmp = (String)map.get(currentPage);
      if( tmp != null )
        return tmp;
    }
    return "";
  }
}
