package fr.srosoft.wineyard.core.configuration;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WineyardConfiguration {
	
	@Value( "${data.neo4j.uri}" )
	private String neo4jUri;
	
	@Value( "${data.neo4j.username}" )
	private String neo4jUsername;
	
	@Value( "${data.neo4j.password}" )
	private String neo4jPassword;
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Driver neo4jDriver() {
		
		
		final Driver driver = GraphDatabase.driver(neo4jUri, AuthTokens.basic(neo4jUsername, neo4jPassword) );
		return driver;
	}
	
	public static void main (String [] args) {
		int tableau[] = {0,6,2,4,1,5,3,7};
		tableau = sort (tableau);
		for (int i : tableau) {
			System.out.println(i);
		}
		
	}
	
	public static int[] sort(int tableau[]) {
		
		//for (int indexUp = tableau.length -1 ; indexUp >= 0 ;indexUp--) {
		for (int indexUp = 0 ; indexUp <= tableau.length -1 ; indexUp++) {
			for (int indexLow = 0 ; indexLow <= tableau.length -1 ; indexLow++) {
				int val1 = tableau[indexUp];
				int val2 = tableau[indexLow];
				System.out.println(indexUp+" "+indexLow);
				if (val2 > val1) {
					tableau[indexUp] = val2;
					tableau[indexLow] = val1;
				}
			}
			
		}
		
		return tableau;
	}
}
