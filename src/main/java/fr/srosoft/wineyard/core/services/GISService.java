package fr.srosoft.wineyard.core.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.GisDao;
import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.Region;
import fr.srosoft.wineyard.core.session.UserSession;

@Service
@Scope("application")
public class GISService {

	@Resource
	private GisDao gisDao;
	
	
	private Map<String,List<Region>> regionsDomainCache = new HashMap<>();
	private Map<String,List<Region>> regionsCountryCache = new HashMap<>();
	
	
	
	
	// **** Regions ***
	public List<Region> findRegionsForCurrentDomain(UserSession context) {
		final Domain currentDomain = context.getCurrentDomain();
		if (regionsDomainCache.get(currentDomain.getId()) == null) {
			this.loadRegionsDomainCache(currentDomain);
		}
		return regionsDomainCache.get(currentDomain.getId());
	}
	
	public List<Region> findRegionsByCountryId(String countryId) {
		if (regionsCountryCache.get(countryId) == null) {
			this.loadRegionsCountryCache(countryId);
		}
		return regionsCountryCache.get(countryId);
	}
	
	private void loadRegionsDomainCache  (Domain domain) {
		this.regionsDomainCache.put(domain.getId(), this.gisDao.findRegionsByIds(domain.getRegionIds()));
	}
	
	private void loadRegionsCountryCache  (String countryId) {
		this.regionsCountryCache.put(countryId, this.gisDao.findRegionsByCountryId(countryId));
	}
	
	
	public void evictFromRegionsDomainCache(String domainId) {
		this.regionsDomainCache.remove(domainId);
	}
	
	
	// *** CRINAOS ***
	public final Map<String,List<Map<String,List<String>>>> findCrinaosByRegionIds (List<String> regionIds){
		return gisDao.findCrinaosByRegionIds(regionIds);
	}
}
