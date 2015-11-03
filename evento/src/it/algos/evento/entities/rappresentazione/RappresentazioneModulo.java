package it.algos.evento.entities.rappresentazione;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.multiazienda.EROContainer;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class RappresentazioneModulo extends EModulePop {

    public RappresentazioneModulo() {
        super(Rappresentazione.class);
    }// end of constructor

    /**
     * Ritorna i posti prenotati per una data rappresentazione.
     */
    public static int getPostiPrenotati(Rappresentazione rapp) {
        int quantiPrenotati = 0;

        // prenotazioni esistenti sulla stassa rapresentazione
        // (escluse le congelate)
        Filter filtroRappresentazione = new Compare.Equal(Prenotazione_.rappresentazione.getName(), rapp);
        Filter filtroNonCongelata = new Compare.Equal(Prenotazione_.congelata.getName(), false);
        EntityManager manager = EM.createEntityManager();
        EROContainer cont = new EROContainer(Prenotazione.class, manager);
        cont.addContainerFilter(filtroRappresentazione);
        cont.addContainerFilter(filtroNonCongelata);

        // spazzola le prenotazioni e calcola il tatale posti prenotati
        List<?> ids = cont.getItemIds(0, cont.size());
        for (Object id : ids) {
            EntityItem<?> item = cont.getItem(id);
            Prenotazione pren = (Prenotazione) item.getEntity();
            quantiPrenotati += pren.getNumTotali();
        }

        manager.close();

        return quantiPrenotati;
    }

    /**
     * Ritorna i posti disponibili per una data rappresentazione.
     */
    public static int getPostiDisponibili(Rappresentazione rapp) {
        return rapp.getCapienza() - getPostiPrenotati(rapp);
    }

    public static void esportaRappresentazione(Object id, UI ui) {
        ArrayList<Prenotazione> lista;
        lista = getListaPrenotazioni(id);
        tableExport(id, lista, ui);
    }// end of method

    public void esportaPartecipanti(UI ui) {

        // crea un container contenente un wrapper per ogni partecipazione
        // alle rappresentazioni correntemente elencate in tabella
        BeanItemContainer<PartecipazioneBean> container = new BeanItemContainer(PartecipazioneBean.class);
        Object[] ids = getTable().getSelectedIds();
        for(Object id:ids){
            EntityItem item = getTable().getJPAContainer().getItem(id);
            Rappresentazione rapp=(Rappresentazione)item.getEntity();
            List<Insegnante> insegnanti = rapp.getInsegnanti();
            for(Insegnante ins : insegnanti){
                PartecipazioneBean bean = new PartecipazioneBean(ins, rapp);
                container.addBean(bean);
            }
        }

        // crea una table da esportare
        Table table = new Table();
        String titoloReport = "Riepilogo partecipanti";
        table.setContainerDataSource(container);

        // i nomi devono corrispondere alle properties del bean (metodi getter senza parola "get")!
        table.setVisibleColumns(new Object[]{"data", "evento", "cognome", "nome", "email"});
        table.setColumnHeaders(new String[]{"Data", "Evento", "Cognome", "Nome" , "Email"});

        final ExcelExport excelExport;

        excelExport = new ExcelExport(table);
        excelExport.setReportTitle(titoloReport);
        excelExport.setExportFileName(titoloReport+".xls");
        excelExport.setDisplayTotals(false);

        Component oldContent = ui.getContent();
        ui.setContent(table);
        excelExport.export();
        ui.setContent(oldContent);


    }// end of method






    private static void tableExport(Object id, ArrayList<Prenotazione> lista, UI ui) {
        Table table = new Table();
        String titoloReport = getTitoloReport(id);
        BeanItemContainer<Prenotazione> container = new BeanItemContainer<Prenotazione>(Prenotazione.class);

        for (Prenotazione bean : lista) {
            container.addBean(bean);
        }// end of for cycle

        table.setContainerDataSource(container);

        table.setVisibleColumns(new Object[]{Prenotazione_.scuola.getName(), Prenotazione_.insegnante.getName(), Prenotazione_.numInteri.getName(), Prenotazione_.numRidotti.getName(),
                Prenotazione_.numDisabili.getName(), Prenotazione_.numAccomp.getName(), Prenotazione_.numTotali.getName()});
        table.setColumnHeaders(new String[]{"Scuola", "Insegnante", "Interi", "Ridotti", "Disabili","Accomp.",
                "Totale"});

        //comp.addComponent(table);
        final ExcelExport excelExport;

        excelExport = new ExcelExport(table);
        excelExport.setReportTitle(titoloReport);
        String filename = StringUtils.stripAccents(titoloReport) + ".xls";    // or ExcelExport throws errors!
        excelExport.setExportFileName(filename);

        Component oldContent = ui.getContent();
        ui.setContent(table);
        excelExport.export();
        ui.setContent(oldContent);

        //comp.removeComponent(table);


    }// end of method

    private static String getTitoloReport(Object id) {
        String titoloReport = "";
        Rappresentazione rappresentazione = Rappresentazione.read(id);
        Evento evento;

        if (rappresentazione != null) {
            evento = rappresentazione.getEvento();
            if (evento != null) {
                titoloReport = evento.getTitolo();
                titoloReport += " - " + rappresentazione.getDateAsString();
            }// end of if cycle
        }// end of if cycle

        return titoloReport;
    }// end of method

    private static ArrayList<Prenotazione> getListaPrenotazioni(Object id) {
        ArrayList<Prenotazione> lista = null;
        List<? extends BaseEntity> listaBean;
        Rappresentazione rappresentazione = Rappresentazione.read(id);

        // qui potrei usare AQuery o EQuery indifferentemente tanto le
        // prenotazioni sono legate direttamente alla rappresentazione
        listaBean = EQuery.queryList(Prenotazione.class, Prenotazione_.rappresentazione, rappresentazione);

        if (listaBean != null) {
            lista = new ArrayList<Prenotazione>();
            for (BaseEntity bean : listaBean) {
                lista.add((Prenotazione) bean);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method

    // come default usa il titolo standard
    // può essere sovrascritto nelle sottoclassi specifiche
    protected String getCaptionSearch() {
        return "rappresentazioni";
    }// end of method

    // come default spazzola tutti i campi della Entity
    // può essere sovrascritto nelle sottoclassi specifiche
    // serve anche per l'ordine con cui vengono presentati i campi
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{Rappresentazione_.dataRappresentazione, Rappresentazione_.evento,
                Rappresentazione_.sala};
    }// end of method

    // come default spazzola tutti i campi della Entity
    // non garantisce l'ordine con cui vengono presentati i campi
    // può essere sovrascritto nelle sottoclassi specifiche (garantendo l'ordine)
    // può mostrare anche il campo ID, oppure no
    // se si vuole differenziare tra Table, Form e Search, sovrascrivere
    // creaFieldsList, creaFieldsForm e creaFieldsSearch
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{Rappresentazione_.evento, Rappresentazione_.sala, Rappresentazione_.capienza,
                Rappresentazione_.dataRappresentazione, Rappresentazione_.note, Rappresentazione_.insegnanti};
    }// end of method

    @Override
    public ATable createTable() {
        return (new RappresentazioneTable(this));
    }// end of method

    @Override
    public AForm createForm(Item item) {
        return (new RappresentazioneForm(this, item));
    }// end of method

    @Override
    public SearchManager createSearchManager() {
        return new RappresentazioneSearch();
    }// end of method

    @Override
    public TablePortal createTablePortal() {
        return new RappresentazioneTablePortal(this);
    }// end of method

    /**
     * Delete selected items button pressed
     */
    public void delete() {

        // prima controlla se ci sono prenotazioni collegate
        boolean cont = true;
        for (Object id : getTable().getSelectedIds()) {
            BeanItem item = getTable().getBeanItem(id);
            List listaPren = EQuery.queryList(Prenotazione.class, Prenotazione_.rappresentazione, item.getBean());
            if (listaPren.size() > 0) {
                Notification.show("Impossibile eliminare le rappresentazioni selezionate perché ci sono delle prenotazioni.\nEliminate prima le prenotazioni collegate o  assegnatele a un'altra rappresentazione.", Notification.Type.WARNING_MESSAGE);
                cont = false;
                break;
            }
        }

        // se tutto ok ritorna il controllo alla superclasse
        if (cont) {
            super.delete();
        }
    }// end of method


}// end of class
