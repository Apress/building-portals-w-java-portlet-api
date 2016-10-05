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
 <jf:thread id="ct">
  <%-- message search form --%>
  <form action="searchForum.jsp" method="get" name="search">
   <center>
    <input type="hidden" name="forum" value="<jsp:getProperty name="cf" property="forumid"/>">
    Words: <input type="text" name="words" size="20">
    User: <input type="text" name="username" size="10">
    <input type="submit" value="Search">
   </center>
  </form>

  <%-- display preferences and thread navigation --%>
  <form action="change_settings.jsp" method="post" name="settings">
   <center>
    <%-- Message Depth, Thread Depth, and Items Per Page display preferences --%>
    Message Depth <select name="messageDepth"><jf:foreach id="loop" begin="0" end="20"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="messageDepth"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach></select>
    Thread Depth <select name="threadDepth"><jf:foreach id="loop" begin="1" end="20"><jf:eval id="test"><jsp:getProperty name="loop" property="value"/> == <jsp:getProperty name="jr" property="threadDepth"/></jf:eval><jf:true eval="test"><option value=<jsp:getProperty name="loop" property="value"/> selected><jsp:getProperty name="loop" property="value"/></jf:true><jf:false eval="test"><option value=<jsp:getProperty name="loop" property="value"/>><jsp:getProperty name="loop" property="value"/></jf:false></jf:foreach></select>
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

  <table cellpadding="0" cellspacing="0" border="0" width="100%">
   <%-- start navigation --%>
   <tr>
    <%-- setup navigation to previous thread if it exists --%>
    <td width="20%" align="left" nowrap>
     &nbsp;
     <jf:prev_thread>
      <a href="viewMessage.jsp?&thread=<jsp:getProperty name="ct" property="prevThreadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>">
       <b>&lt;-Thread</b>
      </a>
     </jf:prev_thread>
    </td>

    <%-- setup navigation to previous forum if it exists --%>
    <td width="20%" align="left" nowrap>
     &nbsp;
     <jf:prev_forum>
      <a href="viewForum.jsp?&forum=<jsp:getProperty name="cf" property="prevForumid"/>">
       <b>&lt;-Forum</b>
      </a>
     </jf:prev_forum>
    </td>

    <%-- post a message to forum if user is logged in --%>
    <td width="20%" align="center" nowrap>
     &nbsp;
     <jf:anonymous_user value="false">
      <a href="post.jsp?forum=<jsp:getProperty name="cf" property="forumid"/>">Post Message</a>
     </jf:anonymous_user>
    </td>

    <%-- setup navigation to next forum if it exists --%>
    <td width="20%" align="right" nowrap>
     &nbsp;
     <jf:next_forum>
      <a href="viewForum.jsp?&forum=<jsp:getProperty name="cf" property="nextForumid"/>">
       <b>Forum-&gt;</b>
      </a>
     </jf:next_forum>
    </td>

    <%-- setup navigation to next thread if it exists --%>
    <td width="20%" align="right" nowrap>
     &nbsp;
     <jf:next_thread>
      <a href="viewMessage.jsp?&thread=<jsp:getProperty name="ct" property="nextThreadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>">
       <b>Thread-&gt;</b>
      </a>
     </jf:next_thread>
    </td>

   </tr>

   <tr>
    <td colspan=5>

     <%-- Display messages by walking thread message tree --%>
     <jf:walk>

      <%-- Provide a link to previous page of messages if not on first page --%>
      <jf:prev_page>
       <center>
        <b><a href="viewMessage.jsp?<jf:prev_item/>">Previous Messages?</a></b>
       </center>
      </jf:prev_page>

      <%-- display current message --%>
      <jf:message id="cm" nested="true">
       <jf:user id="cu">
        <table bgcolor="#ffffff" cellpadding="0" cellspacing="0" border="0" width="100%">
         <tr>
          <%-- indent message display based on message reply depth --%>
          <td width='<jf:calc><jf:current_depth/> * 4</jf:calc>%' bgcolor='#ffffff'>
           &nbsp;
          </td>

          <%-- Use remaining table width for display of message --%>
          <td width='<jf:calc>100 - <jf:calc><jf:current_depth/> * 4</jf:calc></jf:calc>%' bgcolor='#ffffff'>

           <%-- If not on first page of messages for thread
                display summary of any parent message of first
                message to display --%>
           <jf:is_parent>
            <table bgcolor="#ffffff" cellpadding="3" cellspacing="1" border="0" width="100%">
             <tr>
              <td nowrap>
               <%-- Indicate if message is new since last forum visit --%>
               <jf:new_messages>
                <img src="images/bang.gif" width=11 height=11 border=0>
               </jf:new_messages>
               <%-- Display message subject --%>
               <b><jsp:getProperty name="cm" property="subject"/></b>
               <%-- Display user who posted message --%>
               <b>[
                <jf:anonymous_message>Anonymous</jf:anonymous_message>
                <jf:anonymous_message value="false">
                 <jsp:getProperty name="cu" property="username"/>
                </jf:anonymous_message>
               ]</b>
               <%-- Provide a link for replying to message --%>
               <a href="reply.jsp?message=<jsp:getProperty name="cm" property="messageid"/>&thread=<jsp:getProperty name="ct" property="threadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>">Reply</a>
              </td>
             </tr>
            </table>
           </jf:is_parent>

           <%-- Message depth is <= message depth setting, display full message --%>
           <jf:is_message>
            <table bgcolor="#ffffff" cellpadding="3" cellspacing="1" border="1" width="100%">
             <tr bgcolor="#dddddd">
              <td align=left width="40%">
               <%-- Indicate if message is new since last forum visit --%>
               <jf:new_messages>
                <img src="images/bang.gif" width=11 height=11 border=0>
               </jf:new_messages>
               <%-- Display message subject --%>
               <b><jsp:getProperty name="cm" property="subject"/></b>
              </td>
              <%-- Display user who posted message --%>
              <td width="20%" align=center nowrap>
               <b>[
                <jf:anonymous_message>Anonymous</jf:anonymous_message>
                <jf:anonymous_message value="false">
                 <jsp:getProperty name="cu" property="username"/>
                </jf:anonymous_message>
               ]</b>
              </td>
              <%-- Display message creation date --%>
              <td width="30%" align=right nowrap>
               <b>
                <dt:format timezone="tz"><jsp:getProperty name="cm" property="creationDate"/></dt:format>
               </b>
              </td>
              <%-- Provide a link for replying to message --%>
              <td width="10%" align="center">
               <a href="reply.jsp?message=<jsp:getProperty name="cm" property="messageid"/>&thread=<jsp:getProperty name="ct" property="threadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>">
                Reply
               </a>
              </td>
             </tr>
             <%-- Display the body of the message --%>
             <tr bgcolor="#eeeeee">
              <td colspan=4>
               <jsp:getProperty name="cm" property="body"/>
              </td>
             </tr>
            </table>
           </jf:is_message>

           <%-- Message depth > message depth setting but <= thread depth setting --%>
           <jf:is_summary>
            <table bgcolor="#ffffff" cellpadding="3" cellspacing="1" border="0" width="100%">
             <tr>
              <td nowrap>
               <%-- Indicate if message is new since last forum visit --%>
               <jf:new_messages>
                <img src="images/bang.gif" width=11 height=11 border=0>
               </jf:new_messages>
               <%-- Display message subject --%>
               <b><jsp:getProperty name="cm" property="subject"/></b>
               <%-- Display user who posted message --%>
               <b>[
                <jf:anonymous_message>Anonymous</jf:anonymous_message>
                <jf:anonymous_message value="false">
                 <jsp:getProperty name="cu" property="username"/>
                </jf:anonymous_message>
               ]</b>
               <%-- Provide a link for replying to message --%>
               <a href="reply.jsp?message=<jsp:getProperty name="cm" property="messageid"/>&thread=<jsp:getProperty name="ct" property="threadid"/>&forum=<jsp:getProperty name="cf" property="forumid"/>">Reply</a>
              </td>
             </tr>
            </table>
           </jf:is_summary>

           <%-- Message depth > thread depth --%>
           <jf:is_total>
            <table bgcolor="#ffffff" cellpadding="3" cellspacing="1" border="0" width="100%">
             <tr>
              <td>
	       <%-- Display how many message replies are below the users
                    thread depth threshold --%>
               [<jsp:getProperty name="cm" property="messagecount"/>]
               <jf:eval id="total"><jsp:getProperty name="cm" property="messagecount"/> == 1</jf:eval>
               <jf:true eval="total">
                message below your threshold.
               </jf:true>
               <jf:false eval="total">
                messages below your threshold.
               </jf:false>
              </td>
             </tr>
            </table>
           </jf:is_total>
          </td>
         </tr>
        </table>
        <p>
       </jf:user>
      </jf:message>

      <%-- Provide a link to next page of messages if current page
           exceeded users Items Per Page --%>
      <jf:next_page>
       <center>
        <b><a href="viewMessage.jsp?<jf:next_item/>">List more Messages?</a></b>
       </center>
      </jf:next_page>

     </jf:walk>
    </td>
   </tr>
  </table>
 </jf:thread>
</jf:forum>

<%@ include file="footer.jsp" %>
