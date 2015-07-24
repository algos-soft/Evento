package it.algos.evento;

import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.ordinescuola.OrdineScuola_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.stagione.Stagione_;
import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.multiazienda.EROContainer;
import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.web.AlgosApp;
import it.algos.web.entity.BaseEntity;
import it.algos.web.entity.EM;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.comune.ComuneImport;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraKeys;
import it.algos.evento.entities.lettera.LetteraModulo;
import it.algos.evento.entities.lettera.Lettera_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.scuola.Scuola;

import java.util.*;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

public class DemoDataGenerator {

	/**
	 * Name of the folder for bootstrap files <br>
	 * The folder is located in:<br>
	 * /.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/
	 * asteriacultura/
	 */
	public static final String BOOTSTRAP_FOLDER_NAME = "bootstrap";

	/**
	 * Codice della demo company
	 */
	private static final String DEMO_COMPANY_CODE = "demo";

	
	/**
	 * Si assicura che esista l'azienda demo.
	 * <p>
	 * se non c'è la genera ora
	 */
	public static void ensureCompanyDemo(){
		Company company=Company.query.queryOne(Company_.companyCode, DEMO_COMPANY_CODE);
		if (company==null) {
			
			// crea e registra la company
			company = new Company();
			company.setCompanyCode("demo");
			company.setName("Demo");
			company.save();
			
			// crea i dati demo
			company.createDemoData();
						
		}
	}
	
	
	/**
	 * Creazione dei dati iniziali per una data azienda.
	 * <p>
	 * 
	 * @param company
	 *            - l'azienda di riferimento Attenzione! <br>
	 *            L'ordine di creazione delle varie tavole deve rispettare le
	 *            relazioni che esistono tra di esse. <br>
	 */
	public static void createDemoData(Company company) {

		// save current company
		//Company currentCompany = EventoApp.COMPANY;
		Company currentCompany = EventoSession.getCompany();


		try {

			// set company for all subsequent operations
			// EventoApp.COMPANY = company;
			EventoSession.setCompany(company);

			if (EQuery.getCount(Allegato.class) == 0) {
				creaAllegati();
			}

			creaLettere();

			if (EQuery.getCount(Sala.class) == 0) {
				creaSale();
			}
			if (EQuery.getCount(Progetto.class) == 0) {
				creaProgetti();
			}
			if (EQuery.getCount(ModoPagamento.class) == 0) {
				creaPagamenti();
			}
			if (EQuery.getCount(TipoRicevuta.class) == 0) {
				creaTipiRicevuta();
			}
			if (EQuery.getCount(OrdineScuola.class) == 0) {
				creaOrdiniScuola();
			}
			if (EQuery.getCount(Insegnante.class) == 0) {
				creaInsegnanti();
			}
			if (EQuery.getCount(Comune.class) == 0) {
				creaComuni();
			}
			if (EQuery.getCount(Scuola.class) == 0) {
				creaScuole();
			}
			if (EQuery.getCount(Stagione.class) == 0) {
				creaStagioni();
			}
			if (EQuery.getCount(Evento.class) == 0) {
				creaEventi();
			}
			if (EQuery.getCount(Rappresentazione.class) == 0) {
				creaRappresentazioni();
			}
			if (EQuery.getCount(Prenotazione.class) == 0) {
				creaPrenotazioni();
			}

		} catch (Exception e) {
			// restore current company
			//EventoApp.COMPANY = currentCompany;
			EventoSession.setCompany(currentCompany);
		}
		// restore current company
		//EventoApp.COMPANY = currentCompany;
		EventoSession.setCompany(currentCompany);


	}// end of method

	// /**
	// * Create all data of Enumeration
	// */
	// public static void creaPreferenzeGenerali() {
	// Pref.init();
	// }// end of method

	/**
	 * Create some demo data only if the table is empty
	 */
	private static void creaSale() {
		new Sala("Auditorium", 430).save();
		new Sala("Odeon", 220).save();
	}// end of method

