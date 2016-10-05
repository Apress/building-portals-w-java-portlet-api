
<%
/**
 *	$RCSfile: cache.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page import="java.util.*,
				 java.text.*,
                 com.Yasna.forum.*,
				 com.Yasna.util.*,
				 com.Yasna.forum.database.*,
				 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*" %>

<jsp:useBean id="adminBean" scope="session"
 class="com.Yasna.forum.util.admin.AdminBean"/>
 
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
	
	// make sure the user is authorized to create forums::
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isSystemAdmin ) {
		request.setAttribute("message","No permission to create forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%	/////////////////
	// get parameters
	
	int cacheID = ParamUtils.getIntParameter(request,"cacheID",-1);
	boolean doClear = ParamUtils.getBooleanParameter(request,"doClear");
	boolean doEdit = ParamUtils.getBooleanParameter(request,"doEdit");
	boolean doSave = ParamUtils.getBooleanParameter(request,"doSave");
	int maxSize = ParamUtils.getIntParameter(request,"cacheMaxSize",-1);
	boolean cacheEnabled = ParamUtils.getBooleanParameter(request,"cacheEnabled");
	boolean doCache = ParamUtils.getBooleanParameter(request,"doCache");
	
	DbForumFactory dbForumFactory = null;
	try {
		dbForumFactory = (DbForumFactory)((ForumFactoryProxy)forumFactory).getUnderlyingForumFactory(); 
	} catch (Exception e) { }
	DbCacheManager cacheManager = dbForumFactory.getCacheManager();
	
	// clear the requested cache
	if( doClear ) {
		if( cacheID != -1 ) {
			cacheManager.clear(cacheID);
		}
	}
	
	if( doSave ) {
		if( cacheID != -1 ) {
			Cache cache = cacheManager.getCache(cacheID);
			cache.setMaxSize(maxSize*1024);
		}
	}
	
	// turn the cache on or off
	if( doCache ) {
		cacheManager.setCacheEnabled(cacheEnabled);
	}
	cacheEnabled = cacheManager.isCacheEnabled();
%>

<%!	
	Runtime runtime = Runtime.getRuntime();
%>

<html>
<head>
	<title></title>
	<link rel="stylesheet" href="style/global.css">
	<script language="JavaScript" type="text/javascript">
	function convert(src,dest) {
		if( dest.value == "" ) { return; }
		var unit = src.options[src.selectedIndex].value;
		if( unit == "K" ) {
			dest.value = dest.value * 1024;
		} else if( unit == "MB" ) {
			dest.value = dest.value / 1024;
		}
	}
	</script>
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Cache" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<b>Cache Summary</b>
<ul>
	<%  DecimalFormat formatter = new DecimalFormat("#.00");
	%>
	
	<table bgcolor="#999999" cellpadding="0" cellspacing="0" border="0">
	<td>
	<table cellpadding="4" cellspacing="1" border="0" width="100%">
	<tr bgcolor="#eeeeee">
	<td class="forumCellHeader" align="center"><b>Cache Type</b></td>
	<td class="forumCellHeader" align="center"><b>Size</b></td>
	<td class="forumCellHeader" align="center"><b>Objects</b></td>
	<td class="forumCellHeader" align="center"><b>Effectiveness</b></td>
	<td class="forumCellHeader" align="center">&nbsp;</td>
    </tr>
	<%
		Cache cache;
		double memUsed, totalMem, freeMem, hitPercent;
		long hits, misses;
	%>
		
	<tr bgcolor="#ffffff">
		<% cache = cacheManager.getCache(DbCacheManager.MESSAGE_CACHE); %>
		<td>Message</td>
		<% 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		%> 
		<td><%= formatter.format(totalMem) %> MB,
			<%= formatter.format(freeMem)%>% free
		</td>
		<td align=right>&nbsp;<%= cache.getNumElements() %>&nbsp;</td>
		<% 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		%>
		<td><%= formatter.format(hitPercent)%>% (<%= hits %> hits, <%= misses %> misses)</td> 
		<form action="cache.jsp">
		<input type="hidden" name="doEdit" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.MESSAGE_CACHE %>">
		</form>
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.MESSAGE_CACHE %>">
		<td><input type="submit" value="Clear Cache"></td> 
		</form>
	</tr>
	<%	if( doEdit && cacheID==DbCacheManager.MESSAGE_CACHE ) { %>
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doSave" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.MESSAGE_CACHE %>">
		<td colspan="4">
			Set message size:
			<input type="text" value="<%= cache.getMaxSize()/1024 %>" size="6"
			 name="cacheMaxSize">K 
			 <br>
			 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB
		</td>
		<td colspan="2" align="center"><input type="submit" value="Save"></td>
		</form>
	</tr>
	<%	} %>
	
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.THREAD_CACHE %>">
		<% cache = cacheManager.getCache(DbCacheManager.THREAD_CACHE); %>
		<td>Thread</td>
		<% 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		%> 
		<td><%= formatter.format(totalMem) %> MB,
			<%= formatter.format(freeMem)%>% free
		</td>
		<td align=right>&nbsp;<%= cache.getNumElements() %>&nbsp;</td>
		<% 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		%>
		<td><%= formatter.format(hitPercent)%>% (<%= hits %> hits, <%= misses %> misses)</td> 
		<form action="cache.jsp">
		<input type="hidden" name="doEdit" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.THREAD_CACHE %>">
		</form>
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.THREAD_CACHE %>">
		<td><input type="submit" value="Clear Cache"></td> 
		</form>
	</tr>
	<%	if( doEdit && cacheID==DbCacheManager.THREAD_CACHE ) { %>
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doSave" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.THREAD_CACHE %>">
		<td colspan="4">
			Set message size:
			<input type="text" value="<%= cache.getMaxSize()/1024 %>" size="6"
			 name="cacheMaxSize">K 
			 <br>
			 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB
		</td>
		<td colspan="2" align="center"><input type="submit" value="Save"></td>
		</form>
	</tr>
	<%	} %>
	
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.FORUM_CACHE %>">
		<% cache = cacheManager.getCache(DbCacheManager.FORUM_CACHE); %>
		<td>Forum</td>
		<% 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		%> 
		<td><%= formatter.format(totalMem) %> MB,
			<%= formatter.format(freeMem)%>% free
		</td>
		<td align=right>&nbsp;<%= cache.getNumElements() %>&nbsp;</td>
		<% 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		%>
		<td><%= formatter.format(hitPercent)%>% (<%= hits %> hits, <%= misses %> misses)</td> 
		<form action="cache.jsp">
		<input type="hidden" name="doEdit" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.FORUM_CACHE %>">
		</form>
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.FORUM_CACHE %>">
		<td><input type="submit" value="Clear Cache"></td> 
		</form>
	</tr>
	<%	if( doEdit && cacheID==DbCacheManager.FORUM_CACHE ) { %>
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doSave" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.FORUM_CACHE %>">
		<td colspan="4">
			Set message size:
			<input type="text" value="<%= cache.getMaxSize()/1024 %>" size="6"
			 name="cacheMaxSize">K 
			 <br>
			 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB
		</td>
		<td colspan="2" align="center"><input type="submit" value="Save"></td>
		</form>
	</tr>
	<%	} %>
	
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.USER_CACHE %>">
		<% cache = cacheManager.getCache(DbCacheManager.USER_CACHE); %>
		<td>User</td>
		<% 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		%> 
		<td><%= formatter.format(totalMem) %> MB,
			<%= formatter.format(freeMem)%>% free
		</td>
		<td align=right>&nbsp;<%= cache.getNumElements() %>&nbsp;</td>
		<% 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		%>
		<td><%= formatter.format(hitPercent)%>% (<%= hits %> hits, <%= misses %> misses)</td> 
		<form action="cache.jsp">
		<input type="hidden" name="doEdit" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.USER_CACHE %>">
		</form>
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.USER_CACHE %>">
		<td><input type="submit" value="Clear Cache"></td> 
		</form>
	</tr>
	<%	if( doEdit && cacheID==DbCacheManager.USER_CACHE ) { %>
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doSave" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.USER_CACHE %>">
		<td colspan="4">
			Set message size:
			<input type="text" value="<%= cache.getMaxSize()/1024 %>" size="6"
			 name="cacheMaxSize">K 
			 <br>
			 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB
		</td>
		<td colspan="2" align="center"><input type="submit" value="Save"></td>
		</form>
	</tr>
	<%	} %>
	
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.GROUP_CACHE %>">
		<% cache = cacheManager.getCache(DbCacheManager.GROUP_CACHE); %>
		<td>Group</td>
		<% 
			memUsed = (double)cache.getSize()/(1024*1024);
			totalMem = (double)cache.getMaxSize()/(1024*1024);
			freeMem = 100 - 100*memUsed/totalMem;
		%> 
		<td><%= formatter.format(totalMem) %> MB,
			<%= formatter.format(freeMem)%>% free
		</td>
		<td align=right>&nbsp;<%= cache.getNumElements() %>&nbsp;</td>
		<% 
			hits = cache.getCacheHits();
			misses = cache.getCacheMisses();
			if (hits + misses == 0) { hitPercent = 0.0; }
			else { hitPercent = 100*(double)hits/(hits+misses); }
		%>
		<td><%= formatter.format(hitPercent)%>% (<%= hits %> hits, <%= misses %> misses)</td> 
		<form action="cache.jsp">
		<input type="hidden" name="doEdit" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.GROUP_CACHE %>">
		</form>
		<form action="cache.jsp">
		<input type="hidden" name="doClear" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.GROUP_CACHE %>">
		<td><input type="submit" value="Clear Cache"></td> 
		</form>
	</tr>
	<%	if( doEdit && cacheID==DbCacheManager.GROUP_CACHE ) { %>
	<tr bgcolor="#ffffff">
		<form action="cache.jsp">
		<input type="hidden" name="doSave" value="true">
		<input type="hidden" name="cacheID" value="<%= DbCacheManager.GROUP_CACHE %>">
		<td colspan="4">
			Set message size:
			<input type="text" value="<%= cache.getMaxSize()/1024 %>" size="6"
			 name="cacheMaxSize">K 
			 <br>
			 1024 K = 1 MB, 2048 K = 2 MB, 3072 K = 3 MB
		</td>
		<td colspan="2" align="center"><input type="submit" value="Save"></td>
		</form>
	</tr>
	<%	} %>
			
	</table>
	</td>
	</table>

</ul>


<p>
<b>Java VM Memory</b>
<ul>
 	<%   
		double freeMemory = (double)runtime.freeMemory()/(1024*1024);
		double totalMemory = (double)runtime.totalMemory()/(1024*1024);
		double usedMemory = totalMemory - freeMemory;
		double percentFree = ((double)freeMemory/(double)totalMemory)*100.0;
	%>
	<table border=0>
	<tr><td>Used Memory:</td><td><%= formatter.format(usedMemory) %> MB</td></tr>
	<tr><td>Total Memory:</td><td><%= formatter.format(totalMemory) %> MB</td></tr>
	</table>
	<br>
	<!--
	<table bgcolor="#000000" cellpadding="1" cellspacing="0" border="0" width="300">
	<td>
	<table bgcolor="#ffffff" cellpadding="0" cellspacing="0" border="0" width="100%">
	<td width="<%= percentFree %>%" background="images/cache.gif"><img src="images/blank.gif" width="<%= percentFree %>" height="20" border="0"></td>
	<td width="<%= 100-percentFree %>%"></td>
	</table>
	</td>
	</table>
	<b><%= formatter.format(percentFree) %>% free</b>
	<p>
	-->
	<%	int free = 100-(int)Math.round(percentFree); %>
	
	<%	int tableWidth = 200; %>
	<%	int numBlocks = 50; %>
<table border=0><td>	
<table bgcolor="#000000" cellpadding="1" cellspacing="0" border="0" width="<%= tableWidth %>" align=left>
<td>
<table bgcolor="#000000" cellpadding="1" cellspacing="1" border="0" width="100%">
<% for( int i=0; i<numBlocks; i++ ) { %>
<%		if( (i*(100/numBlocks)) < free ) { %>
	<td bgcolor="#00ff00" width="<%= (100/numBlocks) %>%"><img src="blank.gif" width="1" height="15" border="0"></td>
<%		} else { %>
	<td bgcolor="#006600" width="<%= (100/numBlocks) %>%"><img src="blank.gif" width="1" height="15" border="0"></td>
<%		} %>
<%	} %>
</table>
</td>
</table></td><td> &nbsp;<b><%= formatter.format(percentFree) %>% free</b> </td></table>
		
</ul>

<p>

<b>Cache Status</b>
<ul>

<form action="cache.jsp">
<input type="hidden" name="doCache" value="true">
<table bgcolor="#666666" cellpadding="0" cellspacing="0" border="0" width="300">
<td>
<table bgcolor="#666666" cellpadding="3" cellspacing="1" border="0" width="100%">
<tr bgcolor="#ffffff">
	<td align="center"<%= (cacheEnabled)?" bgcolor=\"#99cc99\"":"" %>>
		<input type="radio" name="cacheEnabled" value="true" id="rb01"
		 <%= (cacheEnabled)?"checked":"" %>>
		<label for="rb01"><%= (cacheEnabled)?"<b>On</b>":"On" %></label>
	</td>
	<td align="center"<%= (!cacheEnabled)?" bgcolor=\"#cc6666\"":"" %>>
		<input type="radio" name="cacheEnabled" value="false" id="rb02"
		 <%= (!cacheEnabled)?"checked":"" %>>
		<label for="rb02"><%= (!cacheEnabled)?"<b>Off</b>":"Off" %></label>
	</td>
	<td align="center">
		<input type="submit" value="Update">
	</td>
</tr>
</table>
</td>
</table>
</ul>

</body>
</html>


