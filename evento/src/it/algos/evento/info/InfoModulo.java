package it.algos.evento.info;

import it.algos.webbase.web.module.Module;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class InfoModulo extends Module {

	public InfoModulo() {
		super();
		
		Resource resource = new ThemeResource("help/info_programma.html");		
		
		
		Component comp = new BrowserFrame("Help", resource);
		comp.setWidth("100%");
		comp.setHeight("100%");
		setCompositionRoot(comp);
	}
	
	

}
