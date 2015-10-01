package it.asteria.cultura.preferenze;

import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Preferenze extends BaseEntity {

	private static final long serialVersionUID = 5127490809011569165L;

	Type type;
	String code;

	String stringa;
	Boolean bool;
	Integer intero;

	@Temporal(TemporalType.DATE)
	Date date;

	BigDecimal decimale;

	byte[] bytes;

	public Preferenze() {
		super();
	}// end of constructor

	public enum Type {
		stringa, booleano, intero, decimal, date, bytes;

		Type() {
		}// fine del costruttore
	}// end of internal enumeration

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the stringa
	 */
	public String getStringa() {
		return stringa;
	}

	/**
	 * @param stringa
	 *            the stringa to set
	 */
	public void setStringa(String stringa) {
		this.stringa = stringa;
	}

	/**
	 * @return the bool
	 */
	public Boolean getBool() {
		return bool;
	}

	/**
	 * @param bool
	 *            the bool to set
	 */
	public void setBool(Boolean bool) {
		this.bool = bool;
	}

	/**
	 * @return the intero
	 */
	public Integer getIntero() {
		return intero;
	}

	/**
	 * @param intero
	 *            the intero to set
	 */
	public void setIntero(Integer intero) {
		this.intero = intero;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getDecimale() {
		return decimale;
	}

	public void setDecimale(BigDecimal decimal) {
		this.decimale = decimal;
	}

	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * @param bytes
	 *            the bytes to set
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public static String getStringa(String code) {
		return getStr(code);
	}// end of method

	public static String getStr(String code) {
		return getStr(code, "");
	}// end of method

	public static String getStr(String code, String suggerito) {
		String value = suggerito;
		Preferenze preferenza = null;

		if (code != null && !code.equals("") && !code.equals(" ")) {
			preferenza = getPreferenza(code);
			if (preferenza != null) {
				if (preferenza.type == Preferenze.Type.stringa) {
					value = preferenza.stringa;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		return value;
	}// end of method

	public static Boolean getBool(String code) {
		return getBool(code, false);
	}// end of method

	public static Boolean getBool(String code, Boolean suggerito) {
		Boolean value = suggerito;
		Preferenze preferenza = null;

		if (code != null && !code.equals("") && !code.equals(" ")) {
			preferenza = getPreferenza(code);
			if (preferenza != null) {
				if (preferenza.type == Preferenze.Type.booleano) {
					value = preferenza.bool;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		return value;
	}// end of method

	public static Integer getInt(String code) {
		return getInt(code, 0);
	}// end of method

	public static Integer getInt(String code, Integer suggerito) {
		Integer value = suggerito;
		Preferenze preferenza = null;

		if (code != null && !code.equals("") && !code.equals(" ")) {
			preferenza = getPreferenza(code);
			if (preferenza != null) {
				if (preferenza.type == Preferenze.Type.intero) {
					value = preferenza.intero;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		return value;
	}// end of method

	public static Date getDate(String code) {
		return getDate(code, new Date());
	}// end of method

	public static Date getDate(String code, Date suggerito) {
		Date value = suggerito;
		Preferenze preferenza = null;

		if (code != null && !code.equals("") && !code.equals(" ")) {
			preferenza = getPreferenza(code);
			if (preferenza != null) {
				if (preferenza.type == Preferenze.Type.date) {
					value = preferenza.date;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		return value;
	}// end of method

	public static BigDecimal getDecimal(String code) {
		return getDecimal(code, new BigDecimal(0));
	}// end of method

	public static BigDecimal getDecimal(String code, BigDecimal suggerito) {
		BigDecimal value = suggerito;
		Preferenze preferenza = null;

		if (code != null && !code.equals("") && !code.equals(" ")) {
			preferenza = getPreferenza(code);
			if (preferenza != null) {
				if (preferenza.type == Preferenze.Type.decimal) {
					value = preferenza.decimale;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		return value;
	}// end of method

	public static byte[] getBytes(String code, byte[] suggerito) {
		byte[] value = suggerito;
		Preferenze preferenza = null;

		if (code != null && !code.equals("") && !code.equals(" ")) {
			preferenza = getPreferenza(code);
			if (preferenza != null) {
				if (preferenza.type == Preferenze.Type.bytes) {
					value = preferenza.bytes;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		return value;
	}

	public static byte[] getBytes(String code) {
		return getBytes(code, new byte[0]);
	}

	public static void put(String key, Object value) {

		// retrieves the preference from the storage
		// or create a new one if not existing
		EntityManager manager = EM.createEntityManager();
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Preferenze> cq = cb.createQuery(Preferenze.class);
		Root<Preferenze> root = cq.from(Preferenze.class);
		Predicate predicate = cb.equal(root.get(Preferenze_.code), key);
		cq.where(predicate);
		TypedQuery<Preferenze> query = manager.createQuery(cq);
		List<Preferenze> prefs = query.getResultList();
		manager.close();
		
		Preferenze pref;
		if (prefs.size() > 0) {
			pref = prefs.get(0);
		} else {
			pref = new Preferenze();
			Preferenze.Type type = getType(value.getClass());
			pref.setCode(key);
			pref.setType(type);
		}

		// write the value
		if (pref.getType().equals(Preferenze.Type.stringa)) {
			pref.setStringa((String) value);
		}
		if (pref.getType().equals(Preferenze.Type.booleano)) {
			pref.setBool((Boolean) value);
		}
		if (pref.getType().equals(Preferenze.Type.intero)) {
			pref.setIntero((Integer) value);
		}
		if (pref.getType().equals(Preferenze.Type.decimal)) {
			pref.setDecimale((BigDecimal) value);
		}
		if (pref.getType().equals(Preferenze.Type.date)) {
			pref.setDate((Date) value);
		}
		if (pref.getType().equals(Preferenze.Type.bytes)) {
			pref.setBytes((byte[]) value);
		}

		pref.save();

	}

	/**
	 * Return the type from class
	 */
	private static Preferenze.Type getType(Class clazz) {
		Preferenze.Type type = null;

		if (clazz.equals(String.class)) {
			type = Preferenze.Type.stringa;
		}// end of if cycle

		if (clazz.equals(Boolean.class)) {
			type = Preferenze.Type.booleano;
		}// end of if cycle

		if (clazz.equals(Integer.class)) {
			type = Preferenze.Type.intero;
		}// end of if cycle

		if (clazz.equals(BigDecimal.class)) {
			type = Preferenze.Type.decimal;
		}// end of if cycle

		if (clazz.equals(Date.class)) {
			type = Preferenze.Type.date;
		}// end of if cycle

		if (clazz.equals(byte[].class)) {
			type = Preferenze.Type.bytes;
		}// end of if cycle

		return type;

	}

	// public static void put(Preferenze.Type type, String code, Object value) {
	// boolean registra = false;
	// Preferenze preferenza = new Preferenze();
	// preferenza.setType(type);
	// preferenza.setCode(code);
	//
	// if (type == Preferenze.Type.stringa) {
	// if (value instanceof String) {
	// preferenza.setStringa((String) value);
	// preferenza.setBool(null);
	// preferenza.setIntero(null);
	// preferenza.setDate(null);
	// registra = true;
	// }// end of if cycle
	//
	// }// end of if cycle
	//
	// if (type == Preferenze.Type.booleano) {
	// if (value instanceof Boolean) {
	// preferenza.setStringa(null);
	// preferenza.setBool((Boolean)value);
	// preferenza.setIntero(null);
	// preferenza.setDate(null);
	// registra = true;
	// }// end of if cycle
	// }// end of if cycle
	//
	// if (type == Preferenze.Type.intero) {
	// if (value instanceof Integer) {
	// preferenza.setStringa(null);
	// preferenza.setBool(null);
	// preferenza.setIntero((Integer)value);
	// preferenza.setDate(null);
	// registra = true;
	// }// end of if cycle
	// }// end of if cycle
	//
	// if (type == Preferenze.Type.date) {
	// if (value instanceof Date) {
	// preferenza.setStringa(null);
	// preferenza.setBool(null);
	// preferenza.setIntero(null);
	// preferenza.setDate((Date)value);
	// registra = true;
	// }// end of if cycle
	// }// end of if cycle
	//
	//
	// if (registra) {
	// preferenza.save();
	// }// end of if cycle
	//
	// }// end of method

	public static void putStr(String code, String value) {
		put(code, value);
	}// end of method

	public static void putBool(String code, Boolean value) {
		put(code, value);
	}// end of method

	public static void putInt(String code, Integer value) {
		put(code, value);
	}// end of method

	public static void putDate(String code, Date value) {
		put(code, value);
	}// end of method

	public static void putDecimal(String code, BigDecimal value) {
		put(code, value);
	}// end of method

	public static Preferenze read(Long id) {
		Preferenze instance = null;
		BaseEntity entity = AQuery.queryById(Preferenze.class, id);

		if (entity != null) {
			if (entity instanceof Preferenze) {
				instance = (Preferenze) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method


	
	public static Preferenze getPreferenza(String code) {
		Preferenze preferenza = null;
		SingularAttribute attr = Preferenze_.code;
		List<Preferenze> list = (List<Preferenze>) AQuery.queryList(Preferenze.class, attr, code);
		if (list.size() > 0) {
			preferenza = list.get(0);
		}
		return preferenza;
	}// end of method
	

	public Object getValue() {
		Object obj = null;
		if (getType() == Type.booleano) {
			obj = getBool();
		}
		if (getType() == Type.intero) {
			obj = getIntero();
		}
		if (getType() == Type.stringa) {
			obj = getStringa();
		}
		if (getType() == Type.date) {
			obj = getDate();
		}
		if (getType() == Type.decimal) {
			obj = getDecimale();
		}
		if (getType() == Type.bytes) {
			obj = getBytes();
		}

		return obj;
	}

}// end of entity class
