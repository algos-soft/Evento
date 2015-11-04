package it.algos.evento.entities.prenotazione;

import com.vaadin.ui.*;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.DateField;

import java.util.Date;

/**
 * Created by alex on 4-11-2015.
 */
public class DialogoConfermaPrenotazione  extends ConfirmDialog {
    DateField dateField;

    public DialogoConfermaPrenotazione(Listener closeListener, Date dataConfermaDefault) {
        super(closeListener);
        setTitle("Conferma prenotazione");
        removeAllDetail();
        dateField=new DateField("Data conferma");
        dateField.setValue(dataConfermaDefault);
        addComponent(dateField);
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

    //    @Override
//    protected Component createDetailComponent() {
//        Component comp=new DateField("Data conferma");
//        return comp;
//    }
}
