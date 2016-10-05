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

import com.Yasna.util.Cache;
import com.Yasna.util.CacheObject;
import com.Yasna.util.Cacheable;

/**
 * Central cache management of all caches used by Yazd.
 */
public class DbCacheManager {

    public static int USER_CACHE = 0;
    public static int USER_ID_CACHE = 1;
    public static int GROUP_CACHE = 2;
    public static int GROUP_ID_CACHE = 3;
    public static int FORUM_CACHE = 4;
    public static int FORUM_ID_CACHE = 5;
    public static int THREAD_CACHE = 6;
    public static int MESSAGE_CACHE = 7;
    public static int USER_PERMS_CACHE = 8;

    protected Cache[] caches;

    private boolean cacheEnabled = true;

    public DbCacheManager() {
        int MINUTE = 1000*60;
        int HOUR = MINUTE*60;

        caches = new Cache[9];

        //Initialize all cache structures
        caches[USER_CACHE] = new Cache(256*1024, 6*HOUR);
        caches[USER_ID_CACHE] = new Cache(128*1024, 6*HOUR);
        caches[GROUP_CACHE] = new Cache(128*1024, 6*HOUR);
        caches[GROUP_ID_CACHE] = new Cache(128*1024, 6*HOUR);
        caches[FORUM_CACHE] = new Cache(128*1024, 6*HOUR);
        caches[FORUM_ID_CACHE] = new Cache(128*1024, 6*HOUR);
        caches[THREAD_CACHE] = new Cache(128*1024, 6*HOUR);
        caches[MESSAGE_CACHE] = new Cache(512*1024, 6*HOUR);
        //The user permissions cache is a special one. It's actually a Cache
        //of Cache objects. Each of the cache objects in the main cache
        //corresponds to a particular forum, and is used to cache the
        //permissions that a user has for a forum. In order to handle this
        //requirement, we use a special subclass of Cache.
        caches[USER_PERMS_CACHE] = new UserPermsCache(256*1024, 24*HOUR);
    }

    public Cache getCache(int cacheType) {
        return caches[cacheType];
    }

    public void add(int cacheType, Object key, Cacheable object) {
        caches[cacheType].add(key, object);
    }

    public Cacheable get(int cacheType, Object key) {
        if (!cacheEnabled) {
            return null;
        }
        return caches[cacheType].get(key);
    }

    public void remove(int cacheType, Object key) {
        caches[cacheType].remove(key);
        //when cache becomes distributed, we'd send out an expire message
        //here to all other yazd servers.
    }

    public void removeUserPerm(Object userID) {
        Object [] values  = caches[USER_PERMS_CACHE].values().toArray();
        for (int i=0; i<values.length; i++) {
            Cache cache = (Cache)((CacheObject)values[i]).object;
            cache.remove(userID);
        }
        //when cache becomes distributed, we'd send out an expire message
        //here to all other yazd servers.
    }

    public void removeUserPerm(Object userID, Object forumID) {
        Cache cache = (Cache)caches[USER_PERMS_CACHE].get(forumID);
        if (cache != null) {
            cache.remove(userID);
        }
        //when cache becomes distributed, we'd send out an expire message
        //here to all other yazd servers.
    }

    public void clear(int cacheType) {
        caches[cacheType].clear();
        //when cache becomes distributed, we'd send out an expire message
        //here to all other yazd servers.
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }
}

/**
 * Special purpose Cache to hold all of the different user permission cache
 * objects. The main feature is that new caches are automatically created so
 * that calling get() never returns null.
 */
class UserPermsCache extends Cache {

    public UserPermsCache(int size, long expireTime) {
        super(size, expireTime);
    }

    public synchronized Cacheable get(Object key) {
        Cache subCache = (Cache)super.get(key);
        if (subCache == null) {
            //cache has expired, or is not there, so put a new one in there.
            //Cache objects only need to last as long as a user's session
            //does. Half an hour is a reasonable amount of time for this.
            subCache = new Cache(2*1024, 30*1000*60);
            add(key, subCache);
        }
        return subCache;
    }

    public synchronized void remove(Object key) {
        CacheObject cacheObject = (CacheObject)cachedObjectsHash.get(key);
        //If the object is not in cache, stop trying to remove it.
        if (cacheObject == null) {
            return;
        }
        //remove from the hash map
        cachedObjectsHash.remove(key);
        //remove from the cache order list
        cacheObject.lastAccessedListNode.remove();
        cacheObject.ageListNode.remove();
        //remove references to linked list nodes
        cacheObject.ageListNode = null;
        cacheObject.lastAccessedListNode = null;
        //removed the object, so subtract its size from the total.
        size -= cacheObject.size;

        //Finally, clear the sub-cache to make sure memory is released.
        ((Cache)cacheObject.object).clear();
    }

    /**
     * Returns the current size in bytes of the cache. The base getSize() method
     * does not work correctly because the sub-caches are empty when we first
     * add them rather than the normal cache assumption that objects are near
     * the size that they will always be.
     *
     * @return the size of the cache in bytes.
     */
    public int getSize() {
        int size = 0;
        Object [] values  = values().toArray();
        for (int i=0; i<values.length; i++) {
            Cache cache = (Cache)values[i];
            size += cache.getSize();
        }
        return size;
    }
}

