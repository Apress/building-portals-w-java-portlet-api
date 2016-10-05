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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import com.Yasna.codeviewer.CodeViewer;
import com.Yasna.forum.ForumMessage;
import com.Yasna.forum.ForumMessageFilter;

/**
 * A ForumMessageFilter that syntax highlights Java code appearing between
 * [code][/code] tags in the body of ForumMessage.
 */
public class FilterCodeHighlight extends ForumMessageFilter
        implements Serializable
{

    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Property values of the filter.
     */
    private Properties props;

    /**
     * Property descriptions of the filter.
     */
    private Properties propDescriptions;

    /**
     * Much of the work of this filter is done by a CodeViewer object. We
     * make it a transient variable since there is no reason to serialize it.
     */
    private transient CodeViewer cv;

    /**
     * Creates a new filter not associated with a message. This is
     * generally only useful for defining a template filter that other
     * fitlers will be cloned from.
     */
    public FilterCodeHighlight() {
        super();
        props = new Properties();
        propDescriptions = new Properties();
        cv = new CodeViewer();
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
    public FilterCodeHighlight(ForumMessage message, Properties properties,
            Properties propertyDescriptions)
    {
        super(message);
        this.props = new Properties(properties);
        this.propDescriptions = new Properties(propertyDescriptions);
        cv = new CodeViewer();
        applyProperties();
    }

    /**
     * Clones a new filter that will have the same properties and that
     * will wrap around the specified message.
     *
     * @param message the ForumMessage to wrap the new filter around.
     */
    public ForumMessageFilter clone(ForumMessage message){
        return new FilterCodeHighlight(message, props, propDescriptions);
    }

    /**
     * Returns the name of the filter.
     */
    public String getName() {
        return "Java Code Syntax Highlighter";
    }

    /**
     * Returns a description of the filter.
     */
    public String getDescription() {
        return "Highlights Java code that appears between [code][/code] tags";
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
     * <b>Overloaded</b> to return the body of message with Java code between
     * [code] [/code] tags syntax highlighted.
     */
    public String getBody() {
        return highlightCode(message.getBody());
    }

    /**
     * Creates properties and sets their descriptions.
     */
    private void initializeProperties() {
        props.put("commentStart",cv.getCommentStart());
        props.put("commentEnd",cv.getCommentEnd());
        props.put("stringStart",cv.getStringStart());
        props.put("stringEnd",cv.getStringEnd());
        props.put("reservedWordStart",cv.getReservedWordStart());
        props.put("reservedWordEnd",cv.getReservedWordEnd());

        propDescriptions.put("commentStart","A HTML start tag that determines how comments will be displayed");
        propDescriptions.put("commentEnd","A HTML end tag that should correspond to the commentStart tag");
        propDescriptions.put("stringStart","A HTML start tag that determines how strings will be displayed");
        propDescriptions.put("stringEnd","A HTML end tag that should correspond to the stringStart tag");
        propDescriptions.put("reservedWordStart","A HTML start tag that determines how reserved words will be displayed");
        propDescriptions.put("reservedWordEnd","A HTML end tag that should correspond to the reservedWordStart tag");
    }

    private void applyProperties() {
        cv.setCommentStart(props.getProperty("commentStart"));
        cv.setCommentEnd(props.getProperty("commentEnd"));
        cv.setStringStart(props.getProperty("stringStart"));
        cv.setStringEnd(props.getProperty("stringEnd"));
        cv.setReservedWordStart(props.getProperty("reservedWordStart"));
        cv.setReservedWordEnd(props.getProperty("reservedWordEnd"));
    }

    /**
    * This method takes a string which may contain Java code.
    * The Java code will be highlighted.
    *
    * @param input The text to be converted.
    * @return The input string with any Java code highlighted.
    */
    private String highlightCode( String input ) {
        // Check if the string is null or zero length -- if so, return what was sent in.
        if( input == null || input.length() == 0 ) {
            return input;
        }
        else {
            StringBuffer buf = new StringBuffer();
            int i = 0, j = 0, oldend = 0;

            while ( ( i=input.indexOf( "[code]", oldend ) ) >= 0 ) {
                //Check to see where the ending code tag is and store than in j
                if ( (j=input.indexOf( "[/code]", i+6 ) ) < 0)  {
                    //End at end of input if no closing tag is given
                    j = input.length()-7;
                }
                // Take the string up to the code, append the string returned by CodeViewer
                buf.append(input.substring(oldend,i));
                buf.append("<pre>");
                //Read line by line and filter accordingly.
                //StringTokenizer tokens = new StringTokenizer(input.substring(i+6,j-1), NEW_LINE);
                StringTokenizer tokens = new StringTokenizer(input.substring(i+6,j), NEW_LINE);
                while (tokens.hasMoreTokens()) {
                    buf.append(cv.syntaxHighlight(tokens.nextToken()));
                    buf.append(NEW_LINE);
                }
                buf.append("</pre>");
                // Next time, want to start looking after ending [/code] tag
                oldend = j+7;
            }
            buf.append(input.substring(oldend,input.length()));
            return buf.toString();
        }
    }
}
