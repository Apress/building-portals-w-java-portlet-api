
<%
/**
 *	$RCSfile: forums.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="java.util.*,
                 com.Yasna.forum.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*" %>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>

<%!	///////////////////////////
	// page variables
	private final int DEFAULT_RANGE = 10;
	private final int[] ranges = {10,20,30,50};
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
	
	// make sure the user is authorized to create forums::
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to create forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%	//////////////////////
	// get parameters
	
	// paging vars:
	int start = ParamUtils.getIntParameter(request,"start",0);
	int range = ParamUtils.getIntParameter(request,"range",DEFAULT_RANGE);
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	///////////////////////////////
	// print out message, if any
	String message = (String)request.getAttribute("message");
	if( message != null ) {
%>
	<span class="messageText">
	<%= message %>
	</span>
<%	}
%>

<p>

<%	//////////////////////
	// forum iterator, forum count
	
	Iterator forumIterator = forumFactory.forums();
	int forumCount = forumFactory.getForumCount();
%>

<%-- paging table --%>
<%	if( (forumCount>0) ) { %>
<form>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
	<td width="1%" nowrap>
	<%	if( (start-range) >= 0 ) { %>
		&laquo; <a href="forums.jsp?range=<%=range%>&start=<%=(start-range)%>">Previous <%= range %></a>
	<%	} else { %>
		&nbsp;
	<%	} %>
	</td>
	<td width="98%" align="center">
		show
		<select size="1"
		 onchange="location.href='forums.jsp?start=<%=start%>&range='+this.options[this.selectedIndex].value;">
		<%	for( int i=0; i<ranges.length; i++ ) { %>
		<%		String selected = ""; %>
		<%		if( range == ranges[i] ) { selected = " selected"; } %>
			<option value="<%= ranges[i] %>"<%= selected %>><%= ranges[i] %>
		<%	} %>
		</select>
		forums per page
	</td>
	<td width="1%" nowrap>
	<%	if( (start+range) < forumCount ) { %>
		<a href="forums.jsp?range=<%=range%>&start=<%=(start+range)%>">Next <%= ((start+range-forumCount)<range)?(forumCount-range):range %></a> &raquo;
	<%	} else { %>
		&nbsp;
	<%	} %>
	</td>
</tr>
</table>
<%	} %>
<%	/////////////////////
	// skip results if necessary
	int skip = start;
	while( start-- > 0 ) {
		forumIterator.next();
	}
%>
</form>
<%-- /paging table --%>

<p>

<form>

<table bgcolor="#999999" cellpadding="0" cellspacing="0" border="0" width="100%">
<td>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#eeeeee">
	<td class="forumCellHeader" width="1%" nowrap>
		<b>ID</b>
	</td>
	<td class="forumCellHeader" width="1%" nowrap>
		<b>Forum Name</b>
	</td>
	<td class="forumCellHeader" width="93%"><b>Description</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Threads /<br>Messages</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Properties</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Permissions</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Filters</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Remove</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Content</b></td>
</tr>
<%	int count = 0;
	if( !forumIterator.hasNext() ) {
%>
		<tr bgcolor="#ffffff">
		<td colspan="9" align="center" class="forumCell"><br><i>No forums.<br>Try <a href="createForum.jsp">creating one</a>.</i><br><br></td>
		</tr>
<%
	}
	while( forumIterator.hasNext() && (count++)<range ) {
		Forum forum = (Forum)forumIterator.next();
		int forumID = forum.getID();
		String description = forum.getDescription();
	%>

	<tr bgcolor="#ffffff">
		<td class="forumCell" align="center"><b><%= forum.getID() %></b></td>
		<td class="forumCell">
			<b><a href="forumDetail.jsp?forum=<%= forum.getID() %>"
			    title="More details..."><%= forum.getName() %></a></b>
		</td>
		<td class="forumCell"><i><%= (description!=null&&!description.equals(""))?description:"" %></i></td>
		<td align="center" class="forumCell"><%= forum.getThreadCount() %> / <%= forum.getMessageCount() %></td>
		<td align="center">
			<input type="radio" name="edit"
			 onclick="location.href='editForum.jsp?forum=<%= forumID %>'">
		</td>
		<td align="center">
			<input type="radio" name="perms"
			 onclick="location.href='forumPerms.jsp?forum=<%= forumID %>'">
		</td>
		<td align="center">
			<input type="radio" name="filters"
			 onclick="location.href='forumFilters.jsp?forum=<%= forumID %>';">
		</td>
		<td align="center">
			<input type="radio" name="remove"
			 onclick="location.href='removeForum.jsp?forum=<%= forumID %>';">
		</td>
		<td align="center">
			<input type="radio" name="content"
			 onclick="location.href='forumContent.jsp?forum=<%= forumID %>';">
		</td>
	</tr>

<%	}
%>

</table>
</td>
</table>

</form>

<p>

<form action="forumSearch.jsp">
<center>
	find forum:
	<input type="text" name="q" value="" size="30" maxlength="50">
	<input type="submit" value="Find!">
</center>
</form>

</body>
</html>

