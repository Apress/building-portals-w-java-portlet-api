
<%
/**
 *	$RCSfile: post.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:07 $
 */
%>

<%@ page
	import="java.text.*,
	        com.Yasna.forum.*,
			com.Yasna.forum.util.*"
	errorPage="error.jsp"
%>

<%!	final static String DEFAULT_REDIRECT_PAGE = "viewMessage.jsp";
	final static String STR_TYPE_NEW 	= "new";
	final static String STR_TYPE_REPLY 	= "reply";
	final static String STR_TYPE_EDIT 	= "edit";
	final static String STR_ACTION_DOPOST = "doPost";
	final static int 	TYPE_NEW 	= 1;
	final static int 	TYPE_REPLY 	= 2;
	final static int 	TYPE_EDIT 	= 4;
	final static int 	ACTION_DOPOST = 1;
	SimpleDateFormat dateFormatter = new SimpleDateFormat( "EEEE, MMM d, yyyy" );
	SimpleDateFormat timeFormatter = new SimpleDateFormat( "h:mm a, z" );
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

<%	// Get parameter values
	int 	forumID 		= ParamUtils.getIntParameter(request, "forum", -1);		// forumID: Forum to post to
	int 	threadID 		= ParamUtils.getIntParameter(request, "thread", -1);	// parentID: Parent message ID
	int 	parentID 		= ParamUtils.getIntParameter(request, "message", -1);	// threadID: Thread to post to
	String 	redirectPage 	= ParamUtils.getParameter(request, "redirectPage");		// redirectPage: Where to go when this post is successful
	String 	postTypeStr 	= ParamUtils.getParameter(request, "postType");			// postType: either "new" or "reply" or "edit"
	String 	postActionStr	= ParamUtils.getParameter(request, "postAction");		// postAction: either blank or "doPost"
	String 	subject 		= ParamUtils.getParameter(request, "subject");			// subject: subject of the message
	String 	body	 		= ParamUtils.getParameter(request, "body");				// body: body of the message
	int		postType 		= 0;
	int		postAction 		= 0;

	if (redirectPage == null || redirectPage.length() == 0) {
		redirectPage = DEFAULT_REDIRECT_PAGE;
	}

	// New is default postType
	if (postTypeStr == null) {
		postType = TYPE_NEW;
		postTypeStr = STR_TYPE_NEW;
	} else if (STR_TYPE_NEW.equals(postTypeStr)) {
		postType = TYPE_NEW;
	} else if (STR_TYPE_REPLY.equals(postTypeStr)) {
		postType = TYPE_REPLY;
	} else if (STR_TYPE_EDIT.equals(postTypeStr)) {
		postType = TYPE_EDIT;
	}

	if (STR_ACTION_DOPOST.equals(postActionStr)) {
		postAction = ACTION_DOPOST;
	}
%>

<%	// Get a ForumFactory object, check if it is null. If it is null, redirect to the error page.
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	// Get a forum object, redirect on error:
	Forum forum = forumFactory.getForum(forumID);
	
	// Get a profile manager and create the user object. If the userID of the authorization
	// token can't be found, redirect to the error page
	ProfileManager manager = forumFactory.getProfileManager();
	User thisUser = manager.getUser( authToken.getUserID() );
%>

<%	// If this is a reply, setup the parent message and the thread objects
	ForumThread thread = null;
	ForumMessage parentMessage = null;
	if (postType == TYPE_REPLY || postType == TYPE_EDIT) {
		thread = forum.getThread( threadID );			// throws ForumThreadNotFoundException
		parentMessage = thread.getMessage( parentID );	// throws ForumMessageNotFoundException
	}
%>

<%	// Create the message only if we're posting or replying to a message:
	if (postAction == ACTION_DOPOST) {
		ForumMessage newMessage = null;
		int messageID = -1;
        if (subject == null || body == null) {
			throw new Exception( "Please enter the " + (subject == null ? "subject " : "")  + (body == null ? "message" : ""));
        }
		//try {
			if (postType == TYPE_EDIT) {
                if (parentMessage.getUser().getID() != authToken.getUserID() ||
            	    (System.currentTimeMillis() > parentMessage.getCreationDate().getTime() + 3 * 3600 * 1000)) {
					throw new Exception( "The message can only be changed within 2-3 hours after creation" );
                }
    			parentMessage.setSubject( subject );
    			parentMessage.setBody( body );
    			parentMessage.setProperty( "IP", request.getRemoteAddr() );
    			messageID = parentID;
			} else {
    			newMessage = forum.createMessage( thisUser );
    			newMessage.setSubject( subject );
    			newMessage.setBody( body );
    			newMessage.setProperty( "IP", request.getRemoteAddr() );
    			messageID = newMessage.getID();
    			if (postType == TYPE_REPLY) {
    				thread.addMessage( parentMessage, newMessage );
    			}
    			else if (postType == TYPE_NEW) {
    				// make new thread with the new message as the root.
    				thread = forum.createThread(newMessage);
    				forum.addThread(thread);
    				threadID = thread.getID();
    			}
			}
		//}
		//catch( UnauthorizedException ue) {
		//	System.err.println( "servletforum/post.jsp: " + ue );
		//	response.sendRedirect( "error.jsp?message=" + URLEncoder.encode("Not authorized to post messages here.") );
		//	return;
		//}
		
		// at this point, the message was created and added successfully,
		// so redirect to the redirect page:
		response.sendRedirect( redirectPage + "?forum=" + forumID + "&thread=" + threadID + "&parent=" + parentID + "&message=" + messageID);
		return;
	}
