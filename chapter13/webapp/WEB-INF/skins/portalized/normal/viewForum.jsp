<%@	page import="java.util.*,
                 java.text.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>
<%@ taglib uri="portalbook.tld" prefix="pb" %>


<%!	//////////////////////////////////
	// customize the look of this page
	
	// Colors of the table that displays a list of forums
	final static String threadTableBgcolor = "#bbbbbb";
	final static String threadTableFgcolor = "#ffffff";
	final static String threadTableHeaderFgcolor = "#eeeeee";
	final static String threadTableHiLiteColor = "#eeeeff";
	final static String threadPagingRowColor = "#dddddd";
%>

<%!	///////////////////
	// global variables
	
	// number of threads to display per page:
	private final int[] threadRange = { 15,25,50,100 };
	
	// default starting index and number of threads to display
	// per page (for paging)
	private final int DEFAULT_RANGE = 15;
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
	int forumID  = ParamUtils.getIntParameter(request,"forum",-1);
%>

<%	////////////////////
	// Load up the forum
	
	// If the forum is not found, a ForumNotFoundException will be thrown
	// and the user will be redirected to the error page
	Forum forum = forumFactory.getForum(forumID);
	
	// Get some properties of the forum
	String forumName  = forum.getName();
	int numThreads    = forum.getThreadCount();
	int numMessages   = forum.getMessageCount();
%>

<%	/////////////
	// Toolbar
	
	// The toolbar file looks for the following variables. To make a particular
	// "button" not appear, set a variable to null.
	boolean showToolbar = true;
	String viewLink = null;
	String postLink = "post.jsp?mode=new&forum="+forumID;
	String replyLink = null;
	// we can show a link to a user account if the user is logged in (handled
	// in the toolbar jsp)
	String accountLink = "userAccount.jsp";
%>
<%@ include file="toolbar.jsp" %>

<p>

<h2>
<%= forumName %>
</h2>

<h4>
Topics: <%= numThreads %>, Messages: <%= numMessages %>
</h4>

<%-- table for list of threads --%>
<table bgcolor="<%= threadTableBgcolor %>" cellpadding="0" cellspacing="0" border="0" width="100%">
<td><table bgcolor="<%= threadTableBgcolor %>" cellpadding="4" cellspacing="1" border="0" width="100%">
	<%-- paging row --%>
	<tr bgcolor="<%= threadPagingRowColor %>">
		<td colspan="5" width="99%">
		<%-- table for paging --%>
		<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<td width="1%" nowrap>

            <pb:url state="NORMAL" mode="VIEW" var="back">viewForum.jsp?forum=<%=forumID%>&start=<%= (start-range) %>&range=<%= range %></pb:url>			
	        <pb:url state="NORMAL" mode="VIEW" var="post">post.jsp?forum=<%=forumID%></pb:url>			
	        <pb:url state="NORMAL" mode="VIEW" var="next">viewForum.jsp?forum=<%=forumID%>&start=<%= (start+range) %>&range=<%= range %></pb:url>				

			<%	if( start > 0 ) { %>
				<small>
				<a href="<%=back%>"
				><img src="<pb:href path="images/prev.gif"/>" width="14" height="15" border="0"/></a>
				<a href="<%=back%>" class="toolbar"
				>Previous <%= range %> threads</a>
				<small>
			<%	} %>
				&nbsp;
			</td>
			<td width="98%" align="center">
				<a href="<%=post%>"
				><img src="<pb:href path="images/postnewmsg.gif"/>" width="97" height="15" alt="Post a new topic" border="0"/></a>
			</td>
			<td width="1%" nowrap>
				&nbsp;
			<%	if( numThreads > (start+range) ) { %>
			<%		int numRemaining = (numThreads-(start+range)); %>
				<small>
				<a href="<%=next%>" class="toolbar"
				>Next <%= (numRemaining>range)?range:numRemaining %> threads</a>
				<a href="<%=next%>"
				><img src="<pb:href path="images/next.gif"/>" width="14" height="15" border="0"/></a>
				</small>
			<%	} %>
			</td>
		</table>
		<%-- /table for paging --%>
		</td>
	</tr>
	<%-- /paging row --%>
	<tr bgcolor="<%= threadTableHeaderFgcolor %>">
		<td width="1%" nowrap><small>new</small></td>
		<td width="96%" align="center"><small>subject</small></td>
		<td width="1%" nowrap align="center"><small>replies</small></td>
		<td width="1%" nowrap align="center"><small>posted by</small></td>
		<td width="1%" nowrap align="center"><small>date posted</small></td>
	</tr>

