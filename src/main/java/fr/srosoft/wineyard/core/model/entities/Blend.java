package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;

@SuppressWarnings ("serial")
public class Blend implements Serializable{
	private String id;
	private Grape grape;
	private int proportion;
	
	
	public int getProportion() {
		return proportion;
	}
	public void setProportion(int proportion) {
		this.proportion = proportion;
	}
	public Grape getGrape() {
		return grape;
	}
	public void setGrape(Grape grape) {
		this.grape = grape;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
