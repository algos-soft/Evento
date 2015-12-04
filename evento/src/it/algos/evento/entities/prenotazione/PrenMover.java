package it.algos.evento.entities.prenotazione;

import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.RappresentazioneModulo;

import java.util.ArrayList;

/**
 * Oggetto responsabile di verificare la fattibilità di uno spostamento
 * di prenotazioni e di eseguire l'operazione vera e propria.
 */
class PrenMover {
    private Prenotazione[] aPren;
    private ArrayList<String> warningRows = new ArrayList();
    private ArrayList<String> errorRows = new ArrayList();
    private ArrayList<String> infoRows = new ArrayList();
    private int totPersoneSpostate;
    private Rappresentazione destRapp;

    /**
     * Costruttore.
     *
     * @param aPren    l'array delle prenotazioni da spostare
     * @param destRapp la rappresentazione di destinazione
     */
    public PrenMover(Prenotazione[] aPren, Rappresentazione destRapp) {
        this.aPren = aPren;
        this.destRapp = destRapp;

        // processa le singole prenotazioni
        for (Prenotazione pren : aPren) {
            checkPren(pren);

            // incrementa il totale delle persone che verrebero spostate
            if (!pren.isCongelata()) {
                totPersoneSpostate += pren.getNumTotali();
            }
        }

        // processa l'operazione a livello generale
        checkOp();
    }

    /**
     * Processa una prenotazione e genera le righe di warning o di errore
     */
    private void checkPren(Prenotazione pren) {

        // controllo che la prenotazione non faccia già parte della rappresentazione destinazione
        if (pren.getRappresentazione().equals(destRapp)) {
            errorRows.add(pren + " è già nella rappresentazione selezionata.");
        }


    }

    /**
     * Controlla l'operazione a livello generale
     */
    private void checkOp() {

        // controlla che numero di persone totali dopo lo spostamento
        // non ecceda la capienza della sala
        int numPersoneDopo = RappresentazioneModulo.getPostiPrenotati(destRapp) + totPersoneSpostate;
        int capienza = destRapp.getCapienza();
        if (numPersoneDopo > capienza) {
            String warn = "Dopo lo spostamento, la capienza massima sarà superata (max=" + capienza + ", tot=" + numPersoneDopo+")";
            warningRows.add(warn);
        }


    }


    /**
     * Ritorna la stringa di preview in formato html
     */
    public String getHTMLText() {
        String s="";

        // righe di errpre
        for (String row : errorRows){
            if (!s.equals("")){
                s+="<br>";
            }
            s+="ERR: "+row;
        }

        // righe di warning
        for (String row : warningRows){
            if (!s.equals("")){
                s+="<br>";
            }
            s+=row;
        }


        return s;
    }

    /**
     * @return true se l'operazione è effettuabile
     */
    boolean isEffettuabile() {
        return (errorRows.size() == 0);
    }

}
