package fr.srosoft.wineyard.core.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.model.entities.Region;
import fr.srosoft.wineyard.core.model.entities.User;
import fr.srosoft.wineyard.core.services.DirectoryService;
import fr.srosoft.wineyard.core.services.GISService;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.ModuleLoader;
import fr.srosoft.wineyard.modules.cuvee.CuveeModule;
import fr.srosoft.wineyard.modules.domain.DomainModule;


@Component
@Scope("session")
public class UserSession {

	private String locale;

	private static Map<String, Object> countries;
	static {

		countries = new LinkedHashMap<String, Object>();
		countries.put("English", Locale.ENGLISH);
		countries.put("French", Locale.FRENCH);
	}

	@Resource
	private ModuleLoader loader;
	
	@Resource
	private GISService gisService;
	
	@Resource
	private DirectoryService directoryService;

	private User currentUser;
	private String currentPage = "home";

	private Map<String, AbstractModule> modules;
	private List<ModuleBean> moduleBeans = new ArrayList<>();

	private Scheduler scheduler = new Scheduler();

	private Domain currentDomain;
	private Millesime currentMillesime;
	
	

	@PostConstruct
	public void initCurrentUser() throws Exception {
		currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		currentDomain  = directoryService.getDefaultDomain(currentUser.getId());
		modules = loader.loadAllModules();
	
		modules.entrySet().forEach(e -> {
			final ModuleBean bean = new ModuleBean();
			final fr.srosoft.wineyard.modules.commons.Module moduleAnnotation = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class);
			String label = moduleAnnotation.label();
			String desc = moduleAnnotation.description();
			String name = moduleAnnotation.name();
			Integer order = moduleAnnotation.order();
			e.getValue().loadData(this);
			bean.setModule(e.getValue());
			bean.setLabel(label);
			bean.setDescription(desc);
			bean.setModuleName(name);
			bean.setOrder(order);
			moduleBeans.add(bean);
		});
		currentDomain = ((DomainModule) this.getModule("DomainModule")).getCurrentDomain();
		Collections.sort(moduleBeans);

		

		scheduler.init();
	}

	private void reloadMOdules() {
		modules.entrySet().forEach(e -> e.getValue().loadData(this));
	}

	public void onDomainChange() {

		currentDomain = ((DomainModule) this.getModule("DomainModule")).getCurrentDomain();
		// reloadMOdules();

	}

	public void onMillesimeChange() {
		currentMillesime = ((CuveeModule) this.getModule("CuveeModule")).getCurrentMillesime();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public AbstractModule getModule(String moduleName) {
		return modules.get(moduleName);
	}
	
	public <T extends AbstractModule> T getModule(String moduleName, Class<T> clazz) {
		return clazz.cast(modules.get(moduleName));
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public Map<String, AbstractModule> getModules() {
		return modules;
	}

	public void setModules(Map<String, AbstractModule> modules) {
		this.modules = modules;
	}

	public List<ModuleBean> getModuleBeans() {
		return moduleBeans;
	}

	public void setModuleBeans(List<ModuleBean> moduleBeans) {
		this.moduleBeans = moduleBeans;
	}

	public void changePage(String pageName) {
		this.currentPage = pageName;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public Domain getCurrentDomain() {
		return currentDomain;
	}

	public Millesime getCurrentMillesime() {
		return currentMillesime;
	}

	public void setCurrentMillesime(Millesime currentMillesime) {
		this.currentMillesime = currentMillesime;
	}
	
	public List<Region> getRegions(){
		return gisService.findRegionsByCountryId("FR");
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	// value change event listener
	public void localeChanged(ValueChangeEvent e) {
		String newLocaleValue = e.getNewValue().toString();

		for (Map.Entry<String, Object> entry : countries.entrySet()) {

			if (entry.getValue().toString().equals(newLocaleValue)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
			}
		}
	}

}
