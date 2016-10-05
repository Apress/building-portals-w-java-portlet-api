<%-- Change Display Settings JSP --%>
<%-- Only changes settings, does not output HTML --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize all users and initialize forum state information --%>
<jf:authorize id="jr" anonymous="true"/>

<%-- Globally set properties of Yazd Request --%>
<jsp:setProperty name="jr" property="*"/>

<%-- Set status message to let user know their settings were changed --%>
<sess:setattribute name="status">Display Settings Changed</sess:setattribute>

<%-- Redirect user back to the page they were on, new
     settings will be used when page is redisplayed --%>
<sess:existsattribute name="redirect">
 <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
</sess:existsattribute>
<%-- If all else fails, send user back to main page --%>
<res:sendredirect>index.jsp</res:sendredirect>
