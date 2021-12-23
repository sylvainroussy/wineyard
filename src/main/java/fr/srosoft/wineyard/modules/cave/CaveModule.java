package fr.srosoft.wineyard.modules.cave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Barrel;
import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.ContainerTemplate;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.Trace;
import fr.srosoft.wineyard.core.model.entities.operations.ContainerOperation;
import fr.srosoft.wineyard.core.model.entities.operations.Operation;
import fr.srosoft.wineyard.core.services.CaveService;
import fr.srosoft.wineyard.core.services.ContainerService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.engine.CleaningOperation;
import fr.srosoft.wineyard.engine.DispatchOperation;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;
import fr.srosoft.wineyard.modules.domain.DomainModule;
import fr.srosoft.wineyard.utils.Constants;
import fr.srosoft.wineyard.utils.WineyardUtils;
@Component

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="CaveModule", 
description="Cave Management", 
label="Chai",
order=2)
public class CaveModule extends AbstractModule{

	@Resource
	private CaveService caveService;
	
	@Resource
	private ContainerService containerService;
	
	private List<Barrel> barrels;
	private List<Event> events;
	
	private Tank currentTank;
	private ContainerTemplate currentContainerTemplate;
	private int containerTemplateQuantity;
	
	
	private String currentTankView ="grid";
	private Integer addTanksNumber;
	private Integer addTanksVolume;
	private String addTanksYear;
	
	private boolean showCodes = false;
	
	
	private CaveTransferAction transferAction;
	
	// Trying...
	private ContentsDashlet contentsDashlet;
	
	 private DefaultDiagramModel diagramModel; 
	 
	 private ContainerDetailForm containerDetailForm ;
	
	@Override
	public void loadData(UserSession context) {
		super.loadData(context);		
		contentsDashlet = new ContentsDashlet(this);
		
		
		diagramModel = new DefaultDiagramModel();
        diagramModel.setMaxConnections(-1);
        diagramModel.setConnectionsDetachable(true);
        diagramModel.setContainment(false);
       
        diagramModel.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{stroke:'#98AFC7', strokeWidth:3}");
        connector.setHoverPaintStyle("{stroke:'#5C738B'}");
        diagramModel.setDefaultConnector(connector);
        
        containerDetailForm = new ContainerDetailForm(this.caveService);
		
	}
	
	@Override
	public String getIcon() {
		return "pi pi-sitemap";
	}
	
	public List<Event> getActions(){
		
		List<Event> events = new ArrayList<>();
	       
	      /*  events.add(new Event("Processing", "15/10/2020 14:00", "pi pi-cog", "#673AB7"));
	        events.add(new Event("Shipped", "15/10/2020 16:15", "pi pi-shopping-cart", "#FF9800"));
	        events.add(new Event("Delivered", "16/10/2020 10:00", "pi pi-check", "#607D8B"));*/
		if (this.currentTank != null) {
			final List<Operation> actions = this.currentTank.getActions();
			if (actions != null) {
				actions.stream().forEach(e -> events.add(new Event(e.getActionTitle(), "15/10/2020 10:30", "pi pi-shopping-cart", "#9C27B0", "game-controller.jpg")));
			}
		}
		return events;
	}
	
	public List<Appellation> getAppellations(){
		String currentDomainId = ((DomainModule)context.getModule("DomainModule")).getCurrentDomain().getId();
		return caveService.findAppellations(currentDomainId);
	}
	
	public List<Millesime> getMillesimes() {
		return caveService.getMillesimes (context);
	}
	
	public List<Cuvee> getCuveesByMillesime(int year){
		return caveService.getCuveesByDomainAndMillesime(context, year);
	}
	
	public void prepareTransferTankToLargeAction(Container container) {
		this.transferAction = new TransferTankToLargeAction(container);
		
	}
	
	public void prepareTransferTankToBarrelsAction(Container container) {
		this.transferAction = new TransferTankToBarrelsAction(container);
		
	}
	
