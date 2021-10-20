package fr.srosoft.wineyard.core.model.entities;

public class LinkedDomain {

	private Domain domain;
	private String office;
		
	public LinkedDomain() {
		super();
		
	}
	
	public LinkedDomain(Domain domain, String function) {
		super();
		this.domain = domain;
		this.office = function;
	}
	public Domain getDomain() {
		return domain;
	}
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	
}
