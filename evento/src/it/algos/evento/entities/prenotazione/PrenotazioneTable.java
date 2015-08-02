package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.ETable;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.webbase.web.converter.StringToBigDecimalConverter;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo.StatusChangeListener;
import it.algos.evento.entities.prenotazione.eventi.TipoEventoPren;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.scuola.Scuola;

import java.math.BigDecimal;
import java.util.Locale;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.Action;

@SuppressWarnings("serial")
public class PrenotazioneTable extends ETable {

    private static final StringToBigDecimalConverter bdConv = new StringToBigDecimalConverter(2);
    private static final StringToIntegerConverter intConv = new StringToIntegerConverter();
    private BigDecimal bd;
    private static final BigDecimal bdZero = new BigDecimal(0);
    public static final String PROP_PROGETTO = Rappresentazione.class.getSimpleName().toLowerCase() + "." + Rappresentazione_.evento.getName() + "." + Evento_.progetto.getName();
    public static final String PROP_EVENTO = Rappresentazione.class.getSimpleName().toLowerCase() + "." + Rappresentazione_.evento.getName();
    public static final String PROP_STAGIONE = Rappresentazione.class.getSimpleName().toLowerCase() + "." + Rappresentazione_.evento.getName() + "." + Evento_.stagione.getName();

    // id della colonna generata "stato"
    private static final String COL_STATUS="stato";

    // id della colonna generata "pagamento"
    private static final String COL_PAGAM="pagato";

    // id della colonna generata "referente"
    private static final String COL_PRIVATO ="tipo";



