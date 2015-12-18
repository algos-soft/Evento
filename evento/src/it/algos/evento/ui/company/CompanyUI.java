package it.algos.evento.ui.company;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.pref.EventoPrefs;
import it.algos.evento.ui.DevPassDialog;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.ui.AlgosUI;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UI iniziale di Company.
 */
@Theme("asteriacultura")
//@Theme("valo")
@Title("eVento")
public class CompanyUI extends AlgosUI {

    private final static Logger logger = Logger.getLogger(CompanyUI.class.getName());

    @Override
    protected void init(VaadinRequest request) {

        // parse request parameters
        checkParams(request);

        // intervallo di polling della UI
        // consente di vedere i risultati anche quando si aggiorna
        // la UI da un thread separato sul server
        setPollInterval(1000);

        int autoCompanyId= EventoPrefs.autoLoginCompany.getInt();
        if(autoCompanyId==0) {

            // display the login page or the main page if already logged
            if(LibSession.isLogged()){
                setContent(new CompanyHome());
            }else{
                setContent(new CompanyLogin());
            }

        }else{
            // user login disabilitato, effettua automaticamente il login alla azienda di default
            // e mostra direttamente la home
            Company company = Company.query.queryOne(Company_.id, autoCompanyId);
            if(company!=null){
                EventoSessionLib.setCompany(company);
                setContent(new CompanyHome());
            }
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

        // login from url parameters
        // legge il parametro "user" e "password" ed effettua il login
        if (request.getParameter("user") != null) {
            if (request.getParameter("password") != null) {
                String login = request.getParameter("user");
                String pass = request.getParameter("password");
                Utente user = Utente.validate(login, pass);
                if(user!=null) {

                    // registra la company nella sessione in base all'utente
                    if(EventoSessionLib.registerCompanyByUser(user)) {
                        Login.getLogin().setUser(user);
                        EventoSessionLib.setLogin(Login.getLogin());
                    }else{
                        EventoSessionLib.setLogin(null);
                        String err = "L'utente " + user + " (loggato tramite parametri url) è registrato, ma non c'è l'azienda corrispondente. Login fallito.";
                        logger.log(Level.SEVERE, err);
                    }

                }else{
                    EventoSessionLib.setLogin(null);
                }
            }
        }



    }

}