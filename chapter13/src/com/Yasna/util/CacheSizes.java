/**
 * $RCSfile: CacheSizes.java,v $
 * $Revision: 1.1.1.1 $
 * $Date: 2002/04/26 23:15:09 $
 *
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
 *        CoolServlets.com (http://www.Yasna.com)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Jive" and "CoolServlets.com" must not be used to
 *    endorse or promote products derived from this software without
 *    prior written permission. For written permission, please
 *    contact webmaster@Yasna.com.
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
 * on CoolServlets.com, please see <http://www.Yasna.com>.
 */

package com.Yasna.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class for determining the sizes in bytes of commonly used objects.
 * Classes implementing the Cacheable interface should use this class to
 * determine their size.
 */
public class CacheSizes {

    /**
     * Returns the size in bytes of a basic Object. This method should only
     * be used for actual Object objects and not classes that extend Object.
     *
     * @return the size of an Object.
     */
    public static int sizeOfObject() {
        return 4;
    }

    /**
     * Returns the size in bytes of a String.
     *
     * @param string the String to determine the size of.
     * @return the size of a String.
     */
    public static int sizeOfString(String string) {
        if (string == null) {
            return 0;
        }
        return 4 + string.length()*2;
    }

    /**
     * Returns the size in bytes of a primitive int.
     *
     * @return the size of a primitive int.
     */
    public static int sizeOfInt() {
        return 4;
    }

    /**
     * Returns the size in bytes of a primitive char.
     *
     * @return the size of a primitive char.
     */
    public static int sizeOfChar() {
        return 2;
    }

    /**
     * Returns the size in bytes of a primitive boolean.
     *
     * @return the size of a primitive boolean.
     */
    public static int sizeOfBoolean() {
        return 1;
    }

    /**
     * Returns the size in bytes of a primitive long.
     *
     * @return the size of a primitive long.
     */
    public static int sizeOfLong() {
        return 8;
    }

    /**
     * Returns the size in bytes of a primitive double.
     *
     * @return the size of a primitive double.
     */
    public static int sizeOfDouble() {
        return 8;
    }

    /**
     * Returns the size in bytes of a Date.
     *
     * @return the size of a Date.
     */
    public static int sizeOfDate() {
        return 12;
    }

    /**
     * Returns the size in bytes of a Properties object. All properties and
     * property names must be Strings.
     *
     * @param properties the Properties object to determine the size of.
     * @return the size of a Properties object.
     */
    public static int sizeOfProperties(Properties properties) {
        if (properties == null) {
            return 0;
        }
        //Base properties object
        int size = 36;
        //Add in size of each property
        Enumeration enum = properties.elements();
        while(enum.hasMoreElements()) {
            String prop = (String)enum.nextElement();
            size += sizeOfString(prop);
        }
        //Add in property names
        enum = properties.propertyNames();
        while(enum.hasMoreElements()) {
            String prop = (String)enum.nextElement();
            size += sizeOfString(prop);
        }
        return size;
    }

    /**
     * Returns the size in bytes of a Map object. All keys and
     * values <b>must be Strings</b>.
     *
     * @param map the Map object to determine the size of.
     * @return the size of the Map object.
     */
    public static int sizeOfMap(Map map) {
        if (map == null) {
            return 0;
        }
        //Base map object -- should be something around this size.
        int size = 36;
        //Add in size of each value
        Iterator iter = map.values().iterator();
        while(iter.hasNext()) {
            String value = (String)iter.next();
            size += sizeOfString(value);
        }
        //Add in each key
        iter = map.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            size += sizeOfString(key);
        }
        return size;
    }
}
