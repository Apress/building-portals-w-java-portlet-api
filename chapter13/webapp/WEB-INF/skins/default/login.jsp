
<%
/**
 *	$RCSfile: login.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page import="com.Yasna.forum.*,
                 com.Yasna.forum.util.*" 
	errorPage="error.jsp"
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

<%	///////////////////////
	// page forum variables
	
	// do not delete these
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	User user = forumFactory.getProfileManager().getUser(authToken.getUserID());
	long userLastVisitedTime = SkinUtils.getLastVisited(request,response);
%>

<%	/////////////////////////////
	// get parameters, do a login
	
	// do a login if a login is requested
	boolean doLogin = ParamUtils.getBooleanParameter(request,"doLogin");
	String loginUsername = ParamUtils.getParameter(request,"username");
	String loginPassword = ParamUtils.getParameter(request,"password");
	boolean autoLogin = ParamUtils.getCheckboxParameter(request,"autoLogin");
	String referringPage = ParamUtils.getParameter(request,"referer");
	boolean errors = false;
	String errorMessage = "";
	if( doLogin ) {
		if( loginUsername == null || loginPassword == null ) {
			errors = true;
			errorMessage = "Please enter a username and password.";
		}
		if( !errors ) {
			try {
				SkinUtils.setUserAuthorization(
					request,response,loginUsername,loginPassword,autoLogin
				);
				// at this point if no exceptions were thrown, the user is
				// logged in, so redirect to the page that sent us here:
				response.sendRedirect(referringPage);
				return;
			}
			catch( UnauthorizedException e ) {
				errors = true;
				errorMessage = "Invalid username or password.";
			}
		}
	}
%>

<%	//////////////////////
	// Header file include
	
	// The header file looks for the variable "title"
	String title = "Yazd Forums: Login";
%>
<%@ include file="header.jsp" %>


<%	////////////////////
	// Breadcrumb bar
	
	// The breadcrumb file looks for the variable "breadcrumbs" which
	// represents a navigational path, ie "Home > My Forum > Hello World"
	String[][] breadcrumbs = {
		{ "Home", "index.jsp" },
		{ "Login", "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>

<%	///////////////////////
	// breadcrumb variables
	
	// change these values to customize the look of your breadcrumb bar
	
	// Colors
	String loginBgcolor = "#999999";
	String loginFgcolor = "#ffffff";
	String loginHeaderColor = "#ccccdd";
%>

<h2>Login</h2>

<%	if( errors ) { %>
<h4><i>Error: <%= errorMessage %></i></h4>
<%	} %>

<form action="login.jsp" method="post" name="loginForm">
<input type="hidden" name="doLogin" value="true">
<input type="hidden" name="referer" value="<%= (!errors)?request.getHeader("REFERER"):referringPage %>">

<center>

<table bgcolor="<%= loginBgcolor %>" cellpadding="1" cellspacing="0" border="0" width="350">
<td>
<table bgcolor="<%= loginFgcolor %>" cellpadding="4" cellspacing="0" border="0" width="100%">
<tr>
	<td align="right" width="50%">
	Username
	</td>
	<td width="50%">
	<input type="text" name="username" size="15" maxlength="25"
	 value="<%= (loginUsername!=null)?loginUsername:"" %>">
	</td>
</tr>
<tr>
	<td align="right" width="50%">
	Password
	</td>
	<td width="50%">
	<input type="password" name="password" size="15" maxlength="25"
	 value="<%= (loginPassword!=null)?loginPassword:"" %>">
	</td>
</tr>
<tr>
	<td align="right" width="50%">
	<label for="cb01">Enable Auto-Login</label>
	</td>
	<td width="50%">
	<input type="checkbox" name="autoLogin" id="cb01">
	<span class="label">
	(<a href="" 
	  onclick="alert('Warning: this stores your username and password in a cookie on your hard-drive.');return false;"
	  ><b>?</b></a>)
	</span>
	</td>
</tr>
<tr bgcolor="<%= loginHeaderColor %>">
	<td align="right" colspan="2">
	<input type="submit" value="Login">
	</td>
</tr>
</table>
</td>
</table>

</center>

<p>

<script language="JavaScript" type="text/javascript">
<!--
	document.loginForm.username.focus();
//-->
</script>
	
<center>
<i>Don't have an account? <a href="createAccount.jsp">Create one</a>.</i>
</center>
	
</form>

<p>

<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>


