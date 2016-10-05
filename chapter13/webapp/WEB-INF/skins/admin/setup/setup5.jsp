<%
/**
 * Yazd Setup Tool
 * November 28, 2000
 */
%>

<%@ page import="java.io.*,
                 java.util.*,
				 java.sql.*,
				 java.security.*,
                 com.Yasna.forum.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.forum.database.*,
				 com.Yasna.util.*"
%>

<%!	
	private String[] getPropertyNames( DbConnectionProvider conProvider ) {
		Enumeration enum = conProvider.propertyNames();
		Vector v = new Vector();
		while( enum.hasMoreElements() ) {
			v.addElement( enum.nextElement() );
		}
		String[] propNames = new String[ v.size() ];
		v.copyInto(propNames);
		return propNames;
	}
%>
<%
	//DbConnectionProvider conProvider = DbConnectionManager.getDbConnectionProvider();
%>

<% 
	boolean error = false;
	String errorMessage = null;
	//Make sure the install has not already been completed.
	String setup = PropertyManager.getProperty("setup");
	if( setup != null && setup.equals("true") ) {
		error = true;
		errorMessage = "Yazd setup appears to have already been completed. If you'd like " +
			"to re-run this tool, delete the 'setup=true' property from your yazd.properties " +
			"file.";
	}
%>

<%	// get parameters
	boolean createAdmin = ParamUtils.getBooleanParameter(request,"createAdmin");
	String username = ParamUtils.getParameter(request,"username");
	String password = ParamUtils.getParameter(request,"password");
	if (password == null) {
		password = "";
	}
	String confirmPassword = ParamUtils.getParameter(request,"confirmPassword");
	if (confirmPassword == null) {
		confirmPassword = "";
	}
	String name = ParamUtils.getParameter(request,"name");
	String email = ParamUtils.getParameter(request,"email");
	
	if( name == null ) { name = ""; }
	
	if( createAdmin ) {
		if (! password.equals(confirmPassword)) {
			error = true;
			errorMessage = "Passwords did not match. Please press the back button and retype them carefully.";
		}
		else {
			Connection con = null;
			Statement stmt = null;
			PreparedStatement pstmt = null;
		
			try {
				String sql = "insert into yazdUser(userID,username,passwordHash,name,email,emailVisible,nameVisible) "
					+ " values(?,?,?,?,?,?,?)";
				
            	password = StringUtils.hash(password);
						
				con = DbConnectionManager.getConnection();
				if( con == null ) {
					error = true;
					errorMessage = "Could not access the database. Make sure that you completed the database " +
						"setup step correctly.";
				}
				else {
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1,1);
					pstmt.setString(2,username);
					pstmt.setString(3,password);
					pstmt.setString(4,name);
					pstmt.setString(5,email);
					pstmt.setInt(6,1);
					pstmt.setInt(7,1);
					pstmt.executeUpdate();
					pstmt.close();
			
					stmt = con.createStatement();
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,0)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,1)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,2)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,3)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,4)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,5)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,6)");
					stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,1,7)");
					//stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,-1,0)");
					//stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,-1,6)");
					//stmt.executeUpdate("insert into yazdUserPerm(forumID,userID,permission) values(-1,-1,7)");
					stmt.close();
				}
				if( !error ) {
					out.println( "ok");
					response.sendRedirect("setup6.jsp");
					return;
				}
			}
			catch( SQLException sqle ) {
				System.err.println( sqle );
				sqle.printStackTrace();
				error = true;
				errorMessage = "Error creating an administrator in the database. There are a few " +
					"possible reasons for this error: <ul> <li>Yazd is unable to establish a connection " +
					"with your database. <li> Your forgot to install the Yazd database schema. <li> " +
					"You already created an administrator account during a previous use of the " +
					"setup tool. </ul> <p>" +
					"You can examine your log files to see if more information about " +
					"this error is available there. Please fix the problem, press the back button, " +
					"and then try to create the administrator again."; 
			}
			finally {
				try {
					con.close();
				} catch (Exception e) { }
			}
		}
	}
%>

<html>
<head>
	<title>Yazd Setup - Step 5</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Yazd Setup" border="0">
<hr size="0"><p>

<%
	if (!error) {
%>

<b>Create an Administrator Account</b>

<ul>
	<font size="-1">
	An administrator account will allow you to administer your copy of Yazd. Be sure
	to remember your password! If you forget it, you'll have to manually reset it.
	
	<form action="setup5.jsp" mode="post">
	<input type="hidden" name="createAdmin" value="true">
	
	<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<td><font size="-1">your name</font></td>
		<td><input type="text" size="30" name="name" value=""></td>
	</tr>
	<tr>
		<td><font size="-1">username</font></td>
		<td><input type="text" size="30" name="username" value=""></td>
	</tr>
	<tr>
		<td><font size="-1">email address</font></td>
		<td><input type="text" size="30" name="email" value=""></td>
	</tr>
	<tr>
		<td><font size="-1">password</font></td>
		<td><input type="password" size="30" name="password" value=""></td>
	</tr>
	<tr>
		<td><font size="-1">password</font><br><font size="-1"><i>(confirm)</i></font></td>
		<td><input type="password" size="30" name="confirmPassword" value=""></td>
	</tr>
	</table>

</ul>

<center>
<input type="submit" value="Create Administrator">
</center>

<%
	} //end no error
	else {
%>
	<font color="Red">Error!</font>
	<p><font size=2>
	
	<%= errorMessage %>
	
	</font>
<%
	} //end error
%>

<p>
If you have already created an administrator account, you can <a href="setup6.jsp">
skip this step</a>.

<p><hr size="0">
<center><font size="-1"><i>www.Yasna.com/yazd</i></font></center>
</font>
</body>
</html>


