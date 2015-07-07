package it.algos.evento.entities.prenotazione.eventi;

import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.entities.prenotazione.Prenotazione;

import java.util.Date;

import javax.persistence.*;

@Entity
public class EventoPren extends EventoEntity {

	private static final long serialVersionUID = -4029444656977204712L;
	
//	@ManyToOne(cascade={CascadeType.ALL})
//    @JoinColumn(name="pippo", nullable=false)
//	@ManyToOne(cascade={CascadeType.PERSIST})
	@ManyToOne
	private Prenotazione prenotazione;

	//@NotNull
	private int tipo;

	@Temporal(TemporalType.TIMESTAMP)
	//@NotNull
	private Date timestamp;

	private String user;
	
	private String dettagli;

	private boolean invioEmail;
	
	private boolean emailInviata;

	
	public EventoPren() {
		super();
	}// end of constructor

	public EventoPren(Prenotazione prenotazione, int tipo, Date timestamp, String user, String dettagli) {
		super();
		this.prenotazione = prenotazione;
		this.tipo = tipo;
		this.timestamp = timestamp;
		this.user = user;
		this.dettagli = dettagli;
	}

	public Prenotazione getPrenotazione() {
		return prenotazione;
	}

	public void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazione = prenotazione;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipoEvento) {
		this.tipo = tipoEvento;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDettagli() {
		return dettagli;
	}

	public void setDettagli(String dettagli) {
		this.dettagli = dettagli;
	}

	public boolean isInvioEmail() {
		return invioEmail;
	}

	public void setInvioEmail(boolean invioEmail) {
		this.invioEmail = invioEmail;
	}

	public boolean isEmailInviata() {
		return emailInviata;
	}

	public void setEmailInviata(boolean flag) {
		this.emailInviata = flag;
	}
	
	

}// end of entity class
