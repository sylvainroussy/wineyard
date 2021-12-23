package fr.srosoft.wineyard.core.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Millesime;

@SuppressWarnings("serial")
public class ContentPath implements Serializable{

	private int pathNumber;
	private List<NodePath> nodes = new ArrayList<>();
	
	
	public int getPathNumber() {
		return pathNumber;
	}


	public void setPathNumber(int pathNumber) {
		this.pathNumber = pathNumber;
	}


	public List<NodePath> getNodes() {
		return nodes;
	}


	public void setNodes(List<NodePath> nodes) {
		this.nodes = nodes;
	}


	public static class NodePath  implements Serializable{
		private Appellation appellation;
		private Millesime millesime;
		private Contents contents;
		private Container container;
		
		public Appellation getAppellation() {
			return appellation;
		}
		public void setAppellation(Appellation appellation) {
			this.appellation = appellation;
		}
		public Millesime getMillesime() {
			return millesime;
		}
		public void setMillesime(Millesime millesime) {
			this.millesime = millesime;
		}
		public Contents getContents() {
			return contents;
		}
		public void setContents(Contents contents) {
			this.contents = contents;
		}
		public Container getContainer() {
			return container;
		}
		public void setContainer(Container container) {
			this.container = container;
		}
		
		
	}
}
