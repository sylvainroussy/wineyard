package fr.srosoft.wineyard.core.model.entities;

@SuppressWarnings ("serial")
public class Grape extends WineyardObject {
	
	private String commonName;
	private String synonims;
	private String color;
	private String family;
	private String dataSource;
	
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSynonims() {
		return synonims;
	}
	public void setSynonims(String synonims) {
		this.synonims = synonims;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
}
