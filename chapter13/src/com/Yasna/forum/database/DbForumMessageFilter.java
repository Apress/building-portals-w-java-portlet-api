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

import java.io.Serializable;
import java.util.Enumeration;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageFilter;

/**
 * Provides database persistence of ForumMessageFilter properties. When the
 * database implementation of the forum system is used, all filters are
 * wrapped with an instance of this class.
 */
public class DbForumMessageFilter extends ForumMessageFilter
        implements Serializable
{

    /**
     * Reference to the forum object this filter belongs to. It's neeeded so
     * that we can save the state of the filter properties.
     */
    private transient DbForum forum;

    public DbForumMessageFilter(ForumMessage message, DbForum forum) {
        this.message = message;
        this.forum = forum;
    }

    /**
     * Clones a new filter that will have the same properties and that
     * will wrap around the specified message.
     *
     * @param message the ForumMessage to wrap the new filter around.
     */
    public ForumMessageFilter clone(ForumMessage message) {
        return ((ForumMessageFilter)message).clone(message);
    }

    /**
     * Returns the name of the filter.
     */
    public String getName() {
        return ((ForumMessageFilter)message).getName();
    }

    /**
     * Returns a description of the filter.
     */
    public String getDescription() {
        return ((ForumMessageFilter)message).getDescription();
    }

    /**
     * Returns the author of the filter.
     */
    public String getAuthor() {
        return ((ForumMessageFilter)message).getAuthor();
    }

    /**
     * Returns the major version number of the filter.
     */
    public int getMajorVersion() {
        return ((ForumMessageFilter)message).getMajorVersion();
    }

    /**
     * Returns the minor version number of the filter.
     */
    public int getMinorVersion() {
        return ((ForumMessageFilter)message).getMinorVersion();
    }

    /**
     * Gets a property of the filter by name.
     *
     * @param name the name of the property to get.
     */
    public String getFilterProperty(String name){
        return ((ForumMessageFilter)message).getFilterProperty(name);
    }

    /**
     * Sets a property of the filter.
     *
     * @param name the name of the property to set
     * @param value the new value for the property
     */
    public void setFilterProperty(String name, String value)
            throws IllegalArgumentException
    {
        ((ForumMessageFilter)message).setFilterProperty(name,value);
        //Save properties to db
        saveFilterProperties();
    }

    /**
     * Returns an iterator for the property names for the filter.
     */
    public Enumeration filterPropertyNames() {
        return ((ForumMessageFilter)message).filterPropertyNames();
    }

    /**
     * Saves the state of the filter. Each forum implementation is
     * responsible for implementing this method.
     */
    public void saveFilterProperties() {
        forum.saveFiltersToDb();
    }

    /**
     * Returns a property description.
     *
     * @param name the name of the property to get a description of.
     */
    public String getFilterPropertyDescription(String name) {
        return ((ForumMessageFilter)message).getFilterPropertyDescription(name);
    }

    /**
     * Converts the object to a String by returning the name of the message.
     * This functionality is primarily for Java applications that might be
     * accessing Yazd objects through a GUI.
     */
    public String toString() {
        return message.toString();
    }
}

