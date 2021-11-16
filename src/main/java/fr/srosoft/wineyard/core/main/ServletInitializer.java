package fr.srosoft.wineyard.core.main;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletInitializer implements ServletContextInitializer {

    
     @Override
	public void onStartup(ServletContext jsfServlet) throws ServletException {
    	 System.out.println(jsfServlet.getClass().getName());
	   jsfServlet.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
	    jsfServlet.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
	    jsfServlet.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
	    jsfServlet.setInitParameter("facelets.DEVELOPMENT", "true");
	    jsfServlet.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");
	    jsfServlet.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
	    jsfServlet.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
	    jsfServlet.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
	    jsfServlet.setInitParameter("primefaces.UPLOADER", "native");
	    //jsfServlet.setInitParameter("primefaces.UPLOADER",
        //        "commons");
	    
	   // jsfServlet.addFilter("primeFacesFileUploadFilter", FileUploadFilter.class).addMappingForUrlPatterns(EnumSet.of(javax.servlet.DispatcherType.FORWARD),true, "/*");
	    
	    
	    //jsfServlet.setSessionTimeout(1);

    }
     
   
     
    /* @Bean
     public FilterRegistrationBean FileUploadFilter() {
         FilterRegistrationBean registration = new FilterRegistrationBean();
         registration.setFilter(new org.primefaces.webapp.filter.FileUploadFilter());
         registration.setName("PrimeFaces FileUpload Filter");
         registration.setDispatcherTypes(DispatcherType.FORWARD);
         return registration;
     }
     
     @Bean
     public ServletRegistrationBean<?> facesServlet() {
         FacesServlet servlet = new FacesServlet();
         ServletRegistrationBean registration = new ServletRegistrationBean(servlet, "*.xhtml");
         registration.setName("Faces Servlet");
         registration.setLoadOnStartup(1);
         registration.setMultipartConfig(new
                 MultipartConfigElement((String) null));
         return registration;
     }

     @Bean
     public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
         return new ServletListenerRegistrationBean<ConfigureListener>(new ConfigureListener());
//     }*/

   

    /* @Bean
     public FilterRegistrationBean hiddenHttpMethodFilterDisabled(
             @Qualifier("hiddenHttpMethodFilter") HiddenHttpMethodFilter filter) {
         FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
         filterRegistrationBean.setEnabled(false);
         return filterRegistrationBean;
     }*/

}