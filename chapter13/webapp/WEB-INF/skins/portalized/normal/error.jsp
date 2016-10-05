<%@ page isErrorPage="true" %>

<%@ page import="java.io.*,
                 java.util.*,
                 java.net.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"
%>
<%@ taglib uri="portalbook.tld" prefix="pb" %>

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
	
		You were not authorized to carry out that action.
	
	<%	} else { %>

		An error occured while processing your request.
	
	<%	} %>
   
<% } //end else %>
