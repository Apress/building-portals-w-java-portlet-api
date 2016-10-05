package com.portalbook.custom;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class UAPortlet extends GenericPortlet
{

   protected void doView(RenderRequest request, RenderResponse response)
         throws PortletException, IOException
   {
      Map ua = (Map) request.getAttribute(RenderRequest.USER_INFO);

      response.setContentType("text/html");
      if ((ua == null) || !ua.keySet().iterator().hasNext())
      {
         response.getWriter().write(
               "<b>No user attributes could be found</b>");
      }
      else
      {
         Iterator i = ua.keySet().iterator();
         response.getWriter().write("<table>");
         while (i.hasNext())
         {
            String attributeName = (String) i.next();
            String attributeValue = (String) ua.get(attributeName);

            writeAttributeRow(response, attributeName, attributeValue);
         }
         response.getWriter().write("</table>");
      }
   }

   private void writeAttributeRow(RenderResponse response, String name,
         String value) throws IOException
   {
      StringBuffer buffer = new StringBuffer("<tr><td>");
      buffer.append(name);
      buffer.append("</td><td><b>");
      buffer.append(value);
      buffer.append("</b></td></tr>");
      response.getWriter().write(buffer.toString());
   }
}