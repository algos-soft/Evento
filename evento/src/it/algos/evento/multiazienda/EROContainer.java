package it.algos.evento.multiazienda;

import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import it.algos.evento.EventoSession;
import it.algos.evento.entities.company.Company;

import javax.persistence.EntityManager;

/**
 * Read-Only JPAContainer automatically filtered on a Company.
 */
@SuppressWarnings("serial")
public class EROContainer extends EContainer{


	/**
	 * Read-Only JPAContainer automatically filtered on a given company.
	 * <p>
	 * @param entityClass - the entity class
	 * @param manager - the entity admin
	 * @param company - the company
	 */
	public EROContainer (Class entityClass, EntityManager manager, Company company) {
		super(entityClass, new LocalEntityProvider<EventoEntity>(entityClass, manager), company);
	}
	
	
	/**
	 * Read-Only JPAContainer automatically filtered on the current company
	 * <p>
	 * @param entityClass - the entity class
	 * @param manager - the entity admin
	 */
	public EROContainer (Class entityClass, EntityManager manager) {
		this(entityClass, manager, EventoSession.getCompany());
	}


}
