package it.algos.evento.entities.lettera;

import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.multiazienda.EventoEntityQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Entity
public class Lettera extends EventoEntity {

	private static final long serialVersionUID = 5232130836430670798L;

	@NotEmpty
	@Size(min = 2, max = 30)
	private String sigla = "";

	@Size(min = 2, max = 80)
	private String oggetto = "";

	@Column(length = 16384)
	private String testo = "";

	private String allegati = "";
	
    @OneToMany(mappedBy="lettera")
    @CascadeOnDelete
	private Set<Spedizione> spedizioni;

	private boolean html = false;

	
	public static EventoEntityQuery<Lettera> query = new EventoEntityQuery(Lettera.class);

	
	
	public Lettera() {
		this("", "", "");
	}// end of constructor

	public Lettera(String sigla, String oggetto, String testo) {
		this(sigla, oggetto, testo, "");
	}// end of constructor

	public Lettera(String sigla, String oggetto, String testo, String allegati) {
		super();
		this.setSigla(sigla);
		this.setOggetto(oggetto);
		this.setTesto(testo);
		this.setAllegati(allegati);
	}// end of constructor

	@Override
	public String toString() {
		return getSigla();
	}// end of method

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla
	 *            the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the oggetto
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto
	 *            the oggetto to set
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * @return the testo
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * @param testo
	 *            the testo to set
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}

	/**
	 * @return the allegati
	 */
	public String getAllegati() {
		return allegati;
	}

	/**
	 * @param allegati
	 *            the allegati to set
	 */
	public void setAllegati(String allegati) {
		this.allegati = allegati;
	}
	
	/**
	 * @return the spedizioni
	 */
	public Set<Spedizione> getSpedizioni() {
		return spedizioni;
	}

	/**
	 * @param spedizioni the spedizioni to set
	 */
	public void setSpedizioni(Set<Spedizione> spedizioni) {
		this.spedizioni = spedizioni;
	}


	public boolean isHtml() {
		return html;
	}


	public void setHtml(boolean html) {
		this.html = html;
	}


	/**
	 * Recupera una istanza di Lettera usando la query specifica
	 *
	 * @return istanza di Lettera, null se non trovata
	 */
	public static Lettera find(long id) {
		Lettera instance = null;
		BaseEntity entity = AQuery.queryById(Lettera.class, id);

		if (entity != null) {
			if (entity instanceof Lettera) {
				instance = (Lettera) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

	public static ArrayList< Lettera> readAll() {
		ArrayList<Lettera> lista = null;

		try { // prova ad eseguire il codice
			lista = (ArrayList<Lettera>) EQuery.getList(Lettera.class);
		} catch (Exception unErrore) { // intercetta l'errore
		}// fine del blocco try-catch

		return lista;
	}// end of method


	public String getTestOut(HashMap<String, String> mappaEscape) {
		String testoOut = "";
		String testoIn = "";

		testoIn = this.testo;
		testoOut = LetteraService.getTesto(testoIn, mappaEscape);

		return testoOut;
	}// end of method
	

}// end of entity class
