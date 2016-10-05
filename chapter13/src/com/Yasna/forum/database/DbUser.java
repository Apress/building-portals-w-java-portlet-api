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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.ForumPermissions;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;
import com.Yasna.util.CacheSizes;
import com.Yasna.util.Cacheable;
import com.Yasna.util.StringUtils;

/**
 * Database implementation of the User interface. Additionally, it filters all
 * HTML tags from fields before returning them for security purposes.<p>
 *
 * Use of the Yazd user system is optional. There a number of different ways
 * to create your own user system or to integrate into an existing user
 * system:<ul>
 *   <li> Edit the source of this class and modify the database queries to match
 *     your user system.
 *   <li> Implement a set of custom classes and tell the ForumFactory to load
 *     them. In this case, it is still recommended to use the Yazd user API
 *     since that will mean much less reimplementation work in the other
 *     classes.</ul>
 *
 * If you can follow the Yazd API for your own user data, but need access
 * within Yazd to extended user properties, such as addresses or other
 * personal data, and easy solution is to adapt the user properties facility
 * to load and access this data.
 */
public class DbUser implements User, Cacheable {

    /** DATABASE QUERIES **/
    private static final String LOAD_PROPERTIES =
        "SELECT name, propValue FROM yazdUserProp WHERE userID=?";
    private static final String DELETE_PROPERTIES =
        "DELETE FROM yazdUserProp WHERE userID=?";
    private static final String INSERT_PROPERTY =
        "INSERT INTO yazdUserProp(userID,name,propValue) VALUES(?,?,?)";
    private static final String LOAD_USER_BY_USERNAME =
        "SELECT * FROM yazdUser WHERE username=?";
    private static final String LOAD_USER_BY_ID =
        "SELECT * FROM yazdUser WHERE userID=?";
    private static final String INSERT_USER =
        "INSERT INTO yazdUser(userID,username,passwordHash,email,emailVisible," +
        "nameVisible) VALUES(?,?,?,?,?,?)";
    private static final String SAVE_USER =
        "UPDATE yazdUser SET passwordHash=?,email=?,emailVisible=?,name=?," +
        "nameVisible=? WHERE userID=?";
    private static final String DELETE_PERMISSIONS =
        "DELETE FROM yazdUserPerm WHERE userID=?";
    private static final String INSERT_PERMISSION =
        "INSERT INTO yazdUserPerm(userID,forumID,permission) VALUES(?,?,?)";

    /**
     * user id of -2 means no user id has been set yet. -1 is reserved for
     * "anonymous user" and 0 is reserved for "all users".
     */
    private int id = -2;
    private String username;
    private String passwordHash;
    private String name = "";
    private boolean nameVisible = true;
    private String email;
    private boolean emailVisible = true;
    private Properties properties;
    private Object propertyLock = new Object();

    /**
     * Create a new DbUser with all required fields.
     *
     * @param username the username for the user.
     * @param password a password for the user.
     * @param email the email address for the user.
     */
    protected DbUser(String username, String password, String email) {
        this.id = DbSequenceManager.nextID("User");
        this.username = username;
        //Compute hash of password.
        this.passwordHash = StringUtils.hash(password);
        this.email = email;
        properties = new Properties();
        insertIntoDb();
    }

    /**
     * Load a DbUser object specified by userID.
     *
     * @param userID the userID of the user to load.
     */
    protected DbUser(int userID) throws UserNotFoundException {
        this.id = userID;
        loadFromDb();
        loadProperties();
    }

    /**
     * Load a DbUser object specified by username.
     *
     * @param username the username of the user to load.
     */
    protected DbUser(String username) throws UserNotFoundException {
        this.username = username;
        loadFromDb();
        loadProperties();
    }

    //FROM THE USER INTERFACE//

    public int getID() {
        return id;
    }

    public boolean isAnonymous() {
        return (id==-1);
    }

    public String getUsername() {
        return StringUtils.escapeHTMLTags(username);
    }

    public String getName() {
        return StringUtils.escapeHTMLTags(name);
    }

    public void setName(String name) throws UnauthorizedException {
        this.name = name;
        saveToDb();
    }

    public boolean isNameVisible() {
        return nameVisible;
    }

    public void setNameVisible(boolean visible) throws UnauthorizedException {
        this.nameVisible = visible;
        saveToDb();
    }

    public void setPassword(String password) throws UnauthorizedException {
        //Compute hash of password.
        this.passwordHash = StringUtils.hash(password);
        saveToDb();
    }

    public String getPasswordHash() throws UnauthorizedException {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        saveToDb();
    }

    public String getEmail() {
        return StringUtils.escapeHTMLTags(email);
    }

    public void setEmail(String email) throws UnauthorizedException {
        this.email = email;
        saveToDb();
    }

    public boolean isEmailVisible() {
        return emailVisible;
    }

    public void setEmailVisible(boolean visible) throws UnauthorizedException {
        this.emailVisible = visible;
        saveToDb();
    }

    public String getProperty(String name) {
        return StringUtils.escapeHTMLTags((String)properties.get(name));
    }

    public Enumeration propertyNames() {
        return properties.propertyNames();
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
        saveProperties();
    }

    public ForumPermissions getPermissions(Authorization authorization) {
        if (authorization.getUserID() == id || id==-1 || id==0) {
            return new ForumPermissions(false,false,false,true,false,false,false,false);
        }
        else {
            return ForumPermissions.none();
        }
    }

