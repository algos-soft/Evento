package it.algos.evento.ui.company;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.web.lib.LibSession;

/**
 * Created by Alex on 05/10/15.
 */
public class CompanyUI2 extends UI {


    @Override
    protected void init(VaadinRequest request) {


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
}
