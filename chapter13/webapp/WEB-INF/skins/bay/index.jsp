
<%
/**
 *	$RCSfile: index.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:06 $
 */
%>

<%@ page 
	import="java.util.*,
	        java.text.*,
			com.Yasna.forum.*,
			com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%	// get parameters
	boolean logout = 		ParamUtils.getBooleanParameter(request, "logout");
	int 	forumID = 		ParamUtils.getIntParameter(request, "forum", -1);
	long	lastVisited = 	SkinUtils.getLastVisited(request, response);
%>

<%	// logout if requested:
	if (logout) {
		SkinUtils.removeUserAuthorization(request, response);
		response.sendRedirect("index.jsp");
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

<%	// get a forum factory and forum iterator

	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	Iterator forumIterator = forumFactory.forums();
%>

<%	/////////////////
	// header include
	
	String title = "Yazd: Bay skin";
%>
<%@	include file="header.jsp" %>

<p>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td width="1%"><img src="images/blank.gif" width=30 height=1 border=0></td>
<td width="98%" valign="top">
	<%-- begin main content --%>

	<font face="verdana">
	<b>Yazd example skin</b>
	</font>
	<p>
	
<%	// check to see if there are no forums
	if( !forumIterator.hasNext() ) {
%>
		<ul>
			No forums in the system, or you don't have permission to view forums.
		</ul>

<%	} else { %>
		<table border="0" cellpadding="3" cellspacing="0">
<% 		while( forumIterator.hasNext() ) {
				Forum 	forum 		= (Forum)forumIterator.next();
				int 	id		 	= forum.getID();
				String 	name 		= forum.getName();
				String 	description	= forum.getDescription();
				int 	numThreads 	= forum.getThreadCount();
				int 	numMessages = forum.getMessageCount();
				long 	lastModified= forum.getModifiedDate().getTime();
%>
			<tr>
			<td width="1%" nowrap>
				<%	if (lastModified > lastVisited) { %>
					<img src="images/new.gif">
				<%	} else { %>
					&#149;
				<%	} %>
			</td>
			<td width="99%">
				<font face="verdana" size=2>
				<b><a href="viewForum.jsp?forum=<%= id %>" class="normal">
					<%= name %>
				</a></b>
				<i>(<%= description %>)</i>
				</font>
				<br>
				<font face="verdana" size=1>
				(<%= numThreads %>  <%= (numThreads==1)  ? " thread"  : " threads" %>,
				 <%= numMessages %> <%= (numMessages==1) ? " message" : " messages" %>)
				</font>
			</td>
			</tr>
<%		} %>
		</table>
<%	} %>
	
	<%-- end main content --%>
</td>

<%-- recent dicussions --%>
<td valign="top" width="1%">
	<table bgcolor="#999999" cellpadding="1" cellspacing="0" border="0" width="220">
	<td>
		<jsp:include page="recentMessages.jsp" flush="true"/>
	</td>
	</table>
</td>
<%-- end recent dicussions --%>

</tr>
</table>

<p>

<%@	include file="footer.jsp" %>
