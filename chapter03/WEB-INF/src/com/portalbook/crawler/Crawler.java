package com.portalbook.crawler;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Class to crawl a site or set of sites. Designed to be run in a background 
 * thread. The current set of sites crawled can be gathered at any time by an
 * external thread.  
 */
public class Crawler implements Runnable {

	/**
	 * Construct an instance to crawl a given path. This will
	 * only search within the host of the path specified.
	 *
	 * @param path An HTTP URL represented as a String
	 * @throws MalformedURLException Thrown if the path provided does not represent a valid URL
	 */
	public Crawler(String path)
		throws MalformedURLException
	{
		this(path,DEFAULT_LINK_DEPTH);
	}
	
	/**
	 * Construct an instance to crawl a given path. This will
	 * search within the host of the path specified, and up to 
	 * depth sites away. If depth is two, therefore, the crawler
	 * will look within the host of the path specified and within
	 * the hosts of sites referenced directly from this site, but 
	 * no further.
	 * 
	 * @param path An HTTP URL represented as a String
	 * @param depth The number of hosts away that the crawler may search
	 * @throws MalformedURLException Thrown if the path provided does not represent a valid URL
	 */
	public Crawler(String path, int depth)
		throws MalformedURLException
	{
		this(new HashSet(Arrays.asList(new Object[] { new URL(path) })),
			  new HashSet(),
			  new HashSet(),
			  new HashSet(),
			  new HashSet(),
			  depth);
	}

	/**
	 * Construct an instance to crawl a given set of paths. This will 
	 * search within the hosts specified, and up to depth sites away 
	 * from those sites. It will not search forbidden hosts, failed hosts, 
	 * or already visited hosts.
	 * 
	 * @param links The set of URL links that we want the crawler to search
	 * @param visited The set of URL links that have already been searched
	 * @param visitedHosts The set of hosts (as Strings) that have already been searched
	 * @param forbidden The set of URL links that are forbidden by their hosts' robots.txt files
	 * @param failed The set of URL links that could not be searched for some reason
	 * @param depth The number of hosts away from the links that the crawler may search
	 */
	protected Crawler( Set links, 
					   	 Set visited, 
					   	 Set visitedHosts,
						 	 Set forbidden,
						 	 Set failed, 
							 int depth )
	{
		this.links			= links;
		this.visited		= visited;
		this.visitedHosts	= visitedHosts;
		this.forbidden		= forbidden;
		this.failed			= failed;
		this.currentHost	= (URL)links.iterator().next();
		this.depth			= depth;
	}

	/**
	 * Starts or resumes the processing of the link or links. 
	 */
	public void run() {
		// Flag that work is in progress
		setStopped(false);
		try {
			
			// While we've not been instructed to stop AND there
			// are links to search AND we've not hit the edges of
			// the network of links we're allowed to search...
			while(!isStopped() && (links.size() > 0) && (depth > 0) ) {
				
				// Get the first link from the queue of
				// links to search
				URL link = (URL)links.iterator().next();
				
				// If the link is on a different host
				if( !isCurrentHost(link) ) {
					// Go to that host and search the
					// link if we're allowed
					crawlNewHost(link);
				} else {
					
					// Crawl the link
					crawl(link);
				}
			}
		} finally {
			// Normal or abnormal termination should
			// flag that work has completed regardless
			setStopped(true);
		}
	}
	
