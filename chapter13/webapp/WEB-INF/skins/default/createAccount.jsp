
<%
/**
 *	$RCSfile: createAccount.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page import="com.Yasna.forum.*,
                 com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	///////////////////
	// global variables
	
	private final static String errorFieldColor = "#D5E9FD";
%>

<%	////////////////////////
	// Authorization check
	
	// check for the existence of an authorization token
	Authorization authToken = SkinUtils.getUserAuthorization(request,response);
	
	// if the token was null, they're not authorized. Since this skin will
	// allow guests to view forums, we'll set a "guest" authentication
	// token. This way, either registered users or guests can create a new account.
	if( authToken == null ) {
		authToken = AuthorizationFactory.getAnonymousAuthorization();
	}
%>

<%	///////////////////////
	// page forum variables
	
	// do not delete these
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	User user = forumFactory.getProfileManager().getUser(authToken.getUserID());
%>

<%	//////////////////
	// get parameters
	
	boolean doCreate = ParamUtils.getBooleanParameter(request,"doCreate");
	String newUsername = ParamUtils.getParameter(request,"username");
	String newPassword = ParamUtils.getParameter(request,"password");
	String confirmPassword = ParamUtils.getParameter(request,"confirmPassword");
	String newEmail = ParamUtils.getParameter(request,"email");
	String newName = ParamUtils.getParameter(request,"name");
	boolean autoLogin = ParamUtils.getCheckboxParameter(request,"autoLogin");
%>

<%	//////////////////
	// global error vars
	
	String errorMessage = "";
	
	boolean errorUsername         = (newUsername == null);
	boolean errorPassword         = (newPassword == null);
	boolean errorConfirmPassword  = (confirmPassword == null);
	boolean errorEmail            = (newEmail == null);
	boolean errorName             = (newName == null);
	boolean errorNoMatchPasswords = true;
	if( !errorPassword && !errorConfirmPassword ) {
		errorNoMatchPasswords = !newPassword.equals(confirmPassword);
	}
	boolean errors = (errorUsername || errorPassword || errorConfirmPassword
						|| errorEmail || errorName || errorNoMatchPasswords );
%>

<%	//////////////////
	// error message
	if( errors ) {
		errorMessage = "Oops! There were errors in the form. Please check each field.";
	}
%>

<%	//////////////////////////
	// create user, if no errors
	if( !errors ) { 
		try {
			User newUser = forumFactory.getProfileManager()
			                           .createUser(newUsername,newPassword,newEmail);
			newUser.setName( newName );
			// getting to this point means the account was created successfully.
			// We store a success message so the main page can display it.
			SkinUtils.store(request,response,"message",
				"Account created successfully. You are logged in as " + newUsername + ".");
			// set this new user's authorization token
			SkinUtils.setUserAuthorization(request,response,newUsername,newPassword,autoLogin);
			// redirect to main page
			response.sendRedirect("index.jsp");
			return;
		}
		catch( UserAlreadyExistsException uaee ) {
			errorMessage = "Sorry, the username \"" + newUsername + "\" already "
				+ "exists. Try a different username.";
			errorUsername = true;
			errors = true;
		}
	}
%>


<%	//////////////////////
	// Header file include
	
	// The header file looks for the variable "title"
	String title = "Yazd Forums: Example Skin";
%>
<%@ include file="header.jsp" %>

<%	////////////////////
	// Breadcrumb bar
	
	// The breadcrumb file looks for the variable "breadcrumbs" which
	// represents a navigational path, ie "Home > My Forum > Hello World"
	String[][] breadcrumbs = {
		{ "Home", "index.jsp" },
		{ "Create User Account", "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>

<h2>Yazd Forum Account Creation</h2>

<%	//////////////////
	// print error message if there is one
	if( doCreate && errors ) {
%>
	<h4><i><%= errorMessage %></i></h4>
<%	}
%>

<p>

<center>
	<i>All fields are required!</i>
</center>

<p>

<form action="createAccount.jsp" method="post" name="userForm">
<input type="hidden" name="doCreate" value="true">

	<table cellpadding="3" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="right">
		<span class="label">
			Your Name:
		</span>
		</td>
		<td>
		<%	if( doCreate && errorName ) { %>
		    <input type="text" name="name" size="30" maxlength="50" 
			 value="<%= (newName!=null)?newName:"" %>" style="background-color:<%= errorFieldColor %>">
		<%	} else { %> 
		    <input type="text" name="name" size="30" maxlength="50" 
			 value="<%= (newName!=null)?newName:"" %>">
		<%	} %>
		</td>
	</tr>
	<tr>
		<td align="right"><span class="label">Email:</span></td>
		<td>
		<%	if( doCreate && errorEmail ) { %>
		    <input type="text" name="email" size="30" maxlength="50" 
			 value="<%= (newEmail!=null)?newEmail:"" %>" style="background-color:<%= errorFieldColor %>">
		<%	} else { %>
		    <input type="text" name="email" size="30" maxlength="50" 
			 value="<%= (newEmail!=null)?newEmail:"" %>">
		<%	} %>
		</td>
	</tr>
	<tr>
		<td align="right"><span class="label">Desired Username:</span></td>
		<td>
		<%	if( doCreate && errorUsername ) { %>
		    <input type="text" name="username" size="30" maxlength="50" 
			 value="<%= (newUsername!=null)?newUsername:"" %>" style="background-color:<%= errorFieldColor %>">
		<%	} else { %>
		    <input type="text" name="username" size="30" maxlength="50" 
			 value="<%= (newUsername!=null)?newUsername:"" %>">
		<%	} %>
		</td>
	</tr>
	<tr>
		<td align="right"><span class="label">Password:</span></td>
		<td>
		<%	if( doCreate && (errorPassword || errorNoMatchPasswords) ) { %>
		    <input type="password" name="password" size="30" maxlength="50" 
			 value="<%= (newPassword!=null)?newPassword:"" %>" style="background-color:<%= errorFieldColor %>">
		<%	} else { %>
		    <input type="password" name="password" size="30" maxlength="50" 
			 value="<%= (newPassword!=null)?newPassword:"" %>">
		<%	} %>
		</td>
	</tr>
	<tr>
		<td align="right"><span class="label">Confirm Password:</span></td>
		<td>
		<%	if( doCreate && (errorConfirmPassword || errorNoMatchPasswords) ) { %>
		    <input type="password" name="confirmPassword" size="30" maxlength="50" 
			 value="<%= (confirmPassword!=null)?confirmPassword:"" %>" style="background-color:<%= errorFieldColor %>">
		<%	} else { %>
		    <input type="password" name="confirmPassword" size="30" maxlength="50" 
			 value="<%= (confirmPassword!=null)?confirmPassword:"" %>">
		<%	} %>
		</td>
	</tr>
	<tr>
		<td align="right"><label for="cb01"><span class="label">Auto Login:</span></label></td>
		<td>
			<input id="cb01" type="checkbox" name="autoLogin"<%= autoLogin?" checked":"" %>>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<br>
			<input type="submit" value="Create Account">
		</td>
	</tr>
	</table>
	<script language="JavaScript" type="text/javascript">
	<!--
		document.userForm.name.focus();
	//-->
	</script>
</form>

	
<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>



