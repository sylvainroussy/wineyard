package fr.srosoft.wineyard.core.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.User;

@Service
public class DomainDao extends AbstractDao {

	private final String QUERY_BUILD_DOMAIN = "MERGE (domain:Domain{id:$id}) SET domain+=$patch ";
	private final String QUERY_FIND_DOMAIN_BY_ID = "MATCH (domain:Domain{id:$id}) RETURN domain ";
	private final String QUERY_FIND_DOMAIN_BY_MANAGER = "MATCH (user:User{id:$userid}) "
			+ "MATCH (user)<-[:MANAGED_BY]-(domain) RETURN domain ";
	
	private final String QUERY_FIND_USERS_BY_DOMAIN = "MATCH (user:User) "
			+ "MATCH (user)<-[:MANAGED_BY]-(domain{id:$domainId}) RETURN user ";
	private final String QUERY_ALL_DOMAINS = "MATCH (domain:Domain) RETURN domain";
			
	
	public void createDomain (final Domain domain) {
		final Map<String,Object> parameters = new HashMap<>();
    	final Map<?,?> patch =  MAPPER.convertValue(domain, Map.class);    	
    	parameters.put("id",domain.getId());
    	parameters.put("patch", patch);    	
    	this.writeQuery(QUERY_BUILD_DOMAIN, parameters);
	}
	
	public Domain findDomain (final String id) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("id",id);    		
    	return this.readSingleQuery(QUERY_FIND_DOMAIN_BY_ID, parameters,"domain",Domain.class);
	}
	
	public List<Domain> findManagedDomains (final String userid) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("userid",userid);    		
    	return this.readMultipleQuery(QUERY_FIND_DOMAIN_BY_MANAGER, parameters,"domain",Domain.class);
	}
	
	public List<Domain> findAllDomains () {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	 		
    	return this.readMultipleQuery(QUERY_ALL_DOMAINS, parameters,"domain",Domain.class);
	}
	
	public List<User> findDomainUsers (String domainId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("domainId", domainId);
    	return this.readMultipleQuery(QUERY_FIND_USERS_BY_DOMAIN, parameters,"user",User.class);
	}
	
	
}
