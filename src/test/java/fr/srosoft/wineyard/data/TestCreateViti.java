package fr.srosoft.wineyard.data;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;

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
public class TestCreateViti {
	
	@Resource
	private DomainDao domainDao;
	
	@Test
	public  void buildMonnotRoche() throws IOException{
		final Domain domain = new Domain();
		domain.setDomainName("Domaine Monnot-Roche");
		domain.setCity("CHANGE");
		domain.setZipCode("21340");
		domain.setAddress("3 rue COLLOT");
		domain.setSurface(9);
		domain.setCoords(new double[]{46.930119d,4.632291d});
		domain.setDataSource("https://monnot-roche.com");
		domain.setInfo("Propriétaire récoltant");
		domain.setContact("Christophe et Frédérique MONNOT");
		domain.setPhones(Arrays.asList("06 07 12 11 54","03 85 91 17 74"));
		domain.setWebsite("https://monnot-roche.com");
		domain.setEmail("contact@monnot-roche.com");
		final String id = domain.getDomainName().toLowerCase().replaceAll(" ", "_")+":"+domain.getZipCode();
		domain.setId(id);
		
		Appellation appellation = new Appellation();
		String appName = "Maranges 1er Cru".toUpperCase()+" - La Fussière (vin rouge)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("red");		
		domain.addAppellation(appellation);
		
		appellation = new Appellation();
		appName = "Maranges 1er Cru".toUpperCase()+" - La Croix aux Moines (vin rouge)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("red");
		domain.addAppellation(appellation);
		
		appellation = new Appellation();
		appName = "Saint Aubin 1er Cru".toUpperCase()+" - Les Frionnes (vin rouge)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("red");
		domain.addAppellation(appellation);
		
		appellation = new Appellation();
		appName = "Bourgogne Hautes-Côtes de Beaune".toUpperCase()+" (vin rouge)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("red");
		appellation.setLabel("Le bas de Chassagne");
		domain.addAppellation(appellation);
		
		appellation = new Appellation();
		appName = "Bourgogne Passetoutgrains".toUpperCase()+" (vin rouge)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("red");		
		domain.addAppellation(appellation);
		
		appellation = new Appellation();
		appName = "Bourgogne Hautes-Côtes de Beaune".toUpperCase()+" (vin blanc)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("white");		
		domain.addAppellation(appellation);
		
		appellation = new Appellation();
		appName = "Bourgogne Aligote".toUpperCase()+" (vin blanc)";
		appellation.setId(appName);
		appellation.setAppellation(appName);
		appellation.setWineColor("white");		
		domain.addAppellation(appellation);
		
				
		domainDao.createDomain(domain);
	}
	
	
}
