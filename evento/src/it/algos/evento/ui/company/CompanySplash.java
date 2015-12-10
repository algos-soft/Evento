package it.algos.evento.ui.company;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.evento.EventoApp;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.prenotazione.PrenotazioneModulo;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.EQuery;
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


    private Resource res;
    private CompanyHome home;
    private CompanyDashboard dashboard;

    public CompanySplash(CompanyHome home, Resource res) {
        super();
        this.home = home;
        this.res = res;
        setWidth("100%");
        setHeight("100%");

        // crea la UI
        // personalizzazione Asteria per non fornire la dashboard
        Component comp;
        if(Company.getCurrent().getCompanyCode().equals(EventoApp.ASTERIA_COMPANY_CODE)){
            comp=createUIComponentOld();
        }else{
            comp=createUI();
        }
        addComponent(comp);

        // listener invocato quando il modulo diventa visibile
        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent attachEvent) {
                if (dashboard!=null){
                    dashboard.update();
                }
            }
        });





    }

    private Component createUI() {
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("100%");
        dashboard=new CompanyDashboard(home);
        hLayout.addComponent(dashboard);

        Component logo = createLogo();
        hLayout.addComponent(logo);
        hLayout.setExpandRatio(logo, 1);

        return hLayout;
    }


    private Component createLogo() {
        Label label;

        VerticalLayout main = new VerticalLayout();
        main.setHeight("100%");
        //main.addStyleName("yellowBg");

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
