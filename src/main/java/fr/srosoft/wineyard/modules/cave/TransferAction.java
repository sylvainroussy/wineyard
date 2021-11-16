package fr.srosoft.wineyard.modules.cave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Container;

public class TransferAction {
	// For transfer Action
	private Container sourceContainer;
	private List<TargetContainer> targetContainers = new ArrayList<>();
	
	
	private List<String> actionTypes; 
	private String comment;
	private Date actionDate;
	private String user;
	
	private int targetsNumber;
	
	
	private String destinationContainerType;
	
	public int getDestinationTotalVolume() {
		int volume = 0;
		for (TargetContainer targetContainer : targetContainers) {
			volume = volume+targetContainer.getVolume();
		}
		return volume;
	}
	
	public int getLeftContentsVolume() {
		return this.sourceContainer.getContents().getVolume() - this.getDestinationTotalVolume();
	}
	
	public TransferAction(Container source) {
		this.sourceContainer = source;
	}
	
	public Container getSourceContainer() {
		return sourceContainer;
	}
	public void setSourceContainer(Container sourceContainer) {
		this.sourceContainer = sourceContainer;
	}
	

	public List<String> getActionTypes() {
		return actionTypes;
	}

	public void setActionTypes(List<String> actionTypes) {
		this.actionTypes = actionTypes;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDestinationContainerType() {
		return destinationContainerType;
	}

	public void setDestinationContainerType(String destinationContainerType) {
		this.destinationContainerType = destinationContainerType;
	}

	
	public List<TargetContainer> getTargetContainers() {
		return targetContainers;
	}

	public void setTargetContainers(List<TargetContainer> targetContainers) {
		this.targetContainers = targetContainers;
	}

	public int getTargetsNumber() {
		return targetsNumber;
	}

	public void setTargetsNumber(int targetsNumber) {
		this.targetsNumber = targetsNumber;
		
		
	}

	public static class TargetContainer{
		private Container container;
		private int volume;
		public Container getContainer() {
			return container;
		}
		public void setContainer(Container container) {
			this.container = container;
		}
		public int getVolume() {
			return volume;
		}
		public void setVolume(int volume) {
			this.volume = volume;
		}
	}
}
