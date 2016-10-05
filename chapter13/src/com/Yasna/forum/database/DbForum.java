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

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageFilter;
import com.Yasna.forum.ForumNotFoundException;
import com.Yasna.forum.ForumPermissions;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.ForumThreadNotFoundException;
import com.Yasna.forum.ForumThreadProxy;
import com.Yasna.forum.Group;
import com.Yasna.forum.Query;
import com.Yasna.forum.SearchIndexer;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.YazdConstants;
import com.Yasna.forum.filter.FilterHtml;
import com.Yasna.forum.filter.FilterNewline;
import com.Yasna.util.Cache;
import com.Yasna.util.CacheSizes;
import com.Yasna.util.Cacheable;

/**
 * Database implementation of the Forum interface. It loads and stores forum
 * information from a a database.
 *
 * @see Forum
 */
public class DbForum implements Forum, Cacheable {

    /** DATABASE QUERIES **/
    private static final String ADD_THREAD =
        "UPDATE yazdThread set forumID=? WHERE threadID=?";
    protected static final String DELETE_THREAD = "DELETE FROM yazdThread WHERE threadID=?";
    private static final String THREAD_COUNT =
        "SELECT count(*) FROM yazdThread WHERE forumID=?";
    private static final String MESSAGE_COUNT =
        "SELECT count(*) FROM yazdThread, yazdMessage WHERE " +
        "yazdThread.forumID=? AND yazdThread.threadID=yazdMessage.threadID";
    private static final String ADD_USER_PERM =
        "INSERT INTO yazdUserPerm(forumID,userID,permission) VALUES(?,?,?)";
    private static final String REMOVE_USER_PERM =
        "DELETE FROM yazdUserPerm WHERE forumID=? AND userID=? AND permission=?";
    private static final String USERS_WITH_PERM =
        "SELECT DISTINCT userID FROM yazdUserPerm WHERE forumID=? AND permission=?";
    private static final String ADD_GROUP_PERM =
        "INSERT INTO yazdGroupPerm(forumID,groupID,permission) VALUES(?,?,?)";
    private static final String REMOVE_GROUP_PERM =
        "DELETE FROM yazdGroupPerm WHERE forumID=? AND groupID=? AND permission=?";
    private static final String GROUPS_WITH_PERM =
        "SELECT DISTINCT groupID FROM yazdGroupPerm WHERE forumID=? AND permission=?";
    private static final String LOAD_FILTERS =
        "SELECT filterObject, filterIndex FROM yazdFilter WHERE forumID=? ORDER BY filterIndex ASC";
    private static final String DELETE_FILTERS = "DELETE FROM yazdFilter WHERE forumID=?";
    private static final String ADD_FILTER =
        "INSERT INTO yazdFilter(forumID,filterIndex,filterObject) VALUES(?,?,?)";
    private static final String LOAD_PROPERTIES =
        "SELECT name, propValue FROM yazdForumProp WHERE forumID=?";
    private static final String DELETE_PROPERTIES =
        "DELETE FROM yazdForumProp WHERE forumID=?";
    private static final String INSERT_PROPERTY =
        "INSERT INTO yazdForumProp(forumID,name,propValue) VALUES(?,?,?)";
    private static final String LOAD_FORUM_BY_ID =
        "SELECT forumID, name, description, creationDate, modifiedDate, moderated FROM yazdForum WHERE forumID=?";
    private static final String LOAD_FORUM_BY_NAME =
        "SELECT forumID, name, description, creationDate, modifiedDate, moderated FROM yazdForum WHERE name=?";
    private static final String ADD_FORUM =
        "INSERT INTO yazdForum(forumID, name, description, creationDate, " +
        "modifiedDate, moderated) VALUES (?,?,?,?,?,?)";
    private static final String SAVE_FORUM =
        "UPDATE yazdForum SET name=?, description=?, creationDate=?, " +
        "modifiedDate=?, moderated=? WHERE forumID=?";
    private static final String UPDATE_FORUM_MODIFIED_DATE =
        "UPDATE yazdForum SET modifiedDate=? WHERE forumID=?";

