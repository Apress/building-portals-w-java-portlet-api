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

import com.Yasna.util.CacheSizes;
import com.Yasna.util.Cacheable;

/**
 * Defines a set of permissions for objects in the forum system that
 * users can be granted. Forum permissions are used by the protection
 * proxy objects defined for each major component of the system.
 */
public class ForumPermissions implements Cacheable {

    /**
     * Permission to read object.
     */
    public static final int READ = 0;

    /**
     * Permission to administer the entire sytem.
     */
    public static final int SYSTEM_ADMIN = 1;

    /**
     * Permission to administer a particular forum.
     */
    public static final int FORUM_ADMIN = 2;

    /**
     * Permission to administer a particular user.
     */
    public static final int USER_ADMIN = 3;

    /**
     * Permission to administer a particular group.
     */
    public static final int GROUP_ADMIN = 4;

    /**
     * Permission to take on a moderator role.
     */
    public static final int MODERATOR = 5;

    /**
     * Permission to create a new thread.
     */
    public static final int CREATE_THREAD = 6;

    /**
     * Permission to create a new message.
     */
    public static final int CREATE_MESSAGE = 7;

    private boolean [] values = new boolean[8];

    /**
     * Factory method to create full permissions.
     */
    public static ForumPermissions full() {
        return new ForumPermissions(true, true, true, true, true, true, true, true);
    }

    /**
     * Factory method to create an object with no permissions.
     */
    public static ForumPermissions none() {
        return new ForumPermissions(false, false, false, false, false, false, false, false);
    }

    /**
     * Factory method to create an object with read-only permissions.
     */
    public static ForumPermissions readOnly() {
        return new ForumPermissions(true, false, false, false, false, false, false, false);
    }

    /**
     * Create a new permissions object with the specified permissions.
     */
    public ForumPermissions(boolean READ, boolean SYSTEM_ADMIN,
            boolean FORUM_ADMIN, boolean USER_ADMIN, boolean GROUP_ADMIN,
            boolean MODERATOR, boolean CREATE_THREAD, boolean CREATE_MESSAGE)
    {
        values[0] = READ;
        values[1] = SYSTEM_ADMIN;
        values[2] = FORUM_ADMIN;
        values[3] = USER_ADMIN;
        values[4] = GROUP_ADMIN;
        values[5] = MODERATOR;
        values[6] = CREATE_THREAD;
        values[7] = CREATE_MESSAGE;
    }

    /**
     * Creates a new ForumPermission object by combining two permissions
     * objects. The higher permission of each permission type will be used.
     */
    public ForumPermissions(ForumPermissions perm1,
            ForumPermissions perm2)
    {
        values[0] = perm1.get(0) || perm2.get(0);
        values[1] = perm1.get(1) || perm2.get(1);
        values[2] = perm1.get(2) || perm2.get(2);
        values[3] = perm1.get(3) || perm2.get(3);
        values[4] = perm1.get(4) || perm2.get(4);
        values[5] = perm1.get(5) || perm2.get(5);
        values[6] = perm1.get(6) || perm2.get(6);
        values[7] = perm1.get(7) || perm2.get(7);
    }

    public ForumPermissions(boolean [] permissions) {
        this.values = permissions;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<values.length-1; i++) {
            buf.append(values[i]).append(",");
        }
        buf.append(values[values.length -1]);
        return buf.toString();
    }

    /**
     * Returns true if the permission of a particular type is allowed.
     */
    public boolean get(int type) {
        if (type < 0 || type > 7) {
            return false;
        }
        return values[type];
    }

    /**
     * Returns true if the permissions include system or forum admin
     * permissions.
     */
    public boolean isSystemOrForumAdmin() {
        return (values[FORUM_ADMIN] || values[SYSTEM_ADMIN]);
    }

    //FROM THE CACHEABLE INTERFACE//

    public int getSize() {
        //Approximate the size of the object in bytes by calculating the size
        //of each field.
        int size = 0;
        size += CacheSizes.sizeOfObject();              //overhead of object
        size += CacheSizes.sizeOfObject();              //ref to array
        size += CacheSizes.sizeOfBoolean() * 8;         //boolean array vals
        return size;
    }
}
