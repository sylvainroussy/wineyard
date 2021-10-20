package fr.srosoft.wineyard.modules.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.beans.UserDomain;
import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.User;
import fr.srosoft.wineyard.core.services.DirectoryService;
import fr.srosoft.wineyard.core.services.GISService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="DomainModule", 
			description="Global Domain Management", 
			label="Domaine",
			order=0)
public class DomainModule extends AbstractModule{
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	
	@Resource
	private DomainDao domainDao;
	
	@Resource
	private DirectoryService directoryService;
	
	@Resource
	private GISService gisService;
	
	private Domain currentDomain;

	private User currentUser;
	
	private UserSession context;
	
	private DashletDomain dashletDomain;
	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
		this.context = context;
		this.currentUser = context.getCurrentUser();
		directoryService.getDomainsForUser(currentUser.getId());
		currentDomain  = directoryService.getDefaultDomain(currentUser.getId());
		
		dashletDomain = new DashletDomain();
		
	}


	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adding ârcel to currentDomain
	 */
	public void addToCurrentDomain() {
		 String featureId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("featureId");
		 gisService.registerParcelToDomain(featureId, this.currentDomain.getId());
		 
	}

	public String getJsonManagedDomains() throws Exception {
		
		return MAPPER.writeValueAsString(directoryService.getDomainsForUser(currentUser.getId()));
		
	}
	
	public String getJsonCurrentDomain() throws Exception {
		
		return MAPPER.writeValueAsString(this.currentDomain);
		
	}
	
	public List<Domain> getManagedDomains() {
		return directoryService.getDomainsForUser(currentUser.getId());
	}


	

	public Domain getCurrentDomain() {
		return currentDomain;
	}


	public void setCurrentDomain(Domain currentDomain) {
		this.currentDomain = currentDomain;
	}
	
	
	
	public void changeDomainContext (ValueChangeEvent event) {
		final String domainId = (String)event.getNewValue();
		this.currentDomain = directoryService.getDomainsForUser(currentUser.getId())
				.stream().filter(e -> e.getId().equals(domainId)).findFirst().get();
		dashletDomain = new DashletDomain();
		
	}
	
	public byte[] getDomainLogo() throws Exception{
		return directoryService.getDomainLogo(this.currentDomain.getId());
	}
	


	public List<UserDomain> getUsersOfDomains() {
		
		if (currentDomain != null) {
			return directoryService.getUsersOfDomain(currentDomain.getId());
		}
		else return new ArrayList<>();
	}


	public DashletDomain getDashletDomain() {
		return dashletDomain;
	}


	public void setDashletDomain(DashletDomain dashletDomain) {
		this.dashletDomain = dashletDomain;
	}


	
}
