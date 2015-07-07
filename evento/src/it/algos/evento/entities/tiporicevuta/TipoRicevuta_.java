package it.algos.evento.entities.tiporicevuta;

import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by alex on 30-05-2015.
 */
public class TipoRicevuta_ extends EventoEntity_ {
    public static volatile SingularAttribute<TipoRicevuta, String> sigla;
    public static volatile SingularAttribute<TipoRicevuta, Integer> descrizione;
}
