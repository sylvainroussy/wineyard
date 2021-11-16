package fr.srosoft.wineyard.core.model.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.ContainerTemplate;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.model.entities.Cuvee;
import fr.srosoft.wineyard.utils.Constants.STATE_CONTAINER;

@Service
public class ContainerDao extends AbstractDao {

	@Resource
	private CuveeDao cuveeDao;
	
	
	private static String QUERY_CONTAINER_TEMPLATE_SAVE = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "
			+ "MERGE (template:ContainerTemplate {id:$id}) "
			+ "SET template+=$patch "
			+ "MERGE (module)-[:HAS_CONTAINER_TEMPLATE]->(template) ";
	
	private static String QUERY_CONTAINER_TEMPLATE_FIND_BY_DOMAIN = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "
			+ "MATCH (module)-[:HAS_CONTAINER_TEMPLATE]->(template) "
			+ "RETURN template ORDER BY template.model ASC ";
	
	private static String QUERY_CONTAINER_SAVE_ALL = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "
			+ "MATCH (template:ContainerTemplate {id:$templateId}) "
			+ "UNWIND $containers AS pcontainer "
			+ "MERGE (container:Container{id:pcontainer.id}) "
			+ "SET container+=pcontainer "			
			+ "MERGE (module)-[:HAS_CONTAINER]->(container) "
			+ "MERGE (template)-[:DEFINES]->(container) "
			+ "WITH container "
			+ "CALL apoc.create.addLabels(container, [apoc.text.upperCamelCase($containerType)]) YIELD node "
			+ "RETURN DISTINCT 1";
	
	private static String QUERY_CONTAINER_UPDATE = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "			
			+ "MERGE (module)-[:HAS_CONTAINER]->(container{id:$id}) "
			+ "SET container+=$patch ";	
			
	
	private static String QUERY_CONTAINER_FIND_BY_DOMAIN_AND_TYPE = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "
			+ "MATCH (module)-[:HAS_CONTAINER]->(container) WHERE $containerType IN labels(container) "			
			+ "RETURN container ORDER BY container.number ASC, container.id ASC ";
	
	
			
	// *** Containers templates
	
	public void saveContainerTemplate (ContainerTemplate containerTemplate, String domainId) {
		final Map<?,?> patch =  MAPPER.convertValue(containerTemplate, Map.class);
		patch.remove("id");
		this.writeQuery(QUERY_CONTAINER_TEMPLATE_SAVE, Map.of("domainId", domainId, "id", containerTemplate.getId(),"patch",patch));
	}
	
	public List<ContainerTemplate> findContainerTemplatesByDomain (String domainId){
		return this.readMultipleQuery(QUERY_CONTAINER_TEMPLATE_FIND_BY_DOMAIN, Map.of("domainId", domainId), "template", ContainerTemplate.class);
	}
	
	// *** containers
	@SuppressWarnings("unchecked")
	public void saveContainers (List<Container> containers, String domainId,String containerType, String templateId) {
		final List<Map<?,?>> patch =  MAPPER.convertValue(containers, List.class);
		patch.stream().forEach(e -> {
			e.remove("actions");
			e.remove("contents");
		});
		
		this.writeQuery(QUERY_CONTAINER_SAVE_ALL, Map.of("domainId", domainId, "containers",patch,"templateId",templateId, "containerType", containerType));
	}
	
	public void updateContainer (Container container, String domainId,String containerType) {
		final Map<?,?> patch =  MAPPER.convertValue(container, Map.class);
		patch.remove("id");
		patch.remove("actions");
		patch.remove("contents");
		this.writeQuery(QUERY_CONTAINER_UPDATE, Map.of("id",container.getId(),"domainId", domainId, "patch",patch, "containerType", containerType));
	}
	
	public <T extends Container> List<T> findContainersByDomainAndType (String domainId,Class<T> containerType) {		
		
		return this.readMultipleQuery(QUERY_CONTAINER_FIND_BY_DOMAIN_AND_TYPE, Map.of("domainId", domainId, "containerType",containerType.getSimpleName()),"container",containerType);
	}
	
