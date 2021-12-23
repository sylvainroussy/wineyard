package fr.srosoft.wineyard.core.jsf.converters;

import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Chemical;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.cave.CaveModule;

@Component
@Scope("session")
public class ChemicalConverter implements Converter<Chemical> {
 
    @Resource private UserSession userSession;
     
    @Override
    public Chemical getAsObject(FacesContext fc, UIComponent uic, String value) {
    	
        if(value != null && value.trim().length() > 0) {
            try {
                return userSession.getModule("CaveModule", CaveModule.class)
                		.getContainerDetailForm().getAllChemicals()
                		.stream().filter(e -> e.getId().equals(value)).findFirst().get();
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
    public String getAsString(FacesContext fc, UIComponent uic, Chemical chemical) {
    	
        if(chemical != null) {
            return chemical.getId();
        }
        else {
            return null;
        }
    }   
}