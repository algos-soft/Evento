package it.algos.evento.entities.prenotazione;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.TextArea;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.insegnante.InsegnanteForm;
import it.algos.evento.entities.insegnante.Insegnante_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.PrenotazioneFormToolbar.PrenotazioneFormToolbarListener;
import it.algos.evento.entities.prenotazione.eventi.EventiInPrenTable;
import it.algos.evento.entities.prenotazione.eventi.EventoPrenTable;
import it.algos.evento.entities.prenotazione.eventi.EventoPren_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.scuola.ScuolaForm;
import it.algos.evento.entities.scuola.Scuola_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.multiazienda.ERelatedComboField;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.*;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.toolbar.FormToolbar;
import it.algos.webbase.web.toolbar.FormToolbar.FormToolbarListener;
import org.joda.time.DateTime;

import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class PrenotazioneForm extends AForm {

    private static final String WF = "50px";
    private IntegerField fieldNumTotale;
    private IntegerField fieldDisponibili;
    private DecimalField fieldImportoTotale;
    private DecimalField fieldImportoTotale2;    // nella seconda pagina
    private CheckBoxField fieldCongelata;
    private TextField fieldClasse;
    private RelatedComboField comboScuola;
    private CheckBoxField fieldPrivato;
    private EventoPrenTable eventsTable; // la table con gli eventi
    private Label dettaglioInsegnante;

    public PrenotazioneForm(ModulePop modulo) {
        this(modulo, null);
        doInit();
    }// end of constructor

    public PrenotazioneForm(ModulePop modulo, Item item) {
        super(modulo, item);
        doInit();
    }// end of constructor

    private void doInit() {
        //setMargin(true);
    }


    @Override
    protected void init() {
        super.init();

        refreshDettaglioInsegnante();

    }

    /**
     * Aggiorna l'area di dettaglio insegnante in base all'insegnante correntemente selezionato
     */
    private void refreshDettaglioInsegnante() {
        RelatedComboField field = (RelatedComboField) getField(Prenotazione_.insegnante);
        Object bean = field.getSelectedBean();
        if (bean != null) {
            Insegnante ins = (Insegnante) bean;
            dettaglioInsegnante.setValue(ins.getDettaglioPren());
        }
    }

//	private void refreshDisplayPrivato(){
//		boolean privato = (Boolean)getField(Prenotazione_.privato).getValue();
//		getField(Prenotazione_.privato).setVisible(privato);
//	}

    /**
     * Sincronizza i riferimenti in scheda (email, telefono, flag privato) con quelli dell'insegnante
     */
    @SuppressWarnings("unchecked")
    private void syncRiferimentiInsegnante() {
        RelatedComboField field = (RelatedComboField) getField(Prenotazione_.insegnante);
        Object bean = field.getSelectedBean();
        if (bean != null) {
            Insegnante ins = (Insegnante) bean;
            getField(Prenotazione_.telRiferimento).setValue(ins.getTelefono());
            getField(Prenotazione_.emailRiferimento).setValue(ins.getEmail());
            Field fp = getField(Prenotazione_.privato);
            boolean priv = ins.isPrivato();
            fp.setValue(priv);
        }
    }


    protected FormToolbar createToolBar() {
        // create the toolbar
        PrenotazioneFormToolbar toolbar = new PrenotazioneFormToolbar();

        // listener per eventi generali della scheda
        toolbar.addToolbarListener(new FormToolbarListener() {

            @Override
            public void save_() {
                save();
            }// end of method

            @Override
            public void reset_() {
                // TODO Auto-generated method stub
            }// end of method

            @Override
            public void cancel_() {
                fire(FormEvent.cancel);
            }// end of method

        });// end of anonymous class

        // listener per eventi specifici della scheda prenotazione
        toolbar.addToolbarListener(new PrenotazioneFormToolbarListener() {

            @Override
            public void confermaPrenotazione() {
                confermaPrenotazioneForm();
            }
        });

        return toolbar;
    }// end of method

    @Override
    protected void createFields() {

        @SuppressWarnings("rawtypes")
        Field field;
        RelatedComboField rcField;

        field = new IntegerField("Numero prenotazione");
        field.setWidth("80px");
        addField(Prenotazione_.numPrenotazione, field);

        rcField = new ERelatedComboField(Rappresentazione.class, "Rappresentazione");
        rcField.sort(Rappresentazione_.dataRappresentazione);
        addField(Prenotazione_.rappresentazione, rcField);

        // se nuovo record, il popup delle rappresentazioni mostra solo le rappresentazioni della stagione corrente
        if (isNewRecord()) {
            JPAContainer cont = rcField.getJPAContainer();
            String prop = Evento.class.getSimpleName().toLowerCase() + "." + Evento_.stagione.getName();
            cont.addNestedContainerProperty(prop);
            Container.Filter filter = new Compare.Equal(prop, Stagione.getStagioneCorrente());
            rcField.getJPAContainer().addContainerFilter(filter);
        }


        // invocato quando si seleziona una rappresentazione nel popup
        rcField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                copyPrezziDaEvento();
                syncTotImporto();
            }
        });


        field = new DateField("Data prenotazione");
        addField(Prenotazione_.dataPrenotazione, field);

        comboScuola = new ERelatedComboField(Scuola.class, "Scuola");
        comboScuola.sort(Scuola_.sigla);
        comboScuola.setNewItemHandler(ScuolaForm.class, Scuola_.sigla);
        addField(Prenotazione_.scuola, comboScuola);

        RelatedComboField comboInsegnante = new ERelatedComboField(Insegnante.class, "Insegnante");
        comboInsegnante.sort(Insegnante_.cognome);
        comboInsegnante.setNewItemHandler(InsegnanteForm.class, Insegnante_.cognome);
        addField(Prenotazione_.insegnante, comboInsegnante);

        // invocato quando si registrano delle modifiche dal popup
        comboInsegnante.addRecordEditedListener(new RelatedComboField.RecordEditedListener() {

            @Override
            public void save_(BeanItem bi, boolean newRecord) {
                syncRiferimentiInsegnante();
                refreshDettaglioInsegnante();
//				refreshDisplayPrivato();
            }
        });

        // invocato quando si seleziona un insegnante nel popup
        comboInsegnante.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                RelatedComboField rcField = (RelatedComboField) getField(Prenotazione_.insegnante);
                Object bean = rcField.getSelectedBean();
                if (bean == null) {
                    dettaglioInsegnante.setValue("");
                } else {
                    syncRiferimentiInsegnante();
                    refreshDettaglioInsegnante();
//					refreshDisplayPrivato();
                }
            }
        });

        fieldClasse = new TextField("Classe");
        addField(Prenotazione_.classe, fieldClasse);

        field = new TextField("Tel. referente");
        field.setWidth("260px");
        addField(Prenotazione_.telRiferimento, field);

        field = new EmailField("E-mail referente");
        addField(Prenotazione_.emailRiferimento, field);

        field = new IntegerField("Interi");
        field.setWidth(WF);
        addField(Prenotazione_.numInteri, field);

        field = new IntegerField("Ridotti");
        field.setWidth(WF);
        addField(Prenotazione_.numRidotti, field);

        field = new IntegerField("Disabili");
        field.setWidth(WF);
        addField(Prenotazione_.numDisabili, field);

        field = new IntegerField("Accomp.");
        field.setWidth(WF);
        addField(Prenotazione_.numAccomp, field);

        field = new DecimalField("Interi");
        field.setWidth(WF);
        addField(Prenotazione_.importoIntero, field);

        field = new DecimalField("Ridotti");
        field.setWidth(WF);
        addField(Prenotazione_.importoRidotto, field);

        field = new DecimalField("Disabili");
        field.setWidth(WF);
        addField(Prenotazione_.importoDisabili, field);

        field = new DecimalField("Accomp.");
        field.setWidth(WF);
        addField(Prenotazione_.importoAccomp, field);

        field = new ERelatedComboField(ModoPagamento.class, "Modo");
        field.setWidth("180px");
        addField(Prenotazione_.modoPagamento, field);

        field = new DateField("Scadenza pagamento");
        addField(Prenotazione_.scadenzaPagamento, field);

        field = new CheckBoxField("Pagamento confermato");
        addField(Prenotazione_.pagamentoConfermato, field);

        field = new DecimalField("Importo");
        addField(Prenotazione_.importoPagato, field);

        field = new DateField("Data pagam. confermato");
        addField(Prenotazione_.dataPagamentoConfermato, field);

        field = new ERelatedComboField(TipoRicevuta.class, "Tipo ricevuta");
        field.setWidth("180px");
        addField(Prenotazione_.tipoRicevuta, field);

        TextArea area = new TextArea();
        area.setRows(10);
        addField(Prenotazione_.note, area);

        field = new DateField("Scadenza conferma");
        addField(Prenotazione_.scadenzaConferma, field);

        field = new IntegerField("Livello sollecito conf.");
        addField(Prenotazione_.livelloSollecitoConferma, field);

        fieldCongelata = new CheckBoxField("Congelata");
        addField(Prenotazione_.congelata, fieldCongelata);

        field = new CheckBoxField("Confermata");
        addField(Prenotazione_.confermata, field);

        field = new DateField("Data conferma");
        addField(Prenotazione_.dataConferma, field);

        field = new CheckBoxField("Pagamento ricevuto");
        addField(Prenotazione_.pagamentoRicevuto, field);

        field = new DateField("Data pagam. ricevuto");
        addField(Prenotazione_.dataPagamentoRicevuto, field);

        field = new IntegerField("Livello sollecito pagam.");
        addField(Prenotazione_.livelloSollecitoPagamento, field);

        fieldPrivato = new CheckBoxField("Privato");
        addField(Prenotazione_.privato, fieldPrivato);
        fieldPrivato.addValueChangeListener(event -> {
            onPrivatoChange();
        });


    }

    /**
     * Invocato quando il valore del flag privato cambia.
     * Sincronizza i campi dipendenti.
     */
    private void onPrivatoChange() {
        boolean privato = fieldPrivato.getValue();
        fieldClasse.setVisible(!privato);
        fieldClasse.removeAllValidators();
        comboScuola.removeAllValidators();
        Component comp = comboScuola.getEditComponent();
        comp.setVisible(!privato);
        if (!privato) {
            fieldClasse.addValidator(new StringLengthValidator("La classe è obbligatoria", 1, null, true));
            comboScuola.addValidator(new NullValidator("La scuola è obbligatoria", false));
        }
    }

    protected Component createComponent() {

        TabSheet tabsheet = new TabSheet();
        tabsheet.setWidth("700px");
        tabsheet.addTab(creaTabGenerale(), "Generale");
        tabsheet.addTab(creaTabPagamento(), "Pagamento");
        tabsheet.addTab(creaTabEventi(), "Eventi e note");
        postLayout();
        return tabsheet;

    }// end of method

    private Component creaTabGenerale() {

        // campi esistenti solo nella Presentation
        fieldNumTotale = new IntegerField("Totale");
        fieldNumTotale.setWidth("70px");
        fieldNumTotale.setReadOnly(true);

        fieldDisponibili = new IntegerField("Rimasti");
        fieldDisponibili.setWidth(WF);
        fieldDisponibili.setReadOnly(true);

        fieldImportoTotale = new DecimalField("Importo");
        fieldImportoTotale.setWidth("70px");
        fieldImportoTotale.setReadOnly(true);

        dettaglioInsegnante = new Label("", ContentMode.HTML);

        Layout layout = new AFormLayout();

        HorizontalLayout l1 = new HorizontalLayout();
        l1.setSpacing(true);

        Component comp;
        comp = getField(Prenotazione_.numPrenotazione);
        l1.addComponent(comp);
        l1.setComponentAlignment(comp, Alignment.BOTTOM_LEFT);

        comp = getField(Prenotazione_.dataPrenotazione);
        l1.addComponent(comp);
        l1.setComponentAlignment(comp, Alignment.BOTTOM_LEFT);

        layout.addComponent(l1);

        layout.addComponent(getField(Prenotazione_.rappresentazione));

        HorizontalLayout insPanel = new HorizontalLayout();
        insPanel.setSpacing(true);
        RelatedComboField comboInsegnante = (RelatedComboField) getField(Prenotazione_.insegnante);
        Component comboEdit = comboInsegnante.getEditComponent();
        //Field fPrivato=getField(Prenotazione_.privato);
        insPanel.addComponent(comboEdit);
        insPanel.addComponent(fieldPrivato);
        insPanel.setComponentAlignment(comboEdit, Alignment.BOTTOM_LEFT);
        insPanel.setComponentAlignment(fieldPrivato, Alignment.BOTTOM_LEFT);
        layout.addComponent(insPanel);


        //HorizontalLayout dettPanel = new HorizontalLayout();
        layout.addComponent(dettaglioInsegnante);
//		fPrivato.setEnabled(false);
//		fPrivato.setVisible((boolean) fPrivato.getValue());
        //dettPanel.addComponent(new Spacer());
        //dettPanel.addComponent(fPrivato);
        //dettPanel.setComponentAlignment(fPrivato, Alignment.BOTTOM_RIGHT);
        //layout.addComponent(dettPanel);

        //RelatedComboField comboScuola = (RelatedComboField)getField(Prenotazione_.scuola);
        layout.addComponent(comboScuola.getEditComponent());

        layout.addComponent(fieldClasse);
        layout.addComponent(getField(Prenotazione_.telRiferimento));
        layout.addComponent(getField(Prenotazione_.emailRiferimento));


        // grid persone e prezzi
        layout.addComponent(creaGridPersonePrezzi());

        // pannello conferma prenotazione
        HorizontalLayout confermaPanel = new HorizontalLayout();
        confermaPanel.setSpacing(true);
        confermaPanel.addComponent(getField(Prenotazione_.scadenzaConferma));
        confermaPanel.addComponent(getField(Prenotazione_.dataConferma));
        confermaPanel.addComponent(getField(Prenotazione_.livelloSollecitoConferma));

        Component fieldCongelata = getField(Prenotazione_.congelata);
        confermaPanel.addComponent(fieldCongelata);
        confermaPanel.setComponentAlignment(fieldCongelata, Alignment.BOTTOM_LEFT);

        Component fieldConfermata = getField(Prenotazione_.confermata);
        confermaPanel.addComponent(fieldConfermata);
        confermaPanel.setComponentAlignment(fieldConfermata, Alignment.BOTTOM_LEFT);

        layout.addComponent(confermaPanel);


        return incapsulaPerMargine(layout);
    }


    private GridLayout creaGridPersonePrezzi() {

        // recupera i Fields
        Field fldNumInt = getField(Prenotazione_.numInteri);
        Label lblNumInt = new Label(fldNumInt.getCaption());
        fldNumInt.setCaption(null);

        Field fldNumRid = getField(Prenotazione_.numRidotti);
        Label lblNumRid = new Label(fldNumRid.getCaption());
        fldNumRid.setCaption(null);

        Field fldNumDis = getField(Prenotazione_.numDisabili);
        Label lblNumDis = new Label(fldNumDis.getCaption());
        fldNumDis.setCaption(null);

        Field fldNumAcc = getField(Prenotazione_.numAccomp);
        Label lblNumAcc = new Label(fldNumAcc.getCaption());
        fldNumAcc.setCaption(null);

        Field fldNumTot = fieldNumTotale;
        Label lblNumTot = new Label(fldNumTot.getCaption());
        fldNumTot.setCaption(null);

        Field fldNumAvail = fieldDisponibili;
        Label lblNumAvail = new Label(fldNumAvail.getCaption());
        fldNumAvail.setCaption(null);

        Field fldImpInt = getField(Prenotazione_.importoIntero);
        fldImpInt.setCaption(null);

        Field fldImpRid = getField(Prenotazione_.importoRidotto);
        fldImpRid.setCaption(null);

        Field fldImpDis = getField(Prenotazione_.importoDisabili);
        fldImpDis.setCaption(null);

        Field fldImpAcc = getField(Prenotazione_.importoAccomp);
        fldImpAcc.setCaption(null);

        Field fldImpTot = fieldImportoTotale;
        fldImpTot.setCaption(null);


        // grid persone e prezzi
        GridLayout grid = new GridLayout(7, 3);
        grid.setSpacing(true);
        int row;


        // Riga dei titoli
        row = 0;
        grid.addComponent(lblNumInt, 1, row);
        grid.addComponent(lblNumRid, 2, row);
        grid.addComponent(lblNumDis, 3, row);
        grid.addComponent(lblNumAcc, 4, row);
        grid.addComponent(lblNumTot, 5, row);
        grid.addComponent(lblNumAvail, 6, row);

        // Riga degli spettatori
        row = 1;
        Label labelNum = new Label("quantità");
        grid.addComponent(labelNum, 0, row);
        grid.setComponentAlignment(labelNum, Alignment.MIDDLE_RIGHT);
        grid.addComponent(fldNumInt, 1, row);
        grid.addComponent(fldNumRid, 2, row);
        grid.addComponent(fldNumDis, 3, row);
        grid.addComponent(fldNumAcc, 4, row);
        grid.addComponent(fldNumTot, 5, row);
        grid.addComponent(fldNumAvail, 6, row);

        // Riga dei prezzi
        row = 2;
        Label labelPrice = new Label("prezzo");
        grid.addComponent(labelPrice, 0, row);
        grid.setComponentAlignment(labelPrice, Alignment.MIDDLE_RIGHT);
        grid.addComponent(fldImpInt, 1, row);
        grid.addComponent(fldImpRid, 2, row);
        grid.addComponent(fldImpDis, 3, row);
        grid.addComponent(fldImpAcc, 4, row);
        grid.addComponent(fldImpTot, 5, row);

        return grid;
    }

    private Component creaTabPagamento() {
        Layout layout = new AFormLayout();

        fieldImportoTotale2 = new DecimalField("Importo");
        fieldImportoTotale2.setWidth("80px");
        fieldImportoTotale2.setReadOnly(true);


        // pannello pagamento da effettuare
        HorizontalLayout pagarePanel = new HorizontalLayout();
        pagarePanel.setSpacing(true);
        pagarePanel.setCaption("A pagare");
        pagarePanel.addComponent(fieldImportoTotale2);
        pagarePanel.addComponent(getField(Prenotazione_.modoPagamento));
        pagarePanel.addComponent(getField(Prenotazione_.scadenzaPagamento));
        layout.addComponent(pagarePanel);

        layout.addComponent(new Label("&nbsp;", com.vaadin.shared.ui.label.ContentMode.HTML));

        // pannello pagamento effettuato
        HorizontalLayout pagatoPanel = new HorizontalLayout();
        // FormLayout pagatoPanel = new FormLayout();
        pagatoPanel.setSpacing(true);
        pagatoPanel.setCaption("Conferma pagamento");
        pagatoPanel.addComponent(getField(Prenotazione_.importoPagato));
        pagatoPanel.addComponent(getField(Prenotazione_.dataPagamentoConfermato));
        Component fieldPagato = getField(Prenotazione_.pagamentoConfermato);
        pagatoPanel.addComponent(fieldPagato);
        pagatoPanel.setComponentAlignment(fieldPagato, Alignment.BOTTOM_LEFT);
        layout.addComponent(pagatoPanel);

        layout.addComponent(new Label("&nbsp;", com.vaadin.shared.ui.label.ContentMode.HTML));

        // pannello pagamento ricevuto
        HorizontalLayout contabPanel = new HorizontalLayout();
        contabPanel.setSpacing(true);
        contabPanel.addComponent(getField(Prenotazione_.dataPagamentoRicevuto));
        contabPanel.addComponent(getField(Prenotazione_.livelloSollecitoPagamento));
        Component fieldContabilizzato = getField(Prenotazione_.pagamentoRicevuto);
        contabPanel.addComponent(fieldContabilizzato);
        contabPanel.setComponentAlignment(fieldContabilizzato, Alignment.BOTTOM_LEFT);
        layout.addComponent(contabPanel);

        layout.addComponent(new Label("&nbsp;", com.vaadin.shared.ui.label.ContentMode.HTML));

        // tipo di tiporicevuta
        layout.addComponent(getField(Prenotazione_.tipoRicevuta));

        return incapsulaPerMargine(layout);

    }

    private Component creaTabEventi() {
        Component comp;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth("100%");

        eventsTable = new EventiInPrenTable();
        eventsTable.setPageLength(10);
        JPAContainer container = eventsTable.getJPAContainer();

        SingularAttribute attr = EventoPren_.prenotazione;
        String name = attr.getName();
        Filter filter = new Compare.Equal(name, getPrenotazione());
        container.removeAllContainerFilters();
        container.addContainerFilter(filter);
        container.sort(new String[]{EventoPren_.timestamp.getName()}, new boolean[]{true});

        eventsTable.setWidth("100%");
        layout.addComponent(eventsTable);

        comp = getField(Prenotazione_.note);
        comp.setWidth("100%");
        layout.addComponent(comp);

        return incapsulaPerMargine(layout);
    }

