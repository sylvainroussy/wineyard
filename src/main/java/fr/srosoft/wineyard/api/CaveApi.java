package fr.srosoft.wineyard.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.services.CaveService;

@RestController
@RequestMapping("api")
public class CaveApi {
	@Resource
	private CaveService caveService;
	
	@GetMapping ("/tanks/{domainId}")
	public List<Tank> getTanks (@PathVariable("domainId") String domainId){
		final List<Tank> tanks = caveService.getTanks(domainId);
	
		return tanks;
	}
	
	@GetMapping ("/tanks/{domainId}/count")
	public Integer getTanksCount (@PathVariable("domainId") String domainId){
		final List<Tank> tanks = caveService.getTanks(domainId);
	
		return tanks.size();
	}

}
