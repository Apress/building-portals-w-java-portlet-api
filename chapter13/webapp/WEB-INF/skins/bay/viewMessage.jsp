
<%
/**
 *	$RCSfile: viewMessage.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:07 $
 */
%>

<%@ page
	import="java.util.*,
	        java.text.*,
	        java.net.*,
			com.Yasna.forum.*,
			com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>


<%-- Global variables, methods --%>
<%!	final int DEFAULT_FORUM_ID = 1; // ServletForum.com Beta Forum ID
	final int IMG_WIDTH = 13;
	final int IMG_HEIGHT = 12;
	final SimpleDateFormat dateFormatter = new SimpleDateFormat( "EEEE, MMM d, yyyy" );
	final SimpleDateFormat timeFormatter = new SimpleDateFormat( "H:mm, z" );

	final String TREE_ROOT =    "<img src='images/t0.gif' align='top' width=8 height=20>";
	final String TREE_EMPTY =   "<img src='images/t0.gif' align='top' width=20>";
	final String TREE_LINE =    "<img src='images/t1.gif' align='top'>";
	final String TREE_CORNER =  "<img src='images/t2.gif' align='top'>";
	final String TREE_FORK =    "<img src='images/t3.gif' align='top'>";

	final String TREE_ARROW =   "<img src='images/t_arrow.gif' align='top'>";
	final String TREE_NEW =   	"<img src='images/t_new.gif' align='top'>";

	// Recursive method to print all the children of a message.
	private void printChildren(TreeWalker walker, ForumMessage message, ForumThread thread, 
								int currentMessageID, int level, int childCount, 
								StringBuffer buf, int messageID, int forumID,
								int lineNr[], String tree, long lastVisited,
								String forumParams) 
	{
		java.util.Date date = message.getModifiedDate();
		long	modifiedDate = date.getTime();
		String subj         = message.getSubject();
		String username 	= "Anonymous";
		if( !message.isAnonymous() ) {
			User user = message.getUser();
			username = user.getName();
			if (username == null)
    			username = user.getUsername();
		}
		int msgID           = message.getID();
		int thrID           = thread.getID();
		boolean onCurrent   = (message.getID() == messageID);
		boolean rootMsg     = (lineNr[0] == 0);
		String bgcolor      = "";
		
		if( subj == null || subj.equals("") ) {
			subj = "[no subject]";
		}
		if( (lineNr[0]++ & 1) != 0) {
			//bgcolor = " bgcolor='#dddddd'";
			bgcolor = " bgcolor='#eeeeee'";
		}
		else {
			bgcolor = " bgcolor='#ffffff'";
		}
		if( onCurrent ) {
			bgcolor = " bgcolor='#dddddd'";
			//bgcolor = " bgcolor='#ffffff'";
		}
		
		// print start of row, with appropriate background color
		buf.append("<tr valign=middle").append( bgcolor ).append(">\n");
		
		// start of subject cell
		if( onCurrent ) {
			buf.append("<td width='98%' class='subjectOn'><font size=2>");
		} else {
			buf.append("<td width='98%' class='subject'><font size=2>");
		}
		
		// padding images
		if (rootMsg) {
		    if (!onCurrent)
    		    buf.append(TREE_ROOT);
		} else {
    		buf.append(tree);
    		if (childCount > 0)
    		    buf.append(TREE_FORK);
    		else
    		    buf.append(TREE_CORNER);
		}
		
		if (onCurrent) {
			buf.append(TREE_ARROW);
		}

		// subject cell
		if (modifiedDate > lastVisited) {
			buf.append(TREE_NEW);
		}
		if (!onCurrent) {
			buf.append("<a href='viewMessage.jsp?message=").append(msgID);
			buf.append("&thread=").append(thrID);
			buf.append("&parent=").append(currentMessageID);
			buf.append("&forum=").append(forumID);
			if (forumParams.length() > 0)
				buf.append(forumParams);
			buf.append("'>");
		} 
		else {
			buf.append("<a name='currentMsg'>");
		}
		buf.append(subj);
		buf.append("</a>");
		buf.append("</font></td>").append("\n");
		
		// username cell
		if( onCurrent ) {
			buf.append("<td width='1%' nowrap align='center' class='usernameOn'><font size=2>&nbsp;");
		} else {
			buf.append("<td width='1%' nowrap align='center' class='username'><font size=2>&nbsp;");
		}
		buf.append(username);
		buf.append("&nbsp;</font></td>");
		
		// date cell
		if( onCurrent ) {
			buf.append("<td width='1%' nowrap class='datetimeOn'><font size=1>");
		} else {
			buf.append("<td width='1%' nowrap class='datetime'><font size=1>");
		}
		buf.append(dateFormatter.format(date)).append(" - ").append(timeFormatter.format(date));
		buf.append("</font></td>").append("\n");
		
		// print end of row
		buf.append("</tr>\n");

		// recursive call
		if (!rootMsg) {
    		if (childCount > 0)
    		    tree += TREE_LINE;
    		else
    		    tree += TREE_EMPTY;
		}
        int numChildren = walker.getChildCount(message); // keep
        if( numChildren > 0 ) {
            for( int i=0; i<numChildren; i++ ) {
                ForumMessage child = walker.getChild(message, i);
                printChildren( walker, child, thread, message.getID(), ++level, numChildren - i -1, buf, messageID, forumID, lineNr, tree, lastVisited, forumParams);
				level--;
            }
        }
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

<%	// get parameter values
	int 	parentID  	= ParamUtils.getIntParameter(request, "parent", -1);
	int 	threadID  	= ParamUtils.getIntParameter(request, "thread", -1);
	int 	messageID 	= ParamUtils.getIntParameter(request, "message", -1);
	int 	forumID   	= ParamUtils.getIntParameter(request, "forum", -1);
	String  forumParams = ParamUtils.getParameter(request, "forumparams");
	String  forumParamsEncoded = (forumParams == null) ? "" : "&forumparams=" + URLEncoder.encode(forumParams);
	long	lastVisited = SkinUtils.getLastVisited(request, response);
	int 	nextID  	= -1;
	int 	previousID  = -1;
%>

<%	// Get a ForumFactory object
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);

	// Get a forum object, redirect on error:
	Forum forum = null;
	try {
		forum = forumFactory.getForum(forumID);
	}
	catch( UnauthorizedException ue ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("You don't have access to use this forum") );
		return;
	}
	catch( ForumNotFoundException fnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("Can't view a forum that doesn't exist.") );
		return;
	}
