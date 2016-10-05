
<%
/**
 *	$RCSfile: error.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@ page isErrorPage="true" %>

<%@ page import="java.io.*,
                 java.util.*,
                 java.net.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"
%>

<%! /////////////
	// Debug mode will print out full error messages useful for an administrator or developer.
	// When debug is false, a generic error message more suitable for an end user is printed and
	// stack traces go to System.err.
	boolean debug = false; 
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
	// Header file include
	
	// The header file looks for the variable "title"
	String title = "Yazd Forums: Error";
%>
<%@ include file="header.jsp" %>


<%	////////////////////
	// Breadcrumb bar
	
	// The breadcrumb file looks for the variable "breadcrumbs" which
	// represents a navigational path, ie "Home > My Forum > Hello World"
	String[][] breadcrumbs = {
		{ "Home", "index.jsp" },
		{ "Error", "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>

<p>
	
<font color="red" size="-1" face="verdana,arial,helvetica">
<b>The following exception occured:</b>
</font>
<p>

<%	if (exception != null && debug) { 
		StringWriter sout = new StringWriter();
		PrintWriter pout = new PrintWriter(sout);
		exception.printStackTrace(pout);
%>
	<pre>
	<%= sout.toString() %>
	</pre>
	
<%	}
	else {
   		exception.printStackTrace();   
%>

	<%	if( exception instanceof UnauthorizedException ) { %>
	
		You were not authorized to that action.
	
	<%	} else { %>

		An error occured while processing your request.
	
	<%	} %>
   
<% } //end else %>

<p>

<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>
