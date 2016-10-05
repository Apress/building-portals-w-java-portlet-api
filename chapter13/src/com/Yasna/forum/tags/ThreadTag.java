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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.ForumThreadNotFoundException;
import com.Yasna.forum.UnauthorizedException;

/**
 * JSP Tag <b>thread</b>, used to get information about current thread.
 * <p>
 * Requires that attribute <b>id</b> be set to the name of a
 * script variable for later use in JSP to retrieve Thread data
 * using &lt;jsp:getProperty/&gt;.
 * <p>
 * Gets the current thread from within a <b>thread_loop</b> tag or
 * the current thread from the user state information.
 * <p>
 * The <b>move_thread</b> or <b>move_message</b> tag can be used
 * nested within the <b>thread</b> tag to move a thread to a
 * different forum or move the thread message as a reply to another
 * forum thread message.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;thread&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.ThreadTag&lt;/tagclass&gt;
 *  &lt;teiclass&gt;com.Yasna.forum.tags.ThreadTEI&lt;/teiclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Get the current thread data&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;id&lt;/name&gt;
 *    &lt;required&gt;true&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see ThreadLoopTag
 * @see MoveThreadTag
 * @see MoveMessageTag
 * @see YazdRequest
 * @see YazdState
 * @see NewMessages
 * @see ChangeForum
 * @see GetNestedMessage
 *
 * @author Glenn Nielsen
 */
