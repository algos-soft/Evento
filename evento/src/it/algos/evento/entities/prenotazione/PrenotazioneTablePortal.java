package it.algos.evento.entities.prenotazione;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.webbase.web.importexport.ExportConfiguration;
import it.algos.webbase.web.importexport.ExportManager;
import it.algos.webbase.web.importexport.ExportProvider;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.Toolbar;
import it.algos.evento.entities.mailing.MailingModulo;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneTablePortal extends TablePortal {

    public static final String CMD_REGISTRA_PAGAMENTO = "Registra pagamento...";
    public static final Resource ICON_REGISTRA_PAGAMENTO = FontAwesome.MONEY;

    public static final String CMD_RIEPILOGO_OPZIONE = "Invia riepilogo opzione...";
    public static final Resource ICON_RIEPILOGO_OPZIONE = FontAwesome.ENVELOPE_O;

    public static final String CMD_MEMO_INVIO_SCHEDA_PREN = "Promemoria invio scheda prenotazione...";
    public static final Resource ICON_MEMO_INVIO_SCHEDA_PREN = FontAwesome.ENVELOPE_O;

    public static final String CMD_CONGELA_OPZIONE = "Congela opzione...";
    public static final Resource ICON_CONGELA_OPZIONE = FontAwesome.LOCK;

    public static final String CMD_SPOSTA_AD_ALTRA_DATA = "Sposta ad altra data...";
    public static final Resource ICON_SPOSTA_AD_ALTRA_DATA = FontAwesome.CARET_SQUARE_O_RIGHT;

    public static final String CMD_MEMO_SCAD_PAGA = "Promemoria scadenza pagamento...";
    public static final Resource ICON_MEMO_SCAD_PAGA = FontAwesome.ENVELOPE_O;

    public static final String CMD_ATTESTATO_PARTECIPAZIONE = "Attestato di partecipazione...";
    public static final Resource ICON_ATTESTATO_PARTECIPAZIONE = FontAwesome.FILE_TEXT_O;

    public static final String CMD_GESTIONE_MAILING = "Crea mailing...";
    public static final Resource ICON_GESTIONE_MAILING = FontAwesome.ENVELOPE_O;

    public static final String CMD_EXPORT = "Esporta...";
    public static final Resource ICON_EXPORT = FontAwesome.DOWNLOAD;


    public PrenotazioneTablePortal(ModulePop modulo) {
        super(modulo);

        Toolbar toolbar = getToolbar();

        // bottone Altro...
        MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

        item.addItem("Opzioni da confermare...", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroOpzioniDaConfermare(Stagione.getStagioneCorrente());
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Pagamenti da confermare...", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPagamentiDaConfermare(Stagione.getStagioneCorrente());
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Pagamenti da ricevere...", FontAwesome.CLOCK_O, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPagamentiDaRicevere();
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addSeparator();

        item.addItem("Registra pagamento...", ICON_REGISTRA_PAGAMENTO, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

                Object id = getTable().getSelectedId();

                // controllo selezione
                if (id != null) {
                    PrenotazioneTable.registraPagamento(id, getTable());
                } else {
                    msgNoSelection();
                }

            }
        });// end of anonymous class

        item.addItem(CMD_RIEPILOGO_OPZIONE, ICON_RIEPILOGO_OPZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Object id = getTable().getSelectedId();
                if (id != null) {
                    PrenotazioneModulo.cmdInviaRiepilogoOpzione(id, getTable());
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(CMD_MEMO_INVIO_SCHEDA_PREN, ICON_MEMO_INVIO_SCHEDA_PREN, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Object id = getTable().getSelectedId();
                if (id != null) {
                    PrenotazioneModulo.cmdPromemoriaInvioSchedaPrenotazione(id, getTable());
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(CMD_MEMO_SCAD_PAGA, ICON_MEMO_SCAD_PAGA, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Object id = getTable().getSelectedId();
                if (id != null) {
                    PrenotazioneModulo.cmdPromemoriaScadenzaPagamento(id, getTable());
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class

        item.addItem(CMD_CONGELA_OPZIONE, ICON_CONGELA_OPZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Object id = getTable().getSelectedId();
                if (id != null) {
                    PrenotazioneModulo.cmdCongelamentoOpzione(id);
                } else {
                    msgNoSelection();
                }
            }
        });// end of anonymous class


        item.addItem(CMD_SPOSTA_AD_ALTRA_DATA, ICON_SPOSTA_AD_ALTRA_DATA, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                BeanItem[] beans = getTable().getSelectedBeans();
                if ((beans != null) && (beans.length>0)) {
                    ArrayList<Prenotazione> lPren = new ArrayList<Prenotazione>();
                    for (BeanItem bean : beans){
                        Prenotazione p = (Prenotazione)bean.getBean();
                        lPren.add(p);
                    }
                    Prenotazione[] aPren=lPren.toArray(new Prenotazione[0]);
                    PrenotazioneModulo.cmdSpostaPrenotazioni(aPren, table);
                } else {
                    Notification.show("Seleziona prima le prenotazioni da spostare.");
                }
            }
        });// end of anonymous class


        item.addItem(CMD_ATTESTATO_PARTECIPAZIONE, ICON_ATTESTATO_PARTECIPAZIONE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Object id = getTable().getSelectedId();
                if (id != null) {
                    PrenotazioneModulo.cmdAttestatoPartecipazione(id);
                } else {
                    msgNoSelection();
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


//		item.addItem("Test pren checker", null, new MenuBar.Command() {
//			public void menuSelected(MenuItem selectedItem) {
//				
//				Date checkDate = LibDate.today();
//				PrenChecker checker = new PrenChecker(checkDate);
//				checker.run();
//
//			}
//		});// end of anonymous class




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