    public boolean hasPermission(int type) {
        return true;
    }

    //FROM THE CACHEABLE INTERFACE//

    public int getSize() {
        //Approximate the size of the object in bytes by calculating the size
        //of each field.
        int size = 0;
        size += CacheSizes.sizeOfObject();              //overhead of object
        size += CacheSizes.sizeOfInt();                 //id
        size += CacheSizes.sizeOfString(username);      //username
        size += CacheSizes.sizeOfString(passwordHash);  //password
        size += CacheSizes.sizeOfString(name);          //name
        size += CacheSizes.sizeOfString(email);         //email
        size += CacheSizes.sizeOfBoolean();             //nameVisible
        size += CacheSizes.sizeOfBoolean();             //emailVisible
        size += CacheSizes.sizeOfObject();              //property lock
        size += CacheSizes.sizeOfProperties(properties);//properties object

        return size;
    }

    //OTHER METHODS

    /**
     * Returns a String representation of the User object using the username.
     *
     * @return a String representation of the User object.
     */
    public String toString() {
        return username;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && object instanceof DbUser) {
            return id == ((DbUser)object).getID();
        }
        else {
            return false;
        }
    }

    /**
     * Loads user properties from the database.
     */
    private void loadProperties() {
        //If "anonymous" or "all users", do nothing.
        if (id == -1 || id == 0) {
            properties = new Properties();
            return;
        }
        //Acquire a lock so that no other property loading or saving can be
        //performed at the same time.
        synchronized(propertyLock) {
            Properties newProps = new Properties();
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DbConnectionManager.getConnection();
                pstmt = con.prepareStatement(LOAD_PROPERTIES);
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()) {
                    String name = rs.getString("name");
                    String value = rs.getString("propValue");
                    newProps.put(name, value);
                }
            }
            catch( SQLException sqle ) {
                System.err.println("Error in DbUser:loadProperties():" + sqle);
                sqle.printStackTrace();
            }
            finally {
                try {  pstmt.close(); }
                catch (Exception e) { e.printStackTrace(); }
                try {  con.close();   }
                catch (Exception e) { e.printStackTrace(); }
            }
            this.properties = newProps;
        }
    }

    /**
     * Saves user properties to the database.
     */
    private void saveProperties() {
        //If "anonymous" or "all users", do nothing.
        if (id == -1 || id == 0) {
            return;
        }
        //Acquire a lock so that no other property loading or saving can be
        //performed at the same time.
        synchronized(propertyLock) {
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DbConnectionManager.getConnection();
                //Delete all old values.
                pstmt = con.prepareStatement(DELETE_PROPERTIES);
                pstmt.setInt(1, id);
                pstmt.execute();
                pstmt.close();
                //Now insert new values.
                pstmt = con.prepareStatement(INSERT_PROPERTY);
                Enumeration enum = properties.keys();
                while (enum.hasMoreElements()) {
                    String name = (String)enum.nextElement();
                    String value = (String)properties.get(name);
                    pstmt.setInt(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, value);
                    pstmt.executeUpdate();
                }
            }
            catch( SQLException sqle ) {
                System.err.println(sqle);
            }
            finally {
                try {  pstmt.close(); }
                catch (Exception e) { e.printStackTrace(); }
                try {  con.close();   }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * Load the user data from the database.
     */
    private void loadFromDb() throws UserNotFoundException {
        //If the user is anonymous or "all users", do nothing.
        if (id == -1 || id == 0) {
            return;
        }
        // Otherwise, select user data from User table and fill in relevant fields.
        String query;
        //We may want to do a username lookup.
        if (username != null) {
            query = LOAD_USER_BY_USERNAME;
        }
        //Otherwise, a lookup by id
        else {
            query = LOAD_USER_BY_ID;
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(query);
            if (username != null) {
                pstmt.setString(1, username);
            }
            else {
                pstmt.setInt(1, id);
            }

            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new UserNotFoundException(
                    "Failed to read user " + id + " from database."
                );
            }
            this.id = rs.getInt("userID");
            this.username = rs.getString("username");
            this.passwordHash = rs.getString("passwordHash");
            this.name = rs.getString("name");
            this.nameVisible = (rs.getInt("nameVisible") == 1);
            this.email = rs.getString("email");
            this.emailVisible = (rs.getInt("emailVisible") == 1);
        }
        catch( SQLException sqle ) {
            throw new UserNotFoundException(
                "Failed to read user " + id + " from database.", sqle
            );
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Inserts a new user record into the database.
     */
    private void insertIntoDb() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(INSERT_USER);
            pstmt.setInt(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, passwordHash);
            pstmt.setString(4, email);
            pstmt.setInt(5, emailVisible?1:0);
            pstmt.setInt(6, nameVisible?1:0);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbUser:insertIntoDb()-" + sqle);
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Save the user data to the database.
     */
    private void saveToDb() {
        if ( id == -1 || id == 0 ) {
            //"anonymous" or "all users", do nothing
            return;
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SAVE_USER);
            pstmt.setString(1, passwordHash);
            pstmt.setString(2, email);
            pstmt.setInt(3, emailVisible?1:0);
            pstmt.setString(4, name);
            pstmt.setInt(5, nameVisible?1:0);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println( "SQLException in DbUser.java:saveToDb(): " + sqle );
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }
}
