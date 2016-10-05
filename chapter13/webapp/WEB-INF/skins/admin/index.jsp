
<%
/**
 *	$RCSfile: index.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page 
	import="java.io.*,
            java.util.*,
            com.Yasna.forum.*,
            com.Yasna.forum.util.*,
	        com.Yasna.forum.util.tree.*,
	        com.Yasna.forum.util.admin.*" %>
	
<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>
		
<%	//////////////////////////////
	// Yazd installation check
	
	// Check for the existence of the property "setup" in the 
	// yazd.properties file. This is managed by the PropertyManager class.
	// This property tells us if the admin tool is being run for the first time.
	
	boolean setupError = false;
	String installed = null;
	try {
		installed = PropertyManager.getProperty("setup");
		if( installed == null || !installed.equals("true") ) {
			// the "installed" property doesn't exist or isn't set
			response.sendRedirect("setup/setup.jsp");
			return;
		}
		//else if( !installed.equals("true") ) {
		//	setupError = true;
		//}
	}
	catch( Exception e ) {
		// signal an error. the file yazd.properties might not exist.
		setupError = true;
	}
	
	// print out a setup error:
	if( setupError ) { %>
		<html>
		<head>
		<title>Yazd Administration - Beta</title>
		<link rel="stylesheet" href="style/global.css">
		</head>
		<body>
		Setup Error! Make sure your <b>yazd.properties</b> file is in
		your app server's classpath.
		</body>
		</html>
<%		// for some reason, we have to call flush.. some app servers won't
		// display the above html w/o flushing the stream
		out.flush();
		return;
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

<%	////////////////////////
	// get parameters
	boolean logout = ParamUtils.getBooleanParameter(request,"logout");
%>

<%	/////////////////////////
	// logout if requested:
	if( logout ) {
		try {
			session.invalidate();
			adminBean.resetAuthToken();
		}
		catch( IllegalStateException ignored ) { // if session is already invalid
		}
		finally {
			response.sendRedirect( "index.jsp" );
			return;
		}
	}
%>


<%	///////////////////////////////////////
	// Get the permissions for this user:
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isForumAdmin  = ((Boolean)session.getValue("yazdAdmin.forumAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	boolean isModerator  = ((Boolean)session.getValue("yazdAdmin.Moderator")).booleanValue();
%>

<%	//////////////////////////////////////////////////////////////////
	// set the menu trees in the bean based on the user's permissions
	
	// system tree
	if( isSystemAdmin || isGroupAdmin ) {
		com.Yasna.forum.util.tree.Tree systemTree 
			= new com.Yasna.forum.util.tree.Tree("system");
		int nodeID = 0;
		TreeNode node = null;
		
		if( isSystemAdmin ) {
			node = new TreeNode( nodeID++, "System Settings" );
			node.addChild( new TreeLeaf("Cache", "cache.jsp") );
			node.addChild( new TreeLeaf("Database Info", "dbInfo.jsp") );
			node.addChild( new TreeLeaf("Search Settings", "searchSettings.jsp") );
			node.addChild( new TreeLeaf("Property Manager", "propManager.jsp") );
			node.setVisible(true);
			systemTree.addChild(node);
		}
		
		if( isSystemAdmin ) {
			node = new TreeNode( nodeID++, "Users" );
			node.addChild( new TreeLeaf("User Summary", "users.jsp") );
			//node.addChild( new TreeLeaf("System Admins", "systemAdmins.jsp") );
			node.addChild( new TreeLeaf("Passwords", "password.jsp") );
			node.addChild( new TreeLeaf("Create User", "createUser.jsp") );
			//node.addChild( new TreeLeaf("Edit User",   "editUser.jsp") );
			node.addChild( new TreeLeaf("Remove User", "removeUser.jsp") );
			node.setVisible(true);
			systemTree.addChild(node);
		}
		
		if( isSystemAdmin || isGroupAdmin ) {
			node = new TreeNode( nodeID++, "Groups" );
			node.addChild( new TreeLeaf("Group Summary", "groups.jsp") );
			if( isSystemAdmin ) {
				node.addChild( new TreeLeaf("Create Group", "createGroup.jsp") );
			}
			node.addChild( new TreeLeaf("Edit Group",   "editGroup.jsp") );
			node.addChild( new TreeLeaf("Remove Group", "removeGroup.jsp") );
			node.setVisible(true);
			systemTree.addChild(node);
		}
		
		if( systemTree.size() > 0 ) {
			adminBean.addTree( "systemTree", systemTree );
		}
	}
	
	// forum tree
	if( isSystemAdmin || isModerator ) {
		com.Yasna.forum.util.tree.Tree forumTree 
			= new com.Yasna.forum.util.tree.Tree("forum");
		int nodeID = 0;
		TreeNode node = null;
		
		node = new TreeNode( nodeID++, "Forums" );
		if (isSystemAdmin){
		node.addChild( new TreeLeaf("Summary",     "forums.jsp") );
		node.addChild( new TreeLeaf("Create",      "createForum.jsp") );
		node.addChild( new TreeLeaf("Edit Properties", "editForum.jsp") );
		node.addChild( new TreeLeaf("Remove",      "removeForum.jsp") );
		node.addChild( new TreeLeaf("Filters",     "forumFilters.jsp") );
		}
		if (isSystemAdmin || isModerator){
		node.addChild( new TreeLeaf("Content",     "forumContent.jsp") );
		}
		node.setVisible(true);
		forumTree.addChild(node);
		
		adminBean.addTree( "forumTree", forumTree );
	}
%>

<html>
<head>
<title>Yazd Administration</title>
</head>

<frameset rows="85,*" bordercolor="#0099cc" border="0" frameborder="0" framespacing="0" style="background-color:#0099cc">
	<frame src="header.jsp" name="header" scrolling="no" marginheight="0" marginwidth="0" noresize>
	<%--frame src="toolbar.jsp" name="toolbar" scrolling="no" noresize--%>
	<frameset cols="175,*" bordercolor="#0099cc" border="0" frameborder="0" style="background-color:#0099cc">
		<frame src="sidebar.jsp" name="sidebar" scrolling="auto" marginheight="0" marginwidth="0" noresize>	   
		<frameset rows="15,*" bordercolor="#0099cc" border="0" frameborder="0" style="background-color:#0099cc">
			<frame src="shadow.html" name="shadow" scrolling="no" marginheight="0" marginwidth="0" noresize>	   
			<frame src="main.jsp" name="main" scrolling="auto" noresize>
		</frameset>
		
	</frameset>
	
</frameset>

</html>


