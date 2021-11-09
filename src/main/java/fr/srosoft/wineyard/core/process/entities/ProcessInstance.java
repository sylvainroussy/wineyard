package fr.srosoft.wineyard.core.process.entities;

@SuppressWarnings("serial")
public abstract class ProcessInstance<T> extends InstanceObject {

	protected String processId;
	protected String domainId;
	protected StepInstance currentStep;
	protected String title;
	
	protected T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public StepInstance getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(StepInstance currentStep) {
		this.currentStep = currentStep;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
