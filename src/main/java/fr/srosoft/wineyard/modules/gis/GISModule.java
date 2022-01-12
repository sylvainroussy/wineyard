package fr.srosoft.wineyard.modules.gis;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.services.GISService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="GISModule", 
			description="Goegraphic Information System", 
			label="GIS",
			order=50)
public class GISModule extends AbstractModule{
	
	private static final String NODE_TYPE_VILLE = "ville";
	private static final String NODE_TYPE_VILLE_AOC = "ville_aoc";

	private static final String NODE_TYPE_AOC = "aoc";
		
	@Resource
	private GISService gisService;
	
	private Map<String, List<Map<String, List<String>>>> crinaos;

	
	private TreeNode aocTree = new DefaultTreeNode("AOC", null);
	private TreeNode citiesTree = new DefaultTreeNode("CITIES", null);
	
	@PostConstruct
	public void init () {
		
	}


	@Override
	public void loadData(UserSession context) {
	
		super.loadData(context);
		crinaos = this.gisService.findCrinaosByRegionIds(context.getCurrentDomain().getRegionIds());
		this.loadAOCTree();
	}
	
	@Override
	public String getIcon() {
		return "pi pi-map-marker";
	}

	private void loadAOCTree() {
		for (Map.Entry<String, List<Map<String, List<String>>>> entry : crinaos.entrySet()) {
			final TreeNode level1 = new DefaultTreeNode(entry.getKey(), aocTree);
			level1.setType("region");
			level1.setSelectable(false);
			for (Map<String, List<String>> appellations : entry.getValue()) {
				for (Map.Entry<String, List<String>> appellation : appellations.entrySet()) {
					final TreeNode level2 = new DefaultTreeNode(appellation.getKey(), level1);
					level2.setType("sousRegion");
					level2.setSelectable(false);
					for (String denomination : appellation.getValue()) {
						final TreeNode level3 = new DefaultTreeNode(denomination, level2);
						level3.setType(NODE_TYPE_AOC);
					}
				}
			}

		}
	}

	public Map<String, List<Map<String, List<String>>>> getCrinaos() {
		return crinaos;
	}


	public void setCrinaos(Map<String, List<Map<String, List<String>>>> crinaos) {
		this.crinaos = crinaos;
	}


	public TreeNode getAocTree() {
		return aocTree;
	}


	public void setAocTree(TreeNode aocTree) {
		this.aocTree = aocTree;
	}


	public TreeNode getCitiesTree() {
		return citiesTree;
	}


	public void setCitiesTree(TreeNode citiesTree) {
		this.citiesTree = citiesTree;
	}


	
	
}
