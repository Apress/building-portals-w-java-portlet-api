
<%
/**
 *	$RCSfile: forumDetail.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page import="java.util.*,
                 java.text.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"%>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>

<%!	///////////////////////
	// page variables
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d 'at' hh:mm:ss a");
%>

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
 
<%	////////////////////
	// Security check
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>
 
<%	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
%>
 
<%	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	boolean errors = (noForumSpecified);
%>

<%	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums : Forum Details" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	/////////////////////
	// at this point, we know there is a forum to work with:
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	} catch( ForumNotFoundException fnfe ) {
	} catch( UnauthorizedException ue ) {
	}
	
	Date creationDate = forum.getCreationDate();
	String description = forum.getDescription();
	ForumMessageFilter[] installedFilters = null;
	try {
		installedFilters = forum.getForumMessageFilters();
	}
	catch( UnauthorizedException ue ) {}
	int messageCount = forum.getMessageCount();
	Date modifiedDate = forum.getModifiedDate();
	String forumName = forum.getName();
	ForumPermissions forumPermissions = forum.getPermissions(authToken);
	Enumeration propertyNames = forum.propertyNames();
	int threadCount = forum.getThreadCount();
	
%>

<font size="+1">
Forum: <%= forumName %>
</font>

<p>

<table bgcolor="#999999" cellspacing="0" cellpadding="0" width="100%" border="0">
<td>
<table bgcolor="#999999" cellspacing="1" cellpadding="3" width="100%" border="0">
<tr bgcolor="#ffffff">
	<td nowrap>Description</td>
	<td><i><%= (description!=null&&!description.equals(""))?description:"&nbsp;" %></i></td>
</tr>
<tr bgcolor="#ffffff">
	<td nowrap>Number of Threads</td>
	<td><%= threadCount %></td>
</tr>
<tr bgcolor="#ffffff">
	<td nowrap>Number of Messages</td>
	<td><%= messageCount %></td>
</tr>
<tr bgcolor="#ffffff">
	<td nowrap>Creation Date</td>
	<td><%= dateFormat.format(creationDate) %></td>
</tr>
<tr bgcolor="#ffffff">
	<td nowrap>Last Modified Date</td>
	<td><%= dateFormat.format(modifiedDate) %></td>
</tr>
</table>
</td>
</table>

<p>

<b>Extended Properties:</b><p>
<%	if( !propertyNames.hasMoreElements() ) { %>
	<ul><i>No extended properties.</i></ul>
<%	} else { %>
	<table bgcolor="#999999" cellspacing="0" cellpadding="0" width="95%" align="right">
	<td>
	<table bgcolor="#999999" cellspacing="1" cellpadding="3" width="100%">
	<%	while( propertyNames.hasMoreElements() ) { %>
	<%		String propertyName = (String)propertyNames.nextElement(); %>
	<%		String propertyValue = forum.getProperty(propertyName); %>
		<tr bgcolor="#ffffff">
		<td><%= propertyName %></td>
		<td><%= propertyValue %></td>
		</tr>
	<%	} %>
	</table>
	</td>
	</table>
	<br clear="all"><br>
<%	} %>

<p>

<b>Installed Filters:</b> (<a href="forumFilters.jsp?forum=<%=forumID%>">filters</a>)<p>
<%	if( installedFilters == null || installedFilters.length == 0 ) { %>
	<ul><i>No filters installed. (<a href="forumFilters.jsp?forum=<%=forumID%>">add some</a>)</i></ul>
<%	} else { %>
	<table bgcolor="#999999" cellspacing="0" cellpadding="0" width="95%" align="right">
	<td>
	<table bgcolor="#999999" cellspacing="1" cellpadding="3" width="100%">
	<%	for( int i=0; i<installedFilters.length; i++ ) { %>
	<%		ForumMessageFilter filter = installedFilters[i]; %>
		<tr bgcolor="#ffffff">
		<td><%= filter.getName() %></td>
		<td><%= filter.getDescription() %></td>
		</tr>
	<%	} %>
	</table>
	</td>
	</table>
	<br clear="all"><br>
<%	} %>

</body>
</html>