    private int id = -1;
    private String name;
    private String description;
    private java.util.Date creationDate;
    private java.util.Date modifiedDate;
    private int moderated = 0;
    private ForumMessageFilter[] filters;
    private Properties properties;
    //Lock for saving state to database.
    private Object saveLock = new Object();

    private DbForumFactory factory;

    /**
     * Creates a new forum with the specified name and description.
     *
     * @param name the name of the forum.
     * @param description the description of the forum.
     * @param factory the DbForumFactory the forum is a part of.
     */
    protected DbForum(String name, String description, DbForumFactory factory) {
        this.id = DbSequenceManager.nextID("Forum");
        this.name = name;
        this.description = description;
        long now = System.currentTimeMillis();
        creationDate = new java.util.Date(now);
        modifiedDate = new java.util.Date(now);
        this.factory = factory;
        insertIntoDb();
        properties = new Properties();
        //Forums should start with an html filter by default for
        //security purposes.
        filters = new ForumMessageFilter[2];
        filters[0] = new FilterHtml();
        filters[1] = new FilterNewline();
        saveFiltersToDb();
        //**Commenting out below since it doesn't seem to work for some reason.
        //try {
        //    addForumMessageFilter(new FilterHtml(), 0);
        //    addForumMessageFilter(new FilterNewline(), 1);
        //}
        //catch (UnauthorizedException ue) {
        //     ue.printStackTrace();
        //}
    }

    /**
     * Loads a forum with the specified id.
     */
    protected DbForum(int id, DbForumFactory factory)
            throws ForumNotFoundException
    {
        this.id = id;
        this.factory = factory;
        loadFromDb();
        loadFiltersFromDb();
        loadProperties();
    }

    /**
     * Loads a forum with the specified name.
     */
    protected DbForum(String name, DbForumFactory factory)
            throws ForumNotFoundException
    {
        this.name = name;
        this.factory = factory;
        loadFromDb();
        loadFiltersFromDb();
        loadProperties();
    }

