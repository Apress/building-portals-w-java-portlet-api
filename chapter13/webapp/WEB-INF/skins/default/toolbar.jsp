
<%
/**
 *	$RCSfile: toolbar.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

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
	boolean showSearch = (searchLink!=null && !searchLink.equals(""));
	boolean showAccount = (accountLink!=null && !accountLink.equals(""));
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
	
	// search button
	String searchButtonText = "Search This Forum";
	String searchButtonURL = searchLink;
	String searchButtonImgSRC = "images/search.gif";
	String searchButtonImgWidth = "19";
	String searchButtonImgHeight = "19";
	String searchButtonImgAltText = "Click to search for messages in this forum";
	
	// account button
	String accountButtonText = "Your Account";
	String accountButtonURL = accountLink;
	String accountButtonImgSRC = "images/user.gif";
	String accountButtonImgWidth = "19";
	String accountButtonImgHeight = "19";
	String accountButtonImgAltText = "Click to get information about your account";
	
	// Login button
	String loginButtonText = "Login";
	String loginButtonURL = "login.jsp";
	String loginButtonImgSRC = "images/login.gif";
	String loginButtonImgWidth = "19";
	String loginButtonImgHeight = "19";
	String loginButtonImgAltText = "Login in or create a new user account";
	
	// logout button
	String logoutButtonText = "Logout";
	String logoutButtonURL = "index.jsp?logout=true";
	String logoutButtonImgSRC = "images/x.gif";
	String logoutButtonImgWidth = "19";
	String logoutButtonImgHeight = "19";
	String logoutButtonImgAltText = "Logout of this forum";
	
	
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
		<td width="1%"><a href="<%= viewButtonURL %>"><img src="<%= viewButtonImgSRC %>" width="<%= viewButtonImgWidth %>" height="<%= viewButtonImgHeight %>" border="0" alt="<%= viewButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= viewButtonURL %>" class="toolbar" title="<%= viewButtonImgAltText %>"><%= viewButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	<%	if( showPost ) {  count += 3; %>
		<td width="1%"><a href="<%= postButtonURL %>"><img src="<%= postButtonImgSRC %>" width="<%= postButtonImgWidth %>" height="<%= postButtonImgHeight %>" border="0" alt="<%= postButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= postButtonURL %>" class="toolbar" title="<%= postButtonImgAltText %>"><%= postButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	<%	if( showReply ) {  count += 3; %>
		<td width="1%"><a href="<%= replyButtonURL %>"><img src="<%= replyButtonImgSRC %>" width="<%= replyButtonImgWidth %>" height="<%= replyButtonImgHeight %>" border="0" alt="<%= replyButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= replyButtonURL %>" class="toolbar" title="<%= replyButtonImgAltText %>"><%= replyButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	<%	if( showSearch ) {  count += 3; %>
		<td width="1%"><a href="<%= searchButtonURL %>"><img src="<%= searchButtonImgSRC %>" width="<%= searchButtonImgWidth %>" height="<%= searchButtonImgHeight %>" border="0" alt="<%= searchButtonImgAltText %>"></a></td>
		<td width="1%" nowrap><a href="<%= searchButtonURL %>" class="toolbar" title="<%= searchButtonImgAltText %>"><%= searchButtonText %></a></td>
		<td width="1%">&nbsp;&nbsp;</td>
	<%	} %>

	
	<%	if( showAccount ) { count += 3; } %>
	<%	if( user.isAnonymous() ) { count -= 2; } %>

	<td width="<%= (100-5-count) %>%">
		<span class="toolbar"><br></span>
	</td>
	
<%	// show a link to a user account if there is a registered user
	// logged into this skin. Otherwise, we'll display a link to a 
	// "create account" page
	
	if( showAccount ) { 
		
		// if this is a registered user...
		if( !user.isAnonymous() ) {
%>
	<td width="1%"><a href="<%= accountButtonURL %>"><img src="<%= accountButtonImgSRC %>" width="<%= accountButtonImgWidth %>" height="<%= accountButtonImgHeight %>" border="0" alt="<%= accountButtonImgAltText %>"></a></td>
	<td width="1%" nowrap><a href="<%= accountButtonURL %>" class="toolbar" title="<%= accountButtonImgAltText %>"><%= accountButtonText %></a></td>
	<td width="1%">&nbsp;&nbsp;</td>
<%		} else { %>
	<td width="1%"><a href="<%= loginButtonURL %>"><img src="<%= loginButtonImgSRC %>" width="<%= loginButtonImgWidth %>" height="<%= loginButtonImgHeight %>" border="0" alt="<%= loginButtonImgAltText %>"></a></td>
	<td width="1%" nowrap><a href="<%= loginButtonURL %>" class="toolbar" title="<%= loginButtonImgAltText %>"><%= loginButtonText %></a></td>
	<td width="1%">&nbsp;&nbsp;</td>
<%		}
	}
%>

<%	// only show a logout button if the user is a registered user
	
	if( !user.isAnonymous() ) {
%>
	<td width="1%"><a href="<%= logoutButtonURL %>"><img src="<%= logoutButtonImgSRC %>" width="<%= logoutButtonImgWidth %>" height="<%= logoutButtonImgHeight %>" border="0" alt="<%= logoutButtonImgAltText %>"></a></td>
	<td width="1%" nowrap><a href="<%= logoutButtonURL %>" class="toolbar" title="<%= logoutButtonImgAltText %>"><%= logoutButtonText %></a></td>
	
<%	} %>
	
	</table>
	</td>
	</table>
	
<%	} %>

