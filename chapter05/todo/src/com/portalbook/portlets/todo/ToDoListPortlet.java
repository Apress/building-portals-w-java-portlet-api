package com.portalbook.portlets.todo;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class ToDoListPortlet extends GenericPortlet
{

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException
    {
        //retrieve the to do list out of the user's session
        PortletSession session = request.getPortletSession(true);

        //the to do list is just stored as an ArrayList here, because
        //we are not going to persist it to a database or other storage.
        ArrayList list = (ArrayList) session.getAttribute("ToDoList",
                PortletSession.APPLICATION_SCOPE);

        //if the list doesn't exist, create an empty one.
        if (list == null)
        {
            list = new ArrayList();
        }

        //set up a very simple controller here, based on a
        //request parameter called COMMAND
        String command = request.getParameter("COMMAND");
        String itemParam = request.getParameter("ITEM_ID");

        int itemId = -1;
        if (itemParam != null)
        {
            itemId = Integer.parseInt(itemParam);
        }

        if ("MARK_FINISHED".equals(command))
        {
            ToDoItemBean item = (ToDoItemBean) list.get(itemId);
            item.setStatus(true);

        }
        else if ("DELETE".equals(command))
        {
            list.remove(itemId);
        }
        else if ("MARK_UNFINISHED".equals(command))
        {
            ToDoItemBean item = (ToDoItemBean) list.get(itemId);
            item.setStatus(false);

        }
        else if ("EDIT".equals(command))
        {
            ToDoItemBean item = (ToDoItemBean) list.get(itemId);

            String desc = request.getParameter("DESCRIPTION");
            item.setDescription(desc);

            String priority = request.getParameter("PRIORITY");
            if (priority != null)
            {

                try
                {
                    int p = Integer.parseInt(priority);
                    item.setPriority(p);
                }
                catch (NumberFormatException nfe)
                {
                    getPortletContext().log(
                        "Error trying to format " + priority + " as a number.");
                }
            }
        }
        else if ("NEW".equals(command))
        {
            ToDoItemBean item = new ToDoItemBean();

            String desc = request.getParameter("DESCRIPTION");

            if (desc == null)
            {
                return;
            }

            item.setDescription(desc);
            item.setStatus(false);
            item.setSubmittedDate(new java.util.Date());
            item.setPriority(0);

            list.add(item);
        }

        session.setAttribute("ToDoList", list, PortletSession.APPLICATION_SCOPE);

    }

    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        PortletContext portletContext = getPortletContext();

        PortletRequestDispatcher prd =
            portletContext.getRequestDispatcher("/WEB-INF/jsp/homePage.jsp");
        prd.include(request, response);
    }

    protected void doEdit(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        String display = request.getParameter("DISPLAY");

        PortletContext portletContext = getPortletContext();
        if ("EDIT_PAGE".equals(display))
        {
            PortletRequestDispatcher prd =
                portletContext.getRequestDispatcher(
				    "/WEB-INF/jsp/editItemPage.jsp");
            prd.include(request, response);
        }
        else
        {
            PortletRequestDispatcher prd =
                portletContext.getRequestDispatcher("/WEB-INF/jsp/editPage.jsp");
            prd.include(request, response);
        }
    }

    protected void doHelp(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {
        response.setContentType("text/html");

        Writer writer = response.getWriter();

        PortletContext portletContext = getPortletContext();

        PortletRequestDispatcher prd =
            portletContext.getRequestDispatcher("/WEB-INF/jsp/helpPage.jsp");
        prd.include(request, response);

    }
}
