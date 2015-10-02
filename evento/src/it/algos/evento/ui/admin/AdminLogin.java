package it.algos.evento.ui.admin;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.LoginListener;

import java.util.ArrayList;

/**
 * Login page for the Manager
 */
public class AdminLogin extends VerticalLayout {

	public AdminLogin() {

		super();

		createUI();

		Login.getLogin().setLoginListener(new LoginListener() {

			@Override
			public void onUserLogin(Utente utente, boolean b) {
				doLogin();
			}
		});

	}

	private void createUI(){

		setWidth("100%");
		setHeight("100%");

		// horizontal: spacer left + image + spacer right
		HorizontalLayout logoLayout = new HorizontalLayout();
		logoLayout.setWidth("100%");
		addSpacer(logoLayout);
		Resource res=(LibResource.getImgResource("splash_image.png"));
		if (res!=null) {
			Image img = LibImage.getImage(res);
			logoLayout.addComponent(img);
		}
		addSpacer(logoLayout);

		// vertical: spacer top + image layout + button panel + spacer bottom
		addSpacer(this);
		addComponent(logoLayout);
		addSpacer(this, 0.2f);
		addComponent(createButtonLayout());
		addSpacer(this, 0.2f);
		addSpacer(this);
	}

	private Component createButtonLayout(){
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		addSpacer(layout);
		layout.addComponent(createLoginButton());
		addSpacer(layout);
		return layout;
	}


	private Button createLoginButton(){
		Button button=new Button("Admin Login");
		button.setStyleName("loginbutton");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Login.getLogin().showLoginForm();
			}
		});
		return button;
	}

	/**
	 * Aggiunge a un layout una label con larghezza espandibile al 100%
	 */
	private void addSpacer(AbstractOrderedLayout layout){
		addSpacer(layout, 1.0f);
	}

	/**
	 * Aggiunge a un layout una label con larghezza espandibile
	 */
	private void addSpacer(AbstractOrderedLayout layout, float ratio){
		Label label = new Label();
		layout.addComponent(label);
		layout.setExpandRatio(label, ratio);
	}


	private void doLogin(){

		// controlla se l'utente ha ruolo di admin
		Ruolo adminRole = Ruolo.read("admin");
		Utente user=Login.getLogin().getUser();
		if(user.hasRole(adminRole)) {
			// Avvia la UI del admin
			Component comp = new AdminHome();
			UI.getCurrent().setContent(comp);
		}else{
			// annulla il login e mostra una notifica
			LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, null);
			Notification.show("L'utente non è abilitato all'accesso come admin.", Notification.Type.ERROR_MESSAGE);
		}

	}



}