    //FROM THE FORUM INTERFACE//

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnauthorizedException {
        this.name = name;
        saveToDb();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws UnauthorizedException
    {
        this.description = description;
        saveToDb();
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.util.Date creationDate)
            throws UnauthorizedException
    {
       this.creationDate = creationDate;
       saveToDb();
    }

    public java.util.Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(java.util.Date modifiedDate)
            throws UnauthorizedException
    {
        this.modifiedDate = modifiedDate;
        saveToDb();
    }

    public String getProperty(String name) {
        return (String)properties.get(name);
    }

    public void setProperty(String name, String value)
            throws UnauthorizedException
    {
        properties.put(name, value);
        saveProperties();
    }

    public Enumeration propertyNames() {
        return properties.keys();
    }

    public boolean isModerated(int type) {
        return false;
        //if (type == Forum.THREAD) {
        //    return threadsModerated;
        //}
        //else {
        //    return messagesModerated;
        //}
    }

    public void setModerated(int type, boolean moderated)
            throws UnauthorizedException
    {
        //if (type == Forum.THREAD) {
        //   threadsModerated = moderated;
        //}
        //else {
        //    messagesModerated = moderated;
        //}
    }

    public ForumThread createThread(ForumMessage rootMessage)
        throws UnauthorizedException
    {
        //If the forum is moderated, the thread is not automatically
        //approved.
        boolean approved = true;
        return new DbForumThread(rootMessage, approved, this, factory);
    }

    public ForumMessage createMessage(User user)
        throws UnauthorizedException
    {
        //If the forum is moderated, the message is not automatically
        //approved.
        boolean approved = !isModerated(YazdConstants.MESSAGE);
        return new DbForumMessage(user, factory);
    }

    public void addThread(ForumThread thread) throws UnauthorizedException {
        boolean abortTransaction = false;
        boolean supportsTransactions = false;
        //Add message to db
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            supportsTransactions = con.getMetaData().supportsTransactions();
            if (supportsTransactions) {
                con.setAutoCommit(false);
            }

            pstmt = con.prepareStatement(ADD_THREAD);
            pstmt.setInt(1,id);
            pstmt.setInt(2,thread.getID());
            pstmt.executeUpdate();
            pstmt.close();

            //Now, insert the thread into the database.
            ((ForumThreadProxy)thread).insertIntoDb(con);
        }
        catch(Exception e) {
            e.printStackTrace();
            abortTransaction = true;
            return;
        }
        finally {
            try {
                if (supportsTransactions) {
                    if (abortTransaction == true) {
                        con.rollback();
                    }
                    else {
                        con.commit();
                    }
                }
            }
            catch (Exception e) { e.printStackTrace(); }
            try {
                if (supportsTransactions) {
                    con.setAutoCommit(true);
                }
                con.close();
            }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Since we added a thread, update the modified date of this thread.
        updateModifiedDate(thread.getModifiedDate());
    }

    public ForumThread getThread(int threadID) throws
            ForumThreadNotFoundException
    {
        return factory.getThread(threadID, this);
    }

    public void deleteThread(ForumThread thread) throws UnauthorizedException
    {
        //Delete all messages from the thread. Deleting the root
        //message will delete all submessages.
        ForumMessage message = thread.getRootMessage();
        thread.deleteMessage(message);
    }

    protected void deleteThreadRecord(int threadID) {

        //Delete the actual thread
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DELETE_THREAD);
            pstmt.setInt(1, threadID);
            pstmt.execute();
        }
        catch( Exception sqle ) {
            System.err.println("Error in DbForum:deleteThread()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Now, delete from cache
        Integer threadIDInteger = new Integer(threadID);
        factory.getCacheManager().remove(DbCacheManager.THREAD_CACHE, threadIDInteger);
    }

    public void moveThread(ForumThread thread, Forum forum)
        throws UnauthorizedException
    {
        //Ensure that thread belongs to this forum
        if (thread.getForum().getID() != this.id) {
            throw new IllegalArgumentException("The thread does not belong to this forum.");
        }

        //Modify the SQL record. Only the thread table has information about
        //forumID, so we only need to modify that record. The message records
        //underneath the thread can be left alone.
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADD_THREAD);
            pstmt.setInt(1,forum.getID());
            pstmt.setInt(2,thread.getID());
            pstmt.executeUpdate();
            pstmt.close();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum:addThread()-" + sqle);
            return;
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        DbCacheManager cacheManager = factory.getCacheManager();
        SearchIndexer indexer = factory.getSearchIndexer();

        //Remove both forums from cache.
        Integer key = new Integer(this.id);
        cacheManager.remove(DbCacheManager.FORUM_CACHE, key);
        key = new Integer(forum.getID());
        cacheManager.remove(DbCacheManager.FORUM_CACHE, key);

        //Remove thread from cache.
        key = new Integer(thread.getID());
        cacheManager.remove(DbCacheManager.THREAD_CACHE, key);

        //Loop through all messages in thread
        Iterator messages = thread.messages();
        while (messages.hasNext()) {
            ForumMessage message = (ForumMessage)messages.next();
            //Remove each message from cache.
            key = new Integer(message.getID());
            cacheManager.remove(DbCacheManager.MESSAGE_CACHE, key);
            //Remove and re-add every message to the search index.
            indexer.removeFromIndex(message);
            indexer.addToIndex(message);
        }

        // Update the modified date of thread
        Date now = new Date();
        thread.setModifiedDate(now);
        // Update the modified date of forum thread is now in
        forum.setModifiedDate(now);
    }

    public Iterator threads() {
        return new DbForumIterator(this, factory);
    }

    public Iterator threads(int startIndex, int numResults) {
        return new DbForumIterator(this, factory, startIndex, numResults);
    }

