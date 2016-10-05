<%-- Account JSP, allows user to modify their
     account data. They must be logged in. --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize only users who have been logged in and
     initialize forum state information --%>
<jf:authorize id="jr">
 <%-- inform user they must be logged in --%>
 <jsp:setProperty name="jr" property="error" value="You must be logged in to modify your account information."/>
 <%-- user wasn't logged in, forward them to the login page --%>
 <res:sendredirect>login.jsp</res:sendredirect>
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

<%-- Get user account information,
     update Yazd user information on form submit --%>
<jf:account id="account" confirm="true">
 <%-- if HTTP input parameter modify exists,
      user has submitted the form with changes.  --%>
 <req:existsparameter name="modify">
  <%-- Account information may have been changed successfully,
       check to see if there was an error. --%>
  <jf:if_error value="false">
   <%-- Account information was changed sucessfully --%>
   <%-- Make sure users timezone settings for session is up to date --%>
   <dt:timezone id="tz"><jf:getYazdProperty name="account" property="timezone"/></dt:timezone>
   <%-- Set status so user knows that account information was updated sucessfully --%>
   <sess:setattribute name="status">Your account information has been modified.</sess:setattribute>
   <%-- Redirect users back to the page they were on when they
        selected to create the account --%>
   <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
  </jf:if_error>
 </req:existsparameter>

 <%@ include file="header.jsp" %>

 <%-- Either this is the first time user entered account page,
      or their previous account HTML form submit failed.  --%>
 <center><h3>Modify your user account</h3></center>

 <p>

 <%-- Display the users account information so they
      can modify it.  --%>
 <ul>
  <table cellpadding=3 cellspacing=0 border=0>
   <form action="account.jsp" method="post">

    <%-- Users email address, required --%>
    <tr>
     <td align="right">
      <b>Email address:</b>
     </td>
     <td>
      <input type="text" name="email" value="<jsp:getProperty name="account" property="email"/>">
      <%-- Use a red star to flag that email address is a required field
           if it has not been entered --%>
      <req:existsparameter name="modify">
       <req:existsparameter name="email" value="false">
        <font size=4 color="#ff0000" face="arial,helvetica"><b>*</b></font>
       </req:existsparameter>
      </req:existsparameter>
      <i>(required)</i>
     </td>
    </tr>

    <%-- Users forum user name, can not be changed --%>
    <tr>
     <td align="right">
      <b>Username:</b>
     </td>
     <td><jsp:getProperty name="account" property="username"/></td>
    </tr>

    <%-- Account password and confirmation, if user wants
         to change password, both have to be filled in and match --%>
    <tr>
     <td align="right">
      <b>Password:</b>
     </td>
     <td><input type="password" name="password"></td>
     <td align="right">
      <b>Confirm:</b>
     </td>
     <td><input type="password" name="confirm"></td>
    </tr>

    <%-- User real name, optional --%>
    <tr>
     <td align="right">Name:</td>
     <td>
      <input type="text" name="name" value="<jsp:getProperty name="account" property="name"/>">
     </td>
    </tr>

    <%-- URL for user, optional --%>
    <tr>
     <td align="right">URL:</td>
     <td>
      <input type="text" name="url" value="<jf:getYazdProperty name="account" property="url"/>">
     </td>
    </tr>

    <%-- User signature text, appended to all messages they post, optional --%>
    <tr>
     <td align="right">Signature<br>(255 characters only)</td>
     <td>
      <textarea cols=30 rows=3 name="signature" wrap="physical"><jf:getYazdProperty name="account" property="signature"/></textarea>
     </td>
    </tr>

    <%-- Should users real name be visible, defaults to no --%>
    <tr>
     <td align="right">
      <input type="checkbox" name="nameVisible" value="checked" <jsp:getProperty name="account" property="nameVisible"/>>
     </td>
     <td>Hide my name</td>
    </tr>

    <%-- Should users email address be visible, defaults to no --%>
    <tr>
     <td align="right">
      <input type="checkbox" name="emailVisible" value="checked" <jsp:getProperty name="account" property="emailVisible"/>>
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
       <jf:foreach id="loop" begin="0" end="20"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="messageDepth"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach>
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
       <jf:foreach id="loop" begin="1" end="20"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="threadDepth"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach>
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
       <jf:foreach id="loop" begin="5" end="200" step="5"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="itemsPerPage"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop"property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach>
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
       <dt:timezones id="tz"><jf:eval id="test"> /<jsp:getProperty name="tz" property="zoneId"/>/ == /<jf:getYazdProperty name="account" property="timezone"/>/</jf:eval><jf:true eval="test"><option value="<jsp:getProperty name="tz" property="zoneId"/>" selected><jsp:getProperty name="tz" property="zoneId"/></jf:true><jf:false eval="test"><option value="<jsp:getProperty name="tz" property="zoneId"/>"><jsp:getProperty name="tz" property="zoneId"/></jf:false></dt:timezones>
      </select>
     </td>
    </tr>

    <%-- Modify account HTML for submit button --%>
    <tr>
     <td><br></td>
     <td>
      <input type="submit" name="modify" value="Modify account">
     </td>
     <td>
      <input type="submit" name="cancel" value="Cancel">
     </td>
    </tr>

   </form>
  </table>
 </ul>
</jf:account>

<%@ include file="footer.jsp" %>
