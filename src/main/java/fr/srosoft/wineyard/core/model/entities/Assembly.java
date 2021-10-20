package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;

@SuppressWarnings ("serial")
public class Assembly implements Serializable{
	private String grape;
	private Integer proportion;
	
	public String getGrape() {
		return grape;
	}
	public void setGrape(String grape) {
		this.grape = grape;
	}
	public Integer getProportion() {
		return proportion;
	}
	public void setProportion(Integer proportion) {
		this.proportion = proportion;
	}
}
