<%-- Index JSP, main page for forum where users start,
     lists available forums that user is authorized
     to read --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize all users and initialize forum state information --%>
<jf:authorize id="jr" anonymous="true">
</jf:authorize>          
<%-- set this page as the page to redirect administrative pages like
     login and account page back to so user doesn't loose their place --%>
<sess:setattribute name="redirect"><jsp:getProperty name="req" property="requestURL"/>?<jsp:getProperty name="req" property="queryString"/></sess:setattribute>

<%@ include file="header.jsp" %>

<p>

<%-- Start of HTML to list the available message forums --%>
<table width="100%">
 <%-- Inform user of how many forums there are --%>
 <jf:eval id="num_forums">
  <jsp:getProperty name="jr" property="numberOfForums"/> == 0
 </jf:eval>
 <jf:true eval="num_forums">
  <tr>
   <td width="1%">&nbsp;</td>
   <td>No forums available.</td>
  </tr>
 </jf:true>

 <jf:false eval="num_forums">
  <%-- Loop through all the forums and display them to user --%>
  <jf:forum_loop>

   <%-- Provide a link to the previous page of fourms --%>
   <jf:prev_page>
    <tr>
     <td colspan=3 align="center">
      <b><a href="index.jsp?<jf:prev_item/>">List previous Forums?</a></b>
     </td>
    </tr>
   </jf:prev_page>

   <jf:forum id="forums">
    <tr>
     <td width="1%">&nbsp;</td>
      <%-- Show whether forum has new messages since users last visit --%>
      <jf:new_messages>
       <td width="1%"><img src="images/bang.gif" width=11 height=11 border=0></td>
      </jf:new_messages>
      <jf:new_messages value="false">
       <td width="1%"><img src="images/blank.gif" width=11 height=11 border=0></td>
      </jf:new_messages>
      <td>
       <%-- Now display information about the forum and a link to the forum --%>
       <b>
        <a href="viewForum.jsp?forum=<jsp:getProperty name="forums" property="forumid"/>">
         <jsp:getProperty name="forums" property="name"/>
        </a>
       </b>
       &nbsp;&nbsp;<i>(<jsp:getProperty name="forums" property="description"/>)</i>
       <br>
       (<jsp:getProperty name="forums" property="threadcount"/>
       <jf:eval id="threads">
        <jsp:getProperty name="forums" property="threadcount"/> == 1
       </jf:eval>
       <jf:true eval="threads">thread</jf:true>
       <jf:false eval="threads">threads</jf:false>
        , <jsp:getProperty name="forums" property="messagecount"/>
       <jf:eval id="messages">
        <jsp:getProperty name="forums" property="messagecount"/> == 1
       </jf:eval>
       <jf:true eval="messages">message</jf:true>
       <jf:false eval="messages">messages</jf:false>)
      </td>
     </tr>
    </jf:forum>

    <%-- if the number of forums exceeds the number of items to display
         per page, setup a link to the next page of forums --%>
    <jf:next_page>
     <tr>
      <td colspan=3 align="center">
       <b><a href="index.jsp?<jf:next_item/>">List more Forums?</a></b>
      </td>
     </tr>
    </jf:next_page>

   </jf:forum_loop>

  <tr><td colspan=3>&nbsp;</td></tr>
  <tr>
   <td width="1%">&nbsp;</td>
   <td width="1%"><img src="images/bang.gif" width=11 height=11 border=0></td>
   <td>Indicates forum has new messages since your last visit.</td>
  </tr>
 </jf:false>
</table>
<%-- end list of available forums --%>

<%@ include file="footer.jsp" %>
