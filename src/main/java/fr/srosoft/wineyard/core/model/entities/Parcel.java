package fr.srosoft.wineyard.core.model.entities;

import java.util.Map;

/**
 * {type:\"Feature\",geometry:{type:geo.type, coordinates:cl3}, properties:p{.*}} AS geoNode " +
 * @author sroussy
 *
 */
public class Parcel {
	
	private String appellation;
	private String crinao;
	private String denomination;
	private Integer id;
	private Integer id_app;
	private Integer id_denom;
	private String new_insee;
	private String new_nomcom;
	private String old_insee;
	private String old_nomcom;
	private String type_ig;
	private Double[] center;
	private Integer surface;
	private String parcelNumber;
	
	private String wineyardId;
	private String parcelName;
	
	private Map<String,Object> geometry;
	
	
	public String getParcelName() {
		return parcelName;
	}
	public void setParcelName(String parcelName) {
		this.parcelName = parcelName;
	}
	public Map<String, Object> getGeometry() {
		return geometry;
	}
	public void setGeometry(Map<String, Object> geometry) {
		this.geometry = geometry;
	}
	
	public String getCrinao() {
		return crinao;
	}
	public void setCrinao(String crinao) {
		this.crinao = crinao;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_app() {
		return id_app;
	}
	public void setId_app(Integer id_app) {
		this.id_app = id_app;
	}
	public Integer getId_denom() {
		return id_denom;
	}
	public void setId_denom(Integer id_denom) {
		this.id_denom = id_denom;
	}
	public String getNew_insee() {
		return new_insee;
	}
	public void setNew_insee(String new_insee) {
		this.new_insee = new_insee;
	}
	public String getNew_nomcom() {
		return new_nomcom;
	}
	public String getAppellation() {
		return appellation;
	}
	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}
	public void setNew_nomcom(String new_nomcom) {
		this.new_nomcom = new_nomcom;
	}
	public String getOld_insee() {
		return old_insee;
	}
	public void setOld_insee(String old_insee) {
		this.old_insee = old_insee;
	}
	public String getOld_nomcom() {
		return old_nomcom;
	}
	public void setOld_nomcom(String old_nomcom) {
		this.old_nomcom = old_nomcom;
	}
	public String getType_ig() {
		return type_ig;
	}
	public void setType_ig(String type_ig) {
		this.type_ig = type_ig;
	}
	public String getParcelNumber() {
		return parcelNumber;
	}
	public void setParcelNumber(String parcelNumber) {
		this.parcelNumber = parcelNumber;
	}
	public Integer getSurface() {
		return surface;
	}
	public void setSurface(Integer surface) {
		this.surface = surface;
	}
	public String getWineyardId() {
		return wineyardId;
	}
	public void setWineyardId(String wineyardId) {
		this.wineyardId = wineyardId;
	}
	public Double[] getCenter() {
		return center;
	}
	public void setCenter(Double[] center) {
		this.center = center;
	}
	
}
