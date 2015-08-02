package it.asteria.cultura.preferenze;

import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class PreferenzeForm extends AForm {

	public PreferenzeForm(ModulePop modulo) {
		super(modulo);
		doInit();
	}// end of constructor

	public PreferenzeForm(ModulePop modulo, Item item) {
		super(modulo, item);
		doInit();
	}// end of constructor
	
	private void doInit(){
		//setMargin(true);
	}


	/**
	 * Populate the map to bind item properties to fields.
	 * 
	 * Crea e aggiunge i campi. Implementazione di default nella superclasse. I campi vengono recuperati dal Modello. I
	 * campi vengono cretai del tipo grafico previsto nella Entity. Se si vuole aggiungere un campo (solo nel form e non
	 * nel Modello), usare il metodo sovrascritto nella sottoclasse richiamando prima il metodo della superclasse.
	 */
	protected void createFields() {
		super.createFields();
	}// end of method

	/**
	 * Create the layout.
	 * 
	 * Retrieve the fields from the map and place them in the UI. Implementazione di default nella superclasse. I campi
	 * vengono allineati verticalmente. Se si vuole aggiungere un campo, usare il metodo sovrascritto nella sottoclasse
	 * richiamando prima il metodo della superclasse. Se si vuole un layout completamente differente, implementare il
	 * metodo sovrascritto da solo.
	 */
	protected Component createComponent() {
		return super.createComponent();
	}// end of method

}// end of class