%>

<%	
	// Get the thread, then message
	ForumThread thread = null;
	int threadChildCount = 0;
	try {
		thread = forum.getThread(threadID);
		threadChildCount = thread.getMessageCount()-1;
	}
	catch( ForumThreadNotFoundException tnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("The thread does not exist") );
		return;
	}
	
	ForumMessage message = null;
	try {
		if (messageID < 0) {
			message = thread.getRootMessage();
		}
		else {
			message = thread.getMessage(messageID);
		}
	}
	catch( ForumMessageNotFoundException mnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("The message does not exist") );
		return;
	}

	//Get the TreeWalker	
	TreeWalker walker = thread.treeWalker();
	ForumMessage root = walker.getRoot();
	int numRecursiveChildren = walker.getRecursiveChildCount(message);
		
	ForumMessage parent = null;

	if (parentID != -1) {
		parent = thread.getMessage(parentID);
		
		int numParentsChildren = walker.getChildCount(parent);
	
		//Get the previous and next messages in the thread.
		int currentChildNum = walker.getIndexOfChild(parent,message);
		
		if (currentChildNum >= numParentsChildren-1) {
			nextID = -1;
		}
		else {
			nextID = walker.getChild(parent,currentChildNum + 1).getID();	
		} 
		if (currentChildNum > 0) {
			previousID = walker.getChild(parent,currentChildNum - 1).getID();
		}
	}
%>

<%	/////////////////
	// header include
	
	String title = (message == null) ? "Message not found" : message.getSubject();
%>
<%@	include file="header.jsp" %>

<%-- begin breadcrumbs --%>
<font face="verdana" size="2"><b><a href="index.jsp" class="normal">Home</a>
&gt;
<a href="viewForum.jsp?forum=<%= forumID %><%= forumParams == null ? "" : forumParams %>" class="normal"><%= forum.getName() %></a>
&gt;
<%= thread == null ? "" : thread.getName() %>
</font>
<%-- end breadcrumbs --%>
<p>

