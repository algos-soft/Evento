package it.algos.evento.entities.rappresentazione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.Action;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.insegnante.InsegnanteForm;
import it.algos.evento.entities.insegnante.Insegnante_;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.PrenotazioneBaseTable;
import it.algos.evento.entities.prenotazione.PrenotazioneTablePortal;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.sala.Sala_;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.scuola.Scuola_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.multiazienda.ERelatedComboField;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.AFormLayout;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.ModuleTable;
import it.algos.webbase.web.toolbar.FormToolbar;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("serial")
public class RappresentazioneForm extends ModuleForm {

    private RelatedComboField fInsegnanti;
    private TableInsegnanti tableInsegnanti;


    public RappresentazioneForm(ModulePop module, Item item) {
        super(item, module);
        doInit();
    }

    private void doInit() {
        // se nuovo record mette sala e capienza di default
        if (isNewRecord()) {
            Rappresentazione rapp = getRappresentazione();
            if (rapp != null) {
                Sala sala = Sala.getDefault();
                rapp.setSala(sala);
                if (sala != null) {
                    rapp.setCapienza(sala.getCapienza());
                }
            }
        }
    }


    @Override
    public void createFields() {
        @SuppressWarnings("rawtypes")
        Field field;
        RelatedComboField rField;
        DateField dField;

        rField = new ERelatedComboField(Evento.class, "Evento");
        rField.sort(Evento_.sigla);
        addField(Rappresentazione_.evento, rField);

        // se nuovo record il popup mostra solo gli eventi della stagione corrente
        if (isNewRecord()) {
            Container.Filter filter = new Compare.Equal(Evento_.stagione.getName(), Stagione.getStagioneCorrente());
            rField.getFilterableContainer().addContainerFilter(filter);
        }

        rField = new ERelatedComboField(Sala.class, "Sala");
        rField.sort(Sala_.nome);
        addField(Rappresentazione_.sala, rField);
        rField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                syncCapienza();
            }
        });

        field = new IntegerField("Capienza");
        addField(Rappresentazione_.capienza, field);

        dField = new DateField("Data");
        dField.setResolution(Resolution.MINUTE);
        dField.setWidth("140px");
        addField(Rappresentazione_.dataRappresentazione, dField);

        field = new TextField("Note");
        addField(Rappresentazione_.note, field);

    }


    @Override
    protected FormToolbar createToolBar() {
        return new RappresentazioneFormToolbar(this);
    }

    protected Component createComponent() {
        TabSheet tabsheet = new TabSheet();
        tabsheet.setWidth("700px");
        tabsheet.addTab(creaTabGenerale(), "Generale");
        tabsheet.addTab(creaTabPrenotazioni(), "Prenotazioni");
        tabsheet.addTab(creaTabInsegnanti(), "Partecipanti");
        return tabsheet;

    }// end of method

    private Component creaTabGenerale() {
        FormLayout layout = new AFormLayout();
        layout.setMargin(true);

        layout.addComponent(getField(Rappresentazione_.dataRappresentazione));
        layout.addComponent(getField(Rappresentazione_.evento));
        layout.addComponent(getField(Rappresentazione_.sala));
        layout.addComponent(getField(Rappresentazione_.capienza));
        layout.addComponent(getField(Rappresentazione_.note));
        return layout;
    }


    private Component creaTabPrenotazioni() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        ATable tablePrenotazioni = new TablePrenotazioni(getRappresentazione(), getEntityManager());
        tablePrenotazioni.setPageLength(8);

        layout.addComponent(new Label("Elenco delle prenotazioni"));
        tablePrenotazioni.setWidth("100%");
        layout.addComponent(tablePrenotazioni);

        return layout;
    }


    private Component creaTabInsegnanti() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        HorizontalLayout panComandi = new HorizontalLayout();
        panComandi.setMargin(true);
        panComandi.setSpacing(true);
        fInsegnanti = new ERelatedComboField(Insegnante.class, "Partecipante: ");
        fInsegnanti.sort(Insegnante_.cognome, Insegnante_.nome);
        fInsegnanti.setNewItemHandler(InsegnanteForm.class, Insegnante_.cognome);

        Button bAggiungi = new Button("Aggiungi");
        bAggiungi.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object obj = fInsegnanti.getSelectedBean();
                if (obj != null) {
                    Insegnante insegnante = (Insegnante) obj;
                    List<Insegnante> listaInsegnanti = tableInsegnanti.getListaInsegnanti();
                    if (!containsId(listaInsegnanti, insegnante.getId())) {
                        BeanItemContainer<Insegnante> cont = tableInsegnanti.getBeanContainer();
                        cont.addItem(insegnante);
                    }
                }
            }
        });
        Button bRimuovi = new Button("Rimuovi selezionato");
        bRimuovi.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Object id = tableInsegnanti.getSelectedId(); // get the selected rows id
                if (id != null) {
                    BeanItemContainer<Insegnante> cont = tableInsegnanti.getBeanContainer();
                    cont.removeItem(id);
                }
            }
        });

        panComandi.addComponent(fInsegnanti);
        panComandi.setComponentAlignment(fInsegnanti, Alignment.MIDDLE_LEFT);
        panComandi.addComponent(bAggiungi);
        panComandi.setComponentAlignment(bAggiungi, Alignment.BOTTOM_LEFT);
        panComandi.addComponent(bRimuovi);
        panComandi.setComponentAlignment(bRimuovi, Alignment.BOTTOM_LEFT);

        tableInsegnanti = new TableInsegnanti(getEntityManager());
        tableInsegnanti.setPageLength(8);

        // panComandi.setWidth("100%");
        layout.addComponent(new Label("Elenco degli insegnanti che hanno partecipato"));
        layout.addComponent(panComandi);
        tableInsegnanti.setWidth("100%");
        layout.addComponent(tableInsegnanti);

        return layout;
    }

    /**
     * Check it a given id is contained in a list of Insegnanti
     */
    private static boolean containsId(List<Insegnante> listaInsegnanti, long id) {
        boolean found = false;
        for (BaseEntity entity : listaInsegnanti) {
            if (entity.getId() == id) {
                found = true;
                break;
            }
        }
        return found;
    }

    // tabella interna delle prenotazioni
    private class TablePrenotazioni extends PrenotazioneBaseTable {

        Rappresentazione rappresentazione;

        public TablePrenotazioni(Rappresentazione rapp, EntityManager em) {
            super(em);
            this.rappresentazione = rapp;
            init();
        }

        @Override
        public Container createContainer() {
            LazyEntityContainer<Prenotazione> cont = new LazyEntityContainer<Prenotazione>(getEntityManager(), Prenotazione.class, 100, BaseEntity_.id.getName(), true, true, true);
            cont.addContainerProperty(BaseEntity_.id.getName(), Long.class, 0L, true, true);
            cont.addContainerFilter(new Compare.Equal(Prenotazione_.rappresentazione.getName(), rappresentazione));
            return cont;
        }


        protected Object[] getDisplayColumns() {
            return new Object[]{
                    Prenotazione_.numPrenotazione,
                    Prenotazione_.dataPrenotazione,
                    Prenotazione_.scuola,
                    Prenotazione_.insegnante,
                    Prenotazione_.numTotali,
                    COL_STATUS,
                    COL_PAGAM,
            };


        }


//        /**
//         * Return the Actions to display in contextual menu
//         */
//        protected Action[] getActions(Object target, Object sender) {
//            Action[] actions = null;
//            actions = new Action[2];
//            actions[0] = actionEdit;
//            actions[1] = actionDelete;
//            return actions;
//        }

        private final Action actRegistraPagamento = new Action(PrenotazioneTablePortal.CMD_REGISTRA_PAGAMENTO,
                PrenotazioneTablePortal.ICON_REGISTRA_PAGAMENTO);
        private final Action actIstruzioni = new Action(PrenotazioneTablePortal.CMD_RIEPILOGO_OPZIONE,
                PrenotazioneTablePortal.ICON_RIEPILOGO_OPZIONE);
        private final Action actMemoInvioSchedaPren = new Action(
                PrenotazioneTablePortal.CMD_MEMO_INVIO_SCHEDA_PREN,
                PrenotazioneTablePortal.ICON_MEMO_INVIO_SCHEDA_PREN);
        private final Action actMemoScadPag = new Action(PrenotazioneTablePortal.CMD_MEMO_SCAD_PAGA,
                PrenotazioneTablePortal.ICON_MEMO_SCAD_PAGA);
        private final Action actAttestatoPartecipazione = new Action(PrenotazioneTablePortal.CMD_ATTESTATO_PARTECIPAZIONE,
                PrenotazioneTablePortal.ICON_ATTESTATO_PARTECIPAZIONE);
        private final Action actAvvisoCongOpz = new Action(PrenotazioneTablePortal.CMD_CONGELA_OPZIONE,
                PrenotazioneTablePortal.ICON_CONGELA_OPZIONE);
        private final Action actSpostaAdAltraData = new Action(PrenotazioneTablePortal.CMD_SPOSTA_AD_ALTRA_DATA,
                PrenotazioneTablePortal.ICON_SPOSTA_AD_ALTRA_DATA);

        /**
         * Return the Actions to display in contextual menu
         */
        protected Action[] getActions(Object target, Object sender) {
            ArrayList<Action> aActions = new ArrayList<>();
            aActions.add(actIstruzioni);
            aActions.add(actMemoInvioSchedaPren);
            aActions.add(actMemoScadPag);
            aActions.add(actAttestatoPartecipazione);
            aActions.add(actRegistraPagamento);
            aActions.add(actAvvisoCongOpz);
            aActions.add(actSpostaAdAltraData);
            return aActions.toArray(new Action[0]);
        }


        /**
         * Handle the contextual Actions
         */
        protected void handleAction(Action action, Object sender, Object target) {


            Item rowItem = getTable().getItem(target);
            if (rowItem != null) {
                Object value = rowItem.getItemProperty("id").getValue();

                long id = Lib.getLong(value);
                if (id > 0) {

                    if (action.equals(actRegistraPagamento)) {
                        registraPagamento();
                    }

                    if (action.equals(actIstruzioni)) {
                        inviaRiepilogoPrenotazione();
                    }

                    if (action.equals(actMemoInvioSchedaPren)) {
                        inviaMemoConfermaPren();
                    }

                    if (action.equals(actAvvisoCongOpz)) {
                        congelaPrenotazione();
                    }

                    if (action.equals(actSpostaAdAltraData)) {
                        spostaAdAltraData();
                    }

                    if (action.equals(actMemoScadPag)) {
                        inviaPromemoriaScadenzaPagamento();
                    }

                    if (action.equals(actAttestatoPartecipazione)) {
                        inviaAttestatoPartecipazione();
                    }


                }
            }
        }


    }


