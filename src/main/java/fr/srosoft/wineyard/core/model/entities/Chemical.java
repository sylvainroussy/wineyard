package fr.srosoft.wineyard.core.model.entities;

@SuppressWarnings("serial")
public class Chemical extends WineyardObject{

	private String commonName;
	private String infos;
	private boolean biocompatible;
	
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public String getInfos() {
		return infos;
	}
	public void setInfos(String infos) {
		this.infos = infos;
	}
	public boolean isBiocompatible() {
		return biocompatible;
	}
	public void setBiocompatible(boolean biocompatible) {
		this.biocompatible = biocompatible;
	}
}
