package fr.srosoft.wineyard.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.DomainDao;
import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Tank;

@Service
public class CaveService {
	
	@Resource
	private DomainDao domainDao;
	
	public static final String[] actionNames1= {  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	public static final String[] actionNames2= {"MISE EN BOUTEILLES",  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	
	private Map<String,List<Tank>> tanks = new HashMap<>();
	
	public List<Tank> getTanks(String domainId){
		if(tanks.get(domainId) == null) {
			this.loadTanks(domainId);
		}		
		return tanks.get(domainId);
		
	}
	
	public List<Appellation> findAppellations(String domainId){
		return domainDao.findAppelationsForDomain(domainId);
	}
	
	public Appellation findAppellation(String appellationId){
		return domainDao.findAppellation(appellationId);
	}
	
	private void loadTanks(String domainId) {
		
		List<Appellation> appellations = findAppellations(domainId);
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
