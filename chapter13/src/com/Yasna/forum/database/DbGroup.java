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
import java.util.ArrayList;
import java.util.Iterator;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.ForumPermissions;
import com.Yasna.forum.Group;
import com.Yasna.forum.GroupNotFoundException;
import com.Yasna.forum.ProfileManager;
import com.Yasna.forum.UnauthorizedException;
import com.Yasna.forum.User;
import com.Yasna.forum.UserNotFoundException;
import com.Yasna.util.CacheSizes;
import com.Yasna.util.Cacheable;

/**
 * Database implementation of the Group interface.
 *
 * @see Group
 */
public class DbGroup implements Group, Cacheable {

    /** DATABASE QUERIES **/
    private static final String ADD_ADMIN =
        "INSERT INTO yazdGroupUser(groupID,userID,administrator) VALUES(?,?,1)";
    private static final String REMOVE_ADMIN =
        "DELETE FROM yazdGroupUser WHERE groupID=? AND userID=? AND administrator=1";
    private static final String ADD_USER =
        "INSERT INTO yazdGroupUser(groupID,userID,administrator) VALUES(?,?,0)";
    private static final String REMOVE_USER =
        "DELETE FROM yazdGroupUser WHERE groupID=? AND userID=? AND administrator=0";
    private static final String ADMIN_TEST =
        "SELECT userID FROM yazdGroupUser WHERE groupID=? AND userID=? AND " +
        "administrator=1";
    private static final String MEMBER_TEST =
        "SELECT userID FROM yazdGroupUser WHERE groupID=? AND userID=?";
    private static final String ADMIN_COUNT =
        "SELECT count(*) FROM yazdGroupUser WHERE groupID=? " +
        "AND administrator=1";
    private static final String MEMBER_COUNT =
        "SELECT DISTINCT count(userID) FROM yazdGroupUser " +
        "WHERE groupID=?";
    private static final String LOAD_ADMINS =
        "SELECT userID FROM yazdGroupUser WHERE administrator=1 AND groupID=?";
    private static final String LOAD_USERS =
        "SELECT userID FROM yazdGroupUser WHERE groupID=?";
    private static final String LOAD_GROUP_BY_ID =
        "SELECT * FROM yazdGroup WHERE groupID=?";
    private static final String LOAD_GROUP_BY_NAME =
        "SELECT * FROM yazdGroup WHERE name=?";
    private static final String INSERT_GROUP =
        "INSERT INTO yazdGroup(name,description,groupID) VALUES(?,?,?)";
    private static final String SAVE_GROUP =
        "UPDATE yazdGroup SET name=?, description=? WHERE groupID=?";

    private int id;
    private String name = null;
    private String description = "";

    private ProfileManager profileManager;
    private DbForumFactory factory;

    /**
     * Creates a new group.
     *
     * @param the name of the group.
     * @param profileManager a ProfileManager that can be used to perform user
     *    and group operations.
     */
    protected DbGroup(String name, DbForumFactory factory) {
        this.name = name;
        this.factory = factory;
        this.profileManager = factory.getProfileManager();
        this.id = DbSequenceManager.nextID("Group");
        insertIntoDb();
    }

    /**
     * Loads a group from the database based on its id.
     *
     * @param id the id of the group to load.
     * @param profileManager a ProfileManager that can be used to perform user
     *    and group operations.
     */
    protected DbGroup(int id, DbForumFactory factory)
            throws GroupNotFoundException
    {
        this.id = id;
        this.factory = factory;
        this.profileManager = factory.getProfileManager();
        loadFromDb();
    }

    /**
     * Loads a group from the database based on its name. The implementation
     * of this method is rather hackish since it includes a fake parameter just
     * so that it can have a different method signature than the first
     * constructor. Even so, this methodology makes this class behave more like
     * our other classes, so we're gleefully leaving it this way. :)
     *
     * @param name the name of the group to load.
     * @param fake a fake paramater that can always be null.
     * @param profileManager a ProfileManager that can be used to perform user
     *    and group operations.
     */
    protected DbGroup(String name, Object fake, DbForumFactory factory)
            throws GroupNotFoundException
    {
        this.name = name;
        this.factory = factory;
        this.profileManager = factory.getProfileManager();
        loadFromDb();
    }

    //FROM THE USER INTERFACE//

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws UnauthorizedException {
        this.name = name;
        saveToDb();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description)
            throws UnauthorizedException
    {
        this.description = description;
        saveToDb();
    }