	public void executePlan() {
		final DispatchOperation dop = new DispatchOperation();
		dop.run(this.transferAction.sourceContainer, this.transferAction.targetContainers, this.context);
		this.containerService.updateContainerAndContents(this.transferAction.sourceContainer, context);
		for (TargetContainer targetContainer : this.transferAction.targetContainers) {
			this.containerService.updateContainerAndContents(targetContainer.getContainer(), context);
		}
		this.transferAction = null;
		this.diagramModel.clear();
		
	}
	
	public void cleanContainer (Container container) {
		final CleaningOperation cop = new CleaningOperation();
		cop.run(container);
		this.containerService.cleanContainer(container, context);
	}
	
	public void initDiagramModel () {
		initTransferGraph(this.transferAction.getSourceContainer(),this.transferAction.getDestinationContainerType() );
	}
	
	private void initTransferGraph(Container containerSource, String type) {
		  
			String styleSource = containerSource instanceof Tank ? "pi pi-tablet" :"barrel"; 
		
			this.diagramModel.clear();
	        TargetContainer source = new TargetContainer(containerSource,containerSource.getContents().getVolume());	      
	        source.setVolume(containerSource.getContents().getVolume());
	        
	        final ElementWrapper ewSource = new ElementWrapper();
	        ewSource.setContainers(Arrays.asList(source));
	        ewSource.setLabel(source.getContainer().getNumber());
	        ewSource.setVolume(source.getContainer().getContents().getVolume());
	        ewSource.setStyle(styleSource);
	        
	        Element computerA = new Element(ewSource, "10em", "6em");
	        computerA.setId(source.getContainer().getId());
	        EndPoint endPointCA = createRectangleEndPoint(EndPointAnchor.BOTTOM);
	        endPointCA.setSource(true);
	        computerA.addEndPoint(endPointCA);

	        
	        int baseX = 2;// +15
	        int baseY = 24;
	        if (type.equals("Tank") ) {
		        for(TargetContainer target : this.getPossibleContainers()) {		        	
		        	
		        	
		        	final ElementWrapper ewTarget = new ElementWrapper();
		        	ewTarget.setContainers(Arrays.asList(target));
		        	ewTarget.setLabel(target.getContainer().getNumber());
		        	ewTarget.setVolume(target.getContainer().getVolume());
		        	ewTarget.setStyle("pi pi-tablet");
			        final Element serverA = new Element(ewTarget, baseX+"em", baseY+"em");
			        final EndPoint endPointSA = createDotEndPoint(EndPointAnchor.AUTO_DEFAULT);
			        serverA.setId(target.getContainer().getId());
			        serverA.setDraggable(true);
			        endPointSA.setTarget(true);
			        serverA.addEndPoint(endPointSA);
			        diagramModel.addElement(serverA);
			        baseX = baseX+15;
		        }
		        	
		    }
	        else if (type.equals("Barrel") ) {
	        	final Map<String, List<TargetContainer>> map=  this.getPossibleContainers().stream().collect(Collectors.groupingBy( e -> e.getContainer().getYear()));
	        	for (Map.Entry<String,List<TargetContainer>> targets : map.entrySet()) {
	        		
	        		final ElementWrapper ewTarget = new ElementWrapper();
		        	ewTarget.setContainers(targets.getValue());
		        	ewTarget.setLabel(targets.getKey()+" (Nb:"+targets.getValue().size()+")");
		        	ewTarget.setVolume(targets.getValue().get(0).getContainer().getVolume());
		        	ewTarget.setStyle("barrel");
	        		Element serverA = new Element(ewTarget, baseX+"em", baseY+"em");
			        EndPoint endPointSA = createDotEndPoint(EndPointAnchor.AUTO_DEFAULT);
			        serverA.setId(targets.getKey());
			        serverA.setDraggable(true);
			        endPointSA.setTarget(true);
			        serverA.addEndPoint(endPointSA);
			        diagramModel.addElement(serverA);
			        baseX = baseX+15;
				}
	        }
			
	        diagramModel.addElement(computerA);
	        
	      
	   
	}
	
	public void onConnectDiagram(ConnectEvent event) {
		
	   final ElementWrapper wrapper = (ElementWrapper)event.getTargetElement().getData();
       this.transferAction.getTargetContainers().addAll(wrapper.getContainers());
       
       /*final Action action = new Action();
       action.setActionName("Transfert");
       action.setActionDetail("");
       action.setEngineOperation(engineOperation);*/
    }	
	

