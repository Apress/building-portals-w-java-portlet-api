<%-- Logout JSP, logout a user from Yazd --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize only users who have been logged in and
     initialize forum state information --%>
<jf:authorize id="jr">
  <%-- user wasn't logged in, redirect them to the main page --%>
  <sess:setattribute name="status">Logout failed, you were not logged in.</sess:setattribute>
  <res:sendredirect>index.jsp</res:sendredirect>
</jf:authorize>

<%-- logout user --%>
<jf:logout/>
<%-- Set status message to inform user they were logged out of Yazd --%>
<sess:setattribute name="status">You have been logged out.</sess:setattribute>
<%-- Redirect user back to main page --%>
<res:sendredirect>index.jsp</res:sendredirect>
