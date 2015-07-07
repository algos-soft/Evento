#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

#parse("Header.java")

import it.algos.web.entity.BaseEntity;
import it.algos.web.query.AQuery;

import java.util.ArrayList;
import javax.persistence.Entity;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class ${NAME} extends BaseEntity {

    #if (${Campo_stringa_minuscolo} && ${Campo_stringa_minuscolo} != "")
	@NotEmpty
	private String ${Campo_stringa_minuscolo} = "";
    #end
	
	#if (!${Campo_stringa_minuscolo} || ${Campo_stringa_minuscolo} == "")
	public ${NAME}() {
		super();
	}// end of constructor
    #end


    #if (${Campo_stringa_minuscolo} && ${Campo_stringa_minuscolo} != "")
	public ${NAME}() {
		this("");
	}// end of constructor

	public ${NAME}(String ${Campo_stringa_minuscolo}) {
		super();
		this.set${Campo_stringa_maiuscolo}(${Campo_stringa_minuscolo});
	}// end of constructor
    #end

    #if (${Campo_stringa_minuscolo} && ${Campo_stringa_minuscolo} != "")
	@Override
	public String toString() {
		return ${Campo_stringa_minuscolo};
	}// end of method
    #end

    #if (${Campo_stringa_minuscolo} && ${Campo_stringa_minuscolo} != "")
	/**
	 * @return the ${Campo_stringa_minuscolo}
	 */
	public String get${Campo_stringa_maiuscolo}() {
		return ${Campo_stringa_minuscolo};
	}
    #end

    #if (${Campo_stringa_minuscolo} && ${Campo_stringa_minuscolo} != "")
	/**
	 * @param ${Campo_stringa_minuscolo}
	 *            the ${Campo_stringa_minuscolo} to set
	 */
	public void set${Campo_stringa_maiuscolo}(String ${Campo_stringa_minuscolo}) {
		this.${Campo_stringa_minuscolo} = ${Campo_stringa_minuscolo};
	}
    #end

	/**
	 * Recupera una istanza di ${NAME} usando la query specifica
	 * 
	 * @return istanza di ${NAME}, null se non trovata
	 */
	public static ${NAME} find(long id) {
		${NAME} instance = null;
		BaseEntity entity = AQuery.queryById(${NAME}.class, id);

		if (entity != null) {
			if (entity instanceof ${NAME}) {
				instance = (${NAME}) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

    #if (${Campo_stringa_minuscolo} && ${Campo_stringa_minuscolo} != "")
	/**
	 * Recupera una istanza di ${NAME} usando la query specifica
	 * 
	 * @return istanza di ${NAME}, null se non trovata
	 */
	public static ${NAME} find(String ${Campo_stringa_minuscolo}) {
		${NAME} instance = null;
		BaseEntity entity = AQuery.queryOne(${NAME}.class, ${NAME}_.${Campo_stringa_minuscolo}, ${Campo_stringa_minuscolo});

		if (entity != null) {
			if (entity instanceof ${NAME}) {
				instance = (${NAME}) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method
    #end

    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(${NAME}.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    public synchronized static ArrayList<${NAME}> findAll() {
        return (ArrayList<${NAME}>) AQuery.findAll(${NAME}.class);
    }// end of method

     @Override
     public ${NAME} clone() throws CloneNotSupportedException {
        try {
            return (${NAME}) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
	}// end of method

}// end of entity class
