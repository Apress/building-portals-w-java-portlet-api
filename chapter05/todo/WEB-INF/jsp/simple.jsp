<%@ page session="true" %>

<%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>

<form action="<portlet:actionURL>
 	<portlet:param name="PORTLET_ACTION" value="CREATE_TIP"/>
</portlet:actionURL>" method="post">

	Title: <input type="text" size="30" name="title">
	<br>
	Description: <input type="text" size="30" name="description">
	<br>	
	<input type="submit" text="Create">
</form>