    public void addAdministrator(User user) throws UnauthorizedException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADD_ADMIN);
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getID());
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Now, remove the user from the USER_PERM_CACHE since being in the
        //group could affect their permissions.
        DbCacheManager cacheManager = factory.getCacheManager();
        cacheManager.removeUserPerm(new Integer(user.getID()));
    }

    public void removeAdministrator(User user) throws UnauthorizedException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(REMOVE_ADMIN);
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getID());
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Now, remove the user from the USER_PERM_CACHE since being in the
        //group could affect their permissions.
        DbCacheManager cacheManager = factory.getCacheManager();
        cacheManager.removeUserPerm(new Integer(user.getID()));
    }

    public void addMember(User user) throws UnauthorizedException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADD_USER);
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getID());
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Now, remove the user from the USER_PERM_CACHE since being in the
        //group could affect their permissions.
        DbCacheManager cacheManager = factory.getCacheManager();
        cacheManager.removeUserPerm(new Integer(user.getID()));
    }

    public void removeMember(User user) throws UnauthorizedException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(REMOVE_USER);
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getID());
            pstmt.execute();
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }

        //Now, remove the user from the USER_PERM_CACHE since being in the
        //group could affect their permissions.
        DbCacheManager cacheManager = factory.getCacheManager();
        cacheManager.removeUserPerm(new Integer(user.getID()));
    }

    public boolean isAdministrator(User user) {
        boolean answer = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADMIN_TEST);
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                answer = true;
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return answer;
    }

    public boolean isMember(User user) {
        boolean answer = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(MEMBER_TEST);
            pstmt.setInt(1, id);
            pstmt.setInt(2, user.getID());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                answer = true;
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return answer;
    }

    public int getAdministratorCount() {
        int count = 0;
        boolean answer = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(ADMIN_COUNT);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return count;
    }

    public int getMemberCount() {
        int count = 0;
        boolean answer = false;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(MEMBER_COUNT);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        catch( SQLException sqle ) {
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return count;
    }

    public Iterator members() {
        ArrayList admins = new ArrayList();
        //Load list of group admins from db.
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(LOAD_USERS);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            User user = null;
            while (rs.next()) {
                try {
                    user = profileManager.getUser(rs.getInt("userID"));
                }
                catch (UserNotFoundException unfe) {
                    unfe.printStackTrace(System.out);
                }
                admins.add(user);
            }
        }
        catch( SQLException sqle ) {
            System.err.println( "SQLException in DbGroup.java:" +
            "users():reading group data " + sqle );
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return admins.iterator();
    }

    public Iterator administrators() {
        ArrayList admins = new ArrayList();
        //Load list of group admins from db.
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(LOAD_ADMINS);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            User user = null;
            while (rs.next()) {
                try {
                    user = profileManager.getUser(rs.getInt("userID"));
                }
                catch (UserNotFoundException unfe) {
                    unfe.printStackTrace(System.out);
                }
                admins.add(user);
            }
        }
        catch( SQLException sqle ) {
            System.err.println( "SQLException in DbGroup.java:" +
            "administrators():reading group data " + sqle );
            sqle.printStackTrace();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
        return admins.iterator();
    }

    public ForumPermissions getPermissions(Authorization authorization) {
        int userID = authorization.getUserID();
        try {
            User user = profileManager.getUser(userID);
            if (isAdministrator(user)) {
                return new ForumPermissions(false, false, false, false,
                        true, false, false, false);
            }
        }
        catch (Exception e) { }

        return ForumPermissions.none();
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
        size += CacheSizes.sizeOfString(name);          //name
        size += CacheSizes.sizeOfString(description);   //description
        size += CacheSizes.sizeOfObject();              //profile manager ref.
        size += CacheSizes.sizeOfObject();              //forum factory ref.

        return size;
    }

    //OTHER METHODS

    /**
     * Returns a String representation of the Group object using the group name.
     *
     * @return a String representation of the Group object.
     */
    public String toString() {
        return name;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && object instanceof DbGroup) {
            return id == ((DbGroup)object).getID();
        }
        else {
            return false;
        }
    }

    /**
     * Load the group data from the database.
     */
    private synchronized void loadFromDb() throws GroupNotFoundException {
        String query;
        if (name == null) {
            query = LOAD_GROUP_BY_ID;
        }
        else {
            query = LOAD_GROUP_BY_NAME;
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(query);
            if (name == null) {
                pstmt.setInt(1, id);
            }
            else {
                pstmt.setString(1, name);
            }
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new GroupNotFoundException();
            }
            this.id = rs.getInt("groupID");
            this.name = rs.getString("name");
            this.description = rs.getString("description");
        }
        catch( SQLException sqle ) {
            System.err.println( "SQLException in DbGroup.java:" +
            "loadFromDb():reading group data " + sqle );
            throw new GroupNotFoundException();
        }
        finally {
            try {  pstmt.close(); }
            catch (Exception e) { e.printStackTrace(); }
            try {  con.close();   }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * Inserts a new record into the database.
     */
    private void insertIntoDb() {
        StringBuffer insert = new StringBuffer();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(INSERT_GROUP);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println("Error in DbGroup:insertIntoDb()-" + sqle);
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
     * Saves group data to the db.
     */
    private synchronized void saveToDb() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement(SAVE_GROUP);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        }
        catch( SQLException sqle ) {
            System.err.println( "SQLException in DbGroup.java:saveToDb(): " + sqle );
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

