package fr.srosoft.wineyard.modules.gis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.srosoft.wineyard.core.model.dao.GisDao;
import fr.srosoft.wineyard.core.model.entities.City;
import fr.srosoft.wineyard.core.model.entities.City.Denomination;

@Component("gisView")
@Scope("session")
public class GISView {
	private static final String NODE_TYPE_VILLE = "ville";
	private static final String NODE_TYPE_VILLE_AOC = "ville_aoc";

	private static final String NODE_TYPE_AOC = "aoc";

	private final static Logger LOGGER = LoggerFactory.getLogger(GISView.class);

	@Resource
	private GisDao featureDao;

	private List<City> allCities;
	private Map<String, List<String>> allCrinaos;
	private Map<String, List<Map<String, List<String>>>> crinaos;
	private Set<String> selectedCrinaos;

	private TreeNode aocTree = new DefaultTreeNode("AOC", null);
	private TreeNode citiesTree = new DefaultTreeNode("CITIES", null);

	private Feature currentFeature;
	
	private Boolean focusEnabled = Boolean.TRUE;

	@PostConstruct
	public void initView() {
		this.loadAllCrinaos();
		this.loadAllCities();
		this.loadAOCTree();
		this.loadCitiesTree();
	}

	private void loadAllCrinaos() {
		allCrinaos = featureDao.getAllCrinao();
		crinaos = featureDao.getAll();
		LOGGER.info("CRINAOS loaded");
	}

	private void loadAllCities() {
		allCities = featureDao.getAllCities();

		LOGGER.info("CITIES loaded");
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

	private void loadCitiesTree() {
		for (City city : allCities) {
			final TreeNode level1 = new DefaultTreeNode(city, citiesTree);
			level1.setType(NODE_TYPE_VILLE);
			level1.setSelectable(false);
			for (Denomination denomination : city.getDenominations()) {
				TreeNode level2 = new DefaultTreeNode(denomination, level1);
				level2.setType(NODE_TYPE_VILLE_AOC);
			}
		}
	}

	public void onNodeSelect(NodeSelectEvent event) throws Exception {

		final TreeNode selectedNode = event.getTreeNode();
		System.out.println("NODE : " + selectedNode.getData());
		List<Map<String, ?>> result = null;
		if (selectedNode.getType().equals(NODE_TYPE_AOC)) {
			

			 result = featureDao.getDenomination("" + selectedNode.getData());
			

		} else if (selectedNode.getType().equals(NODE_TYPE_VILLE_AOC)) {
			
			final Denomination denomination = (Denomination) selectedNode.getData();
			result = featureDao.getCity(denomination.getInsee(), denomination.getDenomination());
			
		}
		
		if (result != null) {
			System.out.println("NODE : " + selectedNode.getData() + " LOADED " + result.size());
			ObjectMapper mapper = new ObjectMapper();
			String data = mapper.writeValueAsString(result);
			this.currentFeature = new Feature(selectedNode.getData(), data);
		}
		/*
		 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 * "Selected", event.getTreeNode().toString());
		 * FacesContext.getCurrentInstance().addMessage(null, message);
		 */
	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		final TreeNode selectedNode = event.getTreeNode();
		System.out.println("NODE unselect : " + selectedNode.getData());
		this.currentFeature = new Feature(selectedNode.getData(), null);
		
		/*
		 * FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 * "Unselected", event.getTreeNode().toString());
		 * FacesContext.getCurrentInstance().addMessage(null, message);
		 */
	}

	public Set<String> getSelectedCrinaos() {
		return selectedCrinaos;
	}

	public void setSelectedCrinaos(Set<String> selectedCrinaos) {
		this.selectedCrinaos = selectedCrinaos;
	}

	public Feature getCurrentFeature() {
		return currentFeature;
	}

	public void setCurrentFeature(Feature currentFeature) {
		this.currentFeature = currentFeature;
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

	public Boolean getFocusEnabled() {
		return focusEnabled;
	}

	public void setFocusEnabled(Boolean focusEnabled) {
		this.focusEnabled = focusEnabled;
		LOGGER.info ("setFocusEnabled() set to "+focusEnabled);
		System.out.println("setFocusEnabled() set to "+focusEnabled);
	}

	@SuppressWarnings("serial")
	public static class Feature implements Serializable {
		private String name;
		private String id;
		private String data;

		public Feature(String name, String data) {
			super();
			this.name = name;
			this.id = name;
			this.data = data;
		}

		public Feature(City city, String data) {
			super();
			this.name = city.getNewNomCom();
			this.id = city.getNewInsee();
			this.data = data;
		}
		
		public Feature(Object object, String data) {
			super();
			if (object instanceof City) {
				this.name = ((City)object).getNewNomCom();
				this.id = ((City)object).getNewInsee();
			}
			else if (object instanceof Denomination) {
				this.name = ((Denomination)object).getDenomination();
				this.id = ((Denomination)object).getInsee()+"_"+((Denomination)object).getDenomination();
			}
			else {
				this.name = ""+object;
				this.id = ""+object;
			}
		
			this.data = data;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

	}
}
