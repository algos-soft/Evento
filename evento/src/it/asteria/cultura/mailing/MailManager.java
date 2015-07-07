package it.asteria.cultura.mailing;

import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.web.dialog.ConfirmDialog;
import it.algos.web.field.ArrayComboField;
import it.algos.web.field.TextField;
import it.algos.web.lib.LibSession;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraService;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class MailManager extends ConfirmDialog {

    private Object[] ids;
    private TextField titoloField;
    private ArrayComboField letteraField;
    private OptionGroup destinatariOptions;
    private String itemReferente = "Referente della prenotazione";
    private String itemScuola = "Mail della scuola";
    private String itemEntrambi = "Entrambi gli indirizzi";
    private String currentItem = "";


    public MailManager(Object[] ids) {
        super(null);
        this.ids = ids;
        this.inizializzazioneGUI();
    }// end of constructor


    /**
     * Creazione grafica del dialogo
     */
    private void inizializzazioneGUI() {
        setTitle("Mailing");
        setMessage("Gestione mailing integrata");

        titoloField = new TextField("Titolo del mailing");
        titoloField.setRequired(true);
        addComponent(titoloField);

        letteraField = new ArrayComboField(this.getLettere(), "Seleziona una lettera");
        addComponent(letteraField);

        destinatariOptions = new OptionGroup();
        currentItem = itemReferente;
        destinatariOptions.setImmediate(true);
        destinatariOptions.addItem(itemReferente);
        destinatariOptions.addItem(itemScuola);
        destinatariOptions.addItem(itemEntrambi);
        destinatariOptions.select(currentItem);
        destinatariOptions.addValueChangeListener(new Property.ValueChangeListener() {
            Object obj = null;
            String itemText = "";

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                obj = destinatariOptions.getValue();
                if (obj != null && obj instanceof String) {
                    itemText = (String) obj;
                    setCurrentItem(itemText);
                }// fine del blocco if
            }// end of method
        });// end of inner class Listener
        addComponent(destinatariOptions);
    }// end of method

    /**
     * Dialogo confermato <br>
     * Recupera i valori dalla GUI <br>
     */
    private void dialogoConfermato() {
        String titolo = this.getTitolo();
        Lettera lettera = this.getLettera();
        ArrayList<String> destinatari = this.getDestinatari();

        if (destinatari != null && destinatari.size() > 0) {
            this.gestioneMailing(titolo, lettera, destinatari);
        } else {
            new Notification("Non risulta nessun destinatario del mailing",
                    "Controlla le opzioni",
                    Notification.TYPE_ERROR_MESSAGE, true)
                    .show(Page.getCurrent());
        }// fine del blocco if-else

    }// end of method


    /**
     * Spedizione <br>
     */
    private void gestioneMailing(String titolo, Lettera lettera, ArrayList<String> destinatari) {
        String dest = "";
        String oggetto = "";
        String testo = "";
        boolean spedita = false;

        if (LibSession.isDebug()) {
            String hostName = "smtp.algos.it";
            int smtpPort = 25;
            boolean useAuth = true;
            String username = "gac@algos.it";
            String password = "fulvia";
            String from = "alex@algos.it";
            boolean html = false;
            String allegati = "";

            dest = "gac@algos.it";
            oggetto = "Test/Prova";
            testo = lettera.getTesto();

            try { // prova ad eseguire il codice
                spedita = LetteraService.sendMail(hostName, smtpPort, useAuth, username, password, from, dest, oggetto, testo, html, allegati);
                spedita = LetteraService.sendMail(dest, oggetto, testo, false);
            } catch (Exception unErrore) { // intercetta l'errore
                String alfa = "";
            }// fine del blocco try-catch
        } else {
            new Notification("Occhio che non sei in debug!",
                    "Cambia il parametro d'ingresso",
                    Notification.TYPE_ERROR_MESSAGE, true)
                    .show(Page.getCurrent());
        }// fine del blocco if-else

    }// end of method


    /**
     * Elenco delle lettere <br>
     */
    private Object[] getLettere() {
        Object[] values = null;
        ArrayList lista = null;

        lista = Lettera.readAll();
        if (lista != null) {
            values = lista.toArray();
        }// fine del blocco if

        return values;
    }// end of method


    private boolean dialogoValido() {
        boolean status = false;

        if (esisteTitolo() && esisteLettera() && esistonoDestinatari()) {
            status = true;
        } else {

            if (!esistonoDestinatari()) {
                new Notification("Non risulta nessun destinatario del mailing",
                        "Controlla le opzioni",
                        Notification.TYPE_ERROR_MESSAGE, true)
                        .show(Page.getCurrent());
            } else {
                if (!esisteTitolo()) {
                    new Notification("Manca il titolo del mailing",
                            "Devi inserirlo",
                            Notification.TYPE_ERROR_MESSAGE, true)
                            .show(Page.getCurrent());
                } else {
                    if (!esisteLettera()) {
                        new Notification("Non hai selezionato nessuna lettera",
                                "Devi selezionarla",
                                Notification.TYPE_ERROR_MESSAGE, true)
                                .show(Page.getCurrent());
                    }// fine del blocco if-else
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if-else

        return status;
    }// end of method

    @Override
    protected void onConfirm() {
        if (dialogoValido()) {
            super.onConfirm();
            this.dialogoConfermato();
        }// fine del blocco if
    }// end of method

    private void setCurrentItem(String itemText) {
        this.currentItem = itemText;
    }// end of method

    private String getTitolo() {
        return titoloField.getValue();
    }// end of method

    private boolean esisteTitolo() {
        boolean status = false;
        String titolo = getTitolo();

        if (titolo != null && !titolo.equals("")) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

    private Lettera getLettera() {
        return (Lettera) letteraField.getValue();
    }// end of method

    private boolean esisteLettera() {
        boolean status = false;
        Lettera lettera = getLettera();

        if (lettera != null) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

    /**
     * Recupera le opzioni (radiobottoni) dalla GUI <br>
     * Sviluppa l'elenco dei destinatari <br>
     * Chi non ha indirizzo email non viene considerato <br>
     */
    private ArrayList<String> getDestinatari() {
        ArrayList<String> destinatari = new ArrayList<String>();
        boolean usaReferente = false;
        boolean usaScuola = false;
        long idPrenotazione = 0;
        Prenotazione prenotazione = null;
        String destRef = "";
        String destScuola = "";
        Scuola scuola = null;

        if (currentItem.equals(itemReferente)) {
            usaReferente = true;
        }// fine del blocco if

        if (currentItem.equals(itemScuola)) {
            usaScuola = true;
        }// fine del blocco if

        if (currentItem.equals(itemEntrambi)) {
            usaReferente = true;
            usaScuola = true;
        }// fine del blocco if

        if (ids != null) {

            for (Object value : ids) {
                if (value instanceof Long) {
                    idPrenotazione = (Long) value;
                }// fine del blocco if
                if (idPrenotazione > 0) {
                    prenotazione = Prenotazione.read(idPrenotazione);
                }// fine del blocco if
                if (prenotazione != null) {
                    destRef = prenotazione.getEmailRiferimento();
                    scuola = prenotazione.getScuola();
                    if (scuola != null) {
                        destScuola = scuola.getEmail();
                    }// fine del blocco if
                }// fine del blocco if

                if (usaReferente && !destRef.equals("")) {
                    destinatari.add(destRef);
                }// fine del blocco if
                if (usaScuola && !destScuola.equals("")) {
                    destinatari.add(destScuola);
                }// fine del blocco if
            }// fine del blocco if

        }// fine del blocco if


        return destinatari;
    }// end of method

    private boolean esistonoPrenotazioni() {
        boolean status = false;

        if (ids != null && ids.length > 0) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

    private boolean esistonoDestinatari() {
        boolean status = false;
        ArrayList<String> destinatari = getDestinatari();

        if (destinatari != null && destinatari.size() > 0) {
            status = true;
        }// fine del blocco if

        return status;
    }// end of method

}// end of class
