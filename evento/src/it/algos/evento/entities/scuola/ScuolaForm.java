package it.algos.evento.entities.scuola;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.multiazienda.ERelatedComboField;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class ScuolaForm extends AForm {

//	public ScuolaForm(ModulePop modulo) {
//		super(modulo);
//		doInit();
//	}// end of constructor

	public ScuolaForm(ModulePop modulo, Item item) {
		super(modulo, item);
		doInit();
	}// end of constructor

//	public ScuolaForm(Item item) {
//		this(null, item);
//		doInit();
//	}// end of constructor
	
	private void doInit(){
		//setMargin(true);
	}


	@Override
	protected void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;
		ArrayComboField arrayCombo;

		field = new TextField("Sigla");
		field.setWidth("120px");
		addField(Scuola_.sigla, field);

		field = new TextField("Nome");
		field.setWidth("300px");
		addField(Scuola_.nome, field);

//		arrayCombo = new ArrayComboField(OrdineScuolaEnumOld.values(), "Ordine");
//		Converter converter = new OrdineScuolaConverterOld();
//		arrayCombo.setConverter(converter);
//		addField(Scuola_.ordine, arrayCombo);

		field = new ERelatedComboField(OrdineScuola.class, "Ordine");
		field.setWidth("180px");
		addField(Scuola_.ordine, field);

		field = new TextField("Tipo");
		field.setWidth("200px");
		addField(Scuola_.tipo, field);

		field = new TextField("Indirizzo");
		field.setWidth("200px");
		addField(Scuola_.indirizzo, field);
		
		field = new TextField("Cap");
		field.setWidth("60px");
		addField(Scuola_.cap, field);

		field = new ERelatedComboField(Comune.class, "Comune");
		field.setWidth("200px");
		addField(Scuola_.comune, field);

		field = new TextField("Telefono");
		addField(Scuola_.telefono, field);

		field = new TextField("Fax");
		addField(Scuola_.fax, field);

		field = new EmailField("Email");
		addField(Scuola_.email, field);

		field = new TextField("Note");
		field.setWidth("300px");
		addField(Scuola_.note, field);

	}

	protected Component createComponent() {
		FormLayout layout = new AFormLayout();
		layout.setMargin(true);

		layout.addComponent(getField(Scuola_.sigla));
		layout.addComponent(getField(Scuola_.nome));
		layout.addComponent(getField(Scuola_.ordine));
		layout.addComponent(getField(Scuola_.tipo));
		layout.addComponent(getField(Scuola_.indirizzo));
		layout.addComponent(getField(Scuola_.cap));
		layout.addComponent(getField(Scuola_.comune));
		layout.addComponent(getField(Scuola_.telefono));
		layout.addComponent(getField(Scuola_.fax));
		layout.addComponent(getField(Scuola_.email));
		layout.addComponent(getField(Scuola_.note));
		
		return layout;
	}// end of method

}
