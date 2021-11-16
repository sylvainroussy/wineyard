package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

import fr.srosoft.wineyard.utils.Constants.STATE_CONTAINER;

@SuppressWarnings("serial")
public class Container  extends WineyardObject{
	
	private String number;
	private Integer volume = 0;
	private String year;
	
	private Contents contents;
	private List<Action> actions;
	
	private STATE_CONTAINER status = STATE_CONTAINER.STATE_CONTAINER_NEEDS_NUMBER;

	

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

	public STATE_CONTAINER getStatus() {
		return status;
	}

	public void setStatus(STATE_CONTAINER status) {
		this.status = status;
	}

	
}
