
<%
/**
 *	$RCSfile: createUser.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page import="java.util.*,
                 java.net.URLEncoder,
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
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("No permission to administer users"));
		return;
	}
%>
 
<%	//////////////////////////////////
	// error variables for parameters
	
	boolean errorEmail = false;
	boolean errorUsername = false;
	boolean errorNoPassword = false;
	boolean errorNoConfirmPassword = false;
	boolean errorPasswordsNotEqual = false;
	
	// error variables from user creation
	boolean errorUserAlreadyExists = false;
	boolean errorNoPermissionToCreate = false;
	
	// overall error variable
	boolean errors = false;
	
	// creation success variable:
	boolean success = false;
%>


<%	////////////////////
	// get parameters
	String name             = ParamUtils.getParameter(request,"name");
	String email            = ParamUtils.getParameter(request,"email");
	String username         = ParamUtils.getParameter(request,"username");
	String password         = ParamUtils.getParameter(request,"password");
	String confirmPassword  = ParamUtils.getParameter(request,"confirmPassword");
	boolean usernameIsEmail = ParamUtils.getCheckboxParameter(request,"usernameIsEmail");
	boolean nameVisible     = !ParamUtils.getCheckboxParameter(request,"hideName");
	boolean emailVisible    = !ParamUtils.getCheckboxParameter(request,"hideEmail");
	boolean doCreate        = ParamUtils.getBooleanParameter(request,"doCreate");
%>

<%	///////////////////////////////////////////////////////////////////
	// trim up the passwords so no one can enter a password of spaces
	if( password != null ) {
		password = password.trim();
		if( password.equals("") ) { password = null; }
	}
	if( confirmPassword != null ) {
		confirmPassword = confirmPassword.trim();
		if( confirmPassword.equals("") ) { confirmPassword = null; }
	}
%>

<%	//////////////////////
	// check for errors
	if( doCreate ) {
		if( email == null ) {
			errorEmail = true;
		}
		if( username == null ) {
			errorUsername = true;
		}
		if( password == null ) {
			errorNoPassword = true;
		}
		if( confirmPassword == null ) {
			errorNoConfirmPassword = true;
		}
		if( password != null && confirmPassword != null
		    && !password.equals(confirmPassword) )
		{
			errorPasswordsNotEqual = true;
		}
		errors = errorEmail || errorUsername || errorNoPassword
		         || errorNoConfirmPassword || errorPasswordsNotEqual;
	}
%>

<%	////////////////////////////////////////////////////////////////
	// if there are no errors at this point, start the process of
	// adding the user
	
	ProfileManager profileManager = null;
	if( !errors && doCreate ) {
		// get a profile manager to edit user properties
		profileManager = forumFactory.getProfileManager();
		try {
			User newUser = profileManager.createUser(username,password,email);
			newUser.setName( name );
			newUser.setEmailVisible( emailVisible );
			newUser.setNameVisible( nameVisible );
			success = true;
		}
		catch( UserAlreadyExistsException uaee ) {
			errorUserAlreadyExists = true;
			errorUsername = true;
			errors = true;
		}
		catch( UnauthorizedException ue ) {
			errorNoPermissionToCreate = true;
			errors = true;
		}
	}
%>

<%	//////////////////////////////////////////////////////////////////////
	// if a user was successfully created, say so and return (to stop the 
	// jsp from executing
	if( success ) {
		response.sendRedirect("users.jsp?msg="
			+ URLEncoder.encode("User was created successfully"));
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
	String[] pageTitleInfo = { "Users", "Create User" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	// print error messages
	if( !success && errors ) {
%>
	<p><font color="#ff0000">
	<%	if( errorUserAlreadyExists ) { %>
		The username "<%= username %>" is already taken. Please try 
		another one.
	<%	} else if( errorNoPermissionToCreate ) { %>
		You do not have user creation privileges.
	<%	} else { %>
		An error occured. Please check the following 
		fields and try again.
	<%	} %>
	</font><p>
<%	} %>

<p>

<font size="-1">
This creates a user with no permissions and default privacy settings.
Once you create this user, you should edit their properties.
</font>

<p>

<%-- form --%>
<form action="createUser.jsp" method="post" name="createForm">
<input type="hidden" name="doCreate" value="true">

<b>New User Information</b>
<p>

<table bgcolor="#999999" cellspacing="0" cellpadding="0" border="0" width="95%" align="right">
<td>
<table bgcolor="#999999" cellspacing="1" cellpadding="3" border="0" width="100%">

<%-- name row --%>
<tr bgcolor="#ffffff">
	<td><font size="-1">Name <i>(optional)</i></font></td>
	<td><input type="text" name="name" size="30"
		 value="<%= (name!=null)?name:"" %>">
	</td>	
</tr>

<%-- user email --%>
<tr bgcolor="#ffffff">
	<td><font size="-1"<%= (errorEmail)?(" color=\"#ff0000\""):"" %>>Email</font></td>
	<td><input type="text" name="email" size="30"
		 value="<%= (email!=null)?email:"" %>">
	</td>
</tr>

<%-- username --%>
<tr bgcolor="#ffffff">
	<td><font size="-1"<%= (!usernameIsEmail&&errorUsername)?" color=\"#ff0000\"":"" %>>
		Username
		<br>&nbsp;(<input type="checkbox" name="usernameIsEmail" 
		  id="cb01"<%= (usernameIsEmail)?" checked":"" %>
		  onclick="this.form.username.value=this.form.email.value;"> 
		<label for="cb01">use email</label>)
		</font>
	</td>
	<td><input type="text" name="username" size="30"
		<%	if( usernameIsEmail ) { %>
		 value="<%= (email!=null)?email:"" %>">
		<%	} else { %>
		 value="<%= (username!=null)?username:"" %>">
		<%	} %>
	</td>
</tr>

<%-- password --%>
<tr bgcolor="#ffffff">
	<td><font size="-1"<%= (errorNoPassword||errorPasswordsNotEqual)?" color=\"#ff0000\"":"" %>
		 >Password</font></td>
	<td><input type="password" name="password" value="" size="20" maxlength="30"></td>
</tr>

<%-- confirm password --%>
<tr bgcolor="#ffffff">
	<td><font size="-1"<%= (errorNoConfirmPassword||errorPasswordsNotEqual)?" color=\"#ff0000\"":"" %>
		 >Password (again)</font></td>
	<td><input type="password" name="confirmPassword" value="" size="20" maxlength="30"></td>
</tr>

</table>
</td>
</table>

<br clear="all"><br>

<input type="submit" value="Create User">
&nbsp;
<input type="submit" value="Cancel"
 onclick="location.href='users.jsp';return false;">

</form>

<script language="JavaScript" type="text/javascript">
<!--
document.createForm.name.focus();
//-->
</script>


</body>
</html>

