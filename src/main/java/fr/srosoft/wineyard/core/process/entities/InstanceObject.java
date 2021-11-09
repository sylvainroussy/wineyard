package fr.srosoft.wineyard.core.process.entities;

import fr.srosoft.wineyard.core.model.entities.WineyardObject;

@SuppressWarnings("serial")
public abstract class InstanceObject extends WineyardObject implements Identifiable{
	
	protected String label;
	protected String name;
	protected String description;
	
	
	@Override
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
