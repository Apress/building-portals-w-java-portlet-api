
<%
/**
 *	$RCSfile: removeGroup.jsp,v $
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
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isGroupAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer groups");
		response.sendRedirect("error.jsp");
		return;
	}
%>
 
<%	////////////////////
	// get parameters
	int groupID   = ParamUtils.getIntParameter(request,"group",-1);
	boolean doDelete = ParamUtils.getBooleanParameter(request,"doDelete");
%>
 
<%	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noGroupSpecified = (groupID < 0);
	boolean errors = (noGroupSpecified);
%>

<%	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();
%>
<%	/////////////////////
	// delete group if specified
	if( doDelete && !errors ) {
		String message = "";
		try {
			Group group = manager.getGroup(groupID);
			manager.deleteGroup(group);
			message = "Group deleted successfully!";
		}
		catch( GroupNotFoundException fnfe ) {
			System.err.println( fnfe );
			message = "No group found";
		}
		catch( UnauthorizedException ue ) {
			System.err.println( ue );
			message = "Not authorized to delete this group";
		}
		request.setAttribute("message",message);
		response.sendRedirect("groups.jsp");
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
	String[] pageTitleInfo = { "Groups : Remove Group" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	////////////////////
	// display a list of groups to edit if no group was specified:
	if( noGroupSpecified ) {
		String formAction = "remove";
%>
		<%@ include file="groupChooser.jsp"%>
<%		out.flush();
		return;
	}
%>


<%	/////////////////////
	// at this point, we know there is a group to work with:
    Group group = null;
	try {
		group = manager.getGroup(groupID);
	} catch( GroupNotFoundException gnfe ) {
	}
%>


Remove <b><%= group.getName() %></b>? 

<p>

<ul>
	Warning: This will permanently delete the group. Are
	you sure you really want to do this? (It will <b>not</b> delete the
	users associated with this group, but will remove group-permissions for these users
    granted by membership of this group.).
</ul>

<form action="removeGroup.jsp" name="deleteForm">
<input type="hidden" name="doDelete" value="true">
<input type="hidden" name="group" value="<%= groupID %>">
	<input type="submit" value=" Delete group ">
	&nbsp;
	<input type="submit" name="cancel" value=" Cancel " style="font-weight:bold;"
	 onclick="location.href='groups.jsp';return false;">
</form>

<script language="JavaScript" type="text/javascript">
<!--
document.deleteForm.cancel.focus();
//-->
</script>



</body>
</html>

