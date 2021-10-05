package fr.srosoft.wineyard.modules.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {
	
	public String name();
	public String label();
	public String description() default "";
	public int order();
	
	
}
