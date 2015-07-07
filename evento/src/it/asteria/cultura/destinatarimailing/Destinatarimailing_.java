package it.asteria.cultura.destinatarimailing;

import it.algos.evento.multiazienda.EventoEntity_;
import it.asteria.cultura.mailing.Mailing;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Destinatarimailing.class)
public class Destinatarimailing_ extends EventoEntity_ {
    public static volatile SingularAttribute<Destinatarimailing, Mailing> mailing;
    public static volatile SingularAttribute<Destinatarimailing, String> indirizzo;
    public static volatile SingularAttribute<Destinatarimailing, Date> dataSpedizione;
    public static volatile SingularAttribute<Mailing, Boolean> spedita;
}// end of entity class
