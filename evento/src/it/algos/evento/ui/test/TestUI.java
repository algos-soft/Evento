package it.algos.evento.ui.test;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * Created by alex on 19/12/15.
 */
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Test UI"));
    }
}
