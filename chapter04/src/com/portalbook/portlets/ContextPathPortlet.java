package com.portalbook.portlets;

import javax.portlet.*;
import java.io.*;

public class ContextPathPortlet extends GenericPortlet
{

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        //write out the context path
        writer.write(request.getContextPath());

    }
}