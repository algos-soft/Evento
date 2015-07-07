package it.asteria.cultura.mailing;

import it.algos.evento.multiazienda.EventoEntity;
import it.algos.web.lib.LibDate;
import it.algos.evento.entities.lettera.Lettera;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Mailing extends EventoEntity {

//    public static EventoEntityQuery<Mailing> query = new EventoEntityQuery(Mailing.class);

    @Size(min = 2, max = 80)
    private String titolo = "";

    @NotNull
    private Lettera lettera = null;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCreazione= LibDate.today();

    public Mailing() {
        this("", null, LibDate.today());
    }// end of constructor

    public Mailing(String titolo, Lettera lettera, Date dataCreazione) {
        super();
        this.setTitolo(titolo);
        this.setLettera(lettera);
        this.setDataCreazione(dataCreazione);
    }// end of constructor

    @Override
    public String toString() {
        return getTitolo();
    }// end of method


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Lettera getLettera() {
        return lettera;
    }

    public void setLettera(Lettera lettera) {
        this.lettera = lettera;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
}// end of entity class
