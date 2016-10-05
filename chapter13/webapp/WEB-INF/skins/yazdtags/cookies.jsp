<%-- Cookies JSP, determines if user has cookies enabled --%>

<%-- initialize jsp tag libraries --%>
<%@ taglib uri="yazdforum.tld" prefix="jf" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/response-1.0" prefix="res" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/session-1.0" prefix="sess" %>

<req:issessionfromcookie value="false">
 <%-- User does not have cookies enabled, return back a
      page telling them they must enable cookies to use
      the forum --%>
 <html>
 <head>
  <title>Yazd JSP Tag Library Skin</title>
 </head>

 <body bgcolor="#ffffff" text="#000000" link="#0033cc" vlink="#663366" alink="#ff3300">

 <%-- Display forum banner --%>
 <center>
  <h1>Yazd JSP Tag Library Skin</h1>
 </center>

 <hr size="0">
 <CENTER>
  You need to enable Cookies in your web browser before you can use the Forum.
 </CENTER>
 </body>
 </html>
 <%-- Invalidate the users session so it doesn't take
      up memory in JVM and can be recycled by servlet
      container. --%>
 <sess:invalidate/>
 <%-- Thats all, send the above back to user telling
      them they must enable cookies to use the forum --%>
 <res:skippage/>
</req:issessionfromcookie>

<%-- authorize all users and initialize forum state information --%>
<jf:authorize id="req" anonymous="true"/>

<%-- Redirect users back to the page they were that
     triggered the cookie check --%>
<sess:existsattribute name="redirect">
 <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
</sess:existsattribute>
<%-- If there is no redirect setup, send them back to main page --%>
<res:sendredirect>index.jsp</res:sendredirect>
