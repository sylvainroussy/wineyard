package fr.srosoft.wineyard.data;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.srosoft.wineyard.core.main.WineyardApplication;
import fr.srosoft.wineyard.core.model.dao.GisDao;
import fr.srosoft.wineyard.core.model.entities.Country;
import fr.srosoft.wineyard.core.model.entities.Region;

@SpringBootTest(classes= {WineyardApplication.class})
@RunWith(SpringRunner.class)
public class TestCreateFranceRegions {

	@Resource
	private GisDao gisDao;
	
	@Test
	public void createFranceRegionsTest() {
		
		final Country country = new Country();
		country.setId("FR");
		country.setIso2Code("FR");
		country.setLangCode("fr");
		country.setName("FRANCE");
		country.setLabel("country.fr");
		
		final List<String> regionNames = Arrays.asList("Alsace","Beaujolais","Bordeaux","Bourgogne",
				"Champagne","Corse","Jura-Savoie-Bugey","Languedoc-Roussillon","Loire",
				"Moselle","Provence","Rhône","Sud-Ouest");
		regionNames.forEach(e -> {
			final Region region = new Region();
			region.setCountry(country);
			region.setLabel(e);
			final String name = e.replaceAll("ô", "o").replaceAll("-", "_").toUpperCase();
			region.setName(name);
			region.setId(name);
			
			gisDao.createRegion(region);
			
		});
	}
}