%>

<%	/////////////////
	// header include
	
	String title = "Yazd: Post Message";
%>
<%@	include file="header.jsp" %>

<font face="verdana" size="2"><b><a href="index.jsp" class="normal">Home</a>
&gt;
<a href="viewForum.jsp?forum=<%= forumID %>" class="normal"><%= forum.getName() %></a>
&gt;</b>
<%	if (postType == TYPE_REPLY) { %>
	Reply to: <b><%= parentMessage.getSubject() %></b>
<%	} else if (postType == TYPE_NEW) { %>
	Post a new message
<%	} %>
</font>

<p>

<%	if (postType == TYPE_REPLY || postType == TYPE_EDIT) { %>

	<%-- Message table --%>
	<table bgcolor="#666666" cellpadding=1 cellspacing=0 border=0 width="100%">
	<tr>
	<td>
		<table bgcolor="#dddddd" cellpadding=3 cellspacing=0 border=0 width="100%">
		<td bgcolor="#dddddd" width="99%">
			<font face="Trebuchet MS">
			<b><%= parentMessage.getSubject() %></b>
			</font>
			<br>
			<font face="verdana" size=2>
			<b>
			<%	if (!parentMessage.isAnonymous()) { 
					User parentUser = parentMessage.getUser();
			%>
					<a href="mailto:<%= parentUser.getEmail() %>" class="normal"><%= parentUser.getName() != null ? parentUser.getName() : parentUser.getUsername() %></a>
			<%	} else { %>
					[Anonymous]
			<%	} %>
			</b>
			</font>
		</td>
		<td bgcolor="#dddddd" width="1%" nowrap>
			<font face="verdana" size=1>
			<b>
			<% 	java.util.Date d = parentMessage.getCreationDate(); %>
			<%= dateFormatter.format(d) %> - <%= timeFormatter.format(d) %>
			</b>
			</font>
		</td>
		</table>
		<table bgcolor="#666666" cellpadding=0 cellspacing=0 border=0 width="100%">
		<td><img src="images/blank.gif" width=1 height=1 border=0></td>
		</table>
		<table bgcolor="#ffffff" cellpadding=5 cellspacing=0 border=0 width="100%">
		<td>
			<%= parentMessage.getBody() %>
		</td>
		</table>
	</td>
	</tr>
	</table>
	<%-- Message table --%>

<%	} %>

<p>

<form action="post.jsp" method="post" name="postForm">
	<input type="hidden" name="message" value="<%= parentID %>">
	<input type="hidden" name="thread" value="<%= threadID %>">
	<input type="hidden" name="forum" value="<%= forumID %>">
	<input type="hidden" name="postType" value="<%= postTypeStr %>">
	<input type="hidden" name="postAction" value="<%= STR_ACTION_DOPOST %>">

	<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<td>
			<font face="verdana" size="2"><b>
			<% if (postType == TYPE_EDIT) { %>
				Edit your message:
			<% } else if (postType == TYPE_REPLY) { %>
				Reply to this message:
			<% } else if (postType == TYPE_NEW) { %>
				Post a new message:
			<% } %>
			</b></font>
			
				<p>
			
				<table>
				<tr>
					<td><font face="verdana" size="2"><b>Subject</b></font></td>
					<%	String parentSubject = "";
						if (parentMessage != null) {
							parentSubject = parentMessage.getSubject();
                            if (postType == TYPE_REPLY) {
    							if (parentSubject == null) {
    							    	parentSubject = "Re: your post";
    							} else if (!parentSubject.startsWith( "Re:" )) {
    								parentSubject = "Re: " + parentSubject;
    							}
                            }
						}
					%>
					<td><input type="text" name="subject" value="<%= parentSubject %>" size="50" maxlength="100"></td>
				</tr>
				<%	if (postType == TYPE_REPLY) { %>
					<tr>
					<td><br></td>
					<td>
					<% 
						/*** <font face="verdana,arial,helvetica" size="1">
						[ <a href="" onmouseover="window.status='Click to quote the original message.';return true;" onmouseout="window.status='';return true;" onclick="document.postForm.message.value=quoteOrig(document.postForm.message);return false;" style="text-decoration:none;">Quote original</a> ]
						</font> ***/
					%>
					<br></td>
					</tr>
				<%	} %>
				<tr>
					<td valign="top"><font face="verdana" size="2"><b>
					<%	if (postType == TYPE_REPLY) { %>
						Reply
					<%	} else if (postType == TYPE_NEW) { %>
						Question
					<%	} %>
					</b></font></td>
					<%	String parentBody = "";
						if (parentMessage != null && postType == TYPE_EDIT) {
						    parentBody = parentMessage.getBody();
/***/		// QQQ unfilter waiting for interface change...
			parentBody = StringUtils.replace(parentBody, "<BR>", "\n");
			parentBody = StringUtils.replace(parentBody, "<b>", "[b]");
			parentBody = StringUtils.replace(parentBody, "</b>", "[/b]");
			parentBody = StringUtils.replace(parentBody, "<i>", "[i]");
			parentBody = StringUtils.replace(parentBody, "</i>", "[/i]");
/***/
						}
					%>
					<td><textarea name="body" cols="50" rows="10" wrap="virtual"><%= parentBody %></textarea></td>
				</tr>
				<tr>
					<td><br></td>
					<td>
						<input type="submit" value="Post Message">
					</td>
				</tr>
				</table>
				
		</td>
		<td></td>
	</tr>
	</table>

</form>

<%@	include file="footer.jsp" %>

