package it.algos.evento.ui.company;

import com.vaadin.server.*;
import com.vaadin.ui.*;
import it.algos.evento.*;
import it.algos.evento.config.ConfigScreen;
import it.algos.evento.debug.DialogoCheckPrenScadute;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.evento.entities.comune.ComuneModulo;
import it.algos.evento.entities.evento.EventoModulo;
import it.algos.evento.entities.insegnante.InsegnanteModulo;
import it.algos.evento.entities.lettera.LetteraModulo;
import it.algos.evento.entities.modopagamento.ModoPagamentoModulo;
import it.algos.evento.entities.ordinescuola.OrdineScuolaModulo;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo;
import it.algos.evento.entities.prenotazione.eventi.EventoPrenModulo;
import it.algos.evento.entities.progetto.ProgettoModulo;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;
import it.algos.evento.entities.sala.SalaModulo;
import it.algos.evento.entities.scuola.ScuolaModulo;
import it.algos.evento.entities.spedizione.SpedizioneModulo;
import it.algos.evento.entities.stagione.StagioneModulo;
import it.algos.evento.entities.tiporicevuta.TipoRicevutaModulo;
import it.algos.evento.help.HelpModulo;
import it.algos.evento.info.InfoModulo;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.pref.EventoPrefs;
import it.algos.evento.statistiche.StatisticheModulo;
import it.algos.evento.ui.*;
import it.algos.webbase.domain.vers.VersMod;
import it.algos.webbase.web.Command.MenuCommand;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import it.algos.evento.entities.destinatario.DestinatarioModulo;
import it.algos.evento.entities.mailing.MailingModulo;
import it.algos.evento.test.StressTest;
import it.algos.webbase.web.navigator.AlgosNavigator;

import java.util.Collection;

/**
 * Home page della Company.
 */
public class CompanyHome extends VerticalLayout {

    private CompanySplash splashScreen;
    private MenuBar.MenuItem loginItem; // il menuItem di login
    private MenuBar.MenuItem itemPrenotazioni; // il menuItem di Prenotazioni



    public CompanyHome() {

        // regolazioni di questo layout
        setMargin(true);
        setSpacing(false);
        setSizeFull();

        // crea la MenuBar principale
        MenuBar mainBar = createMainMenuBar();


        // aggiunge la menubar principale
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setHeight("32px");
        menuLayout.setWidth("100%");
        menuLayout.addComponent(mainBar);
        menuLayout.setExpandRatio(mainBar, 1.0f);
        mainBar.setHeight("100%");

        // crea e aggiunge la login bar (se il login utente è abilitato)
        if(EventoPrefs.autoLoginCompany.getInt()==0) {
            MenuBar loginBar = createLoginMenuBar();
            menuLayout.addComponent(loginBar);
            loginBar.setHeight("100%");
        }

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
        addComponent(placeholder);
        setExpandRatio(placeholder, 1.0f);

        // crea un Navigator e lo configura in base ai contenuti della MenuBar
        AlgosNavigator navigator = new AlgosNavigator(UI.getCurrent(), placeholder);
        navigator.configureFromMenubar(mainBar);
        navigator.navigateTo("splash");

        // set browser window title
        Page.getCurrent().setTitle(EventoApp.APP_NAME);

    }



