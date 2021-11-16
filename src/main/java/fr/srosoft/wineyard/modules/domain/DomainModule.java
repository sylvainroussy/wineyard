package fr.srosoft.wineyard.modules.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFileWrapper;
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
import fr.srosoft.wineyard.core.services.VineyardService;
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
	protected DirectoryService directoryService;
	
	@Resource
	private GISService gisService;
	
	@Resource
	private VineyardService vineyardService;
	
	private Domain currentDomain;

	private User currentUser;
	
	 private UploadedFile file;
	
	private DashletDomain dashletDomain;
	
	@PostConstruct
	public void init () {
		
	}
	
	@Override
	public String getIcon() {
		return "pi pi-home";
	}


	@Override
	public void loadData(UserSession context) {
		super.loadData(context);
		this.currentUser = context.getCurrentUser();
		directoryService.getDomainsForUser(currentUser.getId());
		currentDomain  = directoryService.getDefaultDomain(currentUser.getId());
		
		dashletDomain = new DashletDomain(this);
		
	}



	/**
	 * Adding ârcel to currentDomain
	 */
	public void addToCurrentDomain() {
		 String featureId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("featureId");
		 vineyardService.registerParcelToDomain(featureId, this.currentDomain.getId());
		 
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
		dashletDomain = new DashletDomain(this);
		this.context.onDomainChange();
		
	}
	
	public byte[] getDomainLogo() throws Exception{
		return directoryService.getDomainLogo(this.currentDomain.getId());
	}
	
	public void uploadLogo() {
        if (file != null) {
         this.directoryService.saveDomainLogo(context.getCurrentDomain().getId(), file.getContent());
        }
    }
	
	
	public void handleFileUpload(FileUploadEvent event) {
		UploadedFileWrapper f = null;
		 this.directoryService.saveDomainLogo(context.getCurrentDomain().getId(),  event.getFile().getContent());
     
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

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	

	
	
}
