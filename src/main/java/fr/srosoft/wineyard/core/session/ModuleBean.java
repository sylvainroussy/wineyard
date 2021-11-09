package fr.srosoft.wineyard.core.session;

import java.io.Serializable;

import fr.srosoft.wineyard.modules.commons.AbstractModule;

@SuppressWarnings ("serial")
public class ModuleBean implements Serializable, Comparable<ModuleBean>{
	
	private String moduleName;
	private AbstractModule module;
	private String label;
	private String description;	
	private Integer order;
	
	public AbstractModule getModule() {
		return module;
	}
	public void setModule(AbstractModule module) {
		this.module = module;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Override
	public int compareTo(ModuleBean o) {
		
		return this.order.compareTo(o.getOrder());
	}
	
	
}
