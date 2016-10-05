
<%
/**
 *	$RCSfile: search.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:08 $
 */
%>

<%@	page 
	import="java.util.*,
	        java.text.*,
            com.Yasna.forum.*,
			com.Yasna.forum.Query,
            com.Yasna.forum.util.*,
			com.Yasna.util.*"
	errorPage="error.jsp"
%>

<%!	/////////////////
	// global vars
	
	private final String[] months =
		{"January","February","March","April","May","June","July","August","September","October","November","December"};
	private final int[] results =
		{10,25,50,100};
	private final SimpleDateFormat dateFormatter
		= new SimpleDateFormat( "MMMM dd, yyyy 'at' h:mm:ss a z" );
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

<%	//////////////////
	// get parameters
	
	boolean doSearch = ParamUtils.getBooleanParameter(request,"doSearch");
	boolean advancedSearch = ParamUtils.getBooleanParameter(request,"adv");
	String queryText = ParamUtils.getParameter(request,"q");
	int forumID = ParamUtils.getIntParameter(request,"forum",-1);
	int range = ParamUtils.getIntParameter(request,"numResults",10);
	int start = ParamUtils.getIntParameter(request,"start",0);
	int threadID = ParamUtils.getIntParameter(request,"thread",-1);
	String between = ParamUtils.getParameter(request,"between");
	String when = ParamUtils.getParameter(request,"when");
	
	int month = ParamUtils.getIntParameter(request,"month",-1);
	int day = ParamUtils.getIntParameter(request,"day",-1);
	
	int fromMonth = ParamUtils.getIntParameter(request,"fromMonth",-1);
	int fromDay = ParamUtils.getIntParameter(request,"fromDay",-1);
	int toMonth = ParamUtils.getIntParameter(request,"toMonth",-1);
	int toDay = ParamUtils.getIntParameter(request,"toDay",-1);
%>

<%
	ProfileManager manager = forumFactory.getProfileManager();
%>

<%	//////////////////////////
	// try loading up forums (exceptions may be thrown)
	Forum forum = null;
	boolean notAuthorizedToViewForum = false;
	boolean forumNotFound = false;
	forum = forumFactory.getForum(forumID);
	String forumName = forum.getName();
	ForumThread thread = null;
	if( threadID > -1 ) {
		thread = forum.getThread(threadID);
	}
%>

<%	////////////////////
	// perform a search
	
	Iterator searchResults = null;
	Query query = null;
	int numResults = -1;
	if( doSearch && queryText!=null && forumID>0 ) {
		query = forum.createQuery();
		query.setQueryString(queryText);
		
		// dates
		if( between != null ) {
			Calendar cal = Calendar.getInstance();
			int todayMonth = cal.get(Calendar.MONTH);
			int todayDay = cal.get(Calendar.DAY_OF_MONTH);
			if( between.equals("false") ) {
				if( month > -1 ) { cal.set(Calendar.MONTH,month); }
				if( day > -1 ) { cal.set(Calendar.DAY_OF_MONTH,day); }
				// before or after
				if( when!=null && when.equals("before") && month > -1 && day > -1 ) {
					query.setBeforeDate( cal.getTime() );
				}
				else if( when!=null && when.equals("after") && month > -1 && day > -1 ) {
					query.setAfterDate( cal.getTime() );
				}
			}
			else if( between.equals("true") ) {
				if( fromMonth > -1 ) { cal.set(Calendar.MONTH,fromMonth); }
				if( fromDay > -1 ) { cal.set(Calendar.DAY_OF_MONTH,fromDay); }
				if( fromMonth > -1 && fromDay > -1 ) {
					query.setBeforeDate( cal.getTime() );
				}
				// reset cal obj to today
				cal.set(Calendar.MONTH,todayMonth);
				cal.set(Calendar.DAY_OF_MONTH,todayDay);
				if( toMonth > -1 ) { cal.set(Calendar.MONTH,toMonth); }
				if( toDay > -1 ) { cal.set(Calendar.DAY_OF_MONTH,toDay); }
				if( toMonth > -1 && toDay > -1 ) {
					query.setAfterDate( cal.getTime() );
				}
			}
		}
		//query.filterOnUser( manager.getUser("bill") );
		//if( thread != null ) {
		//	query.filterOnThread( thread );
		//}
		
		// run the search
		searchResults = query.results(start,range);
		// get the number of results
		numResults = query.resultCount();
	}
%>

<%	////////////////////
	// page title variable for header
	String title = "Yazd Forums: search";
%>
<%	/////////////////////
	// page header
%>
<%@ include file="header.jsp" %>


<%	////////////////////
	// breadcrumb variable
	String[][] breadcrumbs = {
		{ "Home", "index.jsp" },
		{ forumName, ("viewForum.jsp?forum="+forumID) },
		{ "Search", "" }
	};
%>
<%@ include file="breadcrumb.jsp" %>

