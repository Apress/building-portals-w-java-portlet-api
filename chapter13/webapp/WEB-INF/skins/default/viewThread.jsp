
<%
/**
 *	$RCSfile: viewThread.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page import="java.io.*,
                 java.util.*,
                 java.text.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	////////////////
	// global variables
	SimpleDateFormat dateFormatter 
		= new SimpleDateFormat("EEE, MMM d 'at' hh:mm:ss z");
	
	/**
	 * Print a child message
	 */
	private String printChildMessage( Forum forum, ForumThread thread, ForumMessage message, int indentation, String mode )
	{
		StringBuffer buf = new StringBuffer();
		try {
		if( mode.equals("flat") ) {
			indentation = 0;
		}
		String subject = message.getSubject();
		boolean msgIsAnonymous = message.isAnonymous();
		User author = message.getUser();
		String authorName = author.getName();
		String authorEmail = author.getEmail();
		String userName = author.getUsername();
		int forumID = forum.getID();
		int threadID = thread.getID();
		int messageID = message.getID();
		Date creationDate = message.getCreationDate();
		String msgBody = message.getBody();
		
		buf.append("<table bgcolor=\"#cccccc\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"");
		buf.append( 100-(3*indentation) ).append("%\" align=\"right\">");
		buf.append("<td><table bgcolor=\"#cccccc\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\" width=\"100%\">");
		buf.append("<tr bgcolor=\"#eeeeee\">");
		buf.append("<td width=\"1%\" nowrap>");
		buf.append("<span class=\"messageHeader\">");
		buf.append( subject );
		buf.append("</span>");
		buf.append("<br>");
		buf.append("<span class=\"messageAuthor\">");
		if( msgIsAnonymous ) {
			String displayName = "<i>Anonymous</i>";
			String savedName = message.getProperty("name");
			String savedEmail = message.getProperty("email");
			if( savedName != null ) {
				displayName = "<i>" + savedName + "</i>";
			}
			if( savedEmail != null ) {
				displayName = "<a href=\"mailto:" + savedEmail + "\">" + displayName + "</a>";
			}
			buf.append(displayName);
		} else {
			
			if( author.isEmailVisible() ) {
				userName = "<a href=\"mailto:" + authorEmail + "\">" + userName + "</a>";
			}
			buf.append("posted by: ").append(userName);
			if( author.isNameVisible() && (authorName!=null && !authorName.equals("")) ) {
				buf.append(" ( ");
				if( author.isEmailVisible() ) {
					buf.append("&nbsp;<i><a href=\"mailto:").append(authorEmail).append("\">");
				}
				buf.append(authorName);
				if( author.isEmailVisible() ) {
					buf.append("</a>");
				}
				buf.append(" )");
			}
		}
		//if( authorEmail != null && !authorEmail.equals("") ) {
		//	buf.append("&nbsp;<i><a href=\"mailto:").append(authorEmail).append("\">").append(authorEmail).append("</a></i>");
		//}
		buf.append("</span>");
		buf.append("</td>");
		buf.append("<td width=\"98%\" align=\"center\">");
		buf.append("<a href=\"post.jsp?reply=true&forum=");
		buf.append(forumID).append("&thread=").append(threadID).append("&message=").append(messageID).append("\"");
		buf.append("><img src=\"images/reply.gif\" width=\"50\" height=\"19\" alt=\"Click to reply to this message\" border=\"0\"></a>");
		buf.append("</td>");
		buf.append("<td width=\"1%\" nowrap align=\"center\">");
		buf.append("<small class=\"date\">Posted ").append(SkinUtils.dateToText(creationDate));
		buf.append("<br><i>").append(dateFormatter.format(creationDate)).append("</i></small>");
		buf.append("</td>");
		buf.append("</tr>");
		buf.append("<tr><td bgcolor=\"#ffffff\" colspan=\"3\">");
		buf.append("<table cellpadding=\"5\" cellspacing=\"0\" border=\"0\" width=\"100%\">");
		buf.append("<td>").append( (msgBody!=null)?msgBody:"" ).append("</td>");
		buf.append("</table>");
		buf.append("</td>");
		buf.append("</tr>");
		buf.append("</table>");
		buf.append("</td>");
		buf.append("</table>");
		buf.append("<br clear=\"all\"><br>\n\n");
		} catch( Exception ignored ) {}
		return buf.toString();
	}
	
	/**
	 * Recursive method to print all the children of a message.
	 */	
	private String printChildren( TreeWalker walker, Forum forum, ForumThread thread, ForumMessage message, int indentation, String mode )
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append( printChildMessage( forum, thread, message, indentation, mode ) );
		
		// recursive call
        int numChildren = walker.getChildCount(message);
        if( numChildren > 0 ) {
            for( int i=0; i<numChildren; i++ ) {
                buf.append(
					printChildren( walker, forum, thread, walker.getChild(message,i), (indentation+1), mode )
				);
            }
        }
		return buf.toString();
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
	// get parameters
	int forumID  = ParamUtils.getIntParameter(request,"forum",-1);
	int threadID = ParamUtils.getIntParameter(request,"thread",-1);
	String mode = ParamUtils.getParameter(request,"mode");
	if( mode == null ) {
		mode = "threaded";
	}
%>

<%	//////////////////////
	// page error variables
	
	String errorMessage = "";
	
	boolean invalidForumID = (forumID < 0);
	boolean invalidThreadID = (threadID < 0);
	boolean notAuthorizedToViewForum = false;
	boolean forumNotFound = false;
	boolean threadNotFound = false;
	boolean rootMessageNotFound = false;
%>

<%	//////////////////////////
	// try loading up forums (exceptions may be thrown)
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	}
	catch( UnauthorizedException ue ) {
		notAuthorizedToViewForum = true;
	}
	catch( ForumNotFoundException fnfe ) {
		forumNotFound = true;
	}
