package it.algos.evento.pref;

import it.algos.webbase.multiazienda.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "COMPANYPREFS")
@Entity
public class PrefEventoEntity extends CompanyEntity {

	private static final long serialVersionUID = -325887743609301921L;
	
	private String code;
	private byte[] value;
	
	public PrefEventoEntity() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

}
