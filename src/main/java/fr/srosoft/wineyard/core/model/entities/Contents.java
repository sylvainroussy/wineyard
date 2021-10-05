package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Contents {

	private String id;
	private String year;
	private String appelation;
	private List<String> grapes;
	private Integer volume;
	private String color;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getAppelation() {
		return appelation;
	}
	public void setAppelation(String appelation) {
		this.appelation = appelation;
	}
	
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public List<String> getGrapes() {
		return grapes;
	}
	public void setGrapes(List<String> grapes) {
		this.grapes = grapes;
	}
	
	public void addGrape(String grape) {
		if (this.grapes == null) this.grapes  = new ArrayList<>();
		this.grapes.add(grape);
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
