package fr.srosoft.wineyard.modules.cave;

import java.util.ArrayList;
import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Action;
import fr.srosoft.wineyard.core.model.entities.Container;


public abstract class CaveTransferAction {

	// For transfer Action
	protected Container sourceContainer;
	protected List<TargetContainer> targetContainers = new ArrayList<>();
	protected String destinationContainerType;
	protected List<Action> actions;

	public int getDestinationTotalVolume() {
		int volume = 0;
		for (TargetContainer targetContainer : targetContainers) {
			volume = volume + targetContainer.getVolume();
		}
		return volume;
	}

	public int getLeftContentsVolume() {
		return this.sourceContainer.getContents().getVolume() - this.getDestinationTotalVolume();
	}

	public Container getSourceContainer() {
		return sourceContainer;
	}

	public void setSourceContainer(Container sourceContainer) {
		this.sourceContainer = sourceContainer;
	}

	public List<TargetContainer> getTargetContainers() {
		return targetContainers;
	}

	public void setTargetContainers(List<TargetContainer> targetContainers) {
		this.targetContainers = targetContainers;
	}

	public String getDestinationContainerType() {
		return destinationContainerType;
	}

	public void setDestinationContainerType(String destinationContainerType) {
		this.destinationContainerType = destinationContainerType;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action) {
		if (this.actions == null) {
			this.actions = new ArrayList<>();
		}
		this.actions.add(action);
	}
}