    /**
     * Creates the container
     * <p>
     *
     * @return un container RW filtrato sulla azienda corrente
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Container createContainer() {
        // aggiunge un filtro sulla stagione corrente
        Container cont = super.createContainer();
        JPAContainer JPAcont = (JPAContainer) cont;
        Filter filter = new Compare.Equal(PrenotazioneTable.PROP_STAGIONE, Stagione.getStagioneCorrente());
        JPAcont.addContainerFilter(filter);
        return JPAcont;
    }// end of method


    public PrenotazioneTable(ModulePop modulo) {
        super(modulo);

        setColumnHeader(Prenotazione_.numPrenotazione, "N.");
        setColumnHeader(Prenotazione_.dataPrenotazione, "data");
//        setColumnHeader(Prenotazione_.privato, "priv");
        setColumnHeader(Prenotazione_.insegnante, "referente");
        setColumnHeader(Prenotazione_.numTotali, "pers");
        setColumnHeader(Prenotazione_.importoDaPagare, "importo");
        setColumnHeader(Prenotazione_.scadenzaConferma, "conf. entro");
//        setColumnHeader(Prenotazione_.livelloSollecitoConferma, "SC");
//        setColumnHeader(Prenotazione_.congelata, "cong");
//        setColumnHeader(Prenotazione_.confermata, "conf");
        setColumnHeader(Prenotazione_.scadenzaPagamento, "pagam. entro");
//        setColumnHeader(Prenotazione_.livelloSollecitoPagamento, "SP");
//        setColumnHeader(Prenotazione_.pagamentoConfermato, "pag conf.");
//        setColumnHeader(Prenotazione_.pagamentoRicevuto, "pag ricev.");
        setColumnHeader(Prenotazione_.modoPagamento, "modo pag");
        setColumnHeader(Prenotazione_.tipoRicevuta, "tipo ricev");


        setColumnWidth(Prenotazione_.dataPrenotazione, 80);
//		setColumnWidth(Prenotazione_.rappresentazione, 220);
//		setColumnWidth(Prenotazione_.scuola, 110);
//		setColumnWidth(Prenotazione_.insegnante, 110);
//        setColumnWidth(Prenotazione_.privato, 40);
        setColumnWidth(Prenotazione_.numTotali, 46);
        setColumnWidth(Prenotazione_.importoDaPagare, 65);
//        setColumnWidth(Prenotazione_.livelloSollecitoConferma, 30);
//        setColumnWidth(Prenotazione_.congelata, 50);
//        setColumnWidth(Prenotazione_.confermata, 50);
        setColumnWidth(Prenotazione_.scadenzaConferma, 80);
//        setColumnWidth(Prenotazione_.pagamentoConfermato, 50);
        setColumnWidth(Prenotazione_.scadenzaPagamento, 80);
//        setColumnWidth(Prenotazione_.livelloSollecitoPagamento, 30);
//        setColumnWidth(Prenotazione_.pagamentoRicevuto, 50);
        setColumnWidth(Prenotazione_.modoPagamento, 70);
        setColumnWidth(Prenotazione_.tipoRicevuta, 70);

        setColumnAlignment(Prenotazione_.dataPrenotazione, Align.CENTER);
        setColumnAlignment(Prenotazione_.scadenzaConferma, Align.CENTER);
//        setColumnAlignment(Prenotazione_.livelloSollecitoConferma, Align.CENTER);
//        setColumnAlignment(Prenotazione_.livelloSollecitoConferma, Align.CENTER);
        setColumnAlignment(Prenotazione_.scadenzaPagamento, Align.CENTER);
//        setColumnAlignment(Prenotazione_.livelloSollecitoPagamento, Align.CENTER);
        setColumnAlignment(COL_STATUS, Align.CENTER);
        setColumnAlignment(COL_PAGAM, Align.CENTER);
        setColumnAlignment(COL_PRIVATO, Align.CENTER);

        setColumnUseTotals(Prenotazione_.numTotali, true);
        setColumnUseTotals(Prenotazione_.importoDaPagare, true);

        // comandi contestuali aggiuntivi per invio email varie
        addActionHandler(new Action.Handler() {

            private final Action actIstruzioni = new Action(PrenotazioneTablePortal.CMD_RIEPILOGO_OPZIONE,
                    PrenotazioneTablePortal.ICON_RIEPILOGO_OPZIONE);
            private final Action actRegistraPagamento = new Action(PrenotazioneTablePortal.CMD_REGISTRA_PAGAMENTO,
                    PrenotazioneTablePortal.ICON_REGISTRA_PAGAMENTO);
            private final Action actMemoScadPag = new Action(PrenotazioneTablePortal.CMD_MEMO_SCAD_PAGA,
                    PrenotazioneTablePortal.ICON_MEMO_SCAD_PAGA);
            private final Action actMemoInvioSchedaPren = new Action(
                    PrenotazioneTablePortal.CMD_MEMO_INVIO_SCHEDA_PREN,
                    PrenotazioneTablePortal.ICON_MEMO_INVIO_SCHEDA_PREN);
            private final Action actAvvisoCongOpz = new Action(PrenotazioneTablePortal.CMD_CONGELA_OPZIONE,
                    PrenotazioneTablePortal.ICON_CONGELA_OPZIONE);
            private final Action actAttestatoPartecipazione = new Action(PrenotazioneTablePortal.CMD_ATTESTATO_PARTECIPAZIONE,
                    PrenotazioneTablePortal.ICON_ATTESTATO_PARTECIPAZIONE);


            public Action[] getActions(Object target, Object sender) {
                Action[] actions = null;
                actions = new Action[6];
                actions[0] = actIstruzioni;
                actions[1] = actRegistraPagamento;
                actions[2] = actMemoInvioSchedaPren;
                actions[3] = actAvvisoCongOpz;
                actions[4] = actMemoScadPag;
                actions[5] = actAttestatoPartecipazione;

                return actions;
            }

            public void handleAction(Action action, Object sender, Object target) {
                Item rowItem = getTable().getItem(target);
                if (rowItem != null) {
                    Object value = rowItem.getItemProperty("id").getValue();
                    long id = Lib.getLong(value);
                    if (id > 0) {

                        if (action.equals(actRegistraPagamento)) {
                            registraPagamento(id, getTable());
                        }

                        if (action.equals(actIstruzioni)) {
                            PrenotazioneModulo.cmdInviaRiepilogoOpzione(id, getTable());
                        }

                        if (action.equals(actMemoInvioSchedaPren)) {
                            PrenotazioneModulo.cmdPromemoriaInvioSchedaPrenotazione(id, getTable());
                        }

                        // if (action.equals(actConfPren)) {
                        // PrenotazioneModulo.cmdSendEmailConfermaPrenotazione(id);
                        // }

                        // if (action.equals(actConfRegisPag)) {
                        // PrenotazioneModulo.cmdSendEmailConfermaRegistrazionePagamento(id);
                        // }

                        if (action.equals(actAvvisoCongOpz)) {
                            PrenotazioneModulo.cmdCongelamentoOpzione(id);
                        }

                        if (action.equals(actMemoScadPag)) {
                            PrenotazioneModulo.cmdPromemoriaScadenzaPagamento(id, getTable());
                        }

                        if (action.equals(actAttestatoPartecipazione)) {
                            PrenotazioneModulo.cmdAttestatoPartecipazione(id);
                        }


                    }
                }

            }
        });

        PrenotazioneModulo.addStatusChangeListener(new StatusChangeListener() {

            @Override
            public void statusChanged(TipoEventoPren tipoEvento) {
                refreshRowCache();
            }
        });


        // colonne collapsed di default
        setColumnCollapsingAllowed(true);
        setColumnCollapsed(Prenotazione_.scadenzaConferma.getName(), true);
        setColumnCollapsed(Prenotazione_.scadenzaPagamento.getName(), true);
        setColumnCollapsed(Prenotazione_.modoPagamento.getName(), true);
        setColumnCollapsed(Prenotazione_.tipoRicevuta.getName(), true);
        setColumnCollapsed(Prenotazione_.numInteri.getName(), true);
        setColumnCollapsed(Prenotazione_.numRidotti.getName(), true);
        setColumnCollapsed(Prenotazione_.numDisabili.getName(), true);
        setColumnCollapsed(Prenotazione_.numAccomp.getName(), true);

    }// end of constructor


    /**
     * Initial sort order for the JPA container
     * <p>
     *
     * @param cont the container to be sorted
     */
    protected void sortJPAContainer(JPAContainer cont) {
        String sortField = Prenotazione_.numPrenotazione.getName();
        cont.sort(new String[]{sortField}, new boolean[]{true});
    }// end of method


