package fr.srosoft.wineyard.core.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Millesime;

@Service
public class CuveeDao extends AbstractDao{
	private final String QUERY_APPELLATIONS = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "MATCH (module)-[:PROPOSES]->(appellation:Appellation)"
			+ "OPTIONAL MATCH (appellation)-[:HAS_BLEND]->(blend) "
			+ "OPTIONAL MATCH (blend)<-[:COMPOSES]-(grape) "
			+ "WITH appellation, CASE WHEN blend IS NULL THEN NULL ELSE {id:blend.id, proportion:blend.proportion, grape:grape{.*}} END AS blend "
			+ "WITH appellation, COLLECT (blend) AS blends "
			+ "RETURN  appellation{.*, blends:blends} AS appellation ORDER BY appellation.appellation ASC";
	
	private final String QUERY_FIND_APPELLATION_BY_ID = "MATCH (appellation:Appellation{id:$id}) "
			+ "OPTIONAL MATCH (appellation)-[:HAS_BLEND]->(blend) "
			+ "OPTIONAL MATCH (blend)<-[:COMPOSES]-(grape:Grape) "
			+ "WITH appellation, CASE WHEN blend IS NULL THEN NULL ELSE {id:blend.id, proportion:blend.proportion, grape:grape{.*}} END AS blend "
			+ "WITH appellation, COLLECT (blend) AS blends "
			+ "RETURN appellation{.*, blends:blends} AS appellation";
	
	private final String QUERY_COUNT_APPELLATION_BY_APPELLATION = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "(module)-[:PROPOSES]->(appellation:Appellation{appellation:$appellation})"
			+ "RETURN COUNT(appellation) AS count";
	
	private final String QUERY_SAVE_APPELLATION = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "MERGE (appellation:Appellation{id:$id}) "
			+ "SET appellation+=$patch "
			+ "MERGE (module)-[:PROPOSES]->(appellation) "
			+ "WITH appellation "
			+ "UNWIND $blends AS pblend "
			+ "MERGE (blend:Blend{id:pblend.id}) SET blend.proportion=pblend.proportion "
			+ "MERGE (appellation)-[:HAS_BLEND]->(blend) "
			+ "WITH blend, pblend "
			+ "MATCH (grape:Grape{id:pblend.grape.id}) "
			+ "MERGE (blend)<-[:COMPOSES]-(grape) ";
	
	private final String QUERY_APPELLATION_DELETE_BLENDS = "MATCH (appellation:Appellation{id:$id}) "
			+ "MERGE (appellation)-[:HAS_BLEND]->(blend) "
			+ "DETACH DELETE blend";
	
	private final String QUERY_SAVE_ALL_CUVEES = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "UNWIND $cuvees AS pcuvee "
			+ "MERGE (cuvee:Cuvee{id:pcuvee.id}) "
			+ "MERGE (module)-[:DEFINES]->(cuvee) "
			+ "SET cuvee+=pcuvee {.creationDate, .creationUser, .lastUpdateDate, .lastUpdateDateUser, .name, .label, .description,.title,  .processId, .domainId } "
			+ "MERGE (millesime:Millesime{id:pcuvee.millesime.id}) "
			+ "   ON CREATE SET millesime.year = pcuvee.millesime.year "
			+ "MERGE (module)-[:WORKS_ON]->(millesime) "
			+ "MERGE (millesime)-[:STAMPS]->(cuvee) "			
			+ "WITH cuvee, pcuvee "
			+ "MATCH (a:Appellation{id:pcuvee.appellation.id}) "
			+ "MERGE (a)-[:MARKS]->(cuvee) ";
	
	private final String QUERY_FIND_CUVEES_BY_DOMAIN =  "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "MATCH (module)-[:WORKS_ON]->(millesime:Millesime)"
			+ "MATCH (millesime)-[:STAMPS]->(cuvee:Cuvee) "
			+ "MATCH (appellation:Appellation)-[:MARKS]->(cuvee) "			
			+ "RETURN cuvee{.*, millesime:millesime{.*}, appellation:appellation{.*}} AS cuvee ";
	
	private final String QUERY_FIND_CUVEES_BY_DOMAIN_AND_MILLESIME =  "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "MATCH (module)-[:WORKS_ON]->(millesime:Millesime{year:$year}) "
			+ "MATCH (cuvee:Cuvee)<-[:STAMPS]-(millesime) "
			+ "MATCH (appellation:Appellation)-[:MARKS]->(cuvee) "
			+ "OPTIONAL MATCH (appellation)-[:HAS_BLEND]->(blend) "
			+ "OPTIONAL MATCH (blend)<-[:COMPOSES]-(grape:Grape) "
			+ "WITH millesime, cuvee, appellation, CASE WHEN blend IS NULL THEN NULL ELSE {id:blend.id, proportion:blend.proportion, grape:grape{.*}} END AS blend "
			+ "WITH millesime, cuvee, appellation, COLLECT (blend) AS blends "
			+ "RETURN cuvee{.*, millesime:millesime{.*}, appellation:appellation{.*, blends:blends}} AS cuvee  ORDER BY appellation.appellation ASC ";
	
