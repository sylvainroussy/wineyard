package fr.srosoft.wineyard.core.process.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@SuppressWarnings ("serial")
public class Step extends ProcessObject{
	
	protected List<Route> outputRoutes;
	
	@JsonManagedReference
	public List<Route> getOutputRoutes() {
		return outputRoutes;
	}
	public void setOutputRoutes(List<Route> outputRoutes) {
		this.outputRoutes = outputRoutes;
	}
	
	public void addOutputRoute(Route outputRoute) {
		if (outputRoutes == null) {
			outputRoutes = new ArrayList<>();
		}
		this.outputRoutes.add(outputRoute);
	}
	
}
