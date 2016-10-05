<%-- Reply JSP, displays a form to reply to a message in a
     forum, posts the message when this form is submitted --%>

<%-- Include init.jsp, it does all of the common
     tasks for most of the JSP pages --%>
<%@ include file="init.jsp" %>

<%-- authorize logged in users and initialize forum state information --%>
<jf:authorize id="jr">
  <%-- User is not logged in, tell them and send them to the login page --%>
  <sess:setattribute name="status">You must be logged in to reply to a message.</sess:setattribute>
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

<%-- post the message reply if this is a form submission
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
  <sess:setattribute name="status">Message Reply Posted</sess:setattribute>
  <%-- Redirect user back to page they were on before selecting to
       reply to a message --%>
  <res:sendredirect><sess:attribute name="redirect"/></res:sendredirect>
 </jf:post_message>
</jf:post>

<%@ include file="header.jsp" %>

<%-- get the current forum, thread, and message being replied to --%>
<jf:forum id="cf">
 <jf:thread id="ct">
  <jf:message id="cm">

   <center>
    <%-- bread crumbs --%>
    <b>
     <a href="index.jsp">Home</a>
     &gt;
     <a href="viewForum.jsp?forum=<jsp:getProperty name="cf" property="forumid"/>">
      <jsp:getProperty name="cf" property="name"/>
     </a>
    </b>
    <%-- end bread crumbs --%>

    <p>
    <h3>Reply to a message</h3>
   </center>

   <%-- Display message user is replying to --%>
   <table bgcolor="#666666" cellpadding=1 cellspacing=0 border=0 width="100%">
    <tr>
     <td>
      <table bgcolor="#dddddd" cellpadding=3 cellspacing=0 border=0 width="100%">
       <tr>
        <%-- Display message subject --%>
        <td width="40%">
         <b><jsp:getProperty name="cm" property="subject"/></b>
        </td>

        <%-- Display message author information --%>
        <td width="30%">
         <center>
          <b>
           <%-- Determine if user was anonymouse --%>
           <jf:anonymous_message>
            [Anonymous]
           </jf:anonymous_message>
           <%-- For a real user, display email address and real name if user
                has emailVisible or nameVisible checked --%>
           <jf:anonymous_message value="false">
            <jf:user id="user">
             <jf:eval id="email"> \<jsp:getProperty name="user" property="email"/>\ == \\ </jf:eval>
             <jf:false eval="email">
              <a href="mailto:<jsp:getProperty name="user" property="email"/>">
               <jf:eval id="real"> \<jsp:getProperty name="user" property="name"/>\ == \\ </jf:eval>
               <jf:false eval="real">
                <jsp:getProperty name="user" property="name"/>
               </jf:false>
               <jf:true eval="real">
                <jsp:getProperty name="user" property="email"/>
               </jf:true>
              </a>&nbsp;
             </jf:false>
             [<jsp:getProperty name="user" property="username"/>]
            </jf:user>
           </jf:anonymous_message>
          </b>
         </center>
        </td>

        <%-- Display message creation date --%>
        <td width="30%" align=right nowrap>
         <b>
          <dt:format timezone="tz"><jsp:getProperty name="cm" property="creationDate"/></dt:format>
         </b>
        </td>

       </tr>
      </table>

      <%-- Display the body of the message user is replying to --%>
      <table bgcolor="#ffffff" cellpadding=5 cellspacing=0 border=0 width="100%">
       <tr><td><jsp:getProperty name="cm" property="body"/></td></tr>
      </table>

     </td>
    </tr>
   </table>
   <%-- end message user is replying to --%>

   <p>

   <%-- display the HTML input form for posting a message --%>
   <form action="reply.jsp" method="post" name="postForm">
    <table cellpadding="3" cellspacing="0" border="0">
     <tr>
      <td>
       <b>Reply to this message:</b>
       <p>
       <table>
        <tr>
         <td><b>Subject</b></td>
         <td colspan=2>
          <%-- Display subject line of new message.
               Use subject line of message being replied to for default
               new message subject line, add the "Re: " prefix if not
               already there. --%>
          <jf:eval id="sub">
           \Re: \ |= \<jsp:getProperty name="cm" property="subject"/>\
          </jf:eval>
          <jf:true eval="sub">
           <input type="text" name="subject" value="<jsp:getProperty name="cm" property="subject"/>" size="50" maxlength="100">
          </jf:true>
          <jf:false eval="sub">
           <input type="text" name="subject" value="Re: <jsp:getProperty name="cm" property="subject"/>" size="50" maxlength="100">
          </jf:false>

         </td>
        </tr>

        <%-- text area for entering body of message --%>
        <tr>
         <td valign="top"><b>Reply</b></td>
         <td colspan=2>
          <textarea name="body" cols="80" rows="10" wrap="physical"></textarea>
         </td>
        </tr>

        <%-- submit reply to message button --%>
        <tr>
         <td>&nbsp;</td>
         <td>
          <input type="submit" name="reply" value="Reply to Message">
         </td>
         <td>
          <input type="submit" name="cancel" value="Cancel">
         </td>
        </tr>

       </table>
      </td>
     </tr>
    </table>
   </form>

  </jf:message>
 </jf:thread>
</jf:forum>

<%@ include file="footer.jsp" %>
