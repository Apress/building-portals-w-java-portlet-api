 <%@ page session="true" %>
 
 <%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>
 <%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
 
 
 <%@ page import="java.util.*"%>

 

 <c:set var="item" value="${ToDoList[param.ITEM_ID]}"/>
 
 <a href="<portlet:renderURL portletMode="edit"/>">Back to Edit All Items</a>
 
 
 <form action="<portlet:actionURL>
 	<portlet:param name="COMMAND" value="EDIT"/>
 	<portlet:param name="ITEM_ID" value="<%=request.getParameter("ITEM_ID")%>"/>
 </portlet:actionURL>" 
 
 method="post">
 	Description: <input type="text" size="30" 
 	value="<c:out value="${item.description}"/>" name="DESCRIPTION">
 	<br>
 	Priority: <input type="text" size="2" 
 	value="<c:out value="${item.priority}"/>" name="PRIORITY">
 	<br>
 	<input type="submit" text="Create">
 	
</form>


