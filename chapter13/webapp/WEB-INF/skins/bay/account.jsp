
<%
/**
 *	$RCSfile: account.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:06 $
 */
%>


<%@ page 
	import="java.io.*,
	        java.text.*,
            java.util.*,
            java.net.*,
            com.Yasna.forum.*,
            com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	final int CREATE = 1;
	final int MANAGE = 2;
	final int PASSWORD = 3;
	final int LOGIN = 4;
	final int VIEW = 5;
%>

<%	////////////////////////
	// Authorization check
	
	// check for the existence of an authorization token
	Authorization authToken = SkinUtils.getUserAuthorization(request,response);
	
	// if the token was null, they're not authorized. Since this skin will
	// allow guests to view forums, we'll set a "guest" authentication
	// token
	if( authToken == null ) {
		authToken = AuthorizationFactory.getAnonymousAuthorization();
	}
%>

<%	// Get parameters
	int     mode		= ParamUtils.getIntParameter(request, "mode", -1);
	int     userID 		= ParamUtils.getIntParameter(request, "user", -1);
	int     forumID		= ParamUtils.getIntParameter(request, "forum", -1);
	boolean doCreate 	= ParamUtils.getBooleanParameter(request, "doCreate");
	String  username 	= ParamUtils.getParameter(request, "username");	    // required to create account
	String  password 	= ParamUtils.getParameter(request, "password");		// required to create account
	String  password2 	= ParamUtils.getParameter(request, "password2");		// required to create account
	String  email    	= ParamUtils.getParameter(request, "email");		// required to create account
	String  URL			= ParamUtils.getParameter(request, "URL");
	String  name		= ParamUtils.getParameter(request, "name");
	String  sig			= ParamUtils.getParameter(request, "signature");
	boolean emailVisible= ParamUtils.getCheckboxParameter(request, "emailVisible");
	boolean nameVisible	= ParamUtils.getCheckboxParameter(request, "nameVisible");
	boolean autoLogin   = ParamUtils.getCheckboxParameter(request, "autoLogin");
	String  message		= ParamUtils.getParameter(request, "message");
	
	if (message == null)
	    message = "";
	
	boolean emailOK				= ( email != null && email.length() != 0 );
	boolean usernameOK			= ( username != null && username.length() != 0 );
	boolean passwordOK			= ( password != null && password.length() != 0 );
	passwordOK 					= ((mode == LOGIN && passwordOK) ||
								   (mode == CREATE && (passwordOK && password.equals(password2))) ||
								   (mode == MANAGE && ((password == null && password2 == null) || (passwordOK && password.equals(password2)))) );
	
	boolean requiredParamsOK	= ( emailOK && usernameOK && passwordOK );
	boolean createSuccess 		= false;
	String	redirectPage		= "account.jsp?mode=" + mode; 
	User 	user = null;
%>

<%	// Create a ForumFactory object
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ProfileManager manager = forumFactory.getProfileManager();
%>

