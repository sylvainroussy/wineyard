package fr.srosoft.wineyard.core.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.entities.City;
import fr.srosoft.wineyard.core.model.entities.Parcel;

@Service
public class FeatureDao extends AbstractDao{
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/*
	 * MATCH (g:PROPS) 
WITH  DISTINCT g.new_nomcom AS ville , g.new_insee AS insee ORDER BY insee SKIP 450 LIMIT 10
CALL apoc.spatial.geocode(ville+' '+insee+' FRANCE')
YIELD location
WITH HEAD(COLLECT(location)) AS location, ville,insee 
MATCH (g:PROPS{new_insee:insee})
SET g+=location
RETURN ville
	 */
	
	/*
	 MATCH (domain:Domain{id:$domainId})  
MATCH (l1:LEVEL1{id:$level1Id})
MATCH (geo:GEOMETRY)-->(l1) 
MATCH (p:PROPS)-->(geo) 
MATCH (l1)-->(l2:LEVEL2) 
MATCH (l2)-->(l3:LEVEL3) 
WITH p, l1.id AS parcelid, geo, l1,l2,l3 ORDER BY l3.l1,l3.l2,l3.l3 ASC 
WITH p, parcelid, geo, l1,l2, COLLECT (l3.coords) as cl3 
WITH p, parcelid, geo, l1, COLLECT(cl3) AS cl3 
WITH p, parcelid,geo, COLLECT(cl3) AS cl3 
WITH {type:"Feature",geometry:{type:geo.type, coordinates:cl3}, properties:p{.*, wineyardId:parcelid}} AS geoNode 
RETURN geoNode
	 */
	
	private static final String PART_GEO_NODE ="MATCH (p)-->(geo:GEOMETRY) " + 
			"MATCH (geo)-->(l1:LEVEL1) " + 
			"MATCH (l1)-->(l2:LEVEL2) " + 
			"MATCH (l2)-->(l3:LEVEL3) " + 
			"WITH p, geo, l1,l2,l3 ORDER BY l3.l1,l3.l2,l3.l3 ASC " + 			 
			"WITH p, geo, l1,l2, COLLECT (l3.coords) as cl3 " + 
			"WITH p, geo, l1, COLLECT(cl3) AS cl3 " + 
			"WITH p, geo, COLLECT(cl3) AS cl3 " + 
			"WITH {type:\"Feature\",geometry:{type:geo.type, coordinates:cl3}, properties:p{.*}} AS geoNode " + 
			"RETURN geoNode";
	
	private static final String PART_PARCEL_NODE =
			"MATCH (parcel)-[:HAS_GEOMETRY]->(geo:GEOMETRY)-->(l1:LEVEL1) " + 			 
			"MATCH (l1)-->(l2:LEVEL2) " + 
			"MATCH (l2)-->(l3:LEVEL3) " + 
			"WITH parcel,  geo, l1,l2,l3 ORDER BY l3.l1,l3.l2,l3.l3 ASC " + 			 
			"WITH parcel,  geo, l1,l2, COLLECT (l3.coords) as cl3 " + 
			"WITH parcel,  geo, l1, COLLECT(cl3) AS cl3 " + 
			"WITH parcel,geo, COLLECT(cl3) AS cl3 " + 
			"WITH {type:\"Feature\",geometry:{type:geo.type, coordinates:cl3}, properties:parcel{.*}} AS geoNode " + 
			"RETURN geoNode";
	private static final String QUERY_PART_GEO_NODE_FOR_DOMAIN ="MATCH (domain:Domain{id:$domainId}) " +
			"MATCH (domain)-[:HAS_PARCEL]->(parcel:Parcel) " +PART_PARCEL_NODE;	
	
	
	private static final String QUERY_ALL_CRINAO ="MATCH (props:PROPS) RETURN DISTINCT props.crinao AS crinao, COLLECT(DISTINCT props.appellation) AS appellations ORDER BY props.crinao";
	
