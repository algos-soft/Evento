package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Property;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
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
    private VerticalLayout destPlaceholder; // il placeholder per i componenti da visualizzare in base all'opzione

    /**
     * @param evento l'evento di riferimento
     * @param aPren  le prenotazioni da spostare (devono essere tutte relative
     *               allo stesso evento passato in evento, altrimenti lancia una eccezione.
     */
    public DialogoSpostaPrenotazioni(Evento evento, Prenotazione[] aPren) throws EventiDiversiException {
        super(null);
        this.evento=evento;
        this.aPren = aPren;

        // controlla che tutte le prenotazioni siano dell'evento
        for(Prenotazione pren : aPren){
            if (!pren.getRappresentazione().getEvento().equals(evento)){
                throw new EventiDiversiException();
            }
        }

        setTitle("Sposta prenotazioni");


////        removeAllDetail();
////        dateField=new DateField("Data conferma");
////        dateField.setValue(dataConfermaDefault);
//        String word;
//        if (aPren.length==1){
//
//        }else {
//
//        }

        Label label = new Label("Spostamento di " + aPren.length + " prenotazioni");

        RelatedComboField destPop = new RelatedComboField(Rappresentazione.class, "destinazione");

//        OptionGroup multi = new OptionGroup("Multiple Selection");
//        multi.setMultiSelect(true);
//        String optExistingDate="Sposta su data esistente";
//        String optNewDate="Crea nuova data";
//        multi.addItem(optExistingDate);
//        multi.addItem(optNewDate);
//        multi.setMultiSelect(false);
//        multi.setCaption("destinazione:");
//        multi.setValue(optExistingDate);
//
//        multi.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                Object val = event.getProperty().getValue();
//                int a = 87;
//                int b=a;
//            }
//        });

        //costruzione UI
        addComponent(label);
        addComponent(destPop);

    }


    /**
     * Sincronizza il componente di destinazione in
     * base all'opzione correntemente selezionata.
     * Se "Sposta su data esistente", mostra il popup di scelta delle rappresentazioni
     * Se "Crea nuova data" richiede di selezionare la data/ora, la sala e la capienza
     */
    private void syncDestArea() {

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
    public class EventiDiversiException extends Exception{
        public EventiDiversiException() {
            super("Le prenotazioni da spostare devono essere tutte relative allo stesso evento");
        }
    }


}
