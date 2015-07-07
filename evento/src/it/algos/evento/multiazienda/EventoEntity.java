package it.algos.evento.multiazienda;

import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;
import it.algos.web.entity.BaseEntity;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuppressWarnings("serial")
public abstract class EventoEntity extends BaseEntity {

	/**
	 * Gli oggetti di questa classe e sottoclassi vengono sempre costruiti con
	 * l'azienda corrente
	 */
	public EventoEntity() {
		super();
		Company company = EventoSession.getCompany();
		setCompany(company);

	}

	@ManyToOne
	// @NotNull - NotNull l'ho dovuto togliere, se no da' constraint violation
	// anche quando non è nullo (???) 28 nov 2014
	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}



}
