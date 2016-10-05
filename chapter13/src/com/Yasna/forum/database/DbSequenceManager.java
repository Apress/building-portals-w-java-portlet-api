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
import java.util.HashMap;

/**
 * Manages sequences of unique ID's that get stored in the database. Database
 * support for sequences varies widely; some don't support them at all. So,
 * we handle unique ID generation with synchronized counters.<p>
 *
 * Selecting the initial ID for each sequence is trivial: we simply query the
 * database to get the last highest unique ID, and then add 1 to that.<p>
 *
 * This method only works if a single instance of Yazd is pointed at a database.
 * Otherwise, the unique ID's will stop being unique. :) A refined class that
 * deals with this is coming soon.
 */
public class DbSequenceManager {

    private static final String NEXT_FORUM_ID = "SELECT max(forumID) FROM yazdForum";
    private static final String NEXT_THREAD_ID = "SELECT max(threadID) FROM yazdThread";
    private static final String NEXT_MESSAGE_ID = "SELECT max(messageID) FROM yazdMessage";
    private static final String NEXT_GROUP_ID = "SELECT max(groupID) FROM yazdGroup";
    private static final String NEXT_USER_ID = "SELECT max(userID) FROM yazdUser";

    /**
     * Singleton type access to the class.
     */
    private static DbSequenceManager sequenceManager = null;

    /**
     * Lock so that we can synchronize during initialization.
     */
    private static Object initLock = new Object();

    /**
     * Returns the next ID of the specified type.
     *
     * @param type the type of unique ID.
     * @return the next unique ID of the specified type.
     */
    public static int nextID(String type) {
        if (sequenceManager == null) {
            synchronized(initLock) {
                if (sequenceManager == null) {
                    sequenceManager = new DbSequenceManager();
                }
            }
        }
        return sequenceManager.nextUniqueID(type);
    }

    private HashMap uniqueIDCounters;

    /**
     * Creates a new DbSequenceManager and initializes all of the counters.
     */
    public DbSequenceManager() {
        uniqueIDCounters = new HashMap();
        //Forum counter.
        uniqueIDCounters.put("Forum", new Counter(getNextDbID(NEXT_FORUM_ID)));
        //ForumThread counter.
        uniqueIDCounters.put("ForumThread", new Counter(getNextDbID(NEXT_THREAD_ID)));
        //ForumMessage counter.
        uniqueIDCounters.put("ForumMessage", new Counter(getNextDbID(NEXT_MESSAGE_ID)));
        //Group counter.
        uniqueIDCounters.put("Group", new Counter(getNextDbID(NEXT_GROUP_ID)));
        //User counter.
        uniqueIDCounters.put("User", new Counter(getNextDbID(NEXT_USER_ID)));
    }

    /**
     * Provides the next available unique ID for a particular object type.
     * Essentially this provides for the functionality of an auto-increment
     * database field. Valid counter names are Forum, ForumThread, ForumMessage,
     * Group, and User.
     * <p>
     * Those that are integrating Yazd into existing tables should be sure
     * that the table names match up so that the correct starting unique
     * id can be determined.
     */
    public int nextUniqueID(String counterName) {
        Counter counter = (Counter)uniqueIDCounters.get(counterName);
        return counter.next();
    }

    /**
     * Do a lookup to see what the next available unique ID is for a
     * particular table. Yazd uses the convention of always making the name of
     * the column that holds the unique id be called id, so this works.
     */
    private int getNextDbID(String query) {
        int currentID = 0;
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            currentID = rs.getInt(1);
        }
        catch( Exception sqle ) {
            System.err.println("Error in DbSequenceManager:getNextDbID()-" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        //If the table is empty, start with id 0
        if (currentID < 0) {
            currentID = 0;
        }
        return currentID;
    }

    /**
     * Internal class to keep track of the current count of a unique id.
     */
    private final class Counter {

        private int count;

        public Counter(int currentCount) {
            count = currentCount;
        }

        public final synchronized int next() {
            return (++count);
        }
    }
}
