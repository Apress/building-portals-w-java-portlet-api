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

import com.Yasna.forum.Forum;
import com.Yasna.forum.Group;
import com.Yasna.forum.GroupAlreadyExistsException;
import com.Yasna.forum.GroupNotFoundException;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserAlreadyExistsException;
import com.Yasna.forum.UserNotFoundException;
import com.Yasna.util.CacheableInteger;

/**
 * Database implementation of the ProfileManager interface.
 */
public class DbProfileManager implements ProfileManager {

    /** DATABASE QUERIES **/
    private static final String USER_GROUPS =
        "SELECT groupID from yazdGroupUser WHERE userID=?";
    private static final String USER_MESSAGE_COUNT =
        "SELECT count(*) FROM yazdMessage,yazdForum,yazdThread WHERE " +
        "yazdMessage.userID=? AND yazdForum.forumID=? AND " +
        "yazdThread.forumID=yazdForum.forumID AND " +
        "yazdMessage.threadID=yazdThread.threadID";
    private static final String USER_COUNT = "SELECT count(*) FROM yazdUser";
    private static final String ALL_USER_MESSAGES =
        "SELECT messageID FROM yazdMessage WHERE userID=?";
    private static final String DELETE_USER_MESSAGES =
        "UPDATE yazdMessage set userID=-1 WHERE userID=?";
    private static final String DELETE_USER_PERMS =
        "DELETE FROM yazdUserPerm WHERE userID=?";
    private static final String DELETE_USER_GROUPS =
        "DELETE FROM yazdGroupUser WHERE userID=?";
    private static final String DELETE_USER_PROPS =
        "DELETE FROM yazdUserProp WHERE userID=?";
    private static final String DELETE_USER =
        "DELETE FROM yazdUser WHERE userID=?";
    private static final String GROUP_COUNT = "SELECT count(*) FROM yazdGroup";
    private static final String DELETE_GROUP_USERS =
        "DELETE FROM yazdGroupUser WHERE groupID=?";
    private static final String DELETE_GROUP =
        "DELETE FROM yazdGroup WHERE groupID=?";

    private User anonymousUser = null;
    private User specialUser = null;
    private DbForumFactory factory;

    /**
     * Creates a new ProfileManager.
     */
    public DbProfileManager(DbForumFactory factory) {
        this.factory = factory;
        try {
            anonymousUser = getUser(-1);
            specialUser = getUser(0);
        }
        catch (UserNotFoundException unfe) {  }
    }

    //FROM THE PROFILEMANAGER INTERFACE//

    public User createUser(String username, String password, String email)
            throws UserAlreadyExistsException
    {
        User newUser = null;
        try {
            User existingUser = getUser(username);

            //The user already exists since now exception, so:
            throw new UserAlreadyExistsException();
        }
        catch (UserNotFoundException unfe) {
            //The user doesn't already exist so we can create a new user
            newUser = new DbUser(username, password, email);
        }
        return newUser;
    }

    public User getUser(int userID) throws UserNotFoundException {
        DbCacheManager cacheManager = factory.getCacheManager();
        //If cache is not enabled, do a new lookup of object
        if (!cacheManager.isCacheEnabled()) {
            return new DbUser(userID);
        }
        //Cache is enabled.
        Integer userIDInteger = new Integer(userID);
        DbUser user = (DbUser)cacheManager.get(
                DbCacheManager.USER_CACHE,
                userIDInteger
        );
        if(user == null) {
            user = new DbUser(userID);
            cacheManager.add(DbCacheManager.USER_CACHE, userIDInteger, user);
        }
        return user;
    }

    public User getUser(String username) throws UserNotFoundException {
        DbCacheManager cacheManager = factory.getCacheManager();
        //If cache is not enabled, do a new lookup of object
        if (!cacheManager.isCacheEnabled()) {
            User user = new DbUser(username);
            return getUser(user.getID());
        }
        //Cache is enabled.
        CacheableInteger userIDInteger = (CacheableInteger)cacheManager.get(
                DbCacheManager.USER_ID_CACHE,
                username
        );
        //if id wan't found in cache, load it up and put it there.
        if (userIDInteger == null) {
            User user = new DbUser(username);
            userIDInteger = new CacheableInteger(new Integer(user.getID()));
            cacheManager.add(DbCacheManager.USER_ID_CACHE, username, userIDInteger);
        }
        return getUser(userIDInteger.getInteger().intValue());
    }

    public User getAnonymousUser() {
        return anonymousUser;
    }

    public User getSpecialUser() {
        return specialUser;
    }

    public void deleteUser(User user) throws UnauthorizedException {
        int userID = user.getID();
        int [] messages;
        //Get array of all user's messages in the system so that
        //we can expire them from cache.
        ArrayList tempMessages = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ALL_USER_MESSAGES);
            pstmt.setInt(1, user.getID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tempMessages.add(new Integer(rs.getInt("messageID")));
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbProfileManager:deleteUser()-" + sqle);
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

        con = null;
        pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //mark all message by user as anonymous
            pstmt = con.prepareStatement(DELETE_USER_MESSAGES);
            pstmt.setInt(1,userID);
            pstmt.execute();
            pstmt.close();
            //remove all permissions given to user
            pstmt = con.prepareStatement(DELETE_USER_PERMS);
            pstmt.setInt(1,userID);
            pstmt.execute();
            pstmt.close();
            //remove user from all groups
            pstmt = con.prepareStatement(DELETE_USER_GROUPS);
            pstmt.setInt(1,userID);
            pstmt.execute();
            pstmt.close();
            //delete all of the users's extended properties
            pstmt = con.prepareStatement(DELETE_USER_PROPS);
            pstmt.setInt(1,userID);
            pstmt.execute();
            pstmt.close();
            //delete the actual user entry
            pstmt = con.prepareStatement(DELETE_USER);
            pstmt.setInt(1,userID);
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Finally, expire all relevant caches
        //all of users's messages
        DbCacheManager cacheManager = factory.getCacheManager();
        for (int i=0; i<messages.length; i++) {
            cacheManager.remove(
                    DbCacheManager.MESSAGE_CACHE,
                    new Integer(messages[i])
            );
        }
        //user cache
        cacheManager.remove(DbCacheManager.USER_ID_CACHE, user.getUsername());
        cacheManager.remove(DbCacheManager.USER_CACHE, new Integer(userID));
    }

