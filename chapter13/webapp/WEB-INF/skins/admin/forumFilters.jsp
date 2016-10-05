
<%
/**
 *	$RCSfile: forumFilters.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%@ page import="java.util.*,
                 com.Yasna.forum.*,
                 com.Yasna.forum.util.*,
				 com.Yasna.forum.util.admin.*"%>

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
	////////////////////
	// Security check
	
	// make sure the user is authorized to administer users:
	ForumFactory forumFactory = ForumFactory.getInstance(authToken);
	ForumPermissions permissions = forumFactory.getPermissions(authToken);
	boolean isSystemAdmin = permissions.get(ForumPermissions.SYSTEM_ADMIN);
	boolean isUserAdmin   = permissions.get(ForumPermissions.FORUM_ADMIN);
	
	// redirect to error page if we're not a forum admin or a system admin
	if( !isUserAdmin && !isSystemAdmin ) {
		request.setAttribute("message","No permission to administer forums");
		response.sendRedirect("error.jsp");
		return;
	}
%>

<%!	// Global variables/methods
	private final int ADD_FILTER = 1;
	private final int REMOVE_FILTER = 2;
	private final int EDIT_FILTER = 3;
	private final int SAVE_MODIFIED_FILTER = 4;
	private final int SAVE_PROPERTIES = 5;
	private final String[] classNames = { "com.Yasna.forum.filter.FilterHtml",
			"com.Yasna.forum.filter.FilterCodeHighlight",
			"com.Yasna.forum.filter.FilterFontStyle",
			"com.Yasna.forum.filter.FilterNewline",
			"com.Yasna.forum.filter.FilterSmileyFace",
			"com.Yasna.forum.filter.FilterURLConverter",
			"com.Yasna.forum.filter.FilterProfanity",
			"com.Yasna.forum.filter.FilterHackerSpeak"};
   	private boolean containsString( ForumMessageFilter[] installedFilters, String filterName ) {
		for( int i=0; i<installedFilters.length; i++ ) {
			if( installedFilters[i].getName().equals(filterName) ) {
				return true;
			}
		}
		return false;
	}
	private String escapeHTML( String line ) {
		StringBuffer buf = new StringBuffer();
		for( int i=0; i<line.length(); i++ ) {
			char ch = line.charAt(i);
			if( ch == '"' ) {
				buf.append( "&quot;" ); //"
			} else if( ch == '&' ) {
				buf.append( "&amp;" );
			} else {
				buf.append( ch );
			}
		}
		return buf.toString();
	}
	private String unescapeHTML( String line ) {
		//line = replace( line, "&gt;", ">" );
		//line = replace( line, "&lt;", "<" );
		line = replace( line, "&quot;", "\"" ); //"
		line = replace( line, "&amp;", "&" );
		return line;
	}
	private String replace( String line, String oldString, String newString ) {
		int i=0;
		if ( ( i=line.indexOf( oldString, i ) ) >= 0 ) {
			int oLength = oldString.length();
			int nLength = newString.length();
			StringBuffer buf = new StringBuffer();
			buf.append(line.substring(0,i)).append(newString);
			i += oLength;
			int j = i;
			while( ( i=line.indexOf( oldString, i ) ) > 0 ) {
				buf.append(line.substring(j,i)).append(newString);
				i += oLength;
				j = i;
			}
			buf.append(line.substring(j));
			return buf.toString();
		}
		return line;
	}
%>
 
<%	////////////////////
	// get parameters
	
	int forumID   = ParamUtils.getIntParameter(request,"forum",-1);
	int mode      = ParamUtils.getIntParameter(request,"mode",-1);
	int filterID  = ParamUtils.getIntParameter(request,"filter",-1);
	//////////////////////////////////
	// global error variables
	
	String errorMessage = "";
	
	boolean noForumSpecified = (forumID < 0);
	
	boolean filterIDOK = (filterID != -1 );
	boolean doMode = (mode != -1);
    ////////////////////
	// make a profile manager
	ProfileManager manager = forumFactory.getProfileManager();

    // Create the factory and forum objects
	Forum forum = null;
	if (!noForumSpecified)
	{
	    try {
		    forum = forumFactory.getForum(forumID);
	    }
	    catch( UnauthorizedException ue ) { System.err.println( "... in admin/admin-filter.jsp: " + ue ); }
	    catch( ForumNotFoundException fnfe ) { System.err.println( "... in admin/admin-filter.jsp: " + fnfe ); }
	    catch( Exception e ) {
		    System.err.println( "... in admin/admin-filter.jsp: " + e );
		    e.printStackTrace( System.err );
	    }
	}
	boolean forumOK = (forum != null);
	// Get an array of all filters in the system
	ForumMessageFilter[] allFilters = null;
	if (!noForumSpecified)
	{
	    try {
		    allFilters = new ForumMessageFilter[classNames.length];
		    for( int i=0; i<classNames.length; i++ ) {
			    Class c = Class.forName(classNames[i]); // load the name of the filter
			    allFilters[i] = (ForumMessageFilter)(c.newInstance());
		    }
	    }
	    catch( ClassNotFoundException cnfe ) { System.err.println( "... in admin/admin-filter.jsp: " + cnfe ); }
	    catch( IllegalAccessException iae ) { System.err.println( "... in admin/admin-filter.jsp: " + iae ); }
	    catch( InstantiationException ie ) { System.err.println( "... in admin/admin-filter.jsp: " + ie ); }
	}
	    // Get an array of installed filters
	    ForumMessageFilter[] installedFilters = null;
	    String[] installedFilterNames = null;
	    if( forumOK ) {
		    try {
			    installedFilters = forum.getForumMessageFilters();
		    }
		    catch( UnauthorizedException ue ) { 
			    System.err.println( "... in admin/admin-filter.jsp: " + ue );
		    }
		    if( installedFilters != null ) {
			    // build up list of class names of the forums (so we can not display the ones that are
			    // installed in the "add new webfilter box"
			    installedFilterNames = new String[installedFilters.length];
			    for( int i=0; i<installedFilterNames.length; i++ ) {
				    installedFilterNames[i] = installedFilters[i].getClass().getName();
			    }
		    }
	    }
	    // Based on the mode, do an action:
	    if( doMode ) {
		    // Add a filter
		    if( mode == ADD_FILTER ) {		
			    if( filterIDOK ) {	
		            try {
		                Class c = Class.forName(classNames[filterID]); // load the name of the filter
					    ForumMessageFilter newFilter = (ForumMessageFilter)(c.newInstance());
					    forum.addForumMessageFilter( newFilter );
		            }
		            catch( ClassNotFoundException cnfe ) { System.err.println( "... in admin/admin-filter.jsp: " + cnfe ); }
				    catch( IllegalAccessException iae ) { System.err.println( "... in admin/admin-filter.jsp: " + iae ); }
				    catch( InstantiationException ie ) { System.err.println( "... in admin/admin-filter.jsp: " + ie ); }
				    catch( UnauthorizedException ue ) { System.err.println( "... in admin/admin-filter.jsp: " + ue ); }
			    }
			    // redirect to same page 
			    response.sendRedirect( "forumFilters.jsp?forum=" + forumID );
				return;
		    }
		    else if( mode == REMOVE_FILTER ) {
			    if( filterIDOK ) {
				    try {
					    forum.removeForumMessageFilter(filterID);
				    } catch( UnauthorizedException ue ) { System.err.println( "... in admin/admin-filter.jsp: " + ue ); }
			    }
			    // redirect to same page
			    response.sendRedirect( "forumFilters.jsp?forum=" + forumID );
				return;
		    }
		    else if( mode == SAVE_PROPERTIES ) {
			    if( filterIDOK ) {
				    Enumeration props = installedFilters[filterID].filterPropertyNames();
				    while( props.hasMoreElements() ) {
					    String propName = (String)props.nextElement();
					    String propValue = request.getParameter(propName);
					    if( propValue != null ) {
						    try {
							    installedFilters[filterID].setFilterProperty(propName,propValue);
						    } catch( IllegalArgumentException iae ) { System.err.println( "... in admin/admin-filter.jsp: " + iae ); }
					    }
				    }
			    }
		    }
		    else {
			    // do nothing
		    }
	    }
%>

<html>
<head>
	<title></title>
<%
    if (!noForumSpecified)
    {
%>

	<script language="JavaScript" type="text/javascript">
	<%-- JavaScript array of filter descriptions --%>
	var descriptions = new Array(
		<%	for( int i=0; i<allFilters.length; i++ ) { %>
			"<%= unescapeHTML(allFilters[i].getDescription()) %>"
			<%	if( (allFilters.length-i) > 1 ) { %>
			,
			<%	} %>
		<%	} %> );
	function doDesc( theForm ) {
		var selected = theForm.filter.options[theForm.filter.selectedIndex].value;
		theForm.filterDesc.value = descriptions[selected];
	}
	</script>
<%
    }
%>
	<link rel="stylesheet" href="style/global.css">
</head>

<body background="images/shadowBack.gif" bgcolor="#ffffff" text="#000000" link="#0000ff" vlink="#800080" alink="#ff0000">

<%	///////////////////////
	// pageTitleInfo variable (used by include/pageTitle.jsp)
	String[] pageTitleInfo = { "Forums : Forum Filters" };
%>
<%	///////////////////
	// pageTitle include
%><%@ include file="include/pageTitle.jsp" %>

<p>

<%	////////////////////
	// display a list of groups to edit if no group was specified:
	if( noForumSpecified ) {
		String formAction = "filters";
%>
		<%@ include file="forumChooser.jsp"%>
<%		out.flush();
		return;
	}
%>


<%	/////////////////////
	// at this point, we know there is a group to work with:
	//Forum forum                  = forumFactory.getForum(forumID);
	String forumName             = forum.getName();
%>

Filters for: <b><%= forumName %></b>

<p>


	<%-- table of Name/Version/Description/Edit/Remove --%>
	<table cellpadding=5 cellspacing=0 border=1 width="100%">
	<tr bgcolor="#eeeeee">
		<td align="center">Current WebFilters installed for forum <b><%= forum.getName() %></b></td>
	</tr>
	<tr>
		<td>
		<%	if( installedFilters==null || installedFilters.length == 0 ) { %>
			<i>No filters installed. (You can install one below)</i>
		<%	} else { %>
			<table cellpadding=3 cellspacing=0 border=1 width="100%">
			<tr bgcolor="#eeeeee">
				<td width="1%" nowrap>Name</td>
				<td width="1%" nowrap>Version</td>
				<td width="97%">Description</td>
				<td width="1%" nowrap align="center">Properties</td>
				<td width="1%" nowrap align="center">Remove</td>
			</tr>
			<%	for( int i=0; i<installedFilters.length; i++ ) { %>
				<tr>
					<td><%= installedFilters[i].getName() %></td>
					<td align="center"><%= installedFilters[i].getMajorVersion() %>.<%= installedFilters[i].getMinorVersion() %></td>
					<td><%= installedFilters[i].getDescription() %></td>
					<form name="editFilterForum">
						<input type="hidden" name="forum" value="<%= forumID %>">
						<input type="hidden" name="filter" value="<%= i %>">
						<input type="hidden" name="mode" value="<%= EDIT_FILTER %>">
						<td align="center"><input type="submit" value="Edit.."></td>
					</form>
					<form name="removeFilterForm">
						<input type="hidden" name="forum" value="<%= forumID %>">
						<input type="hidden" name="filter" value="<%= i %>">
						<input type="hidden" name="mode" value="<%= REMOVE_FILTER %>">
						<td align="center"><input type="radio" value="" name="remove<%= i %>" onclick="if(confirm('Are you sure you want to remove this filter?')){this.form.submit();}"></td>
					</form>
				</tr>
				<%	if( mode==EDIT_FILTER && filterID==i ) { %>
				
					<tr>
						<!-- <td><br></td> -->
						<td colspan=5>
							<%-- begin of props table --%>
							<table cellpadding=2 cellspacing=0 border=1 width="100%">
							<form action="forumFilters.jsp">
							<input type="hidden" name="forum" value="<%= forumID %>">
							<input type="hidden" name="mode" value="<%= SAVE_PROPERTIES %>">
							<input type="hidden" name="filter" value="<%= i %>">
							<%	Enumeration props = installedFilters[i].filterPropertyNames();
								if( props == null ) {
							%>
									<tr>
										<td colspan=4><i>No properties to set</i></td>
									</tr>
							<%	} else { %>
									<tr>
										<td width="1%" nowrap>Name</td>
										<td width="1%" nowrap>Value</td>
										<td width="97%">Description</td>
										<td width="1%" rowspan=99 align="center"><input type="submit" value="Save"></td>
									</tr>
							<%		while( props.hasMoreElements() ) {
										String propName = (String)props.nextElement();
							%>
									<tr>
										<td><%= propName %></td>
										<td><input type="text" name="<%= propName %>" size="25" value="<%= escapeHTML(installedFilters[i].getFilterProperty(propName)) %>"></td>
										<td><%= installedFilters[i].getFilterPropertyDescription(propName) %></td>
									</tr>
							<%		} %>
							<%	} %>
							</form>
							</table>
							<%-- end of props table --%>
						</td>
					</tr>
				
					<%-- end edit filter --%>
		<%			} %>
		<%		} %>
		<%	} %>
		</table>
		</td>
	</tr>
	</table>
	<%-- end table of Name/Version/Description/Edit/Remove --%>
	
	<br>
	
	<%-- table of Install New WebFilter --%>
	<table cellpadding=5 cellspacing=0 border=1 width="50%">
	<tr bgcolor="#eeeeee">
		<td align="center">Install New WebFilter</td>
	</tr>
	<tr>
		<td>
		<table cellpadding=3 cellspacing=0 border=1 width="100%">
		<form action="forumFilters.jsp">
		<input type="hidden" name="forum" value="<%=forumID%>">
		<input type="hidden" name="mode" value="<%=ADD_FILTER%>">
		<tr>
			<td width="1%" nowrap align="center" valign="top">
			Filters:<br>
			<select name="filter" size="7" onchange="doDesc(this.form);">
			<%	for( int i=0; i<allFilters.length; i++ ) {
					// if the filter is not currently installed, show it as 
					// being available for install.
					if( !containsString( installedFilters, allFilters[i].getName() ) ) {
			%>
					<option value="<%= i %>"><%= allFilters[i].getName() %>
			<%		} %>
			<%	} %>
			</select>
			</td>
			<td width="98%"><br></td>
			<td width="1%" nowrap align="center" valign="top">
				Description:<br>
				<textarea rows="6" cols="20" name="filterDesc" wrap="virtual"></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="center"><input type="submit" value="Install..."></td>
		</tr>
		</form>
		</table>
		</td>
	</tr>
	</table>
	<%-- end table of Install New WebFilter --%>


</body>
</html>

