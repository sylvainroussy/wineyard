package fr.srosoft.wineyard.modules.harvest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.services.DirectoryService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="HarvestModule", 
			description="Global Domain Management", 
			label="Vendanges",
			order=5)
public class HarvestModule extends AbstractModule{
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Resource
	private DirectoryService directoryService;
	
	


	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
		
	}


	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}


		
	
}
