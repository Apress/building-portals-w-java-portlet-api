
<%
/**
 *	$RCSfile: toolbar.jsp,v $
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
	<title>toolbar.jsp</title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body marginwidth=0 marginheight=0 leftmargin=0 topmargin=0
      bgcolor="#dddddd" text="#000000" link="#0000ff" vlink="#0000ff" alink="#ff0000">

<table class="toolbarBg" cellpadding="0" cellspacing="0" border="0" height="100%">
	<td width="99%">
		<font face="verdana,arial,helvetica" size="-1">
		&nbsp;

		<%-- "Global" tab --%>
		<%	if( isSystemAdmin || isGroupAdmin ) { %>
			<a href="sidebar.jsp?tree=system" 
			   target="sidebar" class="toolbarLink"
			   title="Goto the Global menu"
			   onclick="location.href='toolbar.jsp?tab=global';"
			><%= (tab.equals("global"))?"<b>Global</b>":"Global" %></a>
		<%	} %>
		
		&nbsp;
		
		<%-- "Forums" tab --%>
		<%	if( isSystemAdmin || isForumAdmin ) { %>
			<a href="sidebar.jsp?tree=forum"
			   target="sidebar" class="toolbarLink"
			   title="Goto the Forums menu"
			   onclick="location.href='toolbar.jsp?tab=forums';"
			><%= (tab.equals("forums"))?"<b>Forums</b>":"Forums" %></a>
		<%	} %>
		
		</font>
	</td>
	<td width="1%" nowrap>
			
		<%-- logout link --%>
		<%	// get the username %>
		<%	try { %>
		<%		ForumFactory forumFactory = ForumFactory.getInstance(authToken); %>
		<%		ProfileManager manager = forumFactory.getProfileManager(); %>
		<%		User user = manager.getUser(authToken.getUserID()); %>
			Logged in as: <b><%= user.getUsername() %></b>
		<%	} catch( Exception ignored ) {} %>
		&nbsp;
		<a href="index.jsp?logout=true" 
		 target="_top" class="toolbarLink"
		 title="Logout of the Yazd Admin tool"
		><b>Logout</b></a>
		
		&nbsp;
	</td>
</table>

</body>
</html>
