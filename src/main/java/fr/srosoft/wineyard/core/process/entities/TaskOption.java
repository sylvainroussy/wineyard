package fr.srosoft.wineyard.core.process.entities;

import fr.srosoft.wineyard.core.model.entities.WineyardObject;

@SuppressWarnings ("serial")
public class TaskOption extends WineyardObject{
	private String name;
	private String label;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
