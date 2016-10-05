
<%
/**
 *	$RCSfile: editGroup.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page import="java.util.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"%>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>

<% try { %>
 
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
	
	int groupID = ParamUtils.getIntParameter(request,"group",-1);
	String newGroupName = ParamUtils.getParameter(request,"groupName");
	String newGroupDescription = ParamUtils.getParameter(request,"groupDescription",true);
	boolean doEdit = ParamUtils.getBooleanParameter(request,"doEdit");
%>

<%	////////////////////
	//
	
	boolean noGroupSpecified = (groupID<0);
%>

<%	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean errorGroupName = (newGroupName == null);
	boolean errorGroupAlreadyExists = false;
	boolean errorNoPermissionToEdit = false;
	boolean errorGroupNotFound = false;
	
	boolean errors = (noGroupSpecified);
%>


<%	////////////////////////////////////////////////////////////////
	// if there are no errors at this point, start the process of
	// adding the user
	
	ProfileManager manager = forumFactory.getProfileManager();
	Group thisGroup = null;
	if( !errors ) {
		try {
			thisGroup = manager.getGroup(groupID);
		} catch( GroupNotFoundException gnfe ) {
			errorGroupNotFound = true;
			System.err.println( "groupNotFoundException" );
		}
	}
	errors = (errors || (thisGroup==null));
	if( !errors && doEdit ) {
		// get a profile manager to edit user properties
		try {
			thisGroup.setName( newGroupName );
			if( newGroupDescription != null ) {
				thisGroup.setDescription( newGroupDescription );
			}
		}
		catch( UnauthorizedException ue ) {
			errorNoPermissionToEdit = true;
		}
	}
	String name = null;
	String description = null;
	if( !errors ) {
		name = thisGroup.getName();
		description = thisGroup.getDescription();
	}
%>

<%	/////////////////////
	// overall  error check
	errors = (errorGroupName || errorGroupAlreadyExists
		|| errorNoPermissionToEdit);
%>

<%	////////////////////
	// set error messages
	if( errors ) {
		if( errorGroupName ) {
			errorMessage = "Please specify a group name.";
		}
		else if( errorGroupAlreadyExists ) {
			errorMessage = "This group already exists, please choose "
				+ "a different name";
		}
		else if( errorNoPermissionToEdit ) {
			errorMessage = "Sorry, you don't have permission to edit "
				+ "a group";
		}
		else if( errorGroupNotFound ) {
			errorMessage = "Group not found";
		}
		else {
			errorMessage = "A general error occured.";
		}
	}
%>

<%	//////////////////////////////////////////////////////////////////////
	// if a group was edited was successfully created, say so and return (to stop the 
	// jsp from executing
	if( !errors ) {
		request.setAttribute("message","Group was edited successfully!");
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
	String[] pageTitleInfo = { "Groups : Edit Group" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	////////////////////
	// display a list of groups to edit if no group was specified:
	if( noGroupSpecified ) {
		String formAction = "edit";
%>
		<%@ include file="groupChooser.jsp"%>
<%		out.flush();
		return;
	}
%>

<%-- form --%>
<form action="editGroup.jsp" method="post">
<input type="hidden" name="group" value="<%= groupID %>">
<input type="hidden" name="doEdit" value="true">

<b>Change Group Information:</b>
<p>

<table bgcolor="#999999" cellspacing="0" cellpadding="0" border="0" width="95%" align="right">
<td>
<table bgcolor="#999999" cellspacing="1" cellpadding="3" border="0" width="100%">

<%-- name row --%>
<tr bgcolor="#ffffff">
	<td><font size="-1">Group Name:</i></font></td>
	<td><input type="text" name="groupName" size="30" maxlength="100"
		 value="<%= (name!=null)?name:"" %>">
	</td>	
</tr>

<%-- description row --%>
<tr bgcolor="#ffffff">
	<td><font size="-1">Group Description:<br>(optional)</i></font></td>
	<td>
		<textarea name="groupDescription" wrap="virtual" cols="40" rows="5"
 		><%= (description!=null)?description:"" %></textarea>
	</td>	
</tr>

</table>
</td>
</table>
<br clear="all"><br>

<p>
<center>
	<input type="submit" value="Update Group Info">
	&nbsp;
	<input type="submit" value="Cancel" onclick="location.href='groups.jsp';return false;">
</center>

</form>
<%-- /form --%>

</body>
</html>

<%	} catch( Exception e ) {
		System.err.println(e);
		e.printStackTrace();
	}
%>

