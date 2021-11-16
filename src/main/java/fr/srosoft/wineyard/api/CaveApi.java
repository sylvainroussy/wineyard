package fr.srosoft.wineyard.api;

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
	
	@GetMapping ("/tanks/{domainId}")
	public List<Tank> getTanks (@PathVariable("domainId") String domainId){
		final List<Tank> tanks = containerService.getTanks("235338bffa3240cea6a5fda41927c3cd");
		// Domain Nicolas
		System.out.println("Calling /tanks/235338bffa3240cea6a5fda41927c3cd");
	
		return tanks;
	}
	
	@GetMapping ("/tanks/{domainId}/count")
	public Integer getTanksCount (@PathVariable("domainId") String domainId){
		final List<Tank> tanks = containerService.getTanks(domainId);
	
		return tanks.size();
	}

}
