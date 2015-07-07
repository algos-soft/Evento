package it.asteria.cultura.mailing;

import it.algos.evento.multiazienda.EventoEntity_;
import it.asteria.cultura.destinatarimailing.Destinatarimailing;
import it.algos.evento.entities.lettera.Lettera;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Mailing.class)
public class Mailing_ extends EventoEntity_ {
    public static volatile SingularAttribute<Mailing, String> titolo;
    public static volatile SingularAttribute<Mailing, Lettera> lettera;
    public static volatile SingularAttribute<Destinatarimailing, Date> dataCreazione;
}// end of entity class