	/**
	 * Create some demo data only if the table is empty
	 */
	private static void creaProgetti() {
		new Progetto("La scienza della vita").save();
		new Progetto("Storia e memoria").save();
		new Progetto("Popoli nel tempo").save();
	}// end of method

	/**
	 * Create some demo data only if the table is empty
	 */
	public static void creaPagamenti() {
		new ModoPagamento("BB", "Bonifico bancario").save();
		new ModoPagamento("VP", "Vaglia postale").save();
		new ModoPagamento("CONT", "Contanti").save();
	}// end of method

	/**
	 * Create some demo data only if the table is empty
	 */
	public static void creaTipiRicevuta() {
		new TipoRicevuta("RIC","Ricevuta").save();
		new TipoRicevuta("FATT", "Fattura").save();
		new TipoRicevuta("FE","Fattura Elettronica").save();
	}// end of method

	/**
	 * Create some demo data only if the table is empty
	 */
	public static void creaOrdiniScuola() {
		new OrdineScuola("INF","Scuola dell'Infanzia").save();
		new OrdineScuola("PRI","Primaria").save();
		new OrdineScuola("MED","Secondaria I grado (medie)").save();
		new OrdineScuola("SUP","Secondaria II grado (superiori)").save();
		new OrdineScuola("UNI","Università").save();
	}// end of method


	/**
	 * Create some demo data only if the table is empty
	 */
	public static void creaInsegnanti() {
		Insegnante ins;

		ins = new Insegnante("Lovecchio", "Luigi", "Prof.",
				"lovecchio.luigi@gmail.com", "matematica, scienze");
		ins.setOrdineScuola(getOrdineScuolaRandom());
		ins.setTelefono("348-784565");
		ins.setIndirizzo1("Via dei Gelsomini, 8");
		ins.setIndirizzo2("20154 Ferrara");
		ins.save();

		ins = new Insegnante("Ferrari", "Sara", "Prof.ssa",
				"ferrari.sara@gmail.com", "lettere");
		ins.setOrdineScuola(getOrdineScuolaRandom());
		ins.setTelefono("885-4455778");
		ins.setIndirizzo1("Via Garibaldi, 26");
		ins.setIndirizzo2("50145 Rovigo");
		ins.save();

		ins = new Insegnante("Sarfatti", "Lucia", "Prof.ssa",
				"lsarfatti@ymail.com", "disegno");
		ins.setOrdineScuola(getOrdineScuolaRandom());
		ins.setTelefono("999-5462335");
		ins.setIndirizzo1("Piazza Po, 12");
		ins.setIndirizzo2("56445 Mantova");
		ins.save();

		ins = new Insegnante("Gasparotti", "Antonella", "Prof.ssa",
				"agasparotti@tin.it", "storia, filosofia");
		ins.setOrdineScuola(getOrdineScuolaRandom());
		ins.setTelefono("884-6589998");
		ins.setIndirizzo1("Largo Brasilia, 22");
		ins.setIndirizzo2("20100 Milano");
		ins.save();

		ins = new Insegnante("Marinelli", "Laura", "Prof.ssa",
				"lmarinelli@hotmail.it", "lettere, storia");
		ins.setOrdineScuola(getOrdineScuolaRandom());
		ins.setTelefono("556-4456658");
		ins.setIndirizzo1("Via Vasco de Gama, 22");
		ins.setIndirizzo2("25556 Castelnuovo Val Tidone (PC)");
		ins.save();
	}// end of method

	/**
	 * Crea i comuni
	 */
	public static void creaComuni() {
		ServletContext svlContext = EventoApp.getServletContext();
		String fullPath = svlContext
				.getRealPath(AlgosApp.DEMODATA_FOLDER_NAME
						+ "comuni/comuni.xls");
		ComuneImport.doImport(fullPath);
	}// end of method

