
<%
/**
 *	$RCSfile: createForum.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page import="java.util.*,
                 com.Yasna.forum.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*" %>

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
	
	// make sure the user is authorized to create forums::
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to create forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>


<%	////////////////////
	// Get parameters
	boolean doCreate         = ParamUtils.getBooleanParameter(request,"doCreate");
	String forumName         = ParamUtils.getParameter(request,"forumName");
	String forumDescription  = ParamUtils.getParameter(request,"forumDescription");
%>

<%	////////////////////
	// Error variables
	boolean forumNameError = (forumName==null);
	
	boolean errors = forumNameError;
	
	String errorMessage = "An error occured.";
%>

<%	//////////////////////////////
	// create forum if no errors and redirect to forum main page
	
	if( !errors && doCreate ) {
		Forum newForum = null;
		String message = null;
		try {
			if( forumDescription == null ) {
				forumDescription = "";
			}
			newForum = forumFactory.createForum( forumName, forumDescription );
			message = "Forum \"" + forumName + "\" created successfully!";
			request.setAttribute("message",message);
			response.sendRedirect("forumPerms.jsp?forum="+newForum.getID());
			return;
		}
		catch( UnauthorizedException ue ) {
			message = "Error creating forum \"" + forumName
				+ "\" - you are not authorized to create forums.";
		}
		catch( Exception e ) {
			message = "Error creating forum \"" + forumName + "\"";
		}
		request.setAttribute("message",message);
		response.sendRedirect("error.jsp");
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
	String[] pageTitleInfo = { "Forums", "Create Forum" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	///////////////////////////////////////////
	// print out error message if there was one
	if( errors && doCreate ) {
%>		<span class="errorText">
		<%= errorMessage %>
		</span>
		<p>
<%	} %>

Note: This creates a forum with no permissions. After you create this forum,
you will be taken to the forum permissions screen.

<form action="createForum.jsp" method="post">
<input type="hidden" name="doCreate" value="true">
	
<%-- forum name --%>
Forum name:
<%	if( forumNameError && doCreate ) { %>
	<span class="errorText">
	Forum name is required.
	</span>
<%	} %>
<ul>
	<input type="text" name="forumName" value="<%= (forumName!=null)?forumName:"" %>">
</ul>

<%-- forum description --%>
Forum description: <i>(optional)</i>
<ul>
	<textarea name="forumDescription" cols="30" rows="5" wrap="virtual"><%= (forumDescription!=null)?forumDescription:"" %></textarea>
</ul>

<%-- create button --%>
<input type="submit" value="Create">
	
</form>

</body>
</html>

