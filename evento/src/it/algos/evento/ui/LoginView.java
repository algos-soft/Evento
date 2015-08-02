package it.algos.evento.ui;

import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;
import it.algos.evento.ui.manager.CompanyView;
import it.algos.evento.ui.manager.ManagerView;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextField;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

public class LoginView extends CustomComponent implements View {
    public static final String NAME = "login";

	private Field<String> userField;
	private Field<String> passField;
    private Button loginButton;
	private Label errMessage;

    
	public LoginView() {
		super();
		
		//EventoApp.COMPANY = company;
		//Image companyLogo = CompanyPrefs.splashImage.getImage();
		//EventoApp.COMPANY = null;
		userField = new TextField("Nome utente");
		userField.setWidth("200px");
		passField = new PasswordField("Password");
		passField.setWidth("200px");
		errMessage = new Label();
		errMessage.addStyleName("red");
		
        // Create login button
        loginButton = new Button("Login");
        loginButton.addClickListener(new Button.ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				loginButtonClicked();
			}
        	
        });


		FormLayout layout = new FormLayout();
//		layout.addComponent(companyLogo);
//		companyLogo.setWidth("150px");

		layout.addComponent(new Spacer(2, Unit.EM));
		layout.addComponent(userField);
		layout.addComponent(passField);
		layout.addComponent(errMessage);
		layout.addComponent(loginButton);
        setCompositionRoot(layout);
	}


	@Override
	public void enter(ViewChangeEvent event) {
        // focus the username field when user arrives to the login view
		userField.focus();
	}
	
	public String getUsername() {
		return (String) userField.getValue();
	}

	public String getPassword() {
		return (String) passField.getValue();
	}


	public void loginButtonClicked() {
		
//        //
//        // Validate the fields using the navigator. By using validators for the
//        // fields we reduce the amount of queries we have to use to the database
//        // for wrongly entered passwords
//        //
//        if (!userField.isValid() || !passField.isValid()) {
//            return;
//        }

        String username = userField.getValue();
        String password = passField.getValue();

        // recupera le credenziali per il confronto
        String reqUser="";
        String reqPassword="";
		Company company= EventoSession.getCompany();
        if (company!=null) {
        	reqUser=company.getUsername();
        	reqPassword=company.getPassword();
        }else{
        	reqUser="manager";
        	reqPassword="manager";
		}

        //
        // Validate username and password with database here. For examples sake
        // I use a dummy username and password.
        //
        boolean isValid = username.equalsIgnoreCase(reqUser)
                && password.equals(reqPassword);

        if (isValid) {

            // Store the current user in the service session
            getSession().setAttribute("user", username);

            // Navigate to main view
            String pageName="";
			Company comp=EventoSession.getCompany();
			if (comp!=null) {
            	pageName=CompanyView.NAME;
			} else {
            	pageName=ManagerView.NAME;
			}
            
            getUI().getNavigator().navigateTo(pageName);

        } else {

            // Wrong password clear the password field and refocuses it
        	passField.setValue(null);
        	passField.focus();

        }
		
	}


}
