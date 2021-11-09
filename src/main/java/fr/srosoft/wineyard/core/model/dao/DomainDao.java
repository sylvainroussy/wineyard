package fr.srosoft.wineyard.core.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.beans.UserDomain;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.User;

@Service
public class DomainDao extends AbstractDao {

	private final String QUERY_BUILD_DOMAIN = "MERGE (domain:Domain{id:apoc.util.md5([$id])}) SET domain+=$patch "
			+ "WITH domain "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(cave:Module:CaveModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(d:Module:DomainModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(cuvee:Module:CuveeModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(gis:Module:GisModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(directory:Module:DirectoryModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(harvest:Module:HarvestModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(vineyard:Module:VineyardModule{id:domain.id}) "
			+ "MERGE (domain)-[:HAS_MODULE{enabled:true}]->(admin:Module:AdminModule{id:domain.id}) "
			+ "UNWIND $appellations AS appellation "
			+ "MERGE (app:Appellation{id:domain.id+':'+apoc.util.md5([appellation.id])})  "
			+ "SET app.appellation = appellation.appellation "
			+ "MERGE (cuvee)-[:PROPOSES]->(app) ";
	
	// One level depth
	private final String QUERY_SAVE_DOMAIN = "MERGE (domain:Domain{id:$id}) SET domain+=$patch ";
	
	private final String QUERY_FIND_DOMAIN_BY_ID = "MATCH (domain:Domain{id:$id}) RETURN domain ";
	private final String QUERY_FIND_DOMAIN_BY_MANAGER = "MATCH (user:User{id:$userid}) "
			+ "MATCH (user)-[:HAS_CONTEXT]->(l:LinkedDomain)-[:LINKED_TO]->(domain:Domain) RETURN domain ";
	
	private final String QUERY_FIND_USER_AND_DOMAINS = "MATCH (user:User{id:$userid}) "
			+ "MATCH (user)-[:HAS_CONTEXT]->(l:LinkedDomain)-[:LINKED_TO]->(domain) RETURN user {.*, domains:COLLECT(domain{.*})} AS user ";
	
	private final String QUERY_FIND_USERS_AND_DOMAINS = "MATCH (user:User) "
			+ "OPTIONAL MATCH (user)-[:HAS_CONTEXT]->(context:LinkedDomain)-[:LINKED_TO]->(domain) "
			+ "RETURN user {.*, linkedDomains : COLLECT({domain: domain {.*}, office:context.office})} AS user ";
	
	private final String QUERY_FIND_USERS_BY_DOMAIN =  "MATCH (user:User)-[:HAS_CONTEXT]->(l:LinkedDomain)-[:LINKED_TO]->(domain:Domain{id:$domainId}) RETURN user ";
	
	private final String QUERY_FIND_USERS_BY_DOMAIN2 = "MATCH (user:User)-[:HAS_CONTEXT]->(l:LinkedDomain)-[:LINKED_TO]->(domain:Domain{id:$domainId}) RETURN {user: user {.*}, office:l.office} AS user";
	
	private final String QUERY_ALL_DOMAINS = "MATCH (domain:Domain) RETURN domain";
	
	private final String QUERY_SAVE_USER = "MERGE (u:User{id:apoc.util.md5([$email])}) SET u+=$patch, u.password=apoc.util.md5([$password]) "
			+ "WITH u "
			+ "OPTIONAL MATCH (u)-[:HAS_CONTEXT]->(oldContext)-[:LINKED_TO]->(d:Domain) "
			+ "WITH u, COLLECT (oldContext) AS oldContexts "
			+ "FOREACH (old IN oldContexts | DETACH DELETE old) "
			+ "WITH u,$linkedDomains AS linkedDomains "
			+ "UNWIND linkedDomains AS linkedDomain "
			+ "MATCH (d:Domain{id:linkedDomain.domain.id}) "
			+ "MERGE (d)<-[r:LINKED_TO]-(l:LinkedDomain{id:u.id+':'+linkedDomain.domain.id}) "
			+ "SET l.office = linkedDomain.office "
			+ "MERGE (u)-[:HAS_CONTEXT]->(l) ";
	
	
			
	
	public void createDomain (final Domain domain) {
		final Map<String,Object> parameters = new HashMap<>();
    	final Map<?,?> patch =  MAPPER.convertValue(domain, Map.class);   
    	patch.values().removeAll(Collections.singleton(null));
    	parameters.put("id",domain.getId());
    	patch.remove("id");
    	parameters.put("patch", patch);  
    	
    	Object apps = patch.remove("appellations");
    	parameters.put("appellations", apps);
    	this.writeQuery(QUERY_BUILD_DOMAIN, parameters);
	}
	
	public void saveDomain (final Domain domain) {
		final Map<String,Object> parameters = new HashMap<>();
    	final Map<?,?> patch =  MAPPER.convertValue(domain, Map.class);   
    	patch.values().removeAll(Collections.singleton(null));
    	parameters.put("id",domain.getId());
    	patch.remove("id");
    	patch.remove("appellations");
    	parameters.put("patch", patch);
    	this.writeQuery(QUERY_SAVE_DOMAIN, parameters);
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
	
	public List<UserDomain> findDomainUsers2 (String domainId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("domainId", domainId);
    	return this.readMultipleQuery(QUERY_FIND_USERS_BY_DOMAIN2, parameters,"user",UserDomain.class);
	}
	
	public User findUserAndDomains (final String userid) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("userid",userid);    		
    	return this.readSingleQuery(QUERY_FIND_USER_AND_DOMAINS, parameters,"user",User.class);
	}
	
	public List<User> findAllUsersAndDomains () {
		final Map<String,Object> parameters = new HashMap<>(); 
    	return this.readMultipleQuery(QUERY_FIND_USERS_AND_DOMAINS, parameters,"user",User.class);
	}
	
	public void createUser (final User user, final String password) {
		final Map<String,Object> parameters = new HashMap<>();
    	final Map<?,?> patch =  MAPPER.convertValue(user, Map.class);
    	patch.remove("id");
    	parameters.put("patch",patch);
    	parameters.put("linkedDomains",patch.get("linkedDomains"));
    	patch.remove("linkedDomains");
    	parameters.put("email", user.getEmail());
    	parameters.put("password", password);
    	this.writeQuery(QUERY_SAVE_USER, parameters);
	}
	
	
	
	
}
