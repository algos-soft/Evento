package it.asteria.cultura.destinatario;

import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.multiazienda.EventoEntityQuery;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import it.asteria.cultura.mailing.Mailing;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

@Entity
public class Destinatario extends EventoEntity {

    @NotNull
    private Mailing mailing = null;

    @NotEmpty
    private String indirizzo = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSpedizione;

    private boolean spedita;

    public static EventoEntityQuery<Destinatario> query = new EventoEntityQuery(Destinatario.class);

    public Destinatario() {
        this(null, "", null,false);
    }// end of constructor

    public Destinatario(Mailing mailing, String indirizzo, Date dataSpedizione,boolean spedita) {
        super();
        this.setMailing(mailing);
        this.setIndirizzo(indirizzo);
        this.setDataSpedizione(dataSpedizione);
        this.setSpedita(spedita);
    }// end of constructor

    @Override
    public String toString() {
        return getIndirizzo();
    }// end of method

    public Mailing getMailing() {
        return mailing;
    }

    public void setMailing(Mailing mailing) {
        this.mailing = mailing;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Date getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(Date dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public boolean isSpedita() {
        return spedita;
    }

    public void setSpedita(boolean spedita) {
        this.spedita = spedita;
    }
}// end of entity class
