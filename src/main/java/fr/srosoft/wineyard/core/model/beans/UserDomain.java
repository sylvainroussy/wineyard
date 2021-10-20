package fr.srosoft.wineyard.core.model.beans;

import java.io.Serializable;

import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.User;

@SuppressWarnings ("serial")
public class UserDomain implements Serializable{

	private Domain domain;
	private User user;
	private String office;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
}
