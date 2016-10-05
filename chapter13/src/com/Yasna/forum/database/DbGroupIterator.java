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

import com.Yasna.forum.Group;
import com.Yasna.forum.GroupNotFoundException;
import com.Yasna.forum.ProfileManager;

/**
 * Iterates through all the groups in the system.
 */
public class DbGroupIterator implements Iterator, ListIterator {

    /** DATABASE QUERIES **/
    private static final String ALL_GROUPS = "SELECT groupID from yazdGroup";

    private int currentIndex = -1;
    private int [] groups;

    private ProfileManager profileManager;

    protected DbGroupIterator(ProfileManager profileManager) {
        this.profileManager = profileManager;
        //We don't know how many results will be returned, so store them
        //in an ArrayList.
        ArrayList tempGroups = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ALL_GROUPS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tempGroups.add(new Integer(rs.getInt("groupID")));
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbGroupIterator:constructor()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        groups = new int[tempGroups.size()];
        for (int i=0; i<groups.length; i++) {
            groups[i] = ((Integer)tempGroups.get(i)).intValue();
        }
    }

    protected DbGroupIterator(ProfileManager profileManager, int startIndex,
            int numResults)
    {
        this.profileManager = profileManager;

        int[] tempResults = new int[numResults];
        //It's very possible that there might not be as many results to get
        //as we requested. Therefore, we keep track of how many results we
        //get by keeping a count.
        int resultCount = 0;

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ALL_GROUPS);
            ResultSet rs = pstmt.executeQuery();
            //Move to start of index
            for (int i=0; i<startIndex; i++) {
                rs.next();
            }
            //Now read in desired number of results
            for (int i=0; i<numResults; i++) {
                if (rs.next()) {
                    tempResults[resultCount] = rs.getInt("groupID");
                    resultCount++;
                }
                else {
                    break;
                }
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbGroupIterator:constructor()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        groups = new int[resultCount];
        for (int i=0; i<resultCount; i++) {
            groups[i] = tempResults[i];
        }
    }

    /**
     * Returns true if there are more groups left to iteratate through.
     */
    public boolean hasNext() {
        return (currentIndex+1 < groups.length);
    }

    /**
     * Returns the next Group.
     */
    public Object next() throws java.util.NoSuchElementException {
        Group group = null;
        currentIndex++;
        if (currentIndex >= groups.length) {
            throw new java.util.NoSuchElementException();
        }
        try {
            group = profileManager.getGroup(groups[currentIndex]);
        }
        catch (GroupNotFoundException gnfe) {
            System.err.println(gnfe);
        }
        return group;
    }

    /**
     * For security reasons, the remove operation is not supported. Use
     * ProfileManager.deleteGroup() instead.
     *
     * @see ProfileManager
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true if there are more groups left to iterate through backwards.
     */
    public boolean hasPrevious() {
        return (currentIndex > 0);
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
     * Returns the previous group.
     */
    public Object previous() throws java.util.NoSuchElementException {
        Group group = null;
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex++;
            throw new java.util.NoSuchElementException();
        }
        try {
            group = profileManager.getGroup(groups[currentIndex]);
        }
        catch (GroupNotFoundException gnfe) {
            System.err.println(gnfe);
        }
        return group;
    }

    /**
     * Returns the index number that would be returned with a call to previous().
     */
    public int previousIndex() {
        return currentIndex-1;
    }
}

