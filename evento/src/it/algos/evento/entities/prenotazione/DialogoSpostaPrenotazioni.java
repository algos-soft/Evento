package it.algos.evento.entities.prenotazione;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.DateField;

/**
 * Dialogo di spostamento di un gruppo di prenotazioni.
 * Prepara ed esegue l'operazione.
 */
public class DialogoSpostaPrenotazioni extends ConfirmDialog {

    private VerticalLayout placeholderLayout;
    private Prenotazione[] aPren;

    public DialogoSpostaPrenotazioni(Prenotazione[] aPren) {
        super(null);
        this.aPren=aPren;
        setTitle("Sposta prenotazioni");



//        removeAllDetail();
//        dateField=new DateField("Data conferma");
//        dateField.setValue(dataConfermaDefault);
        String word;
        if (aPren.length==1){

        }else {

        }

        Label label = new Label("Spostamento di 10 prenotazioni");
        addComponent(new Label("ciao1"));
        addComponent(new Label("ciao2"));
        addComponent(new Label("ciao3"));

    }

    /**
     * The component shown in the detail area.
     */
    protected VerticalLayout createDetailComponent() {
        placeholderLayout = new VerticalLayout();
        placeholderLayout.setSpacing(true);
        placeholderLayout.setMargin(true);
        placeholderLayout.setStyleName("yellowBg");
        return placeholderLayout;
    }

}
