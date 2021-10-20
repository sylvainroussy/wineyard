package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class User implements Serializable {

	private String id;
	private String email;
	private String displayName;
	private List<String> phones;
	private boolean enabled = true;
	private List<LinkedDomain> linkedDomains;
	
	@JsonIgnore
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getPhones() {
		return phones;
	}
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public List<LinkedDomain> getLinkedDomains() {
		return linkedDomains;
	}
	public void setLinkedDomains(List<LinkedDomain> linkedDomains) {
		this.linkedDomains = linkedDomains;
	}
	public void addLinkedDomain(LinkedDomain linkedDomain) {
		if (this.linkedDomains == null) this.linkedDomains = new ArrayList<>();
		this.linkedDomains.add(linkedDomain);
	}
	
	
}
