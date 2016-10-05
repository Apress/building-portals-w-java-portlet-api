package com.portalbook.rss;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import churchillobjects.rss4j.*;
import churchillobjects.rss4j.generator.*;

public class RSSExample 
   extends HttpServlet 
{

    public RSSExample() {
    }

    public void init() {
        document = new RssDocument();
        document.setVersion(RssDocument.VERSION_91);

        RssChannel channel = new RssChannel();
        channel.setChannelLanguage("en");
        channel.setChannelTitle("The Periodical of the Exemplary Society");
        channel.setChannelLink("http://example.com");
        channel.setChannelDescription(
            "Discourse on the subject, object, and practice of examples");
        channel.setChannelUri("http://example.com/rss/");
        document.addChannel(channel);

        for (int i = 0; i < 10; i++) {
            RssChannelItem item = new RssChannelItem();
            item.setItemTitle("Article number " + i);
            item.setItemLink("http://example.com/" + i);
            item.setItemDescription(
                "Example description of article number " + i);
            channel.addItem(item);
        }
    }

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        try {
            response.setContentType("text/xml");
            OutputStream out = response.getOutputStream();
            RssGenerator.generateRss(document, out);
            out.flush();
        } catch (RssGenerationException e) {
            throw new ServletException("Cannot generate RSS feed", e);
        }
    }
    
    private RssDocument document;
}
