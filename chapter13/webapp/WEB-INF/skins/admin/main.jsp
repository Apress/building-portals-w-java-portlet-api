
<%
/**
 *	$RCSfile: main.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="java.util.*,
                 com.Yasna.forum.*" %>

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
	<title>main.jsp</title>
</head>

<body background="images/shadowBack.gif" bgcolor="#FFFFFF" text="#000000" link="#0000FF" vlink="#800080" alink="#FF0000">

<p>

<font face="verdana,arial,helvetica,sans-serif">
<b>Welcome to Yazd Admin</b>
</font>

<font face="verdana,arial,helvetica,sans-serif" size="-1">

<p>

You are running Yazd version <b><%= PropertyManager.getYazdVersion() %></b>
(<a href="http://www.Yasna.com/yazd/download.jsp" target="_blank">check for new version</a>)

<p>

<i>Please send feedback to
	<a href="mailto:aaflatooni@Yasna.com">aaflatooni@Yasna.com</a> about
	this tool.
</i>

</ul>
</font>

</body>
</html>
