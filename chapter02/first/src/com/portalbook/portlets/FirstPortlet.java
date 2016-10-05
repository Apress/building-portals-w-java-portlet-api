package com.portalbook.portlets;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class FirstPortlet extends GenericPortlet
{
    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");
        Writer writer = response.getWriter();
        writer.write("Help, I'm a portlet, and I'm trapped in a portal!");
    }
}
