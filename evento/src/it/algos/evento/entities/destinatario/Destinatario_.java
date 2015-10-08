package it.algos.evento.entities.destinatario;

import it.algos.evento.multiazienda.EventoEntity_;
import it.algos.evento.entities.mailing.Mailing;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Destinatario.class)
public class Destinatario_ extends EventoEntity_ {
    public static volatile SingularAttribute<Destinatario, Mailing> mailing;
    public static volatile SingularAttribute<Destinatario, String> indirizzo;
    public static volatile SingularAttribute<Destinatario, Date> dataSpedizione;
    public static volatile SingularAttribute<Mailing, Boolean> spedita;
}// end of entity class
