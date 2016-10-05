package com.portalbook.forums;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import com.Yasna.forum.Authorization;
import com.Yasna.forum.AuthorizationFactory;
import com.Yasna.forum.ForumFactory;
import com.portalbook.forums.tags.UrlTag;

public class ForumPortlet extends GenericPortlet
{

   public void processAction(ActionRequest request,
         ActionResponse response) throws PortletException, IOException
   {
      // If the user has requested a different count of
      // forums in the default view, extract and preserve this
      // for subsequent use
      if (request.getParameter("ForumCount") != null)
      {
         String count = request.getParameter("ForumCount");
         getPortletContext().log(
               "Forum Count request retrieved: " + count);
         request.getPreferences().setValue("ForumCount", count);
         getPortletContext().log("Forum Count set as preference");
         request.getPreferences().store();
         getPortletContext().log("Forum Count preference stored");
      }

      super.processAction(request, response);
   }
   /**
    * Calls the request dispatcher to include a specified JSP path
    * in the portlet output.
    * 
    * @param path The path to the JSP to include
    * @param request The request object to pass in
    * @param response The response object to pass in
    * @throws PortletException Thrown if there is a 
    *                          problem accessing the context
    * @throws IOException Thrown if there is a 
    *                            problem writing the page fragment
    */
   private void include(String path, RenderRequest request,
         RenderResponse response) throws PortletException, IOException
   {
      getPortletContext().getRequestDispatcher(path).include(request,
            response);
   }

   /**
    * Invoked by the Portal to render our portlet. This should cause
    * the content of an appropriate JSP page to be rendered within 
    * the portlet.
    * 
    * @param request The request object from the invocation
    * @param response The response object from this portlet
    * @throws PortletException Thrown if there is a 
    *                          problem accessing the context
    * @throws IOException Thrown if there is a problem 
    *                     writing the page fragment
    */
   protected void doView(RenderRequest request, RenderResponse response)
         throws PortletException, IOException
   {
      PortletContext context = getPortletContext();
      WindowState state = request.getWindowState();

      // Retrieve the JSP to direct to (if any) (formatted as "path/path/file.jsp")
      String href = request.getParameter(UrlTag.HREF);

      getPortletContext().log("Href retrieved: " + href);

      // Retrieve the query to append (if any) (formatted as "?x=y&z=w")
      String query = request.getParameter(UrlTag.QUERY);
      getPortletContext().log("Query retrieved: " + query);

      if ((href != null) && (query != null))
         href += ("?" + query);
      if (href == null)
         href = VIEW;
      
      if (state.equals(WindowState.NORMAL))
      {
         include(PORTLET_GUI + NORMAL + href, request, response);
      }
      else if (state.equals(WindowState.MINIMIZED))
      {
         include(PORTLET_GUI + MINIMIZED + href, request, response);
      }
      else if (state.equals(WindowState.MAXIMIZED))
      {
         include(PORTLET_GUI + MAXIMIZED + href, request, response);
      }
      else
      {
         throw new PortletException(
               "Unrecognized WindowState in View mode: "
                     + state.toString());
      }
   }

   /**
    * Invoked by the portal to determine the title of
    * the portlet. We return a string identifying the
    * name of the portlet plus the number of forums
    * available to the system.
    * 
    * @param request The render request from the portal
    * @return The title String 
    */
   protected String getTitle(RenderRequest request)
   {
      Authorization auth = AuthorizationFactory
            .getAnonymousAuthorization();
      int count = ForumFactory.getInstance(auth).getForumCount();
      return forumName.trim() + " (" + count
            + ((count != 1) ? " forums)" : " forum)");
   }

   /**
    * Initialise the configuration of the
    * portlet.
    * 
    * @param config The configuration object from which
    *               configuration parameters should be 
    *               drawn.
    */
   public void init(PortletConfig config) throws PortletException
   {
      super.init(config);

      // Retrieve the name of the forums for display
      forumName = config.getInitParameter(FORUM_NAME);
      if (forumName == null)
         forumName = "Portalbook";
   }

   // The name of the forums
   private String forumName;

   // The configuration name in the portlet.xml file
   private static final String FORUM_NAME = "title";

   // The location (relative to the portal's webapp) from which to obtain
   // the GUI components which are all written as JSPs
   private static final String PORTLET_GUI = "/WEB-INF/skins/portalized/";

   // The path in the skin directory containing the various
   // window-state versions of the display
   private static final String MINIMIZED = "minimized/";
   private static final String MAXIMIZED = "maximized/";
   private static final String NORMAL = "normal/";

   // The paths in the appropriate skin directories for
   // the mode views of the portlet
   private static final String VIEW = "view.jsp";

   // The identified for an unknown user
   private static final String UNKNOWN = "anonymous";
}