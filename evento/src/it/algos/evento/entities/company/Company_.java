package it.algos.evento.entities.company;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Company.class)
public class Company_ extends BaseEntity_ {
	public static volatile SingularAttribute<Company, String> name;
	public static volatile SingularAttribute<Company, String> email;
	public static volatile SingularAttribute<Company, String> companyCode;
	public static volatile SingularAttribute<Company, String> address1;
	public static volatile SingularAttribute<Company, String> address2;
	public static volatile SingularAttribute<Company, String> contact;
	public static volatile SingularAttribute<Company, String> contractType;
	public static volatile SingularAttribute<Company, Date> contractStart;
	public static volatile SingularAttribute<Company, Date> contractEnd;
}// end of entity class
