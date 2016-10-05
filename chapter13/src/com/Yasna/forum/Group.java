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
 * Organizes Users into a group for easier permissions management at the
 * Forum level. In this way, Groups essentially serve the same purpose that
 * they do in Unix or Windows.<p>
 *
 * For example, CREATE_THREAD permissions can be set per forum. A forum
 * administrator may wish to create a "Thread Posters" group that has
 * CREATE_THREAD permissions in the forum. Then, users can be added to that
 * group and will automatically receive CREATE_THREAD permissions in that forum.
 * <p>
 *
 * Security for Group objects is provide by GroupProxy protection proxy objects.
 *
 * @see User
 */
public interface Group {

    /**
     * Returns the id of the group.
     *
     * @return the id of the group.
     */
    public int getID();

    /**
     * Returns the name of the group. For example, 'XYZ Admins'.
     *
     * @return the name of the group.
     */
    public String getName();

    /**
     * Sets the name of the group. For example, 'XYZ Admins'.<p>
     *
     * This method is restricted to those with group administration permission.
     *
     * @param name the name for the group.
     * @throws UnauthorizedException if does not have group admin permissions.
     */
    public void setName(String name) throws UnauthorizedException;

    /**
     * Returns the description of the group. The description often summarizes
     * a group's function, such as 'Administrators of the XYZ forum'.
     *
     * @return the description of the group.
     */
    public String getDescription();

    /**
     * Sets the description of the group.
     *
     * The description often summarizes a group's function, such as
     * 'Administrators of the XYZ forum'.<p>
     *
     * This method is restricted to those with group administration permission.
     *
     * @param name the description of the group.
     * @throws UnauthorizedException if does not have group admin permissions.
     */
    public void setDescription(String description) throws UnauthorizedException;

    /**
     * Grants administrator privileges of the group to a user.<p>
     *
     * This method is restricted to those with group administration permission.
     *
     * @param user the User to grant adminstrative privileges to.
     * @throws UnauthorizedException if does not have group admin permissions.
     */
    public void addAdministrator(User user) throws UnauthorizedException;

    /**
     * Revokes administrator privileges of the group to a user.<p>
     *
     * This method is restricted to those with group administration permission.
     *
     * @param user the User to grant adminstrative privileges to.
     * @throws UnauthorizedException if does not have group admin permissions.
     */
    public void removeAdministrator(User user) throws UnauthorizedException;

    /**
     * Adds a member to the group.<p>
     *
     * This method is restricted to those with group administration permission.
     *
     * @param user the User to add to the group.
     * @throws UnauthorizedException if does not have group admin permissions.
     */
    public void addMember(User user) throws UnauthorizedException;

    /**
     * Removes a member from the group. If the User is not in the group, this
     * method does nothing.<p>
     *
     * This method is restricted to those with group administration permission.
     *
     * @param user the User to remove from the group.
     * @throws UnauthorizedException if does not have group admin permissions.
     */
    public void removeMember(User user) throws UnauthorizedException;

    /**
     * Returns true if the User has group administrator permissions. Group
     * administrators are also considered to be members.
     *
     * @return true if the User is an administrator of the group.
     */
    public boolean isAdministrator(User user);

    /**
     * Returns true if if the User is a member of the group.
     *
     * @return true if the User is a member of the group.
     */
    public boolean isMember(User user);

    /**
     * Returns the number of group administrators.
     *
     * @return the number of group administrators.
     */
    public int getAdministratorCount();

    /**
     * Returns the number of group members.
     *
     * @return the number of group members.
     */
    public int getMemberCount();

    /**
     * An iterator for all the users that are members of the group.
     *
     * @return an Iterator for all members of the group.
     */
    public Iterator members();

    /**
     * An iterator for all the users that are administrators of the group.
     *
     * @return an Iterator for all administrators of the group.
     */
    public Iterator administrators();

    /**
     * Returns the permissions for the group that correspond to the
     * passed-in Authorization.
     *
     * @param authorization the auth token to lookup permissions for.
     */
    public abstract ForumPermissions getPermissions(Authorization authorization);

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
