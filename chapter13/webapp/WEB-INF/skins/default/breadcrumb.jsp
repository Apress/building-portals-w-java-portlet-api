
<%
/**
 *	$RCSfile: breadcrumb.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%!	///////////////////////
	// breadcrumb variables
	
	// change these values to customize the look of your breadcrumb bar
	
	// Colors
	final static String crumbBgcolor = "#999999";
	final static String crumbFgcolor = "#ccccdd";
%>

<table bgcolor="<%= crumbBgcolor %>" width="100%" border="0" cellspacing="0" cellpadding="1">
<td><table bgcolor="<%= crumbFgcolor %>" width="100%" border="0" cellspacing="0" cellpadding="3">
<td width="99%">
<%	// don't show any breadcrumbs if the breadcrumbs array is null or
	// zero length
	if( breadcrumbs != null && breadcrumbs.length > 0 ) {
		// loop through the breadcrumbs array, only display the first
		// n-1 items (the last one will be displayed as a non-link 
		// because that should represent the page we're on.
		int i=0;
		while( breadcrumbs.length > 1 && i < (breadcrumbs.length-1) ) { 
%>
	<a class="crumb" href="<%= breadcrumbs[i][1] %>"><%= breadcrumbs[i][0] %></a>
	&nbsp;&gt;&nbsp;
<%			i++;
		}
		if( breadcrumbs.length >= 1 ) {
%>
	<%= breadcrumbs[i][0] %>
	
<%	// end of while loop
	}
%>
</td>
<%	// grab the username to display who we are logged in as. If the
	// authentication token is null, that means a guest is viewing the page,
	// so display "guest" instead of a username:
	
	boolean loggedIn = authToken != null && !user.isAnonymous();
	String username = "<i>guest</i>";
	if( loggedIn ) {
		username = user.getUsername();
	}
%>
<td width="1%" nowrap>
<%	if( loggedIn ) { %>
	Logged in as: <%= username %>
<%	} else { %>
	<i>Not logged in. <a href="login.jsp">Login in or create an account.</a></i>
<%	} %>
</td>
</table></td></table>

<%	} %>

