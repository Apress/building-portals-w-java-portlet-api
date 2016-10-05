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

import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;

/**
 * An Iterator for all the users in the system. Those wishing to integrate
 * Yazd into their own User system should also modify this class.
 */
public class DbUserIterator implements Iterator, ListIterator {

    /** DATABASE QUERIES **/
    private static final String ALL_USERS =
        "SELECT userID from yazdUser";

    private int currentIndex = -1;
    private int [] users;

    private ProfileManager profileManager;

    protected DbUserIterator(ProfileManager profileManager) {
        this.profileManager = profileManager;
        //We don't know how many results will be returned, so store them
        //in an ArrayList.
        ArrayList tempUsers = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ALL_USERS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tempUsers.add(new Integer(rs.getInt("userID")));
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbUserIterator:constructor()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        //Now copy into an array.
        users = new int[tempUsers.size()];
        for (int i=0; i<users.length; i++) {
            users[i] = ((Integer)tempUsers.get(i)).intValue();
        }
    }

    protected DbUserIterator(ProfileManager profileManager, int startIndex,
            int numResults)
    {
        this.profileManager = profileManager;

        int[] tempUsers = new int[numResults];
        //It's very possible that there might not be as many results to get
        //as we requested. Therefore, we keep track of how many results we
        //get by keeping a count.
        int userCount = 0;

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ALL_USERS);
            ResultSet rs = pstmt.executeQuery();
            //Move to start of index
            for (int i=0; i<startIndex; i++) {
                rs.next();
            }
            //Now read in desired number of results
            for (int i=0; i<numResults; i++) {
                if (rs.next()) {
                    tempUsers[userCount] = rs.getInt("userID");
                    userCount++;
                }
                else {
                    break;
                }
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbUserIterator:constructor()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        users = new int[userCount];
        for (int i=0; i<userCount; i++) {
            users[i] = tempUsers[i];
        }
    }

    /**
     * Returns true if there are more users left to iteratate through forwards.
     */
    public boolean hasNext() {
        return (currentIndex+1 < users.length);
    }

    /**
     * Returns true if there are more users left to iteratore through backwards.
     */
    public boolean hasPrevious() {
        return (currentIndex > 0);
    }

    /**
     * Returns the next User.
     *
     * @return the next User.
     * @throws NoSuchElementException if there are no more elements to return.
     */
    public Object next() throws java.util.NoSuchElementException {
        User user = null;
        currentIndex++;
        if (currentIndex >= users.length) {
            throw new java.util.NoSuchElementException();
        }
        try {
            user = profileManager.getUser(users[currentIndex]);
        }
        catch (UserNotFoundException gnfe) {
            gnfe.printStackTrace();
        }
        return user;
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
        User user = null;
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex++;
            throw new java.util.NoSuchElementException();
        }
        try {
            user = profileManager.getUser(users[currentIndex]);
        }
        catch (UserNotFoundException gnfe) {
            System.err.println(gnfe);
        }
        return user;
    }

    /**
     * Returns the index number that would be returned with a call to previous().
     */
    public int previousIndex() {
        return currentIndex-1;
    }
}
