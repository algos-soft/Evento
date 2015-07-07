package it.algos.evento.entities.lettera;

import it.algos.evento.multiazienda.ETable;
import it.algos.web.module.ModulePop;

@SuppressWarnings("serial")
public class LetteraTable extends ETable {

	public LetteraTable(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	// protected Object[] getDisplayColumns() {
	// return new Object[] { Lettera_.sigla, Lettera_.oggetto, Lettera_.allegati };
	// }// end of method

}// end of class
