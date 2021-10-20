package fr.srosoft.wineyard.core.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.User;


@Service
public class SystemDao extends AbstractDao {

	
	
	private final String  QUERY_BUILD_USER = "MERGE (u:User{id:apoc.util.md5([$email])}) SET u+=$patch, u.password=apoc.util.md5([$password]) ";
	private final String  QUERY_EXISTS_USER = "OPTIONAL MATCH (u:User{id:apoc.util.md5([$email])}) RETURN u IS NOT NULL AS exists";
	private final String  QUERY_LOGIN_USER = "OPTIONAL MATCH (user:User{id:apoc.util.md5([$email]), password:apoc.util.md5([$password])}) RETURN user";
	
	
	public void createUser (final User user, final String password) {
		final Map<String,Object> parameters = new HashMap<>();
    	final Map<?,?> patch =  MAPPER.convertValue(user, Map.class);
    	patch.remove("id");
    	parameters.put("patch",patch);
    	parameters.put("email", user.getEmail());
    	parameters.put("password", password);
    	this.writeQuery(QUERY_BUILD_USER, parameters);
	}
	
	public boolean isUserExists(String email) {
		
		final Map<String,Object> parameters = new HashMap<>();
    	parameters.put("email", email);      
    	return this.readSingleQuery(QUERY_EXISTS_USER, parameters,"exists", Boolean.class);
		
	}
	
	public User login(String email, String password) {
		
		final Map<String,Object> parameters = new HashMap<>();
    	parameters.put("email", email);   
    	parameters.put("password", password);  
    	return this.readSingleQuery(QUERY_LOGIN_USER, parameters,"user", User.class);
		
	}
	
	
	
	
	
}
