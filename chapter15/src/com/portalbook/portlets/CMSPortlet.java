package com.portalbook.portlets;

import java.io.*;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;

import org.apache.webdav.lib.WebdavResource;

public class CMSPortlet extends GenericPortlet
{
    public static final String COMMAND = "COMMAND";

    public static final String CHANGE_COLL = "CHANGE_COLLECTION";
    public static final String DISPLAY_CONTENT = "DISPLAY_CONTENT";
    public static final String DISPLAY_PARENT = "DISPLAY_PARENT";
    public static final String PATH = "PATH";

    WebDAVHelper helper;

    public void init(PortletConfig config) throws PortletException
    {
        super.init(config);

        helper = new WebDAVHelper();

        try
        {
            String url = config.getInitParameter("URL");
            String username = config.getInitParameter("username");
            String password = config.getInitParameter("password");
            helper.openURL(url, username, password);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new UnavailableException(e.getMessage());
        }

    }

    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        Writer writer = response.getWriter();

        PortletContext portletContext = getPortletContext();

        WebdavResource resource = helper.getResource();

        request.setAttribute("resource", resource);
        System.out.println("name: " + resource.getName());

        if (resource.isCollection())
        {
            PortletRequestDispatcher prd =
                portletContext.getRequestDispatcher(
                    "/WEB-INF/jsp/ListFiles.jsp");

            prd.include(request, response);

        }
        else
        {
            writer.write(resource.getMethodDataAsString());
        }

    }

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        String cmd = request.getParameter(COMMAND);
        System.out.println("Command: " + cmd);

        if (CHANGE_COLL.equals(cmd))
        {
            String path = request.getParameter(PATH);
            if (path != null)
            {
                System.out.println("path: " + path);
                try
                {
                    helper.setPath(path);
                }
                catch (WebDAVException e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        else if (DISPLAY_CONTENT.equals(cmd))
        {
            String path = request.getParameter(PATH);
            if (path != null)
            {
                System.out.println("path: " + path);
                try
                {
                    helper.setPath(path);
                }
                catch (WebDAVException e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        else if (DISPLAY_PARENT.equals(cmd))
        {
            String path = request.getParameter(PATH);
            if (path != null)
            {
                System.out.println("path: " + path);
                try
                {
                    helper.setPath(helper.getParentPath(path));
                }
                catch (WebDAVException e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
