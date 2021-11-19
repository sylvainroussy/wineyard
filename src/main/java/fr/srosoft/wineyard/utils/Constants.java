package fr.srosoft.wineyard.utils;

public class Constants {

	// Etats de vinification
	public static final String STATE_WAITING_ALCOHOLIC_FERMENTATION="En attente de fermentation alcoolique";
	public static final String STATE_IN_PROGRESS_ALCOHOLIC_FERMENTATION="En cours de fermentation alcoolique";
	public static final String STATE_FINISHED_ALCOHOLIC_FERMENTATION="Fermentation alcoolique achevée";
	
	public static final String STATE_WAITING_MALO_FERMENTATION="En attente de fermentation malolactique";
	public static final String STATE_IN_PROGRESS_MALO_FERMENTATION="En cours de fermentation malolactique";
	public static final String STATE_FINISHED_MALO_FERMENTATION="Fermentation malolactique achevée";
	
	
	public static enum STATE_CONTAINER{
		
		STATE_CONTAINER_READY,
		STATE_CONTAINER_NEEDS_CLEANING,
		STATE_CONTAINER_USED,
		STATE_CONTAINER_BROKEN,
		STATE_CONTAINER_UNDER_MAINTENANCE,
		STATE_CONTAINER_NEEDS_NUMBER,
		STATE_CONTAINER_DISABLED
	}	
	
	
	
}
