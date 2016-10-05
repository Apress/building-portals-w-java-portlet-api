<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri='/WEB-INF/tld/portlet.tld' prefix='portlet'%>

<%@ page import="org.apache.webdav.lib.WebdavResource" %>
<%@ page import="java.util.Vector" %>
<%@ page import="javax.portlet.PortletURL" %>

<%
    WebdavResource resource = (WebdavResource) request.getAttribute("resource");
    Vector children = resource.listBasic();
    pageContext.setAttribute("resource", resource);
    pageContext.setAttribute("children", children);

%>

<c:forEach var="child" items="${children}" varStatus="status">
  <c:set var="path" value="${resource.path}/${child[0]}"/>
  <c:choose>
    <c:when test="${child[2] == 'COLLECTION'}">
      <a href="<portlet:actionURL>
                  <portlet:param name="COMMAND" value="CHANGE_COLLECTION"/>
                  <portlet:param name="PATH" 
                    value="<%=(String)pageContext.getAttribute("path")%>"/>
              </portlet:actionURL>">
              <c:out value="${child[0]}"/>
      </a>
    </c:when>
    <c:otherwise>
      <a href="<portlet:actionURL>
                  <portlet:param name="COMMAND" value="DISPLAY_CONTENT"/>
                  <portlet:param name="PATH" 
                    value="<%=(String)pageContext.getAttribute("path")%>"/>
              </portlet:actionURL>">
              <c:out value="${child[0]}"/>
      </a>        
      <c:out value="${child[1]}"/>
    </c:otherwise>
  </c:choose>
  <c:out value="${child[3]}"/>
  <br>
</c:forEach>
<p/>
<a href="<portlet:actionURL>
            <portlet:param name="COMMAND" value="DISPLAY_PARENT"/>
            <portlet:param name="PATH" value="<%=resource.getPath() + "/"%>"/>
        </portlet:actionURL>">
        Back to parent collection
</a>  
