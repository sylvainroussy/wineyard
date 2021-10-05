package fr.srosoft.wineyard.modules.harvest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.User;
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
	private DomainDao domainDao;
	
	private List<Domain> managedDomains;
	private List<User> usersOfDomains;
	private Domain currentDomain;

	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
		managedDomains = domainDao.findManagedDomains(context.getCurrentUser().getId());
		if (managedDomains.size() == 1) {
			this.currentDomain = managedDomains.get(0);
		}
		
		usersOfDomains = domainDao.findDomainUsers(this.currentDomain.getId());
	}


	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getJsonManagedDomains() throws Exception {
		
		return MAPPER.writeValueAsString(managedDomains);
		
	}
	
	public List<Domain> getManagedDomains() {
		return managedDomains;
	}


	public void setManagedDomains(List<Domain> managedDomains) {
		this.managedDomains = managedDomains;
	}


	public Domain getCurrentDomain() {
		return currentDomain;
	}


	public void setCurrentDomain(Domain currentDomain) {
		this.currentDomain = currentDomain;
	}


	public List<User> getUsersOfDomains() {
		return usersOfDomains;
	}


	public void setUsersOfDomains(List<User> usersOfDomains) {
		this.usersOfDomains = usersOfDomains;
	}
	
	
}
