package it.algos.evento;

import it.algos.evento.entities.company.Company;
import it.algos.web.lib.LibSession;

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
    }// end of method

    public static void setCompany(Company company) {
        LibSession.setAttribute("company", company);
    }// end of method

    public static boolean isManager() {
        boolean manager = false;
        Object attr = LibSession.getAttribute("manager");
        if ((attr != null) & (attr instanceof Boolean)) {
            manager = (Boolean) attr;
        }// fine del blocco if

        return manager;
    }// end of method

    public static void setManager(Boolean manager) {
        LibSession.setAttribute("manager", manager);
    }// end of method

}// end of class
