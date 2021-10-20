package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;
import java.util.Date;

public class WineyardObject implements Serializable, Comparable<WineyardObject>{

	protected String id;
	
	protected Date creationDate;
	
	protected String creationUser;
	
	protected Date lastUpdateDate;
	protected String lastUpdateUser;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreationUser() {
		return creationUser;
	}
	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	@Override
	public int compareTo(WineyardObject o) {
		
		return this.creationDate.compareTo(o.creationDate);
	}
	
	
	
	
}
