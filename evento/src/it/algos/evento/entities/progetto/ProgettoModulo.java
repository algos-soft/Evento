package it.algos.evento.entities.progetto;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")
public class ProgettoModulo extends EModulePop {

	/**
	 * Costruttore senza parametri
	 */
	public ProgettoModulo() {
		super(Progetto.class);
	}// end of constructor

	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "progetti";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Progetto_.descrizione };
	}// end of method

	
	@Override
	public ModuleForm createForm(Item item) {
		return (new ProgettoForm(this, item));
	}// end of method

	@Override
	public SearchManager createSearchManager() {
		return new ProgettoSearch(this);
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono eventi collegati
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BaseEntity entity = getTable().getEntity((Long)id);
			List lista = EQuery.queryList(Evento.class, Evento_.progetto, entity);
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare i progetti selezionati perché sono collegati a degli eventi.\nEliminate prima gli eventi collegati o assegnateli ad altri progetti.", Notification.Type.WARNING_MESSAGE);
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
