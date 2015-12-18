package it.algos.evento.ui.admin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * UI iniziale di Admin.
 */
@Theme("asteriacultura")
@Title("eVento - admin")
public class AdminUI extends AlgosUI {


    @Override
    protected void init(VaadinRequest request) {

        // parse request parameters
        checkParams(request);


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

    /**
     * Legge eventuali parametri passati nella request
     * <p>
     */
    public void checkParams(VaadinRequest request) {

        LibSession.setDeveloper(false);

        // legge il parametro "developer" e regola la variabile statica
        if (request.getParameter("developer") != null) {
            boolean developer = (request.getParameter("developer") != null);
            LibSession.setDeveloper(developer);
        }// fine del blocco if

    }// end of method


}
