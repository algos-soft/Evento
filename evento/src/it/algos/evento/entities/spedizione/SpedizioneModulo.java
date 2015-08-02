package it.algos.evento.entities.spedizione;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;


@SuppressWarnings("serial")
public class SpedizioneModulo extends EModulePop {

	public SpedizioneModulo() {
		super(Spedizione.class);
	}// end of constructor

//	// come default spazzola tutti i campi della Entity
//	// non garantisce l'ordine con cui vengono presentati i campi
//	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
//	// può mostrare anche il campo ID, oppure no
//	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
//	// creaFieldsList, creaFieldsForm e creaFieldsSearch
//	protected Attribute<?, ?>[] creaFieldsAll() {
//		return new Attribute[] { Spedizione_.lettera, Spedizione_.destinatario, Spedizione_.operatore,
//				Spedizione_.spedita, Spedizione_.dataSpedizione, Spedizione_.errore };
//	}// end of method


	@Override
	public ATable createTable() {
		return (new SpedizioneTable());
	}// end of method
	
	@Override
	public SearchManager createSearchManager() {
		return new SpedizioneSearch();
	}// end of method


}// end of class
