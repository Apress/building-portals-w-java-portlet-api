
<%
/**
 *	$RCSfile: propManager.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page 
	import="java.sql.*,
	        java.util.*,
            com.Yasna.forum.*,
			com.Yasna.forum.util.*,
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

<%	///////////////////////
	// get parameters
	
	String propAction = ParamUtils.getParameter(request,"propAction");
	String propName = ParamUtils.getParameter(request,"propName");
	String propValue = ParamUtils.getParameter(request,"propValue",true);
%>

<%	//////////////////
	// perform action
	
	boolean doSomething = (propAction != null);
	
	// add a property
	if( doSomething && propAction.equals("Add") ) {
		if( propName != null && propValue != null ) {
			// add if prop doesn't already exist
			if( PropertyManager.getProperty(propName) == null ) {
				PropertyManager.setProperty(propName,propValue);
			}
		}
	}
	
	// update a property
	else if( doSomething && propAction.equals("Update") ) {
		if( propValue != null ) {
			PropertyManager.setProperty(propName,propValue);
		}
	}
	
	// delete a property
	else if( doSomething && propAction.equals("Delete") ) {
		if( propName != null ) {
			PropertyManager.deleteProperty(propName);
		}
	}
%>

<html>
<head>
	<title>Property Manager</title>
	<link rel="stylesheet" href="style/global.css">
	<style type="text/css">
	INPUT {
		font-family : courier new;
		font-size : 9pt;
	}
	</style>
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%-- header --%>
<table class="pageHeaderBg" cellpadding="1" cellspacing="0" border="0" width="100%">
<td><table class="pageHeaderFg" cellpadding="3" cellspacing="0" border="0" width="100%">
<td>
	<span class="pageHeaderText">
	Property Manager
	</span>
</td>
</table></td>
</table>
<%-- /header --%>

<p>

These are the global Yazd properties.

<p>

<table bgcolor="#333333" cellpadding="0" cellspacing="0" border="0" width="100%">
<td>
<table bgcolor="#333333" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#dddddd">
	<td>
	Property Name
	</td>
	<td>
	Property Value
	</td>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
</tr>
<%	/////////////////////////
	// display all properties
	
	Enumeration propertyNames = PropertyManager.propertyNames();
	while( propertyNames.hasMoreElements() ) {
		String name = (String)propertyNames.nextElement();
		String value = PropertyManager.getProperty(name);
%>
<tr bgcolor="#ffffff">
<form action="propManager.jsp">
<input type="hidden" name="propName" value="<%= name %>">
	<td>
	<span style="font-size:8pt">
	<%= name %>
	</span>
	</td>
	<td>
	<input type="text" value="<%= value %>" name="propValue" size="40">
	</td>
	<td align="center">
	<input type="submit" name="propAction" value="Update">
	</td>
	<td align="center">
	<input type="submit" name="propAction" value="Delete"
	 onclick="return confirm('Are you sure you want to delete this property?')">
	</td>
</form>
</tr>
<%	}
%>
</table>
</td>
</table>

<p>

<b>Add a new property</b>

<form action="propManager.jsp">

<table bgcolor="#333333" cellpadding="0" cellspacing="0" border="0" width="100%">
<td>
<table bgcolor="#333333" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#ffffff">
	<td>
	Name:
	</td>
	<td>
	<input type="text" name="propName" value="">
	</td>
</tr>
<tr bgcolor="#ffffff">
	<td>
	Value:
	</td>
	<td>
	<input type="text" name="propValue" value="">
	</td>
</tr>
<tr bgcolor="#ffffff">
	<td colspan="2" align="center">
	<input type="submit" name="propAction" value="Add">
	</td>
</tr>
</table>
</td>
</table>

</form>

<br>

</body>
</html>


