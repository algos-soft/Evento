package it.algos.evento.ui.manager;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.ui.company.CompanyHome;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.LoginListener;

import java.util.ArrayList;

/**
 * Login page for the Manager
 */
public class ManagerLogin extends VerticalLayout {

	public ManagerLogin() {

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
		Button button=new Button("Manager Login");
		button.setStyleName("loginbutton");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Login.getLogin().openLoginForm();
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

		// controlla se l'utente ha ruolo di manager
		boolean found=false;
		Ruolo managerRole = Ruolo.read("manager");
		if (managerRole!=null){
			Utente user=Login.getLogin().getUser();
			ArrayList<UtenteRuolo> urs = UtenteRuolo.findUtente(user);
			if(urs.size()>0){
				for(UtenteRuolo uruolo : urs){
					if(uruolo.getRuolo().equals(managerRole)){
						found=true;
						break;
					}
				}
			}
		}

		if(found){
			// Avvia la UI del manager
			Component comp = new ManagerHome();
			UI.getCurrent().setContent(comp);
		}else{
			Notification.show("L'utente non è abilitato all'accesso come manager.", Notification.Type.ERROR_MESSAGE);
		}


	}



}
