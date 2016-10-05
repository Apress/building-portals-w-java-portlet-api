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

package com.Yasna.forum;

import java.util.Date;

/**
 * Defines properties of the Yazd indexer for searching. Indexing can either
 * be done real-time by calling indexMessage() every time a new message is
 * created, or by running the indexer in timed update mode. The latter mode
 * is probably preferable since it allows for faster message posting.
 * <p>
 * The automated updating mode can be adjusted by setting how often batch
 * indexing is done. You can adjust this interval to suit the needs of your
 * forums. Frequent updates mean that messages will be searchable more quickly.
 * Less frequent updates use fewer system resources.
 *
 * @see ForumFactory#getSearchIndexer()
 */
public interface SearchIndexer {

    /**
     * Returns the number of hours that the indexer waits between each update.
     *
     * @return the number of hours between automatic index updates.
     */
    public int getHoursUpdateInterval();

    /**
     * Returns the number of minutes that the indexer waits between each update.
     *
     * @return the number of minutes between automatic index updates.
     */
    public int getMinutesUpdateInterval();

    /**
     * Sets the amount of time that indexer should wait between updating the
     * index.
     *
     * @param minutes the number of minutes between automatic index updates.
     * @param hours the number of hours between automatic index updates.
     */
    public void setUpdateInterval(int minutes, int hours);

    /**
     * Returns true if auto indexing is turned on. When auto indexing is on, it
     * will update the search index at the interval specified by the
     * setUpdateInterval method.
     *
     * @return true if auto indexing is turned on.
     */
    public boolean isAutoIndexEnabled();

    /**
     * Enables or disables auto indexing. When auto indexing is on, it
     * will update the search index at the interval specified by the
     * setUpdateInterval method.
     *
     * @param value true to turn auto indexing on, false to turn it off.
     */
    public void setAutoIndexEnabled(boolean value);

    /**
     * Returns the date that the last update to the index was made.
     */
    public Date getLastIndexedDate();

    /**
     * Adds an individual message to the index. This method is useful for doing
     * real-time indexing. However, for maximum posting speed this method can
     * be ignored. In that case, the automatic indexer will pick up all new
     * messages at the next index interval.
     *
     * @param message the message to add to the index.
     */
    public void addToIndex(ForumMessage message);

    /**
     * Removes an individual message from the index.
     *
     * @param message the message to remove from the index.
     */
    public void removeFromIndex(ForumMessage message);

    /**
     * Manually update the index to include all new messages since the last
     * update.
     */
    public void updateIndex();

    /**
     * Manually rebuild the entire index. This operation can be slow if the
     * index is large.
     */
    public void rebuildIndex();
}
