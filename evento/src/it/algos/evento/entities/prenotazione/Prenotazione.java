package it.algos.evento.entities.prenotazione;

import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.eventi.EventoPren;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
//@CascadeOnDelete
//@Indexes(name="EMP_NAME_INDEX", columnList="pippo")
//@Indexes({
//        @Index(columnList={"lastName","firstName"}),
//        @Index(columnList={"firstName"})
//})

//@Table(indexes = { @Index(name="pippo", columnList={"numPrenotazione"}) })


@DefaultSort({"numPrenotazione"})
public class Prenotazione extends EventoEntity {

    private static final long serialVersionUID = -6685175938276422883L;

    private final static Logger logger = Logger.getLogger(PrenotazioneModulo.class.getName());


    @PostPersist
    protected void postPersist() {
        super.postPersist(Prenotazione.class, getId());
    }

    @PostUpdate
    protected void postUpdate() {
        super.postUpdate(Prenotazione.class, getId());
    }

    // @GeneratedValue(strategy = GenerationType.AUTO)
   // @org.eclipse.persistence.annotations.Index
    private int numPrenotazione;

    private String uuid;

    @ManyToOne
    @NotNull
    private Rappresentazione rappresentazione;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataPrenotazione = new Date(); // data e ora della prenotazione

    @ManyToOne
    private Scuola scuola;

    @ManyToOne
    @NotNull
    private Insegnante insegnante;

    //@org.eclipse.persistence.annotations.Index
    private String classe = "";

    @NotEmpty
    private String telRiferimento = ""; // telefono della persona di riferimento

    @Email
    @NotEmpty
    private String emailRiferimento = ""; // email della persona di riferimento

    private int numInteri; // numero di posti interi

    private int numRidotti; // numero di posti ridotti

    private int numDisabili; // numero di posti disabili

    private int numAccomp; // numero di posti accompagnatori

    private int numTotali; // numero di posti totali (calcolato!)

    @Column(precision = 6, scale = 2)
    private BigDecimal importoIntero=new BigDecimal(0);

    @Column(precision = 6, scale = 2)
    private BigDecimal importoRidotto=new BigDecimal(0);

    @Column(precision = 6, scale = 2)
    private BigDecimal importoDisabili=new BigDecimal(0);

    @Column(precision = 6, scale = 2)
    private BigDecimal importoAccomp=new BigDecimal(0);

    @Column(precision = 8, scale = 2)
    private BigDecimal importoDaPagare; // importo totale da pagare (calcolato!)

    @ManyToOne
    private ModoPagamento modoPagamento;

    @Temporal(TemporalType.DATE)
    private Date scadenzaPagamento;

    private int livelloSollecitoPagamento;

    private boolean pagamentoConfermato;

    @Column(precision = 8, scale = 2)
    private BigDecimal importoPagato;

    @Temporal(TemporalType.DATE)
    private Date dataPagamentoConfermato;

    @ManyToOne
    private TipoRicevuta tipoRicevuta;

    @Lob
    private String note = "";

    @Temporal(TemporalType.DATE)
    private Date scadenzaConferma;

    private int livelloSollecitoConferma;

    private boolean congelata;

    private boolean confermata;

    @Temporal(TemporalType.DATE)
    private Date dataConferma;

    private boolean pagamentoRicevuto;

    @Temporal(TemporalType.DATE)
    private Date dataPagamentoRicevuto;

    private boolean privato;


    @OneToMany(mappedBy = "prenotazione")
    @CascadeOnDelete
    private Set<EventoPren> eventiPren;


    public Set<EventoPren> getEventiPren() {
        return eventiPren;
    }

    public void setEventiPren(Set<EventoPren> eventiPren) {
        this.eventiPren = eventiPren;
    }

    @Override
    public String toString() {
        String stringa = "";
        stringa += "N." + numPrenotazione;
        stringa += " del " + LibDate.toStringDDMMYYYY(dataPrenotazione);

        Insegnante i = getInsegnante();
        if (i != null) {
        }
        stringa += " " + i.toString();
        return stringa;
    }