	private static final String QUERY_ALL ="MATCH (props:PROPS) " + 
			"WITH  props.crinao AS crinao, props.appellation AS appellation,props.denomination AS denomination  ORDER BY props.crinao,props.appelation,props.denomination " + 
			"WITH crinao, appellation, collect (DISTINCT denomination) AS denominations " + 
			"WITH crinao, appellation AS appellations, denominations " + 
			"RETURN DISTINCT crinao, appellations, denominations";

	private static final String QUERY_DENOMINATION ="MATCH (p:PROPS{denomination:$denomination}) "+PART_GEO_NODE;
	private static final String QUERY_INSEE ="MATCH (p:PROPS{new_insee:$insee,denomination:$denomination}) "+PART_GEO_NODE;
	
		
	private static final String QUERY_CITIES ="MATCH (g:PROPS) "
			+ "WITH DISTINCT g.new_nomcom as ville,g.new_insee AS insee,g.denomination AS denomination ORDER BY g.new_nomcom,g.denomination "
			+ "RETURN ville, insee, COLLECT (denomination) AS denominations";
	
	
	private static final String QUERY_LINK_FEATURE_TO_DOMAIN = "MATCH (domain:Domain{id:$domainId})  " 
			+ "MATCH (l1:LEVEL1{id:$level1Id}) " 
			+ "MATCH (geo:GEOMETRY)-->(l1) " 
			+ "MATCH (p:PROPS)-->(geo) " 
			+ "MATCH path=(geo)-->(l1)-[*]->(:LEVEL3) "
			+ "WITH domain, geo,p, collect(path) as paths " 
			+ "MERGE (parcel:Parcel{wineyardId:domain.id+':'+apoc.create.uuid()}) "
			+ "SET parcel += p "
			+ "MERGE (domain)-[:HAS_PARCEL]->(parcel) "
			+ "MERGE (geo2:GEOMETRY {wineyardId:domain.id+':'+apoc.create.uuid()}) "
			+ "SET geo2 += geo "
			+" MERGE (parcel)-[:HAS_GEOMETRY]->(geo2) "
			+ "WITH domain, geo,geo2, parcel,  paths " 
			+ "CALL apoc.refactor.cloneSubgraphFromPaths(paths, { " 
			+"    standinNodes:[[geo,geo2]] " 
			+ "}) YIELD input, output, error "
			+ "RETURN error ";
	
	public Map<String,List<String>> getAllCrinao (){
		 try ( Session session = neo4jDriver.session() )
	        {
	          return  session.readTransaction( new TransactionWork<Map<String,List<String>>>()
	            {
	                @Override
	                public Map<String,List<String>> execute( Transaction tx )
	                {
	                	final Map<String,List<String>> crinaos = new HashMap<>();
	                	final Result result = tx.run(QUERY_ALL_CRINAO, new HashMap<>());
	                	result.forEachRemaining(r -> crinaos.put(r.get("crinao").asString(),r.get("appellations").asList(e -> e.toString())));
	                	return crinaos;
	                }
	            } );
	           
	        }
	}
	
	public Map<String,List<Map<String,List<String>>>> getAll (){
		 try ( Session session = neo4jDriver.session() )
	        {
	          return  session.readTransaction( new TransactionWork<Map<String,List<Map<String,List<String>>>>>()
	            {
	                @Override
	                public Map<String,List<Map<String,List<String>>>> execute( Transaction tx )
	                {
	                	final Map<String,List<Map<String,List<String>>>> crinaos = new HashMap<>();
	                	final Result result = tx.run(QUERY_ALL, new HashMap<>());
	                	
	                	result.forEachRemaining(new DenominationsConsumer(crinaos));
	                	
	                	return crinaos;
	                }
	            } );
	           
	        }
	}
	
