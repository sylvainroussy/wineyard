package fr.srosoft.wineyard.modules.directory;

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
@Module (name="DirectoryModule", 
			description="Directory", 
			label="Annuaire",
			order=9)
public class DirectoryModule extends AbstractModule{
	
	@Resource
	private DomainDao domainDao;
	
	private List<Domain> allDomains ;
	
	@PostConstruct
	public void init () {
		allDomains = domainDao.findAllDomains();
	}


	@Override
	public void loadData(UserSession context) {
		
		
	}


	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Domain> getAllDomains() {
		return allDomains;
	}


	public void setAllDomains(List<Domain> allDomains) {
		this.allDomains = allDomains;
	}
	
	
}
