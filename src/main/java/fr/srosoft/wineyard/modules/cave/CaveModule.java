package fr.srosoft.wineyard.modules.cave;

import java.util.ArrayList;
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
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.model.entities.Trace;
import fr.srosoft.wineyard.core.services.CaveService;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;
import fr.srosoft.wineyard.modules.domain.DomainModule;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="CaveModule", 
description="Cave Management", 
label="Cave",
order=2)
public class CaveModule extends AbstractModule{

	@Resource
	private CaveService caveService;
	
	private List<Barrel> barrels;
	private List<Event> events;
	
	private Tank currentTank;
	
	
	private String currentTankView ="grid";
	
	private Integer addTanksNumber;
	private Integer addTanksVolume;
	private String addTanksYear;
	
	private boolean showCodes = false;
	
	private UserSession context;
	
	private ContentsDashlet contentsDashlet;
	
	 private LineChartModel cartesianLinerModel;
	
	@Override
	public void loadData(UserSession context) {
		this.context = context;
		contentsDashlet = new ContentsDashlet(this);
		this.createCartesianLinerModel();
		
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
	
	public void addTanks() {
		for (int i = 0; i < addTanksNumber; i++) {
			Tank t = new Tank ();
			t.setVolume(addTanksVolume);
			t.setYear(addTanksYear);
			t.setId(UUID.randomUUID().toString());
			
			Contents c= new Contents();
			c.setVolume(0);
			c.setYear("2021");
			c.setAppellation(null);
			t.setContents(c);
			String currentDomainId = ((DomainModule)context.getModule("DomainModule")).getCurrentDomain().getId();
			caveService.addTank(currentDomainId,t);
			
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
	
	public void addTank() {
	
			Tank t = new Tank ();
			t.setVolume(20);
			t.setYear("2015");
			t.setId(UUID.randomUUID().toString());
			
			Action action = new Action ();
			action.setActionName("Remplissage");
			action.setCreationDate(new Date());
			action.setDate(new Date());
			t.addAction(action);
			
			Contents c= new Contents();
			c.setVolume(0);
			c.setYear("2021");
			c.setAppellation(null);
			t.setContents(c);
			String currentDomainId = ((DomainModule)context.getModule("DomainModule")).getCurrentDomain().getId();
			caveService.addTank(currentDomainId,t);
			
			
			this.currentTank = t;
	
	}
	
	public void onItemDropped(DragDropEvent<?> event) {
	      String item = (String) event.getData();
	      System.out.println("Source: "+event.getSource());
	      System.out.println("Source: "+event.getComponent());
	      if (event.getDragId().equals("centerPanel:cuve")) {
	    	  this.addTank();
	      }
	      else {
	    	  
	    	  String [] parts = event.getDragId().split("_");
	    	  System.out.println(parts[2]+":"+parts[3]);
	    	  Appellation app = caveService.findAppellation(parts[2]+":"+parts[3]);
	    	    	
	    	  
	    	 // centerPanel:j_idt152:tankGrid:8:card
	    	  parts =  event.getDropId().split(":");
	    	  Tank tank = this.getTanks().get(Integer.parseInt(parts[3]));
	    	  tank.getContents().setAppellation(app);
	    	  
	    	  Trace trace = new Trace();
	    	  trace.setName("Mise en cuve");
	    	  trace.setComment("Mise en cuve n°"+tank.getId());
			  trace.setCreationDate(new Date());
			  trace.setCreationUser(context.getCurrentUser().getDisplayName());
			  trace.setLastUpdateDate(new Date());
			  trace.setLastUpdateUser(context.getCurrentUser().getDisplayName());
			  tank.getContents().getTraceLine().addTrace(trace);
			  tank.getContents().setVolume(tank.getVolume());
			  tank.getContents().setColor(app.getWineColor());
	    	  
	      }
	      //centerPanel:menu_appellation_7801d94608e5120d87ebea4a0b76d704_e5934671321aad7db7b4f077c5652a92
	      System.out.println(event.getDragId());
	      System.out.println(event.getDropId());
	      System.out.println(item);
	      
	  }
	
	
	public void createCartesianLinerModel() {
        cartesianLinerModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        values.add(1100);
        values.add(1080);
        values.add(1060);
        values.add(1040);
        values.add(1020);
        values.add(1000);
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
	
	
	
	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Tank> getTanks() {
		String currentDomainId = ((DomainModule)context.getModule("DomainModule")).getCurrentDomain().getId();
		return caveService.getTanks(currentDomainId);	
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


	
}