    public static void registraPagamento(Object id, ATable table) {

        boolean cont = true;
        Prenotazione pren = null;

        // controllo che sia confermata
        if (cont) {

            pren = Prenotazione.read(id);

            if (!pren.isConfermata()) {
                cont = false;
                Notification.show("Questa prenotazione non è confermata.");
            }
        }

        // controlla che non sia già pagamento confermato e ricevuto
        if (cont) {
            if (pren.isPagamentoConfermato() && pren.isPagamentoRicevuto()) {
                cont = false;
                Notification.show("Il pagamento è già stato ricevuto.");
            }
        }

        if (cont) {
            PrenotazioneModulo.cmdRegistraPagamento(pren, table);
        }

    }

    private PrenotazioneTable getTable() {
        return this;
    }


    @Override
    protected void createAdditionalColumns() {
        // queste property aggiunte servono per consentire
        // di effettuare ricerche su proprietà in relazione
        getJPAContainer().addNestedContainerProperty(PROP_PROGETTO);
        getJPAContainer().addNestedContainerProperty(PROP_EVENTO);
        getJPAContainer().addNestedContainerProperty(PROP_STAGIONE);

        addGeneratedColumn(COL_STATUS, new StatusColumnGenerator());
        addGeneratedColumn(COL_PAGAM, new PagamColumnGenerator());
        addGeneratedColumn(COL_PRIVATO, new PrivatoColumnGenerator());

    }


    protected Object[] getDisplayColumns() {
        return new Object[]{Prenotazione_.numPrenotazione,
                Prenotazione_.dataPrenotazione,
                Prenotazione_.rappresentazione,
                Prenotazione_.scuola,
                COL_PRIVATO,
                Prenotazione_.insegnante,
                Prenotazione_.numTotali,
                Prenotazione_.importoDaPagare,
                COL_STATUS,
                Prenotazione_.scadenzaConferma,
                COL_PAGAM,
                Prenotazione_.scadenzaPagamento,
                Prenotazione_.modoPagamento,
                Prenotazione_.tipoRicevuta,
                Prenotazione_.numInteri,
                Prenotazione_.numRidotti,
                Prenotazione_.numDisabili,
                Prenotazione_.numAccomp
        };


    }// end of method

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
        String string = null;

        if (colId.equals(Prenotazione_.scuola.getName())) {
            Object value = property.getValue();
            if (value != null && value instanceof Scuola) {
                Scuola scuola = (Scuola) value;
                string = scuola.getSigla();
                Comune comune = scuola.getComune();
                if (comune != null) {
                    string += " - " + comune.getNome();
                }
            } else {
                string = "";
            }
            return string;
        }

        if (colId.equals(Prenotazione_.insegnante.getName())) {
            Object value = property.getValue();
            if (value != null && value instanceof Insegnante) {
                Insegnante ins = (Insegnante) value;
                string = ins.getCognome() + " " + ins.getNome();
            } else {
                string = "";
            }
            return string;
        }


        if (colId.equals(Prenotazione_.importoDaPagare.getName())) {
            string = "";
            bd = Lib.getBigDecimal(property.getValue());
            if (!bd.equals(bdZero)) {
                string = bdConv.convertToPresentation(bd);
            }
            return string;
        }

        if (colId.equals(Prenotazione_.livelloSollecitoConferma.getName())) {
            string = "";
            int integer = Lib.getInt(property.getValue());
            if (integer != 0) {
                string = intConv.convertToPresentation(integer, String.class, Locale.getDefault());
            }
            return string;
        }

