<%
/**
 * Jive Setup Tool
 * November 28, 2000
 */
%>

<%@ page import="java.io.*,
                 java.util.*,
				 java.sql.*,
                 com.Yasna.forum.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.forum.database.*"%>

<html>
<head>
	<title>Jive Setup - Step 3</title>
		<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<img src="images/setup.gif" width="210" height="38" alt="Jive Setup" border="0">
<hr size="0"><p>

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
        boolean driverFound = true;
        try {
               Class.forName(PropertyManager.getProperty("DbConnectionDefaultPool.driver"));
        }
        catch (ClassNotFoundException cnfe) {
             driverFound = false;
        }
        if (!driverFound) {
		System.err.println("Yazd Setup Driver Not Found : "+PropertyManager.getProperty("DbConnectionDefaultPool.driver"));
        %>
        <tr><td valign=top><img src="images/x.gif" width="13" height="13"></td><td>
                <font color="red">
		Driver specified does not seem to be in the classpath.<br>
                Please verify the JDBC driver and you might have to restart the application server.
                </font>
        </td></tr>
        <%
                }
	DatabaseMetaData metaData = null;
	Connection con = null;
	if (!error) {
		try {
			con = DbConnectionManager.getConnection();
			metaData = con.getMetaData();
		}
		catch( Exception e ) { 
			e.printStackTrace();
		}
	}
%>

<%
	if (!error) {
		try {
%>

<b>Database Information</b>

<ul>
	<font size="2">
	Jive successfully connected to your database. Press the "Continue" button to proceed to the next step of
	setup. 
	<p>
	
	<table cellpadding="2" cellspacing="2" border="0">
<tr>
	<td colspan="2"><font size="-1"><b>Database Properties</b></font></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Name:</font></td>
	<td><font size="-1"><%= metaData.getDatabaseProductName() %></font></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Version:</font></td>
	<td><font size="-1"><%= metaData.getDatabaseProductVersion() %></font></td>
</tr>
<tr>
	<td colspan="2"><br><b><font size="-1">JDBC Driver Properties</font></b></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Driver:</font></td>
	<td><font size="-1"><%= metaData.getDriverName() %>, version <%= metaData.getDriverVersion() %></font></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Connection URL:</font></td>
	<td><font size="-1"><%= metaData.getURL() %></font></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Connection username:</font></td>
	<td><font size="-1"><%= metaData.getUserName() %></font></td>
</tr>
<tr>
	<td colspan="2"><br><b><font size="-1">Database Capabilities</font></b></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Supports transactions?</font></td>
	<td><font size="-1"><%= (metaData.supportsTransactions())?"Yes":"No" %></font></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Supports multiple connections <br>open at once?</font></td>
	<td><font size="-1"><%= (metaData.supportsMultipleTransactions())?"Yes":"No" %></font></td>
</tr>
<tr>
	<td bgcolor="#eeeeee"><font size="-1">Is in read-only mode?</font></td>
	<td><font size="-1"><%= (metaData.isReadOnly())?"Yes":"No" %></font></td>
</tr>
</table>

<%			con.close();
		}
		catch( Exception e ) {}
%>
	
	<form action="setup4.jsp" method="post">
	
</ul>

<center>
<input type="submit" value="Continue &gt;">
</center>
</form>

<%	} //end no error
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
<hr size="0">
<center><font size="-1"><i>www.Yasna.com/yazd</i></font></center>
</font>
</body>
</html>
