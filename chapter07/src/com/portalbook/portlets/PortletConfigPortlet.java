package com.portalbook.portlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortalContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class PortletConfigPortlet extends GenericPortlet
{
    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        Writer writer = response.getWriter();

        writer.write("<H2>Available Portal Properties</H2>");

        //get the Portal Context
        PortalContext portalContext = request.getPortalContext();

        //get the available portlet propertyNames
        Enumeration availablePropNames = portalContext.getPropertyNames();

        //check to see if there are more than zero property names
        if (!availablePropNames.hasMoreElements())
        {
            writer.write("There are no portal properties defined.<BR>");
        }

        //iterate through the property names, and output them
        while (availablePropNames.hasMoreElements())
        {
            String name = (String) availablePropNames.nextElement();
            if (name != null)
            {
                String value = portalContext.getProperty(name);
                if (value != null)
                {
                    writer.write(
                        "The value of property " + name +
                            " is " + value + "<BR>");
                }
                else
                {
                    writer.write(
                        "There is not a property value defined for " +
                            name + "<BR>");
                }
            }
        }
    }
}
