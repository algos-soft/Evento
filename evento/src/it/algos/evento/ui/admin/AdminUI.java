package it.algos.evento.ui.admin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import it.algos.webbase.web.lib.LibSession;

/**
 * UI iniziale di Admin.
 */
@Theme("asteriacultura")
@Title("eVento - admin")
public class AdminUI extends UI {


    @Override
    protected void init(VaadinRequest request) {


        // intervallo di polling della UI
        // consente di vedere i risultati anche quando si aggiorna
        // la UI da un thread separato sul server
        setPollInterval(1000);

        // display the login page or the main page if already logged
        if(LibSession.isLogged()){
            setContent(new AdminHome());
        }else{
            setContent(new AdminLogin());
        }


    }

}
