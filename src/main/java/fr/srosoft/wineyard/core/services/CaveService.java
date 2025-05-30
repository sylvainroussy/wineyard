package fr.srosoft.wineyard.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.beans.ContentPath;
import fr.srosoft.wineyard.core.model.beans.Stock;
import fr.srosoft.wineyard.core.model.beans.StockSummary;
import fr.srosoft.wineyard.core.model.dao.CuveeDao;
import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Chemical;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.WineyardObject;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.core.unit.Bottle;
import fr.srosoft.wineyard.utils.WineyardUtils;

@Service
public class CaveService {
	
	@Resource
	private CuveeDao cuveeDao;
	
	
	
	public static final String[] actionNames1= {  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	public static final String[] actionNames2= {"MISE EN BOUTEILLES",  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	
	private Map<String,List<Tank>> tanks = new HashMap<>();
	private Map<String,List<Appellation>> appellations = new HashMap<>();
	
	private List<Chemical> chemicalsCache ;
	
	
	public List<Appellation> findAppellations(String domainId){
		if(appellations.get(domainId) == null) {
			this.loadAppellations(domainId);
		}		
		return appellations.get(domainId);		
	}
	
	
	
	public Appellation findAppellation(String appellationId){
		return cuveeDao.findAppellation(appellationId);
	}
	
	public int countAppellationsByAppellation(String appellation, String domainId){
		
		return cuveeDao.countAppellations(appellation, domainId);		
	}
	
	public void saveAppellation(Appellation appellation, UserSession context) {
		this.stampObject(appellation, "Appellation", context);
		this.cuveeDao.saveAppellation(appellation, context.getCurrentDomain().getId());
		this.appellations.remove(context.getCurrentDomain().getId());
	}
	
	private void loadAppellations(String domainId) {
		this.appellations.put(domainId, cuveeDao.findAppelationsForDomain(domainId));
		
	}
	
	// *** Cuvees
	public void saveAllCuvees (List<Cuvee> cuvees, UserSession context ) {		
		cuvees.stream().forEach(e -> this.stampObject(e, "Cuvee", context));		
		this.cuveeDao.saveAllCuvees (cuvees, context.getCurrentDomain().getId());
	}
	
	public List<Cuvee> getCuvees(UserSession context){
		return cuveeDao.findCuveesByDomain(context.getCurrentDomain().getId());
	}
	
	public List<Cuvee> getCurrentCuvees(UserSession context){
		return cuveeDao.findCuveesByDomainAndMillesime(context.getCurrentDomain().getId(), context.getCurrentMillesime().getYear());
	}
	
	public List<Cuvee> getCuveesByDomainAndMillesime(UserSession context, int year){
		return cuveeDao.findCuveesByDomainAndMillesime(context.getCurrentDomain().getId(), year);
	}
	
	public Cuvee getCuveesById(String cuveeId, UserSession context){
		return cuveeDao.findCuveeById(cuveeId);
	}
	
	public StockSummary getStockSummaryForCuvee(String cuveeId, UserSession context) {
		StockSummary stockSummary = new StockSummary();
		final Long stockHl = cuveeDao.getStocksFromContainers(cuveeId);
		
		Stock stock1 = new Stock();
		stock1.setContainerType("Stock cuves");
		stock1.setQuantity(stockHl.intValue());
		stock1.setUnit("hl");
		
		Stock stock2 = new Stock();
		stock2.setContainerType("Equivalence en bouteilles");
		stock2.setQuantity(Bottle.numberOfBottlesForHl(stockHl.intValue()));
		stock2.setUnit("0,75cl");
		
		stockSummary.addStock(stock1);
		stockSummary.addStock(stock2);
		
		return stockSummary;
	}
	
	public List<ContentPath> findContentPaths (String cuveeId){
		return cuveeDao.findContentPaths(cuveeId);
	}
	
	
	// *** Millesime
	public List<Millesime> getMillesimes(UserSession context){
		return cuveeDao.findMillesimesByDomain(context.getCurrentDomain().getId());
	}
	
	
	
	private void stampObject (WineyardObject object, String label, UserSession context) {
		boolean exists = this.cuveeDao.exists(label, object.getId());
		WineyardUtils.stamp(object, !exists, context.getCurrentUser().getDisplayName());
	}	

	
	private void loadTanks(String domainId) {
		
		
		final List<Tank> tanks = new ArrayList<>();
		this.tanks.put(domainId,tanks);
	}
	
	public void addTank(String domainId, Tank tank) {
		this.tanks.get(domainId).add(tank);
		//this.loadTanks(domainId);
	}
	
	// *** Chemicals
	public List<Chemical> findAllChemicals() {
		if (chemicalsCache == null) {
			chemicalsCache = cuveeDao.findAllChemicals();
		}
		return chemicalsCache;
	}

}
