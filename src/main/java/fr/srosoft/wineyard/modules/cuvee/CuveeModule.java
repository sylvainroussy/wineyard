package fr.srosoft.wineyard.modules.cuvee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.beans.CuveeSummary;
import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Blend;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Grape;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.services.CaveService;
import fr.srosoft.wineyard.core.services.VineyardService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;
import fr.srosoft.wineyard.utils.WineyardUtils;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="CuveeModule", 
description="Gestion des cuvées du domaine", 
label="Cuvées",
order=2)
public class CuveeModule extends AbstractModule{

	@Resource
	private CaveService caveService;
	
	@Resource
	private VineyardService vineyardService;
	
	private Appellation currentAppellation;
	private List<Grape> selectedGrapes = new ArrayList<>();
	
	private Cuvee currentCuvee;
	
	private Millesime currentMillesime;
	
	@Override
	public void loadData(UserSession context) {
		super.loadData(context);
		
	}
	
	@Override
	public String getIcon() {
		return "pi pi-tags";
	}

	public List<Appellation> getAppellations(){
		String currentDomainId =context.getCurrentDomain().getId();
		return caveService.findAppellations(currentDomainId);
	}
	
	public void prepareAllCuveesForThisYear() {
		
		final int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		final List<Cuvee> allCuvees = new ArrayList<>();
		
		for (Appellation appellation : this.getAppellations()) {
			
			final Millesime millesime = new Millesime();
			millesime.setYear(year);
			millesime.setId(this.context.getCurrentDomain().getId()+"_"+year);
			WineyardUtils.stamp(millesime, true, context.getCurrentUser().getDisplayName());
			
			final Cuvee cuvee = new Cuvee ();
			cuvee.setId(appellation.getId()+"_"+year);
			cuvee.setMillesime(millesime);		
			cuvee.setAppellation(appellation);
			WineyardUtils.stamp(cuvee, true, context.getCurrentUser().getDisplayName());
			allCuvees.add(cuvee);
		}
		
		caveService.saveAllCuvees(allCuvees, context );
	}
		
	
	public List<Cuvee> getCurrentCuvees() {
		return caveService.getCurrentCuvees (context);
	}
	
	public List<CuveeSummary> getCurrentCuveesSummary() {
		
		return caveService.getCurrentCuvees (context).stream().map(e -> {
			final CuveeSummary summary = new CuveeSummary();
			summary.setCuvee(e);
			
			summary.setStockSummary(caveService.getStockSummaryForCuvee(e.getId(), context));
			
			return summary;
			
		}).collect(Collectors.toList());
		
		
	}
	
	public List<Millesime> getMillesimes() {
		return caveService.getMillesimes (context);
	}
	
	public void openAppellation (Appellation appellation) {
		this.currentAppellation  = appellation;
	}

	public void saveAppellation() {
		caveService.saveAppellation(this.currentAppellation, this.context);
		this.selectedGrapes.clear();
		this.currentAppellation = null;
		
	}
	
	public void duplicateAppellation() {
		final Appellation appellation  = new Appellation();
		final int count  = this.caveService.countAppellationsByAppellation(this.currentAppellation.getAppellation(), this.context.getCurrentDomain().getId());
		appellation.setId(this.currentAppellation.getId()+"_"+(count+1));
		appellation.setAppellation(currentAppellation.getAppellation());
		appellation.setLabel("(cuvée spéciale)");
		for (Blend blend : this.currentAppellation.getBlends() ) {
			Blend newblend = new Blend();
			newblend.setId(blend.getId()+"_"+(count+1));
			newblend.setGrape(blend.getGrape());
			newblend.setProportion(blend.getProportion());
			appellation.addBlend(newblend);
			
			
		}
		
		this.currentAppellation = appellation;
		this.saveAppellation();
	
	}
	
	public void cancelAppellation() {		
		this.selectedGrapes.clear();
		this.currentAppellation = null;
		
	}
	
	public List<Grape> getAllGrapes (){
		return vineyardService.findAllGrapes();
	}
	
	public List<Grape> searchGrapeContains(String query) {
        String queryLowerCase = query.toLowerCase();       
        List<Grape> grapes = vineyardService.findAllGrapes().stream().filter(t -> t.getCommonName().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
        return grapes;
    }
	
	public void handleSelectGrape(SelectEvent<Grape> event) {
		final Grape grape=event.getObject();		
		
		Optional<Blend> alreadyPresent = Optional.empty(); 
		if (this.currentAppellation.getBlends() != null) {
			alreadyPresent = this.currentAppellation.getBlends().stream().filter (e -> e.getGrape().getId().equals(grape.getId())).findAny();
		}
		
		if (alreadyPresent.isEmpty()) {
			final Blend blend = new Blend();
			blend.setGrape(grape);
			blend.setProportion(0f);
			blend.setId(this.currentAppellation.getId()+"_"+grape.getId());
			
			this.currentAppellation.addBlend(blend);
		}
	}
	
	public Appellation getCurrentAppellation() {
		return currentAppellation;
	}

	public List<Grape> getSelectedGrapes() {
		return selectedGrapes;
	}

	public void setSelectedGrapes(List<Grape> selectedGrapes) {
		this.selectedGrapes = selectedGrapes;
	}

	public Millesime getCurrentMillesime() {
		return currentMillesime;
	}

	public void setCurrentMillesime(Millesime currentMillesime) {
		this.currentMillesime = currentMillesime;
		context.onMillesimeChange();
	}

	public Cuvee getCurrentCuvee() {
		return currentCuvee;
	}

	public void setCurrentCuvee(Cuvee currentCuvee) {
		this.currentCuvee = currentCuvee;
	}

}
