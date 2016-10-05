package com.portalbook.portlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class RedirectPortlet extends GenericPortlet
{
    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        PortletURL actionURL = response.createActionURL();

        writer.write("<a href='" + actionURL.toString() + "'>");
        writer.write("Redirect to APress</a>");
    }

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        response.sendRedirect("http://www.apress.com/");
    }
}
