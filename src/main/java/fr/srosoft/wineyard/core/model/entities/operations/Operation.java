package fr.srosoft.wineyard.core.model.entities.operations;

import java.util.Date;

import fr.srosoft.wineyard.core.model.entities.WineyardObject;
import fr.srosoft.wineyard.core.model.entities.WineyardValue;

@SuppressWarnings("serial")
public abstract class Operation<IN  extends WineyardObject,OUT> extends WineyardObject{
	
	public static interface Operator{
		
	}
	
	public static class OperationOutput<T extends WineyardObject>{
		private T targetContainer;
		private WineyardValue value;
	}
	
	private String actionTitle;
	private String actionSummary;
	private String comment;	
	private Date dateSchedule;
	private Date dateDone;
	private String user;
	
	
	private String engineOperationClass;
	private IN input;
	private OUT output;
	
	
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public String getActionTitle() {
		return actionTitle;
	}
	public void setActionTitle(String actionTitle) {
		this.actionTitle = actionTitle;
	}
	public String getActionSummary() {
		return actionSummary;
	}
	public void setActionSummary(String actionSummary) {
		this.actionSummary = actionSummary;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getEngineOperationClass() {
		return engineOperationClass;
	}
	public void setEngineOperationClass(String engineOperationClass) {
		this.engineOperationClass = engineOperationClass;
	}
	public IN getInput() {
		return input;
	}
	public void setInput(IN input) {
		this.input = input;
	}
	public OUT getOutput() {
		return output;
	}
	public void setOutput(OUT output) {
		this.output = output;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
}
