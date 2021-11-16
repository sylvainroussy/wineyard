package fr.srosoft.wineyard.core.unit;

public class Bottle {

	public static final float LITER_VALUE = 0.75f;
	
	public static int numberOfBottlesForHl (int hl) {
		return Double.valueOf (Math.floor((Liter.HECTO_LITER / LITER_VALUE) * hl)).intValue();
		
	}
	
	
}
