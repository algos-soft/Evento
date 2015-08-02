package it.algos.evento;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.webbase.domain.versione.VersioneModulo;
import it.algos.evento.config.ConfigScreen;
import it.algos.evento.config.GeneralDaemonConfigComponent;
import it.algos.evento.config.SMTPServerConfigComponent;
import it.algos.evento.debug.DialogoCheckPrenScadute;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.CompanyModule;
import it.algos.evento.entities.company.Company_;
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
import it.algos.evento.multiazienda.AsteriaMigration;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.statistiche.StatisticheModulo;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.screen.ErrorScreen;
import it.algos.webbase.web.ui.AlgosUI;
import it.asteria.cultura.destinatario.DestinatarioModulo;
import it.asteria.cultura.mailing.MailingModulo;
import it.asteria.cultura.test.StressTest;

import javax.servlet.http.Cookie;
import java.util.List;

@SuppressWarnings("serial")
@Theme("asteriacultura")
@Title("eVento")
// @Theme("valo")
// @Widgetset("AsteriaculturaWidgetset")
// @PreserveOnRefresh
public class EventoUI extends AlgosUI {

    private static final boolean DEBUG_GUI = false;
    private SplashScreen splashScreen;
    private MenuItem loginItem; // il menuItem di login

    public static EventoUI getCurrent() {
        return (EventoUI) UI.getCurrent();
    }

    @Override
    protected void init(VaadinRequest request) {

        super.init(request);

        // intervallo di polling della UI
        // consente di vedere i risultati anche quando si aggiorna
        // la UI da un thread separato sul server
        setPollInterval(1000);

        // declare the menu bar and splash screen

        if (!EventoSession.isManager()) {

            // se non è il manager deve essere una azienda

            // legge il parametro "company" e regola la variabile statica
            // dell'azienda.
            String companyCode = request.getParameter("company");
            if (companyCode != null) {

                EventoSession.setCompany(null);

                // cerco il codice azienda nelle company
                // (il codice company è unico!)
                Company company = Company.query.queryOne(Company_.companyCode, companyCode);

                if (company != null) {

                    EventoSession.setCompany(company);

                    // search username cookie
                    // Cookie userCookie = getCookieByName("user");
                    // String value = userCookie.getValue();

                    // if ((value==null) || (value.equals(""))) {

                    Object attr = getSession().getAttribute("user");
                    String username = Lib.getString(attr);

                    // if (username.equals("")) {
                    // doLogin(company);
                    // }else{
                    // startUI(company);
                    // }
                    startUI();

                    // VaadinSession.getCurrent();

                    // SONO ARRIVATO QUI
                    // IL CONTROLLO DEVE PASSARE AL DIALOGO
                    // E PROSEGUIRE SOLO SE IL DIALOGO PASSA LA VALIDAZIONE.
                    // BISOGNA REGISTRARE LO STATO DI UTENTE LOGGATO
                    // E RICHIEDERE IL LOGIN SOLO SE NON E' LOGGATO.
                    // SE PASSA LA VALIDAZIONE CON SUCCESSO, METTERE
                    // LE CREDENZIALI IN UN COOKIE PER RIPROPORLE LA
                    // VOLTA SUCCESSIVA (SOLO SE CONFERMATO CON CHECKBOX)

                    // checkCredenziali(company);

                    // EventoApp.COMPANY = company;
                    // splashScreen = new SplashScreen(
                    // CompanyPrefs.splashImage.getResource());
                    // placeholderComp = splashScreen;
                    // mb = createMenuBar();

                } else {

                    // azienda inesistente
                    ErrorScreen screen = new ErrorScreen("Azienda " + companyCode + " non trovata.");
                    setContent(screen);

                }

            } else {
                // né manager né azienda
                ErrorScreen screen = new ErrorScreen(
                        "Specificare l'azienda con il parametro ?company=nomeazienda");
                setContent(screen);

            }

        } else {

            // è il manager
            EventoSession.setCompany(null);
            startUI();

        }


    }// end of method

    /**
     * Legge eventuali parametri passati nella request
     * <p>
     */
    protected void checkParams(VaadinRequest request) {
        Object progObj;
        boolean prog;

        super.checkParams(request);

        EventoSession.setManager(false);

        // legge il parametro "manager" e regola la variabile statica
        if (request.getParameter("manager") != null) {
            boolean manager = (request.getParameter("manager") != null);
            EventoSession.setManager(manager);
        }// fine del blocco if

    }// end of method