    public String toStringNumDataInsegnante() {
        String stringa = "";
        stringa += "N." + numPrenotazione;
        stringa += " del " + LibDate.toStringDDMMYYYY(dataPrenotazione);
        Insegnante i = getInsegnante();
        if (i != null) {
            stringa += " " + i.getCognome() + " " + i.getNome();
        }
        return stringa;
    }


    public Prenotazione() {
        super();
        this.uuid = UUID.randomUUID().toString();
    }// end of constructor

//	public Prenotazione(int numPrenotazione, Date dataPrenotazione, String classe, Insegnante insegnante,
//			BigDecimal importoDaPagare) {
//		super();
//		setNumPrenotazione(numPrenotazione);
//		setDataPrenotazione(dataPrenotazione);
//		setClasse(classe);
//		setInsegnante(insegnante);
//		setImportoDaPagare(importoDaPagare);
//	}// end of constructor

//	public Prenotazione(int numPrenotazione, Rappresentazione rappresentazione, Scuola scuola, Insegnante insegnante,
//			String telRiferimento, String emailRiferimento, int numInteri) {
//		super();
//		setNumPrenotazione(numPrenotazione);
//		setRappresentazione(rappresentazione);
//		setScuola(scuola);
//		setInsegnante(insegnante);
//		setTelRiferimento(telRiferimento);
//		setEmailRiferimento(emailRiferimento);
//		setNumInteri(numInteri);
//		setDataPrenotazione(new Date());
//	}// end of constructor

    public int getNumPrenotazione() {
        return numPrenotazione;
    }

