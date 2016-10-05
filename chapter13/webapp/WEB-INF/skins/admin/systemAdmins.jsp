
<%
/**
 *	$RCSfile: systemAdmins.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page 
	import="java.util.*,
            java.net.URLEncoder,
            com.Yasna.forum.*,
            com.Yasna.forum.util.*,
	        com.Yasna.forum.util.admin.*"
	errorPage="error.jsp"
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
 
<%	////////////////////
	// Security check
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	
	// redirect to error page if we're not a user admin or a system admin
	if( !isSystemAdmin ) {
		throw new UnauthorizedException("You don't have permission to work with system administrators.");
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
	
	// get a profile manager to edit user properties
	ProfileManager manager = forumFactory.getProfileManager();
	
	if( !errors && doCreate ) {
		try {
			User newUser = manager.createUser(username,password,email);
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
	String[] pageTitleInfo = { "Users", "System Administrators" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

Current System Administrators:
<p>

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
<%	Iterator userIterator = manager.users(); %>
<%	if( !userIterator.hasNext() ) { %>
	<tr bgcolor="#ffffff">
		<td colspan="5">
		<i>No system administrators.</i>
		</td>
	</tr>
<%	} %>
<%	while( userIterator.hasNext() ) { %>
<%		User user = (User)userIterator.next();
		//Authorization userAuth = authFactory.getAuthorization(                       // XXX fix this!
		boolean sysAdmin = user.hasPermission(ForumPermissions.SYSTEM_ADMIN);
		if( sysAdmin ) {
			int userID = user.getID();
			username = user.getUsername();
			name = user.getName();
			email = user.getEmail();
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
<%		} %>
<%	} %>
</tr>
</table>
</td>
</table>

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
This creates a system administrator.
</font>

<p>

<%-- form --%>
<form action="createUser.jsp" method="post" name="createForm">
<input type="hidden" name="doCreate" value="true">

<b>New System Administrator Properties</b>
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

<input type="submit" value="Create Administrator">
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

