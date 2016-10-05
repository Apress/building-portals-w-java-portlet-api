<%@ page import="java.util.SortedMap" %>
<%@ page import="javax.portlet.PortletURL" %>

<%@ taglib uri="/WEB-INF/tld/taglibs-lucene.tld" prefix="lucene" %>
<%@ taglib uri='http://java.sun.com/portlet' prefix='portlet'%>
<portlet:defineObjects/>

<%
    String query = request.getParameter("query");
    if (query == null)
    {
        query = "";
    }
    String indexPath = (String) request.getAttribute("indexPath");
%>
        
<lucene:search var="hits" 
    scope="page" 
    field="contents" 
    analyzer="standard"
    query="<%= query %>"
    startRow="0"
    maxRows="20"
    directory="<%= indexPath %>"
    count="count" />        
        
<p>Total Number of Pages for <b><%= query %></b> : <%= pageContext.getAttribute("count") %></p>
<% SortedMap[] hits = (SortedMap[]) pageContext.getAttribute("hits"); %>
<% if (hits.length > 0) {   for (int i = 0; i < hits.length; i++) { %>
<p/>
<%
    PortletURL renderUrl = renderResponse.createRenderURL();
    renderUrl.setParameter("contentPath", (String) hits[i].get("url"));
%>
<a href="<%=renderUrl.toString()%>"><%= hits[i].get("title") %></a>
<br/> 
<%= hits[i].get("summary") %>
<br/>(Score : <%= hits[i].get("score") %>)
<% } } %>    
