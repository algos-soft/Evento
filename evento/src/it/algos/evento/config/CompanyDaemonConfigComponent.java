package it.algos.evento.config;

import it.algos.evento.pref.CompanyPrefs;
import it.algos.web.field.ArrayComboField;
import it.algos.web.field.CheckBoxField;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class CompanyDaemonConfigComponent extends BaseConfigPanel {

	private static final String KEY_SERVICE_ACTIVE = "serviceactive";
	private static final String KEY_SERVICE_TIME = "servicetime";

	private ArrayComboField hourField;
	private Field<?> serviceField;
	private String[] aHours;

	public CompanyDaemonConfigComponent() {
		super();

		// crea e registra i fields
		creaFields();

		// crea la UI
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		String title = "<b>Servizio di controllo giornaliero delle posizioni scadute</b><p>";
		title += "Ogni giorno, all'orario prestabilito, effettua il controllo delle <br>"
				+ "posizioni scadute e invia automaticamente le email di sollecito.";
		Label infoLabel = new Label(title, ContentMode.HTML);
		layout.addComponent(infoLabel);
		layout.addComponent(serviceField);
		layout.addComponent(hourField);
		
		addComponent(layout);
		addComponent(createButtonPanel());

	}
	
	// crea e registra i fields
	private void creaFields(){
		
		// check box servizio attivo
		serviceField=new CheckBoxField("Attiva il servizio");

		// combo ora del controllo
		aHours = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
		hourField = new ArrayComboField(aHours, "Esegui controllo alle ore:");
		//hourField.setImmediate(true);

		// bind fields to properties
		getGroup().bind(serviceField, KEY_SERVICE_ACTIVE);
		getGroup().bind(hourField, KEY_SERVICE_TIME);

	}


	@Override
	public Component getUIComponent() {
		return this;
	}

	@Override
	public String getTitle() {
		return "Programmazione controlli automatici";
	}
	
	
	public PrefSetItem createItem() {
		return new DaemonSetItem();
	}

	/**
	 * Item containing form data
	 */
	private class DaemonSetItem extends PropertysetItem implements PrefSetItem {

		public DaemonSetItem() {
			super();
			
			addItemProperty(KEY_SERVICE_ACTIVE, new ObjectProperty<Boolean>(CompanyPrefs.doRunSolleciti.getBool()));
			
			int nOra = CompanyPrefs.oraRunSolleciti.getInt();
			String sOra="";
			if (nOra>=0) {
				sOra = aHours[nOra];				
			}
			addItemProperty(KEY_SERVICE_TIME, new ObjectProperty<String>(sOra));

		}
		
		
		/**
		 * Ritorna l'ora registrata nel'iten come int
		 * <p>
		 * @return l'ora come int, -1 se non selezionata
		 */
		private int getIntHour(){
			int nOra=-1;
			Object obj = getItemProperty(KEY_SERVICE_TIME).getValue();
			if (obj!=null) {
				String sOra = obj.toString();
				nOra = Integer.parseInt(sOra);
			}
			return nOra;
		}
		
		

		public void persist() {
			Object obj;
			boolean cont = true;
			
			// se il servizio è attivo ci deve essere l'ora
			boolean serviceactive = (boolean)getItemProperty(KEY_SERVICE_ACTIVE).getValue();
			if (serviceactive) {
				int servicetime=getIntHour();
				if (servicetime<0) {
					Notification.show("Inserire l'orario di esecuzione.");
					cont=false;
				}
			}
			
			if (cont) {

				obj = getItemProperty(KEY_SERVICE_ACTIVE).getValue();
				CompanyPrefs.doRunSolleciti.put(obj);

				obj = getItemProperty(KEY_SERVICE_TIME).getValue();
				CompanyPrefs.oraRunSolleciti.put(getIntHour());

			}

		}

	}


}
