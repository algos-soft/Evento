package it.algos.evento.entities.company;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.AQuery;

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

	
	public void delete(Object id){
		
		// cancella prima tutti i dati
		Company company = (Company)AQuery.queryById(Company.class, id);
		company.deleteAllData();
		
		// poi cancella la company
		super.delete(id);
		
	}

	@Override
	protected void postSave(Item item, boolean newRecord) {

		// dopo aver creato l'azienda creo anche l'utente e i dati demo
		if(newRecord){
			String code = (String)item.getItemProperty(Company_.companyCode.getName()).getValue();
			Utente user = new Utente();
			user.setNickname(code);
			user.setPassword(code);
			user.setEnabled(true);
			user.save();

			// crea i dati demo
			if(item instanceof BeanItem){
				BeanItem bi = (BeanItem)item;
				Company company = (Company)bi.getBean();
				DemoDataGenerator.createDemoData(company);
			}

		}

	}
}// end of class
