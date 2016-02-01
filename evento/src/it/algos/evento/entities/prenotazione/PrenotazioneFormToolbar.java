package it.algos.evento.entities.prenotazione;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.toolbar.FormToolbar;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneFormToolbar extends FormToolbar {

    private ArrayList<PrenotazioneFormToolbarListener> listeners = new ArrayList<PrenotazioneFormToolbarListener>();

    public void addToolbarListener(PrenotazioneFormToolbarListener listener) {
        this.listeners.add(listener);
    }// end of method

    protected void addButtons() {

        addButton("Conferma prenotazione", FontAwesome.THUMBS_O_UP, 180, new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                prenFire(PrenEvents.confermaPrenotazione);
            }
        });

        addButton(PrenotazioneTablePortal.CMD_REGISTRA_PAGAMENTO, FontAwesome.THUMBS_O_UP, 180, new MenuBar.Command() {
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
