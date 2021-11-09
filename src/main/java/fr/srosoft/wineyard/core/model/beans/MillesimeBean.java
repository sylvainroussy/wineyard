package fr.srosoft.wineyard.core.model.beans;

import java.io.Serializable;
import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Millesime;

@SuppressWarnings("serial")
public class MillesimeBean implements Serializable{

	private Millesime millesime;
	private List<Cuvee> cuvees;
}
