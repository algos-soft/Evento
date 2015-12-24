package it.algos.evento.entities.prenotazione;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

/**
 * Created by alex on 24-12-2015.
 * Dialogo di conferma congelamento prenotazione.
 * Consente di procedere anche senza inviare email.
 *
 */
public class DialogoCongelaPrenotazione extends DialogoConfermaInvioManuale{

    public DialogoCongelaPrenotazione(Prenotazione pren, String titolo, String messaggio) {
        super(pren, titolo, messaggio);
        setConfirmButtonText("Congela");
    }


    @Override
    protected void syncUI() {
        super.syncUI();

        // qui si può confermare anche senza spedizioni
        if(getDestinatari().equals("")){
            getConfirmButton().setEnabled(true);
        }

    }



}