    /**
     * Mostra la UI per una Company, o per il Manager se la company è null.
     */
    private void startUI() {

        // crea la UI di base, un VerticalLayout
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setMargin(true);
        vLayout.setSpacing(false);
        vLayout.setSizeFull();
        if (DEBUG_GUI) {
            vLayout.addStyleName("blueBg");
        }

        // recupera la Company dalla sessione
        Company company = EventoSession.getCompany();

        // crea la MenuBar principale
        MenuBar mainBar = createMenuBar(company);

        // crea la MenuBar di Login
        MenuBar loginBar = createLoginMenuBar();

        // aggiunge la menubar principale e la menubar login
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setHeight("32px");
        menuLayout.setWidth("100%");
        menuLayout.addComponent(mainBar);
        mainBar.setWidth("95%");
        mainBar.setHeight("100%");
        menuLayout.setExpandRatio(mainBar, 1.0f);
        menuLayout.addComponent(loginBar);
        loginBar.setHeight("100%");
        vLayout.addComponent(menuLayout);

        // crea e aggiunge uno spaziatore verticale
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setMargin(false);
        spacer.setSpacing(false);
        spacer.setHeight("5px");
        vLayout.addComponent(spacer);

        // crea e aggiunge il placeholder dove il Navigator inserirà le varie pagine
        // a seconda delle selezioni di menu
        NavigatorPlaceholder placeholder = new NavigatorPlaceholder(null);
        placeholder.setSizeFull();
        if (DEBUG_GUI) {
            placeholder.addStyleName("yellowBg");
        }
        vLayout.addComponent(placeholder);
        vLayout.setExpandRatio(placeholder, 1.0f);

        // assegna la UI
        setContent(vLayout);

        // crea un Navigator e lo configura in base ai contenuti della MenuBar
        EventoNavigator nav = new EventoNavigator(getUI(), placeholder);
        nav.configureFromMenubar(mainBar);
        nav.navigateTo("splash");

        // set browser window title
        Page.getCurrent().setTitle(EventoApp.APP_NAME);

    }

    /**
     * Crea la menubar di Login
     */
    private MenuBar createLoginMenuBar() {
        MenuBar menubar = new MenuBar();


        ThemeResource icon = new ThemeResource("img/action_user.png");
//        MenuBar.Command command = new MenuBar.Command() {
//
//            @Override
//            public void menuSelected(MenuItem selectedItem) {
//                loginCommandSelected();
//            }
//        };

        loginItem = menubar.addItem("Login", icon, null);
        updateLoginUI();

//        loginItem.addItem("Logout", new MenuBar.Command() {
//            @Override
//            public void menuSelected(MenuItem selectedItem) {
//                logout();
//            }
//        });

        return menubar;
    }

    /**
     * Crea la MenuBar in base alla Company.
     * <p>
     * Se la Company è null crea la menubar per il manager.
     */
    private MenuBar createMenuBar(Company company) {
        MenuBar mb = null;

        if (company != null) {

            // company
            splashScreen = new SplashScreen(CompanyPrefs.splashImage.getResource());
            mb = createMenuBar();

        } else {

            // manager
            splashScreen = new SplashScreen(LibResource.getImgResource("splash_image.png"));
            mb = createManagerMenuBar();

        }

        // colora la menubar se programmatore
        //boolean prog=EventoApp.MODO_PROG;
//        boolean prog = EventoUI.isDeveloper();
        boolean prog = LibSession.isDeveloper(); //@todo xAlex: controlla
        if (prog) {
            mb.addStyleName("redBg");
        }

        return mb;

    }

