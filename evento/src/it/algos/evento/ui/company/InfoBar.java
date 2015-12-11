package it.algos.evento.ui.company;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Barra informativa grafica sullo stato delle prenotazioni.
 */
public class InfoBar extends HorizontalLayout {
    private Segment segNonConf;
    private Segment segPagaNonConf;
    private Segment segPagaConf;
    private Segment segPagaRic;


    public InfoBar() {

        setWidth("100%");
        setHeight("3em");
        setSpacing(false);


        segNonConf=new Segment("prenotazioni non confermate","redBg");
        segPagaNonConf=new Segment("pagamenti non confermati","yellowBg");
        segPagaConf=new Segment("pagamenti confermati","blueBg");
        segPagaRic=new Segment("pagamenti ricevuti", "greenBg");

        addComponent(segNonConf);
        addComponent(segPagaNonConf);
        addComponent(segPagaConf);
        addComponent(segPagaRic);

        setSizes(2,250,34,160);

    }


    /**
     * Assegna i pesi ai vari segmenti
     */
    public void setSizes(int nonConf, int pagaNonConf, int pagaConf, int pagaRic){
        setExpandRatio(segNonConf, nonConf);
        segNonConf.setValue(nonConf);

        setExpandRatio(segPagaNonConf, pagaNonConf);
        segPagaNonConf.setValue(pagaNonConf);

        setExpandRatio(segPagaConf, pagaConf);
        segPagaConf.setValue(pagaConf);

        setExpandRatio(segPagaRic, pagaRic);
        segPagaRic.setValue(pagaRic);

    }


    /**
     * Segmento interno alla barra
     */
    private class Segment extends Button {

        private String title;
        private int value;

        public Segment(String title, String style) {
            this.title=title;
            addStyleName(style);
            addStyleName("infoBarSegment");
            setHeight("100%");
            setWidth("100%");

        }

//        public void setLabel(String text){
//            setCaption(text);
//            //label.setValue(text);
//        }

        public void setValue(int value){
            this.value=value;
            setCaption(""+value);
            setDescription(title+" "+value);
        }


    }

}