    public Group createGroup(String name) throws UnauthorizedException,
            GroupAlreadyExistsException
    {
        Group newGroup = null;
        try {
            Group existingGroup = getGroup(name);

            //The group already exists since now exception, so:
            throw new GroupAlreadyExistsException();
        }
        catch (GroupNotFoundException unfe) {
            //The group doesn't already exist so we can create a new group
            newGroup = new DbGroup(name, factory);
        }
        return newGroup;
    }

    public Group getGroup(int groupID) throws GroupNotFoundException {
        DbCacheManager cacheManager = factory.getCacheManager();
        //If cache is not enabled, do a new lookup of object
        if (!cacheManager.isCacheEnabled()) {
            return new DbGroup(groupID, factory);
        }
        //Cache is enabled.
        Integer groupIDInteger = new Integer(groupID);
        DbGroup group = (DbGroup)cacheManager.get(
                DbCacheManager.GROUP_CACHE,
                groupIDInteger
        );
        if(group == null) {
            group = new DbGroup(groupID, factory);
            cacheManager.add(DbCacheManager.GROUP_CACHE, groupIDInteger, group);
        }
        return group;
    }

    public Group getGroup(String name) throws GroupNotFoundException {
        DbCacheManager cacheManager = factory.getCacheManager();
        //If cache is not enabled, do a new lookup of object
        if (!cacheManager.isCacheEnabled()) {
            Group group = new DbGroup(name, null, factory);
            return getGroup(group.getID());
        }
        //Cache is enabled.
        CacheableInteger groupIDInteger = (CacheableInteger)cacheManager.get(
                DbCacheManager.GROUP_ID_CACHE,
                name
        );
        //if id wan't found in cache, load it up and put it there.
        if (groupIDInteger == null) {
            Group group = new DbGroup(name, null, factory);
            groupIDInteger = new CacheableInteger(new Integer(group.getID()));
            cacheManager.add(DbCacheManager.GROUP_ID_CACHE, name, groupIDInteger);
        }
        return getGroup(groupIDInteger.getInteger().intValue());
    }

    public void deleteGroup(Group group) throws UnauthorizedException {
        int groupID = group.getID();
        int [] members = new int[group.getMemberCount()];
        Iterator iter = group.members();
        for (int i=0; i<members.length; i++) {
            User user = (User)iter.next();
            members[i] = user.getID();
        }

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //mark all message by user as anonymous
            pstmt = con.prepareStatement(DELETE_GROUP_USERS);
            pstmt.setInt(1,groupID);
            pstmt.execute();
            pstmt.close();
            //remove all permissions given to user
            pstmt = con.prepareStatement(DELETE_GROUP);
            pstmt.setInt(1,groupID);
            pstmt.execute();
            pstmt.close();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Finally, expire all relevant caches
        DbCacheManager cacheManager = factory.getCacheManager();
        cacheManager.remove(DbCacheManager.GROUP_ID_CACHE, group.getName());
        cacheManager.remove(DbCacheManager.GROUP_CACHE, new Integer(groupID));
        //Removing a group can change the permissions of all the users in that
        //group. Therefore, remove each user from the user perms cache.
        for (int i=0; i<members.length; i++) {
            cacheManager.removeUserPerm(new Integer(members[i]));
        }
    }

    public int getUserCount() {
        int count = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(USER_COUNT);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return count;
    }

    public int getGroupCount() {
        int count = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(GROUP_COUNT);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return count;
    }

    public Iterator users() {
        return new DbUserIterator(this);
    }

    public Iterator users(int startIndex, int numResults) {
        return new DbUserIterator(this, startIndex, numResults);
    }

    public Iterator groups() {
        return new DbGroupIterator(this);
    }

    public Iterator groups(int startIndex, int numResults) {
        return new DbGroupIterator(this, startIndex, numResults);
    }

    public int userMessageCount(User user, Forum forum) {
        int count = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(USER_MESSAGE_COUNT);
            pstmt.setInt(1, user.getID());
            pstmt.setInt(2, forum.getID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return count;
    }

    public Iterator userMessages(User user, Forum forum) {
        return new DbUserMessagesIterator(factory, user, forum);
    }

    /**
     * Returns an array of all the groups that the user belongs to.
     */
    protected int[] getUserGroups(int userID) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int [] groups = new int[0];
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(USER_GROUPS);
            pstmt.setInt(1,userID);
            ResultSet rs = pstmt.executeQuery();
            ArrayList groupList = new ArrayList();
            while (rs.next()) {
                groupList.add(new Integer(rs.getInt("groupID")));
            }
            groups = new int[groupList.size()];
            for (int i=0; i<groups.length; i++) {
                groups[i] = ((Integer)groupList.get(i)).intValue();
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return groups;
    }
}