%>

<%	//////////////////////////////////
	// try loading up the thread
	ForumThread thread = null;
	if( forum != null && !invalidThreadID ) {
		try {
			thread = forum.getThread(threadID);
		} catch( ForumThreadNotFoundException ftnfe ) {}
		if( thread == null ) {
			threadNotFound = true;
		}
	}
%>

<%	///////////////////////
	// Try loading up the root message
	ForumMessage rootMessage = null;
	int rootMessageID = -1;
	rootMessage = thread.getRootMessage();
	rootMessageID = rootMessage.getID();
%>

<%	/////////////////////
	// global error check
	boolean errors = (invalidForumID || invalidThreadID 
		|| notAuthorizedToViewForum || forumNotFound 
		|| threadNotFound || rootMessageNotFound || (forum==null) );
%>

<%	/////////////////////
	// check for errors
	if( errors ) {
		if( invalidForumID ) {
			errorMessage = "No forum specified or invalid forum ID.";
		}
		else if( notAuthorizedToViewForum ) {
			errorMessage = "No permission to view this forum.";
		}
		else if( forumNotFound ) {
			errorMessage = "Requested forum was not found in the system.";
		}
		else if( threadNotFound ) {
			errorMessage = "Requested thread was not found in the system.";
		}
		else if( rootMessageNotFound ) {
			errorMessage = "Requested message was not found in the system.";
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
	// get forum properties (assumed no errors at this point)
	String forumName = forum.getName();
	String threadName = thread.getName();
%>

<%	//////////////////////////////
	// get root message properties
	
	User author = rootMessage.getUser();
	int rootMsgAuthorID = author.getID();
	String userName = null;
	String authorName = null;
	String authorEmail = null;
	authorName = author.getName();
	userName = author.getUsername();
	authorEmail = author.getEmail();
	Date creationDate = rootMessage.getCreationDate();
	boolean rootMsgIsAnonymous = rootMessage.isAnonymous();
	//if( authorName == null ) {
	//	rootMsgIsAnonymous = true;
	//}
	String rootMsgSubject = rootMessage.getSubject();
	String rootMsgBody = rootMessage.getBody();
%>

<%	////////////////////
	// page title variable for header
	String title = "Yazd Forums: " + forumName + ": " + threadName;
%>
<%	/////////////////////
	// page header
%>
<%@ include file="header.jsp" %>


<%	////////////////////
	// breadcrumb variable
	String[][] breadcrumbs = {
		{ "Home", "index.jsp" },
		{ forumName, ("viewForum.jsp?forum="+forumID) },
		{ threadName, "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>


<%	///////////////////
	// toolbar variables
	boolean showToolbar = true;
	String viewLink = "viewForum.jsp?forum="+forumID;
	String postLink = "post.jsp?mode=new&forum="+forumID;
	String replyLink = "post.jsp?reply=true&forum="+forumID+"&thread="+threadID+"&message="+rootMessageID;
	String searchLink = "search.jsp?forum="+forumID;
	String accountLink = "userAccount.jsp";
%>
<%@ include file="toolbar.jsp" %>

<p>

<%-- root message --%>
<table bgcolor="#cccccc" cellpadding="0" cellspacing="0" border="0" width="100%" align="right">
<td><table bgcolor="#cccccc" cellpadding="4" cellspacing="1" border="0" width="100%">
	<tr bgcolor="#eeeeee">
		<td width="1%" nowrap>
			<span class="messageHeader">
			<%= rootMsgSubject %>
			</span>
			<br>
			<span class="messageAuthor">
			<%	if( rootMsgIsAnonymous ) { 
					String savedName = rootMessage.getProperty("name");
					String savedEmail = rootMessage.getProperty("email");
					if( savedEmail != null && savedName != null ) {
						authorName = "<a href=\"mailto:" + savedEmail + "\">" + savedName + "</a>";
					}
					else {
						if( savedName == null ) {
							authorName = "<i>Anonymous</i>";
						}
						else {
							authorName = savedName;
						}
					}
			%>

				posted by: <%= authorName %>

			<%	} else { %>

				<%	if( author.isEmailVisible() ) { %>
					<% userName = "<a href=\"mailto:" + authorEmail + "\">" + userName + "</a>"; %>
				<%	} %>
				posted by: <%= userName %>
					
				<%	if( author.isNameVisible() && (authorName!=null && !authorName.equals("")) ) { %>
				(<%	if( author.isEmailVisible() ) { %>
					&nbsp;<i><a href="mailto:<%= authorEmail %>"
					><% } %><%= authorName %><% if( author.isEmailVisible()) { %></a></i>
					<% } %>
					)
				<%	} %>
			
			<%	} %>
			</span>
		</td>
		<td width="98%" align="center">
			<a href="post.jsp?reply=true&forum=<%= forumID %>&thread=<%= threadID %>&message=<%= rootMessageID %>"
			><img src="images/reply.gif" width="50" height="19" alt="Click to reply to this message" border="0"></a>
		</td>
		<td width="1%" nowrap align="center">
			<small class="date">
			Posted <%= SkinUtils.dateToText(creationDate) %>
			<br><i><%= dateFormatter.format(creationDate) %></i>
			</small>
		</td>
	</tr>
	<tr><td bgcolor="#ffffff" colspan="3">
		<table cellpadding="5" cellspacing="0" border="0" width="100%">
		<td><%= (rootMsgBody!=null)?rootMsgBody:"" %></td>
		</table>
		</td>
	</tr>
</table>
</td>
</table>
<%-- /root message --%>

<br clear="all">
<br>

<%	/////////////////////
	// print out the number of replies
	int numReplies = thread.getMessageCount()-1;
%>
<b>There <%= (numReplies==1)?"is":"are" %> <%= numReplies %> <%= (numReplies==1)?"reply":"replies" %> to this message.</b>

<p>

<%	/////////////////////////
	// print out all child messages:
	
	// if there are children to display:
	if( numReplies > 0 ) {
		StringBuffer buf = new StringBuffer();
		TreeWalker treeWalker = thread.treeWalker();
		int numChildren = treeWalker.getChildCount(rootMessage);
		int indentation = 1;
		for( int i=0; i<numChildren; i++ ) {
			buf.append( 
				printChildren( treeWalker, forum, thread, treeWalker.getChild(rootMessage,i), indentation, mode )
			);
		}
%>
		<%= buf.toString() %>
<%	} %>

<br clear="all">

<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>



