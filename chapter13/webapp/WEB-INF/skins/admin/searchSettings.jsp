
<%
/**
 *	$RCSfile: searchSettings.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%@ page import="java.util.*,
	             java.text.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"
	errorPage="error.jsp"
%>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>

<%!	/////////////////////
	// global variables
	
	// Date formatter
	private final SimpleDateFormat dateFormatter
		= new SimpleDateFormat( "h:mm:ss aaa z 'on' EEE, MMM d, yyyy" );
%>

<%	////////////////////////////////
	// Yazd authorization check
	
	// check the bean for the existence of an authorization token.
	// Its existence proves the user is valid. If it's not found, redirect
	// to the login page
	Authorization authToken = adminBean.getAuthToken();
	if( authToken == null ) {
		response.sendRedirect( "login.jsp" );
		return;
	}
%>
 
<%	////////////////////
	// Security check
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	boolean isSystemAdmin = ((Boolean)session.getValue("yazdAdmin.systemAdmin")).booleanValue();
	
	// redirect to error page if we're not a group admin or a system admin
	if( !isSystemAdmin ) {
		throw new UnauthorizedException("Sorry, you don't have permission to change search settings.");
	}
%>

<%	////////////////////
	// get parameters
	
	boolean doIndex = ParamUtils.getBooleanParameter(request,"doIndex");
	boolean doUpdateIndex = ParamUtils.getBooleanParameter(request,"doUpdateIndex");
	boolean doRebuildIndex = ParamUtils.getBooleanParameter(request,"doRebuildIndex");
	boolean doIntervalUpdate = ParamUtils.getBooleanParameter(request,"doIntervalUpdate");
	boolean makeAutoIndexEnabled = ParamUtils.getBooleanParameter(request,"isAutoIndexEnabled");
	int hourInterval = ParamUtils.getIntParameter(request,"hourInterval",0);
	int minInterval = ParamUtils.getIntParameter(request,"minInterval",0);
%>

<%	////////////////
	// load up indexer
	
	SearchIndexer indexer = forumFactory.getSearchIndexer();
	boolean isAutoIndexEnabled = indexer.isAutoIndexEnabled();
%>

<%	///////////////
	// rebuild or update index or intervals
	
	if( doUpdateIndex ) {
		indexer.updateIndex();
	}
	if( doRebuildIndex ) {
		indexer.rebuildIndex();
	}
	if( doIntervalUpdate ) {
		indexer.setUpdateInterval( minInterval, hourInterval );
	}
	if( doIndex ) {
		indexer.setAutoIndexEnabled( makeAutoIndexEnabled );
	}
	
	// redirect if necessary
	if( doUpdateIndex || doRebuildIndex || doIntervalUpdate || doIndex ) {
		response.sendRedirect("searchSettings.jsp");
		return;
	}
%>

<%	////////////////////////////
	// get properties of the indexer
	
	int hoursUpdateInterval = indexer.getHoursUpdateInterval();
	int minsUpdateInterval = indexer.getMinutesUpdateInterval();
	Date lastIndexedDate = indexer.getLastIndexedDate();
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Search Settings" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

The search indexer periodically adds new messages into the searchable 
message database. You can control how often this update takes place by 
adjusting the refresh interval. Messages will not appear in search results 
until they are indexed, so a relatively frequent refresh interval is 
recommended.

<p>

If you choose, auto-indexing can be turned off.
However, in that case, messages will not be indexed unless you do so manually.
<p>

<form action="searchSettings.jsp">
<input type="hidden" name="doIndex" value="true">
<table bgcolor="#666666" cellpadding="0" cellspacing="0" border="0" width="80%" align="center">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#ffffff">
	<td>Auto Indexing:</td>
	<td align="center"<%= (isAutoIndexEnabled)?" bgcolor=\"#99cc99\"":"" %>>
		<input type="radio" name="isAutoIndexEnabled" value="true" id="rb01"
		 <%= (isAutoIndexEnabled)?"checked":"" %>>
		<label for="rb01"><%= (isAutoIndexEnabled)?"<b>On</b>":"On" %></label>
	</td>
	<td align="center"<%= (!isAutoIndexEnabled)?" bgcolor=\"#cc6666\"":"" %>>
		<input type="radio" name="isAutoIndexEnabled" value="false" id="rb02"
		 <%= (!isAutoIndexEnabled)?"checked":"" %>>
		<label for="rb02"><%= (!isAutoIndexEnabled)?"<b>Off</b>":"Off" %></label>
	</td>
</tr>
</table>
</td>
</table>
<p>
<center>
		<input type="submit" value="Update">
</center>
</form>

<p>

<form action="searchSettings.jsp">
<input type="hidden" name="doIntervalUpdate" value="true">
<table bgcolor="#666666" cellpadding="0" cellspacing="0" border="0" width="80%" align="center">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#ffffff">
	<td>Last indexed:</td>
	<td><%= dateFormatter.format(lastIndexedDate) %></td>
</tr>
<tr bgcolor="#ffffff">
	<td>Refresh Interval:</td>
	<td>
		Refresh every
		<select size="1" name="hourInterval">
		<%	for( int i=0; i<24; i++ ) { %>
		<%		String selected = ""; %>
		<%		if( i==hoursUpdateInterval ) { selected = " selected"; } %>
			<option value="<%= i %>"<%= selected %>><%= i %>
		<%	} %>
		</select>
		hours,
		<select size="1" name="minInterval">
		<%	for( int i=0; i<60; ) { %>
		<%		String selected = ""; %>
		<%		if( i==minsUpdateInterval ) { selected = " selected"; } %>
			<option value="<%= i %>"<%= selected %>><%= i %>
		<%	if( i>=10 ) { %>
		<%		i+=5;  %>
		<%	} else { i++; } %>
		<%	} %>
		</select>
		minutes.
	</td>
</tr>
</table>
</td>
</table>
<p>
<center>
	<input type="submit" value="Save Changes">
	&nbsp;
	<input type="submit" value="Done"
	 onclick="location.href='main.jsp';return false;">
</center>
</form>

<p>

<b>Force update of index</b>
<form action="searchSettings.jsp">
<input type="hidden" name="doUpdateIndex" value="true">
<ul>
	This will index any messages added since the last indexer was run.<br>
	<input type="submit" value="Update Index Now">
</ul>
</form>

<p>

<b>Force rebuild of index</b>
<form action="searchSettings.jsp">
<input type="hidden" name="doRebuildIndex" value="true">
<ul>
	This will reindex all messages. (May take a few moments if you have
	a lot of messages)<br>
	<input type="submit" value="Rebuild Index Now">
</ul>
</form>


</body>
</html>
