
<%
/**
 *	$RCSfile: removeForum.jsp,v $
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
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>
 
<%	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
	boolean doDelete = ParamUtils.getBooleanParameter(request,"doDelete");
%>
 
<%	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	boolean errors = (noForumSpecified);
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
			Forum deleteableForum = forumFactory.getForum(forumID);
			forumFactory.deleteForum(deleteableForum);
			message = "Forum deleted successfully!";
		}
		catch( ForumNotFoundException fnfe ) {
			message = "No forum found";
		}
		catch( UnauthorizedException ue ) {
			message = "Not authorized to delete this forum";
		}
		request.setAttribute("message",message);
		response.sendRedirect("forums.jsp");
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
	String[] pageTitleInfo = { "Forums : Remove Forum" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	////////////////////
	// display a list of forums to remove if no group was specified:
	if( noForumSpecified ) {
		String formAction = "remove";
%>
		<%@ include file="forumChooser.jsp"%>
<%		out.flush();
		return;
	}
%>


<%	/////////////////////
	// at this point, we know there is a forum to work with:
	Forum forum = null;
	try {
		forum                  = forumFactory.getForum(forumID);
	} catch( ForumNotFoundException fnfe ) {
	} catch( UnauthorizedException ue ) {
	}
	String forumName             = forum.getName();
	int threadCount = forum.getThreadCount();
	int messageCount = forum.getMessageCount();
%>

Remove <b><%= forumName %></b>? 
(Threads: <%= threadCount %>, Messages <%= messageCount %>,
<a href="forumContent.jsp?forum=<%=forumID%>">view content</a>
)

<p>

<ul>
	Warning: This will delete <b>all</b> forum messages &amp; threads. Are
	you sure you really want to do this? (It will <b>not</b> delete users or groups
	who have permission to use this forum.)
</ul>

<form action="removeForum.jsp" name="deleteForm">
<input type="hidden" name="doDelete" value="true">
<input type="hidden" name="forum" value="<%= forumID %>">
	<input type="submit" value=" Delete Forum ">
	&nbsp;
	<input type="submit" name="cancel" value=" Cancel " style="font-weight:bold;"
	 onclick="location.href='forums.jsp';return false;">
</form>

<script language="JavaScript" type="text/javascript">
<!--
document.deleteForm.cancel.focus();
//-->
</script>

</body>
</html>

