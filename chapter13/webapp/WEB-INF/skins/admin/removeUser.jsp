
<%
/**
 *	$RCSfile: removeUser.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="java.util.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"%>

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

<%	////////////////////
	// get parameters
	
	String username   = ParamUtils.getParameter(request,"user");
	boolean doDelete = ParamUtils.getBooleanParameter(request,"doDelete");
%>
 
<%	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noUserSpecified = (username == null);
	boolean errors = (noUserSpecified);
%>

<%	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();
%>

<%	/////////////////////
	// delete forum if specified
	if( doDelete && !errors ) {
		String message = "";
		try {
			User user = manager.getUser(username);
			manager.deleteUser(user);
			message = "User deleted successfully!";
		}
		catch( UserNotFoundException fnfe ) {
			System.err.println( fnfe );
			message = "No user found";
		}
		catch( UnauthorizedException ue ) {
			System.err.println( ue );
			message = "Not authorized to delete this user";
		}
		request.setAttribute("message",message);
		response.sendRedirect("users.jsp");
		return;
	}
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Users", "Remove User" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	////////////////////
	// display a list of users to remove if no group was specified:
	if( noUserSpecified ) {
		String formAction = "remove";
%>
		<%@ include file="userChooser.jsp"%>
<%		out.flush();
		return;
	}
%>

<%	/////////////////////
	// at this point, we know there is a user to work with:
	User user = null;
	try {
		user = manager.getUser(username);
		isSystemAdmin = (isSystemAdmin && (user.getID()==authToken.getUserID()));
	} catch( UserNotFoundException unfe ) {
	}
%>

Remove <b><%= user.getUsername() %> (<%= user.getName() %>)</b>? 

<p>

<%	if( isSystemAdmin ) { %>
	<span class="errorText">
	Warning! This user is the system user -- deleting this user is NOT recommened.
	</span>
<%	} %>

<p>

<ul>
	Warning: This will permanently delete the user. Are
	you sure you really want to do this? (It will <b>not</b> delete the
	messages posted by this user. User posted messages will be marked
	as "anonymous" after user deletion.)
</ul>

<form action="removeUser.jsp" name="deleteForm">
<input type="hidden" name="doDelete" value="true">
<input type="hidden" name="user" value="<%= username %>">
	<input type="submit" value=" Delete User ">
	&nbsp;
	<input type="submit" name="cancel" value=" Cancel " style="font-weight:bold;"
	 onclick="location.href='users.jsp';return false;">
</form>

<script language="JavaScript" type="text/javascript">
<!--
document.deleteForm.cancel.focus();
//-->
</script>


</body>
</html>
