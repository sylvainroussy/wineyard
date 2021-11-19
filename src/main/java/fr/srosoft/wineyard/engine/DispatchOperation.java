package fr.srosoft.wineyard.engine;

import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.modules.cave.TargetContainer;
import fr.srosoft.wineyard.utils.Constants.STATE_CONTAINER;
import fr.srosoft.wineyard.utils.WineyardUtils;

public class DispatchOperation extends EngineOperation{

	public void run (Container source, List<TargetContainer> targets) {
		Contents sourceContents = source.getContents();
		for (TargetContainer targetContainer : targets) {
			
			final Contents contents = new Contents();
			contents.setId(WineyardUtils.generateContentsId(targetContainer.getContainer()));
			contents.addParent(sourceContents);
			contents.setCuvee(sourceContents.getCuvee());
			contents.setVolume(targetContainer.getVolume());
			
			final Contents oldContents = targetContainer.getContainer().getContents();
			if (oldContents != null) {
				contents.addParent(oldContents);
				contents.setVolume(targetContainer.getVolume()+oldContents.getVolume());
			}
			
			targetContainer.getContainer().setContents(contents);
			sourceContents.setVolume(sourceContents.getVolume()-targetContainer.getVolume());
			
			
		}
		
		if (sourceContents.getVolume() <= 0) {			
			source.setStatus(STATE_CONTAINER.STATE_CONTAINER_NEEDS_CLEANING);
		}
	}
}
