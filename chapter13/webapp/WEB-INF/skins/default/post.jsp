
<%
/**
 *	$RCSfile: post.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page import="com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.util.*"
	errorPage="error.jsp"
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
%>

<%	/////////////////
	// get parameters
	
	int forumID = ParamUtils.getIntParameter(request,"forum",-1);
	int threadID = ParamUtils.getIntParameter(request,"thread",-1);
	int messageID = ParamUtils.getIntParameter(request,"message",-1);
	boolean doPost = ParamUtils.getBooleanParameter(request,"doPost");
	boolean reply = ParamUtils.getBooleanParameter(request,"reply");
	String name = ParamUtils.getParameter(request,"name");
	String email = ParamUtils.getParameter(request,"email");
	String subject = ParamUtils.getParameter(request,"subject");
	String body = ParamUtils.getParameter(request,"body");
	String referringPage = ParamUtils.getParameter(request,"referer");
%>

<%	///////////////////////////////////////
	// Create forum, parent message objects
	
	// Load the forum -- if an exception is thrown, we'll redirect to
	// the error page
	Forum forum = forumFactory.getForum(forumID);
	
	// Load the forum message and thread it is associated with. Do
	// this only if we're replying to a message
	ForumThread thread = null;
	ForumMessage parentMessage = null;
	if( reply ) {
		thread = forum.getThread(threadID);
		parentMessage = thread.getMessage(messageID);
	}
%>

<%	/////////////////
	// error check
	
	boolean errors = false;
	String errorMessage = "";
	if( doPost && subject == null ) {
		errors = true;
		errorMessage = "Please enter a subject";
	}
	else if( doPost && body == null ) {
		errors = true;
		errorMessage = "Sorry, you can't post a blank message. Please enter a message.";
	}
%>

<%	/////////////////////////
	// Create the new message
	
	if( doPost && !errors ) {
		// Create a new message object
		ForumMessage newMessage = forum.createMessage(user);
		newMessage.setSubject(subject);
		newMessage.setBody(body);
		// add the name and email as an extended property if this user
		// is a guest
		if( user.isAnonymous() ) {
			if( name  != null ) { 
				newMessage.setProperty("name",name);
				SkinUtils.store(request,response,"yazd.post.name",name);
			}
			if( email != null ) {
				newMessage.setProperty("email",email);
				SkinUtils.store(request,response,"yazd.post.email",email);
			}
		}
		// if this is a reply, add it to the thread
		if( reply ) {
			thread.addMessage(parentMessage,newMessage);
		}
		else {
			// it is a new posting
			forum.addThread(forum.createThread(newMessage));
		}
		// Redirect to the thread list page
		response.sendRedirect("viewForum.jsp?forum="+forumID);
		return;
	}
%>

<%	//////////////////////
	// Header file include
	
	// The header file looks for the variable "title"
	String title = "Yazd Forums: Post a message";
%>
<%@ include file="header.jsp" %>

<%	//////////////////////
	// breadcrumb variable
	
	// load up different breadcrumbs based on if this is a reply to a message
	// or a new post.
	String[][] breadcrumbs = null;
	if( reply ) {
		breadcrumbs = new String[][] {
			{ "Home", "index.jsp" },
			{ forum.getName(), ("viewForum.jsp?forum=" + forumID) },
			{ thread.getName(), ("viewThread.jsp?forum=" + forumID + "&thread=" + threadID) },
			{ "Reply to Message", "" }
		};
	} else {
		breadcrumbs = new String[][] {
			{ "Home", "index.jsp" },
			{ forum.getName(), ("viewForum.jsp?forum="+forumID) },
			{ "Post New Message", "" }
		};
	}
%>
<%@ include file="breadcrumb.jsp" %>

<%	/////////////
	// Toolbar
	
	// The toolbar file looks for the following variables. To make a particular
	// "button" not appear, set a variable to null.
	boolean showToolbar = true;
	String viewLink = "viewForum.jsp?forum="+forumID;
	String postLink = null;
	String replyLink = null;
	String searchLink = null;
	// we can show a link to a user account if the user is logged in (handled
	// in the toolbar jsp)
	String accountLink = "userAccount.jsp";
%>
<%@ include file="toolbar.jsp" %>

<p>				 
			 
<h2>
<%	if( reply ) { %>
	Reply to: <%= parentMessage.getSubject() %>
<%	} else { %>
	Post New Message
<%	} %>
</h2>

<%	if( errors ) { %>
<h4 class="error"><i><%= errorMessage %></i></h4>
<%	} %>

<form action="post.jsp" name="postForm" method="post">
<input type="hidden" name="doPost" value="true">
<input type="hidden" name="reply" value="<%= reply %>">
<input type="hidden" name="referer" value="<%= (!doPost)?request.getHeader("REFERER"):referringPage %>">
<input type="hidden" name="forum" value="<%= forumID %>">
<input type="hidden" name="thread" value="<%= threadID %>">
<input type="hidden" name="message" value="<%= messageID %>">

<table cellpadding="3" cellspacing="0" border="0">
<%	// show name and email textfields if the user is a guest
	if( user.isAnonymous() ) { 
		// try to retrieve persisted values of name and email
		String storedName = SkinUtils.retrieve(request,response,"yazd.post.name");
		String storedEmail = SkinUtils.retrieve(request,response,"yazd.post.email");
%>
<tr>
	<td valign="top">Name</td>
	<td><input name="name" value="<%= (storedName!=null)?storedName:"" %>" size="30" maxlength="100"></td>
</tr>
<tr>
	<td valign="top">Email</td>
	<td><input name="email" value="<%= (storedEmail!=null)?storedEmail:"" %>" size="30" maxlength="100"></td>
</tr>
<%	} %>
<%	// Create the subject in the form we're going to display. If this 
	// is a new message, just a blank text field will show up. If this
	// is a reply, the subject of the old message will appear.
	String formSubject = "";
	if( reply ) {
		formSubject = parentMessage.getSubject();
		if( !formSubject.startsWith("Re: ") ) {
			formSubject = "Re: " + formSubject;
		}
	}
	else if( doPost && errors && subject != null ) {
		formSubject = subject;
	}
%>
<tr>
	<td valign="top">Subject</td>
	<td><input name="subject" value="<%= formSubject %>" size="40" maxlength="100"></td>
</tr>
<%	if( reply ) { 
		// replace \r\n (windows newlines) with unix newlines (\n)
		String parentBody = parentMessage.getUnfilteredBody();
		parentBody = StringUtils.replace(parentBody,"\r\n","\n");
		parentBody = StringUtils.replace(parentBody,"\n","\\n");
		parentBody = StringUtils.replace(parentBody,"\r","");
		// replace quotes
		parentBody = StringUtils.replace(parentBody,"\"","\\\"");
%>
<script language="JavaScript" type="text/javascript">
	var parentMessage = "<%= SkinUtils.quoteOriginal(parentBody,">",50) %>";
	function quoteOrig(ta) {
		ta.value = parentMessage + "\n" + ta.value;
		return false;
	}
</script>
<%	} %>
<tr>
	<td valign="top">&nbsp;</td>
	<td class="toolbar">
<%	// only show a "quote original" link if this is a reply
	if( reply ) { %>
		[ <a href="#" class="toolbar" title="Click to quote the original message"
		   onclick="return quoteOrig(document.postForm.body);"
		   >quote original</a> ]
		&nbsp;&nbsp;
<%	} %>
		<%--
		[ <a href="#" class="toolbar" title="Insert a smiley face">:)</a> ]
		&nbsp;&nbsp;
		[ <a href="#" class="toolbar" title="Insert a sad face">:(</a> ]
		--%>
	</td>
</tr>
<tr>
	<td valign="top"><%= (reply)?"Your Reply":"Message" %></td>
	<td><textarea name="body" cols="50" rows="13" wrap="virtual"></textarea></td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><input type="submit" value="Post <%= (reply)?"Reply":"Message" %>">
		&nbsp;
		<input type="submit" value="Cancel" onclick="location.href='viewForum.jsp?forum=<%= forumID %>';return false;">
	</td>
</tr>
</table>

</form>

<script language="JavaScript" type="text/javascript">
<!--
document.postForm.subject.focus();
//-->
</script>

<p>


<br clear="all">
	
<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>

