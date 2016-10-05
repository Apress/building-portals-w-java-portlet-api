
<%
/**
 *	$RCSfile: recentMessages.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:07 $
 */
%>

<%@ page import="java.util.*,
				 java.text.*,
				 com.Yasna.forum.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.util.*" %>

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

<%!
	// List threads of the last 31 days
	final static long MAX_AGE 	= 1000L * 60 * 60 * 24 * 31;
	// At most, 8 threads in the list
	final static int MAX_MSGS	= 8;
	// At most, 3 messages per forum
	final static int MAX_MSGS_PER_FORUM = 3;
%>

<%
	// Message age cutoff timestamp
	long 	boundary = System.currentTimeMillis() - MAX_AGE;
	// Remember to declare the table tags
	boolean	needHeader = true;
	
	// titles of the most recent threads
	String[] titles = new String[MAX_MSGS];
	String title;
	// age, forumId, threadID, msgID of most recent threads
	long[][] msgs = new long[MAX_MSGS][4];
	long[]	msg;
	int 	n;
	int 	nr;
	long 	age;
	
	// Loop through all the forums
	Iterator allForums = ForumFactory.getInstance(authToken).forums();
	while (allForums.hasNext()) {
		// per forum, loop through the first x recent threads
		Forum f = (Forum)allForums.next();
		Iterator fMsgs = f.threads();
		for (nr = 0; nr < MAX_MSGS_PER_FORUM && fMsgs.hasNext() ; ) {
			// if this thread is newer than what we have until now
			ForumThread t = (ForumThread)fMsgs.next();
			age = t.getModifiedDate().getTime();
			if (age > boundary && age > msgs[0][0]) {
				// gets its vitals and sort it into the list of recent msgs
				Iterator ms = t.messages(t.getMessageCount()-1, 1);
				ForumMessage m = (ForumMessage)ms.next();
				title = m.getSubject() + "</a> &nbsp; (" + t.getMessageCount() + ")";
				titles[0] = title;
				msg = msgs[0];
				msg[0] = age; msg[1] = f.getID(); msg[2] = t.getID(); msg[3] = m.getID();
				for (n = 1; n < MAX_MSGS && age > msgs[n][0]; n++) {
					titles[n-1] = titles[n];
					titles[n] = title;
					msgs[n-1] = msgs[n];
					msgs[n] = msg;
				}
				nr++;
			}
		}
	}
	
	// if we found any msgs, output them into a table
	for (n = MAX_MSGS -1; n >= 0 ; n--) {
		if (msgs[n][0] > 0) {
			if (needHeader) {
				needHeader = false;
				%>
				<table bgcolor="#eeeeee" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr bgcolor="#cccccc">
				<td align="center" colspan="2">
					<b>Recent Discussions</b>
				</td>
				</tr>
				<%
			}		
			%>
				<tr>
				<td width="1%" valign="top" align="center"><font size="-1">&#149;</font></td>
				<td width="99%">
				<font size="-1">
				<a href="viewMessage.jsp?message=<%= msgs[n][3] %>&thread=<%= msgs[n][2] %>&forum=<%= msgs[n][1] %>&parent=-1"><%= titles[n] %>
				</font>
				</td>
				</tr>
			<%
		}
	}

	if (!needHeader) {
		%> </table> <%
	}
%>

