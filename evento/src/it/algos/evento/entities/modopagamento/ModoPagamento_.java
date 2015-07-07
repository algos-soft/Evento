package it.algos.evento.entities.modopagamento;

import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ModoPagamento.class)
public class ModoPagamento_ extends EventoEntity_ {
	public static volatile SingularAttribute<ModoPagamento_, String> sigla;
	public static volatile SingularAttribute<ModoPagamento_, String> descrizione;
}// end of entity class
