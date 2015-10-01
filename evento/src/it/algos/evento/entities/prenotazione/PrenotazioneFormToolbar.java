package it.algos.evento.entities.prenotazione;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.webbase.web.toolbar.FormToolbar;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PrenotazioneFormToolbar extends FormToolbar {

	private ArrayList<PrenotazioneFormToolbarListener> listeners = new ArrayList<PrenotazioneFormToolbarListener>();

	public void addToolbarListener(PrenotazioneFormToolbarListener listener) {
		this.listeners.add(listener);
	}// end of method

	protected void addButtons() {

		addButton("Conferma prenotazione", new ThemeResource("img/action_confirm.png"), 180, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				prenFire(PrenEvents.confermaPrenotazione);
			}// end of method
		});// end of anonymous class

		addButton("Annulla", new ThemeResource("img/action_cancel.png"), new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				fire(Events.cancel);
			}// end of method
		});// end of anonymous class

		addButton("Registra", new ThemeResource("img/action_save.png"), new MenuBar.Command() {
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
			default:
				break;
			}
		}

	}// end of method

	public interface PrenotazioneFormToolbarListener {
		public void confermaPrenotazione();
	}// end of method

	public enum PrenEvents {
		confermaPrenotazione;
	}// end of method

}
