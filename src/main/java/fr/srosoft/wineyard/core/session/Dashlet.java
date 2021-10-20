package fr.srosoft.wineyard.core.session;

import fr.srosoft.wineyard.modules.commons.AbstractModule;

public abstract class Dashlet<T extends AbstractModule> {

	protected T module;
	
	public Dashlet (T module) {
		this.module = module;
	}
}
