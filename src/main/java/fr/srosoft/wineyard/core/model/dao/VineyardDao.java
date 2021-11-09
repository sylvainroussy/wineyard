package fr.srosoft.wineyard.core.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.Grape;
import fr.srosoft.wineyard.core.model.entities.Parcel;

@Service
public class VineyardDao  extends AbstractDao{
	
	
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

	// *** Grapes
	private static String QUERY_FIND_ALL_GRAPES="MATCH (grape:Grape) RETURN grape ";
	@SuppressWarnings("unchecked")
	public List<Grape> findAllGrapes(){	
		
		return super.readMultipleQuery(QUERY_FIND_ALL_GRAPES, Collections.EMPTY_MAP, "grape", Grape.class);
	}
	
	// *** Parcels
	private static final String QUERY_ADD_PARCEL_TO_DOMAIN = "MATCH (domain:Domain{id:$domainId})  " 
			+ "MATCH (module:VineyardModule)<-[:HAS_MODULE]-(domain) "
			+ "MATCH (l1:LEVEL1{id:$level1Id}) " 
			+ "MATCH (geo:GEOMETRY)-->(l1) " 
			+ "MATCH (p:PROPS)-->(geo) " 
			+ "MATCH path=(geo)-->(l1)-[*]->(:LEVEL3) "
			+ "WITH domain,module, geo,p, collect(path) as paths " 
			+ "MERGE (parcel:Parcel{wineyardId:domain.id+':'+apoc.create.uuid()}) "
			+ "SET parcel += p "
			+ "MERGE (module)-[:HAS_PARCEL]->(parcel) "
			+ "MERGE (geo2:GEOMETRY {wineyardId:domain.id+':'+apoc.create.uuid()}) "
			+ "SET geo2 += geo "
			+" MERGE (parcel)-[:HAS_GEOMETRY]->(geo2) "
			+ "WITH domain, geo,geo2, parcel,  paths " 
			+ "CALL apoc.refactor.cloneSubgraphFromPaths(paths, { " 
			+"    standinNodes:[[geo,geo2]] " 
			+ "}) YIELD input, output, error "
			+ "RETURN error ";
	
	public void addParcel (String level1Id, String domainId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("domainId",domainId);
    	parameters.put("level1Id", level1Id);    	
    	this.writeQuery(QUERY_ADD_PARCEL_TO_DOMAIN, parameters);
	}
	
	
	private static final String QUERY_PART_GEO_NODE_FOR_DOMAIN ="MATCH (domain:Domain{id:$domainId}) " 
			+ "MATCH (module:VineyardModule)<-[:HAS_MODULE]-(domain) "
			+ "MATCH (module)-[:HAS_PARCEL]->(parcel:Parcel) " +PART_PARCEL_NODE;	
	
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
	
}
