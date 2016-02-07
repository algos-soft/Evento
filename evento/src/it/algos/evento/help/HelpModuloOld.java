package it.algos.evento.help;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import it.algos.webbase.web.module.Module;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class HelpModuloOld extends Module {
	
	public HelpModuloOld()  {
		super();
		
		Resource resource = new ThemeResource("help_old/index.html");

		Component comp = new BrowserFrame("Help", resource);
		comp.setWidth("100%");
		comp.setHeight("100%");
		setCompositionRoot(comp);
	}



}
