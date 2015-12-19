package it.algos.evento.entities.prenotazione.eventi;

import com.vaadin.data.Item;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class EventoPrenModulo extends EModulePop {

	/**
	 * Costruttore senza parametri
	 * La classe implementa il pattern Singleton.
	 * Per una nuova istanza, usare il metodo statico getInstance.
	 * Usare questo costruttore SOLO con la Reflection dal metodo Module.getInstance
	 * Questo costruttore è pubblico SOLO per l'uso con la Reflection.
	 * Per il pattern Singleton dovrebbe essere privato.
	 *
	 * @deprecated
	 */
	public EventoPrenModulo() {
		super(EventoPren.class);
	}// end of constructor

	/**
	 * Crea una sola istanza di un modulo per sessione.
	 * Tutte le finestre e i tab di un browser sono nella stessa sessione.
	 */
	public static EventoPrenModulo getInstance(){
		return (EventoPrenModulo) ModulePop.getInstance(EventoPrenModulo.class);
	}// end of singleton constructor

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
