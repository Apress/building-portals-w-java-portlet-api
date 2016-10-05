<%-- Search Forum Messages JSP, then
     page through a list of messages found --%>

<%-- ViewMessage JSP, pages through a message tree
     for the current thread and forum --%>

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

 <%-- message search form --%>
 <form action="searchForum.jsp" method="get" name="search">
  <center>
   <input type="hidden" name="forum" value="<jsp:getProperty name="cf" property="forumid"/>">
   Words:
   <input type="text" name="words" size="20" value="<req:parameter name="words"/>">
   User: <input type="text" name="username" size="10" value="<req:parameter name="username"/>">
   <input type="submit" value="Search">
  </center>
 </form>

 <%-- display preferences --%>
 <form action="change_settings.jsp" method="post" name="settings">
  <center>
   <%-- Items Per Page display preferences --%>
   Items Per Page <select name="itemsPerPage"><jf:foreach id="loop" begin="5" end="200" step="5"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="itemsPerPage"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach></select>
   <input type="submit" value="Change Display Settings">
  </center>
 </form>
 <hr size=0>

 <%-- begin breadcrumbs --%>
 <center>
  <b>
   <a href="index.jsp">Home</a>
   -&gt;
   <a href="viewForum.jsp?forum=<jsp:getProperty name="cf" property="forumid"/>">
    <jsp:getProperty name="cf" property="name"/>
   </a>
  </b>
 </center>
 <%-- end breadcrumbs --%>

 <br>

 <%-- start navigation --%>
 <table cellpadding=2 cellspacing=0 border=0 width="100%">
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

      <jf:query id="query" property="create"></jf:query>
      <req:existsparameter name="words">
       <jf:query id="query" property="queryString"><req:parameter name="words"/></jf:query>
      </req:existsparameter>
      <req:existsparameter name="username">
       <jf:query id="query" property="userID"><req:parameter name="username"/></jf:query>
      </req:existsparameter>

      <jf:message_loop query="query">

       <jf:prev_page>
        <tr bgcolor="#ffffff">
         <td colspan=4 nowrap>
          <center>
           <b>
            <a href="searchForum.jsp?<jf:prev_item/>&words=<req:querystring name="words"/>&username=<req:querystring name="username"/>">
             Previous Messages?
            </a>
           </b>
          </center>
         </td>
        </tr>
       </jf:prev_page>

       <jf:on_entry>
        <tr bgcolor="#dddddd">
         <td align="center" width="1%" nowrap>new</td>
         <td align="center" width="97%">subject</td>
         <td align="center" width="1%" nowrap>posted by</td>
         <td align="center" width="1%" nowrap>date</td>
        </tr>
       </jf:on_entry>

       <jf:rotate id="bgcolor">
        <jf:rotate_selection>
         <tr bgcolor="#ffffff">
        </jf:rotate_selection>
        <jf:rotate_selection>
         <tr bgcolor="#eeeeee">
        </jf:rotate_selection>
       </jf:rotate>

       <jf:message id="cm" nested="true">
        <td width="1%" align="center">
         <jf:new_messages>
          <img src="images/bang.gif" width=11 height=11 border=0>
         </jf:new_messages>
         <jf:new_messages value="false">
          <img src="images/blank.gif" width=11 height=11 border=0>
         </jf:new_messages>
        </td>

        <td width='97%'>
         <a href='viewMessage.jsp?message=<jsp:getProperty name="cm" property="messageid"/>&thread=<jsp:getProperty name="cm" property="threadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>'>
          <b><jsp:getProperty name="cm" property="subject"/></b>
         </a>
        </td>

        <td width='1%' nowrap align='center'>
	 <jf:user id="user">
          <jsp:getProperty name="user" property="username"/>
	 </jf:user>
        </td>

        <td width='1%' nowrap>
	 <dt:format timezone="tz"><jsp:getProperty name="cm" property="modifiedDate"/></dt:format>
        </td>

       </jf:message>

        </tr>

       <jf:next_page>
        <tr bgcolor="#ffffff">
         <td colspan=4 nowrap>
          <center>
           <b>
            <a href="searchForum.jsp?<jf:next_item/>&words=<req:querystring name="words"/>&username=<req:querystring name="username"/>">
             List more Messages?
            </a>
           </b>
          </center>
         </td>
        </tr>
       </jf:next_page>

      </jf:message_loop>
     </table>
    </td>
   </tr>
  </jf:false>
 </table>
</jf:forum>

<%@ include file="footer.jsp" %>
