package com.portalbook.crawler;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Illustrates some aspects of a Portlet's lifecycle
 */
public class CrawlerPortlet 
	extends GenericPortlet 
{
	/**
	 * Default constructor. No additional behaviour necessary.
	 */
	public CrawlerPortlet() {
		super();
	}

	/**
	 * Extracts configuration information and uses it
	 * to setup the Crawler 
	 *
	 * @param config The configuration information for the portlet
	 * @throws PortletException Thrown if there is a problem accessing the config information
	 */
	public void init(PortletConfig config)
	throws PortletException 
	{
		// Retrieve the path for the crawler as configured.
		String path = (String)config.getInitParameter("crawlPath");
		try {
			// Create the crawler
			crawler = new Crawler(path);
			
			// Start the crawler on a background thread so that the current
			// thread can complete its initialisation promptly.
			Thread background = new Thread(crawler);
			background.start();
		} catch( MalformedURLException e ) {
			// Complain that the provided URL is wrong
			throw new PortletException("Portlet could not be initialised because of a malformed path in the configuration [" + path + "]",e);
		}
	}

	/**
	 * Invoked by the container to destroy the portlet
	 * and free up any resources it is using
	 */
	public void destroy() {
		// The only resource we're using is the crawler running on our background
		// thread - this should be stopped so that it can be garbage collected.
		crawler.stopCrawler();
		crawler = null;
	}

	/**
	 * A convenient method to allow the contents of a collection to be
	 * rendered in HTML as cells in a table.
	 * 
	 * @param out A PrintWriter which will be used as the sink for the rendered HTML
	 * @param title The title of the table
	 * @param collection The collection to be rendered
	 * @throws IOException Thrown if there is a problem writing the data
	 */
	private void renderCollection( PrintWriter out, String title, Collection collection )
		throws IOException
	{
		// Open the table and render the title
		out.write("<table cellspacing=\"0\" border=\"0\">");
		out.write("<tr><td><b>");
		out.write(title);
		out.write("</b></td></tr>");
		
		// Render each of the cells
		Iterator i = collection.iterator();
		while(i.hasNext()) {
			out.write("<tr><td>");
			out.write(i.next().toString());
			out.write("</td></tr>");
		}
		
		// Close the table
		out.write("</table><br>");
	}

	/**
	 * Invoked to display the portlet in its "View" mode. The request
	 * carries any parameters (ignored by our portlet) and the response
	 * is the sink to which we must render our HTML fragment.
	 * 
	 * Exceptions may be thrown if there is a problem handling the view.
	 * 
	 * @param request Represents the render request from the user
	 * @param response Represents the response that we will be sending to the user
	 * @throws PortletException Thrown if there is a problem handling the request
	 * @throws IOException Thrown if there is a problem writing the response
	 */
	protected void doView(RenderRequest request, RenderResponse response)
		throws PortletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.write("<table><tr><td>");
		out.write("<h2>Crawler</h2>");

		out.write("<table cellspacing=\"0\" border=\"1\">");
		out.write("<tr><td align=\"right\"><i>Status</i></td><td><b>");
		out.write(crawler.isStopped() ? "stopped" : "running");
		out.write("</b></td></tr>");
		out.write("</table>");
		
		renderCollection(out, "Hosts Crawled", crawler.getVisitedHosts());
		renderCollection(out, "Links Visited", crawler.getVisitedURLs());
		renderCollection(out, "Failed Links", crawler.getFailedURLs());
		renderCollection(out, "Forbidden Links", crawler.getForbiddenURLs());
		out.write("</td></tr></table>");
	}

	/**
    * Provide a title for the portlet based upon the render request
    * that has been issued.
    * 
    * @param request Represents the render request from the user
    * @return The title of the portlet in the context of the user's request 
	 */
	protected String getTitle(RenderRequest request) {
		return "Link Crawler";
	}

	// The reference to the crawler that will be providing the data
	private Crawler crawler;
}
