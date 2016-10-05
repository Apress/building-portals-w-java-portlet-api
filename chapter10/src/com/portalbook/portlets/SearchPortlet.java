package com.portalbook.portlets;

import java.io.*;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;
import javax.portlet.WindowState;

public class SearchPortlet extends GenericPortlet
{
    String indexPath;

    public void init(PortletConfig config) throws PortletException
    {
        super.init(config);

        //get the location of the lucene index from the
        //indexPath initialization parameter
        indexPath = config.getInitParameter("indexPath");

        if (indexPath == null)
        {
            //this portlet requires this parameter
            String errMsg = "The init parameter indexPath must be set.";
            throw new UnavailableException(errMsg);
        }

        //set Lucene lockdir because java.io.tmpdir may not exist in Pluto
        System.setProperty("org.apache.lucene.lockdir", indexPath);
    }

    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        Writer writer = response.getWriter();

        PortletContext portletContext = getPortletContext();

        PortletSession session = request.getPortletSession();

        PortletRequestDispatcher prd =
            portletContext.getRequestDispatcher("/WEB-INF/jsp/SearchForm.jsp");
        prd.include(request, response);

        String contentPath = request.getParameter("contentPath");

        if (request.getParameter("query") != null)
        {
            request.setAttribute("indexPath", indexPath);
            PortletRequestDispatcher prd2 =
                portletContext.getRequestDispatcher(
                    "/WEB-INF/jsp/SearchResults.jsp");
            prd2.include(request, response);
        }
        else if (contentPath != null)
        {
            File file = new File(contentPath);
            if (!file.exists())
            {
                writer.write("Requested content does not exist.");
                return;
            }

            //write the content of the file out
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null)
            {
                writer.write(line);
            }
        }
    }

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {

        //increase the portlet's size
        response.setWindowState(WindowState.MAXIMIZED);

        //pass the query to the render method
        response.setRenderParameter("query", request.getParameter("query"));
    }
}
