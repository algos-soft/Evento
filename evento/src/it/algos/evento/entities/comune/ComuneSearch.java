package it.algos.evento.entities.comune;

import it.algos.evento.multiazienda.ESearchManager;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

import com.vaadin.data.Container.Filter;

@SuppressWarnings("serial")
public class ComuneSearch extends ESearchManager {

	public ComuneSearch(ModulePop module) {
		super(module);
	}// end of constructor

	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();

		filters.add(createStringFilter(Comune_.nome, SearchType.CONTAINS));
		filters.add(createStringFilter(Comune_.siglaProvincia, SearchType.MATCHES));
		return filters;
	}// end of method

}// end of class
