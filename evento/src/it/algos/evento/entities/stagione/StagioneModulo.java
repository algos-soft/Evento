package it.algos.evento.entities.stagione;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.multiazienda.ERWContainer;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Attribute;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alex on 31/05/15.
 * .
 */
public class StagioneModulo extends EModulePop {


    /**
     * Costruttore senza parametri
     * La classe implementa il pattern Singleton.
     * Per una nuova istanza, usare il metodo statico getInstance.
     * Usare questo costruttore SOLO con la Reflection dal metodo Module.getInstance
     * Questo costruttore è pubblico SOLO per l'usa con la Reflection.
     * Per il pattern Singleton dovrebbe essere privato.
     *
     * @deprecated
     */
    public StagioneModulo() {
        super(Stagione.class);
    }// end of constructor

    /**
     * Crea una sola istanza di un modulo per sessione.
     * Tutte le finestre e i tab di un browser sono nella stessa sessione.
     */
    public static StagioneModulo getInstance(){
        return (StagioneModulo) ModulePop.getInstance(StagioneModulo.class);
    }// end of singleton constructor

    public TablePortal createTablePortal() {
        return new StagioneTablePortal(this);
    }

    @Override
    public ATable createTable() {
        return (new StagioneTable(this));
    }// end of method

    @Override
    public AForm createForm(Item item) {
        return (new StagioneForm(this, item));
    }// end of method

    @Override
    public SearchManager createSearchManager() {
        return new StagioneSearch(this);
    }// end of method

    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return new Attribute[]{Stagione_.sigla, Stagione_.corrente};
    }// end of method

    /**
     * Imposta una stagione come corrente.
     * <p>
     * Invocato dai menu
     * Pone il flag corrente=false da tutte le stagioni
     * Assegna il flag corrente=true alla stagione desiderata
     */
    public static void cmdSetCorrente(final Stagione stagione, final ATable table) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory(EM.PERSISTENCE_UNIT);
        EntityManager manager = factory.createEntityManager();

        JPAContainer container = new ERWContainer(Stagione.class, manager);

        try {

            manager.getTransaction().begin();

            for (Iterator<Object> i = container.getItemIds().iterator(); i.hasNext(); ) {
                Object itemId = i.next();
                EntityItem item = container.getItem(itemId);
                Stagione entity = (Stagione)item.getEntity();
                boolean flag=false;
                if(entity.getId()==stagione.getId()){
                    flag=true;
                }
                Property property = item.getItemProperty(Stagione_.corrente.getName());
                property.setValue(flag);

            }

            manager.getTransaction().commit();

        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

        manager.close();

        table.refresh();


    }


    /**
     * Delete selected items button pressed
     */
    public void delete() {

        // prima controlla se ci sono eventi collegati
        boolean cont=true;
        for (Object id : getTable().getSelectedIds()) {
            BeanItem item = getTable().getBeanItem(id);
            List lista = EQuery.queryList(Evento.class, Evento_.stagione, item.getBean());
            if (lista.size()>0) {
                Notification.show("Impossibile eliminare le stagioni selezionate perché ci sono degli eventi collegati.\nEliminate prima gli eventi.", Notification.Type.WARNING_MESSAGE);
                cont=false;
                break;
            }
        }

        // se tutto ok ritorna il controllo alla superclasse
        if (cont) {
            super.delete();
        }
    }// end of method


}
