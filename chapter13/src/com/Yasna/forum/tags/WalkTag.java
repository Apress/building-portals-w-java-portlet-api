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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.TreeWalker;

/**
 * JSP Tag <b>walk</b>, implements a loop used to walk a tree of
 * messages in a thread.
 * <p>
 * Must be nested inside a <b>thread</b> tag.
 * <p>The thread to walk will be the closest parent thread tag unless
 * you specify the optional <b>thread</b> tag to be the <b>id</b>
 * of a different thread tag.
 * <p>
 * Uses the users display preferences <b>messageDepth</b>,
 * <b>threadDepth</b>, and <b>itemsPerPage</b> to determine
 * which messages to display, how to display them, and how
 * many messages to view on a page before triggering paging.
 * <p>
 * The <b>is_parent</b>, <b>is_message</b>, <b>is_summary</b>,
 * <b>is_total</b>, <b>next_page</b>, and <b>prev_page</b> tags
 * can be used to control what formatted content is generated
 * based on user display preferences.
 * <p>
 * The <b>current_depth</b>, <b>while_child</b>, <b>while_new_child</b>,
 * <b>while_parent</b>, and <b>while_new_parent</b> tags can be used
 * to assist JSP page author to propertly format HTML output when
 * displaying a tree of messages in a thread.
 * <p>
 * During each loop the body of the walk tag is processed.
 * <p>
 * The <b>on_entry</b> tag can be used to include content if this is
 * the first time through the loop.
 * <p>
 * The <b>on_exit</b> tag can be used to include content if this is
 * the last time through the loop.
 * <p>
 * The walk tag continues looping until there are no more messages in the
 * thread or the users items_per_page limit has been reached. The <b>next_page</b>
 * tag can be used to determine if the walk tag loop ended due to the
 * users items_per_page limit.  Then the <b>next_item</b> tag can be
 * used to get the query portion of an HTML href so that you can setup
 * an href to page to the next list of messges in this thread.
 * <p>
 * The walk tag can loop back to the previous list of messages
 * after the walk has been paged. The <b>prev_page</b>
 * tag can be used to determine if the walk tag has a previous page.
 * Then the <b>prev_item</b> tag can be used to get the query portion
 * of an HTML href so that you can setup an href to page to the previous
 * list of messages.
 * <p>
 * JSP Tag Lib Descriptor
 * <p><pre>
 *  &lt;name&gt;walk&lt;/name&gt;
 *  &lt;tagclass&gt;com.Yasna.forum.tags.WalkTag&lt;/tagclass&gt;
 *  &lt;bodycontent&gt;JSP&lt;/bodycontent&gt;
 *  &lt;info&gt;Walks a tree of messages in a thread.&lt;/info&gt;
 *  &lt;attribute&gt;
 *    &lt;name&gt;thread&lt;/name&gt;
 *    &lt;required&gt;false&lt;/required&gt;
 *    &lt;rtexprvalue&gt;false&lt;/rtexprvalue&gt;
 *  &lt;/attribute&gt;
 * </pre>
 *
 * @see ThreadTag
 * @see IsParentTag
 * @see IsMessageTag
 * @see IsSummaryTag
 * @see IsTotalTag
 * @see OnEntryTag
 * @see NextPageTag
 * @see NextItemTag
 * @see PrevPageTag
 * @see PrevItemTag
 * @see OnExitTag
 * @see CurrentDepthTag
 * @see WhileChildTag
 * @see WhileNewChildTag
 * @see WhileParentTag
 * @see WhileNewParentTag
 * @see YazdState
 *
 * @author Glenn Nielsen
 */
