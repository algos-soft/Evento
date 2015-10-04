package it.algos.evento.ui.admin;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import it.algos.evento.*;
import it.algos.evento.config.AccessControlConfigComponent;
import it.algos.evento.config.GeneralDaemonConfigComponent;
import it.algos.evento.config.SMTPServerConfigComponent;
import it.algos.evento.entities.company.CompanyModule;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.ui.EventoNavigator;
import it.algos.evento.ui.MenuCommand;
import it.algos.evento.ui.NavigatorPlaceholder;
import it.algos.evento.ui.SplashScreen;
import it.algos.webbase.domain.ruolo.RuoloModulo;
import it.algos.webbase.domain.utente.UtenteModulo;
import it.algos.webbase.domain.utenteruolo.UtenteRuoloModulo;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;

import java.util.Collection;

/**
 * Home page della Company.
 */
public class AdminHome extends VerticalLayout {

    private SplashScreen splashScreen;
    private MenuBar.MenuItem loginItem; // il menuItem di login

    public AdminHome() {

        // regolazioni di questo layout
        setMargin(true);
        setSpacing(false);
        setSizeFull();

        // crea la MenuBar principale
        MenuBar mainBar = createMainMenuBar();

        // crea la MenuBar di Login
        MenuBar loginBar = createLoginMenuBar();

        // aggiunge la menubar principale e la menubar login
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setHeight("32px");
        menuLayout.setWidth("100%");
        menuLayout.addComponent(mainBar);
        mainBar.setHeight("100%");
        menuLayout.setExpandRatio(mainBar, 1.0f);
        menuLayout.addComponent(loginBar);
        loginBar.setHeight("100%");
        addComponent(menuLayout);

        // crea e aggiunge uno spaziatore verticale
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setMargin(false);
        spacer.setSpacing(false);
        spacer.setHeight("5px");
        addComponent(spacer);

        // crea e aggiunge il placeholder dove il Navigator inserirà le varie pagine
        // a seconda delle selezioni di menu
        NavigatorPlaceholder placeholder = new NavigatorPlaceholder(null);
        placeholder.setSizeFull();
//        if (DEBUG_GUI) {
//            placeholder.addStyleName("yellowBg");
//        }
        addComponent(placeholder);
        setExpandRatio(placeholder, 1.0f);

        // crea un Navigator e lo configura in base ai contenuti della MenuBar
        EventoNavigator nav = new EventoNavigator(UI.getCurrent(), placeholder);
        nav.configureFromMenubar(mainBar);
        nav.navigateTo("splash");

        // set browser window title
        Page.getCurrent().setTitle(EventoApp.APP_NAME+" - admin");

    }



    /**
     * Crea la MenuBar principale.
     */
    private MenuBar createMainMenuBar() {

        splashScreen = new SplashScreen(LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME,"splash_image.png"));

        MenuBar.MenuItem item;
        MenuBar menubar = new MenuBar();

        // Menu Home
        menubar.addItem("", LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME, "manager_menubar_icon.png"), new MenuCommand(menubar, "splash", splashScreen));

        // Menu principali
        menubar.addItem("Aziende", null, new MenuCommand(menubar, "aziende", new CompanyModule()));

        // Menu Utenti e ruoli
        item = menubar.addItem("Utenti e ruoli", null, null);
        item.addItem("Utenti", null, new MenuCommand(menubar, "utenti", new UtenteModulo()));
        item.addItem("Ruoli", null, new MenuCommand(menubar, "ruoli", new RuoloModulo()));
        item.addItem("Utenti-Ruoli", null, new MenuCommand(menubar, "utenteruolo", new UtenteRuoloModulo()));

        // Menu Configurazione
        item = menubar.addItem("Configurazione", null, null);

        // submenu controllo accessi
        AccessControlConfigComponent accessComp = new AccessControlConfigComponent();
        accessComp.loadContent();
        item.addItem("Controllo accessi", null, new MenuCommand(menubar, "accesscontrol", accessComp));

        // submenu smtp server
        SMTPServerConfigComponent smtpComp = new SMTPServerConfigComponent();
        smtpComp.loadContent();
        item.addItem("SMTP Server", null, new MenuCommand(menubar, "smtpserver", smtpComp));

        // submenu daemon controlli automatici
        GeneralDaemonConfigComponent daemonComp = new GeneralDaemonConfigComponent();
        daemonComp.loadContent();
        item.addItem("Daemon controlli automatici", null, new MenuCommand(menubar, "daemon", daemonComp));

//        // Menu Programmatore
//        // boolean prog=EventoApp.MODO_PROG;
////        boolean prog = EventoUI.isDeveloper();
//        boolean prog = LibSession.isDeveloper(); //@todo xAlex: controlla
//        if (prog) {
//            addMenuProgrammatoreManager(menubar);
//        }


        return menubar;

    }


    /**
     * Crea la menubar di Login
     */
    private MenuBar createLoginMenuBar() {
        MenuBar menubar = new MenuBar();
        ThemeResource icon = new ThemeResource("img/action_user.png");
        String username = EventoSessionLib.getAdminLogin().getUser().getNickname();
        loginItem = menubar.addItem(username, icon, null);
        loginItem.addItem("Logout", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                // annulla l'oggetto Login nella sessione
                LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, null);

                // Rimetti il login screen in tutte le UI della sessione
                // (serve se la sessione è aperta in diversi tab o finestre del browser)
                Collection<UI> uis = VaadinSession.getCurrent().getUIs();
                for(UI ui:uis){
                    ui.setContent(new AdminLogin());
                }

            }
        });
        return menubar;
    }





}
