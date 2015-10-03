package it.algos.evento.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import it.algos.evento.ui.MenuCommand;
import it.algos.webbase.web.screen.ErrorScreen;

import java.util.List;

@SuppressWarnings("serial")
public class EventoNavigator extends Navigator{
	
	public EventoNavigator(UI ui, SingleComponentContainer container) {
		super(ui, container);
		setErrorView(new ErrorView());
	}
	
	/**
	 * Configura il Navigator in base a una MenuBar.
	 * <p>
	 * Recupera i Component dalla Menubar e crea le View per il Navigator 
	 */
	public void configureFromMenubar(MenuBar mb){
		List<MenuItem> items = mb.getItems();
		for (MenuItem item : items) {
			scanItem(item);
		}
	}
	
	/**
	 * Crea le View per il Navigator e vi aggiunge i 
	 * componenti referenziati dal MenuItem
	 * (esegue ricorsivamente per i sottomenu).
	 */
	private void scanItem(MenuItem item){
		Command cmd = item.getCommand();
		if (cmd instanceof MenuCommand) {
			MenuCommand mcmd = (MenuCommand) cmd;
			String key = mcmd.getAddress();
			Component comp = mcmd.getComponent();
			View view = new NavigatorView(comp);
			addView(key, view);
		}
		
		// esamina i children dell'item
		List<MenuItem> items = item.getChildren();
		if(items!=null){
			for (MenuItem childItem : items) {
				scanItem(childItem);
			}
		}

	}
	
	
	
	/**
	 * A class encapsulating a Component in a View for the Navigator
	 */
	class NavigatorView extends CustomComponent implements View {

		public NavigatorView(Component content) {
			super();
			setSizeFull();

			content.setSizeFull();
			setCompositionRoot(content);
		}


		@Override
		public void enter(ViewChangeEvent event) {
		}

	}
	
	
	/**
	 * A class encapsulating an ErrorScreen in a View
	 */
	class ErrorView extends CustomComponent implements View {
		
		ErrorScreen errScreen;
		
		public ErrorView() {
			super();
			setSizeFull();
			errScreen = new ErrorScreen();
			setCompositionRoot(errScreen);
		}
		
		@Override
		public void enter(ViewChangeEvent event) {
			String msg = "Questa pagina non esiste: "+event.getViewName();
			errScreen.setMessage(msg);
		}

	}



}
