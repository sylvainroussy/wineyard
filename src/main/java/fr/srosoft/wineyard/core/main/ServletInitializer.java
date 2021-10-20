package fr.srosoft.wineyard.core.main;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletInitializer implements ServletContextInitializer {

    
     @Override
	public void onStartup(ServletContext jsfServlet) throws ServletException {
   jsfServlet.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
    jsfServlet.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
    jsfServlet.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
    jsfServlet.setInitParameter("facelets.DEVELOPMENT", "true");
    jsfServlet.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");
    jsfServlet.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
    jsfServlet.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
    jsfServlet.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
    
    
    //jsfServlet.setSessionTimeout(1);

    }

}