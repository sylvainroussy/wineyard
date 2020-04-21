package fr.srosoft.wineyard.core.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings ("serial")
public class City implements Serializable, Comparable<City>{

	private String newNomCom;
	private String newInsee;
	private String postalCode;
	private List<Denomination> denominations = new ArrayList<City.Denomination>();
	
	public City() {
		
	}
	
	
	
	public City(String newNomCom, String newInsee) {
		super();
		this.newNomCom = newNomCom;
		this.newInsee = newInsee;
	}
	
	public City(String newNomCom, String newInsee, List<String> sdenominations) {
		super();
		this.newNomCom = newNomCom;
		this.newInsee = newInsee;
		
		for (String string : sdenominations) {
			final Denomination denom = new Denomination(newInsee,newNomCom, string);
			this.denominations.add(denom);
		}
	
	}



	public String getNewNomCom() {
		return newNomCom;
	}
	public void setNewNomCom(String newNomCom) {
		this.newNomCom = newNomCom;
	}
	public String getNewInsee() {
		return newInsee;
	}
	public void setNewInsee(String newInsee) {
		this.newInsee = newInsee;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public List<Denomination> getDenominations() {
		return denominations;
	}
	public void setDenominations(List<Denomination> denominations) {
		this.denominations = denominations;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newInsee == null) ? 0 : newInsee.hashCode());
		result = prime * result + ((newNomCom == null) ? 0 : newNomCom.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (newInsee == null) {
			if (other.newInsee != null)
				return false;
		} else if (!newInsee.equals(other.newInsee))
			return false;
		if (newNomCom == null) {
			if (other.newNomCom != null)
				return false;
		} else if (!newNomCom.equals(other.newNomCom))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "City [newNomCom=" + newNomCom + ", newInsee=" + newInsee + ", postalCode=" + postalCode + "]";
	}



	@Override
	public int compareTo(City arg0) {
		
		return this.newNomCom.compareTo(arg0.getNewNomCom());
	}
	
	
	
	public static class Denomination implements Serializable{
		private String insee;
		private String newNomCom;
		private String denomination;
		
		
		public Denomination() {
			super ();
		}
		
		public Denomination(String insee,String newNomCom, String denomination) {
			super();
			this.insee = insee;
			this.denomination = denomination;
			this.newNomCom = newNomCom;
		}
		public String getInsee() {
			return insee;
		}
		public void setInsee(String insee) {
			this.insee = insee;
		}
		public String getDenomination() {
			return denomination;
		}
		public void setDenomination(String denomination) {
			this.denomination = denomination;
		}

		public String getNewNomCom() {
			return newNomCom;
		}

		public void setNewNomCom(String newNomCom) {
			this.newNomCom = newNomCom;
		}
	}
	
	
}