	/**
	 * Create some demo data only if the table is empty
	 */
	public static void creaScuole() {
		Scuola scuola;

		OrdineScuola ordine = (OrdineScuola)EQuery.queryFirst(OrdineScuola.class, OrdineScuola_.sigla, "SUP");

		scuola = new Scuola("Beccaria", "Liceo Classico Beccaria",
				getComuneRandom(), ordine);
		scuola.setIndirizzo("Via Carlo Linneo,5");
		scuola.setCap("20145");
		scuola.setTelefono("025-365487");
		scuola.setEmail("liceobeccaria@yahoo.com");
		scuola.save();

		scuola = new Scuola("Rampaldi", "Istituto Tecnico Rampaldi",
				getComuneRandom(), ordine);
		scuola.setIndirizzo("Via Varzi N. 16");
		scuola.setCap("56554");
		scuola.setTelefono("125-2356487");
		scuola.setEmail("istrampaldi@yahoo.com");
		scuola.save();

		scuola = new Scuola("Leonardo", "Liceo Scientifico Leonardo da Vinci",
				getComuneRandom(), ordine);
		scuola.setIndirizzo("Via Stazione 1");
		scuola.setCap("36665");
		scuola.setTelefono("035-564789");
		scuola.setEmail("liceoleonardo@yahoo.com");
		scuola.save();

		scuola = new Scuola("Falcone", "Istituto Magistrale Giovanni Falcone", ordine);
		scuola.setIndirizzo("Via Dunant, 1");
		scuola.setCap("24128");
		scuola.setTelefono("035-6598745");
		scuola.setEmail("istfalcone@yahoo.com");
		scuola.save();

		scuola = new Scuola("Rota", "Istituto Superiore Lorenzo Rota",
				getComuneRandom(), ordine);
		scuola.setIndirizzo("Via Lavello, 17");
		scuola.setCap("55664");
		scuola.setTelefono("023-564789");
		scuola.setEmail("istitutorota@yahoo.com");
		scuola.save();

	}// end of method

	private static Comune getComuneRandom() {
		return (Comune) getEntityRandom(Comune.class);
	}

	private static OrdineScuola getOrdineScuolaRandom() {
		return (OrdineScuola) getEntityRandom(OrdineScuola.class);
	}


	private static Progetto getProgettoRandom() {
		return (Progetto) getEntityRandom(Progetto.class);
	}

	private static BaseEntity getEntityRandom(Class clazz) {
		List<EventoEntity> entities = (List<EventoEntity>) EQuery
				.getList(clazz);
		int min = 0;
		int max = entities.size() - 1;
		int randomNum = new Random().nextInt((max - min) + 1) + min;
		return entities.get(randomNum);
	}

	/**
	 * @return random da 1 a max
	 */
	private static int getRandom(int max) {
		Random rand = new Random();
		int min = 1;
		return rand.nextInt((max - min) + 1) + min;
	}


	/**
	 * Crea stagioni demo
	 * Crea la stagione corrente
	 */
	public static void creaStagioni() {

		// fino a fine maggio crea la stagione iniziata l'anno precedente
		// da giugno crea la stagione che inizia quest'anno
		DateTime dt = new DateTime();
		int year = dt.getYear();
		int month = dt.getMonthOfYear();  // where January is 1 and December is 12
		int yearStart;
		DateTime dStart, dEnd;
		if(month<6){
			yearStart=year-1;
		}else{
			yearStart=year;
		}
		dStart=new DateTime(yearStart,6,1,0,0,0);
		dEnd=dStart.plusYears(1).minusDays(1);
		String sigla = ""+dStart.getYear()+"-"+dEnd.getYear();

		Stagione stagione = new Stagione();
		stagione.setSigla(sigla);
		stagione.setDatainizio(dStart.toDate());
		stagione.setDatafine(dEnd.toDate());
		stagione.setCorrente(true);

		stagione.save();

	}

