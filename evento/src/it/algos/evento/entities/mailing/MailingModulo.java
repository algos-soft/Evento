package it.algos.evento.entities.mailing;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import it.algos.evento.entities.prenotazione.MailDialog;
import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MailingModulo extends EModulePop {

    /**
     * Costruttore senza parametri
     * La classe implementa il pattern Singleton.
     * Per una nuova istanza, usare il metodo statico getInstance.
     * Usare questo costruttore SOLO con la Reflection dal metodo Module.getInstance
     * Questo costruttore è pubblico SOLO per l'uso con la Reflection.
     * Per il pattern Singleton dovrebbe essere privato.
     *
     * @deprecated
     */
    public MailingModulo() {
        super(Mailing.class);
    }// end of constructor

    /**
     * Crea una sola istanza di un modulo per sessione.
     * Tutte le finestre e i tab di un browser sono nella stessa sessione.
     */
    public static MailingModulo getInstance(){
        return (MailingModulo) ModulePop.getInstance(MailingModulo.class);
    }// end of singleton constructor


    /**
     * Gestione mailing integrata.
     * <p>
     * Nelle prenotazioni si seleziona una lista di prenotazioni <br>
     * Ci si assicura che nelle Lettere esista quella desiderata. Altrimenti la si crea <br>
     * Inserimento del titolo, selezione della lettera e conferma della data di creazione <br>
     * Si selezionano (ricorsivamente) i destinatari <br>
     * Conferma <br>
     * Creazione di 1 record di Mailing <br>
     * Creazione di n records di Destinatarimailing <br>
     */
    public static void gestioneMailing(ArrayList<Long> ids, UI gui) {
        if (ids != null && ids.size() > 0) {
            new MailDialog(ids).show(gui);
        } else {
            new Notification("Non risulta selezionata nessuna prenotazione",
                    "Devi selezionare una o più prenotazioni",
                    Notification.TYPE_HUMANIZED_MESSAGE, true)
                    .show(Page.getCurrent());
        }// fine del blocco if-else

    }// end of method

    /**
     * Titolo (caption) dei dialogo nuovo record. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionNew() {
        return "Nuova mailing";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionEdit() {
        return "Modifica mailing";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionSearch() {
        return "Ricerca mailing";
    }// end of method

    /**
     * Crea i campi visibili
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Non garantiscel'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[]{Mailing_.titolo, Mailing_.lettera, Mailing_.dataCreazione};
    }// end of method
}// end of class
