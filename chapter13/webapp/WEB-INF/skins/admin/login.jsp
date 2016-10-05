
<%
/**
 *	$RCSfile: login.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="com.Yasna.forum.*,
                 com.Yasna.forum.util.*" %>
			
<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>
			
<% try { %>
				 
<%	// get parameters
	String username = ParamUtils.getParameter(request,"username");
	String password = ParamUtils.getParameter(request,"password",true);
	String redirect = ParamUtils.getParameter(request,"redirect");
	boolean doLogin = ParamUtils.getBooleanParameter(request,"doLogin");
%>

<%	// check redirect string
	if( redirect == null ) {
		redirect = "/";
	}
%>

<%	String errorMessage = "";
	if( doLogin ) {
		//AuthorizationFactory authFactory = AuthorizationFactory.getInstance();
		try {
			Authorization authToken = AuthorizationFactory.getAuthorization( username, password );
			boolean isSystemAdmin = SkinUtils.isSystemAdmin(authToken);
			boolean isForumAdmin = SkinUtils.isForumAdmin(authToken);
			boolean isGroupAdmin = SkinUtils.isGroupAdmin(authToken);
			boolean isModerator = SkinUtils.isForumModerator(authToken);
			// set admin booleans in session:
			session.putValue("yazdAdmin.systemAdmin",new Boolean(isSystemAdmin));
			session.putValue("yazdAdmin.forumAdmin",new Boolean(isForumAdmin));
			session.putValue("yazdAdmin.groupAdmin",new Boolean(isGroupAdmin));
			session.putValue("yazdAdmin.Moderator",new Boolean(isModerator));
			if( isSystemAdmin || isGroupAdmin || isModerator ) {
				adminBean.setAuthToken( authToken );
				response.sendRedirect(redirect);
				return;
			}
		}
		catch( UnauthorizedException ue ) {
			errorMessage = "Login failed: Make sure your username and password "
			+ "are correct and that you are authorized to use this tool.";
		}
	}
%>

<html>
<head>
	<title>Yazd Admin Login</title>
	<style type="text/css">
	.label, .error {
		font-family : verdana,arial,helvetica,sans-serif;
		font-size : 10pt;
	}
	.error {
		color : #ff0000;
	}
	</style>
	<script language="JavaScript" type="text/javascript">
		<!-- 
		// break out of frames
		if (self.parent.frames.length != 0) {
			self.parent.location=document.location;
		}
		// go to help page
		function help() {
			location.href = 'help.jsp';
		}
		//-->
	</script>
</head>

<body bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">


<form action="login.jsp" name="loginForm" method="post">
<input type="hidden" name="doLogin" value="true">
<input type="hidden" name="redirect" value="index.jsp">

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
<td width="49%"><br></td>
<td width="2%">	
	<noscript>
	<table border="0" cellspacing="0" cellpadding="0">
	<td class="error"><b>Error:</b> You don't have JavaScript enabled. This tool uses JavaScript
		and much of it will not work correctly without it enabled. Please turn
		JavaScript back on and reload this page.
	</td>
	</table>
	<br><br><br><br>
	</noscript>
	
	<span class="error"><%= errorMessage %></span>
	<p>
	<table border="0" cellspacing="0" cellpadding="0">
	<tr><td background="images/loginbacktop.gif" colspan="4" width="100%"><img src="images/blank.gif" width="250" height="30" border="0"></td></tr>
	<tr>
		<td rowspan="7" background="images/lside.gif"><img src="images/blank.gif" width="10" height="1" border="0"></td>
		<td colspan="2"><img src="images/blank.gif" width="230" height="10" border="0"></td>
		<td rowspan="7" background="images/rside.gif"><img src="images/blank.gif" width="10" height="1" border="0"></td>
	<tr>
		<td align="right" nowrap class="label">username &nbsp;</td>
		<td><input type="text" name="username" size="15" maxlength="25"></td>
	</tr>
	<tr><td colspan="2"><img src="images/blank.gif" width="230" height="5" border="0"></td></tr>
	<tr>
		<td align="right" nowrap class="label">password &nbsp;</td>
		<td><input type="password" name="password" size="15" maxlength="20"></td>
	</tr>
	<tr><td colspan="2"><img src="images/blank.gif" width="1" height="5" border="0"></td></tr>
	<tr>
		<td colspan="2" align="center">
			<input type="submit" value="Login">
			&nbsp;
			<input type="submit" value="Help" onclick="help(); return false;">
		</td>
	</tr>
	<tr>
		<td colspan="2"><img src="images/blank.gif" width="230" height="10" border="0"></td></tr>
	</tr>
	<tr><td background="images/loginbacktop2.gif" colspan="4" width="100%"><img src="images/blank.gif" width="250" height="10" border="0"></td></tr>
	</table>
</td>
<td width="49%"><br></td>
</table>

</form>

<script language="JavaScript" type="text/javascript">
<!--
	document.loginForm.username.focus();
//-->
</script>

</body>
</html>

<% } catch( Exception e ) {
		System.err.println(e);
		e.printStackTrace();
	}
%>