    private MenuBar createMenuBar() {
        MenuBar.MenuItem item;
        MenuBar menubar = new MenuBar();


        // Menu Home
        menubar.addItem("", CompanyPrefs.menubarIcon.getResource(), new MenuCommand(menubar, "splash", splashScreen));

        // Menu principali
        menubar.addItem("Eventi", null, new MenuCommand(menubar, "eventi", new EventoModulo()));
        menubar.addItem("Rappresentazioni", null, new MenuCommand(menubar, "rappresentazioni",
                new RappresentazioneModulo()));
        menubar.addItem("Prenotazioni", null, new MenuCommand(menubar, "prenotazioni", new PrenotazioneModulo()));
        menubar.addItem("Scuole", null, new MenuCommand(menubar, "scuole", new ScuolaModulo()));
        menubar.addItem("Referenti", null, new MenuCommand(menubar, "referenti", new InsegnanteModulo()));
        menubar.addItem("Statistiche", null, new MenuCommand(menubar, "statistiche", new StatisticheModulo()));


        // Menu tabelle
        item = menubar.addItem("Tabelle", null, null);
        item.addItem("Progetti", null, new MenuCommand(menubar, "progetti", new ProgettoModulo()));
        item.addItem("Stagioni", null, new MenuCommand(menubar, "stagioni", new StagioneModulo()));
        item.addItem("Sale", null, new MenuCommand(menubar, "sale", new SalaModulo()));
        item.addItem("Comuni", null, new MenuCommand(menubar, "comuni", new ComuneModulo()));
        item.addItem("Modi di pagamento", null, new MenuCommand(menubar, "pagamenti", new ModoPagamentoModulo()));
        item.addItem("Tipi di ricevuta", null, new MenuCommand(menubar, "tipiricevuta", new TipoRicevutaModulo()));
        item.addItem("Ordini scuole", null, new MenuCommand(menubar, "ordiniscuola", new OrdineScuolaModulo()));
        item.addItem("Lettere base", null, new MenuCommand(menubar, "letterebase", new LetteraModulo()));
        item.addItem("Registro eventi prenotazioni", null, new MenuCommand(menubar, "registroeventipren", new EventoPrenModulo()));
        item.addItem("Registro spedizioni", null, new MenuCommand(menubar, "registrospedizioni", new SpedizioneModulo()));
        item.addItem("Configurazione", null, new MenuCommand(menubar, "config", new ConfigScreen()));
        if (LibSession.isDeveloper()) { //@todo da fissare nella versione definitiva in cui si vende questa funzionalità
            item.addItem("Mailing", null, new MenuCommand(menubar, "mailing", new MailingModulo()));
            item.addItem("Destinatari", null, new MenuCommand(menubar, "destinatarimailing", new DestinatarioModulo()));
        }// fine del blocco if


        // Menu aiuto
        item = menubar.addItem("Aiuto", null, null);
        item.addItem("Informazioni", null, new MenuCommand(menubar, "info", new InfoModulo()));
        item.addItem("Manuale", null, new MenuCommand(menubar, "help", new HelpModulo()));
        item.addItem("Logout", null, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                logout();
            }
        });

        // Menu Programmatore
        // boolean prog=EventoApp.MODO_PROG;
//        boolean prog = EventoUI.isDeveloper();
        boolean prog = LibSession.isDeveloper(); //@todo xAlex: controlla
        if (prog) {
            addMenuProgrammatoreCompany(menubar);
        }


        return menubar;
    }// end of method

    /**
     * Crea la MenuBar per il Manager
     */
    private MenuBar createManagerMenuBar() {
        MenuBar.MenuItem item;
        MenuBar menubar = new MenuBar();

        // Menu Home
        menubar.addItem("", LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME,"manager_menubar_icon.png"), new MenuCommand(menubar, "splash",
                splashScreen));

        // Menu principali
        menubar.addItem("Aziende", null, new MenuCommand(menubar, "aziende", new CompanyModule()));

        // Menu Configurazione
        item = menubar.addItem("Configurazione", null, null);

        SMTPServerConfigComponent smtpComp = new SMTPServerConfigComponent();
        smtpComp.loadContent();
        item.addItem("SMTP Server", null, new MenuCommand(menubar, "smtpserver", smtpComp));

        GeneralDaemonConfigComponent daemonComp = new GeneralDaemonConfigComponent();
        daemonComp.loadContent();
        item.addItem("Daemon controlli automatici", null, new MenuCommand(menubar, "daemon", daemonComp));

        // Menu Programmatore
        // boolean prog=EventoApp.MODO_PROG;
