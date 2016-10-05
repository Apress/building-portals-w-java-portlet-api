package com.portalbook.portlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class CachePortlet extends GenericPortlet
{
    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        //write the current date and time to the portlet
        String format = "EEE, MMM d, yyyy hh:mm:ss aaa";
        SimpleDateFormat df = new SimpleDateFormat(format);
        
		writer.write("<h3>Date and Time</h3>");
        writer.write(df.format(new Date()).toString());
    }
}
