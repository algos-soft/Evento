package it.algos.evento.entities.stagione;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import it.algos.evento.multiazienda.ESearchManager;
import it.algos.webbase.web.component.YesNoCheckboxComponent;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class StagioneSearch extends ESearchManager {


	public StagioneSearch(ModulePop module) {
		super(module);
	}// end of constructor

	public ArrayList<Filter> createFilters() {

		ArrayList<Filter> filters = new ArrayList();
		filters.add(createStringFilter(Stagione_.sigla));
		filters.add(createBoolFilter(Stagione_.corrente));

		return filters;

	}// end of method

}// end of class
