package fr.srosoft.wineyard.core.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.srosoft.wineyard.core.model.dao.ContainerDao;
import fr.srosoft.wineyard.core.model.entities.Amphora;
import fr.srosoft.wineyard.core.model.entities.Barrel;
import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.ContainerTemplate;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.WineyardObject;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.utils.WineyardUtils;

@Service
public class ContainerService {

	@Resource
	private ContainerDao containerDao;
	
	private Map<String,List<Tank>> tanksCache = new HashMap<>();
	private Map<String,List<Barrel>> barrelsCache = new HashMap<>();
	private Map<String,List<Amphora>> amphoraCache = new HashMap<>();
	
	
	public List<ContainerTemplate> getContainerTemplates(UserSession context){
		return containerDao.findContainerTemplatesByDomain(context.getCurrentDomain().getId());
	}
	
	public void saveContainerTemplate (ContainerTemplate containerTemplate,UserSession context) {
		this.stampObject(containerTemplate, "ContainerTemplate", context);
		containerDao.saveContainerTemplate(containerTemplate, context.getCurrentDomain().getId());
	}
	
	public <T  extends Container> List<T> getContainers(UserSession context, Class<T> containerType){
		return containerDao.findContainersByDomainAndType(context.getCurrentDomain().getId(), containerType);
	}
	
	public void saveContainers (List<Container> containers,UserSession context,ContainerTemplate containerTemplate) {
		final String containerType =  containers.get(0).getClass().getSimpleName();
		this.stampObject(containerTemplate,containerType, context);
		containerDao.saveContainers(containers, context.getCurrentDomain().getId(), containerType,containerTemplate.getId());
		tanksCache.remove(context.getCurrentDomain().getId());
	}
	
	public List<Tank> getTanks(String domainId){
		if(tanksCache.get(domainId) == null) {
			this.loadTanks(domainId);
		}		
		return tanksCache.get(domainId);
		
	}
	
	private void loadTanks(String domainId) {
		tanksCache.put(domainId, containerDao.findContainersByDomainAndType(domainId, Tank.class));
	}
	
	private void stampObject (WineyardObject object, String label, UserSession context) {
		boolean exists = this.containerDao.exists(label, object.getId());
		WineyardUtils.stamp(object, !exists, context.getCurrentUser().getDisplayName());
	}
	
	public <T extends Container> T prepareContainer (ContainerTemplate template) {
		Assert.notNull(template,"template must not be null");
		T container = null;
		Class<T> clazz = null;
		try {
			switch (template.getType()) {
				case "burgundyBarrel" : clazz= (Class<T>) Barrel.class;break;
				case "tank" : clazz= (Class<T>)	Tank.class;break;
				case "amphora" : clazz=(Class<T>) Barrel.class;break;
			}
			
			container = clazz.getDeclaredConstructor().newInstance();
			container.setVolume(template.getVolume());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return container;
	}
}