	/**
	 * Processes a buffer containing the contents of a robots.txt file
	 * and adds forbidden URLs to the 
	 * 
	 * The context of the robots.txt file is required to convert the
	 * relative disallowed paths into absolute URLs
	 * 
	 * @param buffer The buffer containing the text of the robots.txt file
	 * @param link The URL representing this specific robots.txt on its host
	 */
	private void processRobotBuffer(StringBuffer buffer,URL link) {
		// Prepare to gather up potential URL strings
		List disallows = new ArrayList();
		
		// Look for Disallow tokens. Any token immediately following a Disallow: is presumed to
		// be (potentially) a path.
		StringTokenizer tokenizer = new StringTokenizer(buffer.toString());
		while(tokenizer.hasMoreElements()) {
			String element = tokenizer.nextToken();
			
			if( element.equalsIgnoreCase(DISALLOW) && tokenizer.hasMoreElements() ) {
				String path = tokenizer.nextToken();
				disallows.add(path);
			}
		}
		
		// Iterate over the disallow tokens gathered and convert them into
		// absolute paths and thence URLs to forbid access
		Iterator i = disallows.iterator();
		while(i.hasNext()) {
			String path = (String)i.next();
			try {
				URL disallowedURL = new URL(link,path);
				forbidden.add(disallowedURL);				
			} catch( MalformedURLException e ) {
				// Couldn't form a URL from this. No point 
				// disallowing access to a link we can't 
				// access anyway.
			}
		}
	}

