package it.algos.evento.entities.destinatario;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

@SuppressWarnings("serial")
public class DestinatarioModulo extends EModulePop {

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
    public DestinatarioModulo() {
        super(Destinatario.class);
    }// end of constructor

    /**
     * Crea una sola istanza di un modulo per sessione.
     * Tutte le finestre e i tab di un browser sono nella stessa sessione.
     */
    public static DestinatarioModulo getInstance(){
        return (DestinatarioModulo) ModulePop.getInstance(DestinatarioModulo.class);
    }// end of singleton constructor

    /**
     * Titolo (caption) dei dialogo nuovo record. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionNew() {
        return "Nuovo destinatario";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di modifica. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionEdit() {
        return "Modifica destinatario";
    }// end of method

    /**
     * Titolo (caption) dei dialogo di ricerca. <br>
     * Come default usa il titolo standard <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     */
    @Override
    protected String getCaptionSearch() {
        return "Ricerca destinatari";
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
        return new Attribute[]{Destinatario_.mailing, Destinatario_.indirizzo, Destinatario_.dataSpedizione, Destinatario_.spedita};
    }// end of method

}// end of class
