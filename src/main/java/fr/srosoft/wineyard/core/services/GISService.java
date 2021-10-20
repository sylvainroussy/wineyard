package fr.srosoft.wineyard.core.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.FeatureDao;
import fr.srosoft.wineyard.core.model.entities.Parcel;

@Service
public class GISService {

	@Resource
	private FeatureDao featureDao;
	
	private Map<String,List<Parcel>> parcelsForDomains = new HashMap<>();
	
	public void registerParcelToDomain(String featureId, String domainId) {
		featureDao.linkFeatureToDomain(featureId, domainId);
		this.loadParcels(domainId);
	}
	
	public List<Parcel> getParcels(String domainId){
		
		if (parcelsForDomains.get(domainId) == null){
			this.loadParcels(domainId);
		}
		return parcelsForDomains.get(domainId);
	}
	
	private void loadParcels (String domainId) {
		parcelsForDomains.put(domainId,featureDao.findParcels(domainId));
	}
	
	
	
	
}
