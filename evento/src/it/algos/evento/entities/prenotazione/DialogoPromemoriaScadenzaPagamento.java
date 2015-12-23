package it.algos.evento.entities.prenotazione;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import it.algos.evento.EventoBootStrap;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.table.ATable;

/**
 * Dialogo conferma invio promemoria scadenza pagamento (da rimuovere?)
 */
class DialogoPromemoriaScadenzaPagamento extends ConfirmDialog {
    private Prenotazione pren;

    private SuccessListener successListener;

    public interface SuccessListener{
        void success();
    }

    public void setSuccessListener(SuccessListener l){
        successListener=l;
    }

    public DialogoPromemoriaScadenzaPagamento(Prenotazione pren) {
        super(null);
        this.pren = pren;
        setTitle("Invio promemoria scadenza pagamento");
        setMessage("Vuoi inviare un promemoria scadenza pagamento?");
        setConfirmButtonText("Invia");
    }

    @Override
    protected void onConfirm() {
        // esegue l'operazione di conferma e l'invio mail in un thread separato
        // al termine dell'operazione invoca il success listener
        new Thread(
                () -> {

                    try {

                        PrenotazioneModulo.doPromemoriaScadenzaPagamento(pren, EventoBootStrap.getUsername());

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
