package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Region implements Serializable{
	
	private String id;
	private String label;
	private String name;
	private Country country;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
}
