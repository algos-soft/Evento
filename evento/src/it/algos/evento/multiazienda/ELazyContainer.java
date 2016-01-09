package it.algos.evento.multiazienda;

import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.company.Company;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.webbase.web.entity.BaseEntity_;
import org.vaadin.addons.lazyquerycontainer.LazyEntityContainer;

import javax.persistence.EntityManager;

/**
 * LazyEntityContainer automatically filtered on a company.
 */
public class ELazyContainer extends LazyEntityContainer {

    private static Company company;

    /**
     * Create a container filtered on a given company.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     * @param batchSize     the paging size
     * @param company       the company on which to filter
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass, int batchSize, Company company) {
        super(entityManager, entityClass, batchSize, BaseEntity_.id.getName(), true, true, true);
        ELazyContainer.company = company;

        // automatically add the id property
        addContainerProperty(BaseEntity_.id.getName(), Long.class, 0L, true, true);

        // add the company filter
        addContainerFilter(createCompanyFilter());
    }

    /**
     * Create a container filtered on the current Company.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     * @param batchSize     the paging size
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass, int batchSize) {
        this(entityManager, entityClass, batchSize, EventoSessionLib.getCompany());
    }

    /**
     * Create a container with a default paging size.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     * @param company       the company on which to filter
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass, Company company) {
        this(entityManager, entityClass, 1000, company);
    }

    /**
     * Create a container with a default paging size and filtered on the current company.
     *
     * @param entityManager the entity manager
     * @param entityClass   the entity class
     */
    public ELazyContainer(EntityManager entityManager, Class entityClass) {
        this(entityManager, entityClass, EventoSessionLib.getCompany());
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
