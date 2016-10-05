
<%
/**
 *	$RCSfile: userChooser.jsp,v $
 *	$Revision: 1.1.1.1 $
 *	$Date: 2002/04/26 23:50:05 $
 */
%>

<%	/////////////////////
	// get an iterator of users and display a list
	
	Iterator userIterator = manager.users();
	if( !userIterator.hasNext() ) {
%>
		No users!
		<br>
		Try <a href="createUser.jsp">creating one</a>.
<%
	} else {
%>

	<%	if( formAction.equals("edit") ) { %>
		<form action="editUser.jsp">
	<%	} else if( formAction.equals("remove") ) { %>
		<form action="removeUser.jsp">
	<%	} %>
	
	<select name="user">
	<option value="">Choose a user:
	<%	while( userIterator.hasNext() ) {
			User tmpUser = (User)userIterator.next();
	%>
				<option value="<%= tmpUser.getUsername() %>"> &gt;
				<%= tmpUser.getUsername() %> (<%= tmpUser.getName() %>)
	<%
		}
	%>
	</select>
	
	<%	if( formAction.equals("edit") ) { %>
		<input type="submit" value="Edit User Properties...">
	<%	} else if( formAction.equals("remove") ) { %>
		<input type="submit" value="Remove This User...">
	<%	} %>

	</form>
	
<%	} %>

