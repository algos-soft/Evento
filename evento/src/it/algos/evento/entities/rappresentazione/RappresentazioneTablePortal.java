package it.algos.evento.entities.rappresentazione;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class RappresentazioneTablePortal extends TablePortal {

	public static final String CMD_MEMO_EXPORT = "Esporta riepilogo rappresentazione...";
	public static final ThemeResource ICON_MEMO_EXPORT = new ThemeResource("img/action_export18.png");

	public static final String CMD_PARTECIPANTI_EXPORT = "Esporta partecipanti...";

	public RappresentazioneTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	@Override
	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem subItem;

		// bottone Altro...
		MenuBar.MenuItem item = toolbar.addButton("Altro...", new ThemeResource("img/action_more.png"), null);

		subItem = item.addItem(CMD_MEMO_EXPORT, ICON_MEMO_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				Object id = getTable().getSelectedId();
				if (id != null) {
					RappresentazioneModulo.esportaRappresentazione(id, getUI());
				} else {
					msgNoSelection();
				}// end of if/else cycle
			}// end of method
		});// end of anonymous class
		subItem.setDescription("Esporta il riepilogo di sala per la rappresentazione selezionata");

		subItem=item.addItem(CMD_PARTECIPANTI_EXPORT, ICON_MEMO_EXPORT, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				if(getTable().getTotalRows()>0){
					getModuloRappresentazione().esportaPartecipanti(getUI());
				}else{
					msgNoRecords();
				}
			}// end of method
		});// end of anonymous class
		subItem.setDescription("Esporta il riepilogo dei partecipanti per tutte le rappresentazioni in lista");



		return toolbar;
	}// end of method



	private void msgNoSelection() {
		Notification.show("Seleziona prima una rappresentazione.");
	}// end of method
	private void msgNoRecords() {
		Notification.show("Carica prima in lista un elenco di rappresentazioni.");
	}// end of method

	private RappresentazioneModulo getModuloRappresentazione(){
		return (RappresentazioneModulo)getModule();
	}

}// end of class
