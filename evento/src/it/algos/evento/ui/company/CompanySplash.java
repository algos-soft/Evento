package it.algos.evento.ui.company;

import com.vaadin.data.util.converter.StringToBigDecimalConverter;
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

import java.math.BigDecimal;
import java.util.Date;

/**
 * Splash screen della Company.
 * E' costituito dalla dashboard e dal logo.
 */
@SuppressWarnings("serial")
public class CompanySplash extends VerticalLayout {

    // classi per il CSS iniettato
    private static final String CSS_MIDDLE = "middle";
    private static final String CSS_BIG = "big";

    private CompanyHome home;
    private Resource res;
    private StringToIntegerConverter intConverter = new StringToIntegerConverter();
    private StringToBigDecimalConverter bdConverter = new StringToBigDecimalConverter();


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

        s = "Andamento stagione " + Stagione.getStagioneCorrente().toString();
        label = new FmtLabel();
        label.setValue(spanBig(s));
        vLayout.addComponent(label);

        //vLayout.addComponent(new Hr());
        //vLayout.addComponent(new Divider());

        // conferme prenotazioni scadute
        HorizontalLayout layoutPscad = new HorizontalLayout();
        layoutPscad.addStyleName("blueBg");
        label = new FmtLabel();
        int confInRitardo = EQuery.countPrenRitardoConferma(getStagione());
        label.setValue(spanSmall("conferme prenotazione scadute:\u2003") + spanBig(getString(confInRitardo)) + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_RITARDO_CONFERMA, true);

                // clicca sul menu Prenotazioni
                clickMenuPren();

            }
        });
        layoutPscad.addComponent(label);
        layoutPscad.addComponent(button);
        layoutPscad.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(layoutPscad);

        // conferme di pagamento in ritardo
        HorizontalLayout layoutPrit = new HorizontalLayout();
        label = new FmtLabel();
        int confPagaInRitardo = EQuery.countPrenRitardoPagamento1(getStagione());
        label.setValue(spanSmall("conferme di pagamento scadute:\u2003") + spanBig(getString(confPagaInRitardo)) + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_RITARDO_PAGAMENTO_1, true);

                // clicca sul menu Prenotazioni
                clickMenuPren();


            }
        });
        layoutPrit.addComponent(label);
        layoutPrit.addComponent(button);
        layoutPrit.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(layoutPrit);

