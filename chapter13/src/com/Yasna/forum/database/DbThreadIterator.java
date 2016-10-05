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
import java.util.Iterator;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumThread;

/**
 * Database implementation of Iterator for ForumMesages in a ForumThread.
 */
public class DbThreadIterator implements Iterator {

    /** DATABASE QUERIES **/
    private static final String MESSAGE_COUNT =
        "SELECT count(messageID) FROM yazdMessage WHERE threadID=?";
    private static final String GET_MESSAGES =
        "SELECT messageID, creationDate FROM yazdMessage WHERE threadID=? " +
        "ORDER BY creationDate ASC";

    private ForumThread thread;
    private int [] messages;
    private int currentIndex = -1;

    private ForumMessage nextMessage = null;

    protected DbThreadIterator(ForumThread thread) {
        this.thread = thread;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(MESSAGE_COUNT);
            pstmt.setInt(1, thread.getID());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            messages = new int[rs.getInt(1)];
            pstmt.close();

            pstmt = con.prepareStatement(GET_MESSAGES);
            pstmt.setInt(1, thread.getID());
            rs = pstmt.executeQuery();
            for (int i=0; i<messages.length; i++) {
                rs.next();
                messages[i] = rs.getInt(1);
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbThreadIterator:constructor()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    protected DbThreadIterator(ForumThread thread, int startIndex, int numResults)
    {
        this.thread = thread;

        int[] tempMessages = new int[numResults];
        //It's very possible that there might not be as many messages to get
        //as we requested. Therefore, we keep track of how many messages we
        //get by keeping a messageCount.
        int messageCount = 0;

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(GET_MESSAGES);
            pstmt.setInt(1, thread.getID());
            ResultSet rs = pstmt.executeQuery();
            //Move to start of index
            for (int i=0; i<startIndex; i++) {
                rs.next();
            }
            //Now read in desired number of results
            for (int i=0; i<numResults; i++) {
                if (rs.next()) {
                    tempMessages[messageCount] = rs.getInt("messageID");
                    messageCount++;
                }
                else {
                    break;
                }
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbThreadIterator:constructor()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        messages = new int[messageCount];
        for (int i=0; i<messageCount; i++) {
            messages[i] = tempMessages[i];
        }
    }

    /**
     * Returns true if there are more messages in the list.
     *
     * @return true if the iterator has more elements.
     */
    public boolean hasNext() {
        //If we are at the end of the list, there can't be any more messages
        //to iterate through.
        if (currentIndex+1 >= messages.length) {
            return false;
        }
        return true;

        /*
        BUG: this code calls getNextMessage() which will increment the
        currentIndex variable as a result of calling this message!

        //Otherwise, see if nextMessage is null. If so, try to load the next
        //message to make sure it exists.

        // hmm, do we really need to do this here? i think it should be left up
        // to the next() method -- BL

        if (nextMessage == null) {
            nextMessage = getNextMessage();
            if (nextMessage == null) {
                return false;
            }
        }
        return true;
        */
    }

    /**
     * Returns the next message in the interation.
     *
     * @return the next message in the interation.
     * @throws NoSuchElementException if the iteration has no more elements.
     */
    public Object next() throws java.util.NoSuchElementException {
        ForumMessage message = null;
        if (nextMessage != null) {
            message = nextMessage;
            nextMessage = null;
        }
        else {
            message = getNextMessage();
            if (message == null) {
                throw new java.util.NoSuchElementException();
            }
        }
        return message;
    }

    /**
     * Not supported for security reasons. Use the deleteMessage method in the
     * ForumThread class instead.
     *
     * @see ForumThread#deleteMessage(ForumMessage)
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the next available message, or null if there are no more
     * messages to return.
     */
    private ForumMessage getNextMessage() {
        while (currentIndex+1 < messages.length) {
            currentIndex++;
            try {
                ForumMessage message = thread.getMessage(messages[currentIndex]);
                return message;
            }
            catch (ForumMessageNotFoundException mnfe) { }
        }
        return null;
    }
}
