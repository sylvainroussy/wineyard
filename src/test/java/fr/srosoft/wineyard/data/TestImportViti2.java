package fr.srosoft.wineyard.data;

import java.io.IOException;
/**
 * Import domains from one page
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.srosoft.wineyard.core.main.WineyardApplication;
import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Domain;

@SpringBootTest(classes= {WineyardApplication.class})
@RunWith(SpringRunner.class)
public class TestImportViti2 {
	
	private List<String> previousUrls = new ArrayList<>();
	private Map<Integer,LinkedList<String>> frontier = new HashMap<Integer,LinkedList<String>>();
	private int depth = 0;
	private final int MAX_DEPTH = 0;
	private static final String HOST = "https://www.vins-bourgogne.fr";
	
	@Resource
	private DomainDao domainDao;
	
	
	@Test
	public void testImport() throws IOException{
		
		
		//final String startUrl = "https://www.vins-bourgogne.fr/nos-savoir-faire/femmes-et-hommes-passionnes/lafouge-joseph-et-fils-change-21340,2397,9211.html?&args=Y29tcF9pZD0xNDA2JmFjdGlvbj12aWV3RmljaGUmaWQ9VklOQk9VMDAwMDIwMDcwMSZ8";
		//this.addInFrontier(startUrl,depth);
		this.importStartUrls();
		this.runLevel();
		
		
		
	}
	
	public void importStartUrls() throws IOException {
		final String urlList = "https://www.vins-bourgogne.fr/nos-savoir-faire/femmes-et-hommes-passionnes/les-vignerons-viticulteurs-passionnes/vignerons-et-maisons-de-bourgogne,2398,9212.html?";
		final Document doc = Jsoup.connect(urlList).get();
		final Elements elements = doc.select("#resultatListeAppellation > ul > li > a");
		for (Element element : elements) {
			String partUrl = element.attr("href");
			 if (!partUrl.toLowerCase().startsWith("http://")) {
				 partUrl = HOST+partUrl;
			 }
			 this.addInFrontier(partUrl, 0);
		}
		
	}
	
	private void addInFrontier(String url, int level) {
		
		//System.out.println("Adding in level "+level+" url: "+url);
		if (frontier.get(level) == null) {
			final LinkedList<String> list = new LinkedList<>();
			frontier.put(level, list);
		}
		
		frontier.get(level).push(url);
	}
	
	private void runLevel () throws IOException{
		System.out.println("LEVEL "+this.depth);
		if (this.depth > MAX_DEPTH) {
			frontier.clear();
			System.out.println("FINISHED: "+this.depth+"/"+MAX_DEPTH);
			return;
		}
		else {
			
			while (frontier.get(this.depth) != null && !frontier.get(this.depth).isEmpty()) {
				String url = frontier.get(this.depth).poll();
				if (previousUrls.contains(url)) {
				
					continue;
				}
				final Domain domain = this.buildViti(url);
				
				domainDao.createDomain(domain);
				domain.setDataSource(url);
				
				this.previousUrls.add(url);
				System.out.println(domain);
			}
			this.depth++;
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			this.runLevel();
		}
	}
	
	protected Domain buildViti(String startUrl) throws IOException{
		final Domain domain = new Domain();
		
		final Document doc = Jsoup.connect(startUrl).get();
		
		
		domain.setDomainName(doc.getElementsByAttributeValue("class","domain-name").text().trim());
		
		final Element contactElement = doc.select("#row1 > div > div:nth-child(2) > div.col-md-4 > div:nth-child(2) > div > ul").first();
		
		Elements elements = contactElement.select("li");
		boolean first = true;
		for (Element element : elements) {
			if (first) {
				first=false;
				domain.setInfo(element.text().trim());
			}
			else {
				final Element child = element.getElementsByTag("img").first();
				if (child != null) {
					final String source  = child.attr("src");
					switch (source) {
					case "/theme_front/theme_front_16/image/domaine/icon-name.svg" : domain.setContact(element.text().trim());break;
					case "/theme_front/theme_front_16/image/domaine/icon-phone.svg" : domain.getPhones().add(element.text().trim());break;
					case "/theme_front/theme_front_16/image/domaine/icon-link.svg" : domain.setWebsite(element.text().trim());break;
					case "/theme_front/theme_front_16/image/domaine/icon-address.svg" : child.remove();this.setAddress(element.html().trim(), domain);break;
					case "/theme_front/theme_front_16/image/domaine/icon-print.svg" : domain.setFax(element.text().trim());break;
					}
				}
			}
			
			
		}
		String scoords = doc.select("#row1 > div > div:nth-child(2) > div.col-md-4 > div:nth-child(4) > div > div:nth-child(2) > span").text();
		String [] acoords = scoords.trim().split(",");
		try {
			if (acoords.length== 2 && !acoords[1].isBlank()) {
				double [] coords = {Double.parseDouble(acoords[0]),Double.parseDouble(acoords[1])};
				domain.setCoords(coords);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		final String id = domain.getDomainName().toLowerCase().replaceAll(" ", "_")+":"+domain.getZipCode();
		domain.setId(id);
		
		final Elements apps = doc.select("#appellations > ul > li > a");
		for (Element element : apps) {
			Appellation appellation = new Appellation();
			appellation.setId(element.text());
			appellation.setAppellation(element.text());
			System.out.println(element.text());
			domain.addAppellation(appellation);
			
		}
		//doc.select("#appellations");
		
		// find urls to add in frontier
		 final Elements neighbors = doc.select("#neighbors > ul > li > a");
		 for (Element element : neighbors) {
			 String partUrl = element.attr("href");
			 if (!partUrl.toLowerCase().startsWith("http://")) {
				 partUrl = HOST+partUrl;
			 }
			this.addInFrontier(partUrl, this.depth+1);
		}
		
		
		return domain;
	}
	
	private void setAddress (String text, Domain info) {
		String [] parts = text.split("<span>");
		info.setAddress(parts[0].trim());
		info.setZipCode(parts[1].substring(0,5).trim());
		String city = parts[1].substring(5,parts[1].length()).replaceFirst("</span>","").trim();
		info.setCity(city);
	}
	
	
	
	
}
