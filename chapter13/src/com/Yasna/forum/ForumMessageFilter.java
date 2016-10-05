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

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * A filter acts as a layer between a forum skin and a real ForumMessage
 * object. It provides extended functionality by dynamically reformatting
 * ForumMessage contents.
 * <p>
 * An unlimited number of ForumMessageFilters can be specified to be applied
 * per Forum. Object-oriented designers will recognize this class as a
 * Decorator for ForumMessages.
 *
 * @see ForumMessage
 */
public abstract class ForumMessageFilter implements ForumMessage, Serializable
{

    /**
     * The underlying message the filter is applied to.
     */
    protected ForumMessage message = null;

    /**
     * Creates a new filter not associated with a message. This is
     * generally only useful for defining a template filter that other
     * fitlers will be cloned from.
     */
    public ForumMessageFilter() {
        //do nothing;
    }

    /**
     * Creates a new filter wrapped around the specified message.
     *
     * @param message the ForumMessage to wrap the new filter around.
     */
    public ForumMessageFilter(ForumMessage message) {
        this.message = message;
    }

    /**
     * Clones a new filter that will have the same properties and that
     * will wrap around the specified message.
     *
     * @param message the ForumMessage to wrap the new filter around.
     */
    public abstract ForumMessageFilter clone(ForumMessage message);

    /**
     * Returns the name of the filter.
     *
     * @return the name of the filter.
     */
    public abstract String getName();

    /**
     * Returns a description of the filter. The description of the filter should
     * include information about what the filter does, as well as any critical
     * usage notes.
     *
     * @return the description of the filter.
     */
    public abstract String getDescription();

    /**
     * Returns a URL that has documentation about the filter. By default, the
     * method returns null, which means that no documentation is available.<p>
     *
     * Documentation should include installation as well as usage guidlines.
     *
     * @return a URL with documentation about the filter, or null if no
     *  documentation is available.
     */
    public String getDocumentationURL() {
        return null;
    }

    /**
     * Returns the author of the filter.
     *
     * @return the author of the filter.
     */
    public abstract String getAuthor();

    /**
     * Returns the major version number of the filter. For example it would
     * return the value of "x" in x.0. Major version numbers should be
     * incremented for new releases of filters that introduct significant new
     * functionality
     *
     * @return the major version number of the filter.
     */
    public abstract int getMajorVersion();

    /**
     * Returns the minor version number of the filter. For example it would
     * return the value of "x" in 1.x. Minor version numbers should be
     * incremented for new releases of filters that fix bugs or add minor
     * functionality.
     *
     * @return the minor version number of the filter
     */
    public abstract int getMinorVersion();

    /**
     * Returns the value of a property of the filter.
     *
     * @param name the name of the property.
     * @return the value of the property.
     */
    public abstract String getFilterProperty(String name);

    /**
     * Returns the description of a property of the filter.
     *
     * @param name the name of the property.
     * @return the description of the property.
     */
    public abstract String getFilterPropertyDescription(String name);

    /**
     * Returns an enumeration of the property names of the filter.
     *
     * @return an enumeration of the property names of the filter.
     */
    public abstract Enumeration filterPropertyNames();

    /**
     * Sets a property of the filter. Each filter has a set number of
     * properties that are determined by the filter author.
     *
     * @param name the name of the property to set.
     * @param value the new value for the property.
     *
     * @throws IllegalArgumentException if the property trying to be set doesn't
     *    exist.
     */
    public abstract void setFilterProperty(String name, String value)
            throws IllegalArgumentException;

    /**
     * Saves the properties of the filter through a persistance layer.
     * Each forum implementation is responsible for providing this layer by
     * overriding this method.
     */
    public void saveFilterProperties() {
        //Empty - each forum implementation must override this method. The
        //forum implementation filter wrapper is responsible for calling this
        //method when filter properties are set.
    };

    //FROM THE FORUMMESSAGE INTERFACE//

    public int getID() {
        return message.getID();
    }

    public Date getCreationDate() {
        return message.getCreationDate();
    }

    public void setCreationDate(Date creationDate) throws UnauthorizedException
    {
        message.setCreationDate(creationDate);
    }

    public Date getModifiedDate() {
        return message.getModifiedDate();
    }

    public void setModifiedDate(Date modifiedDate) throws UnauthorizedException
    {
        message.setModifiedDate(modifiedDate);
    }

    public String getSubject() {
        return message.getSubject();
    }

    public String getUnfilteredSubject() {
        return message.getUnfilteredSubject();
    }

    public void setSubject(String subject) throws UnauthorizedException  {
        message.setSubject(subject);
    }

    public String getBody() {
        return message.getBody();
    }

    public String getUnfilteredBody() {
        return message.getUnfilteredBody();
    }

    public void setBody(String body) throws UnauthorizedException {
        this.message.setBody(body);
    }

    public User getUser() {
        return message.getUser();
    }

    public String getProperty(String name) {
        return message.getProperty(name);
    }

    public String getUnfilteredProperty(String name) {
        return message.getUnfilteredBody();
    }

    public void setProperty(String name, String value) {
        message.setProperty(name, value);
    }

    public Iterator propertyNames() {
        return message.propertyNames();
    }

    public boolean isAnonymous() {
        return message.isAnonymous();
    }

    public ForumThread getForumThread() {
        return message.getForumThread();
    }

    public boolean hasPermission(int type) {
        return message.hasPermission(type);
    }

    //OTHER METHODS//

    public String toString() {
        return message.toString();
    }
}