	public List<City> getAllCities (){
		 try ( Session session = neo4jDriver.session() )
	        {
	          return  session.readTransaction( new TransactionWork<List<City>>()
	            {
	                @Override
	                public List<City> execute( Transaction tx )
	                {
	                	final List<City> cities = new ArrayList<>();
	                	final Result result = tx.run(QUERY_CITIES, new HashMap<>());
	                	
	                	result.forEachRemaining(record -> cities.add(new City(record.get("ville").asString(), record.get("insee").asString(), record.get("denominations").asList(e -> e.asString()))));
	                	
	                	return cities;
	                }
	            } );
	           
	        }
	}
	
	public List<Map<String,?>> getDenomination (final String denomination){
		 try ( Session session = neo4jDriver.session() )
	        {
	          return  session.readTransaction( new TransactionWork<List<Map<String,?>>>()
	            {
	                @Override
	                public List<Map<String,?>> execute( Transaction tx )
	                {
	                	List<Map<String,?>> crinaos = new ArrayList<>();
	                	final Map<String,Object> parameters = new HashMap<>();
	                	parameters.put("denomination", denomination);
	                	final Result result = tx.run(QUERY_DENOMINATION, parameters);
	                	
	                	while (result.hasNext()) {
	                		crinaos.add(result.next().get("geoNode").asMap());
	                	}
	                	
	                	return crinaos;
	                }
	            } );
	           
	        }
	}
	
	public List<Map<String,?>> getCity (final String insee, final String denomination){
		 try ( Session session = neo4jDriver.session() )
	        {
	          return  session.readTransaction( new TransactionWork<List<Map<String,?>>>()
	            {
	                @Override
	                public List<Map<String,?>> execute( Transaction tx )
	                {
	                	List<Map<String,?>> crinaos = new ArrayList<>();
	                	final Map<String,Object> parameters = new HashMap<>();
	                	parameters.put("insee", insee);
	                	parameters.put("denomination", denomination);
	                	final Result result = tx.run(QUERY_INSEE, parameters);
	                	
	                	while (result.hasNext()) {
	                		crinaos.add(result.next().get("geoNode").asMap());
	                	}
	                	
	                	return crinaos;
	                }
	            } );
	           
	        }
	}
	
	public void linkFeatureToDomain (String level1Id, String domainId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("domainId",domainId);
    	parameters.put("level1Id", level1Id);    	
    	this.writeQuery(QUERY_LINK_FEATURE_TO_DOMAIN, parameters);
	}
	
	public List<Parcel> findParcels (final String domainId) {
		
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("domainId",domainId);
    	return this.readMultipleQuery(QUERY_PART_GEO_NODE_FOR_DOMAIN, parameters,"geoNode",Map.class)
    		.stream().map (e -> {
    			Parcel parcel = new Parcel();
    			Map<String,Object> props = (Map<String,Object>) e.get("properties");
    			parcel = MAPPER.convertValue(props, Parcel.class);
    			parcel.setGeometry(e);
    			return parcel;
    		}).collect(Collectors.toList());
    	
    	
	}
	
	
	
	public static class DenominationsConsumer implements Consumer<Record>{

		final Map<String,List<Map<String,List<String>>>> crinaos ;
		String tempApp = null;
		
		
		public DenominationsConsumer ( Map<String,List<Map<String,List<String>>>> crinaos) {
			this.crinaos = crinaos;
		}
		
		@Override
		public void accept(Record r) {
			final Map<String,List<String>> denominations = new HashMap<>();
    		if (tempApp == null || !tempApp.equals(r.get("appellations").asString())) {
    			
    			tempApp = r.get("appellations").asString();
    		}
    		
    		denominations.put(tempApp, r.get("denominations").asList(e -> e.asString()));
    		
    		List<Map<String,List<String>>> apps = crinaos.get(r.get("crinao").asString());
    		if (apps != null) {
    			apps.add(denominations);
    		}
    		else apps = new ArrayList<>();
    		crinaos.put(r.get("crinao").asString(),apps);
			
		}
		
	}
	
	
	
	
}
