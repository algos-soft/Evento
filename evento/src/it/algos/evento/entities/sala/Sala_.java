package it.algos.evento.entities.sala;

import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Sala.class)
public class Sala_ extends EventoEntity_ {
	public static volatile SingularAttribute<Sala, String> nome;
	public static volatile SingularAttribute<Sala, Integer> capienza;
}// end of entity class
