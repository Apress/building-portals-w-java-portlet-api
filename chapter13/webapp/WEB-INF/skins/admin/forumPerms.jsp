
<%
/**
 *	$RCSfile: forumPerms.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="java.util.*,
                 java.net.URLEncoder,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"%>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>
 
<%!	////////////////////////
	// global page variables
	
	private final int READ = ForumPermissions.READ;
	private final int CREATE_THREAD = ForumPermissions.CREATE_THREAD;
	private final int CREATE_MESSAGE = ForumPermissions.CREATE_MESSAGE;
	private final int MODERATOR = ForumPermissions.MODERATOR;
	private final int[] perms = { READ, CREATE_THREAD, CREATE_MESSAGE,MODERATOR };
	private final String[] permDescriptions = {
		"read content of","post threads in","post messages in","forum moderator"
	};
%>

<%!	///////////////////
	// global methods
	
	private int[] getIntListboxParams( String[] paramVal ) {
		if( paramVal == null ) { 
			return new int[0]; 
		}
		int[] params = new int[paramVal.length];
		for (int i=0;i<paramVal.length;i++)
		{
			try {
				params[i] = Integer.parseInt(paramVal[i]);
			} catch( NumberFormatException nfe ) {}
		}
		return params;
	}
%>

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
	boolean isForumAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isForumAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%	////////////////////
	// get parameters
	
	int forumID = ParamUtils.getIntParameter(request,"forum",-1);
	
	// action parameters
	boolean doAddUserPerm = ParamUtils.getBooleanParameter(request,"doAddUserPerm");
	boolean doAddGroupPerm = ParamUtils.getBooleanParameter(request,"doAddGroupPerm");
	boolean doRemoveUserPerm = ParamUtils.getBooleanParameter(request,"doRemoveUserPerm");
	boolean doRemoveGroupPerm = ParamUtils.getBooleanParameter(request,"doRemoveGroupPerm");
	
	int permType = ParamUtils.getIntParameter(request,"permType",-1);
	int[] usersWithPerm = getIntListboxParams(request.getParameterValues("usersWithPerm"));
	int[] groupsWithPerm = getIntListboxParams(request.getParameterValues("groupsWithPerm"));
	int[] userList = getIntListboxParams(request.getParameterValues("userList"));
	String userAddUsername = request.getParameter("userListField");
	int[] groupList = getIntListboxParams(request.getParameterValues("groupList"));
	int[] userPermTypesList = getIntListboxParams(request.getParameterValues("userPermTypes"));
	int[] groupPermTypesList = getIntListboxParams(request.getParameterValues("groupPermTypes"));
	
%>

<%	/////////////////////
	// other page variables
	
	boolean doAction = ( 
		doAddUserPerm || doAddGroupPerm || doRemoveUserPerm || doRemoveGroupPerm
	);
%>

<%	///////////////////////
	// error variables
	
	boolean errors = false;
%>

<%	//////////////////////////////////
	// global variables
	
	ProfileManager manager = forumFactory.getProfileManager();
%>

<%	/////////////////////
	// try to load the forum from the passed in forumID
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	}
	catch( ForumNotFoundException fnfe ) {
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("Forum " + forumID + " not found") );
		return;
	}
	catch( UnauthorizedException ue ) {
		response.sendRedirect("error.jsp?msg="
			+ URLEncoder.encode("No permission to work with this forum"));
		return;
	}
%>

<%	/////////////////////
	// this forum's properties
	
	String forumName = forum.getName();
	String forumDescription = forum.getDescription();
	Iterator allUsers = manager.users();
	Iterator allGroups = manager.groups();
	int[] usersWithReadPerm = new int[0];
	int[] usersWithThreadPerm = new int[0];
	int[] usersWithMessagePerm = new int[0];
	int[] usersWithModeratorPerm = new int[0];
	int[] groupsWithReadPerm = new int[0];
	int[] groupsWithThreadPerm = new int[0];
	int[] groupsWithMessagePerm = new int[0];
	int[] groupsWithModeratorPerm = new int[0];
	try {
		usersWithReadPerm = forum.usersWithPermission(READ);
		usersWithThreadPerm = forum.usersWithPermission(CREATE_THREAD);
		usersWithMessagePerm = forum.usersWithPermission(CREATE_MESSAGE);
		usersWithModeratorPerm = forum.usersWithPermission(MODERATOR);
		groupsWithReadPerm = forum.groupsWithPermission(READ);
		groupsWithThreadPerm = forum.groupsWithPermission(CREATE_THREAD);
		groupsWithMessagePerm = forum.groupsWithPermission(CREATE_MESSAGE);
		groupsWithModeratorPerm = forum.groupsWithPermission(MODERATOR);
	}
	catch( UnauthorizedException ue ) {}
%>


<%	/////////////////////////
	// do an action!
	
	if( doAction ) {
		
		// add a new user permission
		if( doAddUserPerm ) {
			try {
				for( int i=0; i<userList.length; i++ ) {
					User user = manager.getUser(userList[i]);
					for( int j=0; j<userPermTypesList.length; j++ ) {
					 if (userPermTypesList[j] != MODERATOR){
						forum.addUserPermission(user,userPermTypesList[j]);
					 }
					}
				}
			} catch( UserNotFoundException unfe ) {
			} catch( UnauthorizedException ue ) {
			}
			try {
				if( !"-Enter Username-".equals(userAddUsername) 
					&& userAddUsername != null )
				{
					User user = manager.getUser(userAddUsername);
					for( int j=0; j<userPermTypesList.length; j++ ) {
						forum.addUserPermission(user,userPermTypesList[j]);
					}
				}
			} catch( UserNotFoundException unfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
		
		// remove a user permission
		if( doRemoveUserPerm ) {
			try {
				for( int i=0; i<usersWithPerm.length; i++ ) {
					User user = manager.getUser(usersWithPerm[i]);
					forum.removeUserPermission(user,permType);
				}
			} catch( UserNotFoundException unfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
		
		// add a new group permission
		if( doAddGroupPerm ) {
			try {
				for( int i=0; i<groupList.length; i++ ) {
					Group group = manager.getGroup(groupList[i]);
					for( int j=0; j<groupPermTypesList.length; j++ ) {
						forum.addGroupPermission(group,groupPermTypesList[j]);
					}
				}
			} catch( GroupNotFoundException gnfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
		
		// remove a user permission
		if( doRemoveGroupPerm ) {
			try {
				for( int i=0; i<groupsWithPerm.length; i++ ) {
					Group group = manager.getGroup(groupsWithPerm[i]);
					forum.removeGroupPermission(group,permType);
				}
			} catch( GroupNotFoundException gnfe ) {
			} catch( UnauthorizedException ue ) {
			}
		}
	}
%>

<%	////////////////////
	// if we did something, redirect to this page again (since we're doing POSTS
	// on the form)
	
	// uncommented so i can debug parameters!!
	if( doAction ) {
		response.sendRedirect("forumPerms.jsp?forum="+forumID);
		return;
	}
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
	<script language="JavaScript" type="text/javascript">
	<!--
		function selAllListBox( el, chkbx ) {
			if( chkbx.checked ) {
				for( var i=0; i<el.options.length; i++ ) {
					el.options[i].selected = true;
				}
			}
		}
	//-->
	</script>
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums: Forum Permissions" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<b>Permissions for forum:</b>
<%= forumName %>
<p>


<%-- Permissions summary table --%>
<table bgcolor="#666666" cellpadding="0" cellspacing="0" width="80%" align="center" border="0">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" width="100%" border="0">
<tr bgcolor="#eeeeee">
	<td>
	Permission Summary
	</td>
</tr>
<tr bgcolor="#ffffff">
	<td>
		<table cellpadding="3" cellspacing="0" width="100%" border="0">
		<td>
			<br>
			
			<%-- user permission summary --%>
			Users of this forum with a particular permission:
			<p>
			<table cellpadding="3" cellspacing="0" border="0" align="center">
			<tr>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveUserPerm" value="true">
		<input type="hidden" name="permType" value="<%= READ %>">
				<td align="center">
					<b><%= usersWithReadPerm.length %></b>
					user<%= (usersWithReadPerm.length==1)?"":"s" %> 
					with <font color="#008000">READ</font> permission:
					<br>
					<select size="5" name="usersWithPerm" multiple>
					<%	for( int i=0; i<usersWithReadPerm.length; i++ ) { %>
					<%		try { %>
					<%		User user = manager.getUser(usersWithReadPerm[i]); %>
					<%		int userID = user.getID(); %>
					<%		if( userID == -1 ) { %>
						<option value="<%= userID %>">* Anonymous Users
					<%		} else if( userID == 0 ) { %>
						<option value="<%= userID %>">* All Registered Users
					<%		} else { %>
						<option value="<%= userID %>"><%= user.getUsername() %>				
					<%		} %>
					<%		} catch( UserNotFoundException unfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb03"
					  onclick="selAllListBox(this.form.usersWithPerm,this);">
					<label for="cb03">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveUserPerm" value="true">
		<input type="hidden" name="permType" value="<%= CREATE_THREAD %>">
				<td align="center">
					<b><%= usersWithThreadPerm.length %></b>
					user<%= (usersWithThreadPerm.length==1)?"":"s" %> 
					with <font color="#008000">CREATE THREAD</font> permission:
					<br>
					<select size="5" name="usersWithPerm" multiple>
					<%	for( int i=0; i<usersWithThreadPerm.length; i++ ) { %>
					<%		try { %>
					<%		User user = manager.getUser(usersWithThreadPerm[i]); %>
					<%		int userID = user.getID(); %>
					<%		if( userID == -1 ) { %>
						<option value="<%= userID %>">* Anonymous Users
					<%		} else if( userID == 0 ) { %>
						<option value="<%= userID %>">* All Registered Users
					<%		} else { %>
						<option value="<%= userID %>"><%= user.getUsername() %>				
					<%		} %>
					<%		} catch( UserNotFoundException unfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb04"
					  onclick="selAllListBox(this.form.usersWithPerm,this);">
					<label for="cb04">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveUserPerm" value="true">
		<input type="hidden" name="permType" value="<%= CREATE_MESSAGE %>">
				<td align="center">
					<b><%= usersWithMessagePerm.length %></b>
					user<%= (usersWithMessagePerm.length==1)?"":"s" %> 
					with <font color="#008000">CREATE MESSAGE</font> permission:
					<br>
					<select size="5" name="usersWithPerm" multiple>
					<%	for( int i=0; i<usersWithMessagePerm.length; i++ ) { %>
					<%		try { %>
					<%		User user = manager.getUser(usersWithMessagePerm[i]); %>
					<%		int userID = user.getID(); %>
					<%		if( userID == -1 ) { %>
						<option value="<%= userID %>">* Anonymous Users
					<%		} else if( userID == 0 ) { %>
						<option value="<%= userID %>">* All Registered Users
					<%		} else { %>
						<option value="<%= userID %>"><%= user.getUsername() %>				
					<%		} %>
					<%		} catch( UserNotFoundException unfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb05"
					  onclick="selAllListBox(this.form.usersWithPerm,this);">
					<label for="cb05">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveUserPerm" value="true">
		<input type="hidden" name="permType" value="<%= MODERATOR %>">
				<td align="center">
					<b><%= usersWithModeratorPerm.length %></b>
					user<%= (usersWithModeratorPerm.length==1)?"":"s" %> 
					with <font color="#008000">MODERATOR</font> permission:
					<br>
					<select size="5" name="usersWithPerm" multiple>
					<%	for( int i=0; i<usersWithModeratorPerm.length; i++ ) { %>
					<%		try { %>
					<%		User user = manager.getUser(usersWithModeratorPerm[i]); %>
					<%		int userID = user.getID(); %>
					<%		if( userID == -1 ) { %>
						<option value="<%= userID %>">* Anonymous Users
					<%		} else if( userID == 0 ) { %>
						<option value="<%= userID %>">* All Registered Users
					<%		} else { %>
						<option value="<%= userID %>"><%= user.getUsername() %>				
					<%		} %>
					<%		} catch( UserNotFoundException unfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb05"
					  onclick="selAllListBox(this.form.usersWithPerm,this);">
					<label for="cb05">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
			</tr>
			</table>
			<%-- /user permission summary --%>
			
			<p>
			<hr size="0" width="75%">
			<p>
			
			<%-- group permission summary --%>
			Groups of this forum with a particular permission:
			<p>
			<table cellpadding="3" cellspacing="0" border="0" align="center">
			<tr>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveGroupPerm" value="true">
		<input type="hidden" name="permType" value="<%= READ %>">
				<td align="center">
					<b><%= groupsWithReadPerm.length %></b>
					group<%= (groupsWithReadPerm.length==1)?"":"s" %> 
					with <font color="#008000">READ</font> permission:
					<br>
					<select size="5" name="groupsWithPerm">
					<%	for( int i=0; i<groupsWithReadPerm.length; i++ ) { %>
					<%		try { %>
					<%		Group group = manager.getGroup(groupsWithReadPerm[i]); %>
						<option value="<%= group.getID() %>"><%= group.getName() %>				
					<%		} catch( GroupNotFoundException gnfe ) {%> <%= groupsWithReadPerm[i] %> <%} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb06"
					  onclick="selAllListBox(this.form.groupsWithPerm,this);">
					<label for="cb06">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveGroupPerm" value="true">
		<input type="hidden" name="permType" value="<%= CREATE_THREAD %>">
				<td align="center">
					<b><%= groupsWithThreadPerm.length %></b>
					group<%= (groupsWithThreadPerm.length==1)?"":"s" %> 
					with <font color="#008000">CREATE THREAD</font> permission:
					<br>
					<select size="5" name="groupsWithPerm">
					<%	for( int i=0; i<groupsWithThreadPerm.length; i++ ) { %>
					<%		try { %>
					<%		Group group = manager.getGroup(groupsWithThreadPerm[i]); %>
						<option value="<%= group.getID() %>"><%= group.getName() %>				
					<%		} catch( GroupNotFoundException gnfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb07"
					  onclick="selAllListBox(this.form.groupsWithPerm,this);">
					<label for="cb07">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveGroupPerm" value="true">
		<input type="hidden" name="permType" value="<%= CREATE_MESSAGE %>">
				<td align="center">
					<b><%= groupsWithMessagePerm.length %></b>
					group<%= (groupsWithMessagePerm.length==1)?"":"s" %> 
					with <font color="#008000">CREATE MESSAGE</font> permission:
					<br>
					<select size="5" name="groupsWithPerm">
					<%	for( int i=0; i<groupsWithMessagePerm.length; i++ ) { %>
					<%		try { %>
					<%		Group group = manager.getGroup(groupsWithMessagePerm[i]); %>
						<option value="<%= group.getID() %>"><%= group.getName() %>				
					<%		} catch( GroupNotFoundException gnfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb08"
					  onclick="selAllListBox(this.form.groupsWithPerm,this);">
					<label for="cb08">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
		<form action="forumPerms.jsp">
		<input type="hidden" name="forum" value="<%= forumID %>">
		<input type="hidden" name="doRemoveGroupPerm" value="true">
		<input type="hidden" name="permType" value="<%= MODERATOR %>">
				<td align="center">
					<b><%= groupsWithMessagePerm.length %></b>
					group<%= (groupsWithMessagePerm.length==1)?"":"s" %> 
					with <font color="#008000">MODERATOR</font> permission:
					<br>
					<select size="5" name="groupsWithPerm">
					<%	for( int i=0; i<groupsWithModeratorPerm.length; i++ ) { %>
					<%		try { %>
					<%		Group group = manager.getGroup(groupsWithModeratorPerm[i]); %>
						<option value="<%= group.getID() %>"><%= group.getName() %>				
					<%		} catch( GroupNotFoundException gnfe ) {} %>
					<%	} %>
					</select><br>
					(<input type="checkbox" id="cb08"
					  onclick="selAllListBox(this.form.groupsWithPerm,this);">
					<label for="cb08">Select All</label>) <br>
					<input type="submit" value="Remove">
				</td>
		</form>
			</tr>
			</table>
			<%-- /group permission summary --%>
			
		</td>
		</table>
	</td>
</tr>
</table>
</td>
</table>
<%-- /Permissions summary table --%>

<p>

<%-- Add new user permission --%>
<form action="forumPerms.jsp" method="get"> 
<input type="hidden" name="doAddUserPerm" value="true">
<input type="hidden" name="forum" value="<%= forumID %>">
<table bgcolor="#666666" cellpadding="0" cellspacing="0" width="80%" align="center" border="0">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" width="100%" border="0">
<tr bgcolor="#eeeeee">
	<td width="99%">
	Add new <b>user</b> permission
	</td>
	<td width="1%" nowrap>
	(<a href="createUser.jsp">Create a new user</a>)
	</td>
</tr>
<tr bgcolor="#ffffff">
	<td colspan="2">
		<table cellpadding="3" cellspacing="0" width="100%" border="0">
		<td>
			
			<p>
			<table cellpadding="3" cellspacing="0" border="0" align="center">
			<tr>
				<td nowrap>I want</td>
				<td width="1%">
					<select name="userList" size="2" multiple>
					<option value="-1">* Anonymous Users
					<option value="0">* All Registered Users
					</select>
					(<input type="checkbox" id="cb01"
					  onclick="selAllListBox(this.form.userList,this);">
					<label for="cb01">Select All</label>)
					<br>
					<input type="text" size="20" name="userListField"
					 value="-Enter Username-"
					 onclick="this.select();">
					<br>
				</td>
				<td>to be able to</td>
				<td>
					<select name="userPermTypes" size="<%= perms.length %>" multiple>
					<%	for( int i=0; i<perms.length; i++ ) { %>
					<option value="<%= perms[i] %>"><%= permDescriptions[i] %>
					<%	} %>
					</select>
					<br>
					(<input type="checkbox" id="cb02"
					  onclick="selAllListBox(this.form.userPermTypes,this);">
					<label for="cb02">Select All</label>)
				</td>
				<td>this forum</td>
			</tr>
			<tr>
				<td colspan="5" align="center">
				<input type="submit" name="" value="Add User Permission">
				</td>
			</tr>
			</table>
			
		</td>
		</table>
	</td>
</tr>
</table>
</td>
</table>
</form>
<%-- /Add new user permission --%>

<p>

<%-- Add new group permission --%>
<form action="forumPerms.jsp" method="get"> 
<input type="hidden" name="doAddGroupPerm" value="true">
<input type="hidden" name="forum" value="<%= forumID %>">
<table bgcolor="#666666" cellpadding="0" cellspacing="0" width="80%" align="center" border="0">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" width="100%" border="0">
<tr bgcolor="#eeeeee">
	<td width="99%">
	Add new <b>group</b> permission
	</td>
	<td width="1%" nowrap>
	(<a href="createGroup.jsp">Create a new group</a>)
	</td>
</tr>
<tr bgcolor="#ffffff">
	<td colspan="2">
		<table cellpadding="3" cellspacing="0" width="100%" border="0">
		<td>
			
			<p>
			<table cellpadding="3" cellspacing="0" border="0" align="center">
			<tr>
				<td nowrap>I want</td>
				<td>
					<select name="groupList" size="4" multiple>
					<%	while( allGroups.hasNext() ) { %>
					<%		Group group = (Group)allGroups.next(); %>
						<option value="<%= group.getID() %>"><%= group.getName() %>
					<%	} %>
					</select>
					<br>
					(<input type="checkbox" id="cb11"
					  onclick="selAllListBox(this.form.groupList,this);">
					<label for="cb11">Select All</label>)
				</td>
				<td>to be able to</td>
				<td>
					<select name="groupPermTypes" size="<%= perms.length %>" multiple>
					<%	for( int i=0; i<perms.length; i++ ) { %>
					<option value="<%= perms[i] %>"><%= permDescriptions[i] %>
					<%	} %>
					</select>
					<br>
					(<input type="checkbox" id="cb12"
					  onclick="selAllListBox(this.form.groupPermTypes,this);">
					<label for="cb12">Select All</label>)
				</td>
				<td>this forum</td>
			</tr>
			<tr>
				<td colspan="5" align="center">
				<input type="submit" name="" value="Add Group Permission">
				</td>
			</tr>
			</table>
			
		</td>
		</table>
	</td>
</tr>
</table>
</td>
</table>
</form>
<%-- /Add new user permission --%>

</body>
</html>



