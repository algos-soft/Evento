package it.algos.evento.multiazienda;

import it.algos.evento.entities.company.Company;
import it.algos.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(EventoEntity.class)
public abstract class EventoEntity_ extends BaseEntity_{
	
	public static volatile SingularAttribute<EventoEntity, Company> company;

}// end of class
