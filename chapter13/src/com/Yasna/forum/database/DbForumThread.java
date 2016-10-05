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
import java.util.Date;
import java.util.Iterator;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageNotFoundException;
import com.Yasna.forum.ForumMessageProxy;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.ForumThreadNotFoundException;
import com.Yasna.forum.TreeWalker;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.util.CacheSizes;
import com.Yasna.util.Cacheable;

/**
 * Database implementation of the ForumThread interface.
 *
 * @see ForumThread
 */
public class DbForumThread implements ForumThread, Cacheable {

    /** DATABASE QUERIES **/
    private static final String MESSAGE_COUNT =
        "SELECT count(*) FROM yazdMessage WHERE threadID=?";
    private static final String ADD_MESSAGE =
        "INSERT INTO yazdMessageTree(parentID,childID) VALUES(?,?)";
    private static final String MOVE_MESSAGE =
        "UPDATE yazdMessageTree SET parentID=? WHERE childID=?";
    private static final String CHANGE_MESSAGE_THREAD =
        "UPDATE yazdMessage SET threadID=? WHERE messageID=?";
    private static final String UPDATE_THREAD_MODIFIED_DATE =
        "UPDATE yazdThread SET modifiedDate=? WHERE threadID=?";
    private static final String DELETE_MESSAGE1 =
        "DELETE FROM yazdMessageTree WHERE childID=?";
    private static final String DELETE_MESSAGE2 =
        "DELETE FROM yazdMessage WHERE messageID=?";
    private static final String DELETE_MESSAGE_PROPERTIES =
        "DELETE FROM yazdMessageProp WHERE messageID=?";
    private static final String LOAD_THREAD =
        "SELECT rootMessageID, creationDate, modifiedDate FROM yazdThread WHERE threadID=?";
    private static final String INSERT_THREAD =
        "INSERT INTO yazdThread(threadID,forumID, rootMessageID,creationDate," +
        "modifiedDate,approved) VALUES(?,?,?,?,?,?)";
    private static final String SAVE_THREAD =
        "UPDATE yazdThread SET rootMessageID=?, creationDate=?, " +
        "modifiedDate=? WHERE threadID=?";

    private int id = -1;
    //Temporary object reference for when inserting new record.
    private ForumMessage rootMessage;
    private int rootMessageID;
    private java.util.Date creationDate;
    private java.util.Date modifiedDate;
    private boolean approved;

    /**
     * Indicates if the object is ready to be saved or not. An object is not
     * ready to be saved if it has just been created and has not yet been added
     * to its container. For example, a message added to a thread, etc.
     */
    private boolean isReadyToSave = false;

    /**
     * The forum allows us access to the message filters.
     */
    private DbForum forum;

    /**
     * The factory provides services such as db connections and logging.
     */
    private DbForumFactory factory;

    /**
     * Creates a new DbForumThread. The supplied message object is used to
     * derive the name of the thread (subject of message), as well as the
     * creation date and modified date of thread.
     *
     * @param rootMessage the root message of the thread.
     */
    protected DbForumThread(ForumMessage rootMessage, boolean approved,
            DbForum forum, DbForumFactory factory) throws UnauthorizedException
    {
        this.id = DbSequenceManager.nextID("ForumThread");
        this.forum = forum;
        this.factory = factory;
        this.rootMessage = rootMessage;
        this.rootMessageID = rootMessage.getID();
        //Set the creation and modified dates to be the same as those of
        //root message.
        long rootMessageTime = rootMessage.getCreationDate().getTime();
        this.creationDate = new java.util.Date(rootMessageTime);
        this.modifiedDate = new java.util.Date(rootMessageTime);
        this.approved = approved;
    }

    /**
     * Loads a DbForumThread from the database based on its id.
     *
     * @param id in unique id of the ForumThread to load.
     * @param forum the Forum that the thread belongs to.
     * @param factory a ForumFactory to use for loading.
     */
    protected DbForumThread(int id, DbForum forum, DbForumFactory factory)
            throws ForumThreadNotFoundException
    {
        this.id = id;
        this.forum = forum;
        this.factory = factory;
        loadFromDb();
        isReadyToSave = true;
    }

