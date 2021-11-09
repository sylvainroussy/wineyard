package fr.srosoft.wineyard.utils;

import java.util.Date;

import fr.srosoft.wineyard.core.model.entities.WineyardObject;

public class WineyardUtils {

	
	public static void stamp(WineyardObject object, boolean isNew, String actor) {
		if (isNew) {
			object.setCreationDate(new Date());
			object.setCreationUser(actor);
		}
		
		object.setLastUpdateDate(new Date());
		object.setLastUpdateUser(actor);
		
		
	}
	

}
