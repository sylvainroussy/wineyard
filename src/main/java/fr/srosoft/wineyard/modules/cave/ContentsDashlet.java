package fr.srosoft.wineyard.modules.cave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;

import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.session.Dashlet;

public class ContentsDashlet extends Dashlet<CaveModule> {

	private Contents currentContent;
	
	private Map<String,DefaultDiagramModel> models = new HashMap<String, DefaultDiagramModel>();
	
	
	public ContentsDashlet(CaveModule module) {
		super(module);
		
		final List<Appellation> appelations  = module.getAppellations();
		for (Appellation appellation : appelations) {
			final DefaultDiagramModel  model = new DefaultDiagramModel();
	        model.setMaxConnections(-1);
	        model.setContainment(false);

	        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
	        StraightConnector connector = new StraightConnector();
	        connector.setPaintStyle("{stroke:'#98AFC7', strokeWidth:3}");
	        connector.setHoverPaintStyle("{stroke:'#5C738B'}");
	        model.setDefaultConnector(connector);
	       

	        Element ceo = new Element(appellation, "25em", "6em");
	        ceo.setTitle(appellation.getAppellation());
	        ceo.addEndPoint(createEndPoint(EndPointAnchor.BOTTOM));
	        model.addElement(ceo);
	        models.put(appellation.getId(), model);
		}
		
		
		
	}
	
	public void onConnect(ConnectEvent event) {
       System.out.println(event);
    }

    public void onDisconnect(DisconnectEvent event) {
        System.out.println(event);
    }

    public void onConnectionChange(ConnectionChangeEvent event) {
        System.out.println(event);
    }
	
	private EndPoint createEndPoint(EndPointAnchor anchor) {
        DotEndPoint endPoint = new DotEndPoint(anchor);
        endPoint.setStyle("{fill:'#404a4e'}");
        endPoint.setHoverStyle("{fill:'#20282b'}");
       
        return endPoint;
    }
	
	public DefaultDiagramModel getModel(String appellationId) {
		return models.get(appellationId);
	}
	public void prepareNewContents() {
		currentContent = new Contents();
	}
	
	
	

	public Contents getCurrentContent() {
		return currentContent;
	}

	public void setCurrentContent(Contents currentContent) {
		this.currentContent = currentContent;
	}

	
}
