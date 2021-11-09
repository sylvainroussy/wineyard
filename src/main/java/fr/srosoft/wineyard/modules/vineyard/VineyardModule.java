package fr.srosoft.wineyard.modules.vineyard;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.entities.Parcel;
import fr.srosoft.wineyard.core.services.VineyardService;
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
	private VineyardService vineyarService;
	
	@PostConstruct
	public void init () {
		
	}
	
	@Override
	public String getIcon() {
		return "grape_white";
	}


	@Override
	public void loadData(UserSession context) {
		super.loadData(context);
		domainModule = (DomainModule)context.getModule("DomainModule");
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
		return vineyarService.getParcels(domainId);
		
	}
	
	public String getJsonParcel(String parcelId) throws Exception {
		final List<Parcel> parcels = getParcels ();
		final Parcel parcel = parcels.stream().filter(e -> e.getWineyardId().equals(parcelId)).findFirst().get();
		return MAPPER.writeValueAsString(parcel.getGeometry());
		
	}

	
	
}
