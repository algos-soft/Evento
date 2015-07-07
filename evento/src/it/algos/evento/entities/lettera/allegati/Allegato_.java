package it.algos.evento.entities.lettera.allegati;

import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Allegato.class)
public class Allegato_ extends EventoEntity_ {
	public static volatile SingularAttribute<Allegato_, String> name;
	public static volatile SingularAttribute<Allegato_, byte[]> content;
	public static volatile SingularAttribute<Allegato_, String> mimeType;
	public static volatile SingularAttribute<Allegato_, Long> bytes;
}
