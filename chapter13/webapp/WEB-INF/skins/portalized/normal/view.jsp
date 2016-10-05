<%@	page import="java.util.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>
<%@ taglib uri="portalbook.tld" prefix="pb" %>

<%!	//////////////////////////////////
	// customize the look of this page
	
	// Colors of the table that displays a list of forums
	final static String forumTableBgcolor = "#cccccc";
	final static String forumTableFgcolor = "#ffffff";
	final static String forumTableHeaderFgcolor = "#eeeeee";
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

<%
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);

	User user = forumFactory.getProfileManager().getUser(authToken.getUserID());
	long userLastVisitedTime = SkinUtils.getLastVisited(request,response);
%>

<table bgcolor="<%= forumTableBgcolor %>" cellpadding="0" cellspacing="0" border="0" width="100%">
<td>
<table bgcolor="<%= forumTableBgcolor %>" cellpadding="4" cellspacing="1" border="0" width="100%">
<tr bgcolor="<%= forumTableHeaderFgcolor %>">
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


<%
	Iterator forumIterator = forumFactory.forums();

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

    java.text.DateFormat df = 
    	java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);

	boolean forumLoaded = false;
	
	int forumCount = 0;
	while( forumIterator.hasNext() ) {
		Forum forum = (Forum)forumIterator.next();
		forumLoaded = true;
		int forumID = forum.getID();
		String forumName = forum.getName();
		String forumDescription = forum.getDescription();
		int threadCount = forum.getThreadCount();
		int messageCount = forum.getMessageCount();
		String creationDate = df.format(forum.getCreationDate());
		String modifiedDate = df.format(forum.getModifiedDate());		
%>
    <pb:url state="NORMAL" mode="VIEW" var="link">viewForum.jsp?forum=<%=forumID%></pb:url>
	<tr bgcolor="<%= forumTableFgcolor %>">
		<td nowrap><a href="<%=link%>" class="forum"><%= forumName %></a></td>
		<td align="center" nowrap><%= threadCount %> / <%= messageCount %></td>
		<td><i><%= (forumDescription!=null)?forumDescription:"&nbsp;" %></i></td>
		<td nowrap align="center"><small class="date"><%= modifiedDate %></small></td>
	</tr>
<%
       // Retrieve the forum count preference, and 
       // compare against that (5 is the default)...
       String count = ((javax.portlet.RenderRequest)pageContext.getRequest()).getPreferences().getValue("ForumCount","5");
       int c = Integer.parseInt(count);

       if( ++forumCount > (c-1) ) {
%>
    <pb:url state="NORMAL" mode="VIEW" var="more">index.jsp</pb:url>
   	<tr bgcolor="<%= forumTableFgcolor %>">
		<td colspan="5" align="center">
           <i><a href="<%=more%>">More...</a></i>
		</td>
	</tr>
<%
          break;
       }
	}
	
	// if no forums were successfully loaded, print out a "error, no 
	// permissions" message
	if( !forumLoaded ) {
%>
   	<tr bgcolor="<%= forumTableFgcolor %>">
		<td colspan="5" align="center">
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
