
<%
/**
 *	$RCSfile: threadContent.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="java.util.*,
                 java.text.SimpleDateFormat,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"
	errorPage="error.jsp"
%>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>

<%!
	/**
	 * Print a child message
	 */
	private String printChildMessage( Forum forum, ForumThread thread, ForumMessage message, int indentation )
	{
		StringBuffer buf = new StringBuffer();
		try {
		if( message.getID() == thread.getRootMessage().getID() ) {
			return "";
		}
		String subject = message.getSubject();
		boolean msgIsAnonymous = message.isAnonymous();
		User author = message.getUser();
		String authorName = author.getName();
		String authorEmail = author.getEmail();
		int forumID = forum.getID();
		int threadID = thread.getID();
		int messageID = message.getID();
		Date creationDate = message.getCreationDate();
		String msgBody = message.getBody();
		
buf.append("<tr>");
buf.append("<form>");
buf.append("<td width=\"1%\" class=\"forumListCell\" align=\"center\">");
buf.append("<input type=\"radio\"");
buf.append("onclick=\"if(confirm('Are you sure you want to delete this message and its replies?')){");
buf.append("location.href='threadContent.jsp?message=").append(messageID).append("&doDeleteMessage=true");
buf.append("&forum=").append(forumID).append("&thread=").append(threadID).append("';}\">");
buf.append("</td>");
buf.append("<td class=\"forumListCell\" width=\"").append(99-indentation).append("%\">");
buf.append("<table cellpadding=2 cellspacing=0 border=0 width=\"100%\">");
buf.append("<tr bgcolor=\"#dddddd\">");
int i = indentation;
while(i-- >= 0 ) {
buf.append("<td bgcolor=\"#ffffff\">&nbsp;</td>");
}
buf.append("<td><b>").append( message.getSubject() ).append("</b></td>");
buf.append("</tr>");
buf.append("<tr bgcolor=\"#eeeeee\">");
String rootMsgUsername = "<i>Anonymous</i>";
User rootMsgUser = message.getUser();
if( !message.getUser().isAnonymous() ) {
rootMsgUsername = rootMsgUser.getUsername();
}
i = indentation;
while(i-- >= 0 ) {
buf.append("<td bgcolor=\"#ffffff\">&nbsp;</td>");
}
buf.append("<td><font size=\"-2\"><b>Posted by ").append( rootMsgUsername ).append(", on some date ").append( message.getCreationDate() ).append("</b></font></td>");
buf.append("</tr>");
buf.append("<tr>");
i = indentation;
while(i-- >= 0 ) {
buf.append("<td>&nbsp;</td>");
}
buf.append("<td>").append( message.getBody() ).append("</td>");
buf.append("</tr>");
buf.append("</table></td></form></tr>");
	
		} catch( Exception ignored ) {}
		return buf.toString();
	}
	
	/**
	 * Recursive method to print all the children of a message.
	 */	
	private String printChildren( TreeWalker walker, Forum forum, ForumThread thread, ForumMessage message, int indentation )
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append( printChildMessage( forum, thread, message, indentation ) );
		
		// recursive call
        int numChildren = walker.getChildCount(message);
        if( numChildren > 0 ) {
            for( int i=0; i<numChildren; i++ ) {
                buf.append(
					printChildren( walker, forum, thread, walker.getChild(message,i), (indentation+1) )
				);
            }
        }
		return buf.toString();
    }
%>
 
 
<%!	//////////////////////////
	// global vars
	
	// date formatter for message dates
	private final SimpleDateFormat dateFormatter
		= new SimpleDateFormat( "EEE, MMM d 'at' hh:mm:ss z" );
	private final static int RANGE = 15;
	private final static int START = 0;
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
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	boolean isModerator   = permissions.get(ForumPermissions.MODERATOR);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin && !isModerator ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>
 
