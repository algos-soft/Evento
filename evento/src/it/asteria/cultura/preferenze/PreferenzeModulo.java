package it.asteria.cultura.preferenze;

import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class PreferenzeModulo extends ModulePop {

	public PreferenzeModulo() {
		super(Preferenze.class);
	}// end of constructor

	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Preferenze_.type, Preferenze_.code, Preferenze_.stringa, Preferenze_.bool,
				Preferenze_.intero };
	}// end of method

	// @Override
	// public ATable createTable() {
	// return (new PreferenzeTable(this));
	// }// end of method

	// @Override
	// public AForm createForm(Item item) {
	// return (new PreferenzeForm(this, item));
	// }// end of method

}// end of class
