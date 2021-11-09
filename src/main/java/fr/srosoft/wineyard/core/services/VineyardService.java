package fr.srosoft.wineyard.core.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.VineyardDao;
import fr.srosoft.wineyard.core.model.entities.Grape;
import fr.srosoft.wineyard.core.model.entities.Parcel;

@Service
public class VineyardService {

	@Resource
	private VineyardDao vineyardDao;

	private List<Grape> grapesCache;
	private Map<String, List<Parcel>> parcelsForDomains = new HashMap<>();

	// *** Grapes
	public List<Grape> findAllGrapes() {
		if (grapesCache == null) {
			grapesCache = vineyardDao.findAllGrapes();
		}

		return grapesCache;
	}

	
	// *** parcels
	public void registerParcelToDomain(String featureId, String domainId) {
		vineyardDao.addParcel(featureId, domainId);
		this.loadParcels(domainId);
	}

	public List<Parcel> getParcels(String domainId) {

		if (parcelsForDomains.get(domainId) == null) {
			this.loadParcels(domainId);
		}
		return parcelsForDomains.get(domainId);
	}

	private void loadParcels(String domainId) {
		parcelsForDomains.put(domainId, vineyardDao.findParcels(domainId));
	}
}
