package it.algos.evento;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import static it.algos.evento.EventoApp.CONF_PAGE;


@SuppressWarnings("serial")
@WebServlet(value = "/"+CONF_PAGE+"/*", asyncSupported = true, displayName = "eVento - conferma")
@VaadinServletConfiguration(productionMode = false, ui = ConfermaPrenUI.class)
public class ConfermaPrenUIServlet extends VaadinServlet {}

