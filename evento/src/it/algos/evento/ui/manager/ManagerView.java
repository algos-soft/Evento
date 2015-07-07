package it.algos.evento.ui.manager;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

public class ManagerView extends VerticalLayout implements View {

	public static final String NAME = "manager";

	private Component mainComponent;
	
	public ManagerView() {
		super();
		
		MenuBar mb = new ManagerMenuBar();
		mb.setWidth("100%");
		addComponent(mb);
		
		mainComponent=new Label("ciao");
		addComponent(mainComponent);
		setExpandRatio(mainComponent, 1.0f);

		
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
