package com.portalbook.portlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


public class SessionPortlet extends GenericPortlet
{
	public void doView(RenderRequest req, RenderResponse resp)
		throws PortletException, IOException
	{
		String newMessage = null;
		
		//set up for output
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		
		//get the session, or create it if needed
		PortletSession session = req.getPortletSession();
		
		//if there is already a value in the session, get it
		Object message = session.getAttribute("message");
		if (message == null)
		{
			//This is nothing in the session stored by the name 'message'
			newMessage = "Hi, This is the first visit to the portlet.";
		
		} 
		else if (message instanceof String)
		{
			//change the message for repeat visitors
			newMessage = "Welcome back to this portlet!";
		}

		//Store it in the portlet session
		session.setAttribute("message", newMessage);
		
		//write it out
		writer.write(newMessage);
	}

}
