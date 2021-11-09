package fr.srosoft.wineyard.modules.gis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="GISModule", 
			description="Goegraphic Information System", 
			label="GIS",
			order=50)
public class GISModule extends AbstractModule{
	
		
	

	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
	
		super.loadData(context);
	}
	
	@Override
	public String getIcon() {
		return "pi pi-map-marker";
	}


	
	
}
