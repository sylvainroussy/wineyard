package fr.srosoft.wineyard.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.entities.Domain;

public class TestImportViti {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private Driver driver;
	
	private void loadDriver () {
		driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "root" ) );
	}
	
	public void testImport() throws IOException{
		
		this.loadDriver();
		final String startUrl = "https://www.vins-bourgogne.fr/nos-savoir-faire/femmes-et-hommes-passionnes/lafouge-joseph-et-fils-change-21340,2397,9211.html?&args=Y29tcF9pZD0xNDA2JmFjdGlvbj12aWV3RmljaGUmaWQ9VklOQk9VMDAwMDIwMDcwMSZ8";
		final Domain info = this.buildViti(startUrl);
		
		final Map<?,?> patch = MAPPER.convertValue(info, Map.class);
		
		try ( Session session = driver.session() )
        {
           session.writeTransaction( new TransactionWork<Void>()
            {
                @Override
                public Void execute( Transaction tx )
                {
                	final String id = info.getDomainName().toLowerCase().replaceAll(" ", "_")+":"+info.getZipCode();
                	final String query = "MERGE (domain:Domain{id:$id}) SET domain+=$patch ";
                	final Map<String,Object> parameters = new HashMap<>();
                	parameters.put( "patch", patch);
                	parameters.put( "id", id);
                	tx.run(query, parameters);
                	return null;
                }
            });
        };
		
	
		System.out.println(info);
		driver.close();
	}
	
	protected Domain buildViti(String startUrl) throws IOException{
		final Domain info = new Domain();
		
		final Document doc = Jsoup.connect(startUrl).get();
		
		
		info.setDomainName(doc.getElementsByAttributeValue("class","domain-name").text().trim());
		
		final Element contactElement = doc.select("#row1 > div > div:nth-child(2) > div.col-md-4 > div:nth-child(2) > div > ul").first();
		
		Elements elements = contactElement.select("li");
		boolean first = true;
		for (Element element : elements) {
			if (first) {
				first=false;
				info.setInfo(element.text().trim());
			}
			else {
				final Element child = element.getElementsByTag("img").first();
				if (child != null) {
					final String source  = child.attr("src");
					switch (source) {
					case "/theme_front/theme_front_16/image/domaine/icon-name.svg" : info.setContact(element.text().trim());break;
					case "/theme_front/theme_front_16/image/domaine/icon-phone.svg" : info.getPhones().add(element.text().trim());break;
					case "/theme_front/theme_front_16/image/domaine/icon-link.svg" : info.setWebsite(element.text().trim());break;
					case "/theme_front/theme_front_16/image/domaine/icon-address.svg" : child.remove();this.setAddress(element.html().trim(), info);break;
					case "/theme_front/theme_front_16/image/domaine/icon-print.svg" : info.setFax(element.text().trim());break;
					}
				}
			}
			
			
		}
		String scoords = doc.select("#row1 > div > div:nth-child(2) > div.col-md-4 > div:nth-child(4) > div > div:nth-child(2) > span").text();
		String [] acoords = scoords.trim().split(",");
		double [] coords = {Double.parseDouble(acoords[0]),Double.parseDouble(acoords[1])};
		info.setCoords(coords);
		
		return info;
	}
	
	private void setAddress (String text, Domain info) {
		String [] parts = text.split("<span>");
		info.setAddress(parts[0].trim());
		info.setZipCode(parts[1].substring(0,5).trim());
		String city = parts[1].substring(5,parts[1].length()).replaceFirst("</span>","").trim();
		info.setCity(city);
	}
	
	public static void main (String [] args) {
		try {
			TestImportViti id = new TestImportViti();
			
			id.testImport();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
