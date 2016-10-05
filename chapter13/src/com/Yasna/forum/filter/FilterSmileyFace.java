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

import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageFilter;

/**
 * A ForumMessageFilter that converts ASCII faces into images.
 * The faces currently handled are -- are :) , :( , and 8)
 */
public class FilterSmileyFace extends ForumMessageFilter {

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
    public FilterSmileyFace() {
        super();
        props = new Properties();
        propDescriptions = new Properties();
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
    public FilterSmileyFace(ForumMessage message, Properties props,
            Properties propDescriptions)
    {
        super(message);
        this.props = new Properties(props);
        this.propDescriptions = new Properties(propDescriptions);
    }

    /**
     * Clones a new filter that will have the same properties and that
     * will wrap around the specified message.
     *
     * @param message the ForumMessage to wrap the new filter around.
     */
    public ForumMessageFilter clone(ForumMessage message){
        return new FilterSmileyFace(message, props, propDescriptions);
    }

    /**
     * Returns the name of the filter.
     */
    public String getName() {
        return "ASCII Face Converter";
    }

    /**
     * Returns a description of the filter.
     */
    public String getDescription() {
        return "Converts ASCII faces to images. The faces currently " +
                "handled are :) , :( , and 8)";
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
    }

    /**
     * <b>Overloaded</b> to return the subject of the message with ASCII
     * faces converted to images.
     */
    public String getSubject() {
        return addSmileyFace(message.getSubject());
    }

    /**
     * <b>Overloaded</b> to return the body of the message with ASCII
     * faces converted to images.
     */
    public String getBody() {
        return addSmileyFace(message.getBody());
    }

    /**
     * Creates properties and sets their descriptions.
     */
    private void initializeProperties() {
        props.put("happyURL","");
        props.put("sadURL","");
        props.put("coolURL","");

        propDescriptions.put("happyURL","URL of the desired :) image");
        propDescriptions.put("sadURL","URL of the desired :( image");
        propDescriptions.put("coolURL","URL of the desired 8) image");
    }

    /**
    * This method takes a string which may contain ':)' and
    * converts them to smiley face images.
    *
    * @param input The text to be converted.
    * @return The input string with the ':)' replaced
    * with smiley face images.
    */
    private String addSmileyFace( String input ) {
    	String happy = props.getProperty("happyURL");
    	String sad = props.getProperty("sadURL");
    	String cool = props.getProperty("coolURL");

        // Check if the string is null or zero length -- if so, return what was sent in.
        if( input == null || input.length() == 0 ) {
            return input;
        }
        // Use a StringBuffer in lieu of String concatenation -- it is much more efficient this way.
        StringBuffer buf = new StringBuffer();
        char ch = ' ', ch2 = ' ';
        int index;
        for(index = 0; index<input.length()-1; index++ ) {
            ch = input.charAt(index); ch2 = input.charAt(index+1);
            if (happy != null && happy.length() > 0 && ch == ':' && ch2 == ')') {
                buf.append("<img src=\"").append(happy).append("\">");
                index++; // Skip a character
            }
            else if (sad != null && sad.length() > 0 && ch == ':' && ch2 == '(') {
                buf.append("<img src=\"").append(sad).append("\">");
                index++; // Skip a character
            }
            else if (cool != null && cool.length() > 0 && ch == '8' && ch2 == ')') {
                buf.append("<img src=\"").append(cool).append("\">");
                index++; // Skip a character
            }
            else {
                buf.append( ch );
            }
        }
        //Append last character if needed.
        if (index != input.length()) {
            buf.append(input.charAt(input.length()-1));
        }
        return buf.toString();
    }
} 