		/**
         * Create some demo data only if the table is empty
         */
	public static void creaEventi() {

		Evento evento;
		evento = new Evento("Vivarelli", "Un ricordo di Roberto Vivarelli", 16,
				9, 0, 5);
		saveEvento(evento);

		evento = new Evento("Accademia", "Accademia Bizantina", 16, 9, 0, 5);
		saveEvento(evento);

		evento = new Evento("Gentile", "L'assassinio di Giovanni Gentile", 12,
				7, 0, 5);
		saveEvento(evento);

		evento = new Evento("Chimica", "Le Frontiere della Chimica", 12, 7, 0, 5);
		saveEvento(evento);

		evento = new Evento("Big Bang",
				"Big Bang: l'inizio e la fine nelle stelle", 18, 10, 0, 8);
		saveEvento(evento);

		evento = new Evento("Auschwitz", "Auschwitz - parla un testimone", 15,
				8, 0, 5);
		saveEvento(evento);

		evento = new Evento("Mafia",
				"Cercando la verità nel labirinto della mafia", 10, 8, 0, 5);
		saveEvento(evento);

		evento = new Evento("Shackleton", "Sulle orme di Ernest Shackleton",
				16, 8, 0, 5);
		saveEvento(evento);

	}// end of method

	private static void saveEvento(Evento evento) {
		Filter filter = new Compare.Equal(Stagione_.corrente.getName(), true);
		Stagione stagione = (Stagione)EQuery.getEntity(Stagione.class, filter);
		evento.setProgetto(getProgettoRandom());
		evento.setStagione(stagione);
		evento.save();
	}

	private static void creaRappresentazioni() {
		Rappresentazione rapp;

		MutableDateTime dt = new MutableDateTime(2014, 11, 1, 0, 0, 0, 0);

		for (int i = 0; i < 40; i++) {

			dt.addDays(getRandom(5));
			if (getRandom(2) == 1) {
				dt.setTime(10, 30, 0, 0);
			} else {
				dt.setTime(15, 0, 0, 0);
			}
			Evento evento = (Evento) getEntityRandom(Evento.class);
			Sala sala = (Sala) getEntityRandom(Sala.class);

			rapp = new Rappresentazione();
			rapp.setEvento(evento);
			rapp.setDataRappresentazione(dt.toDate());
			rapp.setSala(sala);
			rapp.setCapienza(sala.getCapienza());
			rapp.save();

		}
	}

	/**
	 * Crea un certo numero di prenotazioni.
	 * <p>
	 * 
	 * @return una lista delle prenotazioni create
	 */
	public static ArrayList<Prenotazione> creaPrenotazioni() {
		ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
		int quante = 50;
		Prenotazione pren;

		EntityManager manager = EM.createEntityManager();
		manager.getTransaction().begin();

		MutableDateTime dt = new MutableDateTime(2014, 9, 1, 0, 0, 0, 0);

		try {

			for (int i = 0; i < quante; i++) {

				dt.addDays(getRandom(3) - 1); // da 0 a 2 gg

				// una rappresentazione che sia almeno 1 mese più avanti delle
				// prenotazione
				Rappresentazione rapp = getRappresentazionePost(dt.toDate());

				if (rapp != null) {

					int nInteri = getRandom(80);
					int nRidotti = getRandom(3);
					int nDisabili = getRandom(4);
					int nAccomp = getRandom(4);


					pren = new Prenotazione();
					pren.setNumPrenotazione(CompanyPrefs.nextNumPren.getInt());
					pren.setDataPrenotazione(dt.toDate());
					pren.setRappresentazione(rapp);
					pren.setScuola((Scuola) getEntityRandom(Scuola.class));
					pren.setInsegnante((Insegnante) getEntityRandom(Insegnante.class));
					pren.setEmailRiferimento(pren.getInsegnante().getEmail());
					pren.setTelRiferimento(pren.getInsegnante().getTelefono());

					pren.setNumInteri(nInteri);
					pren.setNumRidotti(nRidotti);
					pren.setNumDisabili(nDisabili);
					pren.setNumAccomp(nAccomp);

					pren.setModoPagamento((ModoPagamento) getEntityRandom(ModoPagamento.class));

					DateTime scadConf = new DateTime(pren.getDataPrenotazione())
							.plusDays(CompanyPrefs.ggScadConfermaPrenotazione
									.getInt());
					pren.setScadenzaConferma(scadConf.toDate());

					DateTime dataRapp = new DateTime(pren.getRappresentazione()
							.getDataRappresentazione());
					DateTime scadPaga = dataRapp
							.minusDays(CompanyPrefs.ggScadConfermaPagamento
									.getInt());
					pren.setScadenzaPagamento(scadPaga.toDate());

					//BigDecimal totPren = Rappresentazione.getTotImporto(rapp, nInteri, nRidotti);
//					BigDecimal iIntero=pren.getImportoIntero();
//					BigDecimal iRidotto=pren.getImportoRidotto();
//					BigDecimal iDisabili=pren.getImportoDisabili();
//					BigDecimal iAccomp=pren.getImportoAccomp();
//					BigDecimal totPren = Prenotazione.getTotImporto(nInteri, nRidotti, nDisabili, nAccomp, iIntero, iRidotto, iDisabili, iAccomp);

//					pren.setImportoDaPagare(totPren);

					// copertura dati obbligatori mancanti
					if ((pren.getEmailRiferimento() == null)
							|| (pren.getEmailRiferimento().equals(""))) {
						pren.setEmailRiferimento("unamail@test.it");
					}
					if ((pren.getTelRiferimento() == null)
							|| (pren.getTelRiferimento().equals(""))) {
						pren.setTelRiferimento("99999999");
					}

					// pren.save();

					manager.persist(pren);

					prenotazioni.add(pren);

					System.out.println("create " + i + " -> " + pren);

					CompanyPrefs.nextNumPren.put(CompanyPrefs.nextNumPren
							.getInt() + 1);

				}

			}

			manager.getTransaction().commit();

		} catch (Exception e) {
			manager.getTransaction().rollback();
		}

		manager.close();

		return prenotazioni;

	}