	/**
	 * Determine the robots file that governs access to the host
	 * on which the link resides. Parses and disallows access to
	 * links as required by the robots specification - however, 
	 * we're ultra polite and assume that ANY link that's disallowed
	 * to ANYBODY must be forbidden to us. This makes the logic 
	 * slightly simpler, but would not normally be done in a 
	 * production system.
	 * 
	 * The robots information is cached by host, so we only look it up
	 * when we encounter the first link from a site.
	 * 
	 * @param link The link for which to verify the robots permissions
	 */
	private void processRobots(URL link) {
		HttpURLConnection connection = null;
		try {
			// If the host has been visited before, we're not
			// obliged to re-read the ROBOTS.TXT file - if this
			// is the first visit, however, we need to determine
			// which paths to discard.
			if( !visitedHosts.contains(link.getHost()) ) {
				
				URL robotLink = new URL(link,"/robots.txt");
				
				// Create a HTTP connection to the link
				connection = (HttpURLConnection)robotLink.openConnection();

				// GET the page
				connection.setRequestMethod(GET);
				connection.setRequestProperty(USER_AGENT,AGENT_IDENTIFIER);
				InputStream input = (InputStream)connection.getContent();
				
				// Read the page into a buffer
				BufferedReader reader = 
				new BufferedReader(new InputStreamReader(input));

				int i = 0;
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while((line = reader.readLine()) != null) {
					i++;
					buffer.append(line);
					buffer.append('\r');
				}
				
				// Close the connection to the page and discard
				// held resources
				reader.close();
				connection.disconnect();
				connection = null;

				// Process the buffer to determine what links are permitted
				processRobotBuffer(buffer,link);
			}
		} catch( IOException e ) {
			// Good. There is no robots.txt file, we're allowed to view 
			// the site.
		} finally {
			// Ensure that the connection is always closed
			if( connection != null ) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Crawls a specified URL and adds all valid HREF entries
	 * to the queue of links to be crawled unless they are 
	 * denied by the appropriate robots.txt file, or have
	 * already been crawled.
	 * 
	 * @param link The link to crawl
	 */
	private void crawl(URL link) {
		HttpURLConnection connection = null;
		try {
			// Check to see what is/isn't permitted on this host
			processRobots(link);
			
			// Create a HTTP connection to the link
			connection = (HttpURLConnection)link.openConnection();

			// GET the page
			connection.setRequestMethod(GET);
			
			String contentType = connection.getContentType();
			if( (contentType != null) && contentType.startsWith(TEXT_HTML) ) {
				InputStream input = (InputStream)connection.getContent();
				
				// Read the page into a buffer
				BufferedReader reader = 
				new BufferedReader(new InputStreamReader(input));

				int i = 0;
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while((line = reader.readLine()) != null) {
					i++;
					buffer.append(line);
				}
				
				// Close the connection to the page and discard
				// held resources
				reader.close();
				connection.disconnect();
				connection = null;

				// Process the page. This assumes that the page is
				// small enough to manage in memory
				processBuffer(buffer,link);
			} else {
				connection.disconnect();
				connection = null;
			}
			
			// We've handled the item, so remove it from the queue of links			
			visited(link);
		} catch( IOException e ) {
			// The item caused a problem, so remove it from the queue
			links.remove(link);
			failed.add(link);			
		} finally {
			// Ensure that the connection is closed
			if( connection != null ) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Carries out actions once a link has been crawled. Removes
	 * the link from the queue to visit, adds it to the set of
	 * visited links, and adds its host to the set of visited hosts
	 * 
	 * @param link The link that has now been crawled
	 */
	private void visited(URL link) {
		links.remove(link);
		visited.add(link);
		visitedHosts.add(link.getHost());
	}
	
	/**
	 * Processes a buffer containing an html page. The context is 
	 * provided so that relative URLs can be resolved to their 
	 * absolute URLs
	 * 
	 * HREFs within the html are extracted, converted to absolute 
	 * URLs and added to the queue of links to be crawled (where
	 * they have not already been visited, and have not been forbidden
	 * by a robots file)
	 *
	 * @param buffer The buffer containing the page
	 * @param url The context of the page being crawled
	 */
	private void processBuffer(StringBuffer buffer, URL url) {

		// Prepare to gather up potential URL strings
		List foundHREFs = new ArrayList();
		
		// Look for HREF tokens. Any token immediately following an HREF is presumed to
		// be (potentially) an HREF.
		StringTokenizer tokenizer = new StringTokenizer(buffer.toString(),DELIM);
		while(tokenizer.hasMoreElements()) {
			String element = tokenizer.nextToken();
			
			if( element.equalsIgnoreCase(HREF) && tokenizer.hasMoreElements() ) {
				String path = tokenizer.nextToken();
				foundHREFs.add(path);
			}
		}

		// Boil 'em down to absolute URLs
		Set absolute = new HashSet();
		Iterator i = foundHREFs.iterator();
		while(i.hasNext()) {
			String path = (String)i.next();
			
			try {
				URL toAdd = new URL(url,path);
				if( !toAdd.getProtocol().equalsIgnoreCase("http") ) {
					// Not a protocol we're interested in
				} else {
					absolute.add(toAdd);
				}
			} catch( MalformedURLException e ) {
				// Invalid path. Ignore; it's probably a typo
			}
		}
		
		// Remove all the URLs that we're not allowed to visit for one reason
		// or another:
		absolute.removeAll(forbidden);
		
		// Remove all the URLs that we've already visited anyway:
		absolute.removeAll(visited);
		
		// Add the remainder to the visit queue
		links.addAll(absolute);
	}
	
	/**
	 * Crawls a link that is on a host other than the one currently
	 * being processed. The simplest way to effect this is to instantiate
	 * a new crawler specifying the appropriate url but passing it the 
	 * information on visited and forbidden hosts so that already crawled
	 * links can be ignored - note that we reduce the permitted depth by
	 * one for this next crawler so that it can't creep too far away from 
	 * the original link. Eventually we'll reach zero and there's no point
	 * in instantiating the crawler because it would simply complete 
	 * immediately.
	 * 
	 * @param url The link to crawl
	 */
	private void crawlNewHost(URL url) {
		Set links = new HashSet();
		links.add(url);
		if( depth > 1 ) {

			// Create a new crawler to crawl the external link
			Crawler crawler = new Crawler(links,visited,visitedHosts,forbidden,failed,(depth - 1));
			
			// The new crawler should be stopped whenever THIS crawler is stopped.
			addListener(crawler);			
			crawler.run();
		}
		
		// Flag that the URL has been visited.
		this.links.remove(url);
	}
	
	/**
	 * Adds a crawler to a set of crawlers that are children of THIS crawler
	 * so that they can all by stopped when this one is stopped.
	 *
	 * @param crawler The crawler to be stopped when this one is stopped
	 */
	private void addListener(Crawler crawler) {
		stopListeners.add(crawler);
	}
	
	/**
	 * Stops all the crawlers that are children of this crawler 
	 */
	private void notifyStopCrawlers() {
		Iterator i = stopListeners.iterator();
		while(i.hasNext() && depth > 0) {
			Crawler crawler = (Crawler)i.next();
			crawler.setStopped(true);
		}
	}
	
	/**
	 * Test to see if the crawler is currently running
	 * 
	 * @return True if the crawler is running, false otherwise
	 */
	public boolean isStopped() {
		return stopped;
	}
	
	/**
	 * Allows an external thread to stop the crawler
	 */
	public void stopCrawler() {
		setStopped(true);
	}
	
	/**
	 * Flags that the crawler should stop (if true) or 
	 * that it has started (if false).
	 * 
	 * @param stopped
	 */
	private void setStopped(boolean stopped) {
		if(stopped) notifyStopCrawlers();
		this.stopped = stopped;
	}

	/**
	 * Retrieves the set of links as URLs that the
	 * crawler has encountered so far and which were valid.
	 * 
	 * @return A copy of the set of valid URLs encountered so far
	 */
	public Set getVisitedURLs() {
		synchronized(this.visited) {
			return new HashSet(this.visited);
		}
	}

	/**
	 * Retrieves the set of hosts as Strings that the
	 * crawler has encountered so far
	 * 
	 * @return A copy of the set of hosts encountered so far
	 */
	public Set getVisitedHosts() {
		synchronized(this.visitedHosts) {
			return new HashSet(this.visitedHosts);
		}
	}
	
	/**
	 * Retrieves the set of links as URLs that the
	 * crawler was forbidden to search by their associated
	 * robots.txt files
	 * 
	 * @return A copy of the set of forbidden link
	 */
	public Set getForbiddenURLs() {
		synchronized(this.forbidden) {
			return new HashSet(this.forbidden);
		}
	}
	
	/**
	 * Retrieves the set of invalid links as URLs that the
	 * crawler could not search (usually because of a network
	 * problem of some sort - "404 Host Not Found" for instance
	 * 
	 * @return A copy of the set of failed links
	 */
	public Set getFailedURLs() {
		synchronized(this.failed) {
			return Collections.unmodifiableSet(this.failed);
		}
	}
	
	/**
	 * Determines if a given link falls within the host being crawled
	 * by this crawler's instance.
	 * 
	 * @param url The link to test
	 * @return True if the host of the link is the one being searched by this crawler, false otherwise
	 */
	private boolean isCurrentHost(URL url) {
		return currentHost.getHost().equalsIgnoreCase(url.getHost());
	}
	
	// Identifies the host that this crawler is searching
	private URL currentHost;
	
	// Thread termination handling attributes
	private Set stopListeners = new HashSet();	
	private boolean stopped = true;
	
	// Maintain information on where we've been so far
	private Set links;
	private Set visited;
	private Set visitedHosts;
	private Set forbidden;
	private Set failed;
	private int depth;
	
	// String constants required by the application
	// mostly during tokenization or protocol negotiation
	private static final String DELIM		=	"\t\r\n\" <>=";
	private static final String TEXT_HTML	=	"text/html";
	private static final String GET 			=	"GET";
	private static final String HREF			=	"HREF";
	private static final String HTTP			=	"HTTP:";
	private static final String DISALLOW	=	"DISALLOW:";
	private static final String USER_AGENT	=	"User-Agent";
	
	// The "official" name of this agent for identification in the
	// logs of the servers we're searching. 
	private static final String AGENT_IDENTIFIER="WoolGatherer";
	
	// Default search depth to be used if the single argument constructor
	// is used.
	private static final int DEFAULT_LINK_DEPTH = 1;
}
