#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

#parse("Header.java")

import it.algos.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(${NAME}.class)
public class ${NAME}_ extends BaseEntity_ {
	public static volatile SingularAttribute<${NAME}, String> ${Campo_stringa_minuscolo};
}// end of entity class