    public int getThreadCount() {
        int threadCount = 0;
        // Based on the id in the object, get the thread data from the database:
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(THREAD_COUNT);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            threadCount = rs.getInt(1 /*"threadCount"*/);
        }
        catch( SQLException sqle ) {
            System.err.println("DbForum:getThreadCount() failed: " + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return threadCount;
    }

    public int getMessageCount() {
        int messageCount = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(MESSAGE_COUNT);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            messageCount = rs.getInt(1 /*"messageCount"*/);
        }
        catch( SQLException sqle ) {
            System.err.println("DbForum:getMessageCount() failed: " + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return messageCount;
    }

    public Query createQuery() {
        return new DbQuery(this, factory);
    }

    public void addUserPermission(User user, int permissionType)
            throws UnauthorizedException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADD_USER_PERM);
            pstmt.setInt(1,id);
            pstmt.setInt(2,user.getID());
            pstmt.setInt(3,permissionType);
            pstmt.execute();
            //Remove user permissions from cache since they've changed.
            factory.getCacheManager().removeUserPerm(
                    new Integer(user.getID()),
                    new Integer(id)
            );
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum.java:" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void removeUserPermission(User user, int permissionType)
            throws UnauthorizedException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(REMOVE_USER_PERM);
            pstmt.setInt(1,id);
            pstmt.setInt(2,user.getID());
            pstmt.setInt(3,permissionType);
            pstmt.execute();
            //Remove user permissions from cache since they've changed.
            factory.getCacheManager().removeUserPerm(
                    new Integer(user.getID()),
                    new Integer(id)
            );
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum.java:" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int[] usersWithPermission(int permissionType)
            throws UnauthorizedException
    {
        int [] users = new int[0];
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(USERS_WITH_PERM);
            pstmt.setInt(1,id);
            pstmt.setInt(2,permissionType);
            ResultSet rs = pstmt.executeQuery();
            ArrayList userList = new ArrayList();
            while (rs.next()) {
                userList.add(new Integer(rs.getInt("userID")));
            }
            users = new int[userList.size()];
            for (int i=0; i<users.length; i++) {
                users[i] = ((Integer)userList.get(i)).intValue();
            }
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum.java:" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return users;
    }

    public void addGroupPermission(Group group, int permissionType)
            throws UnauthorizedException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADD_GROUP_PERM);
            pstmt.setInt(1,id);
            pstmt.setInt(2,group.getID());
            pstmt.setInt(3,permissionType);
            pstmt.execute();
            //Remove user permissions from cache since they've changed. Because
            //of the way that user perm cache is handled, it is easiest to
            //simply remove all the user perm cache for the forum. This is ok
            //since happens infrequently.
            factory.getCacheManager().remove(
                DbCacheManager.USER_PERMS_CACHE,
                new Integer(id)
            );
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum.java:" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void removeGroupPermission(Group group, int permissionType)
            throws UnauthorizedException
    {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(REMOVE_GROUP_PERM);
            pstmt.setInt(1,id);
            pstmt.setInt(2,group.getID());
            pstmt.setInt(3,permissionType);
            pstmt.execute();
            //Remove user permissions from cache since they've changed. Because
            //of the way that user perm cache is handled, it is easiest to
            //simply remove all the user perm cache for the forum. This is ok
            //since happens infrequently.
            factory.getCacheManager().remove(
                DbCacheManager.USER_PERMS_CACHE,
                new Integer(id)
            );
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum.java:" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int[] groupsWithPermission(int permissionType)
            throws UnauthorizedException
    {
        int [] groups = new int[0];
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(GROUPS_WITH_PERM);
            pstmt.setInt(1,id);
            pstmt.setInt(2,permissionType);
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
            System.err.println("Error in DbForum.groupsWithPermission:" + sqle);
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

    public ForumMessage applyFilters(ForumMessage message) {
        //Loop through filters and apply them
        for (int i=0; i < filters.length; i++) {
            message = filters[i].clone(message);
        }
        return message;
    }

    public ForumMessageFilter[] getForumMessageFilters()
            throws UnauthorizedException
    {
        ForumMessageFilter [] dbFilters = new ForumMessageFilter[filters.length];
        for (int i=0; i<filters.length; i++) {
            dbFilters[i] = new DbForumMessageFilter((ForumMessage)filters[i], this);
        }
        return dbFilters;
    }

    public void addForumMessageFilter(ForumMessageFilter filter)
            throws UnauthorizedException
    {
        ArrayList newFilters = new ArrayList(filters.length+1);
        for (int i=0; i<filters.length; i++) {
            newFilters.add(filters[i]);
        }
        newFilters.add(filter);
        ForumMessageFilter[] newArray = new ForumMessageFilter[newFilters.size()];
        for (int i=0; i<newArray.length; i++) {
            newArray[i] = (ForumMessageFilter)newFilters.get(i);
        }
        //Finally, overwrite filters with the new array
        filters = newArray;
        saveFiltersToDb();
    }

    public void addForumMessageFilter(ForumMessageFilter filter, int index)
            throws UnauthorizedException
    {
        ArrayList newFilters = new ArrayList(filters.length+1);
        for (int i=0; i<filters.length; i++) {
            newFilters.add(filters[i]);
        }
        newFilters.add(index, filter);
        ForumMessageFilter[] newArray = new ForumMessageFilter[newFilters.size()];
        for (int i=0; i<newArray.length; i++) {
            newArray[i] = (ForumMessageFilter)newFilters.get(i);
        }
        //Finally, overwrite filters with the new array
        filters = newArray;
        saveFiltersToDb();
    }

    public void removeForumMessageFilter(int index)
            throws UnauthorizedException
    {
        ArrayList newFilters = new ArrayList(filters.length);
        for (int i=0; i<filters.length; i++) {
            newFilters.add(filters[i]);
        }
        newFilters.remove(index);
        ForumMessageFilter[] newArray = new ForumMessageFilter[newFilters.size()];
        for (int i=0; i<newArray.length; i++) {
            newArray[i] = (ForumMessageFilter)newFilters.get(i);
        }
        //Finally, overwrite filters with the new array
        filters = newArray;
        saveFiltersToDb();
    }

    public ForumPermissions getPermissions(Authorization authorization) {
        int userID = authorization.getUserID();

        //Get the user perm cache for this forum
        Cache userPermCache = (Cache)factory.getCacheManager().get(
            DbCacheManager.USER_PERMS_CACHE,
            new Integer(id)
        );

        //Simple case: if cache is turned on and the user is already cached,
        //we can simply return the cached permissions.
        if (userPermCache != null) {
            ForumPermissions permissions =
                    (ForumPermissions)userPermCache.get(new Integer(userID));
            if (permissions != null) {
                return permissions;
            }
        }

        //Not so simple case: cache is not turned on or the user permissions
        //have not been cached yet.
        boolean isAnonymous = (userID == -1);
        boolean isUser = !isAnonymous;

        ForumPermissions finalPermissions = ForumPermissions.none();

        //Step 1 - Get permissions for the User. This includes anonymous
        //perms, "special user" perms, and the specific perms for the user.
        if (isUser) {
            ForumPermissions userPermissions = factory.getUserPermissions(userID, id);
            //Combine permissions
            finalPermissions = new ForumPermissions(finalPermissions, userPermissions);
        }
        //Add in anonymous perms.
        ForumPermissions anonyPermissions = null;
        if (userPermCache != null) {
            anonyPermissions = (ForumPermissions)userPermCache.get(new Integer(-1));
        }
        //Otherwise, do our own lookup.
        if (anonyPermissions == null) {
            anonyPermissions = factory.getUserPermissions(-1, id);
            //Add to cache so it will be there next time.
            if (userPermCache != null) {
                userPermCache.add(new Integer(-1), anonyPermissions);
            }
        }
        //Combine permissions
        finalPermissions = new ForumPermissions(finalPermissions, anonyPermissions);

        //If they are a valid user, figure out "any user" permissions.
        if (isUser) {
            ForumPermissions specialUserPermissions = null;
            //Check for cache
            if (userPermCache != null) {
                specialUserPermissions = (ForumPermissions)userPermCache.get(new Integer(0));
            }
            //Otherwise, do our own lookup.
            if (specialUserPermissions == null) {
                specialUserPermissions = factory.getUserPermissions(0, id);
                //Add to cache so it will be there next time.
                if (userPermCache != null) {
                    userPermCache.add(new Integer(0), specialUserPermissions);
                }
            }
            //Combine permissions
            finalPermissions = new ForumPermissions(finalPermissions, specialUserPermissions);
        }

        //Step 2 -- get Permissions for all groups the user is in.
        int [] groups = ((DbProfileManager)factory.getProfileManager()).getUserGroups(userID);
        for (int i=0; i<groups.length; i++) {
            ForumPermissions groupPermissions = factory.getGroupPermissions(groups[i], id);
            finalPermissions = new ForumPermissions(finalPermissions, groupPermissions);
        }

        //Finally, add user to cache so it will be there next time.
        if (isUser && userPermCache != null) {
            userPermCache.add(new Integer(userID), finalPermissions);
        }

        return finalPermissions;
    }

    public boolean hasPermission(int type) {
        return true;
    }

    //FROM THE CACHEABLE INTERFACE//

    public int getSize() {
        //Approximate the size of the object in bytes by calculating the size
        //of each field.
        int size = 0;
        size += CacheSizes.sizeOfObject();              //overhead of object
        size += CacheSizes.sizeOfInt();                 //id
        size += CacheSizes.sizeOfString(name);          //name
        size += CacheSizes.sizeOfString(description);   //description
        size += CacheSizes.sizeOfDate();                //creation date
        size += CacheSizes.sizeOfDate();                //modified date
        size += CacheSizes.sizeOfInt();                 //moderated
        size += filters.length * 8;                     //each filter is 8 bytes
        size += CacheSizes.sizeOfProperties(properties);//properties object
        size += CacheSizes.sizeOfObject();              //save lock

        return size;
    }

    //OTHER METHODS

    /**
     * Returns a String representation of the Forum object using the forum name.
     *
     * @return a String representation of the Forum object.
     */
    public String toString() {
        return name;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && object instanceof DbForum) {
            return id == ((DbForum)object).getID();
        }
        else {
            return false;
        }
    }

    /**
     * Updates the modified date but doesn't require a security check since
     * it is a protected method.
     */
    protected void updateModifiedDate(java.util.Date modifiedDate) {
        this.modifiedDate = modifiedDate;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(UPDATE_FORUM_MODIFIED_DATE);
            pstmt.setString(1, ""+modifiedDate.getTime());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum:updateModifiedDate()-" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Loads forum properties from the database.
     */
    private void loadProperties() {
        synchronized(saveLock) {
            Properties newProps = new Properties();
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(LOAD_PROPERTIES);
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()) {
                    String name = rs.getString("name");
                    String value = rs.getString("propValue");
                    newProps.put(name, value);
                }
            }
            catch( SQLException sqle ) {
                System.err.println("Error in DbForum:loadProperties():" + sqle);
                sqle.printStackTrace();
            }
            finally {
                try {  pstmt.close(); }
                catch (Exception e) { e.printStackTrace(); }
                try {  con.close();   }
                catch (Exception e) { e.printStackTrace(); }
            }
            this.properties = newProps;
        }
    }

