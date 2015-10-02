package it.algos.evento.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import it.algos.evento.EventoSession;
import it.algos.evento.ui.company.CompanyUI;
import it.algos.evento.ui.manager.ManagerUI;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.servlet.AlgosServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

/**
 * Servlet 3.0 introduces a @WebServlet annotation which can be used to replace the traditional web.xml.
 * <p>
 * The straightforward approach to create a Vaadin application using servlet 3.0 annotations,
 * is to simply move whatever is in web.xml to a custom servlet class (extends VaadinServlet)
 * and annotate it using @WebServlet and add @WebInitParams as needed.
 * <p><
 * Vaadin 7.1 introduces two features which makes this a lot easier, @VaadinServletConfiguration
 * and automatic UI finding.
 * VaadinServletConfiguration is a type safe, Vaadin version of @WebInitParam
 * which provides you with the option to select UI by referring the UI class
 * directly toggle productionMode using a boolean and more
 */
@WebServlet(value = "/admin/*", asyncSupported = true, displayName = "eVento - manager")
@VaadinServletConfiguration(productionMode = false, ui = ManagerUI.class)
public class ManagerServlet extends AlgosServlet {

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        super.sessionInit(event);

        // make sure we have at least one valid manager
        ensureManager();

        // set the cookie prefix to make them unique
        // (cookie path does not work well so we use a prefix)
        Login.getLogin().setCookiePrefix("manager");

        // attempt to login from the cookies
        Login.getLogin().loginFromCookies();

    }// end of method


    /**
     * Make sure that a "manager" user and a "manager" role exist, and
     * that a corresponding UserRole exists. Otherwise, create them.
     */
    private void ensureManager(){

        // make sure that a manager role exists
        Ruolo ruolo = Ruolo.read("manager");
        if (ruolo==null){
            ruolo = new Ruolo();
            ruolo.setNome("manager");
            ruolo.save();
        }

        // make sure that a user named "manager" exists
        // if not create it now with a default password
        Utente user=Utente.read("manager");
        if (user==null){
            user = new Utente();
            user.setNickname("manager");
            user.setPassword(LibCrypto.encrypt("evento"));
            user.setEnabled(true);
            user.save();
        }

        // make sure that a corresponding UserRole exists
        ArrayList<UtenteRuolo> urs = UtenteRuolo.findUtente(user);
        boolean found=false;
        if(urs.size()>0){
            for(UtenteRuolo uruolo : urs){
                if(uruolo.getRuolo().equals(ruolo)){
                    found=true;
                    break;
                }
            }
        }
        if(!found){
            UtenteRuolo ur = new UtenteRuolo();
            ur.setUtente(user);
            ur.setRuolo(ruolo);
            ur.save();
        }

    }

}// end of class
