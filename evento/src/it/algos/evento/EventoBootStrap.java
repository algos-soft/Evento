package it.algos.evento;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.daemons.DaemonPrenScadute;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraKeys;
import it.algos.evento.entities.lettera.Lettera_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.multiazienda.AsteriaMigration;
import it.algos.evento.pref.EventoPrefs;
import it.algos.web.BootStrap;
import it.algos.web.entity.BaseEntity;
import it.algos.web.entity.EM;
import it.algos.web.query.AQuery;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 * Executed on container startup
 * <p>
 * Setup non-UI logic here <br>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO creare la sottoclasse per regolare il valore della persistence unit che crea l'EntityManager <br>
 */
public class EventoBootStrap extends BootStrap {

    /**
     * Valore standard suggerito per ogni progetto
     * Questo singolo progetto può modificarlo nel metodo setPersistenceEntity()
     */
    private static final String DEFAULT_PERSISTENCE_UNIT = "MySqlUnit";

    /**
     * @return the name of the current user
     */
    public static String getUsername() {
        return ""; // not implemented
    }// end of method

    /**
     * Regola il valore della persistence unit per crearae l'EntityManager <br>
     * DEVE essere sovrascritto (obbligatorio) nella sottoclasse del progetto <br>
     */
    @Override
    public void setPersistenceEntity() {
        EM.PERSISTENCE_UNIT = DEFAULT_PERSISTENCE_UNIT;
    }// end of method

    /**
     * Executed on container startup
     * <p>
     * Setup non-UI logic here
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        ServletContext svltCtx = BootStrap.getServletContext();

        // registra il servlet context non appena è disponibile
        EventoApp.setServletContext(svltCtx);

        // Creo l'azienda Asteria se non esiste.
        AsteriaMigration.ensureCompanyAsteria();

        // Creo l'azienda Demo se non esiste
        DemoDataGenerator.ensureCompanyDemo();

        // avvia lo scheduler controllo solleciti che esegue ogni ora
        if (EventoPrefs.startDaemonAtStartup.getBool()) {
            DaemonPrenScadute.getInstance().start();
        }// fine del blocco if

        //esegue qualcosa per ogni Company
        List<Company> comps = Company.query.getList();
        for (Company company : comps) {
            doForCompany(company);
        } // fine del ciclo for
    }// end of method


    /**
     * Esegue per ogni company allo startup del server
     */
    private void doForCompany(Company company) {

        // Controlla che esista la lettera demo con elencate
        // le sostituzioni della Enumeration LetteraKeys.
        // Se manca la crea ora, se c'è la aggiorna.
        Container.Filter fComp = new Compare.Equal(Lettera_.company.getName(), company);
        Container.Filter fType = new Compare.Equal(Lettera_.sigla.getName(), ModelliLettere.demo.getDbCode());
        Container.Filter filter = new And(fComp, fType);
        BaseEntity entity = AQuery.getEntity(Lettera.class, filter);

        Lettera lettera;
        if (entity == null) {
            String siglaDemo = ModelliLettere.demo.getDbCode();
            lettera = new Lettera(siglaDemo, ModelliLettere.demo.getOggettoDefault(), LetteraKeys.getTestoDemo());
            lettera.setCompany(company);
        }else{
            lettera=(Lettera)entity;
            lettera.setTesto(LetteraKeys.getTestoDemo());
        }// fine del blocco if

        lettera.save();


    }// end of method

    /**
     * Tear down logic here<br>
     * Sovrascritto
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        // arresta gli schedulers
        DaemonPrenScadute.getInstance().stop();

        super.contextDestroyed(sce);

    }// end of method

}// end of boot class
