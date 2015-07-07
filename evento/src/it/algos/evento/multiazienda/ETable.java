package it.algos.evento.multiazienda;

import it.algos.web.entity.BaseEntity_;
import it.algos.web.entity.EM;
import it.algos.web.module.ModulePop;
import it.algos.web.table.ATable;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;

/**
 * Una table con JPAContainer già filtrato sulla Company corrente
 */
@SuppressWarnings("serial")
public class ETable extends ATable{

	public ETable(ModulePop module) {
		super(module);
	}
	
	public ETable(Class<?> entityClass) {
		super(entityClass);
	}

	/**
	 * Creates the container
	 * <p>
	 * 
	 * @return un container RW filtrato sulla azienda corrente
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Container createContainer() {
		Class<EventoEntity> entityClass = (Class<EventoEntity>)getEntityClass();
		ERWContainer cont = new ERWContainer(entityClass, EM.createEntityManager());
		return cont;
	}// end of method


	/**
	 * Ritorna il numero di record di competenza della azienda corrente
	 * presenti nella domain class di questa tabella
	 */
	@SuppressWarnings("unchecked")
	public long getTotalRows() {
		Class<EventoEntity> entityClass = (Class<EventoEntity>)getEntityClass();
		return EQuery.getCount(entityClass);
	}// end of method

}
