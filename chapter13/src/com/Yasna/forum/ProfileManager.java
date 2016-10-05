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

import java.util.Iterator;

/**
 * Manages Users and Groups.
 */
public interface ProfileManager {

    /**
     * Factory method for creating a new User. A password, email address, and
     * unique username are all required fields to create a new User.
     *
     * @param username the new and unique username for the account.
     * @param password the password for the account as plain text.
     * @param email the email address for the account.
     * @return a new User.
     * @throws UserAlreadyExistsException if the username already exists in
     *      the system.
     */
    public User createUser(String username, String password, String email)
            throws UserAlreadyExistsException;

    /**
     * Factory method for creating a new Group. A unique name is the only
     * required field.
     *
     * @param name the new and unique name for the group.
     * @return a new Group.
     * @throws GroupAlreadyExistsException if the group name already exists in
     *      the system.
     */
    public Group createGroup(String name) throws UnauthorizedException,
            GroupAlreadyExistsException;

    /**
     * Returns a User specified by their id.
     *
     * @param userid the id of the User to lookup.
     * @return the User specified by the given id.
     * @throws UserNotFoundException if the user does not exist.
     */
    public User getUser(int userID) throws UserNotFoundException;

    /**
     * Returns a User specified by username.
     *
     * throws UserNotFoundException if the user does not exist.
     */
    public User getUser(String username) throws UserNotFoundException;

    /**
     * Returns the special "anonymous user" object.
     */
    public User getAnonymousUser();

    /**
     * Returns the "special user" object. The special user represents any
     * valid user in the system. Getting a handle on this object is only
     * really useful for setting permissions on forums. For example, if you
     * want to allow any registered user to post messgages in a forum, add
     * a user permission for posting messages with the special user as the
     * User parameter.
     */
    public User getSpecialUser();

    /**
     * Gets a Group by ID.
     *
     * throws GroupNotFoundException if the group does not exist.
     */
    public Group getGroup(int groupID) throws GroupNotFoundException;

    /**
     * Gets a Group by name.
     *
     * throws GroupNotFoundException if the group does not exist.
     */
    public Group getGroup(String name) throws GroupNotFoundException;

    /**
     * Deletes a User.
     *
     * @param user the user to delete.
     * @throws UnauthorizedException
     */
    public void deleteUser(User user) throws UnauthorizedException;

    /**
     * Deletes a Group.
     *
     * @param group the group to delete.
     * @throws UnauthorizedException
     */
    public void deleteGroup(Group group) throws UnauthorizedException;

    /**
     * Returns the numer of users in the system.
     */
    public int getUserCount();

    /**
     * Returns the number of groups in the system.
     */
    public int getGroupCount();

    /**
     * Retruns an iterator for all users.
     */
    public Iterator users();

    /**
     * Returns an iterator for all users starting at startIndex with the
     * given number of results. This is useful to support pagination in a GUI
     * where you may only want to display a certain number of results per page.
     * It is possible that the number of results returned will be less than
     * that specified by numResults if numResults is greater than the number
     * of records left in the system to display.
     *
     * @param startIndex the beginning index to start the results at.
     * @param numResults the total number of results to return.
     * @return an Iterator for all users in the specified range.
     */
    public Iterator users(int startIndex, int numResults);

    /**
     * Returns an iterator for all groups in the system.
     *
     * @return an Iterator for all groups.
     */
    public Iterator groups();

    /**
     * Returns an iterator for all groups starting at startIndex with the
     * given number of results. This is useful to support pagination in a GUI
     * where you may only want to display a certain number of results per page.
     * It is possible that the number of results returned will be less than
     * that specified by numResults if numResults is greater than the number
     * of records left in the system to display.
     *
     * @param startIndex the beginning index to start the results at.
     * @param numResults the total number of results to return.
     * @return an Iterator for all groups in the specified range.
     */
    public Iterator groups(int startIndex, int numResults);

    /**
     * Returns the number of messages a user has posted in a particular forum.
     *
     * @param user the user you want results for.
     * @param forum the forum you want to check results in.
     * @return the number of messages the user posted in the forum.
     */
    public int userMessageCount(User user, Forum forum);

    /**
     * Returns an iterator for all the messages that a user posted in a
     * particular forum.
     */
    public Iterator userMessages(User user, Forum forum);
}
