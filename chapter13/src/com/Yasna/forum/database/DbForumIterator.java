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

import com.Yasna.forum.ForumThread;
import com.Yasna.forum.ForumThreadNotFoundException;

/**
 * Database implementation to iterate through threads in a forum.
 * At the moment, threads are always ordered by creationDate. Obviously,
 * various skins will probably want different iterators. The solution will
 * probably be to pass in various flags to forum.iterator() in order to get
 * different types of iterators. That will require small changes to this
 * class.
 */
public class DbForumIterator implements Iterator, ListIterator {

    /** DATABASE QUERIES **/
    private static final String GET_THREADS =
        "SELECT threadID, creationDate FROM yazdThread WHERE forumID=? " +
        "ORDER BY creationDate DESC";

    //A reference to the forum object that the iterator was created from.
    //This is used to load thread objects.
    private DbForum forum;
    //maintain an array of thread ids to iterator through.
    private int [] threads;
    //points to the current thread id that the user has iterated to.
    private int currentIndex = -1;

    DbForumFactory factory;

    public DbForumIterator(DbForum forum, DbForumFactory factory)
    {
        this.forum = forum;
        this.factory = factory;
        //We don't know how many results will be returned, so store them
        //in an ArrayList.
        ArrayList tempThreads = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(GET_THREADS);
            pstmt.setInt(1,forum.getID());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tempThreads.add(new Integer(rs.getInt("threadID")));
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
        threads = new int[tempThreads.size()];
        for (int i=0; i<threads.length; i++) {
            threads[i] = ((Integer)tempThreads.get(i)).intValue();
        }
    }

    public DbForumIterator(DbForum forum, DbForumFactory factory,
            int startIndex, int numResults)
    {
        this.forum = forum;
        this.factory = factory;

        int[] tempThreads = new int[numResults];
        //It's very possible that there might not be as many threads to get
        //as we requested. Therefore, we keep track of how many threads we
        //get by keeping a threadCount.
        int threadCount = 0;

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(GET_THREADS);
            pstmt.setInt(1,forum.getID());
            ResultSet rs = pstmt.executeQuery();

            //Move to start of index
            for (int i=0; i<startIndex; i++) {
                rs.next();
            }
            //Now read in desired number of results
            for (int i=0; i<numResults; i++) {
                if (rs.next()) {
                    tempThreads[threadCount] = rs.getInt("threadID");
                    threadCount++;
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
        threads = new int[threadCount];
        for (int i=0; i<threadCount; i++) {
            threads[i] = tempThreads[i];
        }
    }

    public void add(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return (currentIndex+1 < threads.length);
    }

    public boolean hasPrevious() {
        return (currentIndex > 0);
    }

    public Object next() throws java.util.NoSuchElementException {
        ForumThread thread = null;
        currentIndex++;
        if (currentIndex >= threads.length) {
            currentIndex--;
            throw new java.util.NoSuchElementException();
        }
        try {
            thread = forum.getThread(threads[currentIndex]);
        }
        catch (ForumThreadNotFoundException tnfe) {
            System.err.println(tnfe);
        }
        return thread;
    }

    public int nextIndex() {
        return currentIndex+1;
    }

    public Object previous() throws java.util.NoSuchElementException {
        ForumThread thread = null;
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex++;
            throw new java.util.NoSuchElementException();
        }
        try {
            thread = forum.getThread(threads[currentIndex]);
        }
        catch (ForumThreadNotFoundException tnfe) {
            System.err.println(tnfe);
        }
        return thread;
    }

    public int previousIndex() {
        return currentIndex-1;
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void set(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
