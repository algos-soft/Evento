package it.algos.evento.entities.rappresentazione;

import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.multiazienda.EventoEntity;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Rappresentazione extends EventoEntity {

    private static final long serialVersionUID = -3267255652926186175L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd-MM-yyyy HH:mm");

    @ManyToOne
    @NotNull
    private Evento evento;

    @ManyToOne
    @NotNull
    private Sala sala;

    private int capienza;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataRappresentazione;

    private String note;

    @ManyToMany
    // @CascadeOnDelete mette la constraint ON DELETE CASCADE sulla tabella di incrocio
    // - cancellando One Record cancella automaticamente i record nella tabella di incrocio
    // - non cancella i record nella tabella di destinazione
    // HA EFFETTO NEL DDL (CREAZIONE DATABASE)
    @CascadeOnDelete
    private List<Insegnante> insegnanti;


    @OneToMany(mappedBy = "rappresentazione")
    @CascadeOnDelete
    private List<Prenotazione> prenotazioni;


    public Rappresentazione() {
        super();
    }// end of constructor

    public Rappresentazione(Evento evento, Sala sala, Date dataRappresentazione) {
        super();
        setEvento(evento);
        setSala(sala);
        setDataRappresentazione(dataRappresentazione);
    }// end of constructor

    /**
     * Ritorna una data nel formato adeguato per una Rappresentazione (nomegiorno-giorno-mese-ammo-ora-minuto)
     */
    public static String getDateAsString(Date date) {
        String stringa = "";
        if (date != null) {
            stringa = dateFormat.format(date);
        }
        return stringa;
    }// end of method

    /**
     * Recupera la rappresentazione usando la query specifica
     *
     * @return la rappresentazione, null se non trovato
     */
    public static Rappresentazione read(Object id) {
        Rappresentazione instance = null;
        BaseEntity entity = AQuery.queryById(Rappresentazione.class, id);

        if (entity != null) {
            if (entity instanceof Rappresentazione) {
                instance = (Rappresentazione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    @Override
    public String toString() {
        return getEvento().toString() + " - " + getDateAsString(getDataRappresentazione());
    }// end of method

    /**
     * Ritorna una data nel formato adeguato per una Rappresentazione (nomegiorno-giorno-mese-ammo-ora-minuto)
     */
    public String getDateAsString() {
        return Rappresentazione.getDateAsString(dataRappresentazione);
    }// end of method

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public Date getDataRappresentazione() {
        return dataRappresentazione;
    }

    public void setDataRappresentazione(Date dataRappresentazione) {
        this.dataRappresentazione = dataRappresentazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Insegnante> getInsegnanti() {
        return insegnanti;
    }

    public void setInsegnanti(List<Insegnante> insegnanti) {
        this.insegnanti = insegnanti;
    }


//	/**
//	 * Ritorna l'importo totale di una prenotazione per una data rappresentazione
//	 * @deprecated
//	 * @see Prenotazione
//	 * <p>
//	 *
//	 * @param rappresentazione
//	 *            la rappresentazione
//	 * @param nInteri
//	 *            il numero di posti interi
//	 * @param nRidotti
//	 *            il numero di posti ridotti
//	 * @return l'importo totale
//	 */
//	public static BigDecimal getTotImporto(Rappresentazione rappresentazione, int nInteri, int nRidotti) {
//		BigDecimal totImporto = new BigDecimal(0);
//		if (rappresentazione != null) {
//			Evento evento = rappresentazione.getEvento();
//			if (evento != null) {
//				BigDecimal iIntero = evento.getImportoIntero();
//				BigDecimal iRidotto = evento.getImportoRidotto();
//
//				BigDecimal totInteri = iIntero.multiply(new BigDecimal(nInteri));
//				BigDecimal totRidotti = iRidotto.multiply(new BigDecimal(nRidotti));
//
//				totImporto = totInteri.add(totRidotti);
//			}
//		}
//		return totImporto;
//	}// end of method

}// end of entity class