	private final String QUERY_FIND_CUVEE_BY_ID =  "MATCH (cuvee:Cuvee{id:$id})<-[:STAMPS]-(millesime) "
			+ "MATCH (appellation:Appellation)-[:MARKS]->(cuvee) "
			+ "OPTIONAL MATCH (appellation)-[:HAS_BLEND]->(blend) "
			+ "OPTIONAL MATCH (blend)<-[:COMPOSES]-(grape:Grape) "
			+ "WITH millesime, cuvee, appellation, CASE WHEN blend IS NULL THEN NULL ELSE {id:blend.id, proportion:blend.proportion, grape:grape{.*}} END AS blend "
			+ "WITH millesime, cuvee, appellation, COLLECT (blend) AS blends "
			+ "RETURN cuvee{.*, millesime:millesime{.*}, appellation:appellation{.*, blends:blends}} AS cuvee  ORDER BY appellation.appellation ASC ";
	
	private final String QUERY_FIND_MILLESIMES_BY_DOMAIN =  "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (domain)-[:HAS_MODULE]->(module:CuveeModule) "
			+ "MATCH (module)-[:WORKS_ON]->(millesime:Millesime) "			
			+ "RETURN millesime{.*} AS millesime ";
	
	// *** Appellations
	@SuppressWarnings("unchecked")
	public void saveAppellation(Appellation appellation, String domainId) {
		final Map<String,Object> parameters = new HashMap<>();
		
		final Map<?,?> patch =  MAPPER.convertValue(appellation, Map.class);
		patch.remove("id");
		final List<Map<String,Object>> blends = (List<Map<String,Object>> )patch.remove("blends");
		parameters.put("id", appellation.getId());
		parameters.put("domainId", domainId);
		parameters.put("blends", blends);
		parameters.put("patch", patch);
		
		this.writeQuery(QUERY_SAVE_APPELLATION, parameters, QUERY_APPELLATION_DELETE_BLENDS, parameters);
		
		
	}
	
	public List<Appellation> findAppelationsForDomain (final String domainId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("domainId",domainId);    		
    	return this.readMultipleQuery(QUERY_APPELLATIONS, parameters,"appellation",Appellation.class);
	}
	
	public Appellation findAppellation (final String id) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("id",id);    		
    	return this.readSingleQuery(QUERY_FIND_APPELLATION_BY_ID, parameters,"appellation",Appellation.class);
	}
	
	public int countAppellations (String appellation, String domainId) {
		final Long result = this.readSingleQuery(QUERY_COUNT_APPELLATION_BY_APPELLATION, Map.of("domainId",  domainId,"appellation",appellation),"count", Long.class);
		return result.intValue();
		
	}
	
	// *** Cuvees and millesimes
	@SuppressWarnings("unchecked")
	public void saveAllCuvees (List<Cuvee> cuvees, String domainId) {
		
		final List<Map<?,?>> pcuvees =  MAPPER.convertValue(cuvees, List.class);
		this.writeQuery(QUERY_SAVE_ALL_CUVEES, Map.of("domainId",  domainId,"cuvees",pcuvees));
	}
	
	public List<Cuvee> findCuveesByDomain(String domainId){
		return this.readMultipleQuery(QUERY_FIND_CUVEES_BY_DOMAIN, Map.of("domainId", domainId),"cuvee",Cuvee.class);
	}
	
	public List<Cuvee> findCuveesByDomainAndMillesime(String domainId, int year){
		return this.readMultipleQuery(QUERY_FIND_CUVEES_BY_DOMAIN_AND_MILLESIME, Map.of("domainId", domainId,"year", year),"cuvee",Cuvee.class);
	}
	
	public Cuvee findCuveeById(String cuveeId){
		return this.readSingleQuery(QUERY_FIND_CUVEE_BY_ID, Map.of("id", cuveeId),"cuvee",Cuvee.class);
	}
	
	
	private final String QUERY_FIND_STOCKS_BY_CUVEE =  "MATCH (cuvee:Cuvee{id:$id}) "
			+ "MATCH (cuvee)<-[:ATTACHED_TO]-(contents:Contents) "
			+ "RETURN sum (contents.volume)  AS sum ";
	public Long getStocksFromContainers(String cuveeId){
		return this.readSingleQuery(QUERY_FIND_STOCKS_BY_CUVEE, Map.of("id",cuveeId), "sum", Long.class);
	}
	
	public List<Millesime> findMillesimesByDomain(String domainId){
		return this.readMultipleQuery(QUERY_FIND_MILLESIMES_BY_DOMAIN, Map.of("domainId", domainId),"millesime",Millesime.class);
	}
	
	
}
