package fr.srosoft.wineyard.api;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.services.ContainerService;

@RestController
@RequestMapping("api")
public class CaveApi {
	@Resource
	private ContainerService containerService;
	
	private static final String TEMP_DOMAIN_ID = "36b098dbff476adaa49f2647f33e76e0";
	
	@GetMapping ("/tanks/{domainId}")
	public List<Tank> getTanks (@PathVariable("domainId") String domainId){
		final List<Tank> tanks = containerService.getTanks(TEMP_DOMAIN_ID);
		// Domain Lafouge
		System.out.println("Calling (Lafouge) /tanks/"+TEMP_DOMAIN_ID);
	
		return tanks;
	}
	
	@GetMapping ("/tanks/{domainId}/{tankId}")
	public List<Tank> getTank (@PathVariable("domainId") String domainId,@PathVariable("tankId") String tankId){
		final Tank tank = containerService.getTank(TEMP_DOMAIN_ID, tankId);
		// Domain Lafouge
		System.out.println("Calling (Lafouge) /tanks/"+TEMP_DOMAIN_ID+"/"+tankId);
	
		return Arrays.asList(tank);
	}
	
	@GetMapping ("/tanks/{domainId}/count")
	public Integer getTanksCount (@PathVariable("domainId") String domainId){
		final List<Tank> tanks = containerService.getTanks(domainId);
	
		return tanks.size();
	}

}
