
<%
/**
 *	$RCSfile: userSearch.jsp,v $
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
 
<%!	//////////////////
	// page variables
	private final String YES = "<font color='#006600' size='-1'><b>Yes</b></font>";
	private final String NO  = "<font color='#ff0000' size='-1'><b>No</b></font>";
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
	boolean isUserAdmin   = permissions.get(ForumPermissions.USER_ADMIN);
	
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
	<style type="text/css">
	.userHeader {
		font-size : 8pt;
		text-align : center;
	}
	</style>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Users", "User Search" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

Results: <p>


<%	/////////////////////////////
	// do a search
	ProfileManager manager = forumFactory.getProfileManager();
	Iterator userIterator = manager.users();
	Hashtable results = new Hashtable();
	while( query!=null && userIterator.hasNext() ) {
		User user = (User)userIterator.next();
		int userID = user.getID();
		String name = user.getName();
		String username = user.getUsername();
		String email = user.getEmail();
		if( name.toLowerCase().indexOf(query.toLowerCase()) > -1 
			|| username.toLowerCase().indexOf(query.toLowerCase()) > -1
			|| email.toLowerCase().indexOf(query.toLowerCase()) > -1 ) 
		{
			results.put(""+userID, user);
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

<table bgcolor="#666666" border="0" cellpadding="0" cellspacing="0" width="100%">
<td>
<table border="0" cellpadding="2" cellspacing="1" width="100%">
<tr bgcolor="#eeeeee">
	<td class="userHeader" width="1%" nowrap>&nbsp;ID&nbsp;</td>
	<td width="59%"><b>Username</b><br>Name<br>Email</td>
	<td class="userHeader" width="20%" nowrap>Edit<br>Properties/<br>Permissions</td>
	<td class="userHeader" width="20%" nowrap>Remove</td>
</tr>
	<%	Enumeration resultsEnum = results.elements();
		while( resultsEnum.hasMoreElements() ) {
			User user = (User)resultsEnum.nextElement();
			int userID = user.getID();
			String username = user.getUsername();
			String name = user.getName();
			String email = user.getEmail();
			boolean isNameVisible = user.isNameVisible();
			boolean isEmailVisible = user.isEmailVisible();
	%>
	
	<tr bgcolor="#ffffff">
		<td align="center"><%= userID %></td>
		<td>
			<b><%= username %></b>
			<br>
			<%= (name!=null)?name:"<i>no name</i>" %>
			<br>
			<span style="font-size:8pt"><%= email %></span>
		</td>
		<td align="center">
			<input type="radio" name="props" value=""
			 onclick="location.href='editUser.jsp?user=<%= username %>';">
		</td>
		<td align="center">
			<input type="radio" name="props" value=""
			 onclick="location.href='removeUser.jsp?username=<%= username %>';">
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

