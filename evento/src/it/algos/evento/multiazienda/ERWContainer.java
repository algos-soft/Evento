package it.algos.evento.multiazienda;

import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;
import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;

import javax.persistence.EntityManager;

/**
 * Read-Write JPAContainer automatically filtered.
 */

@SuppressWarnings("serial")
public class ERWContainer extends EContainer{

	/**
	 * Read-Write JPAContainer automatically filtered on a given company.
	 * <p>
	 * @param entityClass - the entity class
	 * @param manager - the entity manager
	 * @param company - the company
	 */
	public ERWContainer(Class entityClass, EntityManager manager, Company company) {
		super(entityClass, new MutableLocalEntityProvider<EventoEntity>(entityClass, manager), company);
	}
	
	/**
	 * Read-Write JPAContainer automatically filtered on the current company
	 * <p>
	 * @param entityClass - the entity class
	 * @param manager - the entity manager
	 */
	public ERWContainer(Class entityClass, EntityManager manager) {
		this(entityClass, manager, EventoSession.getCompany());
	}

	
}
