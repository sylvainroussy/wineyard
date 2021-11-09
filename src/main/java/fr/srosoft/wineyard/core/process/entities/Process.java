package fr.srosoft.wineyard.core.process.entities;

@SuppressWarnings ("serial")
public  class Process extends ProcessObject{

	protected Float version;
	protected Step startPoint;

	protected boolean enabled = true;
		
	public Float getVersion() {
		return version;
	}
	public void setVersion(Float version) {
		this.version = version;
	}
	
	public Step getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Step startPoint) {
		this.startPoint = startPoint;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
	
}