<%	///////////////////
	// toolbar variables
	boolean showToolbar = true;
	String viewLink = "viewForum.jsp?forum="+forumID;
	String postLink = "post.jsp?mode=new&forum="+forumID;
	String replyLink = null;
	String searchLink = null;
	String accountLink = "userAccount.jsp";
%>
<%@ include file="toolbar.jsp" %>

<p>

<center>

<form action="search.jsp" name="searchForm">
<input type="hidden" name="doSearch" value="true">
<input type="hidden" name="forum" value="<%= forumID %>">
<input type="hidden" name="adv" value="<%= advancedSearch %>">

<input type="text" name="q" size="40" maxlength="100"
 value="<%=(queryText!=null)?queryText:""%>">
<input type="submit" value="Search!" style="background-color:#dddddd;">
<br>
<font size="-2">
<a href="search.jsp?forum=<%=forumID%>&adv=<%=!advancedSearch%>&q=<%=(queryText!=null)?queryText:""%>"
 >&raquo; <%= (advancedSearch)?"Simple":"Advanced" %> Search</a>
</font>

<%	if( advancedSearch ) { %>

<%	Calendar today = Calendar.getInstance();
	int todayMonth = today.get(Calendar.MONTH);
	int todayDay = today.get(Calendar.DAY_OF_MONTH);
%>

<script language="JavaScript" type="text/javascript">
function today( obj1, obj2, idx ) {
	obj1.selectedIndex = <%= (todayMonth+1) %>;
	obj2.selectedIndex = <%= todayDay %>;
	document.searchForm.between[idx].checked=true;
	return false;
}
</script>

<table cellpadding="3" cellspacing="0" border="0">
<tr><td colspan="4"><b>Date</b></td></tr>
<tr>
	<td rowspan="2">&nbsp;</td>
	<td><input type="radio" name="between" value="false"<%= (between!=null&&between.equals("false"))?" checked":"" %>></td>
	<td>
		<select size="1" name="when" onchange="this.form.between[0].checked=true;"><option value="after">After<option value="before">Before</select> this date
	</td>
	<td>
		<select size="1" name="month" onchange="this.form.between[0].checked=true;">
		<option value="-1">
		<%	for( int i=0; i<months.length; i++ ) { %>
		<%		String selected = ""; %>
		<%		if( month == i ) { selected = " selected"; } %>
		<option value="<%= i %>"<%= selected %>><%= months[i] %>
		<%	} %>
		</select>
		<select size="1" name="day" onchange="this.form.between[0].checked=true;">
		<option value="-1">
		<%	for( int i=0; i<31; i++ ) { %>
		<%		String selected = ""; %>
		<%		if( day == (i+1) ) { selected = " selected"; } %>
		<option value="<%= (i+1) %>"<%= selected %>><%= (i+1) %>
		<%	} %>
		</select>
		<%= today.get(Calendar.YEAR) %>
		&nbsp;
		<font size="-2">
		[<a href="#" title="Click for today's date"
		  onclick="return today(document.searchForm.month,document.searchForm.day,0);">today</a>]
		</font>
	</td>
</tr>
<tr>
	<td valign="top"><input type="radio" name="between" id="rb02" value="true"<%= (between!=null&&between.equals("true"))?" checked":"" %>></td>
	<td valign="top"><label for="rb02">Between</label></td>
	<td>
		<select size="1" name="fromMonth" onchange="this.form.between[1].checked=true;">
		<option value="-1">
		<%	for( int i=0; i<months.length; i++ ) { %>
		<option value="<%= i %>"><%= months[i] %>
		<%	} %>
		</select>
		<select size="1" name="fromDay" onchange="this.form.between[1].checked=true;">
		<option value="-1">
		<%	for( int i=0; i<31; i++ ) { %>
		<option value="<%= (i+1) %>"><%= (i+1) %>
		<%	} %>
		</select>
		<%= today.get(Calendar.YEAR) %>
		&nbsp;
		<font size="-2">
		[<a href="#" title="Click for today's date"
		  onclick="return today(document.searchForm.fromMonth,document.searchForm.fromDay,1);">today</a>]</font><br>and<br>
		<select size="1" name="toMonth" onchange="this.form.between[1].checked=true;">
		<option value="-1">
		<%	for( int i=0; i<months.length; i++ ) { %>
		<option value="<%= i %>"><%= months[i] %>
		<%	} %>
		</select>
		<select size="1" name="toDay" onchange="this.form.between[1].checked=true;">
		<option value="-1">
		<%	for( int i=0; i<31; i++ ) { %>
		<option value="<%= (i+1) %>"><%= (i+1) %>
		<%	} %>
		</select>
		<%= today.get(Calendar.YEAR) %>
		&nbsp;
		<font size="-2">
		[<a href="#" title="Click for today's date"
		  onclick="return today(document.searchForm.toMonth,document.searchForm.toDay,1);">today</a>]
		</font>
	</td>
</tr>
<tr><td colspan="4"><b>Number of Results</b></td></tr>
<tr>
	<td rowspan="1">&nbsp;</td>
	<td colspan="3">
		Show me
		<select size="1" name="numResults">
		<%	for( int i=0; i<results.length; i++ ) { %>
		<%		String selected = ""; %>
		<%		if( range == results[i] ) { selected = " selected"; } %>
		<option value="<%= results[i] %>"<%= selected %>><%= results[i] %>
		<%	} %>
		</select>
		results per page.
	</td>
</tr>
</table>

<%	} %>

