package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Contents is a part of a container registered with Cuvee
 * @author sroussy
 *
 */
@SuppressWarnings("serial")
public class Contents  extends WineyardObject{

	private Cuvee cuvee;	
	private Integer volume;	
	
	private TraceLine traceLine = new TraceLine();
	private List<Contents> parents;
	private String currentState;
	
	
	
	
	
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	public TraceLine getTraceLine() {
		return traceLine;
	}
	public void setTraceLine(TraceLine traceLine) {
		this.traceLine = traceLine;
	}
	
	public List<Contents> getParents() {
		return parents;
	}
	public void setParents(List<Contents> parents) {
		this.parents = parents;
	}
	public void addParent (Contents parent) {
		if (this.parents == null) {
			this.parents = new ArrayList<>();
		}
		this.parents.add(parent);
	}
	
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public Cuvee getCuvee() {
		return cuvee;
	}
	public void setCuvee(Cuvee cuvee) {
		this.cuvee = cuvee;
	}
}
