package fr.srosoft.wineyard.core.main;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ComponentScan("fr.srosoft.wineyard.core.main")
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

	@Resource
    private CustomAuthenticationProvider authProvider;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authProvider).eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
          .antMatchers("/public/**").permitAll()
          .antMatchers("/javax.faces.resource/**").permitAll()
          .antMatchers("/private/**").authenticated()
          .and()
          .formLogin().defaultSuccessUrl("/private/wineyard/home.jsf");
    }
}
