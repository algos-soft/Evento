package it.algos.evento.entities.ordinescuola;

import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by alex on 30-05-2015.
 */
public class OrdineScuola_ extends EventoEntity_ {
    public static volatile SingularAttribute<OrdineScuola, String> sigla;
    public static volatile SingularAttribute<OrdineScuola, String> descrizione;
}
