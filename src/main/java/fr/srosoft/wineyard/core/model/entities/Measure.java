package fr.srosoft.wineyard.core.model.entities;

import java.util.Date;

import fr.srosoft.wineyard.utils.Constants.MEASURE_TYPE;

@SuppressWarnings("serial")
public class Measure  extends WineyardObject{
	
	private MEASURE_TYPE measureType;
	private String name;
	private Float value;
	private String unit;
	private String comment;
	private Date measureDate = new Date();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getMeasureDate() {
		return measureDate;
	}
	public void setMeasureDate(Date measureDate) {
		this.measureDate = measureDate;
	}
	public MEASURE_TYPE getMeasureType() {
		return measureType;
	}
	public void setMeasureType(MEASURE_TYPE measureType) {
		this.measureType = measureType;
	}

}
