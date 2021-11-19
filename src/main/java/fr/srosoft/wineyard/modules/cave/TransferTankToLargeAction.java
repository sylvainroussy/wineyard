package fr.srosoft.wineyard.modules.cave;

import java.util.Date;
import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Container;

public class TransferTankToLargeAction extends CaveTransferAction{
	
	
	
	private List<String> actionTypes; 
	private String comment;
	private Date actionDate;
	private String user;
	
	
	
	
	public TransferTankToLargeAction(Container source) {
		this.sourceContainer = source;
	}
	
	@Override
	public Container getSourceContainer() {
		return sourceContainer;
	}
	@Override
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

	@Override
	public String getDestinationContainerType() {
		return destinationContainerType;
	}

	@Override
	public void setDestinationContainerType(String destinationContainerType) {
		this.destinationContainerType = destinationContainerType;
	}

	
	
	
}
