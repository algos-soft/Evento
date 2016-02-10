package it.algos.evento.entities.insegnante;

import com.vaadin.data.Item;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.multiazienda.ERelatedComboField;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class InsegnanteForm extends ModuleForm {

	private CheckBoxField fieldPrivato;
	private ERelatedComboField fieldOrdineScuola;
	private TextField fieldMaterie;

//	public InsegnanteForm(Item item) {
//		this(null, item);
//	}

	public InsegnanteForm(Item item, ModulePop modulo) {
		super(item, modulo);
		doInit();
	}// end of constructor
	
	private void doInit(){
		//setMargin(true);
	}

	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;

		field = new TextField("Titolo");
		field.setWidth("80px");
		field.focus();
		addField(Insegnante_.titolo, field);

		field = new TextField("Cognome");
		field.setWidth("200px");
		addField(Insegnante_.cognome, field);

		field = new TextField("Nome");
		field.setWidth("200px");
		addField(Insegnante_.nome, field);

		fieldOrdineScuola = new ERelatedComboField(OrdineScuola.class, "Ordine scuola");
		fieldOrdineScuola.setWidth("180px");
		addField(Insegnante_.ordineScuola, fieldOrdineScuola);

		fieldMaterie = new TextField("Materie");
		fieldMaterie.setWidth("200px");
		addField(Insegnante_.materie, fieldMaterie);

		field = new EmailField("E-mail");
		addField(Insegnante_.email, field);

		field = new TextField("Telefono");
		field.setWidth("300px");
		addField(Insegnante_.telefono, field);

		field = new TextField("Indirizzo");
		field.setWidth("300px");
		addField(Insegnante_.indirizzo1, field);

		field = new TextField("Località");
		field.setWidth("300px");
		addField(Insegnante_.indirizzo2, field);

		field = new TextField("Note");
		field.setWidth("300px");
		addField(Insegnante_.note, field);

		fieldPrivato = new CheckBoxField("Privato");
		addField(Insegnante_.privato, fieldPrivato);
		fieldPrivato.addValueChangeListener(event -> {
			onPrivatoChange();
		});



	}

	protected Component createComponent() {
		AFormLayout layout = new AFormLayout();
		layout.setMargin(true);
		layout.addComponent(getField(Insegnante_.titolo));
		layout.addComponent(getField(Insegnante_.cognome));
		layout.addComponent(getField(Insegnante_.nome));
		layout.addComponent(getField(Insegnante_.privato));
		layout.addComponent(fieldOrdineScuola);
		layout.addComponent(fieldMaterie);
		layout.addComponent(getField(Insegnante_.email));
		layout.addComponent(getField(Insegnante_.telefono));
		layout.addComponent(getField(Insegnante_.indirizzo1));
		layout.addComponent(getField(Insegnante_.indirizzo2));
		layout.addComponent(getField(Insegnante_.note));

		// dopo aver creato i componenti simula un change di privato per sincronizzare la UI
		onPrivatoChange();

		return layout;
	}// end of method

	/**
	 * Invocato quando il valore del flag privato cambia.
	 * Sincronizza i campi dipendenti.
	 */
	private void onPrivatoChange(){
		boolean privato=fieldPrivato.getValue();
		fieldOrdineScuola.setVisible(!privato);
		fieldOrdineScuola.removeAllValidators();
		fieldMaterie.setVisible(!privato);
		if(!privato){
			fieldOrdineScuola.addValidator(new NullValidator("L'ordine della scuola è obbligatorio", false));
		}
	}

}
