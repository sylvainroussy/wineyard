package fr.srosoft.wineyard.core.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.User;
import fr.srosoft.wineyard.modules.commons.AbstractModule;
import fr.srosoft.wineyard.modules.commons.ModuleLoader;

@Component
@Scope("session")
public class UserSession {

	@Resource
	ModuleLoader loader;
	
	private User currentUser;
	private String currentPage="home";
	
	private Map<String,AbstractModule> modules;
	private List<ModuleBean> moduleBeans = new ArrayList<>();
	
	private Scheduler scheduler = new Scheduler();
	
	
	@PostConstruct
	public void initCurrentUser() throws Exception{
		currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		modules = loader.loadAllModules();
		modules.entrySet().forEach(e -> {
			final ModuleBean bean = new ModuleBean();
			
			String label = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class).label();
			String desc = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class).description();
			String name = e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class).name();
			Integer order =  e.getValue().getClass().getAnnotation(fr.srosoft.wineyard.modules.commons.Module.class).order();
			e.getValue().loadData(this);
			bean.setModule(e.getValue());
			bean.setLabel(label);
			bean.setDescription(desc);
			bean.setModuleName(name);
			bean.setOrder(order);
			moduleBeans.add(bean);
		});
		
		Collections.sort(moduleBeans);
		
		scheduler.init();
	}


	public User getCurrentUser() {
		return currentUser;
	}
	
	public AbstractModule getModule (String moduleName) {
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

	public void changePage (String pageName) {
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


	
}
