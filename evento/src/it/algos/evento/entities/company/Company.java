package it.algos.evento.entities.company;

import it.algos.evento.demo.DemoDataGenerator;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.eventi.EventoPren;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.multiazienda.EventoEntity_;
import it.algos.evento.pref.PrefEventoEntity;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.EntityQuery;
import it.algos.evento.entities.destinatario.Destinatario;
import it.algos.evento.entities.mailing.Mailing;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@DefaultSort({"companyCode"})
public class Company extends BaseEntity {

	private static final long serialVersionUID = 8238775575826490450L;

	@NotEmpty
	private String name = "";
	
	@Email
	@NotEmpty
	private String email;
	
	@Column(unique=true)
	@NotEmpty
	private String companyCode;
	
	private String username;
	
	private String password;

	
	private String address1;
	private String address2;
	
	private String contact;

	private String contractType;

	@Temporal(TemporalType.DATE)
	private Date contractStart;

	@Temporal(TemporalType.DATE)
	private Date contractEnd;
	
	

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Comune> comuni;

    @OneToMany(mappedBy = "company", targetEntity=Evento.class)
    @CascadeOnDelete
    private List<Evento> eventi;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Insegnante> insegnanti;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Lettera> lettere;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Allegato> allegati;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<ModoPagamento> modiPagamento;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Progetto> progetti;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Sala> sale;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Scuola> scuola;

    @OneToMany(mappedBy = "company", targetEntity=PrefEventoEntity.class)
    @CascadeOnDelete
    private List<PrefEventoEntity> prefs;
    
	
	public static EntityQuery<Company> query = new EntityQuery(Company.class);

	public Company() {
		super();
	}// end of constructor
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}



	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}



	public String getCompanyCode() {
		return companyCode;
	}



	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}



	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}



	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}



	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}




	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}



	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}



	/**
	 * @return the contractType
	 */
	public String getContractType() {
		return contractType;
	}



	/**
	 * @param contractType the contractType to set
	 */
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}



	/**
	 * @return the contractStart
	 */
	public Date getContractStart() {
		return contractStart;
	}



	/**
	 * @param contractStart the contractStart to set
	 */
	public void setContractStart(Date contractStart) {
		this.contractStart = contractStart;
	}



	/**
	 * @return the contractEnd
	 */
	public Date getContractEnd() {
		return contractEnd;
	}



	/**
	 * @param contractEnd the contractEnd to set
	 */
	public void setContractEnd(Date contractEnd) {
		this.contractEnd = contractEnd;
	}



	@Override
	public String toString() {
		return getName();
	}// end of method


	public void createDemoData(){
		DemoDataGenerator.createDemoData(this);
	};
	
	/**
	 * Elimina tutti i dati di questa azienda.
	 * <p>
	 * L'ordine di cancellazione è critico per l'integrità referenziale
	 */
	public void deleteAllData(){

		// elimina le tabelle
		AQuery.delete(Spedizione.class, EventoEntity_.company, this);
		AQuery.delete(Lettera.class, EventoEntity_.company, this);
		AQuery.delete(EventoPren.class, EventoEntity_.company, this);
		AQuery.delete(Prenotazione.class, EventoEntity_.company, this);
		AQuery.delete(TipoRicevuta.class, EventoEntity_.company, this);
		AQuery.delete(Rappresentazione.class,  EventoEntity_.company, this);
		AQuery.delete(Sala.class,  EventoEntity_.company, this);
		AQuery.delete(Evento.class,  EventoEntity_.company, this);
		AQuery.delete(Stagione.class,  EventoEntity_.company, this);
		AQuery.delete(Progetto.class,  EventoEntity_.company, this);
		AQuery.delete(ModoPagamento.class,  EventoEntity_.company, this);
		AQuery.delete(Insegnante.class,  EventoEntity_.company, this);
		AQuery.delete(Scuola.class,  EventoEntity_.company, this);
		AQuery.delete(OrdineScuola.class,  EventoEntity_.company, this);
		AQuery.delete(Comune.class,  EventoEntity_.company, this);
		AQuery.delete(Destinatario.class,  EventoEntity_.company, this);
		AQuery.delete(Mailing.class,  EventoEntity_.company, this);
		AQuery.delete(PrefEventoEntity.class, EventoEntity_.company, this);
		AQuery.delete(UtenteRuolo.class, EventoEntity_.company, this);
		AQuery.delete(Ruolo.class, EventoEntity_.company, this);
		AQuery.delete(Utente.class, EventoEntity_.company, this);

		// elimina le preferenze
		AQuery.delete(PrefEventoEntity.class, EventoEntity_.company, this);



	}

	/**
	 * Ritorna la Company corrente.
	 * @return la Company corrente
	 */
	public static Company getCurrent(){
		return EventoSessionLib.getCompany();
	}
	


}// end of entity class



