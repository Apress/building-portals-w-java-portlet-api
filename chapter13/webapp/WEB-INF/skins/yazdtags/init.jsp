<%-- Init JSP, included at JSP compile time
     by most JSP pages for forum --%>

<%-- initialize jsp tag libraries --%>
<%@ taglib uri="yazdforum.tld" prefix="jf" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/response-1.0" prefix="res" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<%-- Get a script variable for this HTTP Request --%>
<req:request id="req"/>

<%-- Make sure user has cookies enabled --%>
<req:issessionfromcookie value="false">
 <%-- Users HTTP request did not contain a cookie, user has no previous session --%>
 <%-- Set the maximum inactive interval in seconds for new user session --%>
 <sess:maxinactiveinterval>1800</sess:maxinactiveinterval>
 <%-- Setup redirect back to the page requested --%>
 <sess:setattribute name="redirect"><jsp:getProperty name="req" property="requestURL"/>?<jsp:getProperty name="req" property="queryString"/></sess:setattribute>
 <%-- Setup a test cookie to return to user --%>
 <res:addcookie name="CookiesEnabled" value="true"></res:addcookie>
 <%-- Now send back a redirect to check if user has cookies enabled. --%>
 <%-- encode the URL with the sessionID just in case user doesn't
      have cookies enabled.  --%>
 <res:sendredirect><res:encoderedirecturl>cookies.jsp</res:encoderedirecturl></res:sendredirect>
</req:issessionfromcookie>

<%-- Initialize users timezone if it doesn't exist --%>
<sess:existsattribute name="tz" value="false">
  <dt:timezone id="tz"></dt:timezone>
</sess:existsattribute>

<%-- Make sure proxy servers don't cache page --%>
<res:addheader name="Expires">-1</res:addheader>
<%-- Tell netscape not to cache page --%>
<res:addheader name="Pragma">no-cache</res:addheader>
<%-- Disable MS IE aggressive caching --%>
<res:addheader name="Cache-Control">no-store</res:addheader>
<res:addheader name="Cache-Control">no-cache</res:addheader>
<res:addheader name="Cache-Control">must-revalidate</res:addheader>
<res:addheader name="Cache-Control">post-check=0</res:addheader>   
<res:addheader name="Cache-Control">pre-check=0</res:addheader>