public class WalkTag extends BodyTagSupport
implements GetNestedMessage, Paging {
    private YazdState js = null;
    private String thread = null;
    // WalkData maintains state information for each depth in the tree
    private WalkData wd = null;
    // Used to walk the messages
    private TreeWalker tw = null;
    // Current thread being walked
    private ForumThread ct = null;
    // Current message in the walk
    private ForumMessage cm = null;
    // maintain a stack of WalkData while walking messages
    private LinkedList stack = new LinkedList();
    // variables to maintain state information while walking messages
    private int message_num = 0;
    private int thread_depth = 0;
    private boolean parent_done = false;
    private boolean is_total = false;
    // Did this thread message walk trigger a page
    private boolean next_page = false;
    // Store the href for paging to the next page of messages
    private String next_href = null;
    // Save the query string for the current request
    private String current_href = null;
    // Do we have a previous list of messages that could be displayed
    private boolean prev_page = false;
    // Flag whether this is the first iteration of loop
    private boolean is_entry = true;
    // Flag whether this is the last iteration of loop
    private boolean is_exit = false;
    // deeper and shallower are used by while_new child or parent tags
    private int deeper = 0;
    private int shallower = 0;
    private boolean walk_back = false;
    
    /**
     * Method called at start of walk Tag, initializes data needed to
     * walk a thread of messages.
     *
     * @return an EVAL_BODY_TAG if walk tag loop should continue, or a SKIP_BODY if walk tag loop is completed.
     */
    public final int doStartTag() throws JspException {
        // Get the user state information
        js = (YazdState)pageContext.getAttribute("yazdUserState",
        PageContext.SESSION_SCOPE);
        if( js == null )
            throw new JspException("Yazd walk tag, could not find yazdUserState");
        
        ThreadTag tt = null;
        // Get the thread from its id
        if( thread != null ) {
            tt = (ThreadTag)pageContext.getAttribute(thread,PageContext.PAGE_SCOPE);
        } else {
            // Get the current thread from closest parent thread tag
            try {
                tt = (ThreadTag)TagSupport.findAncestorWithClass(this,ThreadTag.class);
            } catch(Exception e) {
            }
        }
        
        if( tt != null ) {
            ct = tt.getThread();
        }
        
        if( ct == null ) {
            throw new JspException("Could not find thread tag.");
        }
        
        tw = ct.treeWalker();
        
        // Set up the tree of messages to get to current message if
        // previous walk of this thread exceeded itemsPerPage and
        // triggered paging.
        int mesid = -1;
        String tmp;
        ForumMessage next = null;
        HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
        current_href = req.getQueryString();
        WalkData nw = null;
        cm = tw.getRoot();
        wd = new WalkData();
        wd.setMessage(tw,cm);
        stack.addLast((Object)wd);
        
        // Get the previous message id's for walking the tree
        for( int i = 1; (tmp = req.getParameter("walk_" + i)) != null; i++ ) {
            
            try {
                mesid = Integer.valueOf(tmp).intValue();
            } catch( NumberFormatException e ) {
                break;
            }
            try {
                next = ct.getMessage(mesid);
            } catch(ForumMessageNotFoundException e) {
                break;
            }
            //System.out.println("WalkTag Parent: " + mesid);
            nw = new WalkData();
            nw.setMessage(tw,next);
            stack.addLast((Object)nw);
        }
        if( (tmp = req.getParameter("walk_back")) != null ) {
            //System.out.println("Setting walk_back");
            walk_back=true;
        }
        
        if( stack.size() == 1 && !walk_back ) {
            parent_done = true;
            message_num++;
        } else {
            prev_page = true;
        }
        
        return EVAL_BODY_BUFFERED;
        
    }
    
    /**
     * Method called at end of each walk Tag Body to navigate thread
     * message tree.
     *
     * @return an EVAL_BODY_TAG if walk should continue, or a SKIP_BODY if walk is completed.
     */
    public final int doAfterBody() throws JspException {
        is_entry = prev_page = false;
        // If is_exit is true we are all done
        if( is_exit )
            return SKIP_BODY;
        
        // These get reset each iteration of walk tag loop
        deeper = shallower = 0;
        // Walk the tree of parent messages from the root message
        // until we are viewing the current message
        //System.out.println("** parent_done=" + parent_done);
        if( !parent_done && (thread_depth + 1) < stack.size() ) {
            //System.out.println("Walking parent thread");
            WalkData nw = (WalkData)stack.get(thread_depth+1);
            if( nw == null ) {
                //System.out.println("Could not get next WalkData");
                parent_done = true;
            } else {
                thread_depth++;
                deeper++;
                ForumMessage child = nw.getMessage();
                ForumMessage next = null;
                //System.out.println("cmID: " + cm.getID() + " childID: " + child.getID());
                int i,j;
                j = wd.getChildCount();
                //System.out.println("Found " + j + " children");
                for( i = 0; i < j; i++ ) {
                    //System.out.println("ChildCheckLoop: " + i);
                    next = tw.getChild(cm,i);
                    if( next == null ) {
                        //System.out.println("No more children, parent_done");
                        parent_done = true;
                        wd.setChildIndex(i);
                        break;
                    }
                    //System.out.println("nextID: " + next.getID());
                    if( child.getID() == next.getID() ) {
                        //System.out.println("Setting ChildIndex");
                        wd.setChildIndex(i+1);
                        //System.out.println("thread_depth: " + thread_depth + " stack: " + stack.size() + " walk_back=" + walk_back);
                        if( (thread_depth+1) >= stack.size() && walk_back ) {
                            //System.out.println("Walk Back from Child");
                            cm = null;
                            nw.valid = false;
                            break;
                        }
                        //System.out.println("Displaying parent");
                        wd = nw;
                        cm = child;
                        return EVAL_BODY_BUFFERED;
                    }
                }
            }
        }
        
        // Fell through without finding a parent message,
        // start walking messages new to the user
        
        //System.out.println("** Getting new message, is_total:" + is_total +
        //" thread_depth: " + thread_depth + " valid=" + wd.valid);
        //System.out.println("WalkData cm: " + wd.getMessage().getID() +
        //" Count: " + wd.getChildCount() + " Index: " + wd.getChildIndex() );
        if( cm != null ) {
            //System.out.println("CurrentMessageID: " + cm.getID() );
        }
        
        parent_done = true;
        //System.err.println(message_num);
        // First see if we have displayed the items_per_page the
        // user wants.
        if( message_num >= js.getItemsPerPage() ) {
            //System.out.println("Exceeded items_per_page");
            cm = null;
        }
        
        if( cm != null && !is_total ) {
            
            // First see if this message has any children
            if( tw.getChildCount(cm) > 0 ) {
                //System.out.println("Getting children of current message");
                thread_depth++;
                deeper++;
                if( thread_depth >= js.getThreadDepth() ) {
                    //System.out.println("Thread is too deep");
                    is_total = true;
                    message_num++;
                    return EVAL_BODY_BUFFERED;
                }
                WalkData nw = null;
                if( stack.size() <= thread_depth ) {
                    nw = new WalkData();
                    stack.addLast((Object)nw);
                } else {
                    // Object reuse is good!
                    nw = (WalkData)stack.get(thread_depth);
                    nw.valid = true;
                    nw.setChildIndex(0);
                }
                cm = tw.getChild(cm,0);
                nw.setMessage(tw,cm);
                wd.setChildIndex(wd.getChildIndex()+1);
                wd = nw;
                message_num++;
                return EVAL_BODY_BUFFERED;
            }
            //System.out.println("See if Parent has more children");
            // See if the parent message has any more children
            if( wd.getChildIndex() < wd.getChildCount() ) {
                //System.out.println("Parent has another sibling");
                cm = tw.getChild(wd.getMessage(),wd.getChildIndex());
                wd.setChildIndex(wd.getChildIndex()+1);
                message_num++;
                return EVAL_BODY_BUFFERED;
            } else {
                wd.valid = false;
            }
        }
        //System.out.println("Walk back up the message tree.");
        
        // Parent has no more children or exceeded items_per_page,
        // so reduce message depth by one
        cm = null;
        
        if( thread_depth < 1 ) {
            //System.out.println(nextItem());
            is_exit = true;
            return EVAL_BODY_BUFFERED;
        }
        
        if( is_total ) {
            thread_depth--;
            shallower++;
            is_total = false;
            return EVAL_BODY_BUFFERED;
        }
        
        //System.out.println("message_num is" + message_num);
        if( message_num < js.getItemsPerPage() ) {
            wd = (WalkData)stack.get(thread_depth-1);
            //System.out.println("Back to parent message: " + wd.getMessage().getID() );
            if( wd.getChildIndex() < wd.getChildCount() ) {
                //System.out.println("Now Parent has a sibling");
                cm = tw.getChild(wd.getMessage(),wd.getChildIndex());
                wd.setChildIndex(wd.getChildIndex()+1);
                WalkData nw = null;
                if( stack.size() <= thread_depth ) {
                    nw = new WalkData();
                    stack.addLast((Object)nw);
                } else {
                    // Object reuse is good!
                    nw = (WalkData)stack.get(thread_depth);
                    nw.valid = true;
                    nw.setChildIndex(0);
                }
                nw.setMessage(tw,cm);
                wd = nw;
                message_num++;
                return EVAL_BODY_BUFFERED;
            } else {
                wd.valid = false;
            }
        }
        thread_depth--;
        shallower++;
        //System.out.println("thread_depth=" + thread_depth +
        //" valid=" + ((WalkData)stack.get(0)).valid);
        if( thread_depth == 0 && stack.size() > 0 && ((WalkData)stack.get(0)).valid) {
            //System.out.println("WalkTag page = true");
            is_exit = next_page = true;
            nextHref();
        }
        
        //System.err.println("doAfterBody() starting again");
        return EVAL_BODY_BUFFERED;
    }
    
    /**
     * Method called at end of walk Tag to output content
     *
     * @return EVAL_PAGE
     */
    public final int doEndTag() throws JspException {
        try {
            if(bodyContent != null)
                bodyContent.writeOut(bodyContent.getEnclosingWriter());
        } catch(java.io.IOException e) {
            throw new JspException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }
    
     /**
     * Used by message tag to get current message
     *
     * @return current ForumMessage
     */
    public final ForumMessage getMessage() {
        return cm;
    }
    
    /**
     * Used by current_depth, while_child, and while_parent tags
     * to get current depth while walking thread
     *
     * @return String - current message depth in thread
     */
    public final int getDepth() {
        return thread_depth;
    }
    
    /**
     * Used by while_new_child tag to flag a new depth in the thread
     *
     * @return true if thread depth is deeper, false if it isn't any more deeper
     */
    public final boolean isDeeperMessage() {
        if( deeper > 0 ) {
            deeper--;
            return true;
        }
        return false;
    }
    
    /**
     * Used by while_new_parent tag to flag walking back to a
     * shallower message depth.
     *
     * @return true if thread depth is shallower, false if it isn't any more shallow
     */
    public final boolean isShallowerMessage() {
        if( shallower > 0 ) {
            shallower--;
            return true;
        }
        return false;
    }
    
    /**
     * Used by is_parent tag to detect if this is a parent message of
     * the first new message to display after paging to a subsequent
     * page.
     *
     * @return true if this is a parent, false if not
     */
    public final boolean isParentMessage() {
        if( parent_done )
            return false;
        return true;
    }
    
    /**
     * Used by is_message tag to detect if this is a new message
     * below the users messageDepth preference.
     *
     * @return true if this is a message below messageDepth, false if not
     */
    public final boolean isMessageMessage() {
        if( !parent_done )
            return false;
        if( thread_depth < js.getMessageDepth() )
            return true;
        return false;
    }
    
    /**
     * Used by is_summar tag to detect if this is a new message
     * greater than or equal to the users messageDepth preference,
     * but below the users threadDepth preference.
     *
     * @return true if this is a message greater than or equal to the users messageDepth preference, but below the users threadDepth preference; false if not
     */
    public final boolean isSummaryMessage() {
        if( !parent_done )
            return false;
        if( thread_depth < js.getMessageDepth() )
            return false;
        if( thread_depth >= js.getThreadDepth() )
            return false;
        return true;
    }
    
    /**
     * Used by is_total tag to detect if this is a new message
     * greater than or equal to the users threadDepth preference.
     *
     * @return true if this is a new message greater than or equal to the users threadDepth preference, false if not
     */
    public final boolean isNewMessage() {
        if( !parent_done )
            return false;
        if( thread_depth >= js.getThreadDepth() &&
        thread_depth >= js.getMessageDepth() )
            return true;
        return false;
    }
    
    /**
     * Used by message tag to get the total number of child messages
     * (replies) to the current message.
     *
     * @return number of child messages (replies)
     */
    public final int getTotal() {
        return tw.getRecursiveChildCount(cm);
    }
    
    /**
     * Used by <b>on_entry</b> tag to detect if this is the first iteration
     * of the loop.
     *
     * @return true or false
     */
    public final boolean isEntry() {
        return is_entry;
    }
    
    /**
     * Used by <b>on_exit</b> tag to detect if this is the last iteration
     * of the loop.
     *
     * @return true or false
     */
    public final boolean isExit() {
        return is_exit;
    }
    
    /**
     * Used by <b>next_page</b> tag to detect if walk could continue
     * on another page.
     *
     * @return true or false
     */
    public final boolean isNextPage() {
        return next_page;
    }
    
    /**
     * Used by <b>prev_page</b> tag to detect if message listing
     * has a previous page.
     *
     * @return true or false
     */
    public final boolean isPrevPage() {
        return prev_page;
    }
    
    /**
     * Used by <b>next_item</b> tag to return query portion of an HTML GET href
     * for paging to next list of messages to walk in thread.
     *
     * @return String - query portion of an HTML GET href
     */
    public final String nextItem() {
        return next_href;
    }
    
    private void nextHref() {
        StringBuffer args = new StringBuffer();
        if( next_page ) {
            int num = stack.size();
            WalkData nw;
            WalkData pw;
            boolean back_walk = true;
            for( int i = num-1; i > 0; i-- ) {
                nw = (WalkData)stack.get(i);
                //System.out.println("i: " + i + " ID: " + nw.getMessage().getID() +
                //" valid=" + nw.valid + " back_walk=" + back_walk +
                //" Index: " + nw.getChildIndex() + " Count: " + nw.getChildCount());
                if( nw.valid ) {
                    if( back_walk ) {
                        if( nw.getChildIndex() >= nw.getChildCount() ) {
                            pw = (WalkData)stack.get(i-1);
                            //System.out.println("previous: " + i + " ID: " + pw.getMessage().getID() +
                            //" valid=" + pw.valid + " back_walk=" + back_walk +
                            //" Index: " + pw.getChildIndex() + " Count: " + pw.getChildCount());
                            if( pw.getChildIndex() < pw.getChildCount() ) {
                                args.append("walk_back=true&");
                                args.append("walk_" + i + "=" + nw.getMessage().getID() + "&");
                                back_walk = false;
                            }
                        } else {
                            back_walk = false;
                            args.append("walk_" + i + "=" + nw.getMessage().getID() + "&");
                        }
                    } else {
                        args.append("walk_" + i + "=" + nw.getMessage().getID() + "&");
                    }
                }
            }
        }
        if( args.length() > 0 ) {
            args.setLength(args.length()-1);
        }
        //System.out.println("WalkTag page args: " + args);
        next_href = args.toString();
        if( current_href != null ) {
            js.addMessagePage(current_href,next_href);
        }
    }
    
    /**
     * Used by <b>prev_item</b> tag to return query portion of an HTML GET href
     * for paging to previous list of messages.
     *
     * @return String - query portion of an HTML GET href
     */
    public final String prevItem() {
        if( current_href != null ) {
            String tmp = null;
            tmp = js.getMessagePage(current_href);
            if( tmp != null )
                return tmp;
        }
        return "";
    }
    
    /**
     * Set name of thread id to use
     */
    public final void setThread(String tid) {
        thread = tid;
    }
}

