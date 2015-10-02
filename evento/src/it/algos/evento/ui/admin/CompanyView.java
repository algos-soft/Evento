package it.algos.evento.ui.admin;

import it.algos.evento.entities.company.CompanyModule;

public class CompanyView extends AbsMenuBarView  {

	public static final String NAME = "company";
    
	public CompanyView() {
		super();
		addComponent(new CompanyModule());
	}

}
