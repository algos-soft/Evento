package it.algos.evento.entities.prenotazione;

import com.vaadin.ui.*;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.component.HorizontalLine;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.DateField;

import java.util.Date;

/**
 * Created by alex on 4-11-2015.
 */
public class DialogoConfermaPrenotazione  extends DialogoConfermaInvioManuale {
    DateField dateField;

    public DialogoConfermaPrenotazione(Prenotazione pren, Date dataConfermaDefault) {
        super(pren, "Conferma prenotazione", "");

        setConfirmButtonText("Conferma");

        dateField=new DateField();
        dateField.setValue(dataConfermaDefault);

        // aggiunge una riga al GridLayout della superclasse
        Label label=new Label("Data conferma");
        getGridLayout().addComponent(label);
        getGridLayout().setComponentAlignment(label, Alignment.MIDDLE_LEFT);
        getGridLayout().addComponent(dateField);
        getGridLayout().setComponentAlignment(dateField, Alignment.MIDDLE_LEFT);

    }

    public Date getDataConferma(){
        return dateField.getValue();
    }

    @Override
    protected void onConfirm() {
        if(getDataConferma()!=null){
            super.onConfirm();
        }else{
            Notification.show("Inserire la data di conferma.");
        }
    }

    @Override
    // se la spedizione mail alla conferma prenotazione
    // non è abilitata nelle preferenze, spegne le spunte
    protected void populateUI(){

        super.populateUI();

        if(!CompanyPrefs.sendMailConfPren.getBool()) {
            sendRef.setValue(false);
            sendScuola.setValue(false);
        }else{

            // qui l'opzione generale è attiva, copia i flag per Referente e Scuola
            sendRef.setValue(CompanyPrefs.sendMailConfPrenRef.getBool());
            sendScuola.setValue(CompanyPrefs.sendMailConfPrenScuola.getBool());

            // controllo opzione No Privati
            if(getPrenotazione().isPrivato()){
                if(CompanyPrefs.sendMailConfPrenNP.getBool()){
                    sendRef.setValue(false);
                }
            }

        }

    }


    @Override
    protected void syncUI() {
        super.syncUI();

        // questo dialogo si può confermare anche senza effettuare spedizioni
        if(getDestinatari().equals("")){
            getConfirmButton().setEnabled(true);
        }

    }



}
