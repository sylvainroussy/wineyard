package fr.srosoft.wineyard.core.process.entities;

@SuppressWarnings ("serial")
public class StepInstance extends InstanceObject {

	private String stepId;

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
}
