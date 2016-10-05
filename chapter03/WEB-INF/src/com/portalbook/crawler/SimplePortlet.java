package com.portalbook.crawler;
import java.io.*;
import javax.portlet.*;

public class SimplePortlet
	implements Portlet {
	
	public SimplePortlet() {
	}

	public void destroy() {
		portletCounter--;
	}

	public void init(PortletConfig config) 
		throws PortletException
	{
		portletCounter++;
	}

	public void processAction(
			ActionRequest request, 
			ActionResponse response)
		throws PortletException, IOException
	{
		actionCounter++;
	}

	public void render(
			RenderRequest request, 
			RenderResponse response)
		throws PortletException, IOException
	{
		renderCounter++;

		response.setTitle("Simple Portlet");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("The server has instantiated " + 
				portletCounter + 
				" copies of the portlet<BR>");
		
		out.write("This portlet has been rendered " + 
				renderCounter + 
				" times (including this one)<BR>");
		
		out.write("This portlet has received " + 
				actionCounter + 
				" action requests<BR>");
		
		out.write("The user Principal is: " +request.getUserPrincipal());
	}

	private static int portletCounter = 0;
	private int renderCounter = 0;
	private int actionCounter = 0;
}
