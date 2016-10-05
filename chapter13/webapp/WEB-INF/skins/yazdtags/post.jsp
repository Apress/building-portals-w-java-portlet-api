<%-- Post JSP, displays a form to post a new message thread
     to a forum, posts the message when this form is submitted --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize logged in users and initialize forum state information --%>
<jf:authorize id="jr">
  <%-- User is not logged in, tell them and send them to the login page --%>
  <sess:setattribute name="status">You must be logged in to post a message.</sess:setattribute>
  <res:sendredirect>/login.jsp</res:sendredirect>
</jf:authorize>

<%-- Setup redirect if not done already --%>
<sess:existsattribute name="redirect" value="false">
 <sess:setattribute name="redirect">index.jsp</sess:setattribute>
</sess:existsattribute>

<%-- See if user cancelled --%>
<req:existsparameter name="cancel">
 <%-- User cancelled, redirect them back to previous page --%>
 <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
</req:existsparameter>

<%-- post the message if this is a form submission
     and all required input parameters exist --%>
<jf:post id="post">
 <%-- Append user's signature to message body --%>
 <jf:account id="user">
  <jf:post_append>

<jf:getYazdProperty name="user" property="signature"/>
  </jf:post_append>
 </jf:account>
 <jf:post_message>
  <%-- Message posted, tell user using status message --%>
  <sess:setattribute name="status">New Message Posted</sess:setattribute>
  <%-- Redirect user back to page they were on before selecting to
       post a message --%>
  <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
 </jf:post_message>
</jf:post>

<%@ include file="header.jsp" %>

<center>
 <%-- bread crumbs --%>
 <b>
  <a href="index.jsp">Home</a>
  &gt;
  <jf:forum id="cf">
   <a href="viewForum.jsp?forum=<jsp:getProperty name="cf" property="forumid"/>">
    <jsp:getProperty name="cf" property="name"/>
   </a>
  </jf:forum>
 </b>
 <%-- end bread crumbs --%>

 <p>
 <h3>Post a new message</h3>
</center>

<%-- display the HTML input form for posting a message --%>
<form action="post.jsp" method="post" name="postForm">
 <table cellpadding="3" cellspacing="0" border="0">
  <tr>
   <td><b>Subject</b></td>
   <td colspan=2>
    <input type="text" name="subject" size="50" maxlength="100">
   </td>
  </tr>
  <tr>
   <td valign="top">
    <b>Message</b>
   </td>
   <td colspan=2>
    <textarea name="body" cols="80" rows="10" wrap="physical"></textarea>
   </td>
  </tr>
  <tr>
   <td>&nbsp;</td>
   <td>
    <input type="submit" name="post" value="Post New Message">
   </td>
   <td>
    <input type="submit" name="cancel" value="Cancel">
   </td>
  </tr>
 </table>
</form>

<%@ include file="footer.jsp" %>
