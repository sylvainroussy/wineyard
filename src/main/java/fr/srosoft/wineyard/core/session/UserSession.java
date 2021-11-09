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

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.model.entities.Millesime;
import fr.srosoft.wineyard.core.model.entities.User;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.ModuleLoader;
import fr.srosoft.wineyard.modules.cuvee.CuveeModule;
import fr.srosoft.wineyard.modules.domain.DomainModule;
import jakarta.faces.event.ValueChangeEvent;

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

		modules = loader.loadAllModules();
		modules.entrySet().forEach(e -> {
			final ModuleBean bean = new ModuleBean();

			String label = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class)
					.label();
			String desc = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class)
					.description();
			String name = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class)
					.name();
			Integer order = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class)
					.order();
			e.getValue().loadData(this);
			bean.setModule(e.getValue());
			bean.setLabel(label);
			bean.setDescription(desc);
			bean.setModuleName(name);
			bean.setOrder(order);
			moduleBeans.add(bean);
		});

		Collections.sort(moduleBeans);

		currentDomain = ((DomainModule) this.getModule("DomainModule")).getCurrentDomain();

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
