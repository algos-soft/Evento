package it.algos.evento.multiazienda;

import com.vaadin.data.Container;
import it.algos.evento.entities.company.Company;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.ModuleTable;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

/**
 * Una ModuleTable con Container già filtrato sulla Company corrente
 */
@SuppressWarnings("serial")
public class ETable extends ModuleTable{

	public ETable(ModulePop module) {
		super(module);
	}
	
//	public ETable(Class<?> entityClass) {
//		super(entityClass);
//	}

	/**
	 * Creates the container
	 * <p>
	 * 
	 * @return un container RW filtrato sulla azienda corrente
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Container createContainer() {
		Class<EventoEntity> entityClass = (Class<EventoEntity>)getEntityClass();
		Company company = EventoSessionLib.getCompany();
		ELazyContainer entityContainer = new ELazyContainer(getEntityManager(), entityClass, getContainerPageSize() , company);
		return entityContainer;
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
