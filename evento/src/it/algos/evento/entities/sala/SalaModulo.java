package it.algos.evento.entities.sala;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")
public class SalaModulo extends EModulePop {

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
	public SalaModulo() {
		super(Sala.class);
	}// end of constructor

	/**
	 * Crea una sola istanza di un modulo per sessione.
	 * Tutte le finestre e i tab di un browser sono nella stessa sessione.
	 */
	public static SalaModulo getInstance(){
		return (SalaModulo) ModulePop.getInstance(SalaModulo.class);
	}// end of singleton constructor

	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "sale";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Sala_.nome, Sala_.capienza };
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono rappresentazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BeanItem item = getTable().getBeanItem(id);
			List lista = EQuery.queryList(Rappresentazione.class, Rappresentazione_.sala, item.getBean());
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare le sale selezionate perché ci sono delle rappresentazioni collegate.\nEliminate prima le rappresentazioni collegate o cambiate sala.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

}// end of class
