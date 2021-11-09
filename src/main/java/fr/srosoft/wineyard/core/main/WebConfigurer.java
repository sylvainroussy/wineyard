package fr.srosoft.wineyard.core.main;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer  implements WebMvcConfigurer{
	
	 @Override
	  public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/").setViewName("forward:/public/index.html");
	    registry.addViewController("/login").setViewName("login");       
	    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	  }

}
