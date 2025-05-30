package fr.srosoft.wineyard.core.jsf.converters;

import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.cave.CaveModule;
import fr.srosoft.wineyard.modules.cave.TargetContainer;


@Component
@Scope("session")
public class TargetContainerConverter  implements Converter<TargetContainer> {

	 @Resource private UserSession userSession;
	
	@Override
	public TargetContainer getAsObject(FacesContext context, UIComponent component, String value) {

        if(value != null && value.trim().length() > 0) {
            try {
                return ((CaveModule)userSession.getModule("CaveModule"))
                		.getPossibleContainers()
                		.stream().filter(e -> e.getContainer().getId().equals(value)).findFirst().get();
            } catch(Exception e) {
            	e.printStackTrace();
            	throw new RuntimeException(e);
               
            }
        }
        else {
            return null;
        }
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, TargetContainer value) {
		 if(value != null) {
	            return value.getContainer().getId();
	        }
	        else {
	            return null;
	        }
	}

}