<%-- Message table --%>
<table bgcolor="#666666" cellpadding=1 cellspacing=0 border=0 width="100%">
<tr>
<td>
	<table bgcolor="#dddddd" cellpadding=3 cellspacing=0 border=0 width="100%">
	<td bgcolor="#dddddd" width="99%">
		<%	
			java.util.Date date = message.getModifiedDate(); 
			String subj         = message.getSubject();
			boolean canEdit     = false;
			boolean canReply    = message.hasPermission(ForumPermissions.CREATE_MESSAGE);

			if( subj == null || subj.equals("") ) {
				subj = "[no subject]";
			}
		%>
		<font face="Trebuchet MS">
		<b><%= subj %></b>
		</font>
		<br>
		<font face="verdana" size=2>
		<b>
		<%
			if( !message.isAnonymous() ) { 
				User user = message.getUser();
				String email = user.getEmail();
				String name = user.getName();
				if (name == null)
				    name = user.getUsername();

            	canEdit = canReply && (user.getID() == authToken.getUserID() &&
            	           System.currentTimeMillis() < date.getTime() + 2 * 3600 * 1000);

				if (email != null) {
		%>
					<a href="mailto:<%= email %>" class="normal"><%= name %></a>
		<%		} else { %>
					<%= name %>
		<%		}
			} else { %>
			 [Anonymous]
		<%	} %>
		</b>
		</font>
	</td>
	<td bgcolor="#dddddd" width="1%" nowrap>
		<font face="verdana" size=1>
		<b>
		<% 	java.util.Date d = message.getCreationDate(); %>
		<%= dateFormatter.format(d) %> - <%= timeFormatter.format(d) %>
		</b>
		</font>
	</td>
	</table>
	<table bgcolor="#666666" cellpadding=0 cellspacing=0 border=0 width="100%">
	<td><img src="images/blank.gif" width=1 height=1 border=0></td>
	</table>
	<table bgcolor="#eeeeee" cellpadding=3 cellspacing=0 border=0 width="100%">
	<td align="center">
		<%	if (canEdit) { %>
    		<a href="post.jsp?message=<%= messageID %>&thread=<%= threadID %>&forum=<%= forumID %><%= forumParamsEncoded %>&postType=edit"><img src="images/edit_button.gif" width=50 height=16 border="0"></a> &nbsp;
		<%	} %>
		<%	if (canReply) { %>
			<a href="post.jsp?message=<%= messageID %>&thread=<%= threadID %>&forum=<%= forumID %><%= forumParamsEncoded %>&postType=reply"><img src="images/reply_button.gif" width=50 height=16 border="0"></a><br>
		<%	} else { %>
			&nbsp;
		<%	} %>
	</td>
	</table>
	<table bgcolor="#666666" cellpadding=0 cellspacing=0 border=0 width="100%">
	<td><img src="images/blank.gif" width=1 height=1 border=0></td>
	</table>
	<table bgcolor="#ffffff" cellpadding=5 cellspacing=0 border=0 width="100%">
	<td>
		<%= message.getBody() %>
<%	if( !message.isAnonymous() ) {
		User user = message.getUser();
		String sig = (String)user.getProperty("sig");
%>
<pre>
<%= (sig!=null)?sig:"" %></pre>
<%	} %>
	</td>
	</table>
</td>
</tr>
</table>
<%-- Message table --%>

<p>

<font face="tahoma" size="2"><b>
<%	if( numRecursiveChildren == 0 ) { %>
	No replies to this message.
<% 	} else { %>
	<a href="#currentMsg"><%= numRecursiveChildren %> 
	<%= (numRecursiveChildren>1)?" replies":" reply" %></a> to this message.
<%	} %>
</b>
<%	if (canReply) { %>
	[<a href="post.jsp?message=<%= messageID %>&thread=<%= threadID %>&forum=<%= forumID %><%= forumParamsEncoded %>&postType=reply" class="normal">Add your own</a>]
<%	} %>
</font>

<p>

<table bgcolor="#999999" cellpadding=0 cellspacing=0 border=0 width="100%">
<td>
<table bgcolor="#999999" cellpadding=0 cellspacing=1 border=0 width="100%">
<%	StringBuffer buf = new StringBuffer();
	int level = 0;
	int[] lineNr = { 0 };
	String tree = "";
	printChildren( walker, root, thread, -1, level, -1,  buf, messageID, forumID, lineNr, tree, lastVisited, forumParamsEncoded);  
	out.println( buf.toString() );
%>
</table>
</td>
</table>

<br>
<%@	include file="footer.jsp" %>

