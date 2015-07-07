package it.algos.evento.entities.sala;

import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.web.entity.BaseEntity;
import it.algos.web.query.AQuery;

import javax.persistence.Entity;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
public class Sala extends EventoEntity {

	private static final long serialVersionUID = 8238775575826490450L;

	@NotEmpty
	private String nome = "";

	@Min(value = 1)
	private Integer capienza;

	public Sala() {
		this("", 0);
	}// end of constructor

	public Sala(String nome, int capienza) {
		super();
		this.setNome(nome);
		this.setCapienza(capienza);
	}// end of constructor

	@Override
	public String toString() {
		return nome;
	}// end of method

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the capienza
	 */
	public Integer getCapienza() {
		return capienza;
	}

	/**
	 * @param capienza
	 *            the capienza to set
	 */
	public void setCapienza(Integer capienza) {
		this.capienza = capienza;
	}

	/**
	 * Recupera la sala usando la query specifica
	 * 
	 * @return la sala, null se non trovata
	 */
	public static Sala read(long id) {
		Sala instance = null;
		BaseEntity entity = AQuery.queryById(Sala.class, id);

		if (entity != null) {
			if (entity instanceof Sala) {
				instance = (Sala) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method


	/**
	 * Recupera la sala di default eventualmente indicata in preferenze
	 * 
	 * @return la sala di default, null se non specificata
	 */
	public static Sala getDefault() {
		Sala sala = null;
		int idSala = CompanyPrefs.idSalaDefault.getInt();

		if (idSala > 0) {
			sala = Sala.read((long) idSala);
		}// end of if cycle

		return sala;
	}// end of method

}// end of entity class
