package it.algos.evento.entities.modopagamento;

import com.vaadin.data.Item;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class ModoPagamentoForm extends AForm {

//	public ModoPagamentoForm(ModulePop modulo) {
//		super(modulo);
//		doInit();
//	}// end of constructor

	public ModoPagamentoForm(ModulePop modulo, Item item) {
		super(modulo, item);
		doInit();
	}
	
	private void doInit(){
		//setMargin(true);
	}


}