<%	// Login in
	if (mode == LOGIN) {
		// check to make sure the username and password are valid (ie, not null or blank)
		if( !usernameOK || !passwordOK ) {
			message = "Login failed. Please make sure your username and password are correct.";
			response.sendRedirect( "account.jsp?message=" + URLEncoder.encode(message) );
			return;
		}
		else {
			try {
				// get the user's authorization token
				authToken = SkinUtils.setUserAuthorization(request, response, username, password, autoLogin);

				// redirect to the main page
				response.sendRedirect( "index.jsp" );
				return;
			}
			catch( UnauthorizedException ue ) {
				message = "Login failed. Please make sure your username and password are correct.";
				response.sendRedirect( "account.jsp?message=" + URLEncoder.encode(message) );
				return;
			}
		}
	}
	
	// Create a new user, or change your account
	// check to make sure username, password and email are valid (ie, not null or blank)
	else if (doCreate && requiredParamsOK && (mode == CREATE || mode == MANAGE)) {
		try {
			if (mode == CREATE) {
				user = manager.createUser(username, password, email);	// throws a UserAlreadyExistsException
				message = "Account created successfully!";
			} else {
				user = manager.getUser(authToken.getUserID());
				message = "Account updated successfully!";
			}
			
			if (name != null && !name.equals(user.getName())) {
				user.setName(name);
			}
			if (password != null && mode == MANAGE) {
				user.setPassword(password);
			}
			if (email != null && !email.equals(user.getEmail())) {
				user.setEmail(email);
			}
			if (nameVisible != user.isNameVisible()) {
				user.setNameVisible(nameVisible);
			}
			if (emailVisible != user.isEmailVisible()) {
				user.setEmailVisible(emailVisible);
			}
			// IP, URL and Signature are extended properties:
			if (!request.getRemoteAddr().equals(user.getProperty("IP"))) {
				user.setProperty("IP", request.getRemoteAddr());
			}
			if (URL != null && !URL.equals(user.getProperty("URL"))) {
				user.setProperty("URL", URL);
			}
			if (sig != null && !sig.equals(user.getProperty("sig"))) {
				user.setProperty("sig", sig);
			}

			if (mode == CREATE) {
				authToken = SkinUtils.setUserAuthorization(request, response, username, password, autoLogin);
			}

			response.sendRedirect( redirectPage + "&message=" + URLEncoder.encode(message) );
			return;
		}
		catch( UserAlreadyExistsException uaee ) { 
			message = "Sorry, that username is taken.";
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
		catch( UserNotFoundException unfe ) {
			message = "Oops, you do not seem to exist.";
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
		catch( UnauthorizedException ue ) {
			java.io.StringWriter sw = new java.io.StringWriter();
			ue.printStackTrace(new PrintWriter(sw,true));
			message = "You are not authorized." + sw.toString();
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
	}

	// View or Update a users attributes
	// check to make sure username, password and email are valid (ie, not null or blank)
	else if (!doCreate && (mode == VIEW || mode == MANAGE)) {
		try {
			if (mode == VIEW) {
// userID?				user = manager.getUser(authToken.getUserID());
				message = "Account created successfully!";
			} else {
				user = manager.getUser(authToken.getUserID());
			}

			username = user.getUsername();
			name = user.getName();
			if (name == null) {
				name = (mode == VIEW) ? "<i>Not visible</i>" : "";
			}
			email = user.getEmail();
			if (email == null) {
				email = (mode == VIEW) ? "<i>Not visible</i>" : "";
			}
			nameVisible = user.isNameVisible();
			emailVisible = user.isEmailVisible();
			URL = user.getProperty("URL");
			sig = user.getProperty("sig");
		}
		catch( UserNotFoundException unfe ) {
			message = "Oops, you do not seem to exist.";
			response.sendRedirect( redirectPage +"&message=" + URLEncoder.encode(message) );
			return;
		}
	}
	else if (!doCreate && (mode == CREATE)) {
		nameVisible = true;
		emailVisible = true;
	}
%>

<%	// header include
	String title = "Manage Your Account";
%>
<%@	include file="header.jsp" %>

<%-- begin breadcrumbs --%>
<font face="verdana" size="-1"><b><a href="index.jsp" class="normal">Home</a>
&gt;
<%	if( mode > 0 ) { %>
	<a href="account.jsp" class="normal">User Account</a>
	&gt;
	<%	if( mode == CREATE ) { %>
		Create an account
	<%	} else if( mode == MANAGE ) { %>
		Manage your account
	<%	} else if( mode == PASSWORD ) { %>
		Password help
	<%	} else if( mode == LOGIN ) { %>
		User Account Login
	<%	} else { %>
	
	<%	} %>
<%	} else { %>
	User Account Login
<%	} %>
</b></font>
<%-- end breadcrubms --%>

<p>
<font color="#ff0000"><%= message %></font>
<p>

<%	if(mode == CREATE || mode == MANAGE) { %>
	
	<p>

	<%	// if we're trying to create a user and there's an error, print a message:
		if( !createSuccess && doCreate ) { %>
		User account creation failed. Please correct the marked fields.
	<%	} %>

	<ul>
		<table cellpadding=3 cellspacing=0 border=0>
		<form action="account.jsp" method="post">
		<input type="hidden" name="mode" value="<%= mode %>">
		<input type="hidden" name="doCreate" value="true">
		<tr>
			<td align="right">
				<b>Username:</b>
				<%	if( !usernameOK && doCreate ) { %>
				<font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
				<%	} %>
			</td>
			<td>
				<% if (mode == CREATE) { %>
					<input type="text" name="username" value="<%= username == null ? "" : username %>" size=20 maxlength=30>
					<i>(required)</i>
				<% } else {%>
					<input type="hidden" name="username" value="<%= username == null ? "" : username %>">
					<%= username == null ? "[error]" : username %>
				<% } %>
			</td>
		</tr>
		<tr>
			<td align="right">
				<b>Password:</b>
				<%	if( !passwordOK && doCreate ) { %>
				<font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
				<%	} %>
			</td>
			<td><input type="password" name="password" value="">
				<% if (mode == CREATE) { %>
					<i>(required)</i>
				<% } %>
			</td>
		</tr>
		<tr>
			<td align="right">
				<b>Confirm:</b>
				<%	if( !passwordOK && doCreate ) { %>
				<font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
				<%	} %>
			</td>
			<td><input type="password" name="password2" value="">
				<% if (mode == CREATE) { %>
					<i>(required)</i>
				<% } %>
		</tr>
		<tr>
			<td align="right">
				<b>Email address:</b>
				<%	if( !emailOK && doCreate ) { %>
				<font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
				<%	} %>
			</td>
			<td><input type="text" name="email" value="<%= email == null ? "" : email %>" size=30 maxlength=30>
				<i>(required)</i>
			</td>
		</tr>
		<tr>
			<td align="right">Name:</td>
			<td><input type="text" name="name" value="<%= name == null ? "" : name %>" size=40 maxlength=50></td>
		</tr>
		<tr>
			<td align="right">URL:</td>
			<td><input type="text" name="URL" value="<%= URL == null ? "" : URL %>" size=40 maxlength=255></td>
		</tr>
		<tr>
			<td align="right">Signature:<br><font size=1>(255 characters only)</font></td>
			<td><textarea cols=40 rows=4 name="signature" wrap="virtual"><%= sig == null ? "" : sig %></textarea></td>
		</tr>
		<tr>
			<td align="right">Show my name:</td>
			<td><input type="checkbox" name="nameVisible" <%= nameVisible ? "checked" : "" %>></td>
		</tr>
		<tr>
			<td align="right">Show my email:</td>
			<td><input type="checkbox" name="emailVisible" <%= emailVisible ? "checked" : "" %>></td>
		</tr>
		<% if (mode == CREATE) { %>
	    	<tr>
				<td align="right"><font face="verdana" size="-1">Auto login:</font></td>
				<td><input type="checkbox" name="autoLogin"></td>
			</tr>
		<% } %>
		<tr>
			<td><br></td>
			<td><input type="submit" value="<%= mode == CREATE ? "Create account" : "Update account" %>"></td>
		</tr>
		</form>
		</table>
	</ul>
	
<%	} else if( mode == PASSWORD ) { %>

	<font face="verdana" size="-1">
	<b>Send your password</b>
	<ul>
		<li> Not yet implemented.
	</ul>
	</font>
		
<%-- default mode: check if the authorization token references an anonymous user -- that means
	 the user is logged in already so we should display her account options
--%>
<%	} else { %>

	<%	user = manager.getUser( authToken.getUserID() );
		boolean anonUser = user.isAnonymous();
	%>
	
	<font face="verdana" size=2>
	
	<%	if (anonUser) { %>
		<form action="account.jsp" method="post">
		<input type="hidden" name="mode" value="<%= LOGIN %>">
		<ul>
			<table cellpadding=3 cellspacing=0 border=0>
			<tr>
				<td align="right"><font face="verdana" size="-1"><i>username:</i></font></td>
				<td><input type="text" name="username" size="20"></td>
			</tr>
			<tr>
				<td align="right"><font face="verdana" size="-1"><i>password:</i></font></td>
				<td><input type="password" name="password" size="20"></td>
			</tr>
        	<tr>
				<td align="right"><font face="verdana" size="-1"><i>auto login:</i></font></td>
				<td><input type="checkbox" name="autoLogin"></td>
			</tr>
			<tr>
				<td align="right" colspan="2"><input type="submit" value="Login"></td>
			</tr>
			</table>
		</ul>
		</form>
		<p>
		Don't have an account? <a href="account.jsp?mode=<%= CREATE %>" class="normal">Create one</a>
		<p>
		Forget your <a href="account.jsp?mode=<%= PASSWORD %>" class="normal" 
		             onclick="alert('Not implemented yet');return false;">password?</a>
	<%	} else { %>
		You are logged in <b><%= user.getUsername() %></b>.
		<br><br>
		[<a href="index.jsp?logout=true" class="normal">logout</a>]
	<%	} %>

	<br>
	</font>
	
<%	} %>

<%@	include file="footer.jsp" %>


