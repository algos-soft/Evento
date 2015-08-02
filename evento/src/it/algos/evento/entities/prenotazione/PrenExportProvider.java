package it.algos.evento.entities.prenotazione;

import com.vaadin.addon.jpacontainer.EntityItem;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.webbase.web.importexport.ExportProvider;

import java.util.ArrayList;

/**
 * Created by alex on 31-05-2015.
 */
public class PrenExportProvider extends ExportProvider<EntityItem<Prenotazione>> {

    ArrayList<Object> values;

    @Override
    public String[] getTitles() {
        String[] titles = new String[]{
                "N. prenotazione",
                "Evento",
                "Data rappresentazione",
                "Sala",
                "Data prenotazione",
                "Sigla Scuola",
                "Nome Scuola",
                "Comune Scuola",
                "Ordine Scuola",
                "Classe",
                "Insegnante",
                "Privato",
                "e-mail referente",
                "tel. referente",
                "e-mail scuola",
                "n.interi",
                "n.ridotti",
                "n.disabili",
                "n.accomp.",
                "importo interi",
                "importo ridotti",
                "importo disabili",
                "importo accomp.",
                "modo pagamento",
                "scad. pagamento",
                "pag. confermato",
                "pag. ricevuto",
                "importo pagato",
                "tipo ricevuta",
                "confermata",
                "data conferma",
                "congelata",


        };
        return titles;
    }

    @Override
    public Object[] getExportValues(EntityItem<Prenotazione> source) {

        // uso variabile di istanza perché vi accede anche un altro metodo
        // ma la ricreo ogni volta qui
        values = new ArrayList<>();
        Prenotazione pren = source.getEntity();


        // che figata le Lambda!!
        // concateno le chiamate senza temere che falliscano
        // creo dei runnable e li invio al metodo add che gestisce l'errore
        add(() -> values.add(pren.getNumPrenotazione()));
        add(() -> values.add(pren.getRappresentazione().getEvento().getTitolo()));
        add(() -> values.add(pren.getRappresentazione().getDataRappresentazione()));
        add(() -> values.add(pren.getRappresentazione().getSala().getNome()));
        add(() -> values.add(pren.getDataPrenotazione()));
        add(() -> values.add(pren.getScuola().getSigla()));
        add(() -> values.add(pren.getScuola().getNome()));
        add(() -> values.add(pren.getScuola().getComune().getNome()));
        add(() -> values.add(pren.getScuola().getOrdine().getSigla()));
        add(() -> values.add(pren.getClasse()));
        add(() -> values.add(pren.getInsegnante().getCognome()+" "+pren.getInsegnante().getNome()));
        add(() -> values.add(pren.isPrivato()));
        add(() -> values.add(pren.getEmailRiferimento()));
        add(() -> values.add(pren.getTelRiferimento()));
        add(() -> values.add(pren.getScuola().getEmail()));
        add(() -> values.add(pren.getNumInteri()));
        add(() -> values.add(pren.getNumRidotti()));
        add(() -> values.add(pren.getNumDisabili()));
        add(() -> values.add(pren.getNumAccomp()));
        add(() -> values.add(pren.getImportoIntero()));
        add(() -> values.add(pren.getImportoRidotto()));
        add(() -> values.add(pren.getImportoDisabili()));
        add(() -> values.add(pren.getImportoAccomp()));
        add(() -> values.add(pren.getModoPagamento().getSigla()));
        add(() -> values.add(pren.getScadenzaPagamento()));
        add(() -> values.add(pren.isPagamentoConfermato()));
        add(() -> values.add(pren.isPagamentoRicevuto()));
        add(() -> values.add(pren.getImportoPagato()));
        add(() -> values.add(pren.getTipoRicevuta().getSigla()));
        add(() -> values.add(pren.isConfermata()));
        add(() -> values.add(pren.getDataConferma()));
        add(() -> values.add(pren.isCongelata()));




        return values.toArray(new Object[0]);
    }

    /**
     * Esegue il runnable che aggiunge il valore
     * Se fallisce aggiunge un null
     * così non perdo la sincronizzazione
     */
    private void add(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            values.add(null);
        }
    }

}
