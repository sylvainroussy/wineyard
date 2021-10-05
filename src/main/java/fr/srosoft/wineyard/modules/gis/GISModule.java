package fr.srosoft.wineyard.modules.gis;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="GISModule", 
			description="Goegraphic Information System", 
			label="GIS",
			order=2)
public class GISModule extends AbstractModule{
	
	@Resource
	private DomainDao domainDao;
	
	private List<Domain> managedDomains;

	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
		managedDomains = domainDao.findManagedDomains(context.getCurrentUser().getId());
		
	}


	@Override
	public String getMainPage() {
		
		return null;
	}
	
	
}
