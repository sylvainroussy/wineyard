package fr.srosoft.wineyard.modules.cave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Action;
import fr.srosoft.wineyard.core.model.entities.Barrel;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Tank;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.Module;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Module (name="CaveModule", 
description="Cave Management", 
label="Cave",
order=1)
public class CaveModule extends AbstractModule{

	public static final String[] actionNames1= {  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	public static final String[] actionNames2= {"MISE EN BOUTEILLES",  "FILTRATION","PIGEAGE","REMPLISSAGE"};
	
	private List<Tank> tanks;
	private List<Barrel> barrels;
	private List<Event> events;
	
	private Tank currentTank;
	
	@Deprecated
	private Boolean showTankGrid = true;
	
	private String currentTankView ="grid";
	
	@Override
	public void loadData(UserSession context) {
		tanks = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Tank tank = new Tank();
			tank.setId(""+i);
			tank.setNumber(""+i);
			tank.setVolume(400);
			tank.setYear("2015");
			
			Contents c = new Contents();
			c.setAppelation("Maranges 1er Cru La Fussière");
			c.addGrape("Chardonnay");
			c.setColor("white");
			c.setId(UUID.randomUUID().toString());
			c.setYear("2020");
			c.setVolume(400);
			tank.setContents(c);
			tanks.add(tank);
			
			for (int j= 0; j < 3; j++) {
				
				final Action action = new Action();
				action.setId(UUID.randomUUID().toString());
				action.setActionName(actionNames1[j]);
				action.setComment("Commentaire sur l'action");
				action.setDate(new Date());
				tank.addAction(action);
			}
		}
		
		for (int i = 0; i < 5; i++) {
			Tank tank = new Tank();
			tank.setId(""+(i+5));
			tank.setNumber(""+(i+5));
			tank.setVolume(400);
			tank.setYear("2015");
			
			Contents c = new Contents();
			c.setAppelation("Hautes Côtes de Beaune");
			c.addGrape("Pinot noir");
			c.setId(UUID.randomUUID().toString());
			c.setYear("2020");
			c.setColor("red");
			c.setVolume(200);
			tank.setContents(c);
			tanks.add(tank);
			
			for (int j= 0; j < 4; j++) {
				
				final Action action = new Action();
				action.setId(UUID.randomUUID().toString());
				action.setActionName(actionNames2[j]);
				action.setComment("Commentaire sur l'action");
				action.setDate(new Date());
				tank.addAction(action);
			}
		}
		
		

	      
		
	}
	
	public List<Event> getActions(){
		
		List<Event> events = new ArrayList<>();
	       
	      /*  events.add(new Event("Processing", "15/10/2020 14:00", "pi pi-cog", "#673AB7"));
	        events.add(new Event("Shipped", "15/10/2020 16:15", "pi pi-shopping-cart", "#FF9800"));
	        events.add(new Event("Delivered", "16/10/2020 10:00", "pi pi-check", "#607D8B"));*/
		if (this.currentTank != null) {
			final List<Action> actions = this.currentTank.getActions();
			actions.stream().forEach(e -> events.add(new Event(e.getActionName(), "15/10/2020 10:30", "pi pi-shopping-cart", "#9C27B0", "game-controller.jpg")));
		}
		return events;
	}
	
	public void changeTankView() {
		this.showTankGrid = !this.showTankGrid;
	}

	@Override
	public String getMainPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Tank> getTanks() {
		return tanks;
	}

	public void setTanks(List<Tank> tanks) {
		this.tanks = tanks;
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

	public Boolean getShowTankGrid() {
		return showTankGrid;
	}

	public void setShowTankGrid(Boolean showTankGrid) {
		this.showTankGrid = showTankGrid;
	}

	public String getCurrentTankView() {
		return currentTankView;
	}

	public void setCurrentTankView(String currentTankView) {
		this.currentTankView = currentTankView;
	}


	
}
