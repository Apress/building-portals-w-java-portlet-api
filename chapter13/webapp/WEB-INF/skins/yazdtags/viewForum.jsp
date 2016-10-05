<%-- ViewForum JSP, pages through a list of threads
     for the current forum --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize all users and initialize forum state information --%>
<jf:authorize id="jr" anonymous="true"/>

<%-- set this page as the page to redirect administrative pages like
     login and account page back to so user doesn't loose their place --%>
<sess:setattribute name="redirect"><jsp:getProperty name="req" property="requestURL"/>?<jsp:getProperty name="req" property="queryString"/></sess:setattribute>

<%@ include file="header.jsp" %>

<jf:forum id="cf" trackVisits="true">
 <center>
  <%-- message search form --%>
  <form action="searchForum.jsp" method="get" name="search">
   <input type="hidden" name="forum" value="<jsp:getProperty name="cf" property="forumid"/>">
   Words: <input type="text" name="words" size="20">
   User: <input type="text" name="username" size="10">
   <input type="submit" value="Search">
  </form>

  <%-- begin display preferences --%>
  <form action="change_settings.jsp" method="post" name="settings">
   Items Per Page
   <select name="itemsPerPage">
    <jf:foreach id="loop" begin="5" end="200" step="5"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="itemsPerPage"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false>
    </jf:foreach></select>
   <input type="submit" value="Change Display Settings">
  </form>
 <hr size=0>

 <table cellpadding=2 cellspacing=0 border=0 width="100%">

  <%-- begin breadcrumbs --%>
  <tr>
   <td colspan=3 align="center">
    <b>
     <a href="index.jsp">Home</a>
     ->
     <jsp:getProperty name="cf" property="name"/>
    </b>
    <br><br>
   </td>
  </tr>
  <%-- end breadcrumbs --%>

  <%-- start navigation --%>
  <tr>
   <td width="30%" align="left" nowrap>
    &nbsp;
    <jf:prev_forum>
     <a href="viewForum.jsp?&forum=<jsp:getProperty name="cf" property="prevForumid"/>">
      <b>&lt;-Forum</b>
     </a>
    </jf:prev_forum>
   </td>
   <td width="40%" align="center" nowrap>
    &nbsp;
    <jf:anonymous_user value="false">
     <a href="post.jsp?forum=<jsp:getProperty name="cf" property="forumid"/>">Post Message</a>
    </jf:anonymous_user>
   </td>
   <td width="30%" align="right" nowrap>
    &nbsp;
    <jf:next_forum>
     <a href="viewForum.jsp?&forum=<jsp:getProperty name="cf" property="nextForumid"/>">
      <b>Forum-&gt;</b>
     </a>
    </jf:next_forum>
   </td>
  </tr>

  <%-- Determine if current forum has any threads --%>
  <jf:eval id="forum_empty">
   <jsp:getProperty name="cf" property="messagecount"/> == 0
  </jf:eval>
  <%-- Current forum has no threads --%>
  <jf:true eval="forum_empty">
   <tr>
    <td colspan=3 align="center">
     <br>There are no messages in this forum.
    </td>
   </tr>
  </jf:true>

  <%-- Current forum has some threads --%>
  <jf:false eval="forum_empty">
   <tr>
    <td colspan=3>
     <table bgcolor="#999999" cellpadding=3 cellspacing=0 border=1 width="100%">
      <%-- Loop through and display forum threads --%>
      <jf:thread_loop>
       <%-- Provide a link to previous page of threads if not on first page --%>
       <jf:prev_page>
        <tr bgcolor="#ffffff">
         <td colspan=5 align="center" nowrap>
          <b><a href="viewForum.jsp?<jf:prev_item/>">Previous Threads?</a></b>
         </td>
        </tr>
       </jf:prev_page>

       <%-- Do some HTML formatting of table header first time through thread loop --%>
       <jf:on_entry>
        <tr bgcolor="#dddddd">
         <th width="1%" nowrap>new</th>
         <th width="96%">subject</th>
         <th width="1%" nowrap>replies</th>
         <th width="1%" nowrap>posted by</th>
         <th width="1%" nowrap>date</th>
        </tr>
       </jf:on_entry>

       <%-- Display thread root message information --%>
       <jf:thread id="ct">

        <%-- Setup rotating background colors for table rows --%>
        <jf:rotate id="bgcolor">
         <jf:rotate_selection>
          <tr bgcolor="#ffffff">
         </jf:rotate_selection>
         <jf:rotate_selection>
          <tr bgcolor="#eeeeee">
         </jf:rotate_selection>
        </jf:rotate>

         <td width="1%" align="center">
          <%-- Indicate if thread has been modified since last forum visit --%>
          <jf:new_messages>
           <img src="images/bang.gif" width=11 height=11 border=0>
          </jf:new_messages>
          <jf:new_messages value="false">
           &nbsp;
          </jf:new_messages>
         </td>

         <jf:message id="cm" nested="true">

          <%-- Display the thread subject as a link to view thread --%>
          <td width='96%'>
           <a href='viewMessage.jsp?message=<jsp:getProperty name="cm" property="messageid"/>&thread=<jsp:getProperty name="ct" property="threadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>'>
           <b><jsp:getProperty name="cm" property="subject"/></b></a>
          </td>

          <%-- Display the number of replies to thread --%>
          <td width='1%' align='center'>
           <b>
           <jsp:getProperty name="ct" property="messagereplies"/>
           </b>
          </td>

          <%-- Display thread author information --%>
          <td width='1%' nowrap align='center'>
           <jf:anonymous_message>
           <i>Anonymous</i>
           </jf:anonymous_message>
           <jf:anonymous_message value="false">
            <jf:user id="user">
             <jsp:getProperty name="user" property="username"/>
            </jf:user>
           </jf:anonymous_message>
          </td>

          <%-- Display date/time thread last modified --%>
          <td width='1%' nowrap>
           <dt:format timezone="tz"><jsp:getProperty name="ct" property="modifiedDate"/></dt:format>
          </td>

         </jf:message>
        </tr>
       </jf:thread>

       <%-- Provide a link to next page of threads if current page
           exceeded users Items Per Page --%>
       <jf:next_page>
        <tr bgcolor="#ffffff"><td colspan=5 nowrap>
         <td>
          <center>
           <b><a href="viewForum.jsp?<jf:next_item/>">List more Threads?</a></b>
	  </center>
         </td>
        </tr>
       </jf:next_page>

      </jf:thread_loop>
     </table>
    </td>
   </tr>
  </jf:false>
 </table>
</jf:forum>

<%@ include file="footer.jsp" %>
