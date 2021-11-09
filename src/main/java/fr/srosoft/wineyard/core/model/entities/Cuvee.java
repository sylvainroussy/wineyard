package fr.srosoft.wineyard.core.model.entities;

import fr.srosoft.wineyard.core.process.entities.InstanceObject;

@SuppressWarnings("serial")
public class Cuvee extends InstanceObject{

	private Millesime millesime;
	private Appellation appellation;
	
	public Millesime getMillesime() {
		return millesime;
	}
	public void setMillesime(Millesime millesime) {
		this.millesime = millesime;
	}
	public Appellation getAppellation() {
		return appellation;
	}
	public void setAppellation(Appellation appellation) {
		this.appellation = appellation;
	}
	
	
}
