package it.algos.evento.entities.spedizione;

import com.vaadin.data.Item;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;


@SuppressWarnings("serial")
public class SpedizioneModulo extends EModulePop {

	public SpedizioneModulo() {
		super(Spedizione.class);
	}// end of constructor

	@Override
	public AForm createForm(Item item) {
		return (new SpedizioneForm(this, item));
	}// end of method

	@Override
	public ATable createTable() {
		return (new SpedizioneTable());
	}// end of method
	
	@Override
	public SearchManager createSearchManager() {
		return new SpedizioneSearch();
	}// end of method


}// end of class
