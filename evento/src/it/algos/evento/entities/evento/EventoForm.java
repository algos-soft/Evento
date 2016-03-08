package it.algos.evento.entities.evento;

import com.vaadin.data.Item;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.progetto.ProgettoForm;
import it.algos.evento.entities.progetto.Progetto_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.field.DecimalField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class EventoForm extends ModuleForm {


	public EventoForm(ModulePop modulo, Item item) {
		super(item, modulo);
		doInit();
	}

	private void doInit(){
		//setWidth("500px");

		// se nuovo record mette importi di default
		if (isNewRecord()) {
			Field fIntero = getField(Evento_.importoIntero);
			fIntero.setValue(CompanyPrefs.importoBaseInteri.getDecimal());
			Field fRidotto = getField(Evento_.importoRidotto);
			fRidotto.setValue(CompanyPrefs.importoBaseRidotti.getDecimal());
			Field fDisabili = getField(Evento_.importoDisabili);
			fDisabili.setValue(CompanyPrefs.importoBaseDisabili.getDecimal());
			Field fAccomp = getField(Evento_.importoAccomp);
			fAccomp.setValue(CompanyPrefs.importoBaseAccomp.getDecimal());
		}

	}
	
	@Override
	public void createFields() {
		@SuppressWarnings("rawtypes")
		AbstractField field;
		TextField tfield;
		RelatedComboField combo;

		tfield = new TextField("Sigla");
		addField(Evento_.sigla, tfield);

		tfield = new TextField("Titolo");
		tfield.setWidth("300px");
		addField(Evento_.titolo, tfield);

		combo = new ERelatedComboField(Progetto.class, "Progetto");
		combo.setWidth("220px");
		combo.setNewItemHandler(ProgettoForm.class, Progetto_.descrizione);
		addField(Evento_.progetto, combo);

		combo = new ERelatedComboField(Stagione.class, "Stagione");
		combo.setWidth("220px");
		addField(Evento_.stagione, combo);

		field = new DecimalField("Importo intero");
		addField(Evento_.importoIntero, field);

		field = new DecimalField("Importo ridotto");
		addField(Evento_.importoRidotto, field);
		
		field = new DecimalField("Importo disabili");
		addField(Evento_.importoDisabili, field);
		
		field = new DecimalField("Importo accompagnatori");
		addField(Evento_.importoAccomp, field);



	}

	protected Component createComponent() {
		AFormLayout layout = new AFormLayout();
		layout.setMargin(true);
		layout.addComponent(getField(Evento_.sigla));
		layout.addComponent(getField(Evento_.titolo));
		layout.addComponent(getField(Evento_.progetto));
		layout.addComponent(getField(Evento_.stagione));
		layout.addComponent(getField(Evento_.importoIntero));
		layout.addComponent(getField(Evento_.importoRidotto));
		layout.addComponent(getField(Evento_.importoDisabili));
		layout.addComponent(getField(Evento_.importoAccomp));

		return layout;
	}// end of method

}
