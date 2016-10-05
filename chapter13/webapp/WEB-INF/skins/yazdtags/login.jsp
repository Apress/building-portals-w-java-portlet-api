<%-- Login JSP, displays login page and logs
     in the user when they submit the page --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize all users and initialize forum state information --%>
<jf:authorize id="jr" anonymous="true"/>

<%-- Attempt to login user using this pages HTML form input parameters --%>
<jf:login>
 <%-- Login was successful, report same back to user --%>
 <sess:setattribute name="status">Login Successful</sess:setattribute>
 <%-- Set the users timezone for their session --%>
 <jf:account id="account">
  <dt:timezone id="tz"><jf:getYazdProperty name="account" property="timezone"/></dt:timezone>
 </jf:account>
 <%-- Redirect users back to the page they were on when they
      selected to login --%>
 <sess:existsattribute name="redirect">
  <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
 </sess:existsattribute>
 <%-- If there is no redirect setup, send them back to main page --%>
 <res:sendredirect>index.jsp</res:sendredirect>
</jf:login>

<%@ include file="header.jsp" %>

<p>

<%-- HTML form input for login page --%>
<center>
<h3>Login to Forum</h3>
<table border=0 cellspacing=0 cellpadding=5 width="100%">
 <form action="login.jsp" method=POST>
  <tr>
   <td align="right" width="50%">Username:</td>
   <td>
    <input type="text" name="username" size="15">
   </td>
  </tr>
  <tr>
   <td align="right" width="50%">Password:</td>
   <td>
    <input type="password" name="password" size="15">
   </td>
  </tr>
  <tr>
   <td>&nbsp;</td>
   <td>
    <input type="submit" name="login" value="Login">
   </td>
  </tr>
 </form>
</table>
<br>
Don't have an account? <a href="create.jsp">Create one</a>
</center>

<%@ include file="footer.jsp" %>
