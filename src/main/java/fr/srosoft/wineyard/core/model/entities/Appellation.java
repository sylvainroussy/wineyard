package fr.srosoft.wineyard.core.model.entities;

import java.util.List;

@SuppressWarnings("serial")
public class Appellation extends WineyardObject{

	private String appellation;
	private String wineColor;
	private String wineType;
	private List<Assembly> assembly;

	public String getAppellation() {
		return appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	public String getWineColor() {
		return wineColor;
	}

	public void setWineColor(String wineColor) {
		this.wineColor = wineColor;
	}

	public String getWineType() {
		return wineType;
	}

	public void setWineType(String wineType) {
		this.wineType = wineType;
	}

	public List<Assembly> getAssembly() {
		return assembly;
	}

	public void setAssembly(List<Assembly> assembly) {
		this.assembly = assembly;
	}
	
	
}
