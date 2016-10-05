package com.portalbook.portlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class ResourceBundlePortlet extends GenericPortlet
{
	protected void doView(RenderRequest request, RenderResponse response) 
			throws PortletException, IOException
		{
			response.setContentType("text/html");
		
			Writer writer = response.getWriter();
		
			writer.write("<H2>Resource Bundle Values</H2>");
			
			//get the current Locale
			Locale locale = request.getLocale();
			
			//get the Resource Bundle
			ResourceBundle rb = getResourceBundle(locale);
			
			//get the title
			String title = rb.getString("javax.portlet.title");
			
			//get the short title
			String shortTitle = rb.getString("javax.portlet.short-title");
			
			//get the keywords
			String keywords = rb.getString("javax.portlet.keywords");
			
			//Output the title, short title, and keywords
			writer.write("Title: " + title + "<P>");
			writer.write("Short title: " + shortTitle + "<P>");
			writer.write("Keywords: " + keywords + "<P>");
		}
}
