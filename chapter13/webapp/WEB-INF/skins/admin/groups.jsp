
<%
/**
 *	$RCSfile: groups.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page 
	import="java.util.*,
            com.Yasna.forum.*,
	        com.Yasna.forum.util.*,
	        com.Yasna.forum.util.admin.*"
	errorPage="error.jsp"
%>

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
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isGroupAdmin && !isSystemAdmin ) {
		throw new UnauthorizedException("Sorry, you don't have permission to administer groups");
	}
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
	<style type="text/css">
	.groupHeader {
		font-size : 8pt;
		text-align : center;
	}
	</style>
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Groups" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>


<%	////////////////////////////////
	// get an Iterator of users
	ProfileManager manager = forumFactory.getProfileManager();
	Iterator groupIterator = manager.groups();
%>

<p>

<%	if( isSystemAdmin ) { %>
	<p>
	(<a href="createGroup.jsp">Create New Group</a>)
	<p>
<%	} %>

<form>
	
<%-- table of users --%>
<table bgcolor="#666666" cellpadding="0" cellspacing="0" border="0" width="100%">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#eeeeee">
	<td class="groupHeader" width="1%" nowrap>ID</td>
	<td class="groupHeader" width="40%" nowrap>Group Name</td>
	<td class="groupHeader" width="55%">Description</td>
	<td class="groupHeader" width="1%" nowrap>Members</td>
	<td class="groupHeader" width="1%" nowrap>Edit</td>
	<td class="groupHeader" width="1%" nowrap>Permissions</td>
	<td class="groupHeader" width="1%" nowrap>Remove</td>
</tr>

<%	//////////////////
	// no groups:
	if( !groupIterator.hasNext() ) {
%>
		<tr bgcolor="#ffffff">
			<td colspan="7" align="center">
			<br>
			<i>No groups, try <a href="createGroup.jsp">adding one</a>.</i>
			<br><br>
			</td>
		</tr>
<%	}
%>

<%	////////////////////////////////////
	// iterate through users, show info
	
	while( groupIterator.hasNext() ) {
		Group group = (Group)groupIterator.next();
		int groupID = group.getID();
		String groupName = group.getName();
		String groupDescription = group.getDescription();
		int numMembers = group.getMemberCount();
%>

<tr bgcolor="#ffffff">
	<td align="center"><%= groupID %></td>
	<td><b><%= groupName %></b></td>
	<td><i><%= (groupDescription!=null&&!groupDescription.equals(""))?groupDescription:"&nbsp;" %></i></td>
	<td align="center"><%= numMembers %></td>
	<td align="center">
		<input type="radio" name="edit"
		 onclick="location.href='editGroup.jsp?group=<%=groupID%>';">
	</td>
	<td align="center">
		<input type="radio" name="perms"
		 onclick="location.href='groupPerms.jsp?group=<%=groupID%>';">
	</td>
	<td align="center">
		<input type="radio" name="remove"
		 onclick="location.href='removeGroup.jsp?group=<%=groupID%>';">
	</td>
</tr>

<%	}
%>
</table>
</td>
</table>

</form>

</body>
</html>

