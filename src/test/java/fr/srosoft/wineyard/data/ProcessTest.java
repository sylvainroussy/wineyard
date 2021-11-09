package fr.srosoft.wineyard.data;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.srosoft.wineyard.core.main.WineyardApplication;
import fr.srosoft.wineyard.core.model.entities.Contents;
import fr.srosoft.wineyard.core.process.ProcessEngine;
import fr.srosoft.wineyard.core.process.dao.ProcessDao;
import fr.srosoft.wineyard.core.process.entities.ContentsProcessInstance;
import fr.srosoft.wineyard.core.process.entities.Route;
import fr.srosoft.wineyard.core.process.entities.Step;
import fr.srosoft.wineyard.utils.Constants;

@SpringBootTest(classes= {WineyardApplication.class})
@RunWith(SpringRunner.class)
public class ProcessTest {

	private static final String PROCESS_ID = "alcoholic_fermentation";
	@Resource
	ProcessDao processDao;
	
	@Resource
	ProcessEngine processEngine;
	
	@Order(value = 1)
	@Test
	public void testStoreProcess() {
		fr.srosoft.wineyard.core.process.entities.Process process = new fr.srosoft.wineyard.core.process.entities.Process();
		process.setId(PROCESS_ID);
		process.setLabel("Fermentation Alcoolique");
		process.setName(PROCESS_ID);
		process.setDescription("Procédé de fermentation alcoolique");
		
		Step firstStep = new Step();
		firstStep.setId(UUID.randomUUID().toString());
		firstStep.setLabel(Constants.STATE_WAITING_ALCOHOLIC_FERMENTATION);
		process.setStartPoint(firstStep);
		
		Route pigeage = new Route();
		pigeage.setLabel("Pigeage");
		pigeage.setOutputStep(firstStep);
		pigeage.setId(UUID.randomUUID().toString());
		
		Route remontage = new Route();
		remontage.setLabel("Remontage");
		remontage.setOutputStep(firstStep);
		remontage.setId(UUID.randomUUID().toString());
		remontage.setOrder(1);
		
		
		Step secondStep = new Step();
		secondStep.setId(UUID.randomUUID().toString());
		secondStep.setLabel(Constants.STATE_IN_PROGRESS_ALCOHOLIC_FERMENTATION);
		
		Route fermentationStarts = new Route();
		fermentationStarts.setLabel("Fermentation commencée");
		fermentationStarts.setOutputStep(secondStep);
		fermentationStarts.setId(UUID.randomUUID().toString());
		fermentationStarts.setOrder(2);
		
		Step thirdStep = new Step();
		thirdStep.setId(UUID.randomUUID().toString());
		thirdStep.setLabel(Constants.STATE_FINISHED_ALCOHOLIC_FERMENTATION);
		
		Route fermentationEnds = new Route();
		fermentationEnds.setId(UUID.randomUUID().toString());
		fermentationEnds.setLabel("Fermentation terminée");
		fermentationEnds.setOutputStep(thirdStep);
		
		secondStep.addOutputRoute(fermentationEnds);
		
		
		firstStep.addOutputRoute(pigeage);
		firstStep.addOutputRoute(remontage);
		firstStep.addOutputRoute(fermentationStarts);
		
		processDao.storeProcess(process);
	}
	
	@Order(value = 1)
	@Test
	public void loadProcess() {
		final fr.srosoft.wineyard.core.process.entities.Process process = processDao.findProcessById(PROCESS_ID);
		Assert.assertNotNull(process.getStartPoint());
		
		
		Contents c= new Contents();
		c.setId(UUID.randomUUID().toString());
		c.setVolume(20);
		//c.setYear("2021");
		//c.setAppellation(null);
		
		
		ContentsProcessInstance instance = processEngine.startsContentsProcess(PROCESS_ID, c, "7801d94608e5120d87ebea4a0b76d704","Sylvain Roussy");
		
		instance = processDao.findProcessInstanceById(instance.getId());
		Assert.assertNotNull(instance.getCurrentStep());
		List<Route> routes = processDao.findRoutes(instance.getCurrentStep().getStepId());
		processEngine.playProcess(routes.get(0), instance, "sylvain roussy");
		
	}
}
