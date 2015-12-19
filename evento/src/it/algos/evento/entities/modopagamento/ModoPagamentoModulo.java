package it.algos.evento.entities.modopagamento;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

import javax.persistence.metamodel.Attribute;
import java.util.List;

@SuppressWarnings("serial")

public class ModoPagamentoModulo extends EModulePop {

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
	public ModoPagamentoModulo() {
		super(ModoPagamento.class);
	}// end of constructor

	/**
	 * Crea una sola istanza di un modulo per sessione.
	 * Tutte le finestre e i tab di un browser sono nella stessa sessione.
	 */
	public static ModoPagamentoModulo getInstance(){
		return (ModoPagamentoModulo) ModulePop.getInstance(ModoPagamentoModulo.class);
	}// end of singleton constructor

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { ModoPagamento_.sigla, ModoPagamento_.descrizione };
	}// end of method

	@Override
	public ATable createTable() {
		return (new ModoPagamentoTable(this));
	}// end of method

	@Override
	public AForm createForm(Item item) {
		return (new ModoPagamentoForm(this, item));
	}// end of method

	/**
	 * Delete selected items button pressed
	 */
	public void delete() {
		
		// prima controlla se ci sono prenotazioni collegate
		boolean cont=true;
		for (Object id : getTable().getSelectedIds()) {
			BeanItem item = getTable().getBeanItem(id);
			List lista = EQuery.queryList(Prenotazione.class, Prenotazione_.modoPagamento, item.getBean());
			if (lista.size()>0) {
				Notification.show("Impossibile eliminare i tipi di pagamento selezionati perché ci sono delle prenotazioni collegate.\nEliminate prima le prenotazioni collegate o cambiate il tipo di pagamento.", Notification.Type.WARNING_MESSAGE);
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
