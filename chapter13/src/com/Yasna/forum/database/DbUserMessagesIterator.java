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

package com.Yasna.forum.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumThreadNotFoundException;
import com.Yasna.forum.User;

/**
 * An Iterator for all the user's Messages in a forum.
 */
public class DbUserMessagesIterator implements Iterator, ListIterator {

    /** DATABASE QUERIES **/
    private static final String USER_MESSAGES =
        "SELECT messageID, yazdMessage.threadID FROM yazdMessage,yazdForum,yazdThread WHERE " +
        "yazdMessage.userID=? AND yazdForum.forumID=? AND " +
        "yazdThread.forumID=yazdForum.forumID AND " +
        "yazdMessage.threadID=yazdThread.threadID";

    private int currentIndex = -1;
    private int [] messages;
    //The threads that correspond to each message.
    private int [] threads;
    private Forum forum;

    private DbForumFactory factory;

    protected DbUserMessagesIterator(DbForumFactory factory, User user, Forum forum)
    {
        this.factory = factory;
        this.forum = forum;
        //We don't know how many results will be returned, so store them
        //in an ArrayList.
        ArrayList tempMessages = new ArrayList();
        ArrayList tempThreads = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(USER_MESSAGES);
            pstmt.setInt(1, user.getID());
            pstmt.setInt(2, forum.getID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tempMessages.add(new Integer(rs.getInt(1)));
                tempThreads.add(new Integer(rs.getInt(2)));
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbUserMessagesIterator:constructor()-" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        //Now copy into an array.
        messages = new int[tempMessages.size()];
        for (int i=0; i<messages.length; i++) {
            messages[i] = ((Integer)tempMessages.get(i)).intValue();
        }
        //Now copy into an array.
        threads = new int[tempThreads.size()];
        for (int i=0; i<threads.length; i++) {
            threads[i] = ((Integer)tempThreads.get(i)).intValue();
        }
    }

    /**
     * Returns true if there are more users left to iteratate through forwards.
     */
    public boolean hasNext() {
        return (currentIndex+1 < messages.length);
    }

    /**
     * Returns true if there are more users left to iteratore through backwards.
     */
    public boolean hasPrevious() {
        return (currentIndex > 0);
    }

    /**
     * Returns the next User.
     */
    public Object next() throws java.util.NoSuchElementException {
        ForumMessage message = null;
        currentIndex++;
        if (currentIndex >= messages.length) {
            throw new java.util.NoSuchElementException();
        }
        try {
            message = forum.getThread(threads[currentIndex]).getMessage(messages[currentIndex]);
        }
        catch (ForumThreadNotFoundException ftnfe) {
            ftnfe.printStackTrace();
        }
        catch (ForumMessageNotFoundException fmnfe) {
            fmnfe.printStackTrace();
        }
        return message;
    }

    /**
     * For security reasons, the add operation is not supported. Use
     * ProfileManager instead.
     *
     * @see ProfileManager
     */
    public void add(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * For security reasons, the remove operation is not supported. Use
     * ProfileManager instead.
     *
     * @see ProfileManager
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * For security reasons, the set operation is not supported. Use
     * ProfileManager instead.
     *
     * @see ProfileManager
     */
    public void set(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the index number that would be returned with a call to next().
     */
    public int nextIndex() {
        return currentIndex+1;
    }

    /**
     * Returns the previous user.
     */
    public Object previous() throws java.util.NoSuchElementException {
        ForumMessage message = null;
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex++;
            throw new java.util.NoSuchElementException();
        }
        try {
            message = forum.getThread(threads[currentIndex]).getMessage(messages[currentIndex]);
        }
        catch (ForumThreadNotFoundException ftnfe) {
            ftnfe.printStackTrace();
        }
        catch (ForumMessageNotFoundException fmnfe) {
            fmnfe.printStackTrace();
        }
        return message;
    }

    /**
     * Returns the index number that would be returned with a call to previous().
     */
    public int previousIndex() {
        return currentIndex-1;
    }
}
