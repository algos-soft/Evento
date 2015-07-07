package it.algos.evento.pref;

import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;

public class PrefEventoEntity_ extends EventoEntity_ {
	public static volatile SingularAttribute<PrefEventoEntity, String> code;
	public static volatile SingularAttribute<PrefEventoEntity, byte[]> value;
}// end of entity class