//        boolean prog = EventoUI.isDeveloper();
        boolean prog = LibSession.isDeveloper(); //@todo xAlex: controlla
        if (prog) {
            addMenuProgrammatoreManager(menubar);
        }


        return menubar;
    }// end of method

    /**
     * Crea il menu Programmatore per la menubar Company
     */
    private void addMenuProgrammatoreCompany(MenuBar menubar) {
        MenuBar.MenuItem item;
        item = menubar.addItem("Programmatore", null, null);

        item.addItem("Carica comuni da file embedded", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                DemoDataGenerator.creaComuni(null);
            }
        });

        item.addItem("Esegui controllo prenotazioni scadute", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                new DialogoCheckPrenScadute(null).show(getUI());
            }
        });

        item.addItem("Stress test", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                StressTest st = new StressTest();
                st.run();
            }
        });
        item.addItem("Versioni", null, new MenuCommand(menubar, "versioni", new VersioneModulo()));

    }// end of method

    /**
     * Crea il menu Programmatore per la menubar Manager
     */
    private void addMenuProgrammatoreManager(MenuBar menubar) {
        MenuBar.MenuItem item;
        item = menubar.addItem("Programmatore", null, null);

//        item.addItem("Revisione interi -> ridotti su Asteria", null, new MenuBar.Command() {
//
//            @Override
//            public void menuSelected(MenuItem selectedItem) {
//
//                ConfirmDialog dialog = new ConfirmDialog(new ConfirmDialog.Listener() {
//
//                    @Override
//                    public void onClose(ConfirmDialog dialog, boolean confirmed) {
//                        if (confirmed) {
//                            Company compAsteria = AsteriaMigration.getCompanyAsteria();
//                            if (compAsteria != null) {
//                                //EventoApp.COMPANY = compAsteria;
//                                EventoUI.this.company = compAsteria;
//                                new RevInteriRidotti().run();
//                                //EventoApp.COMPANY = null;
//                                EventoUI.this.company = null;
//                            } else {
//                                Notification.show("L'azienda con codice 'asteria' non esiste.",
//                                        Notification.Type.ERROR_MESSAGE);
//                            }
//                        }
//                    }
//                });
//                dialog.setMessage("Per tutti gli Eventi:<br>"
//                        + "- sposta l'importo da Intero a Ridotto, azzera l'Intero<br><br>"
//                        + "Per tutte le Prenotazioni:<br>"
//                        + "- aggiunge i Ridotti (ex Disabili) agli Omaggi (ex Accomp.).<br>"
//                        + "- sposta gli Interi nei Ridotti<br>" + "- azzera gli Interi<br>"
//                        + "I totali non cambiano.");
//                dialog.show(UI.getCurrent());
//
//            }
//        });

        item.addItem("Migrazione Asteria a multi-azienda", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                AsteriaMigration.start();
            }
        });

        item.addItem("Aggiunta UUID prenotazioni dove nulli", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                AsteriaMigration.aggiungiUUIDPrenotazione();
            }
        });

    }// end of method


