package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.*;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.eventi.EventoPren;
import it.algos.evento.entities.prenotazione.eventi.TipoEventoPren;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.converter.StringToBigDecimalConverter;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class PrenotazioneModulo extends EModulePop {

    private static ArrayList<StatusChangeListener> statusChangeListeners = new ArrayList<StatusChangeListener>();
    private final static Logger logger = Logger.getLogger(PrenotazioneModulo.class.getName());

    public static void addStatusChangeListener(StatusChangeListener l) {
        statusChangeListeners.add(l);
    }

    public interface StatusChangeListener {
        public void statusChanged(TipoEventoPren tipoEvento);
    }

    private static void fireStatusChanged(TipoEventoPren tipoEvento) {
        for (StatusChangeListener l : statusChangeListeners) {
            l.statusChanged(tipoEvento);
        }
    }

    public PrenotazioneModulo() {
        super(Prenotazione.class);
    }// end of constructor

    public TablePortal createTablePortal() {
        return new PrenotazioneTablePortal(this);
    }// end of method

    @Override
    public ATable createTable() {
        return (new PrenotazioneTable(this));
    }// end of method

    @Override
    public AForm createForm(Item item) {
        return (new PrenotazioneForm(this, item));
    }// end of method

    @Override
    public SearchManager createSearchManager() {
        return new PrenotazioneSearch();
    }// end of method

    /**
     * Post create / pre edit item
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void postCreate(Item item) {
        Property prop = item.getItemProperty(Prenotazione_.numPrenotazione.getName());
        int nextnum = CompanyPrefs.nextNumPren.getInt();
        prop.setValue(nextnum);
        CompanyPrefs.nextNumPren.put(nextnum + 1);
    }// end of method


    /**
     * Invio email riepilogo opzione e istruzioni
     * <p>
     * Invocato dai menu
     */
    public static void cmdInviaRiepilogoOpzione(Object id, ATable table) {
        Prenotazione pren = Prenotazione.read(id);
        ConfirmDialog dialog = new DialogoInviaRiepilogoOpzione(pren, table);
        dialog.show(UI.getCurrent());
    }

    /**
     * Invio email promemoria scadenza pagamento (da rimuovere?)
     * <p>
     * Invocato dai menu
     */
    public static void cmdPromemoriaScadenzaPagamento(Object id, ATable table) {
        boolean cont = true;
        Prenotazione pren = null;

        // controllo che il pagamento non sia già confermato
        if (cont) {
            pren = Prenotazione.read(id);
            if (pren.isPagamentoConfermato()) {
                cont = false;
                Notification.show("Questo pagamento è già confermato.");
            }
        }

        // controllo che la prenotazione sia confermata
        if (cont) {
            if (!pren.isConfermata()) {
                cont = false;
                Notification.show("Questa prenotazione non è ancora confermata.");
            }
        }

        // controllo che il livello di sollecito sia < 1
        if (cont) {
            if (pren.getLivelloSollecitoPagamento() > 0) {
                cont = false;
                Notification notification = new Notification("Il promemoria è gia stato inviato",
                        "\nSe vuoi puoi reinviarlo dagli Eventi Prenotazione.", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            }
        }

        // presento il dialogo di conferma (se confermato manda mail)
        if (cont) {
            ConfirmDialog dialog = new DialogoPromemoriaScadenzaPagamento(pren, table);
            dialog.show(UI.getCurrent());
        }

    }

    /**
     * Dialogo conferma invio promemoria scadenza pagamento (da rimuovere?)
     */
    private static class DialogoPromemoriaScadenzaPagamento extends ConfirmDialog {
        private Prenotazione pren;
        private ATable table;

        public DialogoPromemoriaScadenzaPagamento(Prenotazione pren, ATable table) {
            super(null);
            this.pren = pren;
            this.table = table;
            setTitle("Invio promemoria scadenza pagamento");
            setMessage("Vuoi inviare un promemoria scadenza pagamento?");
            setConfirmButtonText("Invia");
        }

        @Override
        protected void onConfirm() {
            try {
                PrenotazioneModulo.doPromemoriaScadenzaPagamento(pren, getUsername());
                Notification notification = new Notification("Promemoria inviato", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            } catch (EmailFailedException e) {
                notifyEmailFailed(e);
            }
            table.refreshRowCache();// per il livello di sollecito che può essere incrementato
            super.onConfirm();
        }

    }

    /**
     * Invio email promemoria invio scheda prenotazione
     * <p>
     * Invocato dai menu
     */
    public static void cmdPromemoriaInvioSchedaPrenotazione(Object id, ATable table) {
        boolean cont = true;

        Prenotazione pren = Prenotazione.read(id);

        // controllo che la prenotazione non sia già confermata
        if (cont) {
            if (pren.isConfermata()) {
                cont = false;
                Notification.show("Questa prenotazione è già confermata.");
            }
        }

        // controllo che il livello sollecito sia < 1
        if (cont) {
            if (pren.getLivelloSollecitoConferma() > 0) {
                cont = false;
                Notification notification = new Notification("Il promemoria è gia stato inviato",
                        "\nSe vuoi puoi reinviarlo dagli Eventi Prenotazione.", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            }
        }

        if (cont) {
            ConfirmDialog dialog = new DialogoPromemoriaInvioSchedaPrenotazione(pren, table);
            dialog.show(UI.getCurrent());
        }

    }

    /**
     * Dialogo conferma invio riepilogo opzione
     */
    private static class DialogoInviaRiepilogoOpzione extends ConfirmDialog {
        private Prenotazione pren;

        public DialogoInviaRiepilogoOpzione(Prenotazione pren, ATable table) {
            super(null);
            this.pren = pren;
            setTitle("Invio riepilogo opzione con istruzioni");
            setMessage("Vuoi inviare il riepilogo opzione?");
            setConfirmButtonText("Invia");
        }

        @Override
        protected void onConfirm() {
            try {
                doInvioIstruzioni(pren, getUsername());
                Notification notification = new Notification("Riepilogo inviato", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            } catch (EmailFailedException e) {
                notifyEmailFailed(e);
            }
            super.onConfirm();
        }
    }


    /**
     * Dialogo conferma invio mail di "promemoria invio scheda prenotazione"
     */
    private static class DialogoPromemoriaInvioSchedaPrenotazione extends ConfirmDialog {
        private Prenotazione pren;
        private ATable table;

        public DialogoPromemoriaInvioSchedaPrenotazione(Prenotazione pren, ATable table) {
            super(null);
            this.pren = pren;
            this.table = table;
            setTitle("Invio promemoria scheda prenotazione");
            setMessage("Vuoi inviare il promemoria di invio scheda prenotazione?");
            setConfirmButtonText("Invia");
        }

        @Override
        protected void onConfirm() {
            try {
                doPromemoriaInvioSchedaPrenotazione(pren, getUsername());
                Notification notification = new Notification("Promemoria inviato", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            } catch (EmailFailedException e) {
                notifyEmailFailed(e);
            }
            table.refreshRowCache();// per il numero di solleciti che è incrementato
            super.onConfirm();
        }
    }

    /**
     * Invio email "avviso congelamento opzione"
     * <p>
     * Invocato dalla UI
     */
    public static void cmdCongelamentoOpzione(Object id) {
        boolean cont = true;

        Prenotazione pren = Prenotazione.read(id);

        // controllo che la prenotazione non sia già confermata
        if (cont) {
            if (pren.isConfermata()) {
                cont = false;
                Notification.show("Questa prenotazione è già confermata.");
            }
        }

        // controllo che la prenotazione non sia congelata
        if (cont) {
            if (pren.isCongelata()) {
                cont = false;
                Notification notification = new Notification("Questa prenotazione è già congelata", "\nSe vuoi puoi reinviare la email dagli Eventi Prenotazione.", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            }
        }

        // mostra il dialogo
        if (cont) {
            ConfirmDialog dialog = new DialogoCongelamentoOpzione(pren);
            dialog.show(UI.getCurrent());
        }
    }

    /**
     * Dialogo conferma invio avviso congelamento opzione (da rimuovere?)
     */
    private static class DialogoCongelamentoOpzione extends ConfirmDialog {
        private Prenotazione pren;

        public DialogoCongelamentoOpzione(Prenotazione pren) {
            super(null);
            this.pren = pren;
            setTitle("Congelamento opzione");
            String msg="Il congelamento di una opzione libera i posti impegnati e blocca " +
                    "l'invio di ulteriori soleciti.<br>Una email di avviso viene inviata " +
                    "al referente.<br>";
            setMessage(msg);
            setConfirmButtonText("Congela");
        }

        @Override
        protected void onConfirm() {
            new Thread(
                    () -> {
                        try {
                            boolean emailInviata = doCongelamentoOpzione(pren, getUsername());
                            String strEmail = "";
                            if (emailInviata) {
                                strEmail = "Email inviata";
                            }
                            Notification notification = new Notification("Opzione congelata", strEmail, Notification.Type.HUMANIZED_MESSAGE);
                            notification.setDelayMsec(-1);
                            notification.show(Page.getCurrent());
                        } catch (EmailFailedException e) {
                            notifyEmailFailed(e);
                        }

                    }
            ).start();

            super.onConfirm();
        }
    }

    /**
     * Effettua la registrazione di un pagamento
     * <p>
     * Invocato dai menu
     */
    public static void cmdRegistraPagamento(final Prenotazione pren, final ATable table) {
        new DialogoRegistraPagamentoPren(pren, table).show(UI.getCurrent());
    }

    /**
     * Dialogo registrazione pagamento prenotazione
     */
    private static class DialogoRegistraPagamentoPren extends DialogoRegistraPagamento {
        private ATable table;

        public DialogoRegistraPagamentoPren(Prenotazione pren, ATable table) {
            super(pren, null);
            this.table = table;
        }

        protected void dialogoConfermato() {

            int numInteri = getNumInteri();
            int numRidotti = getNumRidotti();
            int numDisabili = getNumDisabili();
            int numAccomp = getNumAccomp();
            BigDecimal importoPrevisto = getImportoPrevisto();
            BigDecimal importoPagato = getImportoPagato();
            ModoPagamento mezzo = getModoPagamento();
            boolean confermato = isConfermato();
            boolean ricevuto = isRicevuto();
            String user = getUsername();

            // esegue l'operazione di conferma e l'invio mail in un thread separato
            // al termine dell'operazione viene visualizzata una notifica
            new Thread(
                    () -> {
                        try {

                            boolean mailInviata = doConfermaRegistrazionePagamento(getPren(), numInteri, numRidotti, numDisabili, numAccomp,
                                    importoPrevisto, importoPagato, mezzo, confermato, ricevuto, user);

                            String strEmail = "";
                            if (mailInviata) {
                                strEmail = "Email inviata";
                            }
                            Notification notification = new Notification("Pagamento registrato", strEmail, Notification.Type.HUMANIZED_MESSAGE);
                            notification.setDelayMsec(-1);
                            notification.show(Page.getCurrent());
                        } catch (EmailFailedException e) {
                            notifyEmailFailed(e);
                        }
                        table.refreshRowCache();
                    }

            ).start();

        }

    }


    /**
     * Invia l'attestato di partecipazione
     * <p>
     * Invocato dai menu
     */
    public static void cmdAttestatoPartecipazione(Object id) {
        boolean cont = true;

        Prenotazione pren = Prenotazione.read(id);

        // controllo che la prenotazione sia confermata
        if (cont) {
            if (!pren.isConfermata()) {
                cont = false;
                Notification notification = new Notification("Questa prenotazione non è confermata", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            }
        }

        // controllo che il pagamento sia stato effettivamente ricevuto
        if (cont) {
            if (!pren.isPagamentoRicevuto()) {
                cont = false;
                Notification notification = new Notification("Il pagamento non è ancora stato ricevuto", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            }
        }

        // controllo che la prenotazione non sia congelata
        if (cont) {
            if (pren.isCongelata()) {
                cont = false;
                Notification notification = new Notification("Questa prenotazione è già congelata", "\nSe vuoi puoi reinviare la email dagli Eventi Prenotazione.", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            }
        }


        if (cont) {
            new DialogoAttestatoPartecipazione(pren).show(UI.getCurrent());
        }
    }

    /**
     * Dialogo attestato di partecipazione
     */
    private static class DialogoAttestatoPartecipazione extends ConfirmDialog {
        private Prenotazione pren;

        public DialogoAttestatoPartecipazione(Prenotazione pren) {
            super(null);
            this.pren = pren;
            setTitle("Attestato di partecipazione");
            setMessage("Vuoi inviare l'attestato di partecipazione?");
            setConfirmButtonText("Invia");
        }

        @Override
        protected void onConfirm() {
            try {
                doAttestatoPartecipazione(pren, getUsername());
                Notification notification = new Notification("Attestato inviato", Notification.Type.HUMANIZED_MESSAGE);
                notification.setDelayMsec(-1);
                notification.show(Page.getCurrent());
            } catch (EmailFailedException e) {
                notifyEmailFailed(e);
            }
            super.onConfirm();
        }


    }


    /**
     * Invio email istruzioni (no UI)
     */
    public static void doInvioIstruzioni(Prenotazione pren, String user) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.invioIstruzioni;
        sendEmailEvento(pren, tipoEvento, user);
        fireStatusChanged(tipoEvento);
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
    }

    /**
     * Esecuzione conferma prenotazione (no UI)
     */
    public static void doConfermaPrenotazione(Prenotazione pren, String user) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.confermaPrenotazione;
        pren.setConfermata(true);
        pren.setCongelata(false);
        pren.setDataConferma(LibDate.today());
        pren.save();

        PrenotazioneModulo.creaEvento(pren, tipoEvento, "", getUsername());

        fireStatusChanged(tipoEvento);
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
        if (ModelliLettere.confermaPrenotazione.isSend(pren)) {
            sendEmailEvento(pren, tipoEvento, user);
        }
    }

    /**
     * Invio promemoria invio scheda prenotazione (no UI)
     * @param pren la prenotazione
     * @param user l'utente che genera l'evento
     */
    public static void doPromemoriaInvioSchedaPrenotazione(Prenotazione pren, String user) throws EmailFailedException {
        boolean mailInviata=false;
        TipoEventoPren tipoEvento = TipoEventoPren.promemoriaInvioSchedaPrenotazione;

        // pone il livello di sollecito a 1 e prolunga la scadenza a X giorni da oggi
        if (pren.getLivelloSollecitoConferma() < 1) {
            pren.setLivelloSollecitoConferma(1);
            int numDays = CompanyPrefs.ggProlungamentoConfDopoSollecito.getInt(pren.getCompany());
            Date date = new DateTime(LibDate.today()).plusDays(numDays).toDate();
            pren.setScadenzaConferma(date);
            pren.save();
            fireStatusChanged(tipoEvento);
            logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
            sendEmailEvento(pren, tipoEvento, user);
        }

    }

    /**
     * Congelamento opzione (no UI)
     * <p>
     *
     * @return true se ha inviato l'email
     */
    public static boolean doCongelamentoOpzione(Prenotazione pren, String user) throws EmailFailedException {
        boolean emailInviata = false;
        TipoEventoPren tipoEvento = TipoEventoPren.congelamentoOpzione;

        // attiva il flag congelata, toglie la eventuale conferma, logga l'operazione
        pren.setCongelata(true);
        pren.setConfermata(false);
        pren.save();
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);

        // invia la mail se previsto, e incrementa il livello di sollecito
        if(ModelliLettere.congelamentoOpzione.isSend(pren)){
            int level = pren.getLivelloSollecitoConferma();
            pren.setLivelloSollecitoConferma(level + 1);
            pren.save();
            fireStatusChanged(tipoEvento);
            sendEmailEvento(pren, tipoEvento, user);
            emailInviata = true;
        }

        fireStatusChanged(tipoEvento);

        return emailInviata;

    }

    /**
     * Controlli scadenza pagamento (no UI)
     */
    public static void doPromemoriaScadenzaPagamento(Prenotazione pren, String user) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.promemoriaScadenzaPagamento;

        // controlla se la prenotazione è soggetta a invio mail
        if (ModelliLettere.memoScadPagamento.isSend(pren)){

            // invia la mail, pone il livello di sollecito a 1
            // e prolunga la scadenza a X giorni da oggi
            if (pren.getLivelloSollecitoPagamento() < 1) {
                pren.setLivelloSollecitoPagamento(1);
                int numDays = CompanyPrefs.ggProlungamentoPagamDopoSollecito.getInt(pren.getCompany());
                Date date = new DateTime(LibDate.today()).plusDays(numDays).toDate();
                pren.setScadenzaPagamento(date);
                pren.save();
                fireStatusChanged(tipoEvento);
                logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
                sendEmailEvento(pren, tipoEvento, user);
            }

        }

    }

    /**
     * Conferma registrazione pagamento (no UI)
     * <p>
     *
     * @return true se ha inviato una o più mail
     */
    public static boolean doConfermaRegistrazionePagamento(Prenotazione pren, int numInteri, int numRidotti,
                                                           int numDisabili, int numAccomp, BigDecimal importoPrevisto, BigDecimal importoPagato, ModoPagamento mezzo,
                                                           boolean checkConfermato, boolean checkRicevuto, String user) throws EmailFailedException {

        boolean mailInviata = false;

        // indicatori di passaggio di stato da off a on
        boolean accesoConfermato = false;
        boolean accesoRicevuto = false;

        // modifica i valori nella prenotazione
        pren.setNumInteri(numInteri);
        pren.setNumRidotti(numRidotti);
        pren.setNumDisabili(numDisabili);
        pren.setNumAccomp(numAccomp);

        pren.setImportoDaPagare(importoPrevisto);
        pren.setImportoPagato(importoPagato);
        pren.setModoPagamento(mezzo);

        // se si è acceso il flag pagamento confermato, registro flag e data di conferma
        if ((pren.isPagamentoConfermato() == false) && (checkConfermato == true)) {
            pren.setPagamentoConfermato(true);
            pren.setDataPagamentoConfermato(LibDate.today());
            accesoConfermato = true;
        }

        // se si è acceso il flag pagamento ricevuto, registro flag e data di ricevimento
        if ((pren.isPagamentoRicevuto() == false) && (checkRicevuto == true)) {
            pren.setPagamentoRicevuto(true);
            pren.setDataPagamentoRicevuto(LibDate.today());
            accesoRicevuto = true;
        }

        // salvo e genero evento status changed per aggiornare la lista
        pren.save();

        // Se il flag Confermato era spento e ora è stato acceso
        // - genero evento status changed per aggiornare la lista
        // - genero un evento pagamento nel registro
        // - invio la mail se previsto
        if (accesoConfermato) {
            TipoEventoPren tipo = TipoEventoPren.confermaPagamento;
            fireStatusChanged(tipo);
            PrenotazioneModulo.creaEvento(pren, tipo, importoPagato.toString(), user);

            if(ModelliLettere.confermaPagamento.isSend(pren)){
                sendEmailEvento(pren, tipo, user);
                mailInviata = true;
            }
        }

        // Se il flag Ricevuto era spento e ora è stato acceso
        // - genero evento status changed per aggiornare la lista
        // - genero un evento pagamento nel registro
        // - invio la mail se previsto
        if (accesoRicevuto) {
            TipoEventoPren tipo = TipoEventoPren.registrazionePagamento;
            fireStatusChanged(tipo);
            PrenotazioneModulo.creaEvento(pren, tipo, importoPagato.toString(), user);

            if (ModelliLettere.registrazionePagamento.isSend(pren)) {
                sendEmailEvento(pren, tipo, user);
                mailInviata = true;
            }
        }

        return mailInviata;


    }


    /**
     * Esecuzione invio attestato di partecipazione (no UI)
     */
    public static void doAttestatoPartecipazione(Prenotazione pren, String user) throws EmailFailedException {
        TipoEventoPren tipoEvento = TipoEventoPren.attestatoPartecipazione;
        PrenotazioneModulo.creaEvento(pren, tipoEvento, "", getUsername());
        fireStatusChanged(tipoEvento);
        logger.log(Level.INFO, tipoEvento.getDescrizione() + " " + pren);
        String addr = pren.getEmailRiferimento();
        if (!addr.equals("")) {
            sendEmailEvento(pren, tipoEvento, user, addr);
        } else {
            throw new EmailFailedException("Manca l'indirizzo email del referente nella prenotazione.");
        }
    }


    /**
     * Invia una email per una dato evento di prenotazione.
     * <p>
     * I destinatari vengono recuperati dalla prenotazione in base al tipo di lettera
     * Crea un evento di spedizione mail (anche se l'invio fallisce)
     *
     * @param pren       la prenotazione
     * @param tipoEvento il tipo di evento
     * @param user       l'utente che genera l'evento
     */
    public static void sendEmailEvento(Prenotazione pren, TipoEventoPren tipoEvento, String user)
            throws EmailFailedException {
        sendEmailEvento(pren, tipoEvento, user, null);
    }


    /**
     * Invia una email per una dato evento di prenotazione
     * <p>
     * Crea un evento di spedizione mail (anche se l'invio fallisce)
     *
     * @param pren       la prenotazione
     * @param tipoEvento il tipo di evento
     * @param user       l'utente che genera l'evento
     * @param addr       elenco indirizzi destinatari - se nullo li recupera dalla prenotazione in base al tipo di lettera
     */
    public static void sendEmailEvento(Prenotazione pren, TipoEventoPren tipoEvento, String user, String addr)
            throws EmailFailedException {


        // prepara una mappa di informazioni email
        ModelliLettere modelloLettera = tipoEvento.getModelloLettera();

        if (modelloLettera != null) {

            // prepara una mappa di informazioni di sostituzione
            LetteraMap escapeMap = createEscapeMap(pren);

            // crea una mappa di informazioni generale per la stampa/invio delle lettere
            HashMap<String, Object> mailMap;
            try {
                Lettera lettera = Lettera.getLettera(modelloLettera, pren.getCompany());
                mailMap = createMailMap(pren, modelloLettera, lettera.getOggetto(), addr);
            } catch (EmailInfoMissingException e) {
                throw new EmailFailedException(e.getMessage());
            }

            // recupera la lettera da utilizzare
            Lettera lettera = Lettera.getLettera(modelloLettera, pren.getCompany());

            // spedisce la mail
            Spedizione sped = LetteraService.spedisci(lettera, escapeMap, mailMap);

            // crea un nuovo evento di spedizione email
            creaEventoMail(pren, tipoEvento, user, mailMap, sped.isSpedita());

            if (!sped.isSpedita()) {
                throw new EmailFailedException(sped.getErrore());
            }
        }


    }

    private static void notifyEmailFailed(EmailFailedException e) {
        Notification notification = new Notification("Invio email fallito", "\n"+e.getMessage(), Notification.Type.ERROR_MESSAGE);
        notification.setDelayMsec(-1);
        notification.show(Page.getCurrent());
    }

    /**
     * Crea un evento di invio mail per una data prenotazione
     *
     * @param pren       la prenotazione di riferimento
     * @param tipoEvento il tipo di evento
     * @param user       l'utente che ha generato l'evento
     * @param mailMap    la mappa mail per il recupero delle info di invio email
     * @param inviata    true se la mail è stata inviata con successo
     */
    private static void creaEventoMail(Prenotazione pren, TipoEventoPren tipoEvento, String user,
                                       HashMap<String, Object> mailMap, boolean inviata) {
        String detailString = "mail to:-> " + mailMap.get(MailKeys.destinatario.getKey());
        creaEvento(pren, tipoEvento, detailString, user, true, inviata);
    }

    /**
     * Crea una mappa di informazioni generale per la stampa/invio delle lettere
     *
     * @param addr elenco indirizzi destinatari - se nullo li recupera dalla prenotazione in base al tipo di lettera
     */
    private static HashMap<String, Object> createMailMap(Prenotazione pren, ModelliLettere modello, String oggetto, String addr)
            throws EmailInfoMissingException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if(addr==null){
            addr = modello.getEmailDestinatari(pren);
        }

        if (addr.equals("")) {
            throw new EmailInfoMissingException("Nessun indirizzo valido per la prenotazione " + pren);
        }

        // from: dalla company della prenotazione
        Company company = pren.getCompany();
        String from = CompanyPrefs.senderEmailAddress.getString(company);

        map.put(MailKeys.from.getKey(), from);
        map.put(MailKeys.destinatario.getKey(), addr);
        map.put(MailKeys.modello.getKey(), modello);
        map.put(MailKeys.oggetto.getKey(), oggetto);

        return map;
    }// end of method

    /**
     * Crea una mappa di informazioni di sostituzione per la stampa/invio delle lettere
     */
    private static LetteraMap createEscapeMap(Prenotazione pren) {
        Rappresentazione rapp;
        String string;
        StringToBigDecimalConverter bdConv = new StringToBigDecimalConverter(2);

        // prepara una mappa di informazioni da sostituire
        LetteraMap escapeMap = new LetteraMap();
        escapeMap.add(LetteraKeys.numeroPrenotazione, "" + pren.getNumPrenotazione());
        escapeMap.add(LetteraKeys.dataPrenotazione, LibDate.toStringDDMMYYYY(pren.getDataPrenotazione()));

        String strTitolo = "";
        String strCognome = "";
        String strNome = "";
        Insegnante ins = pren.getInsegnante();
        if (ins != null) {
            strTitolo = ins.getTitolo();
            strCognome = ins.getCognome();
            strNome = ins.getNome();
        }
        escapeMap.add(LetteraKeys.titoloInsegnante, strTitolo);
        escapeMap.add(LetteraKeys.cognomeInsegnante, strCognome);
        escapeMap.add(LetteraKeys.nomeInsegnante, strNome);

        escapeMap.add(LetteraKeys.telReferente, pren.getTelRiferimento());

        string = "";

        String nome = "";
        String indirizzo = "";
        String localita = "";
        String telefono = "";
        String fax = "";
        String email = "";
        Scuola scuola = pren.getScuola();

        if (scuola != null) {

            nome = scuola.getNome();

            // indirizzo
            indirizzo = scuola.getIndirizzo();

            // località
            String cap = scuola.getCap();
            if (cap != null && !cap.equals("")) {
                localita += " " + cap;
            }

            Comune comune = scuola.getComune();
            if (comune != null) {
                localita += " " + comune.toString();
            }

            string = scuola.getTelefono();
            if (string != null && !string.equals("")) {
                telefono = string;
            }

            string = scuola.getFax();
            if (string != null && !string.equals("")) {
                fax = string;
            }

            string = scuola.getEmail();
            if (string != null && !string.equals("")) {
                email = string;
            }

        }
        escapeMap.add(LetteraKeys.nomeScuola, nome);
        escapeMap.add(LetteraKeys.indirizzoScuola, indirizzo);
        escapeMap.add(LetteraKeys.localitaScuola, localita);
        escapeMap.add(LetteraKeys.telefonoScuola, telefono);
        escapeMap.add(LetteraKeys.faxScuola, fax);
        escapeMap.add(LetteraKeys.emailScuola, email);

        escapeMap.add(LetteraKeys.classe, pren.getClasse());

        String sImpInteri = "";
        String sImpRidotti = "";
        String sImpDisabili = "";
        String sImpAccomp = "";
        String sData = "";
        String sOra = "";
        String titoloEvento = "";
        String nomeSala = "";
        rapp = pren.getRappresentazione();
        if (rapp != null) {
            Evento evento = rapp.getEvento();
            if (evento != null) {

                sImpInteri = bdConv.convertToPresentation(evento.getImportoIntero());
                sImpRidotti = bdConv.convertToPresentation(evento.getImportoRidotto());
                sImpDisabili = bdConv.convertToPresentation(evento.getImportoDisabili());
                sImpAccomp = bdConv.convertToPresentation(evento.getImportoAccomp());

                string = evento.getTitolo();
                if (string != null && !string.equals("")) {
                    titoloEvento = string;
                }

            }
            sData = LibDate.toStringDDMMYYYY(rapp.getDataRappresentazione());
            sOra = LibDate.toStringHHMM(rapp.getDataRappresentazione());
            nomeSala = rapp.getSala().getNome();

        }
        escapeMap.add(LetteraKeys.dataRappresentazione, sData);
        escapeMap.add(LetteraKeys.oraRappresentazione, sOra);
        escapeMap.add(LetteraKeys.nomeSala, nomeSala);

        escapeMap.add(LetteraKeys.titoloEvento, titoloEvento);
        escapeMap.add(LetteraKeys.importoIntero, sImpInteri);
        escapeMap.add(LetteraKeys.importoRidotto, sImpRidotti);
        escapeMap.add(LetteraKeys.importoDisabile, sImpDisabili);
        escapeMap.add(LetteraKeys.importoAccomp, sImpAccomp);

        escapeMap.add(LetteraKeys.numPostiInteri, "" + pren.getNumInteri());
        escapeMap.add(LetteraKeys.numPostiRidotti, "" + pren.getNumRidotti());
        escapeMap.add(LetteraKeys.numPostiDisabili, "" + pren.getNumDisabili());
        escapeMap.add(LetteraKeys.numPostiAccomp, "" + pren.getNumAccomp());
        escapeMap.add(LetteraKeys.numPostiTotali, "" + pren.getNumTotali());

        escapeMap.add(LetteraKeys.dataScadenzaConfermaPrenotazione, LibDate.toStringDDMMYYYY(pren.getScadenzaConferma()));

        escapeMap.add(LetteraKeys.importoTotale, bdConv.convertToPresentation(pren.getImportoDaPagare()));

        string = "";
        ModoPagamento modo = pren.getModoPagamento();
        if (modo != null) {
            string = modo.toString();
        }
        escapeMap.add(LetteraKeys.modoPagamento, string);

        escapeMap.add(LetteraKeys.dataScadenzaPagamento, LibDate.toStringDDMMYYYY(pren.getScadenzaPagamento()));

        // data corrente
        string = LibDate.toStringDDMMYYYY(LibDate.today());
        escapeMap.add(LetteraKeys.dataCorrente, string);

        // data pagamento ricevuto
        string = LibDate.toStringDDMMYYYY(pren.getDataPagamentoRicevuto());
        escapeMap.add(LetteraKeys.dataPagamentoRicevuto, string);

        // importo effettivamente pagato
        escapeMap.add(LetteraKeys.importoPagato, bdConv.convertToPresentation(pren.getImportoPagato()));

        return escapeMap;
    }// end of method


    /**
     * Crea un evento relativo alla prenotazione nel registro Eventi
     * <p>
     *
     * @param pren     la prenotazione
     * @param tipo     il tipo di evento prenotazione
     * @param dettagli testo di dettaglio
     * @param user     nome dell'utente che invia
     * @param email    true se è un evento di tipo invio email
     * @param inviata  true se l'email è stata inviata con successo
     */
    public static EventoPren creaEvento(Prenotazione pren, TipoEventoPren tipo, String dettagli, String user, boolean email, boolean inviata) {
        EventoPren evento = new EventoPren();
        evento.setDettagli(dettagli);
        evento.setPrenotazione(pren);
        evento.setTimestamp(new Date());
        evento.setTipo(tipo.getId());
        evento.setUser(user);
        evento.setInvioEmail(email);
        evento.setEmailInviata(inviata);
        evento.save();
        return evento;
    }// end of method

    /**
     * Crea un evento relativo alla prenotazione nel registro Eventi
     */
    public static EventoPren creaEvento(Prenotazione pren, TipoEventoPren tipo, String dettagli, String user) {
        return creaEvento(pren, tipo, dettagli, user, false, false);
    }// end of method

    /**
     * Ritorna un filtro che seleziona tutte le prenotazioni scadute e non confermate
     */
    public static Filter getFiltroOpzioniDaConfermare() {
        DateTime jToday = new DateTime().withTimeAtStartOfDay();
        Date today = jToday.toDate();
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(Prenotazione_.confermata.getName(), false));
        filters.add(new Compare.Less(Prenotazione_.scadenzaConferma.getName(), today));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method

    /**
     * Ritorna un filtro che seleziona tutti i pagamenti scaduti e non confermati
     */
    public static Filter getFiltroPagamentiDaConfermare() {
        DateTime jToday = new DateTime().withTimeAtStartOfDay();
        Date today = jToday.toDate();
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(Prenotazione_.confermata.getName(), true));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), false));
        filters.add(new Compare.Less(Prenotazione_.scadenzaPagamento.getName(), today));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method

    /**
     * Ritorna un filtro che seleziona tutti i pagamenti confermati ma ancora da ricevere
     */
    public static Filter getFiltroPagamentiDaRicevere() {
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(new Compare.Equal(Prenotazione_.pagamentoConfermato.getName(), true));
        filters.add(new Compare.Equal(Prenotazione_.pagamentoRicevuto.getName(), false));
        Filter outFilter = new And(filters.toArray(new Filter[0]));
        return outFilter;
    }// end of method

    private static String getUsername() {
        return EventoBootStrap.getUsername();
    }


    /**
     * Invia una lettera a scelta per una data prenotazione
     */
    public static void testLettera(Prenotazione pren) {
        DialogoTestLettera dialogo = new DialogoTestLettera(pren);
        dialogo.show(UI.getCurrent());
    }

    static class DialogoTestLettera extends ConfirmDialog {

        private Prenotazione pren;
        private ArrayComboField letteraField;
        private EmailField emailField;


        public DialogoTestLettera(Prenotazione pren) {
            super(null);
            this.pren = pren;
            setTitle("Test invio lettera");
            letteraField = new ArrayComboField(ModelliLettere.values(), "Modello lettera");
            addComponent(letteraField);
            emailField = new EmailField("email");
            addComponent(emailField);
            setConfirmButtonText("Invia");
        }

        @Override
        protected void onConfirm() {
            boolean cont = true;
            ModelliLettere modello = null;
            String email = null;

            Object value = letteraField.getValue();
            if (value != null) {
                modello = (ModelliLettere) value;
            } else {
                cont = false;
            }

            if (cont) {
                email = emailField.getValue();
                if (email.equals("")) {
                    cont = false;
                }
            }

            if (cont) {

                // prepara una mappa di informazioni per la prenotazione
                LetteraMap escapeMap = createEscapeMap(pren);

                // prepara una mappa di informazioni email
                HashMap<String, Object> mailMap = null;
                try {
                    mailMap = createMailMap(pren, modello, modello.getOggetto(), email);

                    // spedisce la mail
                    Lettera lettera = Lettera.getLettera(modello, pren.getCompany());
                    Spedizione sped = LetteraService.spedisci(lettera, escapeMap, mailMap);
                    if (sped.isSpedita()) {
                        Notification.show("Email spedita");
                        super.onConfirm();
                    } else {
                        Notification notification = new Notification("Invio email fallito", "\n"+sped.getErrore(), Notification.Type.ERROR_MESSAGE);
                        notification.setDelayMsec(-1);
                        notification.show(Page.getCurrent());
                    }


                } catch (EmailInfoMissingException e) {
                    Notification notification = new Notification("Informazioni mancanti", "\n" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    notification.setDelayMsec(-1);
                    notification.show(Page.getCurrent());
                }

            }

        }


    }

}// end of class
