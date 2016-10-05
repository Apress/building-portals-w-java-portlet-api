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
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DateFilter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.Yasna.forum.Forum;
import com.Yasna.forum.ForumThread;
import com.Yasna.forum.User;
import com.portalbook.forums.ForumException;

/**
 * Database implementation of the Query interface using Lucene.
 */
public class DbQuery implements com.Yasna.forum.Query {

    /**
     * The query String to use for searching. Set it the empty String by
     * default so that if the user fails to set a query String, there won't
     * be errors.
     */
    private String queryString = "";
    private java.util.Date beforeDate = null;
    private java.util.Date afterDate = null;
    private User user = null;
    private ForumThread thread = null;

    /**
     * The results of the query as an array of message ID's.
     */
    private int [] results = null;

    private Forum forum;
    private DbForumFactory factory;

    /**
     * The maximum number of results to return with a search.
     */
    private static final int MAX_RESULTS_SIZE = 500;

    private static String indexPath = null;
    private static IndexReader reader;
    private static Searcher searcher;
    private static Directory searchDirectory = null;
    private static long indexLastModified;
    private static Analyzer analyzer = new StandardAnalyzer();

    public DbQuery(Forum forum, DbForumFactory factory){
        this.forum = forum;
        this.factory = factory;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
        //reset results
        results = null;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setBeforeDate(java.util.Date beforeDate) {
        this.beforeDate = beforeDate;
        //reset results
        results = null;
    }

    public java.util.Date getBeforeDate() {
        return beforeDate;
    }

    public void setAfterDate(java.util.Date afterDate){
        this.afterDate = afterDate;
        //reset results
        results = null;
    }

    public java.util.Date getAfterDate(){
        return afterDate;
    }

    public User getFilteredUser() {
        return user;
    }

    public void filterOnUser(User user) {
        this.user = user;
        results = null;
    }

    public ForumThread getFilteredThread() {
        return thread;
    }

    public void filterOnThread(ForumThread thread) {
        this.thread = thread;
        results = null;
    }

    public int resultCount() {
        if (results == null) {
            executeQuery();
        }
        return results.length;
    }

    public Iterator results() {
        if (results == null) {
            executeQuery();
        }
        return new DbQueryIterator(results, factory);
    }

    public Iterator results(int startIndex, int numResults){
        if (results == null) {
            executeQuery();
        }
        return new DbQueryIterator(results, factory, startIndex, numResults);
    }

    /**
     * Execute the query and store the results in the results array.
     */
    private void executeQuery() {
        try {
            Searcher searcher = getSearcher();
            if (searcher == null) {
                //Searcher can be null if the index doesn't exist.
                results = new int[0];
                return;
            }

            org.apache.lucene.search.Query bodyQuery =
                QueryParser.parse(queryString, "body", analyzer);

            org.apache.lucene.search.Query subjectQuery =
                QueryParser.parse(queryString, "subject", analyzer);

            String forumID = Integer.toString(forum.getID());

            BooleanQuery comboQuery = new BooleanQuery();
            comboQuery.add(subjectQuery,false,false);
            comboQuery.add(bodyQuery,false,false);

            MultiFilter multiFilter = new MultiFilter(3);

            //Forum filter
            multiFilter.add(new FieldFilter("forumID", forumID));

            //Date filter
            if (beforeDate != null || afterDate != null) {
                if (beforeDate != null && afterDate != null) {
                    multiFilter.add(new DateFilter("creationDate", beforeDate, afterDate));
                }
                else if (beforeDate == null) {
                    multiFilter.add(DateFilter.After("creationDate", afterDate));
                }
                else {
                    multiFilter.add(DateFilter.Before("creationDate", beforeDate));
                }
            }

            //User filter
            if (user != null) {
                String userID = Integer.toString(user.getID());
                multiFilter.add(new FieldFilter("userID", userID));
            }

            //Thread filter
            if (thread != null) {
                String threadID = Integer.toString(thread.getID());
                multiFilter.add(new FieldFilter("threadID", threadID));
            }

            Hits hits = searcher.search(comboQuery, multiFilter);
            //Don't return more search results than the maximum number allowed.
            int numResults = hits.length() < MAX_RESULTS_SIZE ?
                    hits.length() : MAX_RESULTS_SIZE;
            int [] messages = new int[numResults];
            for (int i=0; i<numResults; i++) {
                messages[i] = Integer.parseInt( ((Document)hits.doc(i)).get("messageID") );
            }
            results = messages;
        }
        catch (Exception e) {
            e.printStackTrace();
            results = new int[0];
        }
    }

    /**
     * Returns a Lucene Searcher that can be used to execute queries. Lucene
     * can handle index reading even while updates occur. However, in order
     * for index changes to be reflected in search results, the reader must
     * be re-opened whenever the modifiedDate changes.<p>
     *
     * The location of the index is the "search" subdirectory in [yazdHome]. If
     * yazdHome is not set correctly, this method will fail.
     *
     * @return a Searcher that can be used to execute queries.
     */
    private static Searcher getSearcher() throws IOException {
        if (indexPath == null) {
            //Get path of where search index should be stored//
            try {
         	      Context ctx = new InitialContext();
         	      if( ctx == null ) throw new ForumException("There was a problem configuring the software","Null Context retrieved",new NullPointerException());
         	      
         	      indexPath = (String)ctx.lookup("java:comp/env/indexes");
         	      if( indexPath == null ) throw new ForumException("There was a problem with the software configuration","Null path to indexes",new NullPointerException());
         	      
         	      indexPath = indexPath + File.separator + "search";
               } catch( NamingException e ) {
                   throw new ForumException("There was a problem connecting to the database","Naming lookup failed",e);
               }
        }

        if (searcher == null) {
            //Acquire a lock -- analyzer is a convenient object to do this on.
            synchronized(analyzer) {
                if (searcher == null) {
                    if (indexExists(indexPath)) {
                        searchDirectory = FSDirectory.getDirectory(indexPath, false);
                        reader = IndexReader.open(searchDirectory);
                        indexLastModified = IndexReader.getCurrentVersion(searchDirectory);
                        searcher = new IndexSearcher(reader);
                    }
                    //Otherwise, the index doesn't exist, so return null.
                    else {
                        return null;
                    }
                }
            }
        }
        if (IndexReader.getCurrentVersion(searchDirectory) > indexLastModified) {
            synchronized (analyzer) {
                if (IndexReader.getCurrentVersion(searchDirectory) > indexLastModified) {
                    if (indexExists(indexPath)) {
                        indexLastModified = IndexReader.getCurrentVersion(searchDirectory);
                        //We need to close the indexReader because it has changed.
                        //Re-opening it will make changes visible.
                        reader.close();

                        searchDirectory = FSDirectory.getDirectory(indexPath, false);
                        reader = IndexReader.open(searchDirectory);
                        searcher = new IndexSearcher(reader);
                    }
                    //Otherwise, the index doesn't exist, so return null.
                    else {
                        return null;
                    }
                }
            }
        }
        return searcher;
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
