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

package com.Yasna.forum.filter;

import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageFilter;

/**
 * A ForumMessageFilter that replaces profanity.
 */
public class FilterProfanity extends ForumMessageFilter {

    /**
     * Array of all the bad words to filter.
     */
    private String [] filterList;

    /**
     * Indicates if case of words should be ignored.
     */
    private boolean ignoreCase = true;

    /**
     * Property values of the filter.
     */
    private Properties props;

    /**
     * Property descriptions of the filter.
     */
    private Properties propDescriptions;

    /**
     * Creates a new filter not associated with a message. This is
     * generally only useful for defining a template filter that other
     * fitlers will be cloned from.
     */
    public FilterProfanity() {
        super();
        this.props = new Properties();
        this.propDescriptions = new Properties();
        initializeProperties();
    }

    /**
     * Creates a new filter wrapped around the specified message. This
     * constructor is normally called when cloning a filter template.
     *
     * @param message the ForumMessage to wrap the new filter around.
     * @param properties the property values for the filter.
     * @param propertyDescriptions the property descriptions for the filter.
     */
    public FilterProfanity(ForumMessage message, Properties props,
            Properties propDescriptions, String [] filterList, boolean ignoreCase)
    {
        super(message);
        this.props = new Properties(props);
        this.propDescriptions = new Properties(propDescriptions);
        this.filterList = filterList;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Clones a new filter that will have the same properties and that
     * will wrap around the specified message.
     *
     * @param message the ForumMessage to wrap the new filter around.
     */
    public ForumMessageFilter clone(ForumMessage message){
        return new FilterProfanity(message, props, propDescriptions,
                filterList, ignoreCase);
    }

    /**
     * Returns the name of the filter.
     */
    public String getName() {
        return "Profanity Filter";
    }

    /**
     * Returns a description of the filter.
     */
    public String getDescription() {
        return "Removes profanity from messages using a custom word list.";
    }

    /**
     * Returns the author of the filter.
     */
    public String getAuthor() {
        return "CoolServlets.com";
    }

    /**
     * Returns the major version number of the filter.
     */
    public int getMajorVersion() {
        return 1;
    }

    /**
     * Returns the minor version number of the filter.
     */
    public int getMinorVersion() {
        return 0;
    }

    /**
     * Returns the value of a property of the filter.
     *
     * @param name the name of the property.
     * @returns the value of the property.
     */
    public String getFilterProperty(String name) {
        return props.getProperty(name);
    }

    /**
     * Returns the description of a property of the filter.
     *
     * @param name the name of the property.
     * @return the description of the property.
     */
    public String getFilterPropertyDescription(String name) {
        return propDescriptions.getProperty(name);
    }

    /**
     * Returns an Enumeration of all the property names.
     */
    public Enumeration filterPropertyNames() {
        return props.propertyNames();
    }

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
    public void setFilterProperty(String name, String value)
            throws IllegalArgumentException
    {
        if (props.getProperty(name) == null) {
            throw new IllegalArgumentException();
        }
        props.put(name, value);
        applyProperties();
    }

    /**
     * <b>Overloaded</b> to return the subject of the message with profanity
     * filtered out.
     */
    public String getSubject() {
        return filterProfanity(message.getSubject());
    }

    /**
     * <b>Overloaded</b> to return the body of the message with profanity
     * filtered out.
     */
    public String getBody() {
        return filterProfanity(message.getBody());
    }

    /**
     * Creates properties and sets their descriptions.
     */
    private void initializeProperties() {
        filterList = new String[0];
        props.put("filterList","");
        props.put("ignoreCase","on");

        propDescriptions.put("filterList","A comma delimitted list of "
            + "the bad words to filter out.");
        propDescriptions.put("ignoreCase","Indicates whether the case "
            + "of words should be ignored or not. For example, when on, the "
            + "words 'CRap' and 'crap' would both be filterd if an entry of "
            + "'CRAP' was found in the filter list.");
    }

    private void applyProperties() {
        ignoreCase = ((String)props.getProperty("ignoreCase")).equals("on");
        String list = (String)props.get("filterList");
        StringTokenizer tokens = new StringTokenizer(list,",");
        String [] newFilterList = new String[tokens.countTokens()];
        for (int i=0; i<newFilterList.length; i++) {
            if (ignoreCase) {
                newFilterList[i] = tokens.nextToken().toLowerCase().trim();
            }
            else {
                newFilterList[i] = tokens.nextToken().trim();
            }
        }
        filterList = newFilterList;
    }

    /**
     * Filters out bad words.
     */
    private String filterProfanity(String str) {
        // Check to see if the string is null or zero-length
        if (str == null || "".equals(str)) {
            return str;
        }
        String lower;
        if (ignoreCase) {
            lower = str.toLowerCase();
        }
        else {
            lower = str;
        }
        for (int i=0; i<filterList.length; i++) {
            str = replace(str, lower, filterList[i], cleanWord(filterList[i].length()));
        }
        return str;
    }

    /**
     * Generates a string of characters of specified length. For example:
     * !@%$ or %!@$%!@@ or *****
     */
    private String cleanWord(int length) {
        char[] newWord = new char[length];
        for (int i=0; i<newWord.length; i++) {
            newWord[i] = '*';
        }
        return new String(newWord);
    }

    /**
     * Replaces all instances of oldString with newString in the String line.
     */
    private String replace(String line, String lowerCaseLine,
            String oldString, String newString )
    {
        int i=0;
        if ( ( i=lowerCaseLine.indexOf( oldString, i ) ) >= 0 ) {
            int oLength = oldString.length();
            int nLength = newString.length();
            StringBuffer buf = new StringBuffer(line.length()+15);
            buf.append(line.substring(0,i)).append(newString);
            i += oLength;
            int j = i;
            while( ( i=lowerCaseLine.indexOf( oldString, i ) ) > 0 ) {
                buf.append(line.substring(j,i)).append(newString);
                i += oLength;
                j = i;
            }
            buf.append(line.substring(j));
            return buf.toString();
        }
        return line;
    }
}

