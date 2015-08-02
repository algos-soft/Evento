package it.asteria.cultura.preferenze;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;

public class PreferenzeTable extends ATable {

	protected static Object[] visibleColumns = { Preferenze_.type, Preferenze_.code, Preferenze_.stringa,
			Preferenze_.bool, Preferenze_.intero };

	public PreferenzeTable(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	protected Object[] getDisplayColumns() {
		return new Object[] { Preferenze_.type, Preferenze_.code, Preferenze_.stringa, Preferenze_.bool,
				Preferenze_.intero };
	}// end of method

}// end of class
