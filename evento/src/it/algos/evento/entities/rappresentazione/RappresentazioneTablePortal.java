package it.algos.evento.entities.rappresentazione;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import java.util.Arrays;

@SuppressWarnings("serial")
public class RappresentazioneTablePortal extends TablePortal {

	public RappresentazioneTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	@Override
	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem subItem;

		// bottone Altro...
		MenuBar.MenuItem item = toolbar.addButton("Altro...", FontAwesome.BARS, null);

		subItem=item.addItem(RappresentazioneModulo.CMD_PRENOTAZIONI_EXPORT, RappresentazioneModulo.ICON_MEMO_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				doExport(RappresentazioneModulo.CMD_PRENOTAZIONI_EXPORT);
			}// end of method
		});// end of anonymous class
		subItem.setDescription("Esporta il riepilogo delle prenotazioni per tutte le rappresentazioni selezionate");

		subItem=item.addItem(RappresentazioneModulo.CMD_PARTECIPANTI_EXPORT, RappresentazioneModulo.ICON_MEMO_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				doExport(RappresentazioneModulo.CMD_PARTECIPANTI_EXPORT);
			}// end of method
		});// end of anonymous class
		subItem.setDescription("Esporta il riepilogo dei partecipanti per tutte le rappresentazioni in selezionate");

		return toolbar;
	}// end of method


	/**
	 * Controlla la selezione ed esegue l'esportazione.
	 * @param cmd la costante che identifica il tipo di esportazione
	 * */
	private void doExport(String cmd){
		BaseEntity[] entities = getTable().getSelectedEntities();
		if(entities.length>0) {
			Rappresentazione[] rapps = Arrays.copyOf(entities, entities.length, Rappresentazione[].class);

			if(cmd.equals(RappresentazioneModulo.CMD_PRENOTAZIONI_EXPORT)){
				RappresentazioneModulo.esportaPrenotazioni(rapps);
			}

			if(cmd.equals(RappresentazioneModulo.CMD_PARTECIPANTI_EXPORT)){
				RappresentazioneModulo.esportaPartecipanti(rapps);
			}


		}else{
			Notification.show("Devi selezionare le rappresentazioni da esportare");
		}
	}




}// end of class