<%	/////////////////////
	// get an iterator of threads
	Iterator threadIterator = forum.threads(start,range);
	
	if( !threadIterator.hasNext() ) {
%>
	<tr bgcolor="<%= threadTableFgcolor %>">
		<td colspan="5" align="center" class="forumListCell">
		<br>
		<i>
		<pb:url state="NORMAL" mode="VIEW" var="postTopic">post.jsp?mode=new&forum=<%=forumID%></pb:url>
		No topics in this forum. Try <a href="<%=postTopic%>">adding your own</a>.
		</i>
		<br><br>
		</td>
	</tr>
<%	}
	while( threadIterator.hasNext() ) {
		ForumThread thread = (ForumThread)threadIterator.next();
		int threadID = thread.getID();
		ForumMessage rootMessage = thread.getRootMessage();
		String threadName = rootMessage.getSubject();
		if( threadName == null ) {
			threadName = "<i>no subject</i>";
		}
		boolean rootMsgIsAnonymous = rootMessage.isAnonymous();
		User rootMessageUser = rootMessage.getUser();
		String username = rootMessageUser.getUsername();
		String name = rootMessageUser.getName();
		String email = rootMessageUser.getEmail();
		Date lastModified = thread.getModifiedDate();
		boolean wasModified = (userLastVisitedTime < lastModified.getTime());
%>
	<tr bgcolor="<%= threadTableFgcolor %>">
		<td width="1%" align="center">
		<%
		   String flagpath = "images/" + ( wasModified ? "bang" : "blank" ) + ".gif";
		%>
		<img src="<pb:href path="<%=flagpath%>"/>" width="8" height="8" border="0"/>
		</td>
        <pb:url state="NORMAL" mode="VIEW" var="viewThread">viewThread.jsp?forum=<%= forumID %>&thread=<%= threadID %></pb:url>				
		<td width="96%" onmouseover="this.bgColor='<%= threadTableHiLiteColor %>';this.style.cursor='hand';" onmouseout="this.bgColor='#ffffff';" onclick="location.href='<%= viewThread %>'">
			<a href="<%=viewThread%>" class="forum"><%= threadName %></a>
		</td>
		<td width="1%" nowrap align="center">
			<%= (thread.getMessageCount()-1) %>
		</td>
		<td width="1%" nowrap align="center">
		<%	if( rootMsgIsAnonymous ) { 
				String savedName = rootMessage.getProperty("name");
				String savedEmail = rootMessage.getProperty("email");
				String displayName = "<i>Anonymous</i>";
				if( savedName != null ) {
					displayName = "<i>" + savedName + "</i>";
				}
				if( savedEmail != null ) {
					displayName = "<a href=\"mailto:" + savedEmail + "\">" + displayName + "</a>";
				}
		%>
		    <%= displayName %>
		<%	} else { 
				boolean emailReadable = rootMessageUser.isEmailVisible();
				String displayName = username;
				if( emailReadable ) {
					displayName = "<a href=\"mailto:" + email + "\">" + displayName + "</a>";
				}
		%>
		    <b><%= displayName %></b>
		<%	} %>
	    </td>
		<td width="1%" nowrap align="right">
			<small class="date"><%= SkinUtils.dateToText( rootMessage.getCreationDate() ) %></small>
		</td>
	</tr>
<%	} %>

<%-- paging row --%>
<tr bgcolor="<%= threadPagingRowColor %>">
	<td class="forumListHeader" colspan="5" width="99%">
	<%-- table for paging --%>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<td width="1%" nowrap>
		<%	if( start > 0 ) { %>
			<small>
			<a href="<%=back%>"
			><img src="<pb:href path="images/prev.gif"/>" width="14" height="15" border="0"/></a>
			<a href="<%=back%>" class="toolbar"
			>Last <%= range %> threads</a>
			<small>
		<%	} %>
		&nbsp;
		</td>
		<td width="98%" align="center"></td>
		<td width="1%" nowrap>
		&nbsp;
		<%	if( numThreads > (start+range) ) { %>
		<%		int numRemaining = (numThreads-(start+range)); %>
			<small>
			<a href="<%=next%>" class="toolbar"
			>Next <%= (numRemaining>range)?range:numRemaining %> threads</a>
			<a href="<%=next%>"
			><img src="<pb:href path="images/next.gif"/>" width="14" height="15" border="0"/></a>
			</small>
		<%	} %>
		</td>
	</table>
	<%-- /table for paging --%>
	</td>
</tr>
<%-- /paging row --%>
	
</table>
</td>
</table>
