package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Property;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.EmailField;

/**
 * Dialogo conferma invio email prenotazione
 */
class DialogoConfermaInvioManuale extends ConfirmDialog {
    private Prenotazione pren;

    private CheckBoxField sendRef;
    private CheckBoxField sendScuola;
    private EmailField mailRef;
    private EmailField mailScuola;

    public DialogoConfermaInvioManuale(Prenotazione pren, String titolo, String messaggio) {
        super(null);
        this.pren = pren;
        setTitle(titolo);
        setMessage(messaggio);
        setConfirmButtonText("Invia");

        sendRef = new CheckBoxField("Invia e-mail al referente");
        sendRef.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncUI();
            }
        });

        sendScuola = new CheckBoxField("Invia e-mail alla scuola");
        sendScuola.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncUI();
            }
        });

        mailRef = new EmailField();
        mailRef.setCaption(null);
        mailScuola = new EmailField();
        mailScuola.setCaption(null);

        addComponent(createUI());
        populateUI();
        syncUI();

    }


    private Component createUI() {
        GridLayout layout = new GridLayout(2, 2);
        layout.setMargin(false);
        layout.setSpacing(true);
        layout.addComponent(sendRef, 0, 0);
        layout.setComponentAlignment(sendRef, Alignment.MIDDLE_LEFT);
        layout.addComponent(mailRef, 1, 0);
        layout.setComponentAlignment(mailRef, Alignment.MIDDLE_LEFT);
        layout.addComponent(sendScuola, 0, 1);
        layout.setComponentAlignment(sendScuola, Alignment.MIDDLE_LEFT);
        layout.addComponent(mailScuola, 1, 1);
        layout.setComponentAlignment(mailScuola, Alignment.MIDDLE_LEFT);
        return layout;
    }


    private void populateUI(){
        String s;
        s=pren.getEmailRiferimento();
        if (s!=null && !s.equals("")){
            sendRef.setValue(true);
            mailRef.setValue(s);
        }

        s=pren.getScuola().getEmail();
        if (s!=null && !s.equals("")){
            sendScuola.setValue(true);
            mailScuola.setValue(s);
        }

    }

    protected void syncUI() {
        mailRef.setEnabled(sendRef.getValue());
        mailScuola.setEnabled(sendScuola.getValue());
        getConfirmButton().setEnabled(sendRef.getValue() | sendScuola.getValue());
    }

    @Override
    protected void onConfirm() {

        String err="";
        if(sendRef.getValue()){
            if(!mailRef.isValid()){
                if(!err.equals("")){err+="<br>";}
                err+="e-mail referente non valida";
            }
            if(mailRef.isEmpty()){
                if(!err.equals("")){err+="<br>";}
                err+="e-mail referente non specificata";
            }
        }

        if(sendScuola.getValue()){
            if(!mailScuola.isValid()){
                if(!err.equals("")){err+="<br>";}
                err+="e-mail scuola non valida";
            }
            if(mailScuola.isEmpty()){
                if(!err.equals("")){err+="<br>";}
                err+="e-mail scuola non specificata";
            }
        }


        if(err.equals("")) {
            super.onConfirm();
        }else{
            Notification n = new Notification(err);
            n.setHtmlContentAllowed(true);
            n.show(Page.getCurrent());
        }

    }


    /**
     * @return l'array degli indirizzi dei destinatari
     * (elenco indirizzi separati da virgola)
     */
    public String getDestinatari(){
        String str="";

        if(sendRef.getValue()){
            if(!mailRef.isEmpty()){
                if(!str.equals("")){str+=", ";}
                str+=mailRef.getValue();
            }
        }
        if(sendScuola.getValue()){
            if(!mailScuola.isEmpty()){
                if(!str.equals("")){str+=", ";}
                str+=mailScuola.getValue();
            }
        }

        return str;
    }

}
