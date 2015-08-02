package it.algos.evento.entities.insegnante;

import com.vaadin.ui.FormLayout;
import it.algos.evento.multiazienda.ESearchManager;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

import com.vaadin.data.Container.Filter;
import it.algos.webbase.web.component.YesNoCheckboxComponent;

@SuppressWarnings("serial")
public class InsegnanteSearch extends ESearchManager {

	private YesNoCheckboxComponent fPrivato;


	public InsegnanteSearch(ModulePop module) {
		super(module);
	}// end of constructor

	protected void createFields(FormLayout layout) {
		super.createFields(layout);



//		fPrivato = new YesNoCheckboxComponent("Privato");
//		layout.addComponent(fPrivato);

//		Field field;
//
//		field = new TextField("cognome");
//		addField(Insegnante_.cognome.getName(), field);
//		layout.addComponent(field);
//
//		field = new TextField("email");
//		addField(Insegnante_.email.getName(), field);
//		layout.addComponent(field);
//
//		field = new TextField("materie");
//		addField(Insegnante_.materie.getName(), field);
//		layout.addComponent(field);
//
//		field = new TextField("note");
//		addField(Insegnante_.note.getName(), field);
//		layout.addComponent(field);

	}// end of method

	public ArrayList<Filter> createFilters() {


		ArrayList<Filter> filters = new ArrayList<>();
		filters.add(createStringFilter(Insegnante_.cognome));
		filters.add(createStringFilter(Insegnante_.email, SearchType.CONTAINS));

		RelatedComboField fOrdine=(RelatedComboField)getField(Insegnante_.ordineScuola);
		filters.add(createBeanFilter(fOrdine, Insegnante_.ordineScuola));

		filters.add(createStringFilter(Insegnante_.materie, SearchType.CONTAINS));
		filters.add(createStringFilter(Insegnante_.note, SearchType.CONTAINS));
		filters.add(createBoolFilter(Insegnante_.privato));
		return filters;
	}// end of method

}// end of class
