package it.algos.evento.entities.rappresentazione;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

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

//		subItem = item.addItem(CMD_MEMO_EXPORT, ICON_MEMO_EXPORT, new MenuBar.Command() {
//			public void menuSelected(MenuItem selectedItem) {
//				Object id = getTable().getSelectedId();
//				if (id != null) {
//					RappresentazioneModulo.esportaRappresentazione(id, getUI());
//				} else {
//					msgNoSelection();
//				}// end of if/else cycle
//			}// end of method
//		});// end of anonymous class
//		subItem.setDescription("Esporta il riepilogo di sala per la rappresentazione selezionata");

		subItem=item.addItem(RappresentazioneModulo.CMD_PARTECIPANTI_EXPORT, RappresentazioneModulo.ICON_MEMO_EXPORT, new MenuBar.Command() {
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