	// una rappresentazione che sia almeno 1 mese più avanti della prenotazione
	private static Rappresentazione getRappresentazionePost(Date date) {
		Rappresentazione rapp = null;
		DateTime dt = new DateTime(date).plusDays(30);
		Filter filter = new Compare.Greater(
				Rappresentazione_.dataRappresentazione.getName(), dt.toDate());
		EntityManager manager = EM.createEntityManager();
		EROContainer container = new EROContainer(Rappresentazione.class,
				manager);
		container.addContainerFilter(filter);
		int max = container.size();
		if (max > 0) {
			int random = getRandom(max);
			Object itemId = container.getIdByIndex(random);
			EntityItem<EventoEntity> rappEntity = container.getItem(itemId);
			if (rappEntity != null) {
				rapp = (Rappresentazione) rappEntity.getEntity();
			}
		}
		manager.close();
		return rapp;
	}

	/**
	 * Crea gli allegati
	 */
	public static void creaAllegati() {
		ArrayList<Allegato> lista = AllegatoModulo.getDemoData();
		for (Allegato allegato : lista) {
			allegato.save();
		}
	}// end of method

	/**
	 * Crea le aziende
	 */
	public static void creaCompany() {
		Company company;

		company = new Company();
		company.setName("Asteria");
		company.save();

		company = new Company();
		company.setName("Algos");
		company.save();

	}// end of method

	/**
	 * Crea le lettere mancanti
	 */
	public static void creaLettere() {
		Lettera lettera;

		// controlla che esistano tutti i modelli previsti, se non esistono li
		// crea
		for (ModelliLettere modello : ModelliLettere.values()) {
			String code = modello.getDbCode();
			BaseEntity entity = EQuery.queryOne(Lettera.class, Lettera_.sigla,
					code);
			if (entity == null) {
				lettera = LetteraModulo.getLetteraDemo(modello);
				lettera.save();
			}
		}

		// in ogni caso cancella e ricostruisce un record demo con
		// elencate le sostituzioni della Enumeration LetteraKeys
		String siglaDemo = ModelliLettere.demo.getDbCode();
		Lettera.sostituisce(siglaDemo, ModelliLettere.demo.getOggettoDefault(),
				LetteraKeys.getTestoDemo());

	}// end of method


}// end of class