</form>
</center>
<script language="JavaScript" type="text/javascript">
<!--
	document.searchForm.q.focus();
//-->
</script>

<%	if( doSearch ) { %>

	<hr size="0">
	
	<p>
	
	<%	if( searchResults == null || !searchResults.hasNext() ) { %>
	
		<center><i>No results were found. Please try another search.</i></center>
	
	<%	} else { %>
	
		<b><%= numResults %> total results for "<%= queryText %>"</b>
		<p>
		
		<%	int rowCount = start+1; %>
		<%	String[] queryWords = StringUtils.toLowerCaseWordArray(queryText); %>
		
<%	while( searchResults.hasNext() ) {
		ForumMessage message = (ForumMessage)searchResults.next();
		int messageID = message.getID();
		int thisThreadID = message.getForumThread().getID();
		User msgUser = message.getUser();
		String name = msgUser.getName();
		if( msgUser.isAnonymous() ) {
			name = message.getProperty("name");
		}
		if( name == null ) {
			name = "<i>Anonymous</i>";
		}
		String body = StringUtils.escapeHTMLTags(message.getUnfilteredBody());
		body = StringUtils.chopAtWord(body, 150) + " ...";
		body = StringUtils.highlightWords( body, queryWords, "<font style='background-color:#ffff00'><b>", "</b></font>" );
%>
		
		<%= rowCount++ %>)
		<a href="viewThread.jsp?forum=<%= forumID %>&thread=<%= thisThreadID %>&message=<%= messageID %>"
		 ><b><%= message.getSubject() %></b></a>
		<br>
		<i>
		<%= body %>
		</i>
		<br>
		<font color="#666666">
		Posted by: <b><%= name %></b>, on <%= dateFormatter.format(message.getCreationDate()) %>
		</font>
		<p>
		
		<%	} %>
			
		<center>
	
<%	String href = "search.jsp?forum="+forumID+"&doSearch="+doSearch+
		"&adv="+advancedSearch+"&q="+((queryText!=null)?queryText:"")+
		"&range="+range;
%>

<%
	int total = (numResults/range)+1;
	int i = (start/range)+1;
	int lTotal = i-1;
	int rTotal = total-i;
	int lCount, rCount;
	
	if (i < 5) { lCount = lTotal; }
	else { lCount = 5; }
	
	if (i+5 > total) {
		rCount = total-i;
		//now, add as much as we can to other side
		lCount += 5-(total-i) > lTotal-lCount? lTotal-lCount: 5-(total-i);
	}
	else {
		rCount = 5;
		//Add in more to right if possible;
		if (lCount < 5) {
			rCount += 5-lCount > rTotal-rCount? rTotal-rCount: 5-lCount;
		}
	}
%>
		<table cellpadding="2" cellspacing="2" border="0">
		<tr>
			<td>
				<%	if( (start-range) >= 0 ) { %>
				<a href="<%= href %>&start=<%= (start-range) %>">&laquo; Previous</a>
				<%	} %>
				&nbsp;
			</td>
			<td align="right">
				<img src="images/search_j.gif" width="14" height="22" alt="" border="0">	
			</td>
			<%	for( int idx=(i-lCount); idx<=(i+rCount); idx++ ) { %>
			<%		boolean onCurrent = (range*(idx-1))!=start; %>
			<%		String color = (onCurrent)?"blue":"red"; %>
			<td align="center">
				<a href="<%= href %>&start=<%= range*(idx-1) %>"
				><img src="images/search_<%= color %>_i.gif" width="10" height="22" alt="" border="0"></a>
			</td>
			<%	} %>
			<td>
				<img src="images/search_ve.gif" width="32" height="22" alt="" border="0">
			</td>
			<td>
				&nbsp;
				<%	if( (start+range) <= numResults ) { %>
				<a href="<%= href %>&start=<%= (start+range) %>">Next &raquo;</a>
				<%	} %>
			</td>
		</tr>
		<tr>
			<td colspan="2">Search Page:</td>
			<%	for( int idx=(i-lCount); idx<=(i+rCount); idx++ ) { %>
			<%		boolean onCurrent = (range*(idx-1))!=start; %>
			<td align="center">
				<%	if( onCurrent ) { %>
				<a href="<%= href %>&start=<%= range*(idx-1) %>"
				><% } %><b><%= idx %></b><%	if( onCurrent ) { %></a><% } %>
			</td>
			<%	} %>
			<td colspan="2">&nbsp;</td>
		</tr>
		</table>
	
		</center>
	

	<%	} // if results %>
	
<%	} %>

<br clear="all">

<%	/////////////////////
	// page footer 
%>
<%@ include file="footer.jsp" %>
