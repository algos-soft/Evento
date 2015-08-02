package it.algos.evento.entities.lettera;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;
import it.algos.evento.entities.lettera.allegati.GestoreAllegati;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class LetteraTablePortal extends TablePortal {


	public LetteraTablePortal(ModulePop modulo) {
		super(modulo);
	}// end of constructor

	public TableToolbar createToolbar() {
		final TableToolbar toolbar = super.createToolbar();

		MenuBar.MenuItem item = toolbar.addButton("Altro...", new ThemeResource("img/action_more.png"), null);

		
		item.addItem("Gestione allegati...", null, new MenuBar.Command() {
			public void menuSelected(MenuItem selectedItem) {
				new GestoreAllegati().show(getUI());
			}// end of method
		});// end of anonymous class


		return toolbar;
	}// end of method

}// end of class