    public void onDisconnectDiagram(DisconnectEvent event) {
    	 final ElementWrapper wrapper = (ElementWrapper)event.getTargetElement().getData();
    	 this.transferAction.getTargetContainers().removeAll(wrapper.getContainers());
    }
    
    public void onConnectionChange(ConnectionChangeEvent event) {
      
    }
	
	 private EndPoint createDotEndPoint(EndPointAnchor anchor) {
	        DotEndPoint endPoint = new DotEndPoint(anchor);
	        endPoint.setScope("network");
	        endPoint.setTarget(true);
	        endPoint.setStyle("{fill:'#98AFC7'}");
	        endPoint.setHoverStyle("{fill:'#5C738B'}");

	        return endPoint;
	    }

	    private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
	        RectangleEndPoint endPoint = new RectangleEndPoint(anchor);
	        endPoint.setScope("network");
	        endPoint.setSource(true);
	      
	        endPoint.setStyle("{fill:'#98AFC7'}");
	        endPoint.setHoverStyle("{fill:'#5C738B'}");

	        return endPoint;
	    }
	
	
	

	public void addTanks() {
		for (int i = 0; i < addTanksNumber; i++) {
			Tank t = new Tank ();
			t.setVolume(addTanksVolume);
			t.setYear(addTanksYear);
			t.setId(UUID.randomUUID().toString());
			
			Contents c= new Contents();
			c.setVolume(0);			
			t.setContents(c);
			
			
			
			Trace trace = new Trace();
			trace.setName("Mise en cuve");
			trace.setComment("Mise en cuve n°"+t.getId());
			trace.setCreationDate(new Date());
			trace.setCreationUser(context.getCurrentUser().getDisplayName());
			trace.setLastUpdateDate(new Date());
			trace.setLastUpdateUser(context.getCurrentUser().getDisplayName());
			
			c.getTraceLine().addTrace(trace);
		}
	}
	
	public Container buildContainer(String containerTemplateId) {
		
			currentContainerTemplate =  this.getContainerTemplates().stream().filter(e -> e.getId().equals(containerTemplateId)).findFirst().get();
			
			final Container container = containerService.prepareContainer(currentContainerTemplate);
			
			container.setYear("2015");
			container.setId(UUID.randomUUID().toString());
			
			ContainerOperation<Void> action = new ContainerOperation<Void> ();
			action.setActionTitle("Remplissage");
			WineyardUtils.stamp(action, true, context.getCurrentUser().getDisplayName());
			action.setDateDone(new Date());
			container.addAction(action);
			
			Contents c= new Contents();			
			c.setVolume(0);
			
			container.setContents(c);
			
			
			return container;
	
	}
	
	public void saveContainer(Container container) {
		
		 this.containerService.updateContainer(container, context);
	}
	
	public List<String> getPossibleContainerTypes(){
		return containerService.getPossibleContainerTypes(this.context);	
	}
	
	public List<TargetContainer> getPossibleContainers(){		
		final List<TargetContainer> targetContainers = new ArrayList<>();
		if (this.transferAction != null) {
			final String type = this.transferAction.getDestinationContainerType();
			if (type != null) {
				switch (type) {
					case "Barrel" : targetContainers.addAll(this.getPossibleBarrels());break;
					case "Tank" : targetContainers.addAll(this.getPossibleTanks());break;
				}
			}
		}
		
		return targetContainers;
	}
	
	private List<TargetContainer> getPossibleTanks(){
		return this.containerService.findPossibleTanks(context).stream()
		.filter (e -> !e.equals(this.transferAction.getSourceContainer()))
		.map(e -> {			
			final TargetContainer targetContainer = new TargetContainer();
			if (e.getContents() == null) targetContainer.setLeftVolume(e.getVolume());
			else targetContainer.setLeftVolume(e.getVolume()-e.getContents().getVolume());
			
			targetContainer.setContainer(e);
			return targetContainer;
		} ).collect(Collectors.toList());
	}
	
	private List<TargetContainer> getPossibleBarrels(){
		return this.containerService.findPossibleBarrels(context).stream().map(e -> {
			final TargetContainer targetContainer = new TargetContainer();
			targetContainer.setContainer(e);
			if (e.getContents() == null) targetContainer.setLeftVolume(e.getVolume());
			else targetContainer.setLeftVolume(e.getVolume()-e.getContents().getVolume());
			return targetContainer;
		} ).collect(Collectors.toList());
	}
	
	public void onItemDropped(DragDropEvent<?> event) {
	      System.out.println("Source: "+event.getDragId());
	      if (event.getDragId().contains("menu_containerTemplate_")) {
	    	  final String parts[] =  event.getDragId().split("_");
	    	  final String containerTemplateId =parts[parts.length-1];
	    	  final Container container = this.buildContainer(containerTemplateId);
	    	  this.containerService.saveContainers(Arrays.asList(container), context, currentContainerTemplate);
	    	  currentContainerTemplate = null;
	    	  
	      }
	      else {
	    	  
	    	  String [] parts = event.getDragId().split("--");
	    	  if (parts.length == 2) {
	    		  final String cuveeId = parts[1].replaceFirst("_", ":");
	    		  
	    		  
		    	  System.out.println("CuveeId : "+cuveeId);
		    	  final Cuvee cuvee = caveService.getCuveesById(cuveeId, context);    	  	    	  
		    	  
		    	 // centerPanel:j_idt152:tankGrid:8:card
		    	  System.out.println("DropId  : "+event.getDropId());
		    	  parts =  event.getDropId().split(":");
		    	  Tank tank = this.getTanks().get(Integer.parseInt(parts[3]));
		    	  Contents c= new Contents();
		    	  c.setId(WineyardUtils.generateContentsId(tank));
		    	  c.setVolume(0);		
		    	  c.setCuvee(cuvee);
		    	  tank.setContents(c);
		    	  WineyardUtils.stamp(c, true, this.context.getCurrentUser().getDisplayName());
		    	
		    	  
		    	  Trace trace = new Trace();
		    	  trace.setName("Mise en cuve");
		    	  trace.setComment("Mise en cuve n°"+tank.getNumber());
				  trace.setCreationDate(new Date());
				  trace.setCreationUser(context.getCurrentUser().getDisplayName());
				  trace.setLastUpdateDate(new Date());
				  trace.setLastUpdateUser(context.getCurrentUser().getDisplayName());
				  tank.getContents().getTraceLine().addTrace(trace);
				  tank.getContents().setVolume(tank.getVolume());			  
				  tank.getContents().setCurrentState(Constants.STATE_WAITING_ALCOHOLIC_FERMENTATION);
				 
				  containerService.addContentToContainer(c, tank, context);
	    	  }
			  
	    	  
	      }
	     
	      
	  }
	
	public void onTargetContainerDrop(DragDropEvent<TargetContainer> ddEvent) {
		
		final TargetContainer targetContainer = ddEvent.getData();
		if (!this.transferAction.getTargetContainers().contains(ddEvent.getData())) {
			this.transferAction.getTargetContainers().add(ddEvent.getData());
			
			final Element source = diagramModel.findElement(this.transferAction.getSourceContainer().getId());
			final Element target = diagramModel.findElement(targetContainer.getContainer().getId());
			
			diagramModel.connect(this.createConnection(source.getEndPoints().get(0),target.getEndPoints().get(0) ,null));
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
	
	
	
	
	
	
	
	public ContainerDetailForm getContainerDetailForm() {
		return containerDetailForm;
	}

	public void setContainerDetailForm(ContainerDetailForm measureForm) {
		this.containerDetailForm = measureForm;
	}

	public List<ContainerTemplate> getContainerTemplates(){
		return containerService.getContainerTemplates(context);
	}
	
	public void saveContainerTemplate(){
		if (this.currentContainerTemplate.getId() == null) {
			this.currentContainerTemplate.setId(UUID.randomUUID().toString());
		}
		this.containerService.saveContainerTemplate(this.currentContainerTemplate, context);
		
		final List<Container> containers = new ArrayList<>();
		for (int i = 0; i < this.containerTemplateQuantity; i++) {
			final Container container = this.buildContainer(currentContainerTemplate.getId());
			containers.add(container);
		}
		if (this.containerTemplateQuantity > 0) this.containerService.saveContainers(containers, context, currentContainerTemplate);
		
		this.containerTemplateQuantity = 0;
		this.currentContainerTemplate = null;
	}
	
	public void prepareContainerTemplate() {
		this.currentContainerTemplate = new ContainerTemplate();
	}
	
	
	
	public List<Tank> getTanks() {
		String currentDomainId = context.getCurrentDomain().getId();
		return containerService.getTanks(currentDomainId);	
	}

	

	public List<Barrel> getBarrels() {
		String currentDomainId = context.getCurrentDomain().getId();
		return containerService.getBarrels(currentDomainId);	
	}

	public void setBarrels(List<Barrel> barrels) {
		this.barrels = barrels;
	}
	
	public Tank getCurrentTank() {
		return currentTank;
	}

	public void setCurrentTank(Tank currentTank) {
		this.currentTank = currentTank;
		this.containerDetailForm.clear();
		this.containerDetailForm.setCurrentTank(currentTank);
		
	}	


	public static class Event {
        String status;
        String date;
        String icon;
        String color;
        String image;

        public Event() {
        }

        public Event(String status, String date, String icon, String color) {
            this.status = status;
            this.date = date;
            this.icon = icon;
            this.color = color;
        }

        public Event(String status, String date, String icon, String color, String image) {
            this.status = status;
            this.date = date;
            this.icon = icon;
            this.color = color;
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }


	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	

	public String getCurrentTankView() {
		return currentTankView;
	}

	public void setCurrentTankView(String currentTankView) {
		this.currentTankView = currentTankView;
	}

	public Integer getAddTanksVolume() {
		return addTanksVolume;
	}


	public ContainerTemplate getCurrentContainerTemplate() {
		return currentContainerTemplate;
	}

	public void setCurrentContainerTemplate(ContainerTemplate currentContainerTemplate) {
		this.currentContainerTemplate = currentContainerTemplate;
	}

	public void setAddTanksVolume(Integer addTanksVolume) {
		this.addTanksVolume = addTanksVolume;
	}

	public String getAddTanksYear() {
		return addTanksYear;
	}

	public void setAddTanksYear(String addTanksYear) {
		this.addTanksYear = addTanksYear;
	}

	
	public Integer getAddTanksNumber() {
		return addTanksNumber;
	}

	public void setAddTanksNumber(Integer addTanksNumber) {
		this.addTanksNumber = addTanksNumber;
	}

	public boolean getShowCodes() {
		return showCodes;
	}

	public void setShowCodes(boolean showCodes) {
		this.showCodes = showCodes;
	}
	
	public ContentsDashlet getContentsDashlet() {
		return contentsDashlet;
	}

	public CaveTransferAction getTransferAction() {
		return transferAction;
	}

	public void setTransferAction(TransferTankToLargeAction transferAction) {
		this.transferAction = transferAction;
	}

	public void setContentsDashlet(ContentsDashlet contentsDashlet) {
		this.contentsDashlet = contentsDashlet;
	}

	public void changeShowCodes () {
		this.showCodes = !this.showCodes;
	}

	

	public int getContainerTemplateQuantity() {
		return containerTemplateQuantity;
	}

	public void setContainerTemplateQuantity(int containerTemplateQuantity) {
		this.containerTemplateQuantity = containerTemplateQuantity;
	}
	
	
	public DefaultDiagramModel getDiagramModel() {
		return diagramModel;
	}

	public void setDiagramModel(DefaultDiagramModel diagramModel) {
		this.diagramModel = diagramModel;
	}


	public static class ElementWrapper {
		private List<TargetContainer> containers;
		private String label;
		private int volume;
		private String unit;
		private String style;
		
		public List<TargetContainer> getContainers() {
			return containers;
		}
		public void setContainers(List<TargetContainer> containers) {
			this.containers = containers;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public int getVolume() {
			return volume;
		}
		public void setVolume(int volume) {
			this.volume = volume;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getStyle() {
			return style;
		}
		public void setStyle(String style) {
			this.style = style;
		}
		
		
		
	}


	
}
