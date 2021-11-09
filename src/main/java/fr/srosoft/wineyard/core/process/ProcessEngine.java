package fr.srosoft.wineyard.core.process;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.process.dao.ProcessDao;
import fr.srosoft.wineyard.core.process.entities.ContentsProcessInstance;
import fr.srosoft.wineyard.core.process.entities.Identifiable;
import fr.srosoft.wineyard.core.process.entities.Process;
import fr.srosoft.wineyard.core.process.entities.Route;
import fr.srosoft.wineyard.core.process.entities.Step;
import fr.srosoft.wineyard.core.process.entities.StepInstance;
import fr.srosoft.wineyard.utils.WineyardUtils;



@Service
public class ProcessEngine {
	
	@Resource
	private ProcessDao processDao;

	public ContentsProcessInstance startsContentsProcess (String processId, Contents contents, String domainId, String actor) {
		ContentsProcessInstance context = new ContentsProcessInstance();
		context.setData(contents);
		context.setId(UUID.randomUUID().toString());
		context.setProcessId(processId);
		context.setDomainId(domainId);
		
		final Process process = this.getProcess(processId);
		this.copyCard(process, context);
	
		
		context.setCurrentStep(this.buildStepInstance(process.getStartPoint(),context, actor));
		
		WineyardUtils.stamp(context, true, actor);
		this.processDao.storeContentsProcessInstance(context);
		
		return context;
	}
	
	public ContentsProcessInstance playProcess(Route route,ContentsProcessInstance context, String actor) {
		
		final StepInstance newStep = this.buildStepInstance(route.getOutputStep(), context, actor);
		this.processDao.playProcess(context.getId(), newStep, route);
		return  this.processDao.findProcessInstanceById(context.getId());
	}
	
	public ContentsProcessInstance unwindProcess(ContentsProcessInstance context) {
		return  null;
	}
	
	
	private StepInstance buildStepInstance (Step step, ContentsProcessInstance context, String actor) {
		final StepInstance instance = new StepInstance();
		instance.setId(UUID.randomUUID().toString());
		instance.setStepId(step.getId());
		this.copyCard(step, instance);
		WineyardUtils.stamp(instance, true, actor);
		return instance;
	}
	
	public Step getCurrentStep (String processId, ContentsProcessInstance context) {
		return null;
	}
	
	/**
	 * @param processId
	 * @return process Object with level 1 (startPoint)
	 */
	public Process getProcess (String processId) {
		return processDao.findProcessById(processId);
	}
	
	private void copyCard (Identifiable source, Identifiable target) {
		target.setLabel(source.getLabel());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
	}
}