//        Hr hr = new Hr();
//        vLayout.addComponent(hr);
//        vLayout.setExpandRatio(hr,0);

        Button b1 = new Button("Ciao");
        b1.setHeight("4px");
        vLayout.addComponent(b1);
        b1.addStyleName("yellowBg");


        // pagamenti confermati
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        int numPagaConfermati = EQuery.countPrenotazioniPagamentoConfermato(getStagione());
        BigDecimal importoPagaConfermati = EQuery.sumImportoPrenotazioniPagamentoConfermato(getStagione());
        label.setValue(spanSmall("pagamenti confermati:\u2003") + spanBig(getString(numPagaConfermati)) + spanSmall("\u2003per\u2003") + spanBig(getString(importoPagaConfermati) + " &euro;") + spanSmall("\u2003"));
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

        // pagamenti da confermare
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        int numPagaDaConfermare = EQuery.countPrenotazioniPagamentoNonConfermato(getStagione());
        BigDecimal importoPagaDaConfermare = EQuery.sumImportoPrenotazioniPagamentoNonConfermato(getStagione());
        label.setValue(spanSmall("pagamenti da confermare:\u2003") + spanBig(getString(numPagaDaConfermare)) + spanSmall("\u2003per\u2003") + spanBig(getString(importoPagaDaConfermare) + " &euro;") + spanSmall("\u2003"));
        button = new Button("Vedi", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_PAGAMENTO_NON_CONFERMATO, true);
                // clicca sul menu Prenotazioni
                clickMenuPren();
            }
        });
        hLayout.addComponent(label);
        hLayout.addComponent(button);
        hLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        vLayout.addComponent(hLayout);

        vLayout.addComponent(new Hr());

        // eventi in programma
        label = new FmtLabel();
        label.setValue(spanSmall("eventi in programma:\u2003") + spanBig(getString(EQuery.countEventi(getStagione()))));
        vLayout.addComponent(label);

        // rappresentazioni effettuate / da effettuare
        label = new FmtLabel();
        int totRapp = EQuery.countRappresentazioni(getStagione());
        int rappPassate = EQuery.countRappresentazioni(getStagione(), new Date());
        percent = Math.round(rappPassate * 100 / totRapp);
        label.setValue(spanSmall("rappresentazioni effettuate:\u2003") + spanBig(getString(rappPassate)) + spanSmall("\u2003su\u2003") + spanBig(getString(totRapp) + " (" + percent + "%)"));
        vLayout.addComponent(label);

        // prenotazioni ricevute
        hLayout = new HorizontalLayout();
        label = new FmtLabel();
        int prenRicevute = EQuery.countPrenotazioni(getStagione());
        int prenCongelate = EQuery.countPrenotazioniCongelate(getStagione());
        s = spanSmall("prenotazioni ricevute:\u2003") + spanBig(getString(prenRicevute));
        button = null;
        if (prenCongelate > 0) {
            s += spanSmall("\u2003(di cui congelate:\u2003");
            s += spanBig(getString(prenCongelate));
            s += spanSmall(")");

            button = new Button("Vedi", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {

                    // regola l'attributo che farà sì che il modulo esegua la query quando diventa visibile
                    LibSession.setAttribute(EventoApp.KEY_MOSTRA_PREN_CONGELATE, true);

                    // clicca sul menu Prenotazioni
                    clickMenuPren();

                }
            });

        }
        s += "\u2003";
        label.setValue(s);
        hLayout.addComponent(label);
        if (prenCongelate > 0) {
            hLayout.addComponent(button);
            hLayout.setComponentAlignment(button, Alignment.MIDDLE_LEFT);
        }
        vLayout.addComponent(hLayout);

        // posti prenotati e posti disponibili
        label = new FmtLabel();
        int prenotati = EQuery.countPostiPrenotati(getStagione());
        int disponibili = EQuery.countCapienza(getStagione());
        percent = Math.round(prenotati * 100 / disponibili);
        label.setValue(spanSmall("posti prenotati:\u2003") + spanBig(getString(prenotati)) + spanSmall("\u2003su\u2003") + spanBig(getString(disponibili) + " (" + percent + "%)"));
        vLayout.addComponent(label);


//        Panel pan = new Panel("Scadenze");
//        VerticalLayout l1 = new VerticalLayout();
//        l1.setMargin(true);
//        l1.setSpacing(true);
//        l1.addComponent(layoutPscad);
//        l1.addComponent(layoutPrit);
//        pan.setContent(l1);
//        vLayout.addComponent(pan);


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
     * Ritorna la stagione corrente
     */
    private Stagione getStagione() {
//        Stagione stagione = null;
//        Object bean = comboStagioni.getSelectedBean();
//        if ((bean != null) && (bean instanceof Stagione)) {
//            stagione = (Stagione) bean;
//        }
        return Stagione.getStagioneCorrente();
    }


    private String getString(int num) {
        return intConverter.convertToPresentation(num, String.class, null);
    }

    private String getString(BigDecimal num) {
        return bdConverter.convertToPresentation(num, String.class, null);
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

    /**
     * HTML Label
     */
    private class FmtLabel extends Label {
        public FmtLabel() {
            setContentMode(ContentMode.HTML);
            addStyleName("greenBg");
        }
    }

    /**
     * Horizontal divider
     */
    private class Hr extends Label {
        Hr() {
            super("<hr/>", ContentMode.HTML);
            addStyleName("yellowBg");
        }
    }

    /**
     * Horizontal divider
     */
    private class Divider extends HorizontalLayout {
        Divider() {
            setWidth("100%");
            setHeight("4px");
            addStyleName("yellowBg");
            setMargin(false);
        }
    }



}