    //FROM THE FORUMMESSAGE INTERFACE//

    public int getID() {
        return id;
    }

    public String getName() {
        return getRootMessage().getSubject();
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.util.Date creationDate)
            throws UnauthorizedException
    {
        this.creationDate = creationDate;
        //Only save to the db if the object is ready
        if (!isReadyToSave) {
            return;
        }
        saveToDb();
    }

    public java.util.Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(java.util.Date modifiedDate)
            throws UnauthorizedException
    {
        this.modifiedDate = modifiedDate;
        //Only save to the db if the object is ready
        if (!isReadyToSave) {
            return;
        }
        saveToDb();
    }

    public Forum getForum() {
        return forum;
    }

    public ForumMessage getMessage(int messageID)
            throws ForumMessageNotFoundException
    {
        ForumMessage message = factory.getMessage(messageID);

        //Apply filters to message.
        message = forum.applyFilters(message);
        return message;
    }

    public ForumMessage getRootMessage()  {
        try {
            return getMessage(rootMessageID);
        }
        catch (ForumMessageNotFoundException e) {
            System.err.println("Could not load root message with id " + rootMessageID);
            e.printStackTrace();
            return null;
        }
        /*DbForumMessage message = (DbForumMessage)factory.cacheManager.get(
                DbCacheManager.MESSAGE_CACHE,
                new Integer(rootMessageID)
        );
        if (message == null) {
            //Load and add to cache
            try {
                message = new DbForumMessage(rootMessageID, factory);
                factory.cacheManager.add(DbCacheManager.MESSAGE_CACHE, new Integer(rootMessageID), message);
            }
            catch (ForumMessageNotFoundException e) {
                System.err.println("Could not load root message with id " + rootMessageID);
                e.printStackTrace();
            }
        }
        return message;
        */
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
            messageCount = rs.getInt(1);
        }
        catch( SQLException sqle ) {
            System.err.println("DbForumThread:getMessageCount() failed: "+sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return messageCount;
    }

    public void addMessage(ForumMessage parentMessage, ForumMessage newMessage) {
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

            //Now, insert the message into the database.
            //Now, insert the message into the database.
            ((ForumMessageProxy)newMessage).insertIntoDb(con, this);

            pstmt = con.prepareStatement(ADD_MESSAGE);
            pstmt.setInt(1, parentMessage.getID());
            pstmt.setInt(2, newMessage.getID());
            pstmt.executeUpdate();
            pstmt.close();
        }
        catch( Exception e ) {
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

        //Added new message, so update the modified date of this thread
        updateModifiedDate(newMessage.getModifiedDate());
        //Also, update the modified date of the forum
        DbForum dbForum = (DbForum)factory.cacheManager.get(
                DbCacheManager.FORUM_CACHE,
                new Integer(forum.getID())
            );
        if (dbForum != null) {
            dbForum.updateModifiedDate(modifiedDate);
        }
        else {
            forum.updateModifiedDate(modifiedDate);
        }
    }

    public void deleteMessage(ForumMessage message)
            throws UnauthorizedException
    {
        //Skip null messages or the case that we're already deleting the thread.
        if (message == null) {
            return;
        }
        //If the message does not belong to this thread, don't perform delete.
        if (message.getForumThread().getID() != this.id) {
            throw new IllegalArgumentException("Message " + message.getID() +
                " could not be deleted. It belongs to thread " +
                message.getForumThread().getID() + ", and not thread " +
                this.id + ".");
        }

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            //Delete the message from the parent/child table
            pstmt = con.prepareStatement(DELETE_MESSAGE1);
            pstmt.setInt(1, message.getID());
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForumThread:deleteMessage()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Recursively delete all children
        TreeWalker walker = treeWalker();
        int childCount = walker.getChildCount(message);
        for (int i=childCount-1; i>=0; i--) {
            ForumMessage childMessage = walker.getChild(message, i);
            if (childMessage == null) {
                System.err.println("child message was null -- index " + i);
            }
            deleteMessage(childMessage);
        }

        try {
            //Delete the actual message.
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(DELETE_MESSAGE2);
            pstmt.setInt(1, message.getID());
            pstmt.execute();
            pstmt.close();

            //Delete any message properties.
            pstmt = con.prepareStatement(DELETE_MESSAGE_PROPERTIES);
            pstmt.setInt(1, message.getID());
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForumThread:deleteMessage()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Now, delete from the cache.
        factory.getCacheManager().remove(
                DbCacheManager.MESSAGE_CACHE,
                new Integer(message.getID())
        );

        //Finally, delete it from the search index
        factory.getSearchIndexer().removeFromIndex(message);

        //Now, make sure that the message being deleted isn't the root message
        //of this thread. If it is, the whole thread should just be deleted.
        if (message.getID() == this.rootMessageID) {
            forum.deleteThreadRecord(this.id);
        }
    }

    public void moveMessage(ForumMessage message, ForumThread newThread,
            ForumMessage parentMessage)
            throws UnauthorizedException, IllegalArgumentException
    {
        if (message.getForumThread().getID() != this.id ||
                parentMessage.getForumThread().getID() != newThread.getID())
        {
            throw new IllegalArgumentException(
                "The messages and threads did not match."
            );
        }

        // Save the mesageID of message to move
        int messageID = message.getID();
        // Original message thread rootMessageID
        int oldRootMessageID = getRootMessage().getID();

        // Move the children of this message to the new thread
        TreeWalker walker = treeWalker();
        int childCount = walker.getChildCount(message);
        for (int i=0; i<childCount; i++) {
            ForumMessage childMessage = walker.getChild(message, i);
            changeMessageThread(childMessage, newThread);
        }

        //Move the message to the new thread.
        changeMessageThread(message, newThread);

        //Make message a child of parentMessage
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();

            if( oldRootMessageID != messageID ) {
                pstmt = con.prepareStatement(MOVE_MESSAGE);
                pstmt.setInt(1, parentMessage.getID());
                pstmt.setInt(2, messageID);
            }
            else {
                pstmt = con.prepareStatement(ADD_MESSAGE);
                pstmt.setInt(1, parentMessage.getID());
                pstmt.setInt(2, messageID);
            }

            pstmt.executeUpdate();
            pstmt.close();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForumThread:moveMessage()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Update the modified date of newThread
        Date now = new Date();
        newThread.setModifiedDate(now);
        //Update the modified date of newThread forum
        newThread.getForum().setModifiedDate(now);

        //Thread has been modified, invalidate the cache
        DbCacheManager cacheManager = factory.getCacheManager();
        Integer key = new Integer(this.id);
        cacheManager.remove(DbCacheManager.THREAD_CACHE, key);

        //If we moved the root message of this thread, the thread should be
        //deleted. Normally, deleting a thread will delete all of it's messages.
        //However, we've already adjusted the thread/message relationship at the
        //SQL level and removed the thread from cache. That should mean we're safe.
        if (getRootMessage().getID() == messageID) {
            //rootMessage = null;
            this.getForum().deleteThread(this);
        }
    }

    public TreeWalker treeWalker() {
        return new DbTreeWalker(this, factory);
    }

    public Iterator messages() {
        return new DbThreadIterator(this);
    }

    public Iterator messages(int startIndex, int numResults) {
        return new DbThreadIterator(this, startIndex, numResults);
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
        size += CacheSizes.sizeOfDate();                //creation date
        size += CacheSizes.sizeOfDate();                //modified date
        size += CacheSizes.sizeOfBoolean();             //approved
        size += CacheSizes.sizeOfObject();              //ref to rootMessage
        size += CacheSizes.sizeOfObject();              //ref to forum
        size += CacheSizes.sizeOfObject();              //ref to factory
        size += CacheSizes.sizeOfBoolean();             //ready save var
        size += CacheSizes.sizeOfBoolean();             //deleting var

        return size;
    }

    //OTHER METHODS//

    /**
     * Converts the object to a String by returning the name of the thread.
     * This functionality is primarily for Java applications that might be
     * accessing Yazd objects through a GUI.
     */
    public String toString() {
        return getName();
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && object instanceof DbForumThread) {
            return id == ((DbForumThread)object).getID();
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
            pstmt = con.prepareStatement(UPDATE_THREAD_MODIFIED_DATE);
            pstmt.setString(1, ""+modifiedDate.getTime());
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForumThread:updateModifiedDate()-" + sqle);
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
     * Moves a message to a new thread by modifying the message table threadID
     * column.
     *
     * @param message the message to move.
     * @param newThread the thread to move the message to.
     */
    private void changeMessageThread(ForumMessage message, ForumThread newThread)
        throws UnauthorizedException
    {
        //Remove message from the search index
        factory.getSearchIndexer().removeFromIndex(message);

        //Remove message from cache.
        DbCacheManager cacheManager = factory.getCacheManager();
        Integer key = new Integer(message.getID());
        cacheManager.remove(DbCacheManager.MESSAGE_CACHE, key);

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(CHANGE_MESSAGE_THREAD);
            pstmt.setInt(1, newThread.getID());
            pstmt.setInt(2, key.intValue());
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }

        // Add message back to search index and update modified date
        try {
            ForumMessage movedMessage = newThread.getMessage(key.intValue());
            factory.getSearchIndexer().addToIndex(movedMessage);
            movedMessage.setModifiedDate(new Date());
        }
        catch(ForumMessageNotFoundException e) {
            System.err.println("Error in DbForumThread:changeMessageThread()-" +
            "messageID=" + key.intValue() + "newThreadID=" + newThread.getID());
        }

        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Loads a ForumThread from the database.
     */
    private void loadFromDb() throws ForumThreadNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(LOAD_THREAD);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if( !rs.next() ) {
                throw new ForumThreadNotFoundException("Thread " + id +
                    " could not be loaded from the database.");
            }
            //try {
                rootMessageID = rs.getInt("rootMessageID");
            //}
            //catch (ForumMessageNotFoundException fmnfe) {
            //    System.err.println("Error: could not load root message of thread "
            //        + id + ". The database record could be corrupt.");
            //    fmnfe.printStackTrace();
            //}
            creationDate = new java.util.Date(Long.parseLong(rs.getString("creationDate").trim()));
            modifiedDate =  new java.util.Date(Long.parseLong(rs.getString("modifiedDate").trim()));
            pstmt.close();
        }
        catch( SQLException sqle ) {
            throw new ForumThreadNotFoundException("Thread " + id +
                " could not be loaded from the database.");
        }
        catch (NumberFormatException nfe) {
            System.err.println("WARNING: In DbForumThread.loadFromDb() -- there " +
                "was an error parsing the dates returned from the database. Ensure " +
                "that they're being stored correctly.");
        }
        finally {
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Inserts a new forum thread into the database. A connection object must
     * be passed in. The connection must be open when passed in, and will
     * remain open when passed back. This method allows us to make insertions
     * be transactional.
     *
     * @param con an open Connection used to insert the thread to the db.
     */
    public void insertIntoDb(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(INSERT_THREAD);
        pstmt.setInt(1, id);
        pstmt.setInt(2, forum.getID());
        pstmt.setInt(3, rootMessageID);
        pstmt.setString(4, Long.toString(creationDate.getTime()));
        pstmt.setString(5, Long.toString(modifiedDate.getTime()));
        pstmt.setInt(6, approved?1:0);
        pstmt.executeUpdate();
        pstmt.close();

        //Now, insert the message into the database.
        ((ForumMessageProxy)rootMessage).insertIntoDb(con, this);

        //since we're done inserting the object to the database, it is ready
        //for future insertions.
        isReadyToSave = true;
    }

    /**
     * Saves the ForumThread to the database.
     */
    private synchronized void saveToDb() {
        Connection con = null;
        PreparedStatement pstmt = null;
         try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SAVE_THREAD);
            pstmt.setInt(1, rootMessageID);
            pstmt.setString(2, Long.toString(creationDate.getTime()));
            pstmt.setString(3, Long.toString(modifiedDate.getTime()));
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbForumThread:saveToDb()-" + sqle);
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }
}
