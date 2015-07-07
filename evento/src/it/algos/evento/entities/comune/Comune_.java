package it.algos.evento.entities.comune;

import it.algos.evento.multiazienda.EventoEntity_;
import it.algos.evento.entities.evento.Evento;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Comune.class)
public class Comune_ extends EventoEntity_ {
	public static volatile SingularAttribute<Evento, String> nome;
	public static volatile SingularAttribute<Evento, String> siglaProvincia;
}// end of entity class
