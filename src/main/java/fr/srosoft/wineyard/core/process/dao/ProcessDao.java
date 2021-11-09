package fr.srosoft.wineyard.core.process.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.dao.AbstractDao;
import fr.srosoft.wineyard.core.process.entities.ContentsProcessInstance;
import fr.srosoft.wineyard.core.process.entities.Process;
import fr.srosoft.wineyard.core.process.entities.Route;
import fr.srosoft.wineyard.core.process.entities.Step;
import fr.srosoft.wineyard.core.process.entities.StepInstance;

@Service
public class ProcessDao extends AbstractDao {

	private static final String QUERY_BUILD_PROCESS =" WITH $patch as data "
			+ "MERGE (process:Process{id:data.id}) "
			+ "SET process+=data {.name, .label, .description, .enabled, .version} "
			+ "WITH process, data.startPoint as startPoint "
			+ "MERGE (start:Step{id:startPoint.id})<-[:STARTS_WITH]-(process) "
			+ "SET start+=startPoint {.name, .label, .description} "
			+ "WITH process "
			+ "UNWIND $steps AS map "
			+ "MERGE (step:Step{id:map.id}) "
			+ "SET step+=map {.name, .label, .description} "
			+ "WITH $routes AS routes "			
			+ "UNWIND routes as route "
			+ "MATCH (source:Step{id:route.sourceId}) "
			+ "MATCH (target:Step{id:route.targetId}) "
			+ "MERGE (source)-[r:LEADS_TO{id:route.id}]->(target) "
			+ "SET r+=route{.name, .label, .description, .order}";
	
	private static final String QUERY_DELETE_PROCESS = "MATCH (process:Process{id:$processId}) "
			+ "MATCH p=(process)-[:STARTS_WITH|LEADS_TO*]->(something) "
			+ "WITH nodes(p) AS nodes "
			+ "UNWIND nodes As node "
			+ "DETACH DELETE node ";
	
	private static final String QUERY_FIND_PROCESS_BY_ID = "MATCH (process:Process{id:$processId}) "
			+ "MATCH (process)-[:STARTS_WITH]->(startPoint:Step) "
			+ "RETURN process{.*, startPoint : startPoint {.*}} AS process ";
	
	
	private static final String QUERY_FIND_ROUTES = "MATCH (step:Step{id:$stepId}) "
			+ "MATCH (step)-[route:LEADS_TO]->(destination:Step) "
			+ "RETURN route{.*, outputStep : destination {.*}} AS route  ORDER BY route.order ASC";
	
	// INSTANCES	
	private static final String QUERY_BUILD_PROCESS_INSTANCE =""
			+ "WITH $patch AS patch "
			+ "MERGE (context:ProcessInstance{id:patch.id}) "
			+ "SET context+=patch{.creationDate, .creationUser, .lastUpdateDate, .lastUpdateDateUser, .name, .label, .description,.title,  .processId, .domainId } "
			+ "MERGE (context)<-[:MANAGED_BY]-(content:Contents{id:patch.data.id}) "
			+ "WITH context, patch.currentStep AS currentStep "
			+ "MERGE (step:StepInstance{id:currentStep.id}) "
			+ "SET step+=currentStep{.creationDate, .creationUser, .lastUpdateDate, .lastUpdateDateUser, .name, .label, .description, .stepId } "
			+ "MERGE (context)-[:STARTS_WITH]->(step) "
			+ "MERGE (context)-[:CURRENT_STEP]->(step) ";
	
	private static final String QUERY_FIND_PROCESS_INSTANCE_BY_ID = "MATCH (context:ProcessInstance{id:$contextId}) "
			+ "MATCH (context)-[:CURRENT_STEP]->(current:StepInstance) "
			+ "RETURN context{.*, currentStep : current{.*}} AS context ";
	
