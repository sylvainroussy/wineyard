package fr.srosoft.wineyard.modules.vineyard;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.entities.Parcel;
import fr.srosoft.wineyard.core.services.GISService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;
import fr.srosoft.wineyard.modules.domain.DomainModule;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="VineyardModule", 
			description="Global Domain Management", 
			label="Vigne",
			order=1)
public class VineyardModule extends AbstractModule{
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private String currentParcelsView ="grid";
	
	private DomainModule domainModule;
	
	@Resource
	private GISService gisService;
	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
		domainModule = (DomainModule)context.getModule("DomainModule");
	}


	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getCurrentParcelsView() {
		return currentParcelsView;
	}


	public void setCurrentParcelsView(String currentParcelsView) {
		this.currentParcelsView = currentParcelsView;
	}
	
	public void changeCurrentParcelsView(String currentParcelsView) {
		this.currentParcelsView = currentParcelsView;
	}
	
	
	public List<Parcel> getParcels (){
		final String domainId = domainModule.getCurrentDomain().getId();
		return gisService.getParcels(domainId);
		
	}
	
	public String getJsonParcel(String parcelId) throws Exception {
		List<Parcel> parcels = getParcels ();
		Parcel parcel = parcels.stream().filter(e -> e.getWineyardId().equals(parcelId)).findFirst().get();
		return MAPPER.writeValueAsString(parcel.getGeometry());
		
	}

	
	
}
