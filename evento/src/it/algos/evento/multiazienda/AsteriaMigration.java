package it.algos.evento.multiazienda;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.IsNull;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.prenotazione.eventi.EventoPren;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe per la migrazione dei dati Asteria a multi azienda
 * Viene creata l'azienda Asteria e tutti i dati vengono attribuiti alla nuova azienda.
 */
public class AsteriaMigration {

    private final static Logger logger = Logger.getLogger(AsteriaMigration.class.getName());
    public static final String CODE_AZIENDA_ASTERIA = "asteria";

    public static void start() {
        migraTabelle();
        aggiungiUUIDPrenotazione();

        logger.log(Level.INFO, "Migrazione completata.");

    }

    /**
     * Regola il riferimento all'azienda in tutte le tabelle
     * <p>
     * La sequenza deve rispettare l'ordine di dipendenza
     */
    private static void migraTabelle() {

        migraTabella(Comune.class);
        migraTabella(Scuola.class);
        migraTabella(Insegnante.class);
        migraTabella(Allegato.class);
        migraTabella(Lettera.class);
        migraTabella(Spedizione.class);
        migraTabella(ModoPagamento.class);

        migraTabella(Progetto.class);
        migraTabella(Evento.class);
        migraTabella(Sala.class);
        migraTabella(Rappresentazione.class);
        migraTabella(Prenotazione.class);
        migraTabella(EventoPren.class);

    }

    /**
     * Regola il riferimento all'azienda in una tabella
     * <p>
     *
     * @param entityClass la entity class (deve essere superclasse di EventoEntity)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void migraTabella(Class entityClass) {
        Company company = getCompanyAsteria();

        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory(EM.PERSISTENCE_UNIT);
        EntityManager manager = factory.createEntityManager();

        JPAContainer container = new ERWContainer(entityClass, manager);
        int quanti = container.size();

        logger.log(Level.INFO,
                "Start migrazione tabella " + entityClass.getSimpleName()
                        + " - " + quanti + " record da migrare");

        int count = 0;

        try {

            manager.getTransaction().begin();

            for (Iterator<Object> i = container.getItemIds().iterator(); i
                    .hasNext(); ) {
                count++;
                Object itemId = i.next();
                EntityItem item = container.getItem(itemId);
                Property property = item.getItemProperty(EventoEntity_.company
                        .getName());
                property.setValue(company);
                logger.log(Level.INFO,
                        "Migrazione " + entityClass.getSimpleName()
                                + " - record " + count);
            }

            manager.getTransaction().commit();

        } catch (Exception e) {
            manager.getTransaction().rollback();
            logger.log(Level.WARNING, "The record cannot be saved ", e);
        }

        manager.close();

        logger.log(Level.INFO,
                "End migrazione tabella " + entityClass.getSimpleName() + " - "
                        + count + " record migrati");

    }

    /**
     * Aggiunge gli UUID in tutte le prenotazioni dove è nullo
     */
    public static void aggiungiUUIDPrenotazione() {
        EntityManager manager = EM.createEntityManager();
        JPAContainer<Prenotazione> container = JPAContainerFactory.makeNonCached(
                Prenotazione.class, manager);
        Filter filter = new IsNull(Prenotazione_.uuid.getName());
        container.addContainerFilter(filter);

        try {

            manager.getTransaction().begin();

            Object id = container.firstItemId();
            while (id != null) {
                EntityItem<Prenotazione> item = container.getItem(id);

                Property property = item.getItemProperty(Prenotazione_.uuid.getName());
                property.setValue(UUID.randomUUID().toString());

                id = container.nextItemId(id);
                logger.log(Level.INFO, "Aggiunto UUID a prenotazione id = " + item.getEntity().getId());
            }
            manager.getTransaction().commit();

        } catch (Exception e) {
            manager.getTransaction().rollback();
            logger.log(Level.INFO, "Aggiunta UUID fallita - rolled back");
        }

        manager.close();
    }

    /**
     * Ritorna la company Centro Asteria.
     *
     * @return la Company dal db, null se non esiste nel db
     */
    public static Company getCompanyAsteria() {
        return Company.query.queryOne(Company_.companyCode,
                CODE_AZIENDA_ASTERIA);
    }

}
