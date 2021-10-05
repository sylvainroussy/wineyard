package fr.srosoft.wineyard.core.main;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.dao.SystemDao;
import fr.srosoft.wineyard.core.model.entities.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Resource
	private SystemDao systemDao;
	
	@Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
		
		
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        final User wineyardUser = systemDao.login(name, password);
        if (wineyardUser != null) {
 
            // use the credentials
            // and authenticate against the third-party system
        	
        	
        	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        			wineyardUser, password, new ArrayList<>());
        	//token.setAuthenticated(true);
            return token;
        } else {
        	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    "anonymous", "*****", new ArrayList<>());
        	token.setAuthenticated(false);
            return token;
           // return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;//authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
