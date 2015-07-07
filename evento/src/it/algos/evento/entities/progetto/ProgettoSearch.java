package it.algos.evento.entities.progetto;

import it.algos.evento.multiazienda.ESearchManager;
import it.algos.web.module.ModulePop;
import it.algos.web.search.SearchManager;

import java.util.ArrayList;

import com.vaadin.data.Container.Filter;

@SuppressWarnings("serial")
public class ProgettoSearch extends ESearchManager {

	public ProgettoSearch(ModulePop module) {
		super(module);
	}// end of constructor

	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(createStringFilter(Progetto_.descrizione, SearchManager.SearchType.CONTAINS));
		return filters;
	}// end of method

}// end of Search class
