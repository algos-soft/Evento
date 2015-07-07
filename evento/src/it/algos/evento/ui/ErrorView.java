package it.algos.evento.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class ErrorView extends CustomComponent implements View {
    public static final String NAME = "error";
    public static String ERR_TEXT="";

    
	public ErrorView() {
		super();
		Label label = new Label(ERR_TEXT);
		setCompositionRoot(label);
	}


	@Override
	public void enter(ViewChangeEvent event) {
	}

}
