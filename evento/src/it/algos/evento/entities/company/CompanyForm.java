package it.algos.evento.entities.company;

import com.vaadin.data.Item;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class CompanyForm extends AForm {

	public CompanyForm(ModulePop modulo) {
		super(modulo);
		doInit();
	}// end of constructor

	public CompanyForm(ModulePop modulo, Item item) {
		super(modulo, item);
		doInit();
	}// end of constructor

	private void doInit(){
		setWidth("500px");
	}
	
	@Override
	protected void createFields() {
		@SuppressWarnings("rawtypes")
		Field field;

		field = new TextField("Id");
		addField(Company_.id, field);

		field = new TextField("Nome");
		addField(Company_.name, field);
		
		field = new EmailField("Email");
		addField(Company_.email, field);
		
		field = new TextField("Codice azienda");
		addField(Company_.companyCode, field);
		
		field = new TextField("Contatto");
		addField(Company_.contact, field);
		
		field = new TextField("Indirizzo1");
		addField(Company_.address1, field);
		
		field = new TextField("Indirizzo2");
		addField(Company_.address2, field);

	}

	protected Component createComponent() {
		Layout layout = new AFormLayout();
		addComponents(layout);
		return incapsulaPerMargine(layout);
	}// end of method


	protected void addComponents(Layout layout){
		Field<?> field=getField(Company_.id);
		field.setReadOnly(true);
		field.setEnabled(false);
		layout.addComponent(field);
		layout.addComponent(getField(Company_.name));
		layout.addComponent(getField(Company_.email));
		layout.addComponent(getField(Company_.companyCode));
		layout.addComponent(getField(Company_.contact));
		layout.addComponent(getField(Company_.address1));
		layout.addComponent(getField(Company_.address2));
	}



	private Company getCompany(){
		BaseEntity entity = getBaseEntity();
		return (Company)entity;
	}

}
