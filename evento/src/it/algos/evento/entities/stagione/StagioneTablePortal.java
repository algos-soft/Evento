package it.algos.evento.entities.stagione;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import it.algos.web.module.ModulePop;
import it.algos.web.table.TablePortal;
import it.algos.web.toolbar.Toolbar;

@SuppressWarnings("serial")
public class StagioneTablePortal extends TablePortal {

	public static final String CMD_SET_CORRENTE = "Imposta come stagione corrente";
	public static final ThemeResource ICON_SET_CORRENTE = new ThemeResource("img/action_checkmark18.png");

	public StagioneTablePortal(ModulePop modulo) {
		super(modulo);

		Toolbar toolbar = getToolbar();

		// bottone Set Stagione Corrente...
		MenuItem item = toolbar.addButton("Corrente", new ThemeResource("img/action_checkmark.png"), new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				Object id = getTable().getSelectedId();

				// controllo selezione
				if (id != null) {
					StagioneTable.setStagioneCorrente(id, getTable());
				} else {
					msgNoSelection();
				}

			}
		});// end of anonymous class);
		item.setDescription("Imposta la stagione selezionata come stagione corrente");


	}// end of method

	private void msgNoSelection() {
		Notification.show("Seleziona prima una stagione.");
	}// end of method

}
