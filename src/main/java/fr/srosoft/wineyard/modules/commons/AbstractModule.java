package fr.srosoft.wineyard.modules.commons;

import fr.srosoft.wineyard.core.session.UserSession;

public abstract class AbstractModule {

	protected UserSession context;
	
	public void loadData (UserSession context) {
		this.context = context;
	}	
	
	
	public String getIcon() {
		return null;
	}

	public UserSession getContext() {
		return context;
	}

	public void setContext(UserSession context) {
		this.context = context;
	}
}
