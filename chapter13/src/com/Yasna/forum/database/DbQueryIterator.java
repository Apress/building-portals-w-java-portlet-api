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

import java.util.Iterator;

import com.Yasna.forum.ForumMessage;

/**
 * Iterator for database query results.
 */
public class DbQueryIterator implements Iterator {

    //maintain an array of message ids to iterator through.
    private int [] messages;
    //points to the current message id that the user has iterated to.
    private int currentIndex = -1;

    private int maxIndex;

    private DbForumFactory factory;

    /**
     * Creates a new query iterator using an array of message id's. The
     * ForumFactory is used to lookup the actual message objects.
     *
     * @param messages the array of message id's to iterate through.
     * @param factory a DbForumFactory to obtain actual message objects from.
     */
    public DbQueryIterator(int [] messages, DbForumFactory factory) {
        this.messages = messages;
        this.factory = factory;
        maxIndex = messages.length;
    }

    /**
     * Creates a new query iterator using an array of message id's. The
     * ForumFactory is used to lookup the actual message objects. A start index
     * and number of results limit the scope of the Iterator to a subset of the
     * message array. However, if the start index or number of results does
     * not fall into the bounds of the message array, the Iterator may return
     * fewer results than the number indicated by the numResults paramater.
     *
     * @param messages the array of message id's to iterate through.
     * @param factory a DbForumFactory to obtain actual message objects from.
     * @param startIndex a starting index in the messages array for the Iterator.
     * @param numResults the max number of results the Iterator should provide.
     */
    public DbQueryIterator(int [] messages, DbForumFactory factory,
            int startIndex, int numResults)
    {
        this.messages = messages;
        this.factory = factory;
        currentIndex = startIndex-1;
        maxIndex = startIndex + numResults;
    }

    /**
     * Returns true if there are more messages left to iteratate through.
     */
    public boolean hasNext() {
        return (currentIndex+1 < messages.length && currentIndex+1 < maxIndex);
    }

    /**
     * Returns the next message.
     */
    public Object next() throws java.util.NoSuchElementException {
        ForumMessage message = null;
        currentIndex++;
        if (currentIndex >= messages.length) {
            throw new java.util.NoSuchElementException();
        }
        try {
            int messageID = messages[currentIndex];
            message = factory.getMessage(messageID);
            //Now, get the message from the it's thread so that filters are
            //applied to the message. This may seem a bit convuluted, but is
            //necessary.
            message = message.getForumThread().getMessage(messageID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * For security reasons, the remove operation is not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
