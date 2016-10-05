package com.portalbook.forums;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Copyright &copy; David Minter 2004
 * 
 * @author Dave
 */
public class SkinServer extends HttpServlet {
    
    public SkinServer() {
        super();
    }

    private String getSkin(HttpSession session){
        log("Getting skin");
        String skin = (String)session.getAttribute(USER_SKIN_ATTRIBUTE);
        return (skin == null) ? this.skin : skin;
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

      log("Service request initialized");
        
      // Obtain the User's desired "skin" from the session
      String skin = getSkin(request.getSession());
      log("Got skin: " + skin);
      
      // Convert the skin into an appropriate path into the META-INF/jsp/skins/* directory        
      String skinPath = "/WEB-INF/skins/" + skin + request.getPathInfo();
      
      // Explicitly set the content type
      response.setContentType("text/html");
      
      // Logging
      log("Handing off request to JSP: " + skinPath);
      getServletContext().getRequestDispatcher(skinPath).include(request,response);
   }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
        log("init(...) invoked");

        // Extract skin behaviour from the configuration...        
        String skin = config.getInitParameter(DEFAULT_SKIN_PARAM);
        log("Skin extracted from params was: " + skin);
        if(skin != null) this.skin = skin;
    }

    private String skin = DEFAULT_SKIN;
    private static final String DEFAULT_SKIN = "admin";
    private static final String DEFAULT_SKIN_PARAM = "defaultskin";
    
    public static final String USER_SKIN_ATTRIBUTE = "USER_SKIN_ATTRIBUTE";
}