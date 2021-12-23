package fr.srosoft.wineyard.modules.cuvee;

import java.text.SimpleDateFormat;
import java.util.List;

import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;

import fr.srosoft.wineyard.core.model.beans.ContentPath;
import fr.srosoft.wineyard.core.model.beans.ContentPath.NodePath;
import fr.srosoft.wineyard.core.model.entities.Cuvee;

public class CuveeDiagram {

	private DefaultDiagramModel diagram;
	private Cuvee cuvee;
	private List<ContentPath> contentPaths;
	
	public CuveeDiagram (Cuvee cuvee, List<ContentPath> contentPaths) {
		this.cuvee = cuvee;
		this.contentPaths = contentPaths;
		this.initDiagram();
	}
	
	private void initDiagram() {
		diagram = new DefaultDiagramModel();
		diagram.setMaxConnections(-1);
		diagram.setConnectionsDetachable(false);
		diagram.setContainment(false);
       
		diagram.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{stroke:'#98AFC7', strokeWidth:3}");
        connector.setHoverPaintStyle("{stroke:'#5C738B'}");
        diagram.setDefaultConnector(connector);
        this.buildDiagram();
        
        
	}
	
	private void buildDiagram() {
		int y = 6;
		int x = 5;
		for (ContentPath contentPath : contentPaths) {
			y = 6;
			final int pathNumber = contentPath.getPathNumber();
			Element previousNode = null;
			
			for (NodePath node : contentPath.getNodes()) {
				
				Element nodeElement = diagram.findElement(node.getContents().getId());
				if (nodeElement == null) {
					nodeElement = new Element(node, x+"em", y+"em");
					nodeElement.setId(node.getContents().getId());
					nodeElement.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
					nodeElement.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
					diagram.addElement(nodeElement);
				}
				
			    if (previousNode == null) {
			    	previousNode = nodeElement;
			    }
			    else {
			    	String date = new SimpleDateFormat("dd/MM/YYYY").format(node.getContents().getCreationDate());
			    	String label = "De "+((NodePath)previousNode.getData()).getContainer().getNumber()+"\n\r Vers "+node.getContainer().getNumber()+" le "+date;
			    	diagram.connect(createConnection(previousNode.getEndPoints().get(0), nodeElement.getEndPoints().get(1), label));
			    	previousNode = nodeElement;
			    }
			    y=y+15;
			}
			x = x+15;
			
		}
	}
	
	private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        if (label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }

        return conn;
    }

	public DefaultDiagramModel getDiagram() {
		return diagram;
	}

	public void setDiagram(DefaultDiagramModel diagram) {
		this.diagram = diagram;
	}

	public Cuvee getCuvee() {
		return cuvee;
	}

	public void setCuvee(Cuvee cuvee) {
		this.cuvee = cuvee;
	}

	public List<ContentPath> getContentPaths() {
		return contentPaths;
	}

	public void setContentPaths(List<ContentPath> contentPaths) {
		this.contentPaths = contentPaths;
	} 
}