//	/**
//	 * Command for main modules
//	 */
//	class ModuleCommand extends AbsCommand {
//
//		public ModuleCommand(Component component) {
//			super(component, "module");
//		}
//
//	}
//
//	/**
//	 * Command for tables
//	 */
//	class TabCommand extends AbsCommand {
//
//		public TabCommand(Component component) {
//			super(component, "table");
//		}
//
//	}
//
//	/**
//	 * Custom menu command to show a component in the placeholder
//	 */
//	abstract class AbsCommand implements MenuBar.Command {
//		private Component component;
//		private String styleName;
//
//		public AbsCommand(Component component, String styleName) {
//			super();
//			this.component = component;
//			this.styleName = styleName;
//		}
//
//		public void menuSelected(MenuItem selectedItem) {
//			// put the component in the placeholder
//			// placeholder.setContent(this.component);
//
//			// restore previous item and style current item
//			if (previous != null) {
//				previous.setStyleName(null);
//			}
//			selectedItem.setStyleName(this.styleName);
//			previous = selectedItem;
//
//		}
//
//	}

    /**
     * Richiede e valida le credenziali
     */
    private void doLogin(Company company) {

        // listener per la conferma
        ConfirmDialog.Listener listener = new ConfirmDialog.Listener() {

            @Override
            public void onClose(ConfirmDialog dialog, boolean confirmed) {
                if (confirmed) {
                    String username = ((LoginDialog) dialog).getUsername();

                    // Store the current user in the service session
                    getSession().setAttribute("user", username);

                    // Cookie userCookie = new Cookie("user", username);
                    // userCookie
                    // .setComment("Cookie for storing the name of the user");
                    // userCookie.setMaxAge(2592000); // 30gg
                    // userCookie.setPath(VaadinService.getCurrentRequest()
                    // .getContextPath());
                    // VaadinService.getCurrentResponse().addCookie(userCookie);
                    startUI();
                }
            }

        };

        // crea e visualizza il dialogo di inserimento delle credenziali
        LoginDialog dialogo = new LoginDialog(company, listener);
        dialogo.show(UI.getCurrent());

    }// end of method

    private void logoutOld() {
        Cookie userCookie = getCookieByName("user");
        userCookie.setValue("");
        VaadinService.getCurrentResponse().addCookie(userCookie);

        // Close the VaadinServiceSession
        // getUI().getSession().close();

        // Invalidate underlying session instead if login info is stored there
        VaadinService.getCurrentRequest().getWrappedSession().invalidate();

        // Redirect to avoid keeping the removed UI open in the browser
        // URI uri = getUI().getPage().getLocation();
        getUI().getPage().setLocation("/evento");

    }// end of method

    /**
     * Il bottone login è stato premuto
     */
    private void loginCommandSelected() {
        login();
        updateLoginUI();
    }// end of method

    private void logoutCommandSelected() {
        logout();
        updateLoginUI();
    }// end of method

    /**
     * Operazioni effettive di login (senza UI)
     */
    private void login() {
        getSession().setAttribute(EventoApp.KEY_USERID, 1);
    }// end of method

    /**
     * Operazioni effettive di logout (senza UI)
     */
    private void logout() {


        // "Logout" the user
        // inutile perché gli attributi vengono persi quando
        // poi chiudo la sessione
        // getSession().setAttribute(EventoApp.KEY_USERID, null);

        // Show the splash page
        getUI().getNavigator().navigateTo("splash");


        // Redirect this page immediately
        // ThemeResource resource = new ThemeResource("layouts/login.html");

        //getPage().setLocation(resource.toString());


        // Close the VaadinServiceSession
        getSession().close();

        // Reload the current page and get a new session
        getPage().reload();

        //getMainWindow().executeJavaScript("window.location.reload();");
        //getUI().getWindow().executeJavaScript("wnd.location.reload(true);");
        //Page.getCurrent().getJavaScript().execute("wnd.location.reload();");

    }// end of method

    /**
     * Aggiorna la UI di login in base ai contenuti della session
     */
    private void updateLoginUI() {
        Object attr = VaadinSession.getCurrent().getAttribute(EventoApp.KEY_USERID);
        if (attr == null) {
            loginItem.setText("Login");
            loginItem.setCommand(new MenuBar.Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    loginCommandSelected();
                }
            });

        } else {
            String username = "Alessandro Valbonesi";
            loginItem.setCommand(null);
            loginItem.setText(username);
            loginItem.removeChildren();
            loginItem.addItem("Logout", new MenuBar.Command() {
                @Override
                public void menuSelected(MenuItem selectedItem) {
                    logoutCommandSelected();
                }
            });
        }
    }// end of method

    private Cookie getCookieByName(String name) {
        Cookie cookieOut = null;
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookieOut = cookie;
                break;
            }
        }

        return cookieOut;
    }// end of method

    /**
     * Custom menu command to show a component in the placeholder
     */
    public class MenuCommand implements MenuBar.Command {
        private MenuBar mb;
        private String address;
        private Component comp;

        public MenuCommand(MenuBar mb, String address, Component comp) {
            super();
            this.mb = mb;
            this.address = address;
            this.comp = comp;
        }

//		public MenuCommand(String address, Component comp) {
//			this(null, address, comp);
//		}


        public void menuSelected(MenuItem selectedItem) {
            // Navigate to a specific state
            getUI().getNavigator().navigateTo(address);

            // de-selects all the items in the menubar
            if (mb != null) {
                List<MenuItem> items = mb.getItems();
                for (MenuItem item : items) {
                    deselectItem(item);
                }
            }

			/* highlights the selected item
             * the style name will be prepended automatically with "v-menubar-menuitem-" */
            selectedItem.setStyleName("highlight");

            // it this item is inside another item, highlight also the parents in the chain
            MenuItem item = selectedItem;
            while (item.getParent() != null) {
                item = item.getParent();
                item.setStyleName("highlight");
            }

        }

        /**
         * Recursively de-selects one item and all its children
         */
        private void deselectItem(MenuItem item) {
            item.setStyleName(null);
            List<MenuItem> items = item.getChildren();
            if (items != null) {
                for (MenuItem child : items) {
                    deselectItem(child);
                }
            }
        }

        /**
         * @return the address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @return the comp
         */
        public Component getComponent() {
            return comp;
        }

    }

}// end of class
