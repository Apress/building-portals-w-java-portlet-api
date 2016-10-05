
<%
/**
 *	$RCSfile: userDetail.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page import="java.util.*,
                 java.text.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	////////////////
	// global variables
	
	// date formatter
	SimpleDateFormat dateFormatter 
		= new SimpleDateFormat("EEE, MMM d 'at' hh:mm:ss z");
	
	// number of threads to display per page array:
	private final int[] threadRange = { 10,25,50,100 };
	
	// default starting index and number of threads to display
	// per page (for paging)
	private final int DEFAULT_RANGE = 10;
	private final int START_INDEX   = 0;
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
	// get parameters
	int range    = ParamUtils.getIntParameter(request,"range",DEFAULT_RANGE);
	int start    = ParamUtils.getIntParameter(request,"start",0);
	String usrname = ParamUtils.getParameter(request,"user");
	int forumID  = ParamUtils.getIntParameter(request,"forum",-1);
%>

<%	//////////////////////
	// page error variables
	
	String errorMessage = "";
	
	boolean invalidUser = (usrname == null);
	boolean invalidForum = (forumID < 0);
	boolean notAuthorizedToViewUser = false;
	boolean userNotFound = false;
%>

<%	//////////////////////////
	// try loading up user (exceptions may be thrown)
	ProfileManager manager = forumFactory.getProfileManager();
	// try to load up the forum (optional paramter)
	Forum forum = null;
	if( !invalidForum ) {
		try {
			forum = forumFactory.getForum(forumID);
		}
		catch( UnauthorizedException ue ) {
			invalidForum = true;
		}
		catch( ForumNotFoundException fnfe ) {
			invalidForum = true;
		}
	}
%>

<%	/////////////////////
	// global error check
	boolean errors = (invalidUser || notAuthorizedToViewUser || userNotFound
		|| (user==null) );
%>

<%	/////////////////////
	// check for errors
	if( errors ) {
		if( invalidUser ) {
			errorMessage = "No user specified or invalid user ID.";
		}
		else if( notAuthorizedToViewUser ) {
			errorMessage = "No permission to view this user.";
		}
		else if( userNotFound ) {
			errorMessage = "Requested user was not found in the system.";
		}
		else {
			errorMessage = "General error occured. Please contact the "
				+ "administrator and bug him/her.";
		}
		request.setAttribute("message",errorMessage);
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%	//////////////////////
	// get user properties (assumed no errors at this point)
	
	int userID = user.getID();
	String email = null;
	String name = null;
	email = user.getEmail();
	name = user.getName();
	boolean isAnonymous = user.isAnonymous();
	boolean isEmailVisible = user.isEmailVisible();
	boolean isNameVisible = user.isNameVisible();
	int userMessageCount = 0;
	Iterator userMessageIterator = null;
	if( !invalidForum ) {
		userMessageCount = manager.userMessageCount(user,forum);
		userMessageIterator = manager.userMessages(user,forum);
	}
	
	// forum properties
	String forumName = null;
	if( !invalidForum ) {
		forumName = forum.getName();
	}
%>

<%	/////////////////////
	// page title
	String title = "Yazd Forums: " + forumName;
%>
<%	/////////////////////
	// page header
%>
<%@ include file="header.jsp" %>


<%	////////////////////
	// breadcrumb array & include
	String[][] breadcrumbs = null;
	if( !invalidForum ) {
		breadcrumbs = new String[][] {
			{ "Home", "index.jsp" },
			{ ("Forum: "+forumName), ("viewForum.jsp?forum="+forumID) },
			{ "User Detail: " +usrname, "" }
		};
	}
	else {
		breadcrumbs = new String[][] {
			{ "Home", "index.jsp" },
			{ "User Detail: " +usrname, "" }
		};
	}
%>
<%@ include file="breadcrumb.jsp" %>


<%	///////////////////
	// toolbar variables
	boolean showToolbar = true;
	String viewLink = null;
	if( !invalidForum ) {
		viewLink = "viewForum.jsp?forum="+forumID;
	}
	String postLink = null;
	if( !invalidForum ) {
		postLink = "post.jsp?mode=new&forum="+forumID;
	}
	String replyLink = null;
	String accountLink = "userAccount.jsp";
	String searchLink = "search.jsp?forum="+forumID;
%>
<%@ include file="toolbar.jsp" %>

<p>

<span class="viewForumHeader">
	User Detail For: <%= usrname %>
</span>
<br>
<%	if( !invalidForum ) { %>
	<span class="viewForumDeck">
	<%	if( isEmailVisible ) { %>
		Email Address: <a href="mailto:<%= email %>"><%= email %></a>
		<br>
	<%	} %>
	Number of messages posted in this forum: <%= userMessageCount %>
	</span>
<%	} %>

<p>

<b>All messages posted by the user in this forum:</b>
<p>

<%-- show list of messages posted by this user --%>
<table bgcolor="#cccccc" cellpadding="0" cellspacing="0" border="0" width="95%" align="right">
<td>
<table bgcolor="#cccccc" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#eeeeee">
	<td class="forumListHeader" width="99%">subject</td>
	<td class="forumListHeader" width="1%" nowrap>Post Date</td>
</tr>
<%	if( !userMessageIterator.hasNext() ) { %>
<tr bgcolor="#ffffff">
	<td align="center" colspan="2"><i>No messages posted by <b><%= usrname %></b> in this forum.</i></td>
</tr>
<%	} %>

<%	while( userMessageIterator.hasNext() ) { %>
<%		ForumMessage message = (ForumMessage)userMessageIterator.next(); 
		String subject = message.getSubject();
		Date creationDate = message.getCreationDate();
		ForumThread thread = message.getForumThread();
%>
<tr bgcolor="#ffffff">
	<td class="forumListCell">
		<a href="viewThread.jsp?forum=<%=forumID%>&thread=<%=thread.getID()%>"
		 ><%= subject %></a>
	</td>
	<td nowrap class="forumListDateCell" align="center"><%= SkinUtils.dateToText(creationDate) %></td>
</tr>
<%	} %>
</table>
</td>
</table>
<br clear="all"><br>
<%-- /show list of messages posted by this user --%>
	
<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>


