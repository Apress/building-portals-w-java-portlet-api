
<%
/**
 *	$RCSfile: search.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:07 $
 */
%>

<%@	page 
	import="java.util.*,
			java.text.*,
			java.net.*,
            com.Yasna.forum.*,
            com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	// Global variables
	final static SimpleDateFormat formatter = new SimpleDateFormat( "EE, MMM d yyyy 'at' h:mm:ss a" );
	final static long ONE_DAY = 1000 * 60 * 60 * 24;
	final static long ONE_WEEK = ONE_DAY * 7;
	final static int DEFAULT_RANGE = 15;
	
	// method to print one table line for a message.
	private String displayMessage(int forumID, ForumMessage message, long now) {
		StringBuffer buf = new StringBuffer();
		if (message != null) {
			java.util.Date messageDate = message.getCreationDate();
			long messageDateMS = messageDate.getTime(); 
			String subject = message.getSubject();
			int messageID = message.getID();
			String username = null;
			if (!message.isAnonymous()) {
				try {
					User user = message.getUser();
					username = user.getName();
					if (username == null)
					    username = user.getUsername();
				} catch (Exception ignore) {
					username = "<i>Somebody</i>";
				}
			}
			if (username == null)
			    username = "<i>Anonymous</i>";
			int threadID = message.getForumThread().getID();
			
			if( subject == null || subject.equals("") ) {
				subject = "[no subject]";
			}
			String dateText = "";
			
			dateText = formatter.format(messageDate);
			
			// subject cell
			buf.append("<td width='97%'><font face='verdana' size='2'>");
			buf.append("<a href='viewMessage.jsp?message=").append(messageID).append("&thread=");
			buf.append(threadID).append("&forum=").append(forumID);
			buf.append("&parent=-1").append("'>");
			buf.append("<b>").append(subject).append("</b>");
			buf.append("</a>");
			buf.append("</font></td>").append("\n");
			
			// username cell
			buf.append("<td width='1%' nowrap align='center'>");
			buf.append("<font size='-1' color='#666666' face='verdana' size='2'>");
			buf.append(username);
			buf.append("</font></td>");
			
			// date cell
			if( messageDateMS >= (now-ONE_DAY) ) {
				buf.append("<td width='1%' nowrap class='dateTimeListToday'>");
			} else { 
				buf.append("<td width='1%' nowrap class='dateTimeList'>");
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
	
	
	/////////////////
	// Get parameters
	
	int forumID      = ParamUtils.getIntParameter(request,"forum",-1);
	boolean doSearch = ParamUtils.getBooleanParameter(request,"doSearch");
	String queryText = ParamUtils.getParameter(request,"q");
	int range        = ParamUtils.getIntParameter(request,"range",DEFAULT_RANGE);
	int start        = ParamUtils.getIntParameter(request,"start",0);
	
	
	///////////////////
	// perform a search
	
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	Forum forum = forumFactory.getForum(forumID);
	int nextStart = start;
	
	Query query = null;
	Iterator searchResults = null;
	if (doSearch && queryText != null) {
		if( forum != null ) {
			query = forum.createQuery();
		}
		query.setQueryString(queryText);
		// use the +1 to see if there are more results...
		searchResults = query.results(start, range +1);
	}
%>

<%	/////////////////
	// header include
	
	String title = "Search";
%>
<%@	include file="header.jsp" %>

<%-- begin breadcrumbs --%>
<font face="verdana" size="-1"><b><a href="index.jsp" class="normal">Home</a>
<%	if (forumID > 0) { %>
&gt;
<a href="viewForum.jsp?forum=<%= forumID %>" class="normal"><%= forum.getName() %></a>
<%	} %>
&gt;
<%	if (doSearch) { %>
	Search results
<%	} else { %>
	Search
<%	} %>
</b></font>
<%-- end breadcrubms --%>

<p>

<center>
<table>
<tr>
<td>
<form action="search.jsp" name="searchForm">
<input type="hidden" name="doSearch" value="true">
<input type="hidden" name="forum" value="<%= forumID %>">
	<input type="text" name="q" value="<%= doSearch && queryText != null ? queryText : "" %>" size="40" maxlength="100">
	<input type="submit" value="Search">
</form>
</td>
</tr>
</table>
</center>
<script language="JavaScript" type="text/javascript">
<!--
	document.searchForm.q.focus();
//-->
</script>

<%	if (doSearch) { %>

	<p>
	<br>
	<p>
	
	<%	if( query == null || searchResults == null || !searchResults.hasNext() ) { %>
	
		<center><i>No results. Try again.</i></center>
	
	<%	} else { %>

		<table bgcolor="#999999" cellpadding=0 cellspacing=0 border=0 width="100%">
		<td>
		<table bgcolor="#999999" cellpadding=3 cellspacing=1 border=0 width="100%">
		<tr bgcolor="#dddddd">
			<td align="center" width="98%"><font size="1" face="tahoma,arial,helvetica">subject</font></td>
			<td align="center" width="1%" nowrap><font size="1" face="tahoma,arial,helvetica">posted by</font></td>
			<td align="center" width="1%" nowrap><font size="1" face="tahoma,arial,helvetica">date</font></td>
		</tr>

		<%
			ForumFactory fact = ForumFactory.getInstance(authToken);
			long 		now = (new java.util.Date()).getTime();
			String 	  	bgColor = null;
			int 	  	nrRows = 0;
			int		  	nrResults = 0;

			while (nrResults++ < range && searchResults.hasNext()) { 
				ForumMessage message = (ForumMessage)searchResults.next();
				int          msgForumID = message.getForumThread().getForum().getID();

				bgColor = (++nrRows % 2 == 0 ) ? "#ffffff": "#eeeeee";
		%>  
				<tr bgcolor="<%= bgColor %>">
				<%= displayMessage(msgForumID, message, now) %>
				</tr>
		<%
			}
		%>

		</table>
		</td>
		</table>

	<%	} %>

	<table cellpadding=0 cellspacing=0 border=0 width="100%">
		<td width="1%" nowrap>
			<font size="-2" face="verdana,arial">
			<br>
			<%	String url = "search.jsp?doSearch=true&forum="+forumID+"&q="+(queryText == null ? "" : URLEncoder.encode(queryText))+"&range="+range;
				if( (start-range) >= 0 ) { %>
				<a href="<%= url %>&start=<%= (start-range) %>"><b>&lt;&lt;</b> previous results</a>
			<%	} %>
			</font>
		</td>
		<td width="98%" nowrap>
		
		</td>
		<td width="1%" nowrap>
			<font size="-2" face="verdana,arial">
			<br>
			<%	if (searchResults != null && searchResults.hasNext()) { %>
				<a href="<%= url %>&start=<%= (nextStart+range) %>">next results <b>&gt;&gt;</b></a>
			<%	} %>
			</font>
		</td>
	</table>

<%	} %>

<%@	include file="footer.jsp" %>

