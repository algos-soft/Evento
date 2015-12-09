package it.algos.evento.ui.company;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.EQuery;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibSession;

import java.util.Date;


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
public class CompanySplash extends VerticalLayout {

    // classi per il CSS iniettato
    private static final String CSS_MIDDLE = "middle";
    private static final String CSS_BIG = "big";

    CompanyHome home;
    private Resource res;
    private RelatedComboField comboStagioni;
    private StringToIntegerConverter converter = new StringToIntegerConverter();


    public CompanySplash(CompanyHome home, Resource res) {
        super();
        this.home = home;
        this.res = res;
        setWidth("100%");
        setHeight("100%");


        //addComponent(createUIComponentOld());

        addComponent(createUI());


    }

    private Component createUI() {
        HorizontalLayout hLayout = new HorizontalLayout();
        //hLayout.addStyleName("yellowBg");
        hLayout.setWidth("100%");
        hLayout.setHeight("100%");
        hLayout.addComponent(createDashboard());
        hLayout.addComponent(createLogo());
        return hLayout;
    }

    private Component createLogo() {
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

    private Component createDashboard() {

        HorizontalLayout hLayout;
        VerticalLayout vLayout;
        Button button;
        FmtLabel label;
        String s;
        int percent;

        injectCSS();

        vLayout = new VerticalLayout();
        vLayout.addStyleName("lightGrayBg");
        vLayout.setWidthUndefined();
        vLayout.setHeight("100%");
        vLayout.setMargin(true);

        comboStagioni = new RelatedComboField(Stagione.class, "Stagione");
        comboStagioni.setValue(Stagione.getStagioneCorrente().getId());
        vLayout.addComponent(comboStagioni);

        label = new FmtLabel();
        label.setValue(spanSmall("eventi in programma:\u2003") + spanBig(getString(EQuery.countEventi(getStagione()))));
        vLayout.addComponent(label);

        label = new FmtLabel();
        int totRapp = EQuery.countRappresentazioni(getStagione());
        int rappPassate = EQuery.countRappresentazioni(getStagione(), new Date());
        percent = Math.round(rappPassate * 100 / totRapp);
        label.setValue(spanSmall("rappresentazioni effettuate:\u2003") + spanBig(getString(rappPassate)) + spanSmall("\u2003su\u2003") + spanBig(getString(totRapp) + " (" + percent + "%)"));
        vLayout.addComponent(label);

        label = new FmtLabel();
        s = spanSmall("prenotazioni ricevute:\u2003");
        s += spanBig(getString(EQuery.countPrenotazioni(getStagione())));
        int congelate = EQuery.countPrenotazioniCongelate(getStagione());
        if (congelate > 0) {
            s += spanSmall("\u2003(congelate:\u2003");
            s += spanBig(getString(congelate));
            s += spanSmall(")");
        }
        label.setValue(s);
        vLayout.addComponent(label);

        label = new FmtLabel();
        int prenotati = EQuery.countPostiPrenotati(getStagione());
        int disponibili = EQuery.countCapienza(getStagione());
        percent = Math.round(prenotati * 100 / disponibili);
        label.setValue(spanSmall("posti prenotati:\u2003") + spanBig(getString(prenotati)) + spanSmall("\u2003su\u2003") + spanBig(getString(disponibili) + " (" + percent + "%)"));
        vLayout.addComponent(label);

        // conferme prenotazioni in ritardo
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        int confInRitardo = EQuery.countPrenRitardoConferma(getStagione());
        label.setValue(spanSmall("conferme prenotazione in ritardo:\u2003") + spanBig(getString(confInRitardo)) + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_RITARDO_CONFERMA, true);

                // clicca sul menu Prenotazioni
                clickMenuPren();

            }
        });
        hLayout.addComponent(label);
        hLayout.addComponent(button);
        hLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(hLayout);

        // conferme di pagamento in ritardo
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        int confPagaInRitardo = EQuery.countPrenRitardoPagamento1(getStagione());
        label.setValue(spanSmall("conferme di pagamento in ritardo:\u2003") + spanBig(getString(confPagaInRitardo)) + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_RITARDO_PAGAMENTO_1, true);

                // clicca sul menu Prenotazioni
                clickMenuPren();


            }
        });
        hLayout.addComponent(label);
        hLayout.addComponent(button);
        hLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(hLayout);



        // pagamenti ricevuti
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        int numPagaRicevuti=EQuery.countPrenotazioniPagamentoConfermato(getStagione());
        int importoPagaRicevuti=900;
        label.setValue(spanSmall("pagamenti confermati:\u2003") + spanBig(getString(numPagaRicevuti)) + spanSmall("\u2003per\u2003") + spanBig(getString(importoPagaRicevuti) + " &euro;") + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_CONFERMATO, true);
                // clicca sul menu Prenotazioni
                clickMenuPren();
            }
        });
        hLayout.addComponent(label);
        hLayout.addComponent(button);
        hLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(hLayout);

        // pagamenti da ricevere
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        label.setValue(spanSmall("pagamenti da ricevere:\u2003") + spanBig("99") + spanSmall("\u2003per\u2003") + spanBig("32000" + " &euro;") + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                int a = 87;
                int b = a;
            }
        });
        hLayout.addComponent(label);
        hLayout.addComponent(button);
        hLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(hLayout);


        return vLayout;
    }

    private String spanSmall(String text) {
        return "<span class='" + CSS_MIDDLE + "'>" + text + "</span>";
    }

    private String spanBig(String text) {
        return "<span class='" + CSS_MIDDLE + " " + CSS_BIG + "'>" + text + "</span>";
    }

    private void injectCSS() {

        Page.Styles styles = Page.getCurrent().getStyles();

        styles.add("." + CSS_MIDDLE + " {   display: inline-block;" +
                "  vertical-align: middle;" +
                "  font-size:1.2em;" +
                "  line-height: normal;}");

        styles.add("." + CSS_BIG + " { font-weight:bold; font-size:2em;}");

    }


    /**
     * Ritorn la stagione correntemente selezionata nel popup
     */
    private Stagione getStagione() {
        Stagione stagione = null;
        Object bean = comboStagioni.getSelectedBean();
        if ((bean != null) && (bean instanceof Stagione)) {
            stagione = (Stagione) bean;
        }
        return stagione;
    }


    private class FmtLabel extends Label {
        public FmtLabel() {
            setContentMode(ContentMode.HTML);
        }
    }

    private String getString(int num) {
        return converter.convertToPresentation(num, String.class, null);
    }

    /**
     * Clicca sul menu Prenotazioni
     */
    private void clickMenuPren() {
        MenuBar.MenuItem mi = home.getItemPrenotazioni();
        mi.getCommand().menuSelected(mi);
        if (mi.isCheckable()) {
            mi.setChecked(!mi.isChecked());
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
