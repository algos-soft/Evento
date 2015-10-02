package it.algos.evento.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public abstract class AbsMenuBarView extends VerticalLayout implements View {

	public AbsMenuBarView() {
		super();
		addComponent(new AdminMenuBar());
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
