package fr.srosoft.wineyard.core.jsf.converters;

import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.srosoft.wineyard.core.model.entities.Domain;
import fr.srosoft.wineyard.core.session.UserSession;
import fr.srosoft.wineyard.modules.admin.AdminModule;

@Component
@Scope("session")
public class DomainConverter implements Converter {
 
    @Resource private UserSession userSession;
     
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
    	
        if(value != null && value.trim().length() > 0) {
            try {
                return ((AdminModule)userSession.getModule("AdminModule"))
                		.getAllDomains()
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
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
    	
        if(object != null) {
            return String.valueOf(((Domain) object).getId());
        }
        else {
            return null;
        }
    }   
}