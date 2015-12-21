package it.algos.evento.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import it.algos.webbase.web.Command.MenuCommand;
import it.algos.webbase.web.navigator.AlgosNavigator;

import java.util.List;

/**
 * Created by alex on 21/12/15.
 */
public class EventoNavigator extends AlgosNavigator {
    public EventoNavigator(UI ui, SingleComponentContainer container) {
        super(ui, container);
    }


    @Override
    protected void scanItem(MenuBar.MenuItem item) {
        MenuBar.Command cmd = item.getCommand();
        if (cmd instanceof MenuCommand) {
            MenuCommand mcmd = (MenuCommand) cmd;
            String key = mcmd.getAddress();
            Class clazz = mcmd.getClazz();
            boolean caching = mcmd.isViewCached();
            addProvider(new NavigatorViewProvider(key, clazz, caching));
        }// fine del blocco if

        // esamina i children dell'item
        List<MenuBar.MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuBar.MenuItem childItem : items) {
                scanItem(childItem);
            } // fine del ciclo for
        }// fine del blocco if
    }


}