	// *** Contents
	/**
	 * private Cuvee cuvee;
	
	
	private TraceLine traceLine = new TraceLine();
	private List<Contents> parents;
	 * @param contents
	 * @param containerId
	 * @param domainId
	 */
	private static String QUERY_ADD_CONTENT_TO_CONTAINER="MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "			
			+ "MATCH (module)-[:HAS_CONTAINER]->(container{id:$containerId}) "
			+ "MATCH (cuvee:Cuvee{id:$cuveeId}) "
			+ "MERGE (contents:Contents{id:$id}) "
			+ "SET contents+=$patch "
			+ "MERGE (cuvee)<-[:ATTACHED_TO]-(contents) "
			+ "WITH contents, container "			
			+ "OPTIONAL MATCH (container)-[r:CURRENT_CONTENT]->(current) "
			+ "DELETE r "
			+ "MERGE (container)-[:CURRENT_CONTENT]->(contents) "
			+ "MERGE (container)<-[:HAD_CONTAINER{when:timestamp()}]-(contents) "
			+ "SET container.status='STATE_CONTAINER_USED'";
	
	public void addContentToContainer (Contents contents, String containerId, String domainId) {
		final Map<?,?> patch =  MAPPER.convertValue(contents, Map.class);
		patch.remove("id");
		patch.remove("traceLine");
		patch.remove("parents");
		Map<?,?> pCuvee = (Map<?, ?>) patch.remove("cuvee");
		this.writeQuery(QUERY_ADD_CONTENT_TO_CONTAINER, Map.of("id",contents.getId(),"domainId", domainId,"containerId",containerId, "patch",patch,"cuveeId",pCuvee.get("id")));
	}
	
	
	private static String QUERY_FIND_CURRENT_CONTENT_FROM_CONTAINER="MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "			
			+ "MATCH (module)-[:HAS_CONTAINER]->(container{id:$containerId}) "
			+ "MATCH (container)-[:CURRENT_CONTENT]->(contents:Contents) "
			+ "MATCH (cuvee)<-[:ATTACHED_TO]-(contents) "
			+ "RETURN contents{.*, cuvee:cuvee{.*}} AS contents";
			
	
	/**
	 * Load the contents and assign it into container object
	 * @param container
	 * @param domainId
	 * @return the assigned contents
	 */
	public Contents loadContentsFromContainer (Container container, String domainId) {
		Contents contents = null;
		try {
			contents = this.readSingleQuery(QUERY_FIND_CURRENT_CONTENT_FROM_CONTAINER, Map.of("domainId",domainId,"containerId",container.getId()),"contents" , Contents.class);
			final Cuvee cuvee = cuveeDao.findCuveeById(contents.getCuvee().getId());
			contents.setCuvee(cuvee);
			container.setContents(contents);
		}
		catch (NoSuchRecordException err) {
			// do nothing, just return null
		}
		return contents;
	}
	
	private static String QUERY_CONTAINERS_FIND_BY_STATUS="MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "			
			+ "MATCH (module)-[:HAS_CONTAINER]->(container:Container) "
			+ "WHERE $containerType IN labels(container) AND container.status =$status "			
			+ "RETURN container ORDER BY container.number ASC, container.id ASC ";
			
	public <T extends Container> List<T> findContainersByStatus(String domainId, Class<T> containerType, STATE_CONTAINER status){
		return this.readMultipleQuery(QUERY_CONTAINERS_FIND_BY_STATUS, Map.of("domainId", domainId, "containerType",containerType.getSimpleName(),"status",status.name()),"container",containerType);
	}
	
	private static String QUERY_CONTAINERS_FIND_TYPES="MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "			
			+ "MATCH (module)-[:HAS_CONTAINER]->(container:Container) "
			+ "WHERE  container.status =$status "
			+ "WITH labels(container) AS labels "
			+ "UNWIND labels AS type "
			+ "WITH type WHERE type<>'Container'"			
			+ "RETURN DISTINCT type oRDER BY type ASC ";
	public List<String> getContainerTypesByStatus(String domainId, STATE_CONTAINER status) {
		return this.readMultipleQuery(QUERY_CONTAINERS_FIND_TYPES, Map.of("domainId", domainId,"status",status.name()),"type",String.class);
	}
}
