package it.algos.evento.ui;

import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.webbase.web.lib.LibImage;


//Andamento stagione 2015-2016
//
//        numero di eventi in programma: 7
//        numero di rappresentazioni effettuate: 45 su 99 previste
//        numero di prenotazioni ricevute: 87
//        numero di posti assegnati : 1245 su 3400
//
//        conferme prenotazione in ritardo: 8 -> [Vai]
//        conferme di pagamento in ritardo: 22 -> [Vai]
//
//        pagamenti ricevuti: N. 38 per 13.200 € -> [Vai]
//        pagamenti da ricevere: N. 22 pari a 6.500€ -> [Vai]

@SuppressWarnings("serial")
public class SplashScreen extends VerticalLayout {
    private Resource res;

    public SplashScreen(Resource res) {
        super();
        this.res = res;
        setWidth("100%");
        setHeight("100%");

        addComponent(createUIComponentOld());


    }

    private Component createUIComponent() {

        Page.Styles styles = Page.getCurrent().getStyles();
        // inject the style.
        styles.add(".div1 {  width: 250px;\n" +
                "  height: 100px;\n" +
                "  line-height: 100px;\n" +
                "  text-align: center;}");

        styles.add(".span1 {   display: inline-block;\n" +
                "  vertical-align: middle;\n" +
                "  line-height: normal;}");

        styles.add(".lab1 { font-weight:bold; font-size:3em;}");

        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("yellowBg");

        FmtLabel label = new FmtLabel();
        String s = "<div class='div1'>\n" +
                "  <span class='span1'>Lorem ipsum dolor sit amet </span><span class='span1 lab1'>88</span>\n" +
                "</div>";
        label.setValue(s);
        label.addStyleName("greenBg");

        //label.setValue("<span>numero di eventi in programma: </span>"+"<span class='lab1'>7</span>");
        layout.addComponent(label);

        label = new FmtLabel();
        label.setValue("<span>numero di rappresentazioni effettuate: </span>"+"<span style='font-weight:bold;'>45</span> su 99 previste");
        layout.addComponent(label);

        return layout;
    }

    private class FmtLabel extends Label{
        public FmtLabel() {
            setContentMode(ContentMode.HTML);
        }
    }


    private Component createUIComponentOld() {
        Label label;

        VerticalLayout main = new VerticalLayout();
        main.setWidth("100%");
        main.setHeight("100%");

        // horizontal: label left + image + label right
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        if (res != null) {
            Image img = LibImage.getImage(res);
            layout.addComponent(img);
        }

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        // vertical: label top + image layout + label bottom
        label = new Label();
        main.addComponent(label);
        main.setExpandRatio(label, 1.0f);

        main.addComponent(layout);

        label = new Label();
        main.addComponent(label);
        main.setExpandRatio(label, 1.0f);

        return main;
    }

}
