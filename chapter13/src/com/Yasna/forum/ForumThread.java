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

package com.Yasna.forum;

import java.util.Date;
import java.util.Iterator;

/**
 * A ForumThread is a container for a hierarchy of ForumMessages.<p>
 *
 * Intimately tied to the concept of a ForumThread is a root message. A
 * root message must be supplied when creating a thread. Subsequently, all
 * further messages posted to the thread are children of the root message.<p>
 *
 * To get a handle on a ForumThread object, the <code>getThread</code> method
 * should be called from a Forum object. To create a thread, <code>createThread</code>
 * from a Forum object should be used. After creating a thread, you must
 * attach it to a Forum by calling <code>addThread</code> from a Forum object.
 * To delete a ForumThread, call the <code>deleteThread</code> method from the
 * Forum object that the thread is attached to.<p>
 *
 * There are two options for navigating through the messages of a thread.
 * <ul>
 *   <li>A TreeWalker -- this provides a hierarchical view of the messages in
 *      in the thread. For most skins, this will be the most appropriate
 *      navigation method.
 *   <li>An Iterator -- this provides a flat view of the messages in the thread.
 *      Since the message structure is not really flat, a field to sort by
 *      must be provided. This view of thread is useful for skins that want
 *      to provide functionality such as listing all the messages in the order
 *      they were created, etc.
 * </ul>
 *
 * Because a root message must be passed in when creating a thread, you must
 * first create that message before creating the thread. The following code
 * snippet demonstrates:
 * <pre>
 * //Assume that a forum object and user object are already defined.
 * ForumMessage rootMessage = myForum.createMessage(myUser);
 * rootMessage.setSubject("A subject");
 * rootMessage.setBody("A body");
 * ForumThread myThread = myForum.createThread(rootMessage);
 * </pre>
 */
public interface ForumThread {

    /**
     * Returns the unique id of the thread.
     */
    public int getID();

    /**
     * Returns the name of the thread (which is the subject of the root message).
     * This is a convenience method that is equivalent to
     * <code>getRootMessage().getSubject()</code>.
     *
     * @return the name of the thread, which is the subject of the root message.
     */
    public String getName();

    /**
     * Returns the Date that the thread was created.
     */
    public Date getCreationDate();

    /**
     * Sets the creation date of the thread. In most cases, the creation date
     * will default to when the thread was entered into the system. However,
     * the creation date needs to be set manually when importing data.
     * In other words, skin authors should ignore this method since it only
     * intended for system maintenance.
     *
     * @param creationDate the date the thread was created.
     *
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void setCreationDate(Date creationDate) throws UnauthorizedException;

    /**
     * Returns the Date that the thread was last modified. In other words, the
     * date of the most recent message in the thread.
     */
    public Date getModifiedDate();

    /**
     * Sets the date the thread was last modified. In most cases, last modifed
     * will default to when the thread data was last changed. However,
     * the last modified date needs to be set manually when importing data.
     * In other words, skin authors should ignore this method since it only
     * intended for system maintenance.
     *
     * @param modifiedDate the date the thread was modified.
     *
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void setModifiedDate(Date modifiedDate) throws UnauthorizedException;

    /**
     * Returns the parent Forum of the thread.
     */
    public Forum getForum();

    /**
     * Returns a message from the thread based on its id.
     *
     * @param messageID the ID of the message to get from the thread.
     */
    public ForumMessage getMessage(int messageID)
            throws ForumMessageNotFoundException;

    /**
     * Returns the root message of a thread. The root message is a special
     * first message that is intimately tied to the thread for most forumViews.
     * All other messages in the thread are children of the root message.
     */
    public ForumMessage getRootMessage();

    /**
     * Returns the number of messages in the thread. This includes the root
     * message. So, to find the number of replies to the root message,
     * subtract one from the answer of this method.
     */
    public int getMessageCount();

    /**
     * Adds a new message to the thread.
     *
     * @param parentMessage some message in the thread that will be parent
     * @param newMessage message to add to the thread under the parent
     */
    public void addMessage(ForumMessage parentMessage, ForumMessage newMessage);

    /**
     * Deletes a message from the thread. Throws an IllegalArgumentException
     * if the message is not in the thread. If the message is deleted, it
     * should be entirely erased from the Forum system. Therefore, the
     * behavior is unspecified if a message object is first removed from a
     * thread and then added to another (this action not recommended).
     *
     * @throws IllegalArgumentException if the message does not belong to the
     *   thread.
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void deleteMessage(ForumMessage message)
            throws UnauthorizedException;

    /**
     * Moves a message from one thread to another. The message will become
     * a child of <code>parentMessage</code> in <code>newThread</code>
     *
     * For this to work, <code>message</code> must exist in the thread that
     * this method is invoked on, <code>parentMessage</code> must be in
     * <code>newThread</code>, and the user calling this method must have
     * ADMIN permissions for the forum this method is invoked on and the forum
     * that <code>newThread</code> belongs to.<p>
     *
     * The main purpose of this method is to allow admins to move non-topical
     * messages into a more appropriate thread.
     *
     * @param message the message to move to another thread.
     * @param newThread the thread to move the message to.
     * @param parentMessage the message under newThread that <code>message</code>
     *      should become a child of.
     * @throws UnauthorizedException if does not have ADMIN permissions for the
     *      this forum and the forum that <code>newThread</code> belongs to.
     * @throws IllegalArgumentException if <code>message</code> does not belong
     *      to the thread that this method is invoked on, or <code>parentMessage
     *      </code> does not belong to <code>newThread</code>.
     */
    public void moveMessage(ForumMessage message, ForumThread newThread,
            ForumMessage parentMessage) throws UnauthorizedException,
            IllegalArgumentException;

    /**
     * Returns a TreeWalker for the entire thread. A TreeWalker is used
     * to navigate through the tree of messages in a hierarchical manner.
     */
    public TreeWalker treeWalker();

    /**
     * Return an Iterator for all the messages in a thread.
     */
    public Iterator messages();

    /**
     * Return an Iterator for all the messages in a thread. The startIndex
     * and numResults restrict the number of results returned, which is useful
     * for multi-page HTML navigation.
     *
     * @param startIndex the index you'd like to start the iterator at.
     * @param numResuls the max number of results iterator will hold.
     */
    public Iterator messages(int startIndex, int numResults);

    /**
     * Returns true if the handle on the object has the permission specified.
     * A list of possible permissions can be found in the ForumPermissions
     * class. Certain methods of this class are restricted to certain
     * permissions as specified in the method comments.
     *
     * @see ForumPermissions
     */
    public boolean hasPermission(int type);
}
