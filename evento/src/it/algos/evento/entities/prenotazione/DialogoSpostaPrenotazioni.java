package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.RelatedComboField;

/**
 * Dialogo di spostamento di un gruppo di prenotazioni relative allo stesso evento.
 * Prepara ed esegue l'operazione.
 */
public class DialogoSpostaPrenotazioni extends ConfirmDialog {

    private Evento evento;  // evento di riferimento
    private Prenotazione[] aPren;   // prenotazioni da spostare
    private VerticalLayout contentLayout;
    private VerticalLayout previewPlaceholder; // il placeholder per la visualizzazione del preview operazione
    private RelatedComboField destPop;
    private PrenMover mover;

    /**
     * @param evento l'evento di riferimento
     * @param aPren  le prenotazioni da spostare (devono essere tutte relative
     *               allo stesso evento passato in evento, altrimenti lancia una eccezione.
     */
    public DialogoSpostaPrenotazioni(Evento evento, Prenotazione[] aPren) throws EventiDiversiException {
        super(null);
        this.evento = evento;
        this.aPren = aPren;

        // controlla che tutte le prenotazioni siano dell'evento
        for (Prenotazione pren : aPren) {
            if (!pren.getRappresentazione().getEvento().equals(evento)) {
                throw new EventiDiversiException();
            }
        }

        setTitle("Sposta prenotazioni");

        int posti=getTotPostiSpostati();
        Label label = new Label("Spostamento di " + aPren.length + " prenotazioni ("+posti+" posti)");

        // combo filtrato sulle rappresentazioni dello stesso evento
        this.destPop = new RelatedComboField(Rappresentazione.class, "destinazione");
        Container.Filter filter = new Compare.Equal(Rappresentazione_.evento.getName(), this.evento);
        destPop.getJPAContainer().addContainerFilter(filter);

        // selezione del popup modificata
        destPop.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                syncConfirmButton();
                if (getSelectedRapp() != null) {
                    mover = new PrenMover(DialogoSpostaPrenotazioni.this.aPren, getSelectedRapp());
                }
                syncPreviewArea();
            }
        });

        // placeholder per la preview dell'operazione
        previewPlaceholder = new VerticalLayout();

        //costruzione UI
        addComponent(label);
        addComponent(destPop);
        addComponent(previewPlaceholder);

        syncConfirmButton();


    }

    /**
     * Ritorna il numero di posti spostati
     * (le prenotazioni congelate non sono conteggiate)
     * @return il numero di posti spostati
     */
    private int getTotPostiSpostati(){
        int posti=0;
        for (Prenotazione pren : aPren){
            if (!pren.isCongelata()){
                posti+=pren.getNumTotali();
            }
        }
        return posti;
    }


    /**
     * Sincronizza l'area di preview in base alla rappresentazione selezionata.
     */
    private void syncPreviewArea() {
        Rappresentazione rapp = getSelectedRapp();
        previewPlaceholder.removeAllComponents();
        if (rapp != null) {
            if (mover != null) {
                Label label = new Label(rapp.toString());
                label.setContentMode(ContentMode.HTML);
                label.setValue(mover.getHTMLText());
                previewPlaceholder.addComponent(label);
            }
        }
    }


    /**
     * Abilita il bottone di conferma in base allo stato corrente del dialogo
     */
    private void syncConfirmButton() {
        boolean enabled = false;
        if (getSelectedRapp() != null) {
            if (mover != null) {
                if (mover.isEffettuabile()) {
                    enabled = true;
                }
            }
        }
        getConfirmButton().setEnabled(enabled);
    }


    /**
     * @return la rappresentazione selezionata, null se nessuna
     */
    private Rappresentazione getSelectedRapp() {
        Rappresentazione rapp = null;
        Object value = destPop.getSelectedBean();
        if ((value != null) && (value instanceof Rappresentazione)) {
            rapp = (Rappresentazione) value;
        }
        return rapp;
    }


    /**
     * The component shown in the detail area.
     */
    protected VerticalLayout createDetailComponent() {
        contentLayout = new VerticalLayout();
        contentLayout.setSpacing(true);
        contentLayout.setMargin(true);
        contentLayout.setStyleName("yellowBg");
        return contentLayout;
    }


    @Override
    protected void onConfirm() {
        super.onConfirm();
    }




    /**
     * Custom exception se si cerca di spostare prenotazioni relative ad eventi diversi
     */
    public class EventiDiversiException extends Exception {
        public EventiDiversiException() {
            super("Le prenotazioni da spostare devono essere tutte relative allo stesso evento");
        }
    }


}
