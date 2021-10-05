package fr.srosoft.wineyard.core.main;



import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan (basePackages={"fr.srosoft"})
public class WineyardApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
        SpringApplication.run(WineyardApplication.class, args);
    }
	
	@Bean
    public ServletRegistrationBean<Servlet> servletRegistrationBean() {
        Servlet servlet = new FacesServlet();
        return new ServletRegistrationBean<Servlet>(servlet, "*.jsf");
    }
}
