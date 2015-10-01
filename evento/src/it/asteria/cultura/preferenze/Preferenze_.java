package it.asteria.cultura.preferenze;

import it.algos.webbase.web.entity.BaseEntity_;
import it.asteria.cultura.preferenze.Preferenze.Type;

import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.Date;

public class Preferenze_ extends BaseEntity_ {
	public static volatile SingularAttribute<Preferenze, Type> type;
	public static volatile SingularAttribute<Preferenze, String> code;
	public static volatile SingularAttribute<Preferenze, String> stringa;
	public static volatile SingularAttribute<Preferenze, Boolean> bool;
	public static volatile SingularAttribute<Preferenze, Integer> intero;
	public static volatile SingularAttribute<Preferenze, Date> date;
	public static volatile SingularAttribute<Preferenze, BigDecimal> decimal;
	public static volatile SingularAttribute<Preferenze, byte[]> bytes;
}// end of entity class
