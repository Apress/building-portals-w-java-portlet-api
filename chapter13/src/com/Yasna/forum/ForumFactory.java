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
 * A ForumFactory provides access to and management of Forums. It is the point
 * of entry for the entire Yazd system.
 * <p>
 * A concrete instance of ForumFactory can be obtained by calling the getInstance()
 * method with an Authorization token. The Authorization token determines with
 * what permissions the rest of the objects in the system will be accessed with.
 * <p>
 * Usually the first steps of any program interacting with the Yazd system are:
 * <ul>
 *   <li> Obtain an authorization token by calling
 *    AuthorizationFactory.getInstance().getAuthorization(username, password);
 *   <li> Use that authorization to get a ForumFactory instance.
 *   <li> Use the forum factory to access forums and other Yazd content.
 * </ul>
 * It is also possible to access Yazd content with anonymous permissions. See
 * the AuthorizationFactory class for more information.
 * <p>
 * ForumFactory is an abstract class so that the actual implementation is
 * pluggable. For example, the default Yazd implementation uses a database
 * backend. You can optionally plug in your own backend that might use the
 * filesystem, for example. When first creating the forum factory, Yazd will
 * look for the Yazd property "ForumFactory.className". If it fails to find
 * that property, it will use the default class.
 *
 * @see AuthorizationFactory
 */
public abstract class ForumFactory {

    private static Object initLock = new Object();
    private static String className = "com.Yasna.forum.database.DbForumFactory";
    private static ForumFactory factory = null;

    /**
     * Returns a concrete ForumFactory instance. Permissions corresponding
     * to the Authorization will be used. If getting the factory fails, null
     * will be returned.
     *
     * @param authorization the auth token for the user.
     * @return a concrete ForumFactory instance.
     */
    public static ForumFactory getInstance(Authorization authorization) {
        //If no valid authorization passed in, return null.
        if (authorization == null) {
            return null;
        }
        if (factory == null) {
            synchronized(initLock) {
                if (factory == null) {
                    String classNameProp = PropertyManager.getProperty("ForumFactory.className");
                    if (classNameProp != null) {
                        className = classNameProp;
                    }
                    try {
                        //Load the class and create an instance.
                        Class c = Class.forName(className);
                        factory = (ForumFactory)c.newInstance();
                    }
                    catch (Exception e) {
                        System.err.println("Failed to load ForumFactory class "
                            + className + ". Yazd cannot function normally.");
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }

        //Wrap the factory with a proxy to provide security. We also pass
        //in the username and password to the proxy for its special
        //implementation of the getForum() method. See below for more details.
        ForumFactoryProxy proxy = new ForumFactoryProxy(
                                    factory,
                                    authorization,
                                    factory.getPermissions(authorization)
                                  );
        return proxy;
    }

    /**
     * Creates a new forum. This method should always be used instead of
     * trying to instantiate a forum directly.
     *
     * @param name the name of the forum.
     * @param description the description of the forum.
     * @throws UnauthorizedException if not allowed to create a Forum.
     * @throws ForumAlreadExistsException if the forum name already exists.
     */
    public abstract Forum createForum(String name, String description)
            throws UnauthorizedException, ForumAlreadyExistsException;

    /**
     * Returns the forum with the specified forumID.
     *
     * @param forumID the id of the forum to return.
     * @return the Forum specified by forumID.
     * @throws UnauthorizedException if not allowed to read the forum.
     * @throws ForumNotFoundException if the requested forum does not exist.
     */
    public abstract Forum getForum(int forumID)
            throws ForumNotFoundException, UnauthorizedException;

    /**
     * Returns the Forum with the specified name.
     *
     * @param name the name of the forum to return.
     * @return the forum with the specified name.
     * @throws ForumNotFoundException if the requested forum does not exist.
     * @throws UnauthorizedException if not allowed to read the forum.
     */
    public abstract Forum getForum(String name)
            throws ForumNotFoundException, UnauthorizedException;

    /**
     * Returns the total number of forums. This number might not agree
     * with the number of forums returned by ForumFactory.forums() since that
     * method return an Iterator of forums that a user has READ access for.
     *
     * @return the total number of forums.
     */
    public abstract int getForumCount();

    /**
     * Returns an Iterator of Forum objects for all the forums in the system
     * that the user has READ access for. Read access can be granted in the
     * following ways:
     * <ul>
     *   <li> Anonymous read permission is enabled for the forum; anyone can
     *        read it.
     *   <li> The "all users" read permission is set so that any registered
     *        user can read the forum.
     *   <li> The user belongs to a group that has been granted read permission.
     *   <li> The user has been specifically granted read permission.
     *   <li> The current user is a system admin or admin of this forum. This
     *        allows automatic read permission.
     * </ul>
     *
     * @return an Iterator of Forum objects for all forums in the system that
     *    the user has read permission for.
     */
    public abstract Iterator forums();
   
   /**
    * Returns an Iterator of Forum objects for all the forums in the system
    * that the user has Moderation, Forum Admin or System Admin access to.
    */

    public abstract Iterator forumsModeration();


    /**
     * Deletes a forum and all of its content. This method is not always
     * guaranteed to be safe to call. For example, if multiple clients have
     * handles on a forum, and that forum is subsequently deleted, the behavior
     * of the forum objects that the clients have handles on is unspecified and
     * may result in errors.
     *
     * @param forum the forum to delete.
     * @throws UnauthorizedException if not allowed to delete a forum.
     */
    public abstract void deleteForum(Forum forum)
            throws UnauthorizedException;

    /**
     * Returns a ProfileManager that can be used to manage Users and Groups.
     */
    public abstract ProfileManager getProfileManager();

    /**
     * Returns the search indexer which can be used to manage the index used
     * by Yazd to perform searches.
     *
     * @throws UnauthorizedException if not a system administator.
     * @return a search indexer.
     */
    public abstract SearchIndexer getSearchIndexer()
            throws UnauthorizedException;

    /**
     * Returns all the userID's of users with a particular system permission.
     * System permissions apply to all forums.
     *
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public abstract int [] usersWithPermission(int permissionType)
            throws UnauthorizedException;

    /**
     * Returns all the groupID's of groups with a particular system permission.
     * System permissions apply to all forums.
     *
     * @throws UnauthorizedException if does not have ADMIN permissions.
     */
    public abstract int[] groupsWithPermission(int permissionType)
            throws UnauthorizedException;

    /**
     * Returns the permissions for the factory that correspond to the
     * passed-in Authorization.
     *
     * @param authorization the auth token for the user.
     * @return the permissions for this object.
     */
    public abstract ForumPermissions getPermissions(Authorization authorization);

    /**
     * Returns true if the handle on the object has the permission specified.
     * A list of possible permissions can be found in the ForumPermissions
     * class. Certain methods of this class are restricted to certain
     * permissions as specified in the method comments.
     *
     * @param type the type of permission to check for.
     * @see ForumPermissions
     */
    public abstract boolean hasPermission(int type);
}
