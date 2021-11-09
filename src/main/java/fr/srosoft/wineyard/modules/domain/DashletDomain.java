package fr.srosoft.wineyard.modules.domain;

import fr.srosoft.wineyard.core.session.Dashlet;

public class DashletDomain extends Dashlet<DomainModule>{
	
	public static final String MODE_READ="read";
	public static final String MODE_EDIT="edit";
	
	public DashletDomain(DomainModule module) {
		super(module);
		
	}

	
	
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
	
	public void saveDomain() {
		module.directoryService.saveDomain(module.getCurrentDomain(), module.getContext());
		this.mode = MODE_READ;
	}
	
	
	
		
	
}
