package it.algos.evento.entities.spedizione;

import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.multiazienda.EventoEntity_;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Spedizione_ extends EventoEntity_ {
	public static volatile SingularAttribute<Spedizione, Lettera> lettera;
	public static volatile SingularAttribute<Spedizione, String> destinatario;
	public static volatile SingularAttribute<Spedizione, String> operatore;
	public static volatile SingularAttribute<Spedizione, Boolean> spedita;
	public static volatile SingularAttribute<Spedizione, Date> dataSpedizione;
	public static volatile SingularAttribute<Spedizione, String> errore;
}// end of entity class
