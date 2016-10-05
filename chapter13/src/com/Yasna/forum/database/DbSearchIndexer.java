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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.SearchIndexer;
import com.Yasna.forum.PropertyManager;

/**
 * Database implementation of SearchIndexer using the Lucene search package.
 *
 * Search indexes are stored in the "search" subdirectory of directory pointed to
 * by the Yazd property "yazdHome".
 */
public class DbSearchIndexer extends Thread implements SearchIndexer{

    /** DATABASE QUERIES **/
    //private static final String MESSAGES_BEFORE_DATE =
    //  "SELECT messageID FROM yazdMessage WHERE modifiedDate < ?";
    private static final String MESSAGES_BEFORE_DATE =
        "SELECT messageID, userID, yazdMessage.threadID, forumID, " +
        "subject, body, yazdMessage.creationDate " +
        "FROM yazdMessage, yazdThread WHERE yazdMessage.threadID=yazdThread.threadID " +
        "AND yazdMessage.modifiedDate < ?";
    private static final String MESSAGES_BEFORE_DATE_COUNT =
        "SELECT count(messageID) FROM yazdMessage WHERE modifiedDate < ?";
    private static final String MESSAGES_SINCE_DATE =
        "SELECT messageID FROM yazdMessage WHERE modifiedDate > ? " +
        "AND modifiedDate < ?";
    private static final String MESSAGES_SINCE_DATE_COUNT =
        "SELECT count(messageID) FROM yazdMessage WHERE modifiedDate > ? " +
        "AND modifiedDate < ?";
    private static final String LOAD_MESSAGE =
        "SELECT subject, body, userID, yazdMessage.threadID, forumID, " +
        "yazdMessage.creationDate FROM yazdMessage, yazdThread WHERE " +
        "yazdMessage.threadID=yazdThread.threadID AND yazdMessage.messageID=?";

    /**
     * Path to where index is stored.
     */
    private static String indexPath = null;

    /**
     * Time constants (in milleseconds)
     */
    private static final long MINUTE = 1000 * 60;
    private static final long HOUR = MINUTE * 60;

    /**
     * Maintains the amount of time that should elapse until the next index.
     */
    private long updateInterval;

    /**
     * Maintains the time that the last index took place.
     */
    private long lastIndexed;

    /**
     * Indicates whether auto-indexing should be on or off. When on, an update
     * will be run at the "updateInterval".
     */
    private boolean autoIndex = true;

    /**
     * ForumFactory so that we can load message objects based on their ID.
     */
    private DbForumFactory factory;

    /**
     * Lock so that only one indexing function can be executed at once. Not
     * locking could impact the database integrity. Therefore, in a cluster of
     * Yazd servers all pointed at the same db, only one indexer should be
     * running once.
     */
    private Object indexLock = new Object();

    private static Analyzer analyzer = new StopAnalyzer();

    /**
     * Creates a new DbSearchIndexer. It attempts to load properties for
     * the update interval and when the last index occured from the Yazd 
     * properties then starts the indexing thread.
     */
    public DbSearchIndexer(DbForumFactory factory) {
        this.factory = factory;

        //Default to performing updates ever 10 minutes.
        updateInterval = 10 * MINUTE;
        //If the update interval property exists, use that
        String updInterval = PropertyManager.getProperty("DbSearchIndexer.updateInterval");
        try {
            updateInterval = Long.parseLong(updInterval);
        }
        catch (Exception e) { /* ignore */ }

        //Attempt to get the last updated time from the Yazd properties
        String lastInd = PropertyManager.getProperty("DbSearchIndexer.lastIndexed");
        try {
            lastIndexed = Long.parseLong(lastInd);
        }
        catch (Exception e) {
            //Something went wrong. Therefore, set lastIndexed far into the past
            //so that we'll do a full index.
            lastIndexed = 0;
        }
        //Make this a daemon thread.
        this.setDaemon(true);
        //Start the indexing thread.
        start();
    }

    public int getHoursUpdateInterval() {
        return (int)(updateInterval / HOUR);
    }

    public int getMinutesUpdateInterval() {
        return (int)((updateInterval - getHoursUpdateInterval()*HOUR) / MINUTE);
    }

    public void setUpdateInterval(int minutes, int hours) {
        updateInterval = (minutes * MINUTE) + (hours * HOUR);
        //Save it to the properties
        PropertyManager.setProperty("DbSearchIndexer.updateInterval", ""+updateInterval);
    }

    public java.util.Date getLastIndexedDate() {
        return new java.util.Date(lastIndexed);
    }

    public boolean isAutoIndexEnabled() {
        return autoIndex;
    }

    public void setAutoIndexEnabled(boolean value) {
        autoIndex = value;
    }

