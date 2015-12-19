package it.algos.evento.entities.spedizione;

import com.vaadin.data.Item;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;


@SuppressWarnings("serial")
public class SpedizioneModulo extends EModulePop {

	/**
	 * Costruttore senza parametri
	 * La classe implementa il pattern Singleton.
	 * Per una nuova istanza, usare il metodo statico getInstance.
	 * Usare questo costruttore SOLO con la Reflection dal metodo Module.getInstance
	 * Questo costruttore è pubblico SOLO per l'usa con la Reflection.
	 * Per il pattern Singleton dovrebbe essere privato.
	 *
	 * @deprecated
	 */
	public SpedizioneModulo() {
		super(Spedizione.class);
	}// end of constructor

	/**
	 * Crea una sola istanza di un modulo per sessione.
	 * Tutte le finestre e i tab di un browser sono nella stessa sessione.
	 */
	public static SpedizioneModulo getInstance(){
		return (SpedizioneModulo) ModulePop.getInstance(SpedizioneModulo.class);
	}// end of singleton constructor

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
