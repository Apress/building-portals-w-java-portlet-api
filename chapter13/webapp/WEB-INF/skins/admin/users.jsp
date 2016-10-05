
<%
/**
 *	$RCSfile: users.jsp,v $
 *	$Revision: 1.2 $
 *	$Date: 2002/05/12 18:21:50 $
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
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.USER_ADMIN);
	
	// redirect to error page if we're not a user admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer users");
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%	///////////////////
	// get parameters
	
	// parameter for paging through list of users
	int start = ParamUtils.getIntParameter(request,"start",0);
	int range = ParamUtils.getIntParameter(request,"range",DEFAULT_RANGE);
	String msg = ParamUtils.getParameter(request,"msg");
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
	<style type="text/css">
	.userHeader {
		font-size : 8pt;
		text-align : center;
	}
	</style>
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Users" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>


<%	////////////////////////////////
	// get an Iterator of users
	ProfileManager manager = forumFactory.getProfileManager();
	Iterator userIterator = manager.users();
	int userCount = manager.getUserCount();
%>

<p>

<%	if( msg != null ) { %>
	<span class="messageText">
	<%= msg %>
	</span>
<%	} %>

<p>

<form>
	
<%-- paging table --%>
<%	if( (userCount>0) ) { %>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
	<td width="1%" nowrap>
	<%	if( (start-range) >= 0 ) { %>
		&laquo; <a href="users.jsp?range=<%=range%>&start=<%=(start-range)%>">Previous <%= range %></a>
	<%	} else { %>
		&nbsp;
	<%	} %>
	</td>
	<td width="98%" align="center">
		show
		<select size="1"
		 onchange="location.href='users.jsp?start=<%=start%>&range='+this.options[this.selectedIndex].value;">
		<%	for( int i=0; i<ranges.length; i++ ) { %>
		<%		String selected = ""; %>
		<%		if( range == ranges[i] ) { selected = " selected"; } %>
			<option value="<%= ranges[i] %>"<%= selected %>><%= ranges[i] %>
		<%	} %>
		</select>
		users per page
	</td>
	<td width="1%" nowrap>
	<%	if( (start+range) < userCount ) { %>
		<a href="users.jsp?range=<%=range%>&start=<%=(start+range)%>">Next <%= (start+(2*range) < userCount)?range:(userCount % range) %></a> &raquo;
	<%	} else { %>
		&nbsp;
	<%	} %>
	</td>
</tr>
</table>
<%	/////////////////////
	// skip results if necessary
	int skip = start;
	if( skip > 0 ) {
		while( userIterator.hasNext() && (skip-- > 0) ) {
			userIterator.next();
		}
	}
%>
<%-- /paging table --%>

<p>
	
<%-- table of users --%>
<table bgcolor="#666666" border="0" cellpadding="0" cellspacing="0" width="100%">
<td>
<table border="0" cellpadding="2" cellspacing="1" width="100%">
<tr bgcolor="#eeeeee">
	<td class="userHeader" width="2%" nowrap>&nbsp;ID&nbsp;</td>
	<td width="25%"><b>Username</b></td>
	<td width="36%"><b>Name</b></td>
	<td width="25%"><b>Email</b></td>
	<td class="userHeader" width="4%" nowrap>Edit<br>Properties</td>
	<%--<td class="userHeader" width="4%" nowrap>Permissions</td>--%>
	<td class="userHeader" width="4%" nowrap>Remove</td>
</tr>

<%	////////////////////////////////////
	// iterate through users, show info
	
	int count = 0;
	while( userIterator.hasNext() && (count++)<range ) {
		User user = (User)userIterator.next();
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
	</td>
	<td>
		<%= (name!=null&&!name.equals(""))?name:"&nbsp;" %>
	</td>
	<td>
		<%= (email!=null&&!email.equals(""))?email:"&nbsp;" %>
	</td>
	<td align="center">
		<input type="radio" name="props" value=""
		 onclick="location.href='editUser.jsp?user=<%= username %>';">
	</td>
	<%--
	<td align="center">
		<input type="radio" name="props" value=""
		 onclick="location.href='userProps.jsp?user=<%= username %>';">
	</td>
	--%>
	<td align="center">
		<input type="radio" name="props" value=""
		 onclick="location.href='removeUser.jsp?user=<%= username %>';">
	</td>
</tr>

<%	} 
%>

</table>
</td>
</table>

</form>

<p>

<form action="userSearch.jsp">
<center>
	find user:
	<input type="text" name="q" value="" size="30" maxlength="50">
	<input type="submit" value="Find!">
</center>
</form>
<%	} else { %>
<center>
	<b>No users in the database!</b>
</center>
<%	} %>
</body>
</html>


