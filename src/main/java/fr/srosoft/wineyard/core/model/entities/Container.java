package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Container {
	private String id;
	private String number;
	private Integer volume = 0;
	private String year;
	
	private Contents contents;
	private List<Action> actions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Contents getContents() {
		return contents;
	}

	public void setContents(Contents contents) {
		this.contents = contents;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action) {
		if (this.actions == null) this.actions = new ArrayList<>();
		this.actions.add(action);
	}
}
