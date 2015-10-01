package it.algos.evento.entities.company;

import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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

	
}// end of class
