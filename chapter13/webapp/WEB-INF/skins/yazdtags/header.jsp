<%-- Header JSP, included at JSP compile time by most JSP pages
     for forum.  Requires that the page including this has:

     Initialized the yazdforum tag library using prefix "jf",
     the session tag library using prefix "sess", and has used
     the jf:authorize tag to create the script variable "jr". --%>

<html>
<head>
 <title>Yazd JSP Tag Library Skin</title>
</head>

<body bgcolor="#ffffff" text="#000000" link="#0033cc" vlink="#663366" alink="#ff3300">

<%-- Display forum banner --%>
<center>
 <h1>Yazd JSP Tag Library Skin</h1>
</center>

<center>
 <%-- For anonymous user display login banner --%>
 <jf:anonymous_user>
  You must have an account and be logged in to post messages.
  <br>
  Have an account, <a href="login.jsp">Login</a>. Need an account,
  <a href="create.jsp">Create</a> one.
 </jf:anonymous_user>

 <%-- For a user that is logged in, display logout and account banner --%>
 <jf:anonymous_user value="false">
  <a href="logout.jsp">Logout</a> of Forum.
  <a href="account.jsp">Modify</a> your account.
 </jf:anonymous_user>
</center>

<hr size="0">

<%-- Display any error messages that exist --%>
<jf:if_error>
 <center>
  <b>Errors</b>
 </center>
 <BR>
 <jf:error_loop id="errs">
  <li><jsp:getProperty name="errs" property="error"/></li>
 </jf:error_loop>
 <%-- Make sure errors get deleted now that they have been displayed --%>
 <jsp:setProperty name="jr" property="error" value=""/>
 <hr size="0">
</jf:if_error>

<%-- Display the status message if it exists --%>
<sess:existsattribute name="status">
 <center>
  <sess:attribute name="status"/>
 </center>
 <%-- Make sure status message gets deleted now that it has been displayed --%>
 <sess:removeattribute name="status"/>
 <hr size="0">
</sess:existsattribute>
