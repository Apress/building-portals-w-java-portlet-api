
<%
/**
 *	$RCSfile: forumChooser.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:04 $
 */
%>

<%	/////////////////////
	// get an iterator of forums and display a list
	
	Iterator forumIterator = forumFactory.forums();
	if( !forumIterator.hasNext() ) {
%>
		No forums!
		<br>
		Try <a href="createForum.jsp">creating one</a>.
<%
	} else {
%>

	<%	if( formAction.equals("edit") ) { %>
		<form action="editForum.jsp">
	<%	} else if( formAction.equals("remove") ) { %>
		<form action="removeForum.jsp">
	<%	} else if( formAction.equals("filters") ) { %>
		<form action="forumFilters.jsp">
	<%	} else if( formAction.equals("content") ) { %>
		<form action="forumContent.jsp">
	<%	} %>
	
	<select name="forum">
	<%	while( forumIterator.hasNext() ) {
			Forum tmpForum = (Forum)forumIterator.next();
	%>
			<option value="<%= tmpForum.getID() %>"><%= tmpForum.getName() %>
	<%	}
	%>
	</select>
	
	<%	if( formAction.equals("edit") ) { %>
		<input type="submit" value="Edit Properties...">
	<%	} else if( formAction.equals("remove") ) { %>
		<input type="submit" value="Remove This Forum...">
	<%	} else if( formAction.equals("filters") ) { %>
		<input type="submit" value="Work with filters...">
	<%	} else if( formAction.equals("content") ) { %>
		<input type="submit" value="Forum Content...">
	<%	} %>

	</form>
	
<%	} %>
