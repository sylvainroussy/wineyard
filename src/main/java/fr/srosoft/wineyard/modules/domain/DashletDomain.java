package fr.srosoft.wineyard.modules.domain;

import fr.srosoft.wineyard.core.model.entities.Domain;

public class DashletDomain {
	
	public static final String MODE_READ="read";
	public static final String MODE_EDIT="edit";
	
	/**
	 * Modes : "edit" or "read"
	 */
	private String mode = MODE_READ;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public void changeMode (String mode) {
		this.mode = mode;
	}
	
	public void saveDomain(Domain domain) {
		
		this.mode = MODE_READ;
	}
	
	
}
