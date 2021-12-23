package fr.srosoft.wineyard.core.model.dao;

import fr.srosoft.wineyard.core.model.entities.operations.Operation;

public class OperationDao extends AbstractDao {

	private String QUERY_STORE_OPERATION = "M";
	
	public void saveOperation(Operation<?,?> operation) {
		//operation.getInput()
	}
}
