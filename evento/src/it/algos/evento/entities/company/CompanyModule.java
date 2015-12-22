package it.algos.evento.entities.company;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.evento.entities.prenotazione.PrenotazioneTablePortal;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class CompanyModule extends ModulePop implements View {

	public CompanyModule() {
		super(Company.class);
	}// end of constructor


	// come default spazzola tutti i campi della Entity
	// non garantisce l'ordine con cui vengono presentati i campi
	// può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
	// può mostrare anche il campo ID, oppure no
	// se si vuole differenziare tra Table, Form e Search, sovrascrivere
	// creaFieldsList, creaFieldsForm e creaFieldsSearch
	protected Attribute<?, ?>[] creaFieldsAll() {
		return new Attribute[] { Company_.name, Company_.contractType,  Company_.contractEnd};
	}// end of method

	
	@Override
	protected Attribute<?, ?>[] creaFieldsList() {
		return new Attribute[] { Company_.name, Company_.companyCode, Company_.contractType,  Company_.contractEnd};
	}

	@Override
	public AForm createForm(Item item) {
		return (new CompanyForm(this, item));
	}// end of method

	@Override
	public void enter(ViewChangeEvent event) {
	}

	public TablePortal createTablePortal() {
		return new CompanyTablePortal(this);
	}// end of method



	public void delete(Object id){
		
		// cancella prima tutti i dati
		Company company = (Company)AQuery.queryById(Company.class, id);
		company.deleteAllData();
		
		// poi cancella la company
		super.delete(id);
		
	}

	/**
	 * Create button pressed in table
	 * <p>
	 * Create a new item and edit it in a form
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void create() {

		String msg = "<b>Normalmente le aziende si creano tramite la funzione Altro->Attiva nuova azienda.</b><p>";
		msg+="Se crei una azienda manualmente dovrai anche creare l'utente e i dati.<br>";
		msg+="Vuoi continuare?<br>";

		ConfirmDialog dialog = new ConfirmDialog("Creazione azienda", msg, new ConfirmDialog.Listener() {
			@Override
			public void onClose(ConfirmDialog dialog, boolean confirmed) {
				if(confirmed){
					CompanyModule.super.create();
				}
			}
		});

		dialog.show();

	}// end of method

}// end of class
