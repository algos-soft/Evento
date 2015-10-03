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
		
		field = new TextField("Username");
		addField(Company_.username, field);
		
		field = new TextField("Contatto");
		addField(Company_.contact, field);
		
		field = new TextField("Indirizzo1");
		addField(Company_.address1, field);
		
		field = new TextField("Indirizzo2");
		addField(Company_.address2, field);

	}

	protected Component createComponent() {
		
		Button button;
		
		Layout layout = new AFormLayout();
		Field<?> field=getField(Company_.id);
		field.setReadOnly(true);
		field.setEnabled(false);

		layout.addComponent(field);
		layout.addComponent(getField(Company_.name));
		layout.addComponent(getField(Company_.email));
		layout.addComponent(getField(Company_.companyCode));
		layout.addComponent(getField(Company_.username));
		layout.addComponent(getField(Company_.contact));
		layout.addComponent(getField(Company_.address1));
		layout.addComponent(getField(Company_.address2));
		
		
		button = new Button("Genera dati demo");
		button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				ConfirmDialog dialog = new ConfirmDialog("Generazione dati demo", "Vuoi procedere?",
						new ConfirmDialog.Listener() {

							@Override
							public void onClose(ConfirmDialog dialog, boolean confirmed) {
								if (confirmed) {
									getCompany().createDemoData();
								}// end of if cycle
							}// end of inner method
						});// end of anonymous inner class
				dialog.setConfirmButtonText("Procedi");
				dialog.show(getUI());
				
			}
		});
		layout.addComponent(button);

		button = new Button("Elimina dati");
		button.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = new ConfirmDialog("Eliminazione dati", "Vuoi procedere?",
						new ConfirmDialog.Listener() {

							@Override
							public void onClose(ConfirmDialog dialog, boolean confirmed) {
								if (confirmed) {
									getCompany().deleteAllData();
								}// end of if cycle
							}// end of inner method
						});// end of anonymous inner class
				dialog.setConfirmButtonText("Procedi");
				dialog.show(getUI());

			}
		});
		layout.addComponent(button);

		
		
		return incapsulaPerMargine(layout);
	}// end of method
	
	
	private Company getCompany(){
		BaseEntity entity = getBaseEntity();
		return (Company)entity;
	}

}