    /**
     * Saves forum properties to the database.
     */
    private void saveProperties() {
        synchronized(saveLock) {
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DbConnectionManager.getConnection();
                //Delete all old values.
                pstmt = con.prepareStatement(DELETE_PROPERTIES);
                pstmt.setInt(1, id);
                pstmt.execute();
                pstmt.close();
                //Now insert new values.
                pstmt = con.prepareStatement(INSERT_PROPERTY);
                Enumeration enum = properties.keys();
                while (enum.hasMoreElements()) {
                    String name = (String)enum.nextElement();
                    String value = (String)properties.get(name);
                    pstmt.setInt(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, value);
                    pstmt.executeUpdate();
                }
            }
            catch( SQLException sqle ) {
                System.err.println(sqle);
            }
            finally {
                try {  pstmt.close(); }
                catch (Exception e) { e.printStackTrace(); }
                try {  con.close();   }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Loads filters from the database.
     */
    private void loadFiltersFromDb() {
        ArrayList newFilters = new ArrayList();
        Connection con = null;
        boolean abort = false;
        boolean supportsTransactions = false;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            supportsTransactions = con.getMetaData().supportsTransactions();
            if (supportsTransactions) {
                 con.setAutoCommit(false);
            }

            pstmt = con.prepareStatement(LOAD_FILTERS);
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                try {
                    ObjectInputStream in = new ObjectInputStream(rs.getBinaryStream("filterObject"));
                    newFilters.add(in.readObject());
                }
                catch (ClassCastException cce) {
                    //ignore for now since the filter might be updated. we
                    //need a solution for this. probably custom class loading
                    //of filter classes to protect against failure like this.
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
                try {
                    if (supportsTransactions) {
                        if (abort == true) {
                            con.rollback();
                        }
                        else {
                            con.commit();
                        }
                    }
                }
                catch (Exception e) { e.printStackTrace(); }
                try {
                    if (supportsTransactions) {
                        con.setAutoCommit(true);
                    }
                }
                catch (Exception e) { e.printStackTrace(); }
                try {  con.close();   }
                catch (Exception e) { e.printStackTrace(); }

        }
        filters = new ForumMessageFilter[newFilters.size()];
        for (int i=0; i<filters.length; i++) {
            filters[i] = (ForumMessageFilter)newFilters.get(i);
        }
        //Finally, save filters back to Db. Effectively, this deletes filters
        //from the database that failed to load. See note above.
        //saveFiltersToDb(); <<-- commenting out to try to fix filters bug.
    }

    /**
     * Saves filters to the database. Filter saving works by serializing
     * each filter to a byte stream and then inserting that stream into
     * the database.
     */
    protected void saveFiltersToDb() {
        boolean abort = false;
        boolean supportsTransactions = false;
        synchronized (saveLock) {
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DbConnectionManager.getConnection();

                supportsTransactions = con.getMetaData().supportsTransactions();
                if (supportsTransactions) {
                    con.setAutoCommit(false);
                }

                pstmt = con.prepareStatement(DELETE_FILTERS);
                pstmt.setInt(1,id);
                pstmt.execute();
                //Now insert new list of filters.
                pstmt.close();
                pstmt = con.prepareStatement(ADD_FILTER);
                for (int i=0; i<filters.length; i++) {
                    try {
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(byteOut);
                        out.writeObject(filters[i]);
                        pstmt.setInt(1,id);
                        pstmt.setInt(2,i);
                        pstmt.setBytes(3,byteOut.toByteArray());
                        pstmt.execute();
                    }
                    catch (Exception e) {
                        abort = true;
                        e.printStackTrace();
                    }
                }
                pstmt.close();
            }
            catch( SQLException sqle ) {
                abort = true;
                sqle.printStackTrace();
            }
            finally {
                try {
                    if (supportsTransactions) {
                        if (abort == true) {
                            con.rollback();
                        }
                        else {
                            con.commit();
                        }
                    }
                }
                catch (Exception e) { e.printStackTrace(); }
                try {
                    if (supportsTransactions) {
                        con.setAutoCommit(true);
                    }
                }
                catch (Exception e) { e.printStackTrace(); }
                try {  con.close();   }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Loads forum data from the database.
     */
    private void loadFromDb() throws ForumNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //See if we should load by forumID or by name
            if (id == -1) {
                pstmt = con.prepareStatement(LOAD_FORUM_BY_NAME);
                pstmt.setString(1,name);
            }
            else {
                pstmt = con.prepareStatement(LOAD_FORUM_BY_ID);
                pstmt.setInt(1, id);
            }
            ResultSet rs = pstmt.executeQuery();
            if( !rs.next() ) {
                throw new ForumNotFoundException("Forum " + getID() +
                    " could not be loaded from the database.");
            }
            id = rs.getInt("forumID");
            name = rs.getString("name");
            description = rs.getString("description");
            this.creationDate =
                new java.util.Date(Long.parseLong(rs.getString("creationDate").trim()));
            this.modifiedDate =
                new java.util.Date(Long.parseLong(rs.getString("modifiedDate").trim()));
            moderated = rs.getInt("moderated");
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
            throw new ForumNotFoundException("Forum " + getID() +
                " could not be loaded from the database.");
        }
        catch (NumberFormatException nfe) {
            System.err.println("WARNING: In DbForum.loadFromDb() -- there " +
                "was an error parsing the dates returned from the database. Ensure " +
                "that they're being stored correctly.");
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Inserts a new record into the database.
     */
    private void insertIntoDb() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADD_FORUM);
            pstmt.setInt(1,id);
            pstmt.setString(2,name);
            pstmt.setString(3,description);
            pstmt.setString(4, Long.toString(creationDate.getTime()));
            pstmt.setString(5, Long.toString(modifiedDate.getTime()));
            pstmt.setInt(6, moderated);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum:insertIntoDb()-" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Saves forum data to the database.
     */
    private synchronized void saveToDb() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SAVE_FORUM);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, Long.toString(creationDate.getTime()));
            pstmt.setString(4, Long.toString(modifiedDate.getTime()));
            pstmt.setInt(5, moderated);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForum:saveToDb()-" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }
}
