package fr.srosoft.wineyard.core.model.entities;

@SuppressWarnings("serial")
public class ContainerTemplate extends WineyardObject{

	private String label;
	private String model;
	private String description;
	
	

	/**
	 * Barrel, Tank, 
	 */
	private String type;
	
	/**
	 * Inox, cement, oak, etc.
	 */
	private String material;
	private int volume;
	private String unit;
	
	private String supplier;

	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
