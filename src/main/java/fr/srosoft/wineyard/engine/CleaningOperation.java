package fr.srosoft.wineyard.engine;

import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.utils.Constants.STATE_CONTAINER;

public class CleaningOperation {

	public void run(Container source) {
		source.setStatus(STATE_CONTAINER.STATE_CONTAINER_READY);
	}
}