    /**
     * Crea la MenuBar principale.
     */
    private MenuBar createMainMenuBar() {

        MenuBar.MenuItem item;
        MenuBar menubar = new MenuBar();

        // Home menu
        // no caching, crea nuovo ogni volta che è visualizzata
        menubar.addItem("Home", FontAwesome.HOME, new MenuCommand(menubar, "splash", CompanySplash.class));

        // Menu principali
        menubar.addItem("Eventi", null, new MenuCommand(menubar, "eventi",new EventoModulo()));
        menubar.addItem("Rappresentazioni", null, new MenuCommand(menubar, "rappresentazioni",RappresentazioneModulo.class));
        itemPrenotazioni=menubar.addItem("Prenotazioni", null, new MenuCommand(menubar, "prenotazioni", PrenotazioneModulo.class));
        menubar.addItem("Scuole", null, new MenuCommand(menubar, "scuole", ScuolaModulo.class));
        menubar.addItem("Referenti", null, new MenuCommand(menubar, "referenti", InsegnanteModulo.class));
        menubar.addItem("Statistiche", null, new MenuCommand(menubar, "statistiche", StatisticheModulo.class));

        // Menu tabelle
        item = menubar.addItem("Tabelle", null, null);
        item.addItem("Progetti", null, new MenuCommand(menubar, "progetti", ProgettoModulo.class));
        item.addItem("Stagioni", null, new MenuCommand(menubar, "stagioni", StagioneModulo.class));
        item.addItem("Sale", null, new MenuCommand(menubar, "sale", SalaModulo.class));
        item.addItem("Comuni", null, new MenuCommand(menubar, "comuni", ComuneModulo.class));
        item.addItem("Modi di pagamento", null, new MenuCommand(menubar, "pagamenti", ModoPagamentoModulo.class));
        item.addItem("Tipi di ricevuta", null, new MenuCommand(menubar, "tipiricevuta", TipoRicevutaModulo.class));
        item.addItem("Ordini scuole", null, new MenuCommand(menubar, "ordiniscuola", OrdineScuolaModulo.class));
        item.addItem("Lettere base", null, new MenuCommand(menubar, "letterebase", LetteraModulo.class));
        item.addItem("Registro eventi prenotazioni", null, new MenuCommand(menubar, "registroeventipren", EventoPrenModulo.class));
        item.addItem("Registro spedizioni", null, new MenuCommand(menubar, "registrospedizioni", SpedizioneModulo.class));
        item.addItem("Configurazione", null, new MenuCommand(menubar, "config", ConfigScreen.class));

        if (LibSession.isDeveloper()) { //@todo da fissare nella versione definitiva in cui si vende questa funzionalità
            item.addItem("Mailing", null, new MenuCommand(menubar, "mailing", MailingModulo.class));
            item.addItem("Destinatari", null, new MenuCommand(menubar, "destinatarimailing", DestinatarioModulo.class));
        }// fine del blocco if


        // Menu aiuto
        item = menubar.addItem("Aiuto", null, null);
        item.addItem("Informazioni", null, new MenuCommand(menubar, "info", InfoModulo.class));
        item.addItem("Manuale", null, new MenuCommand(menubar, "help", HelpModulo.class));

        // Modo Programmatore
        if (LibSession.isDeveloper()) {

            DevPassDialog dialog = new DevPassDialog(new ConfirmDialog.Listener() {
                @Override
                public void onClose(ConfirmDialog dialog, boolean confirmed) {
                    if(confirmed) {
                        addMenuProgrammatore(menubar);
                        menubar.addStyleName("redBg");
                    }else{
                        LibSession.setDeveloper(false);
                    }
                }
            });

            dialog.show();

        }

        return menubar;

    }


    /**
     * Crea il menu Programmatore per la menubar Company
     */
    private void addMenuProgrammatore(MenuBar menubar) {
        MenuBar.MenuItem item;
        item = menubar.addItem("Programmatore", null, null);

        item.addItem("Carica comuni da file embedded", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                DemoDataGenerator.creaComuni(null);
            }
        });

        item.addItem("Esegui controllo prenotazioni scadute", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                new DialogoCheckPrenScadute(null).show(getUI());
            }
        });

        item.addItem("Stress test", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                StressTest st = new StressTest();
                st.run();
            }
        });
        item.addItem("Versioni", null, new MenuCommand(menubar, "versioni", new VersMod()));

    }// end of method


    /**
     * Crea la menubar di Login
     */
    private MenuBar createLoginMenuBar() {
        MenuBar menubar = new MenuBar();
        Resource icon = FontAwesome.USER;
        String username = Login.getLogin().getUser().getNickname();
        loginItem = menubar.addItem(username, icon, null);
        loginItem.addItem("Logout", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                // annulla l'oggetto Login nella sessione
                LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, null);

                // annulla l'oggetto Company nella sessione
                EventoSessionLib.setCompany(null);

                // Rimetti il login screen in tutte le UI della sessione
                // (serve se la sessione è aperta in diversi tab o finestre del browser)
                Collection<UI> uis = VaadinSession.getCurrent().getUIs();
                for(UI ui:uis){
                    ui.setContent(new CompanyLogin());
                }


            }
        });
        return menubar;
    }


    public MenuBar.MenuItem getItemPrenotazioni() {
        return itemPrenotazioni;
    }
}
