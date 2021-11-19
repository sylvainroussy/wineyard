package fr.srosoft.wineyard.modules.cave;

import java.util.Date;
import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Container;

public class TransferTankToBarrelsAction extends CaveTransferAction{
	
	
	
	private List<String> actionTypes; 
	private String comment;
	private Date actionDate;
	private String user;
	
	private int targetsNumber;
	
	
	private String destinationContainerType;
	
	
	
	public TransferTankToBarrelsAction(Container source) {
		this.sourceContainer = source;
		this.destinationContainerType="Barrel";
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

	
	

	public int getTargetsNumber() {
		return targetsNumber;
	}

	public void setTargetsNumber(int targetsNumber) {
		this.targetsNumber = targetsNumber;
		
		
	}

	
}
