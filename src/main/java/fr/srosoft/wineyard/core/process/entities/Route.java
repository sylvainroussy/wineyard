package fr.srosoft.wineyard.core.process.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

@SuppressWarnings ("serial")
public class Route  extends ProcessObject{
	private Step outputStep;
	private int order;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@JsonBackReference
	public Step getOutputStep() {
		return outputStep;
	}

	public void setOutputStep(Step outputStep) {
		this.outputStep = outputStep;
	}
}
