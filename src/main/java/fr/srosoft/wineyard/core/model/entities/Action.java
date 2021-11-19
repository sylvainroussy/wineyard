package fr.srosoft.wineyard.core.model.entities;

import java.util.Date;

import fr.srosoft.wineyard.engine.EngineOperation;

@SuppressWarnings("serial")
public class Action extends WineyardObject{

	
	private String actionName;
	private String actionDetail;
	private String comment;	
	private Date dateSchedule;
	private Date dateDone;
	private User user;
	
	private transient EngineOperation engineOperation;
	
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getActionDetail() {
		return actionDetail;
	}
	public void setActionDetail(String actionDetail) {
		this.actionDetail = actionDetail;
	}
	public EngineOperation getEngineOperation() {
		return engineOperation;
	}
	public void setEngineOperation(EngineOperation engineOperation) {
		this.engineOperation = engineOperation;
	}
	public Date getDateSchedule() {
		return dateSchedule;
	}
	public void setDateSchedule(Date dateSchedule) {
		this.dateSchedule = dateSchedule;
	}
	public Date getDateDone() {
		return dateDone;
	}
	public void setDateDone(Date dateDone) {
		this.dateDone = dateDone;
	}
	
}
