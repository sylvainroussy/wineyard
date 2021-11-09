package fr.srosoft.wineyard.core.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.ContainerTemplate;

@Service
public class ContainerDao extends AbstractDao {

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
	
	private static String QUERY_CONTAINER_FIND_BY_DOMAIN_AND_TYPE = "MATCH (domain:Domain{id:$domainId}) "
			+ "MATCH (module:CaveModule)<-[:HAS_MODULE]-(domain) "
			+ "MATCH (module)-[:HAS_CONTAINER]->(container) WHERE $containerType IN labels(container)"
			+ "RETURN container ORDER BY container.id ASC ";
			
			
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
	
	public <T extends Container> List<T> findContainersByDomainAndType (String domainId,Class<T> containerType) {		
		
		return this.readMultipleQuery(QUERY_CONTAINER_FIND_BY_DOMAIN_AND_TYPE, Map.of("domainId", domainId, "containerType",containerType.getSimpleName()),"container",containerType);
	}
}
