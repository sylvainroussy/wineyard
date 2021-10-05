package fr.srosoft.wineyard.modules.commons;

import fr.srosoft.wineyard.core.session.UserSession;

public abstract class AbstractModule {

	public abstract void loadData (UserSession context);
	
	public abstract String getMainPage ();
}