    public void addToIndex(ForumMessage message) {
        //acquire the index lock so that no other indexing operations
        //are performed.
        synchronized (indexLock) {
            IndexWriter writer = null;
            try {
                writer = getWriter(false);
                addMessageToIndex(writer, message.getID(),
                    message.getUnfilteredSubject(), message.getUnfilteredBody(),
                    message.getUser().getID(), message.getForumThread().getID(),
                    message.getForumThread().getForum().getID(),
                    message.getCreationDate()
                );
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
            finally{
                try { writer.close(); }
                catch (Exception e) { }
            }
        }
    }

    public void removeFromIndex(ForumMessage message) {
        //acquire the index lock so that no other indexing operations
        //are performed.
        synchronized (indexLock) {
            try {
                int [] toDelete = new int [] { message.getID() };
                deleteMessagesFromIndex(toDelete);
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void updateIndex() {
        //acquire the index lock so that no other indexing operations
        //are performed.
        synchronized (indexLock) {
            long now = System.currentTimeMillis();
            updateIndex(lastIndexed, now);
            lastIndexed = now;
            //Save the time as a Yazd property.
            PropertyManager.setProperty("DbSearchIndexer.lastIndexed",
                "" + lastIndexed);
        }
    }

    public void rebuildIndex() {
        //acquire the index lock so that no other indexing operations
        //are performed.
        synchronized (indexLock) {
            long now = System.currentTimeMillis();
            rebuildIndex(now);
            lastIndexed = now;
            //Save the time as a Yazd property.
            PropertyManager.setProperty("DbSearchIndexer.lastIndexed",
                "" + lastIndexed);
        }
    }

    /**
     * Indexing thread logic. It wakes up once a minute to see if any threaded
     * action should take place.
     */
    public void run() {
        while (true){
            //If auto indexing is on
            if (autoIndex) {
                long now = System.currentTimeMillis();
                //If we want to re-index everything.
                if (lastIndexed == 0) {
                    synchronized(indexLock) {
                        rebuildIndex(now);
                        lastIndexed = now;
                        //Save the time as a Yazd property.
                        PropertyManager.setProperty("DbSearchIndexer.lastIndexed",
                            "" + lastIndexed);
                    }
                }
                //We only want to do an update.
                else {
                    long nextIndex = lastIndexed + updateInterval;
                    if (now > nextIndex) {
                        synchronized(indexLock) {
                            updateIndex(lastIndexed, now);
                            lastIndexed = now;
                            //Save the time as a Yazd property.
                            PropertyManager.setProperty("DbSearchIndexer.lastIndexed",
                                "" + lastIndexed);
                        }
                    }
                }
            }
            //sleep for 1 minute and then check again.
            try {
                Thread.sleep(60000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   /**
     * Indexes an indivual message. The connection is assumed to be open when
     * passed in and will remain open after the method is done executing.
     */
    protected final void addMessageToIndex(IndexWriter writer, int messageID,
            String subject, String body, int userID, int threadID, int forumID,
            java.util.Date creationDate) throws IOException
    {
        if (writer == null) {
            return;
        }
        //Ignore messages with a null subject or body.
        if (subject == null || body == null) {
            return;
        }

        Document doc = new Document();
        doc.add(Field.Keyword("messageID", Integer.toString(messageID)));
        doc.add(new Field("userID", Integer.toString(userID), false, true, false));
        doc.add(new Field("threadID", Integer.toString(threadID), false, true, false));
        doc.add(new Field("forumID", Integer.toString(forumID), false, true, false));
        doc.add(Field.UnStored("subject", subject));
        doc.add(Field.UnStored("body", body));
        doc.add(new Field("creationDate", DateField.dateToString(creationDate), false, true, false));

        writer.addDocument(doc);
    }

    /**
     * Deletes a message from the index.
     */
    protected final void deleteMessagesFromIndex(int [] messages) throws IOException {
        if (messages == null) {
            return;
        }
        IndexReader reader = getReader();
        if (reader == null) {
            //Reader will be null if the search index doesn't exist.
            return;
        }
        Term messageIDTerm;
        for (int i=0; i<messages.length; i++) {
            messageIDTerm = new Term("messageID", Integer.toString(messages[i]));
            try {
                reader.delete(messageIDTerm);
            }
            catch (Exception e) { }
        }
        try {
            reader.close();
        }
        catch (Exception e) { }
    }

    /**
     * Rebuilds the search index from scratch. It deletes the entire index
     * and word tables and then indexes every message up to the end time.
     */
    protected final void rebuildIndex(long end) {
        System.err.println("Rebuilding index...");

        IndexWriter writer = null;
        Connection con = null;
        try {
            writer = getWriter(true);
            con = DbConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(MESSAGES_BEFORE_DATE);
            pstmt.setString(1, Long.toString(end));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int messageID = rs.getInt(1);
                int userID = rs.getInt(2);
                int threadID = rs.getInt(3);
                int forumID = rs.getInt(4);
                String subject = rs.getString(5);
                String body = rs.getString(6);
                java.util.Date creationDate =
                    new java.util.Date(Long.parseLong(rs.getString(7).trim()));
                //ForumMessage message = new DbForumMessage(messageID, factory);// factory.getMessage(messageID);
                addMessageToIndex(writer, messageID, subject, body, userID, threadID, forumID, creationDate);
            }
            pstmt.close();
        }
        catch( Exception sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
            try {
                //A rebuild of the index warrants calling optimize.
                writer.optimize();
            }
            catch (Exception e) { }
            try {
                writer.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.err.println("Done rebuilding index.");
    }

    /**
     * Updates the index. It first deletes any messages in the index between
     * the start and end times, and then adds all messages to the index that
     * are between the start and end times.
     */
    protected final void updateIndex(long start, long end) {
        Connection con = null;
        PreparedStatement pstmt = null;
        IndexWriter writer = null;
        int [] messages = null;

        try {
            con = DbConnectionManager.getConnection();
            //For a clean update, we need to make sure that we first delete
            //any index entries that were made since we last updated. This
            //might happen if a process was calling indexMessage() between runs
            //of this method. For this reason, the two types of indexing (manual
            //and automatic) should not be intermixed. However, we still perform
            //this deletion to be safe.
            pstmt = con.prepareStatement(MESSAGES_SINCE_DATE_COUNT);
            pstmt.setString(1, Long.toString(start));
            pstmt.setString(2, Long.toString(end));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int messageCount = rs.getInt(1);
            messages = new int[messageCount];
            pstmt.close();
            pstmt = con.prepareStatement(MESSAGES_SINCE_DATE);
            pstmt.setString(1, Long.toString(start));
            pstmt.setString(2, Long.toString(end));
            rs = pstmt.executeQuery();
            for (int i=0; i<messages.length; i++) {
                rs.next();
                messages[i] = rs.getInt("messageID");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        try {
            deleteMessagesFromIndex(messages);

            //Finally, index all new messages;
            writer = getWriter(false);
            for (int i=0; i<messages.length; i++) {
                ForumMessage message = factory.getMessage(messages[i]);
                addMessageToIndex(writer, message.getID(),
                    message.getUnfilteredSubject(), message.getUnfilteredBody(),
                    message.getUser().getID(), message.getForumThread().getID(),
                    message.getForumThread().getForum().getID(),
                    message.getCreationDate()
                );
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        finally {
            try {  writer.close();  }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Returns a Lucene IndexWriter.
     */
    private static IndexWriter getWriter(boolean create) throws IOException {
        if (indexPath == null) {
            //Get path of where search index should be. It should be
            //the search subdirectory of [yazdHome].
            String yazdHome = PropertyManager.getProperty("yazdHome");
            if (yazdHome == null) {
                System.err.println("ERROR: the yazdHome property is not set.");
                throw new IOException("Unable to open index for searching " +
                        "because yazdHome was not set.");
            }
            indexPath =  yazdHome + File.separator + "search";
        }

        IndexWriter writer = null;

        //If create is true, we always want to create a new index writer.
        if (create) {
            try {
                writer = new IndexWriter(indexPath, analyzer, true);
            }
            catch (Exception e) {
                System.err.println("ERROR: Failed to create a new index writer.");
                e.printStackTrace();
            }
        }
        //Otherwise, use an existing index if it exists.
        else {
            if (indexExists(indexPath)) {
                try {
                    writer = new IndexWriter(indexPath, analyzer, false);
                }
                catch (Exception e) {
                    System.err.println("ERROR: Failed to open an index writer.");
                    e.printStackTrace();
                }
            }
            else {
                try {
                    writer = new IndexWriter(indexPath, analyzer, true);
                }
                catch (Exception e) {
                    System.err.println("ERROR: Failed to create a new index writer.");
                    e.printStackTrace();
                }
            }
        }

        return writer;
    }

    /**
     * Returns a Lucene IndexReader.
     */
    private static IndexReader getReader() throws IOException {
        if (indexPath == null) {
            //Get path of where search index should be. It should be
            //the search subdirectory of [yazdHome].
            String yazdHome = PropertyManager.getProperty("yazdHome");
            if (yazdHome == null) {
                System.err.println("ERROR: the yazdHome property is not set.");
                throw new IOException("Unable to open index for searching " +
                        "because yazdHome was not set.");
            }
            indexPath = yazdHome + File.separator + "search";
        }

        if (indexExists(indexPath)) {
            IndexReader reader = IndexReader.open(indexPath);
            return reader;
        }
        else {
            return null;
        }
    }

    /**
     * Returns true if the search index exists at the specified path.
     *
     * @param indexPath the path to check for the search index at.
     */
    private static boolean indexExists(String indexPath) {
        //Lucene always creates a file called "segments" -- if it exists, we
        //assume that the search index exists.
        File segments = new File(indexPath + File.separator + "segments");
        return segments.exists();
    }
}
