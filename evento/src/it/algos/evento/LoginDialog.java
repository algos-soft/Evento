package it.algos.evento;

import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

import it.algos.evento.entities.company.Company;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.web.component.Spacer;
import it.algos.web.dialog.ConfirmDialog;
import it.algos.web.field.PasswordField;
import it.algos.web.field.TextField;

public class LoginDialog extends ConfirmDialog {

	private Company company;
	private Field userField;
	private Field passField;
	private Label errMessage;

	public LoginDialog(Company company, Listener closeListener) {
		super(closeListener);
		this.company = company;
		init();
	}

	private void init() {

		//EventoApp.COMPANY = company;
		EventoSession.setCompany(company);
		Image companyLogo = CompanyPrefs.splashImage.getImage();
		//EventoApp.COMPANY = null;
		EventoSession.setCompany(null);

		userField = new TextField("Nome utente");
		userField.setWidth("200px");
		passField = new PasswordField("Password");
		passField.setWidth("200px");
		errMessage = new Label();
		errMessage.addStyleName("red");

		FormLayout layout = new FormLayout();
		addComponent(layout);

		layout.addComponent(companyLogo);
		companyLogo.setWidth("150px");

		layout.addComponent(new Spacer(2, Unit.EM));
		layout.addComponent(userField);
		layout.addComponent(passField);
		layout.addComponent(errMessage);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.algos.web.dialog.ConfirmDialog#onConfirm()
	 */
	@Override
	protected void onConfirm() {
		
		if ((getUsername().equalsIgnoreCase(company.getUsername()))
				&& (getPassword().equals(company.getPassword()))) {
			super.onConfirm();
		} else {
			errMessage.setValue("Login fallito.");
		}
	}

	public String getUsername() {
		return (String) userField.getValue();
	}

	public String getPassword() {
		return (String) passField.getValue();
	}

}
