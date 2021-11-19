package fr.srosoft.wineyard.modules.cave;

import java.io.Serializable;

import fr.srosoft.wineyard.core.model.entities.Container;

@SuppressWarnings("serial")
public class TargetContainer implements Serializable{
	private Container container;
	// tranferred volume
	private int volume;
	private int leftVolume;

	public TargetContainer() {
	}
	
	public TargetContainer(Container container, int leftVolume) {
		this.container = container;
		this.leftVolume = leftVolume;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getLeftVolume() {
		return leftVolume;
	}

	public void setLeftVolume(int leftVolume) {
		this.leftVolume = leftVolume;
	}
}