package it.algos.evento.ui.company;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.*;
import it.algos.webbase.web.lib.LibSession;

import java.util.Locale;

/**
 * Created by alex on 12-12-2015.
 */
public class InfoBarLegend extends HorizontalLayout {


    public InfoBarLegend() {
        setSizeUndefined();
        setSpacing(true);
        addComponent(new Element("infoBarSegment1","Non confermate"));
        addComponent(new Element("infoBarSegment2","Pagamento non confermato"));
        addComponent(new Element("infoBarSegment3","Pagamento confermato"));
        addComponent(new Element("infoBarSegment4","Pagamento ricevuto"));
    }

    /**
     * Segmento interno alla barra
     */
    private class Element extends HorizontalLayout {

        public Element(String style, String text) {
            setSpacing(true);
            setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            Button button = new Button();
            button.addStyleName(style);
            Label label = new Label(text);
            addComponent(button);
            addComponent(label);
        }



    }

//
//        /**
//     * Segmento interno alla barra
//     */
//    private class Element extends Button {
//
//        private String title;
//        private final StringToIntegerConverter intConv = new StringToIntegerConverter();
//
//        public Element(String title, String style, final String key) {
//            this.title=title;
//            addStyleName(style);
//            addStyleName("infoBarSegment");
//            setHeight("100%");
//            setWidth("100%");
//            setHtmlContentAllowed(true);
//
//            addClickListener(new ClickListener() {
//                @Override
//                public void buttonClick(ClickEvent clickEvent) {
//
//                    // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
//                    LibSession.setAttribute(key, true);
//                    // clicca sul menu Prenotazioni
//                    clickMenuPren();
//
//                }
//            });
//
//        }
//
//        public void setValue(int value){
//            String s = intConv.convertToPresentation(value, String.class, Locale.getDefault());
//            if (euro){
//                s+="&euro;";
//            }
//            setCaption(s);
//            setDescription(title + ": " + s+"<br><strong>clicca sul grafico per vedere</strong>");
//        }
//
//        /**
//         * Clicca sul menu Prenotazioni
//         */
//        private void clickMenuPren() {
//            MenuBar.MenuItem mi = home.getItemPrenotazioni();
//            mi.getCommand().menuSelected(mi);
//            if (mi.isCheckable()) {
//                mi.setChecked(!mi.isChecked());
//            }
//        }
//
//
//
//    }

}
