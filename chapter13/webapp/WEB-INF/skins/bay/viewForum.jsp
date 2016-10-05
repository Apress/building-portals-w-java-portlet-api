
<%
/**
 *	$RCSfile: viewForum.jsp,v $
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

<%!	// Global variables
	//final SimpleDateFormat formatter = new SimpleDateFormat( "EE, MMM d" );
	final SimpleDateFormat formatter = new SimpleDateFormat( "EE, MMM d yyyy 'at' H:mm" );
	final SimpleDateFormat timeFormatter = new SimpleDateFormat( "h:mm a" );
	final SimpleDateFormat dayFormatter = new SimpleDateFormat( "EEEE" );
	final long ONE_DAY = 1000L * 60 * 60 * 24;
	final long ONE_WEEK = ONE_DAY * 7;
	final int DEFAULT_MSG_RANGE = 15;
	
	// Method to print one line on the thread table
	private String displayMessage(int forumID, ForumThread thread, ForumMessage message, String forumParams, long now, long lastVisited) {
		StringBuffer buf = new StringBuffer();
		if( message != null ) {
			Date messageDate = thread.getModifiedDate();
			long messageDateMS = messageDate.getTime(); 
			String subject = message.getSubject();
			int messageID = message.getID();
			String username = null;

			if( !message.isAnonymous() ) {
				User user = message.getUser();
				username = user.getName();
				if (username == null)
				    username = user.getUsername();
			}
			if (username == null)
			    username = "<i>Anonymous</i>";
			int threadID = thread.getID();
			int numReplies = thread.getMessageCount()-1;
			
			if( subject == null || subject.equals("") ) {
				subject = "[no subject]";
			}
			String dateText = "";
			
			if (messageDateMS >= (now - 2 * ONE_DAY)) {
				dateText = SkinUtils.dateToText(messageDate);
			}
			else {
				dateText = formatter.format(messageDate);
			}

			// new flag
			buf.append("<td width='1%'>");
			buf.append(messageDateMS > lastVisited ? "<img src=\"images/new.gif\">" : "&nbsp;");
			buf.append("</td>");

			// subject cell
			buf.append("<td width='96%'><font face='verdana' size='2'>");
			buf.append("<a href='viewMessage.jsp?message=").append(messageID).append("&thread=");
			buf.append(threadID).append("&forum=").append(forumID);
			if (forumParams.length() > 0)
				buf.append("&forumparams=").append(forumParams);
			buf.append("'><b>").append(subject);
			buf.append("</b></a>");
			//buf.append("&nbsp;[").append(numReplies).append("]");
			buf.append("</font></td>").append("\n");

			// replies cell
			buf.append("<td width='1%' align='center'>");
			buf.append("<font size='-1' color='#666666' face='verdana' size='2'>");
			buf.append("[");
			buf.append("<a href='viewMessage.jsp?message=").append(messageID).append("&thread=");
			buf.append(threadID).append("&forum=").append(forumID);
			if (forumParams.length() > 0)
				buf.append("&forumparams=").append(forumParams);			
			buf.append("'><b>").append(numReplies);
			buf.append("</b></a>");
			buf.append("]");
			buf.append("</font></td>");
			
			// username cell
			buf.append("<td width='1%' nowrap align='center'>");
			buf.append("<font size='-1' color='#666666' face='verdana' size='2'>");
			buf.append(username);
			buf.append("</font></td>");
			
			// date cell
			if( messageDateMS >= (now-ONE_WEEK) ) {
				buf.append("<td width='1%' nowrap class='dateTimeListToday' align='center'>");
			} else { 
				buf.append("<td width='1%' nowrap class='dateTimeList' align='center'>");
			}
			buf.append("<font face='verdana' size='2'>");
			buf.append( dateText );
			buf.append("</font></font>");
			buf.append("</td>").append("\n");

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

<%	// get parameters
	int 	forumID = 	ParamUtils.getIntParameter(request, "forum", -1);
	int 	range = 	ParamUtils.getIntParameter(request, "range", DEFAULT_MSG_RANGE);
	int 	start = 	ParamUtils.getIntParameter(request, "start", 0);
	long	lastVisited = SkinUtils.getLastVisited(request, response);
	String	startParam =  (start == 0) ? "" : "&start=" + start;
	String	rangeParam =  (range == DEFAULT_MSG_RANGE) ? "" : "&range=" + range;
%>

<%	ForumThread thread = null;
	Forum forum = null;
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	try {
		forum = forumFactory.getForum(forumID); // throws ForumNotFoundException
	}
	catch( UnauthorizedException ue ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("You don't have access to view this forum.") );
		return;
	}
	catch( ForumNotFoundException fnfe ) {
		response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("Can't view a forum that doesn't exist.") );
		return;
	}

	String 	 forumName = forum.getName();
	Iterator forumIterator = forumFactory.forums();
%>

<%	/////////////////
	// header include
	
	String title = forumName;
%>
<%@	include file="header.jsp" %>

<%-- begin breadcrumbs --%>
<form>
<font face="verdana" size="2"><b><a href="index.jsp" class="normal">Home</a>
&gt;</b>
<font size=2><b>
<select name="forumName" size=1 class="breadcrumbBox" onchange="location=this.options[this.selectedIndex].value;">
<option value="" selected><%= forumName %>
<option value="viewForum.jsp?forum=<%= forumID %>">-----------------
<%	while (forumIterator.hasNext()) {
		Forum tmpForum = (Forum)forumIterator.next();
		String name = tmpForum.getName();
		String location = "viewForum.jsp?forum=" + tmpForum.getID();
%>
		<option value="<%= location %>">&gt; <%= name %>
<%	}
%>
</select>
</b></font>
</font>
</form>
<%-- end breadcrubms --%>

<%	Iterator it = forum.threads(start,range);
	if( !it.hasNext() ) {
%>
		<ul>
		<font face="verdana" size=2><b>
		There are no messages in this forum.
		</b><br>
		<a href="post.jsp?forum=<%= forumID %>" class="normal"><i>Add your own message.</i></a>
		</font>
		</ul>
		
<%	} else { %>

	<table bgcolor="#999999" cellpadding=0 cellspacing=0 border=0 width="100%">
	<td>
	<table bgcolor="#999999" cellpadding=3 cellspacing=1 border=0 width="100%">
	<tr bgcolor="#dddddd">
		<td width="1%"> &nbsp; </td>
		<td align="center" width="96%"><font size="1" face="tahoma,arial,helvetica">subject</font></td>
		<td align="center" width="1%" nowrap><font size="1" face="tahoma,arial,helvetica">replies</font></td>
		<td align="center" width="1%" nowrap><font size="1" face="tahoma,arial,helvetica">posted by</font></td>
		<td align="center" width="1%" nowrap><img src="images/arrow-up.gif" width=8 height=7 border=0 hspace=6><font size="1" face="tahoma,arial,helvetica">date</font></td>
	</tr>
<%		long now = (new java.util.Date()).getTime();
		ForumMessage root =  null;
		int rowColor = 0;
		String bgcolor = "";
		String forumParams = URLEncoder.encode(startParam + rangeParam);
		int numThreadInForum = forum.getThreadCount();
		
		while(it.hasNext()) {
			rowColor++;
			thread = (ForumThread)it.next();
			root = thread.getRootMessage();
			if( rowColor%2 == 0 ) {
				bgcolor = "#ffffff";
			} else {
				bgcolor = "#eeeeee";
			}
%>
			<tr bgcolor="<%= bgcolor %>">
			<%= displayMessage(forumID, thread, root, forumParams, now, lastVisited) %>
			</tr>
<%		} %>
	</table>
	</td>
	</table>
	
<table cellpadding=0 cellspacing=0 border=0 width="100%">
		<td width="1%" nowrap>
			<font size="-2" face="verdana,arial">
			<br>
			<%	if( (start-range) >= 0 ) { %>
				<a href="viewForum.jsp?forum=<%= forumID %><%= rangeParam %>&start=<%= (start-range) %>"><b>&lt;&lt;</b> previous <%= range %> messages</a>
			<%	} %>
			</font>
		</td>
		<td width="98%" nowrap>
		
		</td>
		<td width="1%" nowrap>
			<font size="-2" face="verdana,arial">
			<br>
			<%	if( (range+start) < numThreadInForum ) { %>
				<a href="viewForum.jsp?forum=<%= forumID %><%= rangeParam %>&start=<%= (start+range) %>">next <%= (((numThreadInForum-range)<=range)?(numThreadInForum-range):range) %> messages <b>&gt;&gt;</b></a>
			<%	} %>
			</font>
		</td>
		</table>
	
<%	} %>

<%@	include file="footer.jsp" %>

