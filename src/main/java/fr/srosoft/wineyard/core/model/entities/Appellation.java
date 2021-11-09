package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * LOAD CSV FROM
'file:///grapes.csv'
AS line FIELDTERMINATOR ';'
WITH apoc.text.clean (line[0]) AS id,line[0] As commonName, line[1] AS color, line[2] As synonyms
MERGE (grape:Grape{id:id})
SET grape.color=color, grape.synonims=synonyms, grape.dataSource="AnnexeBOagrimars20.pdf",
grape.creationDate=timestamp(),grape.creationUser ="System", grape.lastUpdateDate=timestamp(), grape.lastUpdateUser="System", grape.commonName=commonName
RETURN id
 * @author sroussy
 *
 */
@SuppressWarnings("serial")
public class Appellation extends WineyardObject{

	private String appellation;
	private String wineColor;
	private String wineType;
	private List<Blend> blends = new ArrayList<>();
	private String label;

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

	public List<Blend> getBlends() {
		return blends;
	}

	public void setBlends(List<Blend> blends) {
		this.blends = blends;
	}
	
	public void addBlend(Blend blend) {
		if (this.blends == null) {
			this.blends = new ArrayList<>();
		}
		this.blends.add(blend);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
