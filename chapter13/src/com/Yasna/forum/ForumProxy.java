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
import java.util.Enumeration;
import java.util.Iterator;

/**
 * A protection proxy for Forums. A proxy has a set of permissions that are
 * specified at creation time of the proxy. Subsequently, those permissions
 * are use to restrict access to protected Forum methods. If a user does
 * not have the right to execute a particular method, and UnauthorizedException
 * is thrown.
 *
 * @see Forum
 * @see ForumPermissions
 * @see UnauthorizedException
 */
public class ForumProxy implements Forum {

    private Forum forum;
    private Authorization authorization;
    private ForumPermissions permissions;

    /**
     * Creates a new ForumProxy object.
     *
     * @param forum the forum to protect by proxy
     * @param authorization the user's authorization token
     * @param permissions the permissions to use with this proxy.
     */
    public ForumProxy(Forum forum, Authorization authorization,
            ForumPermissions permissions)
    {
        this.forum = forum;
        this.authorization = authorization;
        this.permissions = permissions;
    }

    //** Methods from interface below**//

    public int getID() {
        return forum.getID();
    }

    public String getName() {
        return forum.getName();
    }

    public void setName(String name) throws UnauthorizedException,
            ForumAlreadyExistsException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.setName(name);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public String getDescription() {
        return forum.getDescription();
    }

    public void setDescription(String description)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.setDescription(description);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public Date getCreationDate() {
        return forum.getCreationDate();
    }

    public void setCreationDate(Date creationDate)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.setCreationDate(creationDate);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public Date getModifiedDate() {
        return forum.getModifiedDate();
    }

    public void setModifiedDate(Date modifiedDate)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.setModifiedDate(modifiedDate);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public String getProperty(String name) {
        return forum.getProperty(name);
    }

    public void setProperty(String name, String value)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.setProperty(name, value);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public Enumeration propertyNames() {
        return forum.propertyNames();
    }

    public ForumThread createThread(ForumMessage rootMessage)
            throws UnauthorizedException
    {
        if (permissions.get(ForumPermissions.CREATE_THREAD))
        {
            ForumThread thread = forum.createThread(rootMessage);
            return new ForumThreadProxy(thread, authorization, permissions);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public ForumMessage createMessage(User user)
            throws UnauthorizedException
    {
        if (permissions.get(ForumPermissions.CREATE_MESSAGE) ||
                permissions.get(ForumPermissions.CREATE_THREAD) )
        {
            //The user must be anonymous or the actual user in order to post as
            //that user. Otherwise, throw an exception.
            if (user.hasPermission(ForumPermissions.USER_ADMIN) ||
                user.isAnonymous() )
            {
                ForumMessage message = forum.createMessage(user);
                return new ForumMessageProxy(message, authorization, permissions);
            }
            else {
                throw new UnauthorizedException();
            }

        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void deleteThread(ForumThread thread) throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin() || permissions.get(ForumPermissions.MODERATOR)) {
            forum.deleteThread(thread);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void moveThread(ForumThread thread, Forum newForum) throws
            UnauthorizedException, IllegalArgumentException
    {
        //If the user is an amdin of both forums
        if (permissions.isSystemOrForumAdmin() && (
                newForum.hasPermission(ForumPermissions.SYSTEM_ADMIN) ||
                newForum.hasPermission(ForumPermissions.FORUM_ADMIN)))
        {
            forum.moveThread(thread, newForum);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void addThread(ForumThread thread) throws UnauthorizedException
    {
        if (permissions.get(ForumPermissions.CREATE_THREAD)) {
            forum.addThread(thread);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public ForumThread getThread(int threadID) throws ForumThreadNotFoundException
    {
        ForumThread thread = forum.getThread(threadID);
        //Apply protection proxy and return.
        return new ForumThreadProxy(thread, authorization, permissions);
    }

    public Iterator threads() {
        Iterator iterator = forum.threads();
        return new ThreadIteratorProxy(iterator, authorization, permissions);
    }

    public Iterator threads(int startIndex, int numResults) {
        Iterator iterator = forum.threads(startIndex, numResults);
        return new ThreadIteratorProxy(iterator, authorization, permissions);
    }

    public int getThreadCount() {
        return forum.getThreadCount();
    }

    public int getMessageCount() {
        return forum.getMessageCount();
    }

    public Query createQuery() {
        return new QueryProxy(forum.createQuery(), authorization, permissions);
    }

    public void addUserPermission(User user, int permissionType)
            throws UnauthorizedException
    {
        //Don't let someone become a System Admin through this method.
        //The ForumPermissions class probably needs to be changed.
        if (permissionType == ForumPermissions.SYSTEM_ADMIN) {
            throw new UnauthorizedException();
        }
        if (permissions.isSystemOrForumAdmin()) {
            forum.addUserPermission(user, permissionType);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void removeUserPermission(User user, int permissionType)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.removeUserPermission(user, permissionType);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public int [] usersWithPermission(int permissionType)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            return forum.usersWithPermission(permissionType);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void addGroupPermission(Group group, int permissionType)
            throws UnauthorizedException
    {
        //Don't let someone become a System Admin through this method.
        //The ForumPermissions class probably needs to be changed.
        if (permissionType == ForumPermissions.SYSTEM_ADMIN) {
            throw new UnauthorizedException();
        }
        if (permissions.isSystemOrForumAdmin()) {
            forum.addGroupPermission(group, permissionType);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void removeGroupPermission(Group group, int permissionType)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.removeGroupPermission(group, permissionType);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public int[] groupsWithPermission(int permissionType)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            return forum.groupsWithPermission(permissionType);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public ForumMessageFilter[] getForumMessageFilters()
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            return forum.getForumMessageFilters();
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void addForumMessageFilter(ForumMessageFilter filter)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.addForumMessageFilter(filter);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void addForumMessageFilter(ForumMessageFilter filter, int index)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.addForumMessageFilter(filter, index);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public void removeForumMessageFilter(int index)
            throws UnauthorizedException
    {
        if (permissions.isSystemOrForumAdmin()) {
            forum.removeForumMessageFilter(index);
        }
        else {
            throw new UnauthorizedException();
        }
    }

    public ForumMessage applyFilters(ForumMessage message) {
        return forum.applyFilters(message);
    }

    public ForumPermissions getPermissions(Authorization authorization) {
        return forum.getPermissions(authorization);
    }

    public boolean hasPermission(int type) {
        return permissions.get(type);
    }

    public String toString() {
        return forum.toString();
    }
}

