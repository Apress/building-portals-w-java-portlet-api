	package com.portalbook.portlets.content;
	
	import java.io.IOException;
	import java.io.Writer;
	import java.util.HashMap;
	
	import javax.portlet.GenericPortlet;
	import javax.portlet.PortletException;
	import javax.portlet.PortletSession;
	import javax.portlet.RenderRequest;
	import javax.portlet.RenderResponse;
	
	public class ContentPortlet extends GenericPortlet
	{
	
	    private HashMap contentMap = new HashMap();
	
	    public void init()
	    {
	        contentMap.put("xerces", "Xerces is an open source XML Parser.");
	        contentMap.put("lucene", "Lucene is a Java search engine.");
	        contentMap.put("xalan", "Xalan is an open source XSLT engine.");
	        contentMap.put("jdom", "JDOM is an open source Java XML parser.");
	    }
	
	    public void doView(RenderRequest request, RenderResponse response)
	        throws PortletException, IOException
	    {
	        response.setContentType("text/html");
	        Writer writer = response.getWriter();
	
	        PortletSession session = request.getPortletSession(true);
	
	        String contentId =
	            (String) session.getAttribute(
	                "contentId",
	                PortletSession.APPLICATION_SCOPE);
	
	        if (contentId == null)
	        {
	            writer.write("No content selected yet.");
	        }
	        else
	        {
	            if (contentMap.containsKey(contentId))
	            {
	
	                String content = (String) contentMap.get(contentId);
	                writer.write(content);
	            }
	            else
	            {
	                writer.write("Content not found for: " + contentId);
	            }
	
	        }
	
	    }
	}