//	/**
//	 * Refreshes the table containing the events
//	 */
//	private void refreshEventsTable() {
//		eventsTable.getJPAContainer().removeAllContainerFilters();
//		eventsTable.refresh();
//	}

    // private Component creaTabNote() {
    // VerticalLayout layout = new VerticalLayout();
    // layout.setMargin(true);
    // layout.setSpacing(true);
    // layout.setWidth("100%");
    // Component comp = getField(Prenotazione_.note);
    // comp.setWidth("100%");
    // layout.addComponent(comp);
    // return layout;
    // }
    //

    /**
     * Eseguito dopo il disegno dei layout di tutte le pagine
     */
    @SuppressWarnings("rawtypes")
    private void postLayout() {
        // set read-only fields
        getField(Prenotazione_.numPrenotazione).setReadOnly(true);

        // aggiungi i listeners per la sincronizzazione dei totali
        Field field;
        field = getField(Prenotazione_.rappresentazione);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
                syncDataScadenzaPagamento();
            }
        });

        field = getField(Prenotazione_.numInteri);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.numRidotti);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.numDisabili);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.numAccomp);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });

        field = getField(Prenotazione_.importoIntero);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoRidotto);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoDisabili);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });
        field = getField(Prenotazione_.importoAccomp);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotImporto();
            }
        });


        field = getField(Prenotazione_.congelata);
        field.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                syncTotali();
            }
        });
        field = getField(Prenotazione_.dataPrenotazione);
        field.addValueChangeListener(new ValueChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                Object obj = getField(Prenotazione_.dataPrenotazione).getValue();
                if ((obj != null) && (obj instanceof Date)) {
                    Date dataPren = (Date) obj;
                    Date dataScadConf = Prenotazione.getDataScadenzaConferma(dataPren);
                    getField(Prenotazione_.scadenzaConferma).setValue(dataScadConf);
                }
            }
        });

        syncTotali();

        onPrivatoChange();

    }

    private void syncTotali() {
        syncTotSpettatori();
        syncTotImporto();
        syncDisponibili();
    }

    /**
     * Calcola la data scadenza pagamento in base alla data della rappresentazione
     */
    @SuppressWarnings("unchecked")
    private void syncDataScadenzaPagamento() {
        RelatedComboField fRappresentazione = (RelatedComboField) getField(Prenotazione_.rappresentazione);
        Rappresentazione rapp = (Rappresentazione) fRappresentazione.getSelectedBean();
        Date dataScad = null;
        if (rapp != null) {
            Date dataRapp = rapp.getDataRappresentazione();
            DateTime dt = new DateTime(dataRapp);
            dt = dt.minusDays(CompanyPrefs.ggScadConfermaPagamento.getInt());
            dataScad = dt.toDate();
        }

        getField(Prenotazione_.scadenzaPagamento).setValue(dataScad);

    }

    private void syncTotSpettatori() {

        int interi = getIntValue(Prenotazione_.numInteri);
        int ridotti = getIntValue(Prenotazione_.numRidotti);
        int disabili = getIntValue(Prenotazione_.numDisabili);
        int accomp = getIntValue(Prenotazione_.numAccomp);
        Integer tot = interi + ridotti + disabili + accomp;
        boolean roState = fieldNumTotale.isReadOnly();
        fieldNumTotale.setReadOnly(false);
        fieldNumTotale.setValue(tot);
        fieldNumTotale.setReadOnly(roState);
    }

    /**
     * Sincronizza l'importo totale come somma dei vari importi (q.tà x prezzo)
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void syncTotImporto() {

        // recupera l'importo totale
        int nInteri = getIntValue(Prenotazione_.numInteri);
        int nRidotti = getIntValue(Prenotazione_.numRidotti);
        int nDisabili = getIntValue(Prenotazione_.numDisabili);
        int nAccomp = getIntValue(Prenotazione_.numAccomp);
        BigDecimal iInteri = getBigDecimalValue(Prenotazione_.importoIntero);
        BigDecimal iRidotti = getBigDecimalValue(Prenotazione_.importoRidotto);
        BigDecimal iDisabili = getBigDecimalValue(Prenotazione_.importoDisabili);
        BigDecimal iAccomp = getBigDecimalValue(Prenotazione_.importoAccomp);

        BigDecimal totPren = Prenotazione.getTotImporto(nInteri, nRidotti, nDisabili, nAccomp, iInteri, iRidotti, iDisabili, iAccomp);

        // scrive il valore nei campi totale
        Field field;
        boolean roState;

        field = fieldImportoTotale;
        roState = field.isReadOnly();
        field.setReadOnly(false);
        field.setValue(totPren);
        field.setReadOnly(roState);

        field = fieldImportoTotale2;
        roState = field.isReadOnly();
        field.setReadOnly(false);
        field.setValue(totPren);
        field.setReadOnly(roState);


    }


    /**
     * Copia i prezzi dall'evento correntemente selezionato ai campi della scheda
     */
    private void copyPrezziDaEvento() {
        RelatedComboField field = (RelatedComboField) getField(Prenotazione_.rappresentazione);
        Object bean = field.getSelectedBean();
        if (bean != null) {
            Rappresentazione rapp = (Rappresentazione) bean;
            Evento evento = rapp.getEvento();
            getField(Prenotazione_.importoIntero).setValue(evento.getImportoIntero());
            getField(Prenotazione_.importoRidotto).setValue(evento.getImportoRidotto());
            getField(Prenotazione_.importoDisabili).setValue(evento.getImportoDisabili());
            getField(Prenotazione_.importoAccomp).setValue(evento.getImportoAccomp());
        }
    }

    /**
     * Sincronizza l'indicatore dei posti diponibili
     */
    private void syncDisponibili() {
        int disponibili = getPostiDisponibili();
        boolean roState = this.fieldDisponibili.isReadOnly();
        this.fieldDisponibili.setReadOnly(false);
        this.fieldDisponibili.setValue(disponibili);
        this.fieldDisponibili.setReadOnly(roState);

    }

    /**
     * Ritorna il numero di posti ancora disponibili per questa rappresentazione.
     * Per la prenotazione corrente, non considera i posti registrati sul db ma
     * considera quelli correntemente presenti nella scheda
     */
    private int getPostiDisponibili(){
        int disponibili = 0;
        Rappresentazione questaRapp = null;

        long idRapp = getLongValue(Prenotazione_.rappresentazione);
        questaRapp = Rappresentazione.read(idRapp);

        if (questaRapp != null) {

            // tutte le prenotazioni non congelate (compresa questa)
            disponibili = RappresentazioneModulo.getPostiDisponibili(questaRapp);

            // tolgo questa come risulta dal db
            Prenotazione prenDb = Prenotazione.read(getItemId());
            if(prenDb!=null){
                if (!prenDb.isCongelata()) {
                    disponibili += prenDb.getNumTotali();
                }
            }

            // aggiungo questa come risulta dalla scheda
            if (!fieldCongelata.getValue()) {
                disponibili -= fieldNumTotale.getValue();
            }

        }

        return disponibili;

    }




    @Override
    protected boolean save() {
        boolean saved;

        saved = super.save();

        if (saved) {

            // avviso posti esauriti
            int disponibili = getPostiDisponibili();
            if (disponibili < 0) {
                Notification.show("Attenzione", "\nPosti esauriti! ("+disponibili+")", Notification.Type.WARNING_MESSAGE);
            }

            // se si tratta di nuova prenotazione, eventualmente invia email di istruzioni
            if (isNewRecord()) {

                if (ModelliLettere.istruzioniPrenotazione.isSend(getPrenotazione())) {
                    Prenotazione pren = getPrenotazione();

                    // invia la mail di istruzioni in un thread separato
                    // (usa una lambda al posto del runnable)
                    new Thread(
                            () -> {

                                Notification notification1 = null;
                                String detail = pren.toStringNumDataInsegnante();

                                try {
                                    PrenotazioneModulo.doInvioIstruzioni(pren, getUsername());
                                    notification1 = new Notification("Inviata email di istruzioni", detail, Notification.Type.HUMANIZED_MESSAGE);
                                } catch (EmailFailedException e) {
                                    notification1 = new Notification("Invio email istruzioni fallito: " + e.getMessage(), detail, Notification.Type.ERROR_MESSAGE);
                                } catch (Exception e) {
                                    notification1 = new Notification("Errore durante l'invio della email di istruzioni: " + e.getMessage(), detail, Notification.Type.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                                notification1.setDelayMsec(-1);
                                notification1.show(Page.getCurrent());

                            }
                    ).start();

                }
            }


        }

        return saved;

    }

    /**
     * Checks if the current values are valid and ready to be persisted.
     * <p>
     *
     * @return a list of strings containing the reasons if not valid, empty list if valid.
     */
    protected ArrayList<String> isValid() {
        ArrayList<String> reasons = super.isValid();
        int numTot = Lib.getInt(fieldNumTotale.getValue());
        if (numTot <= 0) {
            reasons.add("Il numero totale di spettatori è zero.");
        }
        return reasons;
    }


    private Prenotazione getPrenotazione() {
        Prenotazione pren = null;
        BaseEntity entity = getBaseEntity();
        if (entity != null) {
            pren = (Prenotazione) entity;
        }
        return pren;
    }

    /**
     * Tenta di confermare la prenotazione
     */
    @SuppressWarnings("rawtypes")
    private void confermaPrenotazioneForm() {
        boolean cont = true;
        final Field fieldConfermata = getField(Prenotazione_.confermata);

        // controlla che non sia già confermata
        boolean confermata = (Boolean) fieldConfermata.getValue();
        if (confermata) {
            Notification.show("Questa prenotazione è già confermata.");
            cont = false;
        }

        // controlla che la prenotazione sia valida
        if (cont) {
            try {
                getBinder().commit();
            } catch (CommitException e) {
                Notification.show("Questa prenotazione non è valida.");
                cont = false;
            }
        }

        // presenta il dialogo di conferma
        if (cont) {
            ConfirmDialog dialog = new ConfirmDialog((dialog1, confirmed) -> {
                if (confirmed) {

                    // invia la mail di istruzioni in un thread separato
                    // (usa una lambda al posto del runnable)
                    new Thread(
                            () -> {

                                Prenotazione pren = getPrenotazione();
                                String detail = pren.toStringNumDataInsegnante();
                                Notification notif;
                                try {
                                    String user = EventoBootStrap.getUsername();

                                    // questo comando scrive i campi e salva la prenotazione
                                    // ed eventualmente invia la mail
                                    PrenotazioneModulo.doConfermaPrenotazione(pren, user);

                                    String inviata = "";
                                    if (ModelliLettere.confermaPrenotazione.isSend(pren)) {
                                        inviata = "Inviata e-mail di conferma";
                                    }
                                    notif = new Notification("Prenotazione confermata", inviata + " " + detail, Notification.Type.HUMANIZED_MESSAGE);
                                } catch (EmailFailedException e) {
                                    notif = new Notification("Invio email fallito: " + e.getMessage(), detail, Notification.Type.ERROR_MESSAGE);
                                }

                                notif.setDelayMsec(-1);
                                notif.show(Page.getCurrent());

                            }
                    ).start();

                    // chiude la finestra
                    Window w = getWindow();
                    if (w != null) {
                        w.close();
                        ;
                    }

                }
            });
            dialog.setTitle("Conferma prenotazione");
            dialog.setMessage("Vuoi confermare la prenotazione?");
            dialog.show(getUI());
        }

    }


    private static String getUsername() {
        return EventoBootStrap.getUsername();
    }

    // hello

}