        if (colId.equals(Prenotazione_.livelloSollecitoPagamento.getName())) {
            string = "";
            int integer = Lib.getInt(property.getValue());
            if (integer != 0) {
                string = intConv.convertToPresentation(integer, String.class, Locale.getDefault());
            }
            return string;
        }

        if (colId.equals(Prenotazione_.modoPagamento.getName())) {
            Object value = property.getValue();
            if (value != null && value instanceof ModoPagamento) {
                ModoPagamento tipo = (ModoPagamento) value;
                string = tipo.getSigla();
            } else {
                string = "";
            }
            return string;
        }

        if (colId.equals(Prenotazione_.tipoRicevuta.getName())) {
            Object value = property.getValue();
            if (value != null && value instanceof TipoRicevuta) {
                TipoRicevuta tipo = (TipoRicevuta) value;
                string = tipo.getSigla();
            } else {
                string = "";
            }
            return string;
        }


        return super.formatPropertyValue(rowId, colId, property);
    }// end of method

    /**
     * Genera la colonna di stato.
     */
    class StatusColumnGenerator implements ColumnGenerator {

        @SuppressWarnings("unchecked")
        public Component generateCell(Table source, Object itemId, Object columnId) {
            boolean conf = Lib.getBool(getContainerProperty(itemId, Prenotazione_.confermata.getName()).getValue());
            boolean cong = Lib.getBool(getContainerProperty(itemId, Prenotazione_.congelata.getName()).getValue());
            int levelSC = Lib.getInt(getContainerProperty(itemId, Prenotazione_.livelloSollecitoConferma.getName()).getValue());
            String imgName;
            String description="";
            if (conf) {
                imgName = "confirmed_20px.png";
                description="La prenotazione è confermata";
            } else {
                if (cong) {
                    imgName = "frozen_20px.png";
                    description="La prenotazione è stata congelata";
                } else {
                    switch (levelSC) {
                        case 1:
                            imgName = "bell_num_1_20px.png";
                            description="E' stato inviato un sollecito di conferma prenotazione";
                            break;
                        case 2:
                            imgName = "bell_num_2_20px.png";
                            description="Sono stati inviati due solleciti di conferma prenotazione";
                            break;
                        default:
                            imgName = null;
                            break;
                    }
                }
            }
            Image img = null;
            if (imgName != null) {
                img = new Image(null, LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME,imgName));
                img.setDescription(description);
            }

            return img;
        }
    }

    /**
     * Genera la colonna del pagamento.
     */
    class PagamColumnGenerator implements ColumnGenerator {

        @SuppressWarnings("unchecked")
        public Component generateCell(Table source, Object itemId, Object columnId) {
            boolean conf = Lib.getBool(getContainerProperty(itemId, Prenotazione_.pagamentoConfermato.getName()).getValue());
            boolean regis = Lib.getBool(getContainerProperty(itemId, Prenotazione_.pagamentoRicevuto.getName()).getValue());
            int levelSC = Lib.getInt(getContainerProperty(itemId, Prenotazione_.livelloSollecitoPagamento.getName()).getValue());
            String imgName;
            String description="";
            if (regis) {
                imgName = "confirmed_20px.png";
                description="Il pagamento è stato confermato e registrato";
            } else {
                if (conf) {
                    imgName = "confirmed_half_20px.png";
                    description="Il pagamento è stato confermato dal referente ma non ancora registrato";
                } else {
                    switch (levelSC) {
                        case 1:
                            imgName = "bell_num_1_20px.png";
                            description="E' stato inviato un sollecito di conferma pagamento";
                            break;
                        case 2:
                            imgName = "bell_num_2_20px.png";
                            description="Sono stati inviati due solleciti di conferma pagamento";
                            break;
                        default:
                            imgName = null;
                            break;
                    }
                }
            }
            Image img = null;
            if (imgName != null) {
                img = new Image(null, LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME,imgName));
                img.setDescription(description);
            }

            return img;
        }
    }


    /**
     * Genera la colonna del Referente.
     */
    private class PrivatoColumnGenerator implements ColumnGenerator {
        @Override
        public Object generateCell(Table source, Object itemId, Object columnId) {
            boolean priv = Lib.getBool(getContainerProperty(itemId, Prenotazione_.privato.getName()).getValue());
            String loc_img_name="";
            String description="";
            if(priv){
                loc_img_name="person_20px.png";
                description="Il referente è un privato";
            }else{
                loc_img_name="teacher_20px.png";
                description="Il referente è un docente";
            }
            Image img = new Image(null, LibResource.getImgResource(EventoApp.IMG_FOLDER_NAME,loc_img_name));
            img.setDescription(description);
            return img;
        }
    }

}
