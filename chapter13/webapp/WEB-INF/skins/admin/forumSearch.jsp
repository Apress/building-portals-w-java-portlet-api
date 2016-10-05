
<%
/**
 *	$RCSfile: forumSearch.jsp,v $
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
		request.setAttribute("message","No permission to search forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%	//////////////////////
	// get parameters
	
	// paging vars:
	String query = ParamUtils.getParameter(request,"q");
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums", "Forum Search" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

Results: <p>


<%	/////////////////////////////
	// do a search
	Iterator forumIterator = forumFactory.forums();
	Hashtable results = new Hashtable();
	while( query!=null && forumIterator.hasNext() ) {
		Forum forum = (Forum)forumIterator.next();
		int forumID = forum.getID();
		String name = forum.getName();
		String description = forum.getDescription();
		if( description == null ) { description = ""; }
		if( name.toLowerCase().indexOf(query.toLowerCase()) > -1
			|| description.toLowerCase().indexOf(query.toLowerCase()) > -1 ) 
		{
			results.put(""+forumID,forum);
		}
	}
	if( results.size() == 0 ) {
%>
		<i>No results</i>
<%
	} else { 
%>

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
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Filters</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Remove</b></td>
	<td class="forumCellHeader" align="center" width="1%" nowrap><b>Content</b></td>
</tr>
	<%	Enumeration resultsEnum = results.elements();
		while( resultsEnum.hasMoreElements() ) {
			Forum forum = (Forum)resultsEnum.nextElement();
			String forumName = forum.getName();
			int forumID = forum.getID();
			
	%>
	
	<tr bgcolor="#ffffff">
		<td class="forumCell" align="center"><b><%= forum.getID() %></b></td>
		<td class="forumCell">
			<b><a href="forumDetail.jsp?forum=<%= forum.getID() %>"
			    title="More details..."><%= forum.getName() %></a></b>
		</td>
		<td class="forumCell"><i><%= forum.getDescription() %></i></td>
		<td align="center" class="forumCell"><%= forum.getThreadCount() %> / <%= forum.getMessageCount() %></td>
		<td align="center">
			<input type="radio" name="edit"
			 onclick="location.href='editForum.jsp?forum=<%= forumID %>'">
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

<%	} %>

</form>

</body>
</html>

