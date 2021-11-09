package fr.srosoft.wineyard.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.CuveeDao;
import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.WineyardObject;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.utils.WineyardUtils;

@Service
public class CaveService {
	
	@Resource
	private CuveeDao cuveeDao;
	
	
	
	public static final String[] actionNames1= {  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	public static final String[] actionNames2= {"MISE EN BOUTEILLES",  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	
	private Map<String,List<Tank>> tanks = new HashMap<>();
	private Map<String,List<Appellation>> appellations = new HashMap<>();
	
	
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
	
	public List<Millesime> getMillesimes(UserSession context){
		return cuveeDao.findMillesimesByDomain(context.getCurrentDomain().getId());
	}
	
	
	
	private void stampObject (WineyardObject object, String label, UserSession context) {
		boolean exists = this.cuveeDao.exists(label, object.getId());
		WineyardUtils.stamp(object, !exists, context.getCurrentUser().getDisplayName());
	}	

	
	private void loadTanks(String domainId) {
		
		
		final List<Tank> tanks = new ArrayList<>();
		
		/*for (int i = 0; i < 4; i++) {
			Tank tank = new Tank();
			tank.setId(""+i);
			tank.setNumber(""+i);
			tank.setVolume(400);
			tank.setYear("2015");
			
			Contents c = new Contents();
			c.setAppellation(appellations.get(2));
			c.addGrape("Chardonnay");
			c.setColor("white");
			c.setId(UUID.randomUUID().toString());
			c.setYear("2020");
			c.setVolume(400);
			tank.setContents(c);
			tanks.add(tank);
			
			for (int j= 0; j < 3; j++) {
				
				final Action action = new Action();
				action.setId(UUID.randomUUID().toString());
				action.setActionName(actionNames1[j]);
				action.setComment("Commentaire sur l'action");
				action.setDate(new Date());
				tank.addAction(action);
			}
		}
		
		for (int i = 0; i < 5; i++) {
			Tank tank = new Tank();
			tank.setId(""+(i+5));
			tank.setNumber(""+(i+5));
			tank.setVolume(400);
			tank.setYear("2015");
			
			Contents c = new Contents();
			c.setAppellation(appellations.get(3));
			c.addGrape("Pinot noir");
			c.setId(UUID.randomUUID().toString());
			c.setYear("2020");
			c.setColor("red");
			c.setVolume(200);
			tank.setContents(c);
			tanks.add(tank);
			
			for (int j= 0; j < 4; j++) {
				
				final Action action = new Action();
				action.setId(UUID.randomUUID().toString());
				action.setActionName(actionNames2[j]);
				action.setComment("Commentaire sur l'action");
				action.setDate(new Date());
				tank.addAction(action);
			}
		}*/
		
		this.tanks.put(domainId,tanks);
	}
	
	public void addTank(String domainId, Tank tank) {
		this.tanks.get(domainId).add(tank);
		//this.loadTanks(domainId);
	}

}