//    // tabella interna delle prenotazioni
//    private class TablePrenotazioni extends ATable {
//
//        Rappresentazione rappresentazione;
//
//        public TablePrenotazioni(Rappresentazione rapp, EntityManager em) {
//            super(Prenotazione.class, em);
//            this.rappresentazione=rapp;
//            init();
//        }
//
//        @Override
//        public Container createContainer() {
//            LazyEntityContainer<Prenotazione> cont = new LazyEntityContainer<Prenotazione>(getEntityManager(), Prenotazione.class, 100, BaseEntity_.id.getName(), true, true, true);
//            cont.addContainerProperty(BaseEntity_.id.getName(), Long.class, 0L, true, true);
//            cont.addContainerFilter(new Compare.Equal(Prenotazione_.rappresentazione.getName(), rappresentazione));
//            return cont;
//        }
//
//
//		@SuppressWarnings("rawtypes")
//		protected Object[] getDisplayColumns() {
//			ArrayList<Attribute> columns = new ArrayList();
//			columns.add(Prenotazione_.numPrenotazione);
//			columns.add(Prenotazione_.dataPrenotazione);
//			columns.add(Prenotazione_.scuola);
//			return columns.toArray(new Attribute[0]);
//		}
//
//
//		@Override
//		protected String formatPropertyValue(Object rowId, Object colId, Property property) {
//			String string = null;
//
//			if (colId.equals(Prenotazione_.scuola.getName())) {
//				Object value = property.getValue();
//				if (value != null && value instanceof Scuola) {
//					Scuola scuola = (Scuola) value;
//					string = scuola.getSigla();
//					Comune comune = scuola.getComune();
//					if (comune != null) {
//						string += " - " + comune.getNome();
//					}
//				} else {
//					string = "";
//				}
//				return string;
//			}
//
//			return super.formatPropertyValue(rowId, colId, property);
//		}
//
//
//	}


    // tabella interna degli insegnanti partecipanti
    private class TableInsegnanti extends ATable {

        public TableInsegnanti(EntityManager em) {
            super(Insegnante.class, em);
            init();
        }

        /**
         * Creates the container
         * <p>
         *
         * @return the container
         */
        public Container createContainer() {
            BeanItemContainer cont = new BeanItemContainer<Insegnante>(Insegnante.class);

            // fill the container with data
            List<Insegnante> lista = getRappresentazione().getInsegnanti();
            if (lista != null) {
                cont.removeAllItems();
                cont.addAll(lista);
            }
            return cont;
        }

        @SuppressWarnings("rawtypes")
        protected Object[] getDisplayColumns() {
            ArrayList<Attribute> columns = new ArrayList<Attribute>();
            columns.add(Insegnante_.cognome);
            columns.add(Insegnante_.nome);
            columns.add(Insegnante_.indirizzo2);
            columns.add(Insegnante_.materie);
            return columns.toArray(new Attribute[0]);
        }// end of method


        @SuppressWarnings({"rawtypes", "unchecked"})
        public BeanItemContainer<Insegnante> getBeanContainer() {
            return (BeanItemContainer) getContainerDataSource();
        }


        public ArrayList<Insegnante> getListaInsegnanti() {
            ArrayList<Insegnante> lista = new ArrayList<Insegnante>();
            BeanItemContainer<Insegnante> cont = getBeanContainer();
            Collection<?> ids = cont.getItemIds();
            for (Object id : ids) {
                BeanItem<Insegnante> bi = cont.getItem(id);
                lista.add(bi.getBean());
            }
            return lista;
        }

    }

    /**
     * Ritorna la Rappresentazione gestita da questa scheda
     */
    private Rappresentazione getRappresentazione() {
        Rappresentazione rapp = null;
        BaseEntity entity = getBaseEntity();
        if (entity != null) {
            rapp = (Rappresentazione) entity;
        }
        return rapp;
    }

    @SuppressWarnings("unchecked")
    private void syncCapienza() {
        RelatedComboField field = (RelatedComboField) getField(Rappresentazione_.sala);
        Sala sala = (Sala) field.getSelectedBean();
        int capienza = sala.getCapienza();
        getField(Rappresentazione_.capienza).setValue(capienza);
    }

    /**
     * Registra il contenuto della tabella Insegnanti nella relativa property
     * prima di registrare.
     */
    @SuppressWarnings({"unchecked"})
    @Override
    protected boolean save() {
        Property<ArrayList<?>> prop = getItem().getItemProperty(Rappresentazione_.insegnanti.getName());
        prop.setValue(tableInsegnanti.getListaInsegnanti());
        return super.save();
    }


}
