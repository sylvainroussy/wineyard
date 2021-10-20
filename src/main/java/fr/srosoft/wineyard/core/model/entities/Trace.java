package fr.srosoft.wineyard.core.model.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Trace extends WineyardObject{
	private String name;
	private String comment;	
	private String type;
	private Date traceDate;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getTraceDate() {
		return traceDate;
	}
	public void setTraceDate(Date traceDate) {
		this.traceDate = traceDate;
	}
	
	public String getHumanDate () {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.creationDate);
	}
}
