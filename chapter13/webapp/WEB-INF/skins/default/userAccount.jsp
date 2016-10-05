
<%
/**
 *	$RCSfile: userAccount.jsp,v $
 *	$Revision: 1.2 $
 *	$Date: 2002/11/20 21:08:40 $
 */
%>

<%@	page import="java.net.URLEncoder,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*" %>


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

<%	////////////////////
	// get parameters
	
	boolean showEmail = ParamUtils.getBooleanParameter(request,"showEmail");
	boolean showName = ParamUtils.getBooleanParameter(request,"showName");
	boolean saveChanges = ParamUtils.getBooleanParameter(request,"saveChanges");
	String message = ParamUtils.getParameter(request,"msg");
%>

<%	///////////////////
	// error/page variables
	
	boolean unauthorized = false;
	boolean showMessage = (message!=null);
%>

<%	/////////////////////
	// create user object
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ProfileManager manager = forumFactory.getProfileManager();
	User user = null;
	try {
		user = manager.getUser(authToken.getUserID());
	}
	catch( UserNotFoundException unfe ) {}
	boolean isEmailVisible = user.isEmailVisible();
	boolean isNameVisible = user.isNameVisible();
%>

<%	////////////////////
	// save changes if necessary
	
	if( saveChanges && user!=null ) {
		try {
			if( showName != isNameVisible ) {
				user.setNameVisible(showName);
			}
			if( showEmail != isEmailVisible ) {
				user.setEmailVisible(showEmail);
			}
			response.sendRedirect("userAccount.jsp?msg=" 
				+ URLEncoder.encode("Changes saved successfully.") );
			return;
		}
		catch( UnauthorizedException ue ) {
			unauthorized = true;
		}
	}
%>

<%	/////////////////////
	// page title
	String title = "Yazd User Account";
%>
<%	/////////////////////
	// page header
%>
<%@ include file="header.jsp" %>

<%	////////////////////
	// breadcrumb array & include
	String[][] breadcrumbs = {
		{ "Home", "index.jsp" },
		{ "User Account", "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>

<%	///////////////////
	// toolbar variables
	boolean showToolbar = true;
	String viewLink = null;
	String postLink = null;
	String replyLink = null;
	String searchLink = null;
	String accountLink = null;
%>
<%@ include file="toolbar.jsp" %>

<p>

<span class="pageHeader">
	User Account
</span>

<p>

<%	if( showMessage ) { %>
	<span class="pageMessage">
	<%= message %>
	</span>
	<p>
<%	} %>

<%	if( unauthorized && saveChanges ) { %>
	<span class="error">
		You are not authorized to make changes to this user account.
	</span>
<%	} %>

<p>

<b>Privacy:</b>

<p>

<form action="userAccount.jsp">
<input type="hidden" name="saveChanges" value="true">

<table bgcolor="#666666" align="center" cellpadding="0" cellspacing="0" border="0" width="80%">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#eeeeee">
	<td width="60%">&nbsp;</td>
	<td width="20%" align="center"><b>Yes</b></td>
	<td width="20%" align="center"><b>No</b></td>
</tr>
<tr bgcolor="#ffffff">
	<td width="60%">
		Show my email address in the forums
	</td>
	<td width="20%" align="center"><input type="radio" name="showEmail" value="true"<%= isEmailVisible?" checked":"" %>></td>
	<td width="20%" align="center"><input type="radio" name="showEmail" value="false"<%= !isEmailVisible?" checked":"" %>></td>
</tr>
<tr bgcolor="#ffffff">
	<td width="60%">
		Show my name in the forums
	</td>
	<td width="20%" align="center"><input type="radio" name="showName" value="true"<%= isNameVisible?" checked":"" %>></td>
	<td width="20%" align="center"><input type="radio" name="showName" value="false"<%= !isNameVisible?" checked":"" %>></td>
</tr>
<tr bgcolor="#ffffff">
	<td colspan="3" align="center">
		<i>You will still see your name and email address in the forums even
		if you choose to hide them -- others will not, however.</i>
	</td>
</tr>
</table>
</td>
</table>

<p>

<center>
<input type="submit" value="Save Changes">
&nbsp;
<input type="submit" value="Cancel" onclick="location.href='index.jsp';return false;">
</center>

</form>

<br><br>


<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>
