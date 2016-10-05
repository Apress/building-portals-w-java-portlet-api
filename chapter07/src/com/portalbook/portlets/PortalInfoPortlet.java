package com.portalbook.portlets;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.GenericPortlet;
import javax.portlet.PortalContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class PortalInfoPortlet extends GenericPortlet
{
    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        Writer writer = response.getWriter();

        writer.write("<H2>Portal Information</H2>");

        //get the Portal Context
        PortalContext portalContext = request.getPortalContext();

        //get the available portal information
        String portalInfo = portalContext.getPortalInfo();

        writer.write("The portal this portlet is running under is: ");
        writer.write(portalInfo);

    }
}
