package it.algos.evento.ui.manager;

import com.vaadin.ui.MenuBar;
import it.algos.evento.EventoApp;
import it.algos.web.lib.LibResource;
import it.algos.web.lib.LibSession;

public class ManagerMenuBar extends MenuBar {

    public ManagerMenuBar() {
        super();
        addStyleName("mybarmenu");
        //boolean prog=EventoApp.MODO_PROG;
//		boolean prog= EventoUI.isDeveloper();
        boolean prog = LibSession.isDeveloper(); //@todo xAlex: controlla
        if (prog) {
            addStyleName("redBg");
        }

        addItem("",
                LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME,"manager_menubar_icon.png"),
                new MenuBar.Command() {

                    @Override
                    public void menuSelected(MenuItem selectedItem) {
                        getUI().getNavigator().navigateTo(ManagerSplashView.NAME);
                    }

                });

        addItem("Aziende", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                getUI().getNavigator().navigateTo(CompanyView.NAME);
            }

        });

        MenuBar.MenuItem item = addItem("Configurazione", null, null);

//		SMTPServerConfigComponent smtpComp = new SMTPServerConfigComponent();
//		smtpComp.loadContent();
        item.addItem("SMTP Server", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                getUI().getNavigator().navigateTo(SMTPServerView.NAME);
            }

        });

        item.addItem("Daemon controlli automatici", null, new MenuBar.Command() {

            @Override
            public void menuSelected(MenuItem selectedItem) {
                getUI().getNavigator().navigateTo(DaemonConfigView.NAME);
            }

        });


//		GeneralDaemonConfigComponent daemonComp = new GeneralDaemonConfigComponent();
//		daemonComp.loadContent();
//		item.addItem("Daemon controlli automatici", null, new TabCommand(
//				daemonComp));

//		// Menu Programmatore
//		if (EventoApp.MODO_PROG) {
//			addMenuProgrammatoreManager(menubar);
//		}


    }

}
