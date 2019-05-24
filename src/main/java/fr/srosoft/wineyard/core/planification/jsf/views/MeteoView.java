package fr.srosoft.wineyard.core.planification.jsf.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.client.RestTemplate;


//@Component
//@Scope ("session")
public class MeteoView {
	
	public MeteoView() {	 
	}

	@Resource
	RestTemplate restTemplate;
	
	List<MeteoBean> meteo = new ArrayList<MeteoBean>();
	
	public void loadMeteo (){
		
		meteo.clear();
		
		Map<String,Object> map = restTemplate.getForObject("https://www.prevision-meteo.ch/services/json/lyon", Map.class);
		for (int i = 0; i < 5; i++) {
			Map<String,Object> mapFcst =(Map<String,Object>) map.get("fcst_day_"+i);
			if (mapFcst != null){
			MeteoBean bean = new MeteoBean();
			bean.setCondition(""+mapFcst.get("condition"));
			bean.setDayLong(""+mapFcst.get("day_long"));
			bean.setIcon(""+mapFcst.get("icon"));
			bean.setTmax((int) mapFcst.get("tmax"));
			bean.setTmin((int) mapFcst.get("tmin"));
			meteo.add(bean);
			}
			else System.out.println("FCST null for index: "+i);
			/*
			 * "fcst_day_0":{"date":"02.05.2019","day_short":"Jeu.","day_long":"Jeudi","tmin":8,"tmax":17,"condition":"Eclaircies","condition_key":"eclaircies","icon":"https:\/\/www.prevision-meteo.ch\/style\/images\/icon\/eclaircies.png","icon_big":"https:\/\/www.prevision-meteo.ch\/style\/images\/icon\/eclaircies-big.png",
			 */
		}
		
	}
	
	public List<MeteoBean> getMeteo() {
		return meteo;
	}

	public void setMeteo(List<MeteoBean> meteo) {
		this.meteo = meteo;
	}

	@SuppressWarnings ("serial")
	public static class MeteoBean implements Serializable{
		private String dayLong;
		private int tmin;
		private int tmax;
		private String condition;
		private String icon;
		public String getDayLong() {
			return dayLong;
		}
		public void setDayLong(String dayLong) {
			this.dayLong = dayLong;
		}
		public int getTmin() {
			return tmin;
		}
		public void setTmin(int tmin) {
			this.tmin = tmin;
		}
		public int getTmax() {
			return tmax;
		}
		public void setTmax(int tmax) {
			this.tmax = tmax;
		}
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		
		
	}
}
