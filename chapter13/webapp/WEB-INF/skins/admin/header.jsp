
<%
/**
 *	$RCSfile: header.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page 
	import="com.Yasna.forum.*,
	        com.Yasna.forum.util.*"
%>

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

<%	////////////////////////////////////////
	// figure out what type of user we are:
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isForumAdmin  = ((Boolean)session.getValue("yazdAdmin.forumAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	boolean isModerator  = ((Boolean)session.getValue("yazdAdmin.Moderator")).booleanValue();
%>

<%	////////////////
	// get parameters
	
	String tab = ParamUtils.getParameter(request,"tab");
	if( tab == null ) {
		tab = "global";
	}
%>

<html>
<head>
	<title>header.jsp</title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/backleft.gif" marginwidth=0 marginheight=0 leftmargin=0 topmargin=0 bgcolor="#ffffff">

<%-- begin header --%>
<table cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<tr height="100%">
	<td height="100%" width="1%" nowrap valign="bottom"
	><img src="images/title.gif" hspace="5" width="123" height="50" alt="Yazd Administration Tool" border="0"
	><br><%
	if ( isSystemAdmin){ %><a href="sidebar.jsp?tree=system" onclick="location.href='header.jsp?tab=global';" target="sidebar"
	><img src="images/tabs_global_<%= (tab.equals("global"))?"on":"off" %>.gif" width="138" height="35" alt="" border="0"
	></a><%
	}
	if (isSystemAdmin || isModerator){ %><a href="sidebar.jsp?tree=forum" onclick="location.href='header.jsp?tab=forums';" target="sidebar"
	><img src="images/tabs_forum_<%= (tab.equals("forums"))?"on":"off" %>.gif" width="138" height="35" alt="" border="0"
	></a><%}%></td><td height="100%" width="98%" valign="bottom"
	><img src="images/tabs_padding.gif" width="100%" height="85" alt="" border="0"></td
	><td height="100%" width="1%" valign="bottom"
	><a href="index.jsp?logout=true"
	><img src="images/logout.gif" width="64" height="85" alt="" border="0" target="_top"></a></td>
</tr>
</table>
<%-- end header --%>

</body>
</html>
