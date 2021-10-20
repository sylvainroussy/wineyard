package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Contents  extends WineyardObject{

	private String step;
	private String year;
	private Appellation appellation;
	private List<String> grapes;
	private Integer volume;
	private String color;
	
	private TraceLine traceLine = new TraceLine();
	private List<Contents> parents;
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
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
	public Appellation getAppellation() {
		return appellation;
	}
	public void setAppellation(Appellation appellation) {
		this.appellation = appellation;
	}
	public TraceLine getTraceLine() {
		return traceLine;
	}
	public void setTraceLine(TraceLine traceLine) {
		this.traceLine = traceLine;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public List<Contents> getParents() {
		return parents;
	}
	public void setParents(List<Contents> parents) {
		this.parents = parents;
	}
}
