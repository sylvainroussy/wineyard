package fr.srosoft.wineyard.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestImportDataFull {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private Driver driver;
	
	private void loadDriver () {
		driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "root" ) );
	}
	
	public void renameFiles() throws FileNotFoundException,IOException{
		
		//this.loadDriver();
		final String dataFolder ="C:\\z_data\\wineyard\\parcels";
		
		final File f = new File (dataFolder);
		for (File file : f.listFiles()) {
			System.out.println("Starting "+file.getName());
			String fileName = file.getName().replaceAll(" ", "_");
			System.out.println(file.getParent()+"\\"+fileName);
			File fs = new File (file.getParent()+"\\"+fileName);
			System.out.println(file.renameTo(fs));
			//Path path = file.toPath();
			//File
			System.out.println("Ended "+fs.getName());
		}
		//driver.close();
	}
	
	public void testImport() throws FileNotFoundException,IOException{
		
		this.loadDriver();
		final String dataFolder ="C:\\z_data\\wineyard\\parcels\\";
		
		final File f = new File (dataFolder);
		int i = 0;
		for (File file : f.listFiles()) {
			System.out.println(i+") Starting "+file.getName());
			
			
			this.loadWriteParcel(file.getName());
			System.out.println(i+") Ended "+file.getName());
			i++;
		}
		driver.close();
	}
	
	private void loadWriteParcel (String fileName) {
		 try ( Session session = driver.session() )
	        {
	           session.writeTransaction( new TransactionWork<Void>()
	            {
	                @Override
	                public Void execute( Transaction tx )
	                {
	                	String query ="CALL apoc.load.json($file) YIELD value " + 
	                			"WITH value as feature " + 
	                			"MATCH (geo:GIS:GEOMETRY {id:\"geo_\"+feature.properties.id}) " + 
	                			"WITH  geo, feature, feature.geometry.coordinates AS level1 " + 
	                			" " + 
	                			"FOREACH(l1n IN range (0, size(level1)-1) | " + 
	                			"	MERGE (l1:GIS:LEVEL1 {id:feature.properties.id+\"_\"+l1n, level:1}) " + 
	                			"	MERGE (geo)-[:HAS_LEVEL1]->(l1) " + 
	                			" " + 
	                			"	FOREACH(l2n IN range (0, size(level1[l1n])-1) | " + 
	                			"		MERGE (l2:GIS:LEVEL2 {id:feature.properties.id+\"_\"+l1n+\"_\"+l2n, level:2}) " + 
	                			"		MERGE (l1)-[:HAS_LEVEL2]->(l2) " + 
	                			" " + 
	                			"		FOREACH(l3n IN range (0, size(level1[l1n][l2n])-1) |	 " + 
	                			" " + 
	                			"				MERGE (l3:GIS:LEVEL3 {id:feature.properties.id+\"_\"+l1n+\"_\"+l2n+\"_\"+l3n, level:3}) " + 
	                			"					SET l3.coords=level1[l1n][l2n][l3n], l3.l1=l1n, l3.l2=l2n, l3.l3=l3n " + 
	                			"				MERGE (l2)-[:HAS_LEVEL3]->(l3) " + 
	                			" " + 
	                			")))";
	                	
	                	final Map<String,Object> parameters = new HashMap<>();
	                	parameters.put( "file", fileName);
	                	tx.run(query, parameters);
	                   
	                   return null;
	                }
	            } );
	           
	        }
	}
	
	public static void main (String [] args) {
		try {
			TestImportDataFull id = new TestImportDataFull();
			//id.renameFiles();
			id.testImport();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
