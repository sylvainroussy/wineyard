package fr.srosoft.wineyard.modules.commons;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

@Service
public class ModuleLoader {
	
	@Resource
	private ApplicationContext context;

	public Map<String,AbstractModule> loadAllModules() throws Exception{
		Map<String,AbstractModule> modules = new HashMap<String, AbstractModule>();
		final ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);

				scanner.addIncludeFilter(new AnnotationTypeFilter(Module.class));

				for (BeanDefinition bd : scanner.findCandidateComponents("fr.srosoft.wineyard.modules")) {
					Class<?> moduleClass = Class.forName(bd.getBeanClassName());
					String moduleName = moduleClass.getAnnotation(Module.class).name();
					AbstractModule module = (AbstractModule)context.getBean(moduleClass);
					modules.put(moduleName, module);
				}
					
		return modules;   
				
				
		
	}
}
