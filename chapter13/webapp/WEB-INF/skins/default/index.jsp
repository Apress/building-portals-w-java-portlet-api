
<%
/**
 *	$RCSfile: index.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page import="java.util.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	//////////////////////////////////
	// customize the look of this page
	
	// Colors of the table that displays a list of forums
	final static String forumTableBgcolor = "#cccccc";
	final static String forumTableFgcolor = "#ffffff";
	final static String forumTableHeaderFgcolor = "#eeeeee";
%>

<%	//////////////////////////////
	// Logout a user if requested
	
	// check for the parameter "logout=true"
	if( ParamUtils.getBooleanParameter(request,"logout") ) {
		// invalidate their authentication token
		SkinUtils.removeUserAuthorization(request,response);
		// redirect them to the page from where they came
		response.sendRedirect(request.getHeader("REFERER"));
		return;
	}
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

<%	//////////////////////
	// Header file include
	
	// The header file looks for the variable "title"
	String title = "Yazd Forums: Example Skin";
%>
<%@ include file="header.jsp" %>


<%	////////////////////
	// Breadcrumb bar
	
	// The breadcrumb file looks for the variable "breadcrumbs" which
	// represents a navigational path, ie "Home > My Forum > Hello World"
	String[][] breadcrumbs = {
		{ "Home", "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>


<%	/////////////
	// Toolbar
	
	// The toolbar file looks for the following variables. To make a particular
	// "button" not appear, set a variable to null.
	boolean showToolbar = true;
	String viewLink = null;
	String postLink = null;
	String replyLink = null;
	String searchLink = null;
	// we can show a link to a user account if the user is logged in (handled
	// in the toolbar jsp)
	String accountLink = "userAccount.jsp";
%>
<%@ include file="toolbar.jsp" %>


<%	////////////////////////////////////////////////////
	// customize which forums are displayed on this page
	
	// There are two ways to decide which forums get diplayed to the user
	//
	//	1.	Display a list of all forums the current user has permission to
	//		view. For instance, if there are 4 forums in the system and
	//		a "guest" has authorization to view 2 of them, then just those
	//		two forums will be displayed. This works the same way for 
	//		registered users -- they'll only see the forums they have 
	//		access to.
	//		
	//	2.	Specify by name which forums to display. Again, if there are
	//		4 forums in the system and you specify all forums by name and
	//		the skin user has access to only 2 of them, then only 2 forums
	//		will be displayed
	
	// Set the following boolean variable to "false" to display a list
	// of forums in the system (case 1) or set it to "true" to specify
	// a list of forums by name (case 2)
	
	boolean loadForumsByName = false;
	
	// If you choose to load forums by name, specify the names by
	// adding them to the following list of forum names:
	
	ArrayList forumNames = new ArrayList(0);
	if( loadForumsByName ) {
		forumNames.add( "First Forum" );
		forumNames.add( "Another Forum" );
	}
%>

<p>

<h2>Forums</h1>

<p>

<%	/////////////////////
	// check for messages
	
	// we might come to this page from another page and that page might
	// pass us a message. If so, grab it and display it (also remove it from persistence)
	String message = SkinUtils.retrieve(request,response,"message",true);
	if( message != null ) {
%>
	<h4><i><%= message %></i></h4>
	<p>
<%	} %>

<%	// print out a greeting to a registered user.
	
	if( !user.isAnonymous() ) { %>
	
	Welcome back, <%= user.getName() %>! 
	(If you're not <%= user.getName() %>, <a href="index.jsp?logout=true">click here</a>.)
	
<%	} %>

<p>

<table bgcolor="<%= forumTableBgcolor %>" cellpadding="0" cellspacing="0" border="0" width="100%">
<td>
<table bgcolor="<%= forumTableBgcolor %>" cellpadding="4" cellspacing="1" border="0" width="100%">
<tr bgcolor="<%= forumTableHeaderFgcolor %>">
	<td align="center" width="1%">
		<small>New<br>Posts</small>
	</td>
	<td align="center" width="1%" nowrap>
		<small>Forum Name</small>
	</td>
	<td align="center" width="1%">
		<small>Topics/<br>Messages</small>
	</td>
	<td align="center" width="95%">
		<small>Description</small>
	</td>
	<td align="center" width="1%" nowrap>
		<small>Last Updated</small>
	</td>
</tr>


<%	//////////
	// Note:
	
	// the following java code should not be modified -- changing html is ok.
	
	// The iterator we use to loop through forums
	Iterator forumIterator;
	if( loadForumsByName ) {
		forumIterator = forumNames.iterator();
	}
	else {
		forumIterator = forumFactory.forums();
	}
%>

<%	/////////////////////
	// loop through forums, display forum info:
	if( !forumIterator.hasNext() ) {
%>
   	<tr bgcolor="<%= forumTableFgcolor %>">
		<td colspan="6" align="center">
		<br>
		<span class="error">
		Sorry, there are no forums in the Yazd system. Please have your forum administrator create some.
		</span>
		<br><br>
		</td>
	</tr>
<%	}
	boolean forumLoaded = false;
	while( forumIterator.hasNext() ) {
		Forum forum;
		// since loading a forum could throw an unauthorized exception, we 
		// should catch it so we can skip this forum and try to load another
		// forum
		try {
			if( loadForumsByName ) {
				forum = forumFactory.getForum( (String)forumIterator.next() );
			}
			else {
				forum = (Forum)forumIterator.next();
			}
			forumLoaded = true;
			int forumID = forum.getID();
			String forumName = forum.getName();
			String forumDescription = forum.getDescription();
			int threadCount = forum.getThreadCount();
			int messageCount = forum.getMessageCount();
			Date creationDate = forum.getCreationDate();
			Date modifiedDate = forum.getModifiedDate();
			boolean wasModified = (userLastVisitedTime < modifiedDate.getTime());
%>
	<tr bgcolor="<%= forumTableFgcolor %>">
		<td align="center"><img src="images/<%= (wasModified)?"bang":"blank" %>.gif" width="8" height="8" border="0"></td>
		<td nowrap><a href="viewForum.jsp?forum=<%= forumID %>" class="forum"><%= forumName %></a></td>
		<td align="center" nowrap><%= threadCount %> / <%= messageCount %></td>
		<td><i><%= (forumDescription!=null)?forumDescription:"&nbsp;" %></i></td>
		<td nowrap align="right"><small class="date"><%= SkinUtils.dateToText(modifiedDate) %></small></td>
	</tr>
<%		} catch( UnauthorizedException ignored ) {
		}
	}
	
	// if no forums were successfully loaded, print out a "error, no 
	// permissions" message
	if( !forumLoaded ) {
%>
   	<tr bgcolor="<%= forumTableFgcolor %>">
		<td colspan="6" align="center">
		<br>
		<span class="error">
		Sorry, you don't have permission to view any forums.
		</span>
		<br><br>
		</td>
	</tr>
<%	}
%>
</table>
</td>
</table>

<p>

<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>

