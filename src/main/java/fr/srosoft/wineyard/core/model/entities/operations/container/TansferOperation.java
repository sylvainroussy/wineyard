package fr.srosoft.wineyard.core.model.entities.operations.container;

import java.util.List;

import fr.srosoft.wineyard.core.model.entities.Container;
import fr.srosoft.wineyard.core.model.entities.WineyardValue;
import fr.srosoft.wineyard.core.model.entities.operations.ContainerOperation;
import fr.srosoft.wineyard.core.model.entities.operations.container.TansferOperation.TransferTask;

@SuppressWarnings("serial")
public class TansferOperation extends ContainerOperation<List<TransferTask>>{

	public static class TransferTask {
		private Container targetContainer;
		private WineyardValue value;
		
		public Container getTargetContainer() {
			return targetContainer;
		}
		public void setTargetContainer(Container targetContainer) {
			this.targetContainer = targetContainer;
		}
		public WineyardValue getValue() {
			return value;
		}
		public void setValue(WineyardValue value) {
			this.value = value;
		}
	}
}
