package fr.srosoft.wineyard.core.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.beans.UserDomain;
import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.dao.SystemDao;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.User;

@Service
public class DirectoryService {

	@Resource
	private DomainDao domainDao;
	
	
	
	@Resource
	private SystemDao systemDao;
	
	private List<User> allUsers;
	
	private Map<String,List<UserDomain>> usersOfDomain = new HashMap<>();
	
	private Map<String,List<Domain>> domainsOfUser = new HashMap<>();
	
	
	public List<UserDomain> getUsersOfDomain(String domainId){
		if (usersOfDomain.get(domainId) == null) this.loadUsersOfDomain(domainId);
		return usersOfDomain.get(domainId);
	}
	
	private void loadUsersOfDomain(String domainId) {
		final List<UserDomain> users = domainDao.findDomainUsers2( domainId);
		usersOfDomain.put(domainId,users);
	}
	
	public Domain getDefaultDomain(String userId) {
		if (this.getDomainsForUser(userId) ==null) return null;
		else return this.getDomainsForUser(userId).get(0);
	}
	
	public List<Domain> getDomainsForUser(String userId){
		if (domainsOfUser.get(userId) == null) {
			loadDomainsOfUser (userId);
		}
		return domainsOfUser.get(userId);
	}
	
	public byte[] getDomainLogo(String domainId) throws Exception{
		byte[] bytes = {};
		try {
			FileInputStream stream = new FileInputStream(new File("C:\\z_data\\wineyard\\wineyard-fs\\"+domainId+"\\logo.png"));
			bytes =  stream.readAllBytes();
			stream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	private void loadDomainsOfUser (String userId) {
		final List<Domain> domains = domainDao.findManagedDomains(userId);
		domainsOfUser.put (userId,domains);
	}
	
	public List<User> getAllUsers() {
		if (allUsers == null) this.loadAllUsers();
		return allUsers;
	}
	
	private void loadAllUsers() {
		allUsers = domainDao.findAllUsersAndDomains();
	}
	
	public void saveUser(User user) {
		domainDao.createUser(user, user.getPassword());
		this.loadAllUsers();
		if(user.getLinkedDomains() != null) {
			user.getLinkedDomains().stream().forEach(e -> loadUsersOfDomain(e.getDomain().getId()) );
		}
	}
	
	
}
