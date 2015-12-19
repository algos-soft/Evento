package it.algos.evento.entities.scuola;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")
public class ScuolaModulo extends EModulePop {

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
	public ScuolaModulo() {
		super(Scuola.class);
	}// end of constructor

	/**
	 * Crea una sola istanza di un modulo per sessione.
	 * Tutte le finestre e i tab di un browser sono nella stessa sessione.
	 */
	public static ScuolaModulo getInstance(){
		return (ScuolaModulo) ModulePop.getInstance(ScuolaModulo.class);
	}// end of singleton constructor

	// come default usa il titolo standard
	// può essere sovrascritto nelle sottoclassi specifiche
	protected String getCaptionSearch() {
		return "scuole";
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsList() {
		return new Attribute[] { Scuola_.sigla, Scuola_.nome, Scuola_.ordine, Scuola_.tipo, Scuola_.comune, Scuola_.telefono };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsForm() {
		return new Attribute[] { Scuola_.sigla, Scuola_.nome, Scuola_.ordine, Scuola_.email, Scuola_.telefono };
	}// end of method

	// come default spazzola tutti i campi della Entity
	// può essere sovrascritto nelle sottoclassi specifiche
	// serve anche per l'ordine con cui vengono presentati i campi
	protected Attribute<?, ?>[] creaFieldsSearch() {
		return new Attribute[] { Scuola_.sigla, Scuola_.nome, Scuola_.ordine, Scuola_.comune };
	}// end of method


	@Override
	public ATable createTable() {
		return (new ScuolaTable(this));
	}// end of method
	
	/**
	 * Create the Table Portal
	 * 
	 * @return the TablePortal
	 */
	public TablePortal createTablePortal() {
		return new ScuolaTablePortal(this);
	}// end of method


	@Override
	public AForm createForm(Item item) {
		return (new ScuolaForm(this, item));
	}// end of method

	@Override
	public SearchManager createSearchManager() {
		return new ScuolaSearch(this);
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono prenotazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BeanItem item = getTable().getBeanItem(id);
			List listaPren = EQuery.queryList(Prenotazione.class, Prenotazione_.scuola, item.getBean());
			if (listaPren.size()>0) {
				Notification.show("Impossibile eliminare le scuole selezionate perché ci sono delle prenotazioni.\nEliminate prima le prenotazioni collegate.", Notification.Type.WARNING_MESSAGE);
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
