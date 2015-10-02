package it.algos.evento.ui.manager;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import it.algos.evento.ui.company.CompanyHome;
import it.algos.evento.ui.company.CompanyLogin;
import it.algos.webbase.web.lib.LibSession;

/**
 * UI iniziale della Company.
 */
@Theme("asteriacultura")
@Title("eVento")
public class ManagerUI extends UI {


    @Override
    protected void init(VaadinRequest request) {


        // intervallo di polling della UI
        // consente di vedere i risultati anche quando si aggiorna
        // la UI da un thread separato sul server
        setPollInterval(1000);

        // display the login page or the main page if already logged
        if(LibSession.isLogged()){
            setContent(new ManagerHome());
        }else{
            setContent(new ManagerLogin());
        }


    }

}
