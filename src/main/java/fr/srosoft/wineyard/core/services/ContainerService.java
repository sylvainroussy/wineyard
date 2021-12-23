package fr.srosoft.wineyard.core.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.srosoft.wineyard.core.model.dao.ContainerDao;
import fr.srosoft.wineyard.core.model.entities.Amphora;
import fr.srosoft.wineyard.core.model.entities.Barrel;
import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.ContainerTemplate;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.WineyardObject;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.utils.Constants.STATE_CONTAINER;
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
	
	public void updateContainer (Container container,UserSession context) {
		if (container.getId() == null) throw new IllegalArgumentException ("updateContainer() Container must already exist! ");
		final String containerType =  container.getClass().getSimpleName();
		this.stampObject(container,containerType, context);
		if (container.getStatus().equals(STATE_CONTAINER.STATE_CONTAINER_NEEDS_NUMBER) 
				&& container.getNumber() != null
				&& !container.getNumber().isBlank()) {
			container.setStatus(STATE_CONTAINER.STATE_CONTAINER_READY);			
		}
		containerDao.updateContainer(container, context.getCurrentDomain().getId(), containerType);
		tanksCache.remove(context.getCurrentDomain().getId());
	}
	
	public void updateContainerAndContents (Container container,UserSession context) {
		if (container.getId() == null) throw new IllegalArgumentException ("updateContainer() Container must already exist! ");
		final String containerType =  container.getClass().getSimpleName();
		this.stampObject(container,containerType, context);
		if (container.getStatus().equals(STATE_CONTAINER.STATE_CONTAINER_NEEDS_NUMBER) 
				&& container.getNumber() != null
				&& !container.getNumber().isBlank()) {
			container.setStatus(STATE_CONTAINER.STATE_CONTAINER_READY);			
		}
		containerDao.updateContainerAndContents(container, context.getCurrentDomain().getId(), containerType);
		tanksCache.remove(context.getCurrentDomain().getId());
	}
	
	public void cleanContainer(Container container, UserSession context) {
		this.stampObject(container, container.getClass().getSimpleName(), context);
		containerDao.cleanContainer(container, context.getCurrentDomain().getId(), container.getClass().getSimpleName());
		tanksCache.remove(context.getCurrentDomain().getId());
	}
	
	public List<Tank> getTanks(String domainId){
		if(tanksCache.get(domainId) == null) {
			this.loadTanks(domainId);
		}		
		return tanksCache.get(domainId);
		
	}
	
	public Tank getTank(String domainId, String tankId){
		if(tanksCache.get(domainId) == null) {
			this.loadTanks(domainId);
		}		
		return tanksCache.get(domainId).stream().filter(e -> e.getId().equals(tankId)).findFirst().get();
		
	}
	
	public List<Barrel> getBarrels(String domainId){
		if(barrelsCache.get(domainId) == null) {
			this.loadBarrels(domainId);
		}		
		return barrelsCache.get(domainId);
		
	}
	
	public void addContentToContainer (Contents contents, Container container,UserSession context) {
		container.setStatus(STATE_CONTAINER.STATE_CONTAINER_USED);
		containerDao.addContentToContainer(contents, container.getId(), context.getCurrentDomain().getId());
		this.updateContainer(container, context);
		tanksCache.remove(context.getCurrentDomain().getId());
	}
	
	public Contents loadContentsFromContainer (Container container, UserSession context) {
		
		return containerDao.loadContentsFromContainer(container,context.getCurrentDomain().getId());
	}
	
	public List<Tank> findPossibleTanks(UserSession context){
		return containerDao.findContainersByStatus(context.getCurrentDomain().getId(), Tank.class, Arrays.asList(STATE_CONTAINER.STATE_CONTAINER_READY,STATE_CONTAINER.STATE_CONTAINER_USED))
				.stream().filter(e -> e.getContents()==null || e.getContents().getVolume() < e.getVolume())
				.collect(Collectors.toList());
				
	}
	
	public List<Barrel> findPossibleBarrels(UserSession context){
		return containerDao.findContainersByStatus(context.getCurrentDomain().getId(), Barrel.class,Arrays.asList(STATE_CONTAINER.STATE_CONTAINER_READY,STATE_CONTAINER.STATE_CONTAINER_USED))
				.stream().filter(e -> e.getContents()==null || e.getContents().getVolume() < e.getVolume())
				.collect(Collectors.toList());
				
	}
	
	public List<String> getPossibleContainerTypes(UserSession context){		
		return containerDao.getContainerTypesByStatus(context.getCurrentDomain().getId(), Arrays.asList(STATE_CONTAINER.STATE_CONTAINER_READY,STATE_CONTAINER.STATE_CONTAINER_USED));
	}
	
	
	private void loadTanks(String domainId) {
		final List<Tank> tanks = containerDao.findContainersByDomainAndType(domainId, Tank.class);
		tanks.stream().forEach(e -> containerDao.loadContentsFromContainer(e, domainId));		
		tanksCache.put(domainId, tanks);
	}
	
	private void loadBarrels(String domainId) {
		final List<Barrel> barrels = containerDao.findContainersByDomainAndType(domainId, Barrel.class);
		barrels.stream().forEach(e -> containerDao.loadContentsFromContainer(e, domainId));		
		barrelsCache.put(domainId, barrels);
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
				case "amphora" : clazz=(Class<T>) Tank.class;break;
			}
			
			container = clazz.getDeclaredConstructor().newInstance();
			container.setVolume(template.getVolume());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return container;
	}
	
}
