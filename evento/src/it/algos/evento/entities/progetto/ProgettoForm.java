package it.algos.evento.entities.progetto;

import com.vaadin.data.Item;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class ProgettoForm extends AForm {

	public ProgettoForm(Item item) {
		super(item);
		doInit();
	}

	public ProgettoForm(ModulePop modulo) {
		super(modulo);
		doInit();
	}// end of constructor

	public ProgettoForm(ModulePop modulo, Item item) {
		super(modulo, item);
		doInit();
	}// end of constructor

	private void doInit(){
		//setMargin(true);
	}

	@Override
	protected void createFields() {
		TextField tfield;

		tfield = new TextField("Descrizione");
		tfield.setWidth("260px");
		tfield.focus();
		addField(Progetto_.descrizione, tfield);
	}// end of method

}// end of class
