<%-- Create JSP, create a new user account --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize all users and initialize forum state information --%>
<jf:authorize id="jr" anonymous="true"/>

<%-- Setup redirect if not done already --%>
<sess:existsattribute name="redirect" value="false">
 <sess:setattribute name="redirect">index.jsp</sess:setattribute>
</sess:existsattribute>

<%-- See if user cancelled --%>
<req:existsparameter name="cancel">
 <%-- User cancelled, redirect them back to previous page --%>
 <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
</req:existsparameter>

<%-- If all required parameters are available when form submitted,
     try to create the new account --%>
<jf:create id="create" login="true" confirm="true">
 <%-- Account was created, set status message to inform user --%>
 <sess:setattribute name="status">Your account has been created and you are now logged in.</sess:setattribute>
 <%-- Set the users timezone for their session --%>
 <dt:timezone id="tz"><jf:getYazdProperty name="create" property="timezone"/></dt:timezone>
 <%-- Redirect users back to the page they were on when they
      selected to create the account --%>
 <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
</jf:create>

<%@ include file="header.jsp" %>

<%-- Display the create account HTML form
     If a user submits a create account form that fails,
     previously entered fields are preserved and any
     required fields not yet filled in are flagged
     with a red star --%>
<center><h3>Create a new user account</h3></center>
<p>

<ul>
 <table cellpadding=3 cellspacing=0 border=0>
  <form action="create.jsp" method="post">

   <%-- Users email address, required --%>
   <tr>
    <td align="right">
     <b>Email address:</b>
    </td>
    <td>
     <input type="text" name="email" value="<req:parameter name="email"/>">
     <%-- Use a red star to flag that email address is a required field
          if it has not been entered --%>
     <req:existsparameter name="email" value="false">
      <font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
     </req:existsparameter>
     <i>(required)</i>
    </td>
   </tr>

   <%-- Users forum user name, required, must be unique --%>
   <tr>
    <td align="right">
     <b>Username:</b>
    </td>
    <td>
     <input type="text" name="username" value="<req:parameter name="username"/>">
     <%-- Use a red star to flag that Username is a required field
          if it has not been entered --%>
     <req:existsparameter name="username" value="false">
      <font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
     </req:existsparameter>
     <i>(required)</i>
    </td>
   </tr>

   <%-- Account password and confirmation, required --%>
   <tr>
    <td align="right">
     <b>Password:</b>
    </td>
    <td>
     <input type="password" name="password" value="<req:parameter name="password"/>">
     <%-- Use a red star to flag that password is a required field
          if it has not been entered --%>
     <req:existsparameter name="password" value="false">
      <font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
     </req:existsparameter>
     <i>(required)</i>
    </td>
    <td align="right">
     <b>Confirm:</b>
    </td>
    <td>
     <input type="password" name="confirm" value="<req:parameter name="confirm"/>">
     <%-- Use a red star to flag that password confirmation is required
          if it has not been entered --%>
     <req:existsparameter name="confirm" value="false">
      <font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
     </req:existsparameter>
     <i>(required)</i>
    </td>
   </tr>

   <%-- User real name, optional --%>
   <tr>
    <td align="right">Name:</td>
    <td><input type="text" name="name" value="<req:parameter name="name"/>"></td>
   </tr>

   <%-- URL for user, optional --%>
   <tr>
    <td align="right">URL:</td>
    <td><input type="text" name="url" value="<req:parameter name="url"/>"></td>
   </tr>

   <%-- User signature text, appended to all messages they post, optional --%>
   <tr>
    <td align="right">Signature<br>(255 characters only)</td>
    <td>
     <textarea cols=30 rows=3 name="signature" wrap="physical"><req:parameter name="signature"/></textarea>
    </td>
   </tr>

   <%-- Should users real name be visible, defaults to no --%>
   <tr>
    <td align="right">
     <input type="checkbox" name="nameVisible" value="checked" <req:parameter name="nameVisible"/>>
    </td>
    <td>Hide my name</td>
   </tr>

   <%-- Should users email address be visible, defaults to no --%>
   <tr>
    <td align="right">
     <input type="checkbox" name="emailVisible" value="checked" <req:parameter name="emailVisible"/>>
    </td>
    <td>Hide my email</td>
   </tr>

   <%-- Users can set their preferences for how deep
        into a tree of messages a the full text of the
        message will be displayed.  Messages deeper
        than this will have a one line summary. --%>
   <tr>
    <td align="right">Message Depth</td>
    <td>
     <select name="messageDepth">
      <jf:foreach id="loop" begin="0" end="20"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="messageDepth"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value="<jsp:getProperty name="loop" property="value"/>"><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach>
     </select>
    </td>
   </tr>
   <%-- Users can set their preferences for how deep
        into a tree of messages a message summary will
        be displayed when viewing messages for a thread.
        Messages deeper than this value can be listed as
        X number of messages below your threshold. --%>
   <tr>
    <td align="right">Thread Depth</td>
    <td>

     <select name="threadDepth">
      <jf:foreach id="loop" begin="1" end="20"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="threadDepth"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value="<jsp:getProperty name="loop" property="value"/>"><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach>
     </select>
    </td>
   </tr>

   <%-- Users can set their preferences for how many
        items per page they can view, such as forums,
        threads, and messages.  Their preference is
        only in affect if they are logged in. --%>
   <tr>
    <td align="right">Items Per Page</td>
    <td>
     <select name="itemsPerPage">
      <jf:foreach id="loop" begin="5" end="200" step="5"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="itemsPerPage"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach>
     </select>
    </td>
   </tr>

   <%-- List all available Java time zones,
        for a logged in user the timezone is used
        to format message dates to their local time --%>
   <tr>
    <td align="right">Time Zone</td>
    <td>
     <select name="timezone">
      <option value="">Select
      <dt:timezones id="tz"><jf:eval id="test"> /<jsp:getProperty name="tz" property="zoneId"/>/ == /<req:parameter name="timezone"/>/</jf:eval><jf:true eval="test"><option value="<jsp:getProperty name="tz" property="zoneId"/>" selected><jsp:getProperty name="tz" property="zoneId"/></jf:true><jf:false eval="test"><option value="<jsp:getProperty name="tz" property="zoneId"/>"><jsp:getProperty name="tz" property="zoneId"/></jf:false></dt:timezones>
     </select> 
    </td>
   </tr>

   <%-- Create account HTML for submit button --%>
   <tr>
    <td><br></td>
    <td>
     <input type="submit" name="create" value="Create account">
    </td>
    <td>
     <input type="submit" name="cancel" value="Cancel">
    </td>
   </tr>

  </form>
 </table>
</ul>

<%@ include file="footer.jsp" %>
