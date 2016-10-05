package com.portalbook.portlets;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class AdvancedPortlet extends GenericPortlet
{

    protected void doEdit(
        RenderRequest renderRequest,
        RenderResponse renderResponse)
        throws PortletException, IOException
    {
        renderResponse.setContentType("text/html");

        Writer writer = renderResponse.getWriter();

        //get the existing parameters to use as defaults.
        String title = renderRequest.getParameter("title");
        String contents = renderRequest.getParameter("contents");

        if (title == null)
        {
            title = "";
        }

        if (contents == null)
        {
            contents = "";
        }

        writer.write("<H1>Portlet Settings</H1>");
        writer.write("<FORM ACTION=");
        writer.write(renderResponse.createActionURL().toString());
        writer.write(">");

        writer.write(
            "Title: <INPUT TYPE=text NAME=title VALUE='" + title + "' SIZE=25>");
        writer.write(
            "Contents: <INPUT TYPE=text NAME=contents VALUE= '"
                + contents
                + "' SIZE=25>");
        writer.write("<P>");
        writer.write("<INPUT TYPE=submit>");
        writer.write("</FORM>");

    }

    protected void doHelp(
        RenderRequest renderRequest,
        RenderResponse renderResponse)
        throws PortletException, IOException
    {
        //return a helpful message
        renderResponse.setContentType("text/html");

        Writer writer = renderResponse.getWriter();
        writer.write(
            "This portlet allows you to change its content and title.");
    }

    protected void doView(
        RenderRequest renderRequest,
        RenderResponse renderResponse)
        throws PortletException, IOException
    {
        renderResponse.setContentType("text/html");
        Writer writer = renderResponse.getWriter();
        String contents = renderRequest.getParameter("contents");
        if (contents != null)
        {
            writer.write(contents);
        }
        else
        {
            //return the default contents
            writer.write("This is the default portlet contents.  To change ");
            writer.write("this message, edit the portlet's settings.");
        }

        writer.write("<p>");
        writer.write(
            "<IMG SRC="
                + renderResponse.encodeURL(
                    renderRequest.getContextPath() + "/images/picture.jpg")
                + ">");

        writer.write("<p>");
        writer.write(
            "<IMG SRC=http://www.greenninja.com/images/teton1-small.jpg>");
    }

    protected String getTitle(RenderRequest renderRequest)
    {
        String title = renderRequest.getParameter("title");
        if (title != null)
        {
            return title;
        }
        //else return a default title, if we don't have one set yet.
        return "Advanced Portlet";
    }

    public void processAction(
        ActionRequest actionRequest,
        ActionResponse actionResponse)
        throws PortletException, IOException
    {
        //check for parameters
        String title = actionRequest.getParameter("title");
        if (title != null)
        {
            actionResponse.setRenderParameter("title", title);
        }

        String contents = actionRequest.getParameter("contents");
        if (contents != null)
        {
            actionResponse.setRenderParameter("contents", contents);
        }
    }

}
