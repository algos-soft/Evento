package it.algos.evento;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.ui.admin.AdminHome;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.Login;

/**
 * Created by alex on 28-05-2015.
 *
 */
public class EventoSession {


    public static Company getCompany() {
        Company company = null;
        Object attr = LibSession.getAttribute("company");
        if ((attr != null) & (attr instanceof Company)) {
            company = (Company) attr;
        }// fine del blocco if

        return company;
    }

    public static void setCompany(Company company) {
        LibSession.setAttribute("company", company);
    }

    public static boolean isManager() {
        boolean manager = false;
        Object attr = LibSession.getAttribute("admin");
        if ((attr != null) & (attr instanceof Boolean)) {
            manager = (Boolean) attr;
        }// fine del blocco if

        return manager;
    }

    public static void setLogin(Login login) {
        LibSession.setAttribute(Login.LOGIN_KEY_IN_SESSION, login);
    }

    /**
     * Individua la Company relativa a un dato utente
     * e la registra nella sessione corrente.
     * Se non è stata inviduata una Company ritorna false.
     */
    public static boolean registerCompanyByUser(Utente user){

        boolean success=false;

        // cerca una company con lo stesso nome
        String username=user.getNickname();
        Company company = Company.query.queryOne(Company_.companyCode, username);
        if(company!=null){
            EventoSession.setCompany(company);
            success=true;
        }
        return success;

    }


    /**
     * Returns a customized Login object for the Admin session.
     * The Login has a custom cookie prefix.
     * For the Admin login, always use this method instead of Login.getLogin()
     * @return the customized Login object
     */
    public static Login getAdminLogin(){
        Login login = Login.getLogin();
        login.setCookiePrefix("admin");
        return login;
    }



}// end of class
