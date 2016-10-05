<%@ page session="true" %>

<%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %>



<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.jsp.jstl.core.LoopTagStatus"%>


<table>
	<tr class="portlet-section-header">
		<td class="portlet-section-header">Description</td>
		<td>Priority</td>
		<td>Created Date</td>
		<td>Status</td>
	</tr>
<c:forEach var="item" 
              items="${ToDoList}" 
              varStatus="status">
              	<%
  	         LoopTagStatus status = (LoopTagStatus) pageContext.getAttribute("status");
  	         String itemId = Integer.toString(status.getCount() - 1);
		%>              
<tr>
	<td><c:out value="${item.description}"/></td>
	<td><c:out value="${item.priority}"/></td>
	<td><fmt:formatDate value="${item.submittedDate}" dateStyle="short"/></td>
	<td>
	  <c:choose>
	    <c:when test="${item.status}">

	      Complete <a href="<portlet:actionURL>
 					<portlet:param name="COMMAND" value="MARK_UNFINISHED"/>
 					<portlet:param name="ITEM_ID" value="<%=itemId%>"/>
				</portlet:actionURL>"
	      >(change)</a>
	    </c:when>
	    <c:otherwise>
	    
	      Incomplete <a href="<portlet:actionURL>
 					<portlet:param name="COMMAND" value="MARK_FINISHED"/>
 					<portlet:param name="ITEM_ID" value="<%=itemId%>"/>
				</portlet:actionURL>"
	      >(change)</a>
	    </c:otherwise>
	  </c:choose>
	</td>
	<td>

	      <a href="<portlet:renderURL>
 					<portlet:param name="DISPLAY" value="EDIT_PAGE"/>
 					<portlet:param name="ITEM_ID" value="<%=itemId%>"/>
				</portlet:renderURL>"
	      >Edit</a>
	</td>
	<td>
	      <a href="<portlet:actionURL>
 					<portlet:param name="COMMAND" value="DELETE"/>
 					<portlet:param name="ITEM_ID" value="<%=itemId%>"/>
				</portlet:actionURL>"
	      >Delete</a>
	</td>	
</tr>
</c:forEach>


</table>

<form action="<portlet:actionURL>
 	<portlet:param name="COMMAND" value="NEW"/>
</portlet:actionURL>" 
method="post">
	Description: <input type="text" size="30" name="DESCRIPTION">
	<br>
	<input type="submit" text="Create">
</form>