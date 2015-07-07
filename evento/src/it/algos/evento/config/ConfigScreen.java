package it.algos.evento.config;

import java.util.ArrayList;

import com.vaadin.ui.Accordion;

@SuppressWarnings("serial")
public class ConfigScreen extends Accordion {

	private ArrayList<ConfigComponent> configComponents;
	
	public ConfigScreen() {
		super();
		
		configComponents = new ArrayList<ConfigComponent>();
		configComponents.add(new CompanyDaemonConfigComponent());
		configComponents.add(new EmailConfigComponent());
		configComponents.add(new PrenConfigComponent());
		configComponents.add(new EventiConfigComponent());
		configComponents.add(new PersonalConfigComponent());
		
		for(ConfigComponent comp : configComponents){
			addTab(comp.getUIComponent(), comp.getTitle());
			comp.loadContent();
		}
		
		addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// potrei recuperare solo il singolo ma ricarico tutti
				for(ConfigComponent comp : configComponents){
					comp.loadContent();
				}
			}
		});
		
	}
	
	

}
