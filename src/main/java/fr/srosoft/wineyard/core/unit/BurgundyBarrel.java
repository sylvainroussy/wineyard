package fr.srosoft.wineyard.core.unit;

/**
 * Piece
 * @author sroussy
 *
 */
public class BurgundyBarrel {

	public static final int LITER_VALUE = 226;
	
	public float getBottles(int numberOfBarrels) {
		return (LITER_VALUE * Bottle.LITER_VALUE) *  numberOfBarrels;
	}
	
	public float getLiters(int numberOfBarrels) {
		return LITER_VALUE * numberOfBarrels;
	}
	
	public float getHectoLiters(int numberOfBarrels) {
		return (LITER_VALUE / Liter.HECTO_LITER) * numberOfBarrels;
	}
}
