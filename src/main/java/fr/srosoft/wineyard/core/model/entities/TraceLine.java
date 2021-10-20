package fr.srosoft.wineyard.core.model.entities;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class TraceLine extends WineyardObject{

	private List<Measure> measures = new ArrayList<>();
	private List<Trace> traces = new ArrayList<>();
	
	
	public List<Trace> getTraces() {
		return traces;
	}
	public void setTraces(List<Trace> traces) {
		this.traces = traces;
	}
	public void addTrace(Trace trace) {
		this.traces.add(trace);
	}
	
	public List<Measure> getMeasures() {
		return measures;
	}
	public void setMeasures(List<Measure> measures) {
		this.measures = measures;
	}
	public void addMeasure (Measure measure) {
		this.measures.add(measure);
	}
	
}