	private static final String QUERY_PLAY_PROCESS = "MATCH (context:ProcessInstance{id:$contextId}) "
			+ "MATCH (context)-[r:CURRENT_STEP]->(oldCurrent:StepInstance) "
			+ "DELETE r "
			+ "WITH context, oldCurrent, $patch  AS patch, $route AS route "
			+ "MERGE (newCurrent:StepInstance{id:patch.id}) "
			+ "SET newCurrent+=patch{.creationDate, .creationUser, .lastUpdateDate, .lastUpdateDateUser, .name, .label, .description, .stepId } "
			+ "MERGE (context)-[:CURRENT_STEP]->(newCurrent) "
			+ "MERGE (oldCurrent)-[r:LEADS_TO]->(newCurrent) "
			+ "SET r+=route{.name, .label, .description, .order} ";
			
			
	
	
	public void storeProcess (Process process) {
		final Map<String,Object> parameters = new HashMap<>();
		
		// process and stapoint objects
    	final Map<?,?> patch =  MAPPER.convertValue(process, Map.class);
    	parameters.put("patch", patch);
    	
    	// All Steps (without routes)
    	final ArrayList<Step> steps = new ArrayList<>();
    	this.loadAllSteps(process.getStartPoint(), steps);
    	final List lsteps =  MAPPER.convertValue(steps, List.class);
    	parameters.put("steps", lsteps);
    	
    	// All routes 
    	final List<Map<String,Object>> routes = new ArrayList<>();
    	for (Step step : steps) {
			this.loadAllRoutes(step, routes);
		}
    	parameters.put("routes", routes);
    	
    	Map<String,Object> preParameters = new HashMap<>();
    	preParameters.put("processId", process.getId());
    	
    	this.writeQuery(QUERY_BUILD_PROCESS, parameters,QUERY_DELETE_PROCESS,preParameters);
	}
	
	/**
	 * Load Process object with first level (startPoint) 
	 * @param processId
	 * @return
	 */
	public Process findProcessById(String processId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("processId",processId);    		
    	return this.readSingleQuery(QUERY_FIND_PROCESS_BY_ID, parameters,"process",Process.class);
	}
	
	public List<Route> findRoutes (String stepId){
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("stepId",stepId);    		
    	return this.readMultipleQuery(QUERY_FIND_ROUTES, parameters,"route",Route.class);
	}
	
	private void loadAllSteps (Step currentStep, List<Step> steps) {
		if (currentStep == null)  return;
		steps.add(currentStep);
		final List<Route> routes = currentStep.getOutputRoutes();
		if (routes != null) {
			for (Route route : routes) {
				if (!steps.contains(route.getOutputStep())) {
					this.loadAllSteps(route.getOutputStep(), steps);
				}
			}
		}
		
	}
	
	private void loadAllRoutes (Step currentStep, List<Map<String,Object>> maps) {
		if (currentStep == null)  return;
		
		final List<Route> routes = currentStep.getOutputRoutes();
		if (routes != null) {
			for (Route route : routes) {
				Map<String,Object> map = new  HashMap<>();
				final Step outputStep =  route.getOutputStep();
				map.put("id", route.getId());
				map.put("label", route.getLabel());
				map.put("name", route.getName());
				map.put("description", route.getDescription());
				map.put("sourceId", currentStep.getId());
				map.put("targetId", outputStep.getId());
				maps.add(map);
				
			}
		}
		
	}
	
	// Instance
	public void storeContentsProcessInstance (ContentsProcessInstance processInstance) {
		final Map<String,Object> parameters = new HashMap<>();
		
		// process and stapoint objects
    	final Map<?,?> patch =  MAPPER.convertValue(processInstance, Map.class);
    	parameters.put("patch", patch);
    	this.writeQuery(QUERY_BUILD_PROCESS_INSTANCE, parameters);
	}
	
	public ContentsProcessInstance findProcessInstanceById(String processInstanceId) {
		final Map<String,Object> parameters = new HashMap<>();    	    	
    	parameters.put("contextId",processInstanceId);    		
    	return this.readSingleQuery(QUERY_FIND_PROCESS_INSTANCE_BY_ID, parameters,"context",ContentsProcessInstance.class);
	}
	
	public void playProcess(String contextId, StepInstance stepInstance, Route route) {
		final Map<String,Object> parameters = new HashMap<>();
    	final Map<?,?> patch =  MAPPER.convertValue(stepInstance, Map.class);
    	final Map<?,?> routePatch =  MAPPER.convertValue(route, Map.class);
    	parameters.put("patch", patch);
    	parameters.put("route", routePatch);
    	parameters.put("contextId", contextId);
    	this.writeQuery(QUERY_PLAY_PROCESS, parameters);
	}
	
}
