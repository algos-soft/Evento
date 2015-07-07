package it.algos.evento;

import it.algos.evento.entities.company.Company;
import it.algos.web.lib.LibSession;

/**
 * Created by alex on 28-05-2015.
 */
public class EventoSession {



    public static Company getCompany(){
        Company company = null;
        Object attr = LibSession.getAttribute("company");
        if((attr!=null)&(attr instanceof Company)){
            company=(Company)attr;
        }
        return company;
    }

    public static void setCompany(Company company) {
        LibSession.setAttribute("company",company);
    }

    public static boolean isManager(){
        boolean manager = false;
        Object attr = LibSession.getAttribute("manager");
        if((attr!=null)&(attr instanceof Boolean)){
            manager=(Boolean)attr;
        }
        return manager;
    }

    public static void setManager(Boolean manager) {
        LibSession.setAttribute("manager",manager);
    }



}
