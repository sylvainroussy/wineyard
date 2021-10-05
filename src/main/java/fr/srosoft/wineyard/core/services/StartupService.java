package fr.srosoft.wineyard.core.services;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.SystemDao;
import fr.srosoft.wineyard.core.model.entities.User;

@Service
public class StartupService {

	private final String SYSTEM_EMAIL = "wineyard@srosoft.fr";
	
	@Resource
	private SystemDao systemDao;
	
	@PostConstruct
	public void init() {
		if(!systemDao.isUserExists(SYSTEM_EMAIL)) {
			final User systemUser = new User() ;
			systemUser.setEmail(SYSTEM_EMAIL);
			systemUser.setDisplayName("Wineyard System Administrator");
			systemDao.createUser(systemUser, "root");
		}
	}
}
