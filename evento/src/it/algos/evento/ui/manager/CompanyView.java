package it.algos.evento.ui.manager;

import it.algos.evento.entities.company.CompanyModule;

public class CompanyView extends AbsMenuBarView  {

	public static final String NAME = "company";
    
	public CompanyView() {
		super();
		addComponent(new CompanyModule());
	}

}
