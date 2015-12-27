package it.algos.evento.entities.prenotazione;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.importexport.ExportProvider;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.Toolbar;
import it.algos.evento.entities.mailing.MailingModulo;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneTablePortal extends TablePortal {

    public static final String CMD_REGISTRA_PAGAMENTO = "Registra pagamento...";
    public static final Resource ICON_REGISTRA_PAGAMENTO = FontAwesome.EURO;

    public static final String CMD_RIEPILOGO_OPZIONE = "Invia riepilogo prenotazione...";
    public static final Resource ICON_RIEPILOGO_OPZIONE = FontAwesome.ENVELOPE_O;

    public static final String CMD_MEMO_INVIO_SCHEDA_PREN = "Invia sollecito conferma prenotazione...";
    public static final Resource ICON_MEMO_INVIO_SCHEDA_PREN = FontAwesome.ENVELOPE_O;

    public static final String CMD_CONGELA_OPZIONE = "Congela prenotazione...";
    public static final Resource ICON_CONGELA_OPZIONE = FontAwesome.LOCK;

    public static final String CMD_SPOSTA_AD_ALTRA_DATA = "Sposta ad altra data...";
    public static final Resource ICON_SPOSTA_AD_ALTRA_DATA = FontAwesome.ARROW_RIGHT;

    public static final String CMD_MEMO_SCAD_PAGA = "Invia sollecito conferma pagamento...";
    public static final Resource ICON_MEMO_SCAD_PAGA = FontAwesome.ENVELOPE_O;

    public static final String CMD_ATTESTATO_PARTECIPAZIONE = "Invia attestato di partecipazione...";
    public static final Resource ICON_ATTESTATO_PARTECIPAZIONE = FontAwesome.ENVELOPE_O;

    public static final String CMD_GESTIONE_MAILING = "Crea mailing...";
    public static final Resource ICON_GESTIONE_MAILING = FontAwesome.ENVELOPE_O;

    public static final String CMD_EXPORT = "Esporta...";
    public static final Resource ICON_EXPORT = FontAwesome.DOWNLOAD;


    public PrenotazioneTablePortal(ModulePop modulo) {
        super(modulo);

        Toolbar toolbar = getToolbar();

        // bottone Altro...
        MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

        item.addItem("Mostra prenotazioni scadute", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPrenotazioniScadute();
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Mostra conferme pagamento scadute", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPagamentiDaConfermare();
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

//        item.addItem("Mostra pagamenti scaduti...", FontAwesome.CLOCK_O, new MenuBar.Command() {
//            public void menuSelected(MenuItem selectedItem) {
//                Filter filter = PrenotazioneModulo.getFiltroPagamentiScaduti();
//                JPAContainer cont = getTable().getJPAContainer();
//                cont.removeAllContainerFilters();
//                cont.refresh(); // refresh container before applying new filters
//                cont.addContainerFilter(filter);
//            }
//        });// end of anonymous class

        item.addItem("Mostra prenotazioni congelate", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPrenCongelate();
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addSeparator();

        item.addItem(CMD_RIEPILOGO_OPZIONE, ICON_RIEPILOGO_OPZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedBean() != null) {
                    getPrenotazioneTable().inviaRiepilogoPrenotazione();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(CMD_MEMO_INVIO_SCHEDA_PREN, ICON_MEMO_INVIO_SCHEDA_PREN, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedBean() != null) {
                    getPrenotazioneTable().inviaMemoConfermaPren();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(CMD_MEMO_SCAD_PAGA, ICON_MEMO_SCAD_PAGA, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedBean() != null) {
                    getPrenotazioneTable().inviaPromemoriaScadenzaPagamento();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(CMD_ATTESTATO_PARTECIPAZIONE, ICON_ATTESTATO_PARTECIPAZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedBean() != null) {
                    getPrenotazioneTable().inviaAttestatoPartecipazione();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class


        item.addItem(CMD_REGISTRA_PAGAMENTO, ICON_REGISTRA_PAGAMENTO, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                getPrenotazioneTable().registraPagamento();
            }
        });// end of anonymous class



        item.addItem(CMD_CONGELA_OPZIONE, ICON_CONGELA_OPZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                if (getTable().getSelectedBean() != null) {
                    getPrenotazioneTable().congelaPrenotazione();
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class


        item.addItem(CMD_SPOSTA_AD_ALTRA_DATA, ICON_SPOSTA_AD_ALTRA_DATA, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                BeanItem[] beans = getTable().getSelectedBeans();
                if ((beans != null) && (beans.length>0)) {
                    getPrenotazioneTable().spostaAdAltraData();
                } else {
                    Notification.show("Seleziona prima le prenotazioni da spostare.");
                }
            }
        });// end of anonymous class




        item.addSeparator();

        item.addItem(CMD_EXPORT, ICON_EXPORT, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Class clazz = Prenotazione.class;
                String filename = "prenotazioni";
                JPAContainer cont = getTable().getJPAContainer();
                ExportProvider provider = new PrenExportProvider();
                ExportConfiguration conf = new ExportConfiguration(clazz, filename, cont, provider);
                new ExportManager(conf).show(getUI());
            }// end of method
        });// end of anonymous class


        if (LibSession.isDeveloper()) {
            item.addItem(CMD_GESTIONE_MAILING, ICON_GESTIONE_MAILING, new MenuBar.Command() {
                public void menuSelected(MenuItem selectedItem) {
                    Object[] selected = getTable().getSelectedIds();
                    ArrayList<Long> selezionati = getSelIds(selected);
                    if (selected != null) {
                        MailingModulo.gestioneMailing(selezionati, getUI());
                    } else {
                        msgNoSelection();
                    }
                }
            });// end of anonymous class
        }// fine del blocco if


        item.addItem("Test lettera", null, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Object bean = getTable().getSelectedBean();
                if (bean != null) {
                    Prenotazione pren = (Prenotazione) bean;
                    PrenotazioneModulo.testLettera(pren);
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class




    }// end of method


    /**
     * Ritorna la table specifica
     */
    private PrenotazioneTable getPrenotazioneTable(){
        PrenotazioneTable pTable=null;
        ATable table = getTable();
        if(table!=null && table instanceof PrenotazioneTable){
            pTable=(PrenotazioneTable)table;
        }
        return  pTable;
    }

    /**
     * Gli oggetti selezionati sono SEMPRE dei valori Long
     * Trasformo in un array di long, più facile da gestire
     */
    private ArrayList<Long> getSelIds(Object[] selected) {
        ArrayList<Long> selezionati = null;

        if (selected != null && selected.length > 0) {
            selezionati = new ArrayList<Long>();

            for (Object obj : selected) {
                if (obj instanceof Long) {
                    selezionati.add((long) obj);
                }// fine del blocco if
            } // fine del ciclo for-each

        }// fine del blocco if

        return selezionati;
    }// end of method

    private void msgNoSelection() {
        Notification.show("Seleziona prima una prenotazione.");
    }// end of method

}