public class ThreadTag extends TagSupport implements GetNestedMessage,
	ChangeForum, NewMessages
{
  private ForumThread ct = null;
  private YazdRequest jr = null;
  private YazdState js = null;
  // ID's of previous and next thread for use as a navigation aide
  private int prevThreadID = -1;
  private int nextThreadID = -1;
  // Move thread or thread message to another forum variables
  private Forum mf = null;
  private int mtid = 0;
  private int mmid = 0;

  /**
   * Method called at start of thread Tag to get Thread
   *
   * @return EVAL_BODY_INCLUDE if there is a thread or SKIP_BODY if there is not a thread to view
   */
  public final int doStartTag() throws JspException
  {
    // Get the user state information
    js = (YazdState)pageContext.getAttribute("yazdUserState",
                PageContext.SESSION_SCOPE);
    if( js == null ) {
      throw new JspException("Yazd thread tag could not get yazd state.");
    }

    // Get the user request information
    jr = (YazdRequest)pageContext.getAttribute("yazdUserRequest",
                PageContext.REQUEST_SCOPE);
    if( jr == null )
      throw new JspException("Yazd thread tag, could not find request");

    // See if we are nested inside a thread_loop tag
    ThreadLoopTag tl = null;
    try {
      tl = (ThreadLoopTag)TagSupport.findAncestorWithClass(this,ThreadLoopTag.class);
    } catch(Exception e) {
    }

    if( tl != null ) {
      ct = tl.getThread();
    } else {
      // Get the thread from user state information
      ct = jr.getThread();
    }

    if( ct == null )
      return SKIP_BODY;

    // Get the next and previous thread ID's just in case they are needed
    Iterator thrit = jr.getForum().threads();
    int nextid;
    int previd = -1;
    while( thrit.hasNext() ) {
      nextid = ((ForumThread)thrit.next()).getID();
      if( nextid == ct.getID() ) {
        prevThreadID = previd;
        if( thrit.hasNext() )
          nextThreadID = ((ForumThread)thrit.next()).getID();
        break;
      }
      previd = nextid;
    }

    // Save the script variable so JSP author can access thread data
    pageContext.setAttribute(id,this,PageContext.PAGE_SCOPE);
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Used by MessageTag, WalkTag, and MoveThreadTag to get current ForumThread
   *
   * @return ForumThread
   */
  public final ForumThread getThread()
  {
    return ct;
  }

  /**
   * Thread ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="threadid"/&gt;
   *
   * @return String - thread ID
   */
  public final String getThreadid()
  {
    return "" + ct.getID();
  }

  /**
   * Thread MessageCount property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="messagecount"/&gt;
   *
   * @return String - thread MessageCount
   */
  public final String getMessagecount()
  {
    return "" + ct.getMessageCount();
  }

  /**
   * Thread Message Replies property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="messagereplies"/&gt;
   *
   * @return String - thread Message Replies
   */
  public final String getMessagereplies()
  {
    return "" + (ct.getMessageCount() - 1);
  }

  /**
   * Return total number of messages in thread, required by
   * GetNestedMessage.
   *
   * @return String - thread MessageCount
   */
  public final int getTotal()
  {
    return ct.getMessageCount();
  }

  /**
   * Thread Name property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="name"/&gt;
   *
   * @return String - thread Name
   */
  public final String getName()
  {
    return ct.getName();
  }

  /**
   * Forum Name property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="forumName"/&gt;
   *
   * @return String - name of the forum this thread is in
   */
  public final String getForumName()
  {
    return ct.getForum().getName();
  }

  /**
   * Forum ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="forumid"/&gt;
   *
   * @return String - id of the forum this thread is in
   */
  public final String getForumid()
  {
    return "" + ct.getForum().getID();
  }

  /**
   * Date and time of Thread CreationDate (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="creationDate"/&gt;
   *
   * @return date and time of Thread CreationDate as an integer
   */
  public final String getCreationDate()
  {
    return "" + ct.getCreationDate().getTime();
  }

  /**
   * Date and time of Thread ModifiedDate (integer) property which can be
   * obtained by the JSP page using
   * &lt;jsp:getProperty name=<i>"id"</i> property="modifiedDate"/&gt;
   *
   * @return date and time of Thread ModifiedDate as an integer
   */
  public final String getModifiedDate()
  {
    return "" + ct.getModifiedDate().getTime();
  }

  /**
   * Next Thread ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="nextThreadid"/&gt;
   *
   * @return String - next thread ID
   */
  public final String getNextThreadid()
  {
    return "" + nextThreadID;
  }

  /**
   * Previous Thread ID property which can be obtained by the JSP page
   * using &lt;jsp:getProperty name=<i>"id"</i> property="prevThreadid"/&gt;
   *
   * @return String - previous thread ID
   */
  public final String getPrevThreadid()
  {
    return "" + prevThreadID;
  }

  /**
   * Get the root message for this thread
   *
   * @return ForumMessage
   */
  public final ForumMessage getMessage() {
    return ct.getRootMessage();
  }

  /**
   * Move the thread to a different forum, used by move_thread tag.
   *
   * @return true if thread moved, false if thread move failed
   */
  public final boolean moveThread() throws JspException
  {
    Forum cf = ct.getForum();
    int threadid = ct.getID();
    if( mf == null )
      throw new JspException(
	"Yazd thread tag, you must set a forum using change_forum tag before using move_thread tag");
    try {
      cf.moveThread(ct,mf);
      return true;

      /* This should no longer be needed, it should be
         encapsulated in moveThread now
      try {
        ForumThread nt = mf.getThread(threadid);
	ct = nt;
	SearchIndexer si = jr.getForumFactory().getSearchIndexer();
        si.updateThreadForum(ct);
	return true;
      } catch(ForumThreadNotFoundException e) {}
      */

    } catch(UnauthorizedException ue) {
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.post.authorize.failed"));
    }
    return false;
  }

  /**
   * Remove the thread and post its message as a reply to a parent message
   * for a different forum thread, used by move_message tag.
   *
   * @return true if message moved, false if message move failed
   */
  public final boolean moveMessage() throws JspException
  {
    Forum cf = ct.getForum();
    int threadid = ct.getID();
    ForumMessage cm = null;
    ForumThread mt = null;

    cm = ct.getRootMessage();
    if( cm == null )
      throw new JspException(
        "Yazd thread tag, could not find message to move using move_message tag");

    if( ct.getMessageCount() != 1 )
      throw new JspException(
        "Yazd thread tag, you can not move a message thread that has replies using move_message tag");

    if( mf == null )
      throw new JspException(
        "Yazd thread tag, you must set a forum using change_forum tag before using move_message tag");

    if( mtid == 0 )
      throw new JspException(
        "Yazd thread tag, you must set a thread using set_thread tag before using move_message tag");

    if( mmid == 0 )
      throw new JspException(
        "Yazd thread tag, you must set a parent message using set_parent_message tag before using move_message tag");

    cm = ct.getRootMessage();
    if( cm == null ) {
      throw new JspException(
        "Yazd thread tag, could not find thread root message for move_message tag");
    }

    try {
      mt = mf.getThread(mtid);
    } catch(ForumThreadNotFoundException e) {
      throw new JspException(
        "Yazd thread tag, could not find thread to move message to");
    }
    try {
      ForumMessage parentMessage = mt.getMessage( mmid );
      ct.moveMessage(cm,mt,parentMessage);
      ct = mt;
      return true;

      /* old move message code
      cf.removeThread(ct);
      try {
	ForumMessage parentMessage = mt.getMessage( mmid );
	mt.addMessage( parentMessage, cm );
        ct = mt;
        SearchIndexer si = jr.getForumFactory().getSearchIndexer();
        si.updateMessageForum(ct.getMessage(cm.getID()));
        return true;
      } catch(ForumMessageNotFoundException e) {
        throw new JspException(
          "Yazd thread tag, could not find parent message for move_message tag");
      }
      */

    } catch(UnauthorizedException ue) {
      jr.addError(TagPropertyManager.getTagProperty("yazd.tag.post.authorize.failed"));
    } catch(ForumMessageNotFoundException e) {
      throw new JspException(
        "Yazd thread tag, could not find parent message for move_message tag");
    }
    return false;
  }

  /**
   *  Set an alternate forum where message is to be moved
   */
  public final void changeForum(Forum apf)
  {
    mf = apf;
  }

  /**
   * Determine if thread has new messages since users last visit.
   *
   * @return boolean - true or false
   */
  public final boolean newMessages()
  {
    if( js.getLastForumVisitDate(ct.getForum(),jr).getTime() <
        ct.getModifiedDate().getTime() )
                return true;
    return false;
  }

  /**
   * Determine if there is a thread preceding this one
   *
   * @return boolean - true or false
   */
  public final boolean prevThread()
  {
    if( prevThreadID > 0 )
      return true;
    return false;
  }

  /**
   * Determine if there is a thread following this one
   *
   * @return boolean - true or false
   */
  public final boolean nextThread()
  {
    if( nextThreadID > 0 )
      return true;
    return false;
  }

  /**
   *  Set the threadId for thread message is being moved to
   */
  public final void setThread(int tid)
  {
    mtid = tid;
  }

  /**
   *  Change to a different thread
   */
  public final void changeThread(int at) throws JspException
  {
    // See if we are nested inside a forum tag
    ForumTag ft = null;
    try {
      ft = (ForumTag)TagSupport.findAncestorWithClass(this,ForumTag.class);
    } catch(Exception e) {
    }
    if( ft == null ) {
      throw new JspException("Yazd thread tag must be nested inside a forum tag");
    }

    try {
      ForumThread nt = ft.getForum().getThread(at);
      ct = nt;
    } catch(ForumThreadNotFoundException e) {
      throw new JspException(
        "Yazd thread tag, could not find thread to change_thread to");
    }
  }

  /**
   *  Set the parent message id for message being moved
   */
  public final void setParentMessage(int mid)
  {
    mmid = mid;
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
