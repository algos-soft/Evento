package it.algos.evento.entities.prenotazione;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import it.algos.evento.EventoBootStrap;
import it.algos.webbase.web.dialog.ConfirmDialog;

/**
 * Dialogo attestato di partecipazione
 */
class DialogoAttestatoPartecipazione extends ConfirmDialog {
    private Prenotazione pren;

    private SuccessListener successListener;

    public interface SuccessListener{
        void success();
    }

    public void setSuccessListener(SuccessListener l){
        successListener=l;
    }

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
            PrenotazioneModulo.doAttestatoPartecipazione(pren, EventoBootStrap.getUsername());

            // notifica il listener se registrato
            if(successListener!=null){
                successListener.success();
            }

        } catch (EmailFailedException e) {
            PrenotazioneModulo.notifyEmailFailed(e);
        }
        super.onConfirm();
    }


}
