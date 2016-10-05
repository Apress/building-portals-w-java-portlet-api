
<%
/**
 *	$RCSfile: sidebar.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.tree.*,
				 com.Yasna.forum.util.admin.*" %>

<%!	////////////////////
	// page variables
	private final String bullet = "<font color='#666666'>&#149;</font>";
%>

<%!	////////////////////
	// page methods
	
	private final String printTree( com.Yasna.forum.util.tree.Tree tree ) {
		StringBuffer buf = new StringBuffer();
		for( int i=0; i<tree.size(); i++ ) {
			com.Yasna.forum.util.tree.TreeObject treeObject = tree.getChild(i);
			if( treeObject.getType() == tree.NODE ) {
				com.Yasna.forum.util.tree.TreeNode node 
					= (com.Yasna.forum.util.tree.TreeNode)treeObject;
				buf.append( printNodeHTML(tree,node) );
				if( node.isVisible() ) {
					buf.append( printTree( node.getChildren() ) );
				}
			}
			else {
				com.Yasna.forum.util.tree.TreeLeaf leaf 
					= (com.Yasna.forum.util.tree.TreeLeaf)treeObject;
				buf.append( printLeafHTML(leaf) );
			}
		}
		return buf.toString();
	}
	
	private final String printNodeHTML( com.Yasna.forum.util.tree.Tree tree, 
			com.Yasna.forum.util.tree.TreeNode node ) 
	{
		StringBuffer buf = new StringBuffer();
		buf.append( "<table cellpadding='3' cellspacing='0' border='0' width='100%'><td width='1%'>" );
		buf.append( "<a href='sidebar.jsp?flip=" ).append(node.getId()).append("&tree=").append(tree.getName()).append("'>" );
		if( node.isVisible() ) {
			buf.append( "<img src='images/minus.gif' width='11' height='11' border='0'>" );
		}
		else {
			buf.append( "<img src='images/plus.gif' width='11' height='11' border='0'>" );
		}
		buf.append( "</a>" );
		buf.append( "</td><td width='99%' class='sidebarNode'><font size='-1'>" );
		String link = node.getLink();
		if( link != null ) {
			buf.append( "<a href='").append(link).append("' target='main' class='sidebarLink'>" )
			   .append( node.getName() ).append( "</a>" );
		}
		else {
			buf.append( node.getName() );
		}
		buf.append( "</font></td></table>" );
		return buf.toString();
	}
	
	private final String printLeafHTML( com.Yasna.forum.util.tree.TreeLeaf leaf ) {
		StringBuffer buf = new StringBuffer();
		buf.append( "<table cellpadding='3' cellspacing='0' border='0' width='100%'><td width='1%'>" );
		buf.append( "<img src='images/blank.gif' width='11' height='11' border='0'>" );
		buf.append( "</td><td width='1%'>").append(bullet).append("</td><td width='98%' class='sidebarLeaf'>" );
		buf.append( "<a href='" ).append( leaf.getLink() ).append( "' target='main' class='sidebarLink'>" );
		buf.append( leaf.getName() );
		buf.append( "</a></td></table>" );
		return buf.toString();
	}
%>

				 
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

<%	//////////////////////
	// Get parameters
	
	// "tree" (tells which tree to display
	String treeName = ParamUtils.getParameter(request,"tree");
	
	// "flip" (id of node to expand/contract)
	int flip = ParamUtils.getIntParameter(request,"flip",-1);
%>

<%	///////////////////////////////////////
	// Get the permissions for this user:
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	boolean isForumAdmin  = ((Boolean)session.getValue("yazdAdmin.forumAdmin")).booleanValue();
	boolean isGroupAdmin  = ((Boolean)session.getValue("yazdAdmin.groupAdmin")).booleanValue();
	boolean isModerator  = ((Boolean)session.getValue("yazdAdmin.Moderator")).booleanValue();
%>

<%	///////////////////////////////////////
	// Figure out which tree to display
	
	com.Yasna.forum.util.tree.Tree tree = null;
	
	// if treeName is null, try to show the system tree by default if
	// that user has access to see it:
	if( treeName == null ) {
		if( isSystemAdmin || isGroupAdmin ) {
			tree = adminBean.getTree("systemTree");
		}else if (isModerator){
			tree = adminBean.getTree("forumTree");
		}
	}
	// treeName is not null so use that variable to get the tree
	else {
		if( treeName.equals("system") && 
			(isSystemAdmin || isGroupAdmin) ) 
		{
			tree = adminBean.getTree("systemTree");
		}
		else if( treeName.equals("forum") && ( isSystemAdmin || isForumAdmin || isModerator ) ) {
			tree = adminBean.getTree("forumTree");
		}
	}
	
	// Flip any nodes that need to be flipped:
	if( flip > -1 ) {
		com.Yasna.forum.util.tree.TreeNode node 
			= (com.Yasna.forum.util.tree.TreeNode)tree.getChild(flip);
		node.toggleVisible();
	}
%>

<html>
<head>
	<title>sidebar.jsp</title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body bgcolor="#dddddd" text="#000000" link="#0000ff" vlink="#0000ff" alink="#ff0000">

<img src="images/blank.gif" width="50" height="10" border="0"><br>

<%	if( tree != null ) { %>
	<%= printTree(tree) %>
<%	} %>

<br><br>

</body>
</html>

