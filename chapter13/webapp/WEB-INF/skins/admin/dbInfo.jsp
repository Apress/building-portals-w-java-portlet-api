
<%
/**
 *	$RCSfile: dbInfo.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page 
	import="java.sql.*,
            com.Yasna.forum.*,
            com.Yasna.forum.database.*"%>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>

<%	////////////////////////////////
	// Yazd authorization check
	
	// check the bean for the existence of an authorization token.
	// Its existence proves the user is valid. If it's not found, redirect
	// to the login page
	Authorization authToken = adminBean.getAuthToken();
	if( authToken == null ) {
		response.sendRedirect( "login.jsp" );
		return;
	}
%>

<html>
<head>
	<title>DbConnector.jsp</title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%-- header --%>
<table class="pageHeaderBg" cellpadding="1" cellspacing="0" border="0" width="100%">
<td><table class="pageHeaderFg" cellpadding="3" cellspacing="0" border="0" width="100%">
<td>
	<span class="pageHeaderText">
	Database Information
	</span>
</td>
</table></td>
</table>
<%-- /header --%>

<p>

<% 
	boolean error = false;
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
	}
%>

</body>
</html>


