package fr.srosoft.wineyard.modules.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.LinkedDomain;
import fr.srosoft.wineyard.core.model.entities.User;
import fr.srosoft.wineyard.core.services.DirectoryService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="AdminModule", 
			description="Wineyard administration", 
			label="Administration",
			order=100)
public class AdminModule extends AbstractModule{
	
	@Resource
	private DomainDao domainDao;
	
	@Resource
	private DirectoryService directoryService;
	
	private List<Domain> allDomains ;
	
	private List<User> allUsers;
	
	private List<Domain> selectedDomains = new ArrayList<>();
	
	
	private String usersView="list";
	
	private User editedUser;
	
	@PostConstruct
	public void init () {
		allDomains = domainDao.findAllDomains();
		allUsers = directoryService.getAllUsers();
	}
	
	@Override
	public void loadData(UserSession context) {
		super.loadData(context);
		
	}	
	
	public List<Domain> searchDomainContains(String query) {
        String queryLowerCase = query.toLowerCase();       
        return allDomains.stream().filter(t -> t.getDomainName().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }
	
	

	public void switchToCreateUser() {
		if(this.selectedDomains != null) {
			this.selectedDomains.clear();
		}
		
		
		editedUser = new User();
		editedUser.setEnabled(true);
		
		this.changeUsersView("new");
	}
	
	public void switchToEditUser(User user) {
		
		user.getLinkedDomains().stream().forEach (e -> this.selectedDomains.add (e.getDomain()));
		
		
		editedUser = user;
		
		
		this.changeUsersView("edit");
	}
	
	
	public void handleSelect(SelectEvent<Domain> event) {
		final Domain domain=event.getObject();		
		if(this.selectedDomains != null) {
			LinkedDomain ud = new LinkedDomain(domain,null);
			this.editedUser.addLinkedDomain(ud);
		
		}
	  
	}
	
	public void removeDomain(LinkedDomain domain) {
		this.selectedDomains.remove(domain.getDomain());
		this.editedUser.getLinkedDomains().remove(domain);
	}
	
	public void cancelUser() {
		this.changeUsersView("list");
		if (this.selectedDomains != null) this.selectedDomains.clear();
		this.editedUser = null;
		this.init();
	}
	
	public void saveUser() {
		directoryService.saveUser(this.editedUser);
		this.changeUsersView("list");
		if (this.selectedDomains != null) this.selectedDomains.clear();
		this.editedUser = null;
		this.init();
	}
	

	

	public List<Domain> getAllDomains() {
		return allDomains;
	}


	public void setAllDomains(List<Domain> allDomains) {
		this.allDomains = allDomains;
	}


	public List<User> getAllUsers() {
		return allUsers;
	}


	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	public List<Domain> getSelectedDomains() {
		return selectedDomains;
	}

	public void setSelectedDomains(List<Domain> selectedDomains) {
		this.selectedDomains = selectedDomains;
	}

	public String getUsersView() {
		return usersView;
	}

	public void setUsersView(String usersView) {
		this.usersView = usersView;
	}
	
	public void changeUsersView(String usersView) {
		this.usersView = usersView;
	}

	public User getEditedUser() {
		return editedUser;
	}

	public void setEditedUser(User editedUser) {
		this.editedUser = editedUser;
	}

	
	
}
