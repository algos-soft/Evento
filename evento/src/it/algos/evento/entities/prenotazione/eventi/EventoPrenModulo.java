package it.algos.evento.entities.prenotazione.eventi;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.web.form.AForm;
import it.algos.web.search.SearchManager;
import it.algos.web.table.ATable;
import it.algos.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Item;

@SuppressWarnings("serial")
public class EventoPrenModulo extends EModulePop {

	public EventoPrenModulo() {
		super(EventoPren.class);
	}// end of constructor

	@SuppressWarnings("rawtypes")
	protected Attribute[] creaFieldsForm() {
		return new Attribute[] { EventoPren_.timestamp, EventoPren_.tipo, EventoPren_.user, EventoPren_.dettagli };
	}// end of method

	/**
	 * Create the Table Portal
	 * 
	 * @return the TablePortal
	 */
	public TablePortal createTablePortal() {
		return new EventoPrenTablePortal(this);
	}

	@Override
	public ATable createTable() {
		return (new EventoPrenTable(this));
	}// end of method

	@Override
	public AForm createForm(Item item) {
		return (null);
	}// end of method

	@Override
	public SearchManager createSearchManager() {
		return new EventoPrenSearch();
	}// end of method

}
