
<%
/**
 *	$RCSfile: header.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:06 $
 */
%>

<%@ page
	import="com.Yasna.forum.*,
			 com.Yasna.forum.util.*,
			 com.Yasna.util.*"
%>

<%	// prepend "header" to variables used here to prevent clashes with including pages...
	String		siteTitle = "CoolServlets";
	String		pageTitle = (title == null || title.length() == 0) ? siteTitle : title + " - " + siteTitle;
	String 		headerUserName = null;
	boolean		headerCanPost = false;
	if (forumID > -1) {
		try {
			headerCanPost = forumFactory.getForum(forumID).hasPermission(ForumPermissions.CREATE_THREAD);
		} catch (Exception ignoreFnF) {}
	}
	if (authToken != null && forumFactory != null) {
		try {
			ProfileManager headerProfileManager = forumFactory.getProfileManager();
			User headerUser = (headerProfileManager == null) ? null : headerProfileManager.getUser(authToken.getUserID());
			if (headerUser != null && !headerUser.isAnonymous()) {
				headerUserName = headerUser.getName();
				if (headerUserName == null || headerUserName.trim().length() == 0) {
					headerUserName = headerUser.getUsername();
				}
			}
		} catch (Exception ignore) {}
	}
%>

<html>
<head>
	<title><%= title %></title>
	<link rel="stylesheet" href="style/global.css">
</head>
<body bgcolor="#ffffff" text="#000000" link="#0033cc" vlink="#663366" alink="#ff3300">

<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td width="1%">
	<a href="index.jsp"><img src="images/Yazdheader.gif" alt="Yazd" border="0"></a>
</td>
<td width="1%">
	&nbsp;
</td>

<%	if (headerUserName != null) { %>
	<td width="1%" nowrap align="center">
		<a href="account.jsp?mode=2"><b><%= headerUserName %></b></a>
	</td>
<%	} %>

<%-- Spacer --%>
<%	if (forumID > 0) { %>
	<td width="90%">&nbsp;</td>
<%	} else { %>
	<td width="94%">&nbsp;</td>
<%	} %>

<%	if (headerUserName != null) { %>
	<td width="1%" nowrap align="center">
		<a href="index.jsp?logout=true" class="normal">
			Logout
		</a>
	</td>
<%	} else { %>
	<td width="1%" nowrap align="center">
		<a href="account.jsp" class="normal">
			Login
		</a>
	</td>
<%	} %>
<%	if (forumID > 0) { %>
	<td width="1%" nowrap align="center">
		&nbsp;
		<img src="images/bluedot.gif" width=1 height=25 border=0>
		&nbsp;
	</td>
	<td width="1%" nowrap>
		<a href="viewForum.jsp?forum=<%= forumID %>"
			><img src="images/read_tb.gif" width=71 height=30 alt="Read messages" border="0"
		></a>
	</td>
<%	if (headerCanPost) { %>
	<td width="1%" nowrap>
		<img src="images/vertLine.gif" width=1 height=30 border=0 hspace=4>
	</td>
	<td width="1%" nowrap>
		<a href="post.jsp?forum=<%= forumID %>"
			><img src="images/post_tb.gif" width=62 height=30 alt="Post a message" border="0"
		></a>
	</td>
<%	} %>
	<td width="1%" nowrap>
		<img src="images/vertLine.gif" width=1 height=30 border=0 hspace=4>
	</td>
	<td width="1%" nowrap>
		<a href="search.jsp?forum=<%= forumID %>"
			><img src="images/search_tb_left.gif" height=30 alt="Search" border="0"
			><img src="images/search_tb_right.gif" height=30 border="0"
		></a>
	</td>
<%	} else { %>
	<% if( forumID > -1 ) { %>
	<td width="1%" nowrap align="center">
		&nbsp;
		<img src="images/bluedot.gif" width=1 height=25 border=0>
		&nbsp;
	</td>
	<td width="1%" nowrap>
		<a href="search.jsp?forum=-1"
			><img src="images/search_tb_left.gif" height=30 alt="Search" border="0"
			><img src="images/search_tb_right.gif" height=30 border="0"
		></a>
	</td>
	<% } %>
<%	} %>

</tr>
</table>

<hr size="0">
<br>
