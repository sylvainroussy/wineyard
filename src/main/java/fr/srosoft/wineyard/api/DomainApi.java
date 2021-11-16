package fr.srosoft.wineyard.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Domain;

@RequestMapping("api")
public class DomainApi {

	@Resource
	private DomainDao domainDao;
	
	@GetMapping ("/domain/{userEmail}")
	public List<Domain> getDomainsForUser(){
		return null;//sdomainDao.findManagedDomains(userid);
	}
}