<%	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
	boolean doDeleteThread = ParamUtils.getBooleanParameter(request,"doDeleteThread");
	boolean doDeleteMessage = ParamUtils.getBooleanParameter(request,"doDeleteMessage");
	int threadID = ParamUtils.getIntParameter(request,"thread",-1);
	int messageID = ParamUtils.getIntParameter(request,"message",-1);
	int start = ParamUtils.getIntParameter(request,"start",START);
	int range = ParamUtils.getIntParameter(request,"range",RANGE);
%>
 
<%	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	boolean noThreadSpecified = (threadID < 0);
%>

<%	////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();
%>

<%	//////////////////////////
	// delete an entire thread
	
	if( doDeleteThread ) {
		Forum tempForum = forumFactory.getForum(forumID);
		ForumThread tempThread = tempForum.getThread(threadID);
		tempForum.deleteThread(tempThread);
		response.sendRedirect("forumContent.jsp?forum=" + forumID);
		return;
	}
	else if( doDeleteMessage ) {
		Forum tempForum = forumFactory.getForum(forumID);
		ForumThread tempThread = tempForum.getThread(threadID);
		ForumMessage tempMessage = tempThread.getMessage(messageID);
		tempThread.deleteMessage(tempMessage);
		response.sendRedirect("threadContent.jsp?forum=" + forumID + "&thread=" + threadID);
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
	String[] pageTitleInfo = { "Forums : Manage Thread Content" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	//////////////////////
	// show the name of the forum we're working with (if one was selected)
	Forum currentForum = null;
	if( !noForumSpecified ) {
		try {
			currentForum = forumFactory.getForum(forumID);
	%>
			You're currently working with forum: <b><%= currentForum.getName() %></b>, 
			thread: <strong><%= currentForum.getThread(threadID).getName() %></strong>
	<%	}
		catch( ForumNotFoundException fnfe ) {
	%>
			<span class="errorText">Forum not found.</span>
	<%	}
		catch( UnauthorizedException ue ) {
	%>
			<span class="errorText">Not authorized to administer this forum.</span>
	<%	}
	}
%>
	
<p>

<form action="forumContent.jsp">
<input type="hidden" name="forum" value="<%= forumID %>">
<input type="submit" value="Cancel / Go Back">
</form>

<p>

<%-- thread table --%>

<%
	ForumThread thread = currentForum.getThread(threadID);
	TreeWalker walker = thread.treeWalker();
	ForumMessage rootMessage = walker.getRoot();
%>

<table bgcolor="#cccccc" cellpadding=0 cellspacing=0 border=0 width="100%">
<td>
<table bgcolor="#cccccc" cellpadding=3 cellspacing=1 border=0 width="100%">
<tr bgcolor="#dddddd">
	<td class="forumListHeader" width="1%" nowrap bgcolor="#cccccc"><b>delete?</b></td>
	<td class="forumListHeader" width="99%">&nbsp;</td>
</tr>

	<tr>
		<form>
		<td width="1%" class="forumListCell" align="center">
			<input type="radio"
			 onclick="if(confirm('Are you sure you want to delete this THREAD and all its messages?')){location.href='threadContent.jsp?forum=<%= forumID %>&thread=<%= threadID %>&doDeleteThread=true';}">
			<font size="-2">(delete thread)</font>
		</td>
		<td class="forumListCell" width="99%">
		
		<table cellpadding=2 cellspacing=0 border=0 width="100%">
		<tr bgcolor="#dddddd">
			<td><b><%= rootMessage.getSubject() %></b></td>
		</tr>
		<tr bgcolor="#eeeeee">

<%	String rootMsgUsername = "<i>Anonymous</i>";
	User rootMsgUser = rootMessage.getUser();
	if( !rootMessage.getUser().isAnonymous() ) {
		rootMsgUsername = rootMsgUser.getUsername();
	}
%>
			<td><font size="-2"><b>Posted by <%= rootMsgUsername %>, on some date <%= rootMessage.getCreationDate() %></b></font></td>
		</tr>
		<tr>
			<td><%= rootMessage.getBody() %></td>
		</tr>
		</table>
		
		</td>
		</form>
	</tr>
	
<%= printChildren( walker, currentForum, thread, rootMessage, 0 ) %>
	
</table></td></table>


</body>
</html>

