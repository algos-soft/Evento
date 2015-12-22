package it.algos.evento.entities.prenotazione;

import com.sun.net.httpserver.Authenticator;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import it.algos.evento.EventoBootStrap;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.table.ATable;

/**
 * Dialogo conferma invio riepilogo opzione
 */
class DialogoInviaRiepilogoOpzione extends ConfirmDialog {
    private Prenotazione pren;
    private SuccessListener successListener;

    public interface SuccessListener{
        void success();
    }

    public void setSuccessListener(SuccessListener l){
        successListener=l;
    }


    public DialogoInviaRiepilogoOpzione(Prenotazione pren) {
        super(null);
        this.pren = pren;
        setTitle("Invio riepilogo opzione con istruzioni");
        setMessage("Vuoi inviare il riepilogo opzione?");
        setConfirmButtonText("Invia");
    }

    @Override
    protected void onConfirm() {

        // esegue l'operazione di conferma e l'invio mail in un thread separato
        // al termine dell'operazione viene visualizzata una notifica
        new Thread(
                () -> {

                    try {

                        PrenotazioneModulo.doInvioIstruzioni(pren, EventoBootStrap.getUsername());

                        // notifica il listener se registrato
                        if(successListener!=null){
                            successListener.success();
                        }


                    } catch (EmailFailedException e) {
                        PrenotazioneModulo.notifyEmailFailed(e);
                    }



                }

        ).start();

        super.onConfirm();
    }
}
