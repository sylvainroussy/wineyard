package fr.srosoft.wineyard.core.process.entities;

public interface Identifiable {
	String getId();
	String getLabel();
	String getName();
	String getDescription();
	
	void setId(String id);
	void setLabel(String label);
	void setName(String name);
	void setDescription(String description);
}
