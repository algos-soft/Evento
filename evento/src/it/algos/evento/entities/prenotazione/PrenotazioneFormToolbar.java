package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.toolbar.FormToolbar;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneFormToolbar extends FormToolbar {

    private ArrayList<PrenotazioneFormToolbarListener> listeners = new ArrayList<PrenotazioneFormToolbarListener>();

    private PrenotazioneForm form;

    private MenuBar.MenuItem bConfermaPren;
    private MenuBar.MenuItem bRegistraPaga;

    public PrenotazioneFormToolbar(PrenotazioneForm form) {
        super();
        this.form=form;

        // recupera il field confermata dal form e aggiunge un valueChange listener
        // per sincronizzare l'abilitazione dei bottoni Conferma Prenotazione e Registra Pagamento
        Field field = form.getBinder().getField(Prenotazione_.confermata.getName());
        bConfermaPren.setEnabled(!Lib.getBool(field.getValue()));
        bRegistraPaga.setEnabled(Lib.getBool(field.getValue()));
        field.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                bConfermaPren.setEnabled(!Lib.getBool(field.getValue()));
                bRegistraPaga.setEnabled(Lib.getBool(field.getValue()));
            }
        });



    }

    public void addToolbarListener(PrenotazioneFormToolbarListener listener) {
        this.listeners.add(listener);
    }// end of method

    protected void addButtons() {

        bConfermaPren = addButton("Conferma prenotazione", FontAwesome.THUMBS_O_UP, 180, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                prenFire(PrenEvents.confermaPrenotazione);
            }
        });

        bRegistraPaga = addButton(PrenotazioneTablePortal.CMD_REGISTRA_PAGAMENTO, FontAwesome.THUMBS_O_UP, 180, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                prenFire(PrenEvents.registraPagamento);
            }
        });

        // uno spaziatore per staccare i due gruppi di bottoni
        Component spacer = new Spacer();
        addCommandComponent(spacer);
        commandLayout.setExpandRatio(spacer, 1);

        addButton("Annulla", FontAwesome.BAN, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Events.cancel);
            }// end of method
        });// end of anonymous class

        addButton("Registra", FontAwesome.SAVE, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                fire(Events.save);
            }// end of method
        });// end of anonymous class


    }// end of method

    protected void prenFire(PrenEvents event) {
        for (PrenotazioneFormToolbarListener l : listeners) {
            switch (event) {
                case confermaPrenotazione:
                    l.confermaPrenotazione();
                    break;
                case registraPagamento:
                    l.registraPagamento();
                    break;
                default:
                    break;
            }
        }

    }// end of method

    public interface PrenotazioneFormToolbarListener {
        void confermaPrenotazione();
        void registraPagamento();
    }// end of method



    public enum PrenEvents {
        confermaPrenotazione,
        registraPagamento;
    }// end of method

}
