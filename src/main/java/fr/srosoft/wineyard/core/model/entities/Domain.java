package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class Domain implements Serializable{
	
	private String id;
	private String domainName;
	private String info;
	private List<String> phones = new ArrayList<String>();
	private String fax;
	private String contact;
	private String website;
	private String zipCode;
	private String city;
	private String address;
	private double[] coords;
	private String dataSource;
	private List<Appellation> appellations;
	
	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<String> getPhones() {
		return phones;
	}
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double[] getCoords() {
		return coords;
	}
	public void setCoords(double[] coords) {
		this.coords = coords;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public List<Appellation> getAppellations() {
		return appellations;
	}
	public void setAppellations(List<Appellation> appellations) {
		this.appellations = appellations;
	}
	public void addAppellation(Appellation appellation) {
		if (this.appellations == null) this.appellations = new ArrayList<>();
		this.appellations.add(appellation);
	}
	
	@Override
	public String toString() {
		return "VitiInfo [domainName=" + domainName + ", info=" + info + ", phones=" + phones + ", fax=" + fax
				+ ", contact=" + contact + ", website=" + website + ", zipCode=" + zipCode + ", city=" + city
				+ ", address=" + address + ", coords=" + Arrays.toString(coords) + "]";
	}
}
