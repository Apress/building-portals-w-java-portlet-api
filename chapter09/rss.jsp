<%@ 
   page contentType="text/xml" session="false"
%><?xml version="1.0" encoding="UTF-8"?>
<%
   java.util.Date today = new java.util.Date();
%>
<rss version="2.0">
   <channel>
      <title>Example Channel</title>
      <link>http://localhost:8080/</link>
      <description>
         The Example Channel discusses exemplary behaviour
      </description>
      <copyright>(c) Dave Minter &amp; Jeff Linwood 2004</copyright>
      <lastBuildDate><%= today %></lastBuildDate>
      <generator>Example RSS JSP</generator>
      <managingEditor>tom@example.com</managingEditor>
      <webMaster>dick@example.com</webMaster>
   
      <% for( int i = 0; i < 10; i++ ) { %>
      <item>        
         <title>Example Title Number <%= i %></title>
         <guid isPermaLink="false"><%= i %></guid>
         <link>http://localhost:8080/<%= i %>/</link>
         <pubDate><%= today %></pubDate>
         <description>
            Description for example article number <%= i %>.
         </description>
      </item>
      <% } %>
   </channel>
</rss>
