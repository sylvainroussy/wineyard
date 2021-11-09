package fr.srosoft.wineyard.core.model.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Stock implements Serializable{

	private int quantity;
	private String unit;
	private String containerType;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getContainerType() {
		return containerType;
	}
	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}
}
