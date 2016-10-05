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
import java.util.Iterator;

/**
 * A ForumMessage encapsulates message data. Each message belongs to a thread,
 * and relates to other messages in a thread in a tree relationship. This system
 * allows messages to represent threaded conversations. For example:
 *
 * <pre>
 *   [thread]
 *        |- [message]
 *        |- [message]
 *                 |- [message]
 *                 |- [message]
 *                          |- [message]
 *        |- [message]
 * </pre><p>
 *
 * Each message has a subject and body. Messages are authored by a user
 * in the system or can be anonymous. An ID is given to each message so that
 * it can be tracked uniquely. Because is possible that one might want to store
 * considerable more information with each message besides a subject and body,
 * each message can have an arbitrary number of properties. For example, a
 * property "IP" could be stored with each message that records the IP address
 * of the person posting the message for security reasons.<p>
 *
 * The creation date, and the date the message was last modified are maintained
 * for each message.<p>
 *
 * For added functionality, any number of filters can be applied to a message.
 * Filters dynamically format the subject and body of a message. Methods are
 * also provided to bypass filters.
 *
 * @see ForumMessageFilter
 */
public interface ForumMessage {

    /**
     * Returns the id of the message.
     *
     * @return the unique id of the message.
     */
    public int getID();

    /**
     * Returns the date the message was created.
     *
     * @return the date the message was created.
     */
    public Date getCreationDate();

    /**
     * Sets the creation date of the message. In most cases, the creation date
     * will default to when the message was entered into the system. However,
     * the creation date needs to be set manually when importing messages.
     * In other words, skin authors should ignore this method since it only
     * intended for system maintenance.
     *
     * @param creationDate the date the message was created.
     *
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void setCreationDate(Date creationDate) throws UnauthorizedException;

    /**
     * Returns the date the message was last modified. When a message is first
     * created, the date returned by this method is identical to the creation
     * date. The modified date is updated every time a message property is
     * updated, such as the message body.
     *
     * @return the date the message was last modified.
     */
    public Date getModifiedDate();

    /**
     * Sets the date the message was last modified. In most cases, last modifed
     * will default to when the message data was last changed. However,
     * the last modified date needs to be set manually when importing messages.
     * In other words, skin authors should ignore this method since it only
     * intended for system maintenance.
     *
     * @param modifiedDate the date the message was modified.
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void setModifiedDate(Date modifiedDate) throws UnauthorizedException;

    /**
     * Returns the message subject. If message filters are active, the
     * subject returned will be a filtered one. Because filters often provide
     * security functionality, this method is the preferred way to get the
     * subject of a message.
     *
     * @return the subject of the message.
     */
    public String getSubject();

    /**
     * Returns the message subject, bypassing any active filters. Because
     * filters often provide security, this method should be used with caution.
     * In particular, you should avoid showing unfiltered data in an environment
     * where embedded HTML might be interpreted.<p>
     *
     * Unfiltered content is necessary for a few reasons. One is when saving
     * Yazd content to another persistence mechanism such as an XML format.
     * Another is when you need to skip filter formatting, such as when a user
     * is responding to another user's message.
     *
     * @return the subject of the message.
     */
    public String getUnfilteredSubject();

    /**
     * Sets the subject of the message.
     *
     * @param subject the subject of the message.
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void setSubject(String subject) throws UnauthorizedException;

    /**
     * Returns the message body. If message filters are active, the body
     * returned will be a filtered one. Because filters often provide security
     * functionality, this method is the preferred way to get the body of a
     * message.
     *
     * @return the body of the message.
     */
    public String getBody();

    /**
     * Returns the message body, bypassing any active filters. Because filters
     * often provide security, this method should be used with caution. In
     * particular, you should avoid showing unfiltered data in an environment
     * where embedded HTML might be interpreted.<p>
     *
     * Unfiltered content is necessary for a few reasons. One is when saving
     * Yazd content to another persistence mechanism such as an XML format.
     * Another is when you need to skip filter formatting, such as when a user
     * is responding to another user's message.
     *
     * @return the body of the message.
     */
    public String getUnfilteredBody();

    /**
     * Sets the body of the message.
     *
     * @param body the body of the message.
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public void setBody(String body) throws UnauthorizedException;

    /**
     * Returns the User that authored the message. If the message was created
     * anonymously, the Anonymous User object will be returned.
     *
     * @return the author of the message.
     */
    public User getUser();

    /**
     * Returns an extended property of the message. Each message can have an
     * arbitrary number of extended properties. This lets particular skins
     * or filters provide enhanced functionality that is not part of the base
     * interface.<p>
     *
     * For security reasons, all property values are run through an HTML filter
     * before being returned.
     *
     * @param name the name of the property to get.
     * @return the value of the property.
     */
    public String getProperty(String name);

    /**
     * Returns an extended property of the message, bypassing the HTML filter.
     * Each message can have an arbitrary number of extended properties. This
     * lets particular skins or filters provide enhanced functionality that is
     * not part of the base interface.<p>
     *
     * Because properties are not filtered before being returned, this method
     * should be used with caution. In particular, you should avoid showing
     * unfiltered data in an environment where embedded HTML might be
     * interpreted.
     *
     * @param name the name of the property to get.
     * @return the value of the property.
     */
    public String getUnfilteredProperty(String name);

    /**
     * Sets an extended property of the message. Each message can have an
     * arbitrary number of extended properties. This lets particular skins
     * or filters provide enhanced functionality that is not part of the base
     * interface.
     *
     * @param name the name of the property to set.
     * @param value the new value for the property.
     */
    public void setProperty(String name, String value);

    /**
     * Returns an Iterator for all the names of the message properties.
     *
     * @return an Iterator for the names of all message properties.
     */
    public Iterator propertyNames();

    /**
     * Returns whether the message was posted anonymously. This is a convenience
     * method and is equivalent to getUser().isAnonymous();
     *
     * @return true if the message was posted anonymously.
     */
    public boolean isAnonymous();

    /**
     * Returns the thread the message belongs to.
     *
     * @return the thread the message belongs to.
     */
    public ForumThread getForumThread();

    /**
     * Returns true if the handle on the object has the permission specified.
     * A list of possible permissions can be found in the ForumPermissions
     * class. Certain methods of this class are restricted to certain
     * permissions as specified in the method comments.
     *
     * @param type a permission type.
     * @return true if the specified permission is valid.
     * @see ForumPermissions
     */
    public boolean hasPermission(int type);
}