    public void setNumPrenotazione(int numPrenotazione) {
        this.numPrenotazione = numPrenotazione;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Rappresentazione getRappresentazione() {
        return rappresentazione;
    }

    public void setRappresentazione(Rappresentazione rappresentazione) {
        this.rappresentazione = rappresentazione;
    }

    public Date getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(Date dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public Scuola getScuola() {
        return scuola;
    }

    public void setScuola(Scuola scuola) {
        this.scuola = scuola;
    }


    public Insegnante getInsegnante() {
        return insegnante;
    }

    public void setInsegnante(Insegnante insegnante) {
        this.insegnante = insegnante;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTelRiferimento() {
        return telRiferimento;
    }

    public void setTelRiferimento(String telRiferimento) {
        this.telRiferimento = telRiferimento;
    }

    public String getEmailRiferimento() {
        return emailRiferimento;
    }

    public void setEmailRiferimento(String emailRiferimento) {
        this.emailRiferimento = emailRiferimento;
    }

    public int getNumInteri() {
        return numInteri;
    }

    public void setNumInteri(int num) {
        this.numInteri = num;
    }

    public int getNumRidotti() {
        return numRidotti;
    }

    public void setNumRidotti(int num) {
        this.numRidotti = num;
    }

    public int getNumDisabili() {
        return numDisabili;
    }

    public void setNumDisabili(int num) {
        this.numDisabili = num;
    }


    public int getNumAccomp() {
        return numAccomp;
    }

    public void setNumAccomp(int numAccomp) {
        this.numAccomp = numAccomp;
    }

    // questo è un campo calcolato, il getter
    // ritorna sempre il risultato del calcolo
    public int getNumTotali() {
        return numInteri + numRidotti + numDisabili + numAccomp;
    }


    public BigDecimal getImportoIntero() {
        return importoIntero;
    }

    public void setImportoIntero(BigDecimal importo) {
        if(importo==null){  // evitiamo i nulli nei numeri sul database
            importo=new BigDecimal(0);
        }
        this.importoIntero = importo;
    }

    public BigDecimal getImportoRidotto() {
        return importoRidotto;
    }

    public void setImportoRidotto(BigDecimal importo) {
        if(importo==null){
            importo=new BigDecimal(0);
        }
        this.importoRidotto = importo;
    }

    public BigDecimal getImportoDisabili() {
        return importoDisabili;
    }

    public void setImportoDisabili(BigDecimal importo) {
        if(importo==null){
            importo=new BigDecimal(0);
        }
        this.importoDisabili = importo;
    }

    public BigDecimal getImportoAccomp() {
        return importoAccomp;
    }

    public void setImportoAccomp(BigDecimal importo) {
        if(importo==null){
            importo=new BigDecimal(0);
        }
        this.importoAccomp = importo;
    }

    // questo è un campo calcolato, il getter
    // ritorna sempre il risultato del calcolo
    public BigDecimal getImportoDaPagare() {
        BigDecimal totale;

        int nInteri = getNumInteri();
        int nRidotti = getNumRidotti();
        int nDisabili = getNumDisabili();
        int nAccomp = getNumAccomp();
        BigDecimal iInteri = Lib.getBigDecimal(getImportoIntero());
        BigDecimal iRidotti = Lib.getBigDecimal(getImportoRidotto());
        BigDecimal iDisabili = Lib.getBigDecimal(getImportoDisabili());
        BigDecimal iAccomp = Lib.getBigDecimal(getImportoAccomp());
//		(Prenotazione_.importoIntero);
//		BigDecimal iRidotti=getBigDecimalValue(Prenotazione_.importoRidotto);
//		BigDecimal iDisabili=getBigDecimalValue(Prenotazione_.importoDisabili);
//		BigDecimal iAccomp=getBigDecimalValue(Prenotazione_.importoAccomp);


        totale = Prenotazione.getTotImporto(nInteri, nRidotti, nDisabili, nAccomp,
                iInteri, iRidotti, iDisabili, iAccomp);
        return totale;
    }

//    public void setImportoDaPagare(BigDecimal importoDaPagare) {
//        this.importoDaPagare = importoDaPagare;
//    }

    public ModoPagamento getModoPagamento() {
        return modoPagamento;
    }

    public void setModoPagamento(ModoPagamento modoPagamento) {
        this.modoPagamento = modoPagamento;
    }

    public Date getScadenzaPagamento() {
        return scadenzaPagamento;
    }

    public void setScadenzaPagamento(Date scadenzaPagamento) {
        this.scadenzaPagamento = scadenzaPagamento;
    }

    public boolean isPagamentoConfermato() {
        return pagamentoConfermato;
    }

    public void setPagamentoConfermato(boolean pagato) {
        this.pagamentoConfermato = pagato;
    }

    public BigDecimal getImportoPagato() {
        return importoPagato;
    }

    public void setImportoPagato(BigDecimal importoPagato) {
        this.importoPagato = importoPagato;
    }

    public Date getDataPagamentoConfermato() {
        return dataPagamentoConfermato;
    }

    public void setDataPagamentoConfermato(Date data) {
        this.dataPagamentoConfermato = data;
    }

    public TipoRicevuta getTipoRicevuta() {
        return tipoRicevuta;
    }

    public void setTipoRicevuta(TipoRicevuta tipoRicevuta) {
        this.tipoRicevuta = tipoRicevuta;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getScadenzaConferma() {
        if ((scadenzaConferma == null) && (dataPrenotazione != null)) {
            DateTime jdate = new DateTime(dataPrenotazione);
            int days = CompanyPrefs.ggScadConfermaPrenotazione.getInt();
            jdate = jdate.plusDays(days);
            scadenzaConferma = jdate.toDate();
        }
        return scadenzaConferma;
    }

    public void setScadenzaConferma(Date scadenzaConferma) {
        this.scadenzaConferma = scadenzaConferma;
    }

    public boolean isConfermata() {
        return confermata;
    }

    public void setConfermata(boolean confermata) {
        this.confermata = confermata;
    }

    public Date getDataConferma() {
        return dataConferma;
    }

    public void setDataConferma(Date dataConferma) {
        this.dataConferma = dataConferma;
    }

    public boolean isPagamentoRicevuto() {
        return pagamentoRicevuto;
    }

    public void setPagamentoRicevuto(boolean flag) {
        this.pagamentoRicevuto = flag;
    }

    public Date getDataPagamentoRicevuto() {
        return dataPagamentoRicevuto;
    }

    public void setDataPagamentoRicevuto(Date data) {
        this.dataPagamentoRicevuto = data;
    }

    public int getLivelloSollecitoPagamento() {
        return livelloSollecitoPagamento;
    }

    public void setLivelloSollecitoPagamento(int livello) {
        this.livelloSollecitoPagamento = livello;
    }

    public int getLivelloSollecitoConferma() {
        return livelloSollecitoConferma;
    }

    public void setLivelloSollecitoConferma(int livello) {
        this.livelloSollecitoConferma = livello;
    }

    public boolean isCongelata() {
        return congelata;
    }

    public void setCongelata(boolean congelata) {
        this.congelata = congelata;
    }

    public boolean isPrivato() {
//		boolean privato=false;
//		Insegnante ins=getInsegnante();
//		if(ins!=null){
//			privato=ins.isPrivato();
//		}
        return privato;
    }

    public void setPrivato(boolean privato) {
        this.privato = privato;
    }


//	@Transient
//	public Progetto progetto;
//
//	/**
//	 * Recupera il progetto
//	 *
//	 * @return la Prenotazione, null se non trovato
//	 */
//	@Transient
//	public Progetto getProgetto() {
//		Progetto progetto = null;
//		try {
//			progetto=getRappresentazione().getEvento().getProgetto();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return progetto;
//	}

    /**
     * Ritorna la data di scadenza conferma in base alla data
     * <p>
     * di prenotazione e ai giorniimpostati nelle preferenze
     */
    public static Date getDataScadenzaConferma(Date dataPren) {
        DateTime jdate = new DateTime(dataPren);
        int days = CompanyPrefs.ggScadConfermaPrenotazione.getInt();
        jdate = jdate.plusDays(days);
        Date outDate = jdate.toDate();
        return outDate;
    }

    /**
     * Recupera la Prenotazione per id
     *
     * @return la Prenotazione, null se non trovato
     */
    public static Prenotazione read(Object id) {
        Prenotazione instance = null;
        BaseEntity entity = AQuery.queryById(Prenotazione.class, id);
        if (entity != null) {
            if (entity instanceof Prenotazione) {
                instance = (Prenotazione) entity;
            }
        }
        return instance;
    }


    /**
     * Ritorna l'importo totale di una prenotazione
     * <p>
     *
     * @return l'importo totale
     */
    public static BigDecimal getTotImporto(int nInteri, int nRidotti, int nDisabili, int nAccomp, BigDecimal iInteri, BigDecimal iRidotti, BigDecimal iDisabili, BigDecimal iAccomp) {
        BigDecimal totImporto = new BigDecimal(0);

        try {


            BigDecimal totInteri= new BigDecimal(0);
            if(iInteri!=null){
                totInteri = iInteri.multiply(new BigDecimal(nInteri));
            }

            BigDecimal totRidotti= new BigDecimal(0);
            if(iRidotti!=null){
                totRidotti = iRidotti.multiply(new BigDecimal(nRidotti));
            }

            BigDecimal totDisabili= new BigDecimal(0);
            if(iDisabili!=null){
                totDisabili=iDisabili.multiply(new BigDecimal(nDisabili));
            }

            BigDecimal totAccomp= new BigDecimal(0);
            if(iAccomp!=null){
                totAccomp=iAccomp.multiply(new BigDecimal(nAccomp));
            }

            totImporto = totImporto.add(totInteri);
            totImporto = totImporto.add(totRidotti);
            totImporto = totImporto.add(totDisabili);
            totImporto = totImporto.add(totAccomp);

        } catch (Exception e) {
            logger.log(Level.WARNING, "Errore nel calcolo totale importo prenotazione: " + e.getMessage());
        }

        return totImporto;
    }// end of method


}// end of entity class
