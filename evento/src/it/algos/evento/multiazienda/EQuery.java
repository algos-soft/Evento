package it.algos.evento.multiazienda;

import it.algos.evento.EventoApp;
import it.algos.evento.EventoSession;
import it.algos.evento.EventoUI;
import it.algos.web.entity.BaseEntity;
import it.algos.web.entity.EM;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;

/**
 * Utility methods for FILTERED queries.
 * <p>
 * The results of these methods are always filtered on the current Company.
 */
public class EQuery {

	/**
	 * Ritorna il numero di record di competenza della azienda corrente
	 * presenti nella domain class specificata
	 * @param c la domain class
	 * @return il numero di record
	 */
	public static long getCount(Class<? extends EventoEntity> c) {
		long count = 0;
		EntityManager manager = EM.createEntityManager();
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EventoEntity> root = (Root<EventoEntity>) cq.from(c);
		//Predicate predicate = cb.equal(root.get(EventoEntity_.company), EventoApp.COMPANY);
		Predicate predicate = cb.equal(root.get(EventoEntity_.company), EventoSession.getCompany());
		cq.where(predicate);
		CriteriaQuery<Long> select = cq.select(cb.count(root));
		TypedQuery<Long> typedQuery = manager.createQuery(select);
		count = typedQuery.getSingleResult();
		manager.close();
		return count;
	}// end of method

	
	/**
	 * Search for all entities with a specified attribute value.
	 * Filtrato sulla azienda corrente.
	 * <p>
	 * 
	 * @param clazz
	 *            the entity class
	 * @param attr
	 *            the searched attribute
	 * @param value
	 *            the value to search for
	 * @return a list of entities corresponding to the specified criteria
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<? extends EventoEntity> queryList(Class<? extends EventoEntity> clazz, SingularAttribute attr,
			Object value) {
		EntityManager manager = EM.createEntityManager();
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<? extends BaseEntity> cq = cb.createQuery(clazz);
		Root<EventoEntity> root = (Root<EventoEntity>) cq.from(clazz);
		
		Predicate pred;
	    List<Predicate> predicates = new ArrayList<Predicate>();
		
	    pred = cb.equal(root.get(attr), value);
		predicates.add(pred);
		
		//pred = cb.equal(root.get(EventoEntity_.company), EventoApp.COMPANY);
		pred = cb.equal(root.get(EventoEntity_.company), EventoSession.getCompany());
		predicates.add(pred);

		cq.where(predicates.toArray(new Predicate[]{}));
		
		TypedQuery<? extends BaseEntity> query = manager.createQuery(cq);
		List<EventoEntity> entities = (List<EventoEntity>)query.getResultList();
		manager.close();

		return entities;
	}
	
	
	/**
	 * Search for the only entity with a specified attribute value.
	 * <p>
	 * If multiple entities exist, null is returned.
	 * 
	 * @param clazz
	 *            the entity class
	 * @param attr
	 *            the searched attribute
	 * @param value
	 *            the value to search for
	 * @returnthe the only entity corresponding to the specified criteria, or null
	 */
	@SuppressWarnings({ "rawtypes" })
	public static BaseEntity queryOne(Class<? extends EventoEntity> clazz, SingularAttribute attr, Object value) {
		BaseEntity bean = null;
		List<? extends EventoEntity> entities = queryList(clazz, attr, value);
		if (entities.size() == 1) {
			bean = entities.get(0);
		}
		return bean;
	}

	/**
	 * Search for the first entities with a specified attribute value.
	 * <p>
	 * 
	 * @param clazz
	 *            the entity class
	 * @param attr
	 *            the searched attribute
	 * @param value
	 *            the value to search for
	 * @return the first entity corresponding to the specified criteria
	 */
	@SuppressWarnings({ "rawtypes" })
	public static BaseEntity queryFirst(Class<? extends EventoEntity> clazz, SingularAttribute attr, Object value) {
		BaseEntity bean = null;
		List<? extends EventoEntity> entities = queryList(clazz, attr, value);
		if (entities.size() > 0) {
			bean = entities.get(0);
		}
		return bean;
	}

	
	
	
	/**
	 * Search for the all entities 
	 * 
	 * @param clazz
	 *            the entity class
	 * @return a list of entities
	 */
	@SuppressWarnings("unchecked")
	public static List<? extends EventoEntity> getList(Class<? extends EventoEntity> clazz) {
		return getList(clazz, null);
	}// end of method

	
	/**
	 * Return a list of entities for a given domain class and filters.
	 * <p>
	 * @param entityClass - the entity class
	 * @param filters - an array of filters (you can use FilterFactory 
	 * to build filters, or create them as Compare....)
	 * @return the list with the entities found
	 */
	public static List<? extends EventoEntity> getList(Class<? extends EventoEntity> entityClass, Filter... filters) {
		ArrayList<EventoEntity> list = new ArrayList<> ();
		JPAContainer<EventoEntity> container = getContainer(entityClass, filters);
		for(Object obj : container.getItemIds()){
			EntityItem<EventoEntity> item = container.getItem(obj);
			list.add(item.getEntity());
		}
		container.getEntityProvider().getEntityManager().close();
		return list;
	}
	
	/**
	 * Return a single entity for a given domain class and filters.
	 * <p>
	 * @param entityClass - the entity class
	 * @param filters - an array of filters (you can use FilterFactory 
	 * to build filters, or create them as Compare....)
	 * @return the single (or first) entity found
	 */
	public static EventoEntity getEntity(Class<? extends EventoEntity> entityClass, Filter... filters) {
		EventoEntity entity=null;
		List<? extends EventoEntity> list=getList(entityClass, filters);
		if (list.size()>0) {
			entity=list.get(0);
		}
		return entity;
	}

	
	
	/**
	 * Create a read-only JPA container for a given domain class and filters.
	 * <p>
	 * @param entityClass - the entity class
	 * @param filters - an array of filters (you can use FilterFactory 
	 * to build filters, or create them as Compare....), null for no filters
	 * @return the JPA container
	 */
	public static JPAContainer<EventoEntity> getContainer(Class<? extends EventoEntity> entityClass, Filter... filters) {
		EntityManager manager = EM.createEntityManager();
		JPAContainer<EventoEntity> container = new EROContainer(entityClass, manager);
		if (filters!=null) {
			for (Filter filter: filters) {
				container.addContainerFilter(filter);
			}
		}
		return container;
	}


	/**
	 * Delete all the records for a given domain class
	 */
	public static void deleteAll(Class<? extends EventoEntity> entityClass) {
		
		EntityManager manager = EM.createEntityManager();
		ERWContainer cont = new ERWContainer(entityClass, manager);
		try {
			
			manager.getTransaction().begin();
			
			for (Object id : cont.getItemIds()) {
				EventoEntity entity = cont.getItem(id).getEntity();
				entity = manager.merge(entity);
			    manager.remove(entity);
			}
			
			manager.getTransaction().commit();
			
		} catch (Exception e) {
			manager.getTransaction().rollback();
		}
		manager.close();

	}// end of method

	
}

