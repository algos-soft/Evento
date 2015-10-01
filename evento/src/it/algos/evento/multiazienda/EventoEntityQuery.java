package it.algos.evento.multiazienda;

import com.vaadin.data.Container.Filter;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

/**
 * Query helper class typed on a specific EventoEntity type.
 * <p>
 * Routes the queries to the EQuery class adding the type paramenetr and casting
 * the return type.<br>
 * Used to perform queries directly on the Entity classes.<br>
 * 
 * Usage: 1) add a static variable to the entity class<br>
 * <code>public static EventoEntityQuery<MyClass> query = new EventoEntityQuery(MyClass.class);</code>
 * 2) perform the query like this:<br>
 * <code>List<MyClass> entities = MyClass.query.queryList(MyClass_.myField, aValue);</code>
 */
public class EventoEntityQuery <T extends EventoEntity>{

	final Class<EventoEntity> type;

	public EventoEntityQuery(Class<EventoEntity> type) {
		this.type = type;
	}

	public List<T> queryList(SingularAttribute attr, Object value) {
		return (List<T>) EQuery.queryList(type, attr, value);
	}

	public T queryFirst(SingularAttribute attr, Object value) {
		return (T) EQuery.queryFirst(type, attr, value);
	}
	
	public T queryOne(SingularAttribute attr, Object value) {
		return (T) EQuery.queryOne(type, attr, value);
	}
	
	public long getCount() {
		return EQuery.getCount(type);
	}

	public List<T> getList() {
		return (List<T>) EQuery.getList(type);
	}

	public List<T> getList(Filter... filters) {
		return (List<T>) EQuery.getList(type, filters);
	}

	public T getEntity(Filter... filters) {
		return (T) EQuery.getEntity(type, filters);
	}

}
