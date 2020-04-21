package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultTreeNode;

public class INAORegion extends DefaultTreeNode{

	private List<INAOSousRegion> sousRegions = new ArrayList<>();

	public List<INAOSousRegion> getSousRegions() {
		return sousRegions;
	}

	public void setSousRegions(List<INAOSousRegion> sousRegions) {
		this.sousRegions = sousRegions;
	}
	
	public void addSousRegion (INAOSousRegion sousRegions) {
		this.sousRegions.add(sousRegions);
	}
	
	
}
