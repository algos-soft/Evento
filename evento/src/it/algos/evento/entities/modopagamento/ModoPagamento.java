package it.algos.evento.entities.modopagamento;

import it.algos.evento.multiazienda.EventoEntity;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class ModoPagamento extends EventoEntity {

	private static final long serialVersionUID = 3897134530007559731L;

	@NotEmpty
	@Size(min = 2, max = 30)
	private String sigla;

	@NotEmpty
	private String descrizione;


	public ModoPagamento() {
		this("","");
	}// end of constructor

	public ModoPagamento(String sigla, String descrizione) {
		super();
		this.setSigla(sigla);
		this.setDescrizione(descrizione);
	}// end of constructor

	@Override
	public String toString() {
		return getSigla()+" "+getDescrizione();
	}// end of method

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}// end of entity class
