package fr.srosoft.wineyard.core.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.types.Node;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractDao {

	protected static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Resource
	protected Driver neo4jDriver;
	
	protected void writeQuery(final String query,final Map<String,Object> parameters) {
		try ( Session session = neo4jDriver.session() )
        {
            session.writeTransaction( new TransactionWork<Void>()
            {
                @Override
                public Void execute( Transaction tx )
                {	
                	tx.run(query, parameters);
                	
                	return null;
                }
            } );
           
        }
	}
	
	protected void writeQuery(final String query,final Map<String,Object> parameters,final String preQuery,final Map<String,Object> preParameters) {
		try ( Session session = neo4jDriver.session() )
        {
            session.writeTransaction( new TransactionWork<Void>()
            {
                @Override
                public Void execute( Transaction tx )
                {	
                	tx.run(preQuery, preParameters);
                	tx.run(query, parameters);
                	
                	return null;
                }
            } );
           
        }
	}
	
	protected <T> T readSingleQuery(final String query,final Map<String,Object> parameters, String columnName, Class<T> clazz) {
		try ( Session session = neo4jDriver.session() )
        {
          return  session.readTransaction( new TransactionWork<T>()
            {
                @Override
                public T execute( Transaction tx )
                {
                	        	
                	final Result result = tx.run(query, parameters);
                	Object map = result.single().get(columnName).asObject();
                	if (map instanceof Node) {
                		final Node node = (Node) map;
                		map = node.asMap();
                		return MAPPER.convertValue(map,clazz);        
                	}
                	else return MAPPER.convertValue(map,clazz);
                	        	
                }
            } );
           
        }
	}
	protected <T> List<T> readMultipleQuery(final String query,final Map<String,Object> parameters, String columnName, Class<T> clazz) {
		final List<T> results = new ArrayList<>();
		try ( Session session = neo4jDriver.session() )
        {
			session.readTransaction( new TransactionWork<Void>()
            {
                @Override
                public Void execute( Transaction tx )
                {
                	        	
                	final Result result = tx.run(query, parameters);
                	result.forEachRemaining(e -> {
                		Object map =e.get(columnName).asObject();
                    	if (map instanceof Node) {
                    		final Node node = (Node) map;
                    		map = node.asMap();
                    		results.add(MAPPER.convertValue(map,clazz));
                    	}
                    	else results.add(MAPPER.convertValue(map,clazz));
                	});  
                	return null;
                }
            } );
        }
		return results;
	}
	
	public boolean exists (String label, String id) {
		try ( Session session = neo4jDriver.session() )
        {
          return  session.readTransaction( new TransactionWork<Boolean>()
            {                
				@Override
                public Boolean execute( Transaction tx )
                {
                	     	
                	final Result result = tx.run("OPTIONAL MATCH (o:"+label+"{id:$id}) RETURN o IS NOT NULL as exists", Map.of("id", id));
                	return result.single().get("exists").asBoolean();                	
                	        	
                }
            } );
           
        }
	}
	
	
}
