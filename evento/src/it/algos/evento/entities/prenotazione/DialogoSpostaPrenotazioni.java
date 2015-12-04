package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.RelatedComboField;

import java.util.ArrayList;

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
    private OpWrapper opWrapper;

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

        // combo filtrato sulle rappresentazioni dello stesso evento
        this.destPop = new RelatedComboField(Rappresentazione.class, "destinazione");
        Container.Filter filter = new Compare.Equal(Rappresentazione_.evento.getName(), this.evento);
        destPop.getJPAContainer().addContainerFilter(filter);

        // selezione del popup modificata
        destPop.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                syncConfirmButton();
                if (getSelectedRapp()!=null){
                    buildOpWrapper();
                }
                syncPreviewArea();
            }
        });

        // placeholder per la preview dell'operazione
        previewPlaceholder=new VerticalLayout();

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
        addComponent(previewPlaceholder);

        syncConfirmButton();


    }


    /**
     * Sincronizza l'area di preview in base alla rappresentazione selezionata.
     */
    private void syncPreviewArea() {
        Rappresentazione rapp = getSelectedRapp();
        previewPlaceholder.removeAllComponents();
        if(rapp!=null){
            if (opWrapper!=null){
                Label label = new Label(rapp.toString());
                label.setContentMode(ContentMode.HTML);
                label.setValue(opWrapper.getHTMLText());
                previewPlaceholder.addComponent(label);
            }
        }
    }


    /**
     * Abilita il bottone di conferma in base allo stato corrente del dialogo
     */
    private void syncConfirmButton(){
        boolean enabled=false;
        if(getSelectedRapp()!=null){
            if (opWrapper!=null){
                if (opWrapper.isEffettuabile()){
                    enabled=true;
                }
            }
        }
        getConfirmButton().setEnabled(enabled);
    }


    /**
     * @return la rappresentazione selezionata, null se nessuna
     */
    private Rappresentazione getSelectedRapp(){
        Rappresentazione rapp=null;
        Object value = destPop.getSelectedBean();
        if((value!=null) && (value instanceof Rappresentazione)){
            rapp=(Rappresentazione)value;
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
     * Costruisce il wrapper delle operazioni
     */
    private void buildOpWrapper(){
        opWrapper=new OpWrapper();
    }


    /**
     * Wrapper che mantiene i dati di controllo della operazione
     * */
    private class OpWrapper{

        ArrayList<String> warningRows = new ArrayList();
        ArrayList<String> errorRows = new ArrayList();
        ArrayList<String> infoRows = new ArrayList();
        private int totPersoneSpostate;

        Rappresentazione destRapp;

        public OpWrapper() {
            destRapp=getSelectedRapp();
            for (Prenotazione pren : aPren){
                checkPren(pren);

                // incrementa il totale delle persone che verrebero spostate
                if(!pren.isCongelata()){
                    totPersoneSpostate +=pren.getNumTotali();
                }
            }
        }

        /**
         * Processa una prenotazione e genera le righe di warning o di errore
         */
        private void checkPren(Prenotazione pren){

            // controllo che la prenotazione non faccia già parte della rappresentazione destinazione
            if (pren.getRappresentazione().equals(destRapp)){
                errorRows.add(pren+" è già nella rappresentazione selezionata.");
            }


        }

        /**
         * Controlla l'operazione a livello generale
         */
        private void checkOp(Prenotazione pren){

            // numero di persone totali nella rappresentazione destinazione dopo lo spostamento
            int numPersoneDopo = RappresentazioneModulo.getPostiPrenotati(destRapp)+totPersoneSpostate;
            int capienza = destRapp.getCapienza();
            if (numPersoneDopo>capienza){
                String warn = "Dopo lo spostamento, la capienza massima sarà superata (max="+capienza+", tot="+numPersoneDopo;
                warningRows.add(warn);
            }


        }




            String getHTMLText(){
            String s = "a<br>b<br>c<br>d<br>e";
            return s;
        }

        /**
         * @return true se l'operazione è effettuabile
         */
        boolean isEffettuabile(){
            return false;
        }

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
