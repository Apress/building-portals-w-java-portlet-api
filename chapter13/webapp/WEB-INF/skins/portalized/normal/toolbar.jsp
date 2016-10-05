<%@ taglib uri="portalbook.tld" prefix="pb" %>
<%!	/////////////////////
	// toolbar variables
	
	// change these values to customize the look of your toolbar
	
	// Colors
	final static String toolbarBgcolor = "#cccccc";
	final static String toolbarFgcolor = "#eeeeff";
%>


<%	//////////////////
	// variables for displaying items on the toolbar
	boolean showLink = (viewLink!=null && !viewLink.equals(""));
	boolean showPost = (postLink!=null && !postLink.equals(""));
	boolean showReply = (replyLink!=null && !replyLink.equals(""));
%>

<%	/////////////////////
	// toolbar variables
	
	// change these values to customize the text and links of the
	// toolbar buttons
	
	// view button
	String viewButtonText = "View Topics";
	String viewButtonURL = viewLink;
	String viewButtonImgSRC = "images/doc_white.gif";
	String viewButtonImgWidth = "19";
	String viewButtonImgHeight = "19";
	String viewButtonImgAltText = "Click to view all topics in this forum";
	
	// post button
	String postButtonText = "Post New Message";
	String postButtonURL = postLink;
	String postButtonImgSRC = "images/doc_yellow.gif";
	String postButtonImgWidth = "19";
	String postButtonImgHeight = "19";
	String postButtonImgAltText = "Click to post a new message in this forum";
	
	// reply button
	String replyButtonText = "Reply";
	String replyButtonURL = replyLink;
	String replyButtonImgSRC = "images/doc_green.gif";
	String replyButtonImgWidth = "19";
	String replyButtonImgHeight = "19";
	String replyButtonImgAltText = "Click to reply to this message";
	
	// display the post button only if the user has 
	// permission to post in this forum
	/*
	boolean canPost = forum.hasPermission(ForumPermissions.CREATE_THREAD)
		|| forum.hasPermission(ForumPermissions.CREATE_MESSAGE);
	if( !canPost ) {
		showPost = false;
	}
	*/
%>

<%	////////////////////
	//
	if( showToolbar ) {
%>
   	<table bgcolor="<%= toolbarBgcolor %>" width="100%" border="0" cellspacing="0" cellpadding="1">
	<td><table bgcolor="<%= toolbarFgcolor %>" width="100%" border="0" cellspacing="0" cellpadding="3">

	<%	int count = 0; %>

	<%	if( showLink ) {  count += 3; %>
        <pb:url state="NORMAL" mode="VIEW" var="viewHref"><%=viewButtonURL%></pb:url>		
	
		<td width="1%"><a href="<%= viewHref %>"><img src="<pb:href path="<%= viewButtonImgSRC %>" />" width="<%= viewButtonImgWidth %>" height="<%= viewButtonImgHeight %>" border="0" alt="<%= viewButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= viewHref %>" class="toolbar" title="<%= viewButtonImgAltText %>"><%= viewButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	<%	if( showPost ) {  count += 3; %>
        <pb:url state="NORMAL" mode="VIEW" var="postHref"><%=postButtonURL%></pb:url>
        	
		<td width="1%"><a href="<%= postHref %>"><img src="<pb:href path="<%= postButtonImgSRC %>" />" width="<%= postButtonImgWidth %>" height="<%= postButtonImgHeight %>" border="0" alt="<%= postButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= postHref %>" class="toolbar" title="<%= postButtonImgAltText %>"><%= postButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	<%	if( showReply ) {  count += 3; %>
        <pb:url state="NORMAL" mode="VIEW" var="replyHref"><%=replyButtonURL%></pb:url>

		<td width="1%"><a href="<%= replyHref %>"><img src="<pb:href path="<%= replyButtonImgSRC %>" />" width="<%= replyButtonImgWidth %>" height="<%= replyButtonImgHeight %>" border="0" alt="<%= replyButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= replyHref %>" class="toolbar" title="<%= replyButtonImgAltText %>"><%= replyButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	<td width="<%= (100-5-count) %>%">
		<span class="toolbar"><br></span>
	</td>
	
	</table>
	</td>
	</table>
	
<%	} %>
