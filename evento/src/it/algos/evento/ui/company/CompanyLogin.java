package it.algos.evento.ui.company;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.ui.company.CompanyHome;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.LoginListener;

public class CompanyLogin extends VerticalLayout {

	public CompanyLogin() {

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
		Button button=new Button("Login");
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

		// Trova la Company relativa all'utente loggato
		String user=Login.getLogin().getUser().getNickname();
		Company company = Company.query.queryOne(Company_.companyCode, user);

		if(company!=null){

			// registro la Company nella sessione
			EventoSession.setCompany(company);

			// Avvia la UI della Company
			Component comp = new CompanyHome();
			UI.getCurrent().setContent(comp);

		}else{
			Notification.show("L'utente "+user+" è registrato ma non c'è l'azienda corrispondente.\nContattateci per creare la vostra azienda.", Notification.Type.ERROR_MESSAGE);
		}

	}

}
