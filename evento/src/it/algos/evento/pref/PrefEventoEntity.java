package it.algos.evento.pref;

import it.algos.evento.multiazienda.EventoEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "companyprefs")
@Entity
public class PrefEventoEntity extends EventoEntity {

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
