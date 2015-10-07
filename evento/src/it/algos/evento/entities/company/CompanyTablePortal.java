package it.algos.evento.entities.company;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.evento.demo.DemoDataGenerator;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.Toolbar;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class CompanyTablePortal extends TablePortal {

    public CompanyTablePortal(ModulePop modulo) {
        super(modulo);

        Toolbar toolbar = getToolbar();

        // bottone Altro...
        MenuItem item = toolbar.addButton("Altro...", new ThemeResource("img/action_more.png"), null);


        // crea nuova azienda e utente
        item.addItem("Attiva nuova azienda", new ThemeResource("img/action_add18.png"), new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

                Company company = new Company();
                ActivateCompanyForm form = new ActivateCompanyForm(getModule(), new BeanItem(company));
                form.addFormListener(new AForm.FormListener() {
                    @Override
                    public void cancel_() {
                        form.getWindow().close();
                    }

                    @Override
                    public void commit_() {
                        form.getWindow().close();
                        CompanyService.activateCompany(company, form.getPassword(), form.isCreateData());
                        getTable().getJPAContainer().refresh();
                    }
                });

                Window window = new Window("Attivazione azienda", form);
                window.setResizable(false);

                form.setHeightUndefined();
                window.center();
                UI.getCurrent().addWindow(window);


            }
        });

        item.addSeparator();

        // crea dati demo
        item.addItem("Crea dati demo", new ThemeResource("img/action_gear18.png"), new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {

                Company company = (Company)getTable().getSelectedBean();
                if (company != null) {
                    ConfirmDialog dialog = new ConfirmDialog("Crazione dati","Confermi la creazione dei dati demo per l'azienda "+company+"?",new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
                            if(confirmed){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DemoDataGenerator.createDemoData(company);
                                        Notification.show("Creazione dati demo completata per "+company+".");
                                    }
                                }).start();
                            }
                        }
                    });
                    dialog.show(UI.getCurrent());
                } else {
                    msgNoSelection();
                }

            }
        });

        item.addItem("Cancella dati azienda", new ThemeResource("img/action_delete18.png"), new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                Company company = (Company)getTable().getSelectedBean();
                if (company != null) {
                    ConfirmDialog dialog = new ConfirmDialog("Eliminazione dati","Confermi l'eliminazione di tutti dati dell'azienda "+company+"?",new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
                            if(confirmed){
                                company.deleteAllData();
                                Notification.show("Tutti i dati di "+company+" sono stati cancellati.");
                            }
                        }
                    });
                    dialog.show(UI.getCurrent());
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
        Notification.show("Seleziona prima una azienda.");
    }// end of method

}
