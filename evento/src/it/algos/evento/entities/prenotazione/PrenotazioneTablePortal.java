package it.algos.evento.entities.prenotazione;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.web.importexport.ExportConfiguration;
import it.algos.web.importexport.ExportManager;
import it.algos.web.importexport.ExportProvider;
import it.algos.web.lib.LibSession;
import it.algos.web.module.ModulePop;
import it.algos.web.table.TablePortal;
import it.algos.web.toolbar.Toolbar;
import it.asteria.cultura.mailing.MailingModulo;

@SuppressWarnings("serial")
public class PrenotazioneTablePortal extends TablePortal {

    public static final String CMD_REGISTRA_PAGAMENTO = "Registra pagamento...";
    public static final ThemeResource ICON_REGISTRA_PAGAMENTO = new ThemeResource("img/action_calc18.png");

    public static final String CMD_RIEPILOGO_OPZIONE = "Invia riepilogo opzione...";
    public static final ThemeResource ICON_RIEPILOGO_OPZIONE = new ThemeResource("img/action_email18.png");

    public static final String CMD_MEMO_INVIO_SCHEDA_PREN = "Promemoria invio scheda prenotazione...";
    public static final ThemeResource ICON_MEMO_INVIO_SCHEDA_PREN = new ThemeResource("img/action_email18.png");

    public static final String CMD_CONGELA_OPZIONE = "Congela opzione...";
    public static final ThemeResource ICON_CONGELA_OPZIONE = new ThemeResource("img/action_snow18.png");

    public static final String CMD_MEMO_SCAD_PAGA = "Promemoria scadenza pagamento...";
    public static final ThemeResource ICON_MEMO_SCAD_PAGA = new ThemeResource("img/action_email18.png");

    public static final String CMD_ATTESTATO_PARTECIPAZIONE = "Attestato di partecipazione...";
    public static final ThemeResource ICON_ATTESTATO_PARTECIPAZIONE = new ThemeResource("img/action_email18.png");

    public static final String CMD_GESTIONE_MAILING = "Crea mailing...";
    public static final ThemeResource ICON_GESTIONE_MAILING = new ThemeResource("img/action_email18.png");

    public static final String CMD_EXPORT = "Esporta...";
    public static final ThemeResource ICON_EXPORT = new ThemeResource("img/action_export.png");


    public PrenotazioneTablePortal(ModulePop modulo) {
        super(modulo);

        Toolbar toolbar = getToolbar();

        // bottone Altro...
        MenuBar.MenuItem item = toolbar.addButton("Altro...", new ThemeResource("img/action_more.png"), null);

        item.addItem("Opzioni da confermare...", new ThemeResource("img/action_clock18.png"), new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroOpzioniDaConfermare();
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Pagamenti da confermare...", new ThemeResource("img/action_clock18.png"), new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Filter filter = PrenotazioneModulo.getFiltroPagamentiDaConfermare();
                JPAContainer cont = getTable().getJPAContainer();
                cont.removeAllContainerFilters();
                cont.refresh(); // refresh container before applying new filters
                cont.addContainerFilter(filter);
            }
        });// end of anonymous class

        item.addItem("Pagamenti da ricevere...", new ThemeResource("img/action_clock18.png"), new MenuBar.Command() {
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
                    if (selected != null) {
                        MailingModulo.gestioneMailing(selected, getUI());
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

    private void msgNoSelection() {
        Notification.show("Seleziona prima una prenotazione.");
    }// end of method

}
