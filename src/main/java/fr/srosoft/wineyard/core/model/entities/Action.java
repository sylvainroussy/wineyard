package fr.srosoft.wineyard.core.model.entities;

import java.util.Date;

@SuppressWarnings("serial")
public class Action extends WineyardObject{

	
	private String actionName;
	private String comment;	
	private Date date;
	private User user;
	
	
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
