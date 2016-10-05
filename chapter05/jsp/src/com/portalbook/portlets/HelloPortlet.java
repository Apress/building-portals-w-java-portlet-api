package com.portalbook.portlets;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class HelloPortlet extends GenericPortlet
{
    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        PortletContext portletContext = getPortletContext();
        PortletRequestDispatcher reqDispatcher =
            portletContext.getRequestDispatcher("/hello");
        reqDispatcher.include(request, response);

        PortletRequestDispatcher namedDispatcher =
            portletContext.getNamedDispatcher("HelloServlet");
        namedDispatcher.include(request, response);

    }
}
