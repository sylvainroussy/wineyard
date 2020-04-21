package fr.srosoft.wineyard.core.meteo.jsf.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Scope("session")
public class MeteoView {

	private List<MeteoBean> meteo = new ArrayList<>();
	
	@Resource
	private RestTemplate restTemplate;
	
	public void loadMeteo(String latLng) {
		meteo.clear();
		
		final Map<String,Object> json = restTemplate.getForObject("https://www.prevision-meteo.ch/services/json/lat=46.259lng=5.235", Map.class);
		for (int j = 0; j < 5; j++) {
			final Map<String,Object> entry = (Map<String,Object>) json.get("fcst_day_"+j);
			MeteoBean mb = new MeteoBean();
			mb.setCondition(""+entry.get("condition"));
			mb.setIcon(""+entry.get("icon"));
			mb.settMax((int) entry.get("tmax"));
			mb.settMin((int) entry.get("tmin"));
			mb.setDayLong(""+ entry.get("day_long"));
			
			meteo.add(mb);
		} 
				
	}
	
	public List<MeteoBean> getMeteo() {
		return meteo;
	}

	public void setMeteo(List<MeteoBean> meteo) {
		this.meteo = meteo;
	}

	@SuppressWarnings("serial")
	
	public static class MeteoBean implements Serializable{
		private String condition;
		private int tMax;
		private int tMin;
		private String icon;
		private String dayLong;
		
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public int gettMax() {
			return tMax;
		}
		public void settMax(int tMax) {
			this.tMax = tMax;
		}
		public int gettMin() {
			return tMin;
		}
		public void settMin(int tMin) {
			this.tMin = tMin;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getDayLong() {
			return dayLong;
		}
		public void setDayLong(String dayLong) {
			this.dayLong = dayLong;
		}
		
		
	}
}
