package it.algos.evento.config;

import it.algos.evento.multiazienda.ERelatedComboField;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.field.DecimalField;
import it.algos.evento.entities.sala.Sala;

import java.math.BigDecimal;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class EventiConfigComponent extends BaseConfigPanel {

	private static final String KEY_INTERI = "impInteri";
	private static final String KEY_RIDOTTI = "impRidotti";
	private static final String KEY_DISABILI = "impDisabili";
	private static final String KEY_ACCOMP = "impAccomp";

	private static final String KEY_ID_SALA = "idSala";

	@SuppressWarnings("rawtypes")
	public EventiConfigComponent() {
		super();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);


		// create and add fields and other components
		Field importoInteri = new DecimalField("Importo interi");
		layout.addComponent(importoInteri);
		Field importoRidotti = new DecimalField("Importo ridotti");
		layout.addComponent(importoRidotti);
		Field importoDisabili = new DecimalField("Importo disabili");
		layout.addComponent(importoDisabili);
		Field importoAccomp = new DecimalField("Importo accompagnatori");
		layout.addComponent(importoAccomp);


		Field salaDefault = new ERelatedComboField(Sala.class, "Sala di default");
		layout.addComponent(salaDefault);
		layout.addComponent(createButtonPanel());
		
		addComponent(layout);

		// bind fields to properties
		getGroup().bind(importoInteri, KEY_INTERI);
		getGroup().bind(importoRidotti, KEY_RIDOTTI);
		getGroup().bind(importoDisabili, KEY_DISABILI);
		getGroup().bind(importoAccomp, KEY_ACCOMP);

		getGroup().bind(salaDefault, KEY_ID_SALA);

	}

	public PrefSetItem createItem() {
		return new EventoSetItem();
	}
	
	@Override
	public String getTitle() {
		return "Configurazione Eventi e Rappresentazioni";
	}


	/**
	 * Item containing form data
	 */
	private class EventoSetItem extends PropertysetItem implements PrefSetItem {

		public EventoSetItem() {
			super();
			addItemProperty(KEY_INTERI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseInteri.getDecimal()));
			addItemProperty(KEY_RIDOTTI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseRidotti.getDecimal()));
			addItemProperty(KEY_DISABILI, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseDisabili.getDecimal()));
			addItemProperty(KEY_ACCOMP, new ObjectProperty<BigDecimal>(CompanyPrefs.importoBaseAccomp.getDecimal()));
			Sala sala = Sala.getDefault();
			if (sala == null) {
				sala = new Sala();
			}
			addItemProperty(KEY_ID_SALA, new ObjectProperty<Sala>(sala));
		}

		public void persist() {
			CompanyPrefs.importoBaseInteri.put(getItemProperty(KEY_INTERI).getValue());
			CompanyPrefs.importoBaseRidotti.put(getItemProperty(KEY_RIDOTTI).getValue());
			CompanyPrefs.importoBaseDisabili.put(getItemProperty(KEY_DISABILI).getValue());
			CompanyPrefs.importoBaseAccomp.put(getItemProperty(KEY_ACCOMP).getValue());

			int idSala = 0;
			Object obj = getItemProperty(KEY_ID_SALA).getValue();
			if ((obj != null) && (obj instanceof Sala)) {
				idSala = ((Sala) obj).getId().intValue();
			}
			CompanyPrefs.idSalaDefault.put(idSala);
		}

	}

}
