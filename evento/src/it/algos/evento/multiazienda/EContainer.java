package it.algos.evento.multiazienda;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.company.Company;

/**
 * JPAContainer automatically filtered on a company.
 */
@SuppressWarnings("serial")
public abstract class EContainer extends JPAContainer<EventoEntity> {

	private static Company company;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EContainer(Class entityClass, EntityProvider entityProvider, Company company) {
		super(entityClass);
		EContainer.company=company;
		setEntityProvider(entityProvider);
		addContainerFilter(createCompanyFilter());
	}



	private static Filter createCompanyFilter() {
		return new Compare.Equal(EventoEntity_.company.getName(), company);
	}

	/**
	 * Restores the company filter after removing all the filters
	 */
	@Override
	public void removeAllContainerFilters() {
		super.removeAllContainerFilters();
		addContainerFilter(createCompanyFilter());
	}
	
	
}
