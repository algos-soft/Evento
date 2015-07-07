package it.algos.evento.entities.modopagamento;

import it.algos.web.form.AForm;
import it.algos.web.module.ModulePop;

import com.vaadin.data.Item;

@SuppressWarnings("serial")
public class ModoPagamentoForm extends AForm {

	public ModoPagamentoForm(ModulePop modulo) {
		super(modulo);
		doInit();
	}// end of constructor

	public ModoPagamentoForm(ModulePop modulo, Item item) {
		super(modulo, item);
		doInit();
	}// end of constructor
	
	private void doInit(){
		//setMargin(true);
	}


}
