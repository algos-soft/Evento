package it.algos.evento.multiazienda;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.company.Company;
import it.algos.evento.lib.EventoSessionLib;

import javax.persistence.EntityManager;

/**
 * JPAContainer automatically filtered on a company.
 */
@SuppressWarnings("serial")
public class EJPAContainer extends JPAContainer<EventoEntity> {

    private static Company company;

    /**
     * Create a container filtered on a given company.
     *
     * @param entityClass the entity class
     * @param manager     the entity manager
     * @param company     the company on which to filter
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EJPAContainer(Class entityClass, EntityManager manager, Company company) {
        super(entityClass);
        EJPAContainer.company = company;
        EntityProvider entityProvider = new LocalEntityProvider<EventoEntity>(entityClass, manager);
        setEntityProvider(entityProvider);
        addContainerFilter(createCompanyFilter());
    }

    /**
     * Create a container filtered on the current Company.
     *
     * @param entityClass the entity class
     * @param manager     the entity manager
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EJPAContainer(Class entityClass, EntityManager manager) {
        this(entityClass, manager, EventoSessionLib.getCompany());
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
