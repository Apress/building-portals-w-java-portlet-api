
<%--
/**
 *	pageTitle.jsp
 */
--%>

<%-- header --%>
<%	if( pageTitleInfo != null ) { %>
	<table class="pageHeaderBg" cellpadding="1" cellspacing="0" border="0" width="100%">
	<td><table class="pageHeaderFg" cellpadding="3" cellspacing="0" border="0" width="100%">
	<td>
	<span class="pageTitle">
	<%	for( int i=0; i<pageTitleInfo.length; i++ ){ %>
		<%= pageTitleInfo[i] %>
		<%	if( (i+1)<pageTitleInfo.length ) { %>
			&nbsp;:&nbsp;
		<%	} %>
	<%	} %>
	</span>
	</td>
	</table></td>
	</table>
<%	} %>
<%-- /header --%>



