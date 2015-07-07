package it.algos.evento.entities.evento;

import java.util.List;

import com.vaadin.data.Property;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.web.form.AForm;
import it.algos.web.search.SearchManager;
import it.algos.web.table.ATable;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;

import javax.persistence.metamodel.Attribute;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class EventoModulo extends EModulePop {

	public EventoModulo() {
		super(Evento.class);
	}// end of constructor

	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "eventi";
	}// end of method

//	// può essere sovrascritto nelle sottoclassi specifiche
//	// serve anche per l'ordine con cui vengono presentati i campi
//	protected Attribute<?, ?>[] creaFieldsList() {
//		return new Attribute[] { Evento_.id, Evento_.sigla, Evento_.titolo, Evento_.progetto, Evento_.stagione };
//	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsForm() {
		return new Attribute[] { Evento_.sigla, Evento_.titolo, Evento_.progetto, Evento_.importoIntero,
				Evento_.importoRidotto };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsSearch() {
		return new Attribute[] { Evento_.sigla, Evento_.titolo, Evento_.progetto, Evento_.stagione };
	}// end of method


	@Override
	public ATable createTable() {
		return (new EventoTable(this));
	}// end of method

	@Override
	public AForm createForm(Item item) {
		return (new EventoForm(this, item));
	}// end of method

	public SearchManager createSearchManager() {
		return new EventoSearch();
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono rappresentazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BeanItem item = getTable().getBeanItem(id);
			List lista = EQuery.queryList(Rappresentazione.class, Rappresentazione_.evento, item.getBean());
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare gli eventi selezionati perché ci sono delle rappresentazioni.\nEliminate prima le rappresentazioni collegate.", Notification.Type.WARNING_MESSAGE);
				cont=false;
				break;
			}
		}

		// se tutto ok ritorna il controllo alla superclasse
		if (cont) {
			super.delete();
		}
	}// end of method

	/**
	 * Post create / pre edit item.
	 * Assegna la stagione corrente ai nuovi record
	 */
	protected void postCreate(Item item) {
		Property prop = item.getItemProperty(Evento_.stagione.getName());
		if (prop!=null){
			prop.setValue(Stagione.getStagioneCorrente());
		}
	}// end of method

}// end of class
