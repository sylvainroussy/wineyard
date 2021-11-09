package fr.srosoft.wineyard.core.unit;

public class Carton {
	
	protected int number;
	

	public int getBottles(int numberOfCartons) {
		return 6 * numberOfCartons;
	}
	
	public float getLiters(int numberOfCartons) {
		return (6 * Bottle.LITER_VALUE) * numberOfCartons;
	}
	
	public float getHectoLiters(int numberOfCartons) {
		return (((6 * Bottle.LITER_VALUE) * numberOfCartons ) / Liter.HECTO_LITER);
	}
}
