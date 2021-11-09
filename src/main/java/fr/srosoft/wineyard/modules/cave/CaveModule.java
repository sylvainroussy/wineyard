package fr.srosoft.wineyard.modules.cave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.primefaces.event.DragDropEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Action;
import fr.srosoft.wineyard.core.model.entities.Appellation;
import fr.srosoft.wineyard.core.model.entities.Barrel;
import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.ContainerTemplate;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.Trace;
import fr.srosoft.wineyard.core.services.CaveService;
import fr.srosoft.wineyard.core.services.ContainerService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;
import fr.srosoft.wineyard.modules.domain.DomainModule;
import fr.srosoft.wineyard.utils.Constants;
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
	
	
	
	
	
	private ContentsDashlet contentsDashlet;
	
	 private LineChartModel cartesianLinerModel;
	
	@Override
	public void loadData(UserSession context) {
		super.loadData(context);		
		contentsDashlet = new ContentsDashlet(this);
		this.createCartesianLinerModel();
		
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
			final List<Action> actions = this.currentTank.getActions();
			if (actions != null) {
				actions.stream().forEach(e -> events.add(new Event(e.getActionName(), "15/10/2020 10:30", "pi pi-shopping-cart", "#9C27B0", "game-controller.jpg")));
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
			
			Action action = new Action ();
			action.setActionName("Remplissage");
			action.setCreationDate(new Date());
			action.setDate(new Date());
			container.addAction(action);
			
			Contents c= new Contents();			
			c.setVolume(0);
			
			container.setContents(c);
			
			
			return container;
	
	}
	
	public void onItemDropped(DragDropEvent<?> event) {
	      String item = (String) event.getData();
	      System.out.println("Source: "+event.getSource());
	      System.out.println("Source: "+event.getDragId());
	      if (event.getDragId().contains("menu_containerTemplate_")) {
	    	  final String parts[] =  event.getDragId().split("_");
	    	  final String containerTemplateId =parts[parts.length-1];
	    	  final Container container = this.buildContainer(containerTemplateId);
	    	  this.containerService.saveContainers(Arrays.asList(container), context, currentContainerTemplate);
	    	  currentContainerTemplate = null;
	    	  
	      }
	      else {
	    	  
	    	  String [] parts = event.getDragId().split("_");
	    	  System.out.println(parts[2]+":"+parts[3]);
	    	  Appellation app = caveService.findAppellation(parts[2]+":"+parts[3]);
	    	  Cuvee cuvee = new Cuvee();
	    	  final Millesime millesime = new Millesime ();
	    	  millesime.setYear(Integer.parseInt("2021"));
	    	  cuvee.setAppellation(app);
	    	  cuvee.setMillesime(millesime);
	    	  
	    	  
	    	 // centerPanel:j_idt152:tankGrid:8:card
	    	  parts =  event.getDropId().split(":");
	    	  Tank tank = this.getTanks().get(Integer.parseInt(parts[3]));
	    	  Contents c= new Contents();
	    	  c.setVolume(0);		
	    	  c.setCuvee(cuvee);
	    	  tank.setContents(c);
	    	
	    	  
	    	  Trace trace = new Trace();
	    	  trace.setName("Mise en cuve");
	    	  trace.setComment("Mise en cuve n°"+tank.getId());
			  trace.setCreationDate(new Date());
			  trace.setCreationUser(context.getCurrentUser().getDisplayName());
			  trace.setLastUpdateDate(new Date());
			  trace.setLastUpdateUser(context.getCurrentUser().getDisplayName());
			  tank.getContents().getTraceLine().addTrace(trace);
			  tank.getContents().setVolume(tank.getVolume());			  
			  tank.getContents().setCurrentState(Constants.STATE_WAITING_ALCOHOLIC_FERMENTATION);
	    	  
	      }
	     
	      
	  }
	
	
	public void createCartesianLinerModel() {
        cartesianLinerModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        values.add(1100);
        values.add(1090);
        values.add(1070);
        values.add(1040);
        values.add(1020);
        values.add(980);
        dataSet.setData(values);
        dataSet.setLabel("Densité");
        dataSet.setYaxisID("left-y-axis");
        dataSet.setFill(true);
        //dataSet.setTension(0.5);

        LineChartDataSet dataSet2 = new LineChartDataSet();
        List<Object> values2 = new ArrayList<>();
        values2.add(10);
        values2.add(12);
        values2.add(14);
        values2.add(14);
        values2.add(12);
        values2.add(10);
        dataSet2.setData(values2);
        dataSet2.setLabel("Température");
        dataSet2.setYaxisID("right-y-axis");
        dataSet2.setFill(true);
        dataSet2.setBorderColor("#f3f6f9");
        //dataSet2.setTension(0.5);

        data.addChartDataSet(dataSet);
        data.addChartDataSet(dataSet2);

        List<String> labels = new ArrayList<>();
        labels.add("Jour 1");
        labels.add("Jour 2");
        labels.add("Jour 3");
        labels.add("Jour 4");
        labels.add("Jour 5");
        labels.add("Jour 6");
        data.setLabels(labels);
        cartesianLinerModel.setData(data);

        //Options
        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("left-y-axis");
        linearAxes.setPosition("left");
        CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
        linearAxes2.setId("right-y-axis");
        linearAxes2.setPosition("right");

        cScales.addYAxesData(linearAxes);
        cScales.addYAxesData(linearAxes2);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Température et densité (fermentation)");
        options.setTitle(title);

        cartesianLinerModel.setOptions(options);
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
		String currentDomainId = ((DomainModule)context.getModule("DomainModule")).getCurrentDomain().getId();
		return containerService.getTanks(currentDomainId);	
	}

	

	public List<Barrel> getBarrels() {
		return barrels;
	}

	public void setBarrels(List<Barrel> barrels) {
		this.barrels = barrels;
	}
	
	public Tank getCurrentTank() {
		return currentTank;
	}

	public void setCurrentTank(Tank currentTank) {
		this.currentTank = currentTank;
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

	public void setContentsDashlet(ContentsDashlet contentsDashlet) {
		this.contentsDashlet = contentsDashlet;
	}

	public void changeShowCodes () {
		this.showCodes = !this.showCodes;
	}

	public LineChartModel getCartesianLinerModel() {
		return cartesianLinerModel;
	}

	public void setCartesianLinerModel(LineChartModel cartesianLinerModel) {
		this.cartesianLinerModel = cartesianLinerModel;
	}

	public int getContainerTemplateQuantity() {
		return containerTemplateQuantity;
	}

	public void setContainerTemplateQuantity(int containerTemplateQuantity) {
		this.containerTemplateQuantity = containerTemplateQuantity;
	}


	
}
