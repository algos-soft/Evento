package it.algos.evento.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

import java.util.List;

/**
 * Custom menu command to show a component in the placeholder
 */
public class MenuCommand implements MenuBar.Command {
    private MenuBar mb;
    private String address;
    private Component comp;

    public MenuCommand(MenuBar mb, String address, Component comp) {
        super();
        this.mb = mb;
        this.address = address;
        this.comp = comp;
    }

//		public MenuCommand(String address, Component comp) {
//			this(null, address, comp);
//		}


    public void menuSelected(MenuBar.MenuItem selectedItem) {

        // Navigate to a specific state
        UI.getCurrent().getNavigator().navigateTo(address);

        // de-selects all the items in the menubar
        if (mb != null) {
            List<MenuBar.MenuItem> items = mb.getItems();
            for (MenuBar.MenuItem item : items) {
                deselectItem(item);
            }
        }

        /* highlights the selected item
         * the style name will be prepended automatically with "v-menubar-menuitem-" */
        selectedItem.setStyleName("highlight");

        // it this item is inside another item, highlight also the parents in the chain
        MenuBar.MenuItem item = selectedItem;
        while (item.getParent() != null) {
            item = item.getParent();
            item.setStyleName("highlight");
        }

    }

    /**
     * Recursively de-selects one item and all its children
     */
    private void deselectItem(MenuBar.MenuItem item) {
        item.setStyleName(null);
        List<MenuBar.MenuItem> items = item.getChildren();
        if (items != null) {
            for (MenuBar.MenuItem child : items) {
                deselectItem(child);
            }
        }
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the comp
     */
    public Component getComponent() {
        return comp;
    }

}
