package com.portalbook.portlets.content;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class TopicPortlet extends GenericPortlet
{
    List topics = new ArrayList();

    public void init()
    {
        topics.add("xerces");
        topics.add("lucene");
        topics.add("xalan");
        topics.add("jdom");
    }

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");
        Writer writer = response.getWriter();

        Iterator iter = topics.iterator();
        while (iter.hasNext())
        {
            String key = (String) iter.next();

            PortletURL actionURL = response.createActionURL();
            actionURL.setParameter("id", key);

            writer.write("<a href='" + actionURL.toString() + "'>");
            writer.write(key + "</a><br>");
        }

    }

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        PortletSession session = request.getPortletSession(true);
        String id = request.getParameter("id");
        session.setAttribute("contentId", id, PortletSession.APPLICATION_SCOPE);
    }

}