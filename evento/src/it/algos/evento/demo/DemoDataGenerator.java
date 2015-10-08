package it.algos.evento.demo;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.EventoApp;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.company.Company_;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.comune.ComuneImport;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraModulo;
import it.algos.evento.entities.lettera.Lettera_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.ordinescuola.OrdineScuola_;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.stagione.Stagione_;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//import it.algos.evento.multiazienda.EROContainer;

public class DemoDataGenerator {


    /**
     * Codice della demo company
     */
    private static final String DEMO_COMPANY_CODE = "demo";

    /**
     * Creazione dei dati iniziali per una data azienda.
     * <p>
     *
     * @param company - l'azienda di riferimento Attenzione! <br>
     *                L'ordine di creazione delle varie tavole deve rispettare le
     *                relazioni che esistono tra di esse. <br>
     */
    public static void createDemoData(Company company) {


        try {

            // In questa classe devo sempre registrare la company nei record prima di salvare
            // perché questo codice può essere eseguito dal server prima che sia avviata
            // una sessione e quindi non posso assumere di poter prendere
            // la company dalla sessione.
            // In questa classe non posso quindi usare classi come EQuery o EContainer
            // che sono internamente filtrate in base alla Company che si
            // troverebbe nella sessione.
            // Anche nell'uso delle preferenze la company va sempre esplicitata

            if (getCount(Allegato.class, company) == 0) {
                creaAllegati(company);
            }
            if (getCount(Lettera.class, company) == 0) {
                creaLettere(company);
            }
            if (getCount(Sala.class, company) == 0) {
                creaSale(company);
            }
            if (getCount(Progetto.class, company) == 0) {
                creaProgetti(company);
            }
            if (getCount(ModoPagamento.class, company) == 0) {
                creaPagamenti(company);
            }
            if (getCount(TipoRicevuta.class, company) == 0) {
                creaTipiRicevuta(company);
            }
            if (getCount(OrdineScuola.class, company) == 0) {
                creaOrdiniScuola(company);
            }
            if (getCount(Insegnante.class, company) == 0) {
                creaInsegnanti(company);
            }
            if (getCount(Comune.class, company) == 0) {
                creaComuni(company);
            }
            if (getCount(Scuola.class, company) == 0) {
                creaScuole(company);
            }
            if (getCount(Stagione.class, company) == 0) {
                creaStagioni(company);
            }
            if (getCount(Evento.class, company) == 0) {
                creaEventi(company);
            }
            if (getCount(Rappresentazione.class, company) == 0) {
                creaRappresentazioni(company);
            }
            if (getCount(Prenotazione.class, company) == 0) {
                creaPrenotazioni(company);
            }
            // personalizza le preferenze per questa azienda
            creaPreferenze(company);

            // cose eseguite per ogni company ad ogi avvio del server - le devo fare anche qui
            // dopo la creazione della nuova company.
            EventoBootStrap.doForCompany(company);

        } catch (Exception e) {
            e.printStackTrace();
        }


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
    private static void creaSale(Company company) {
        Sala sala;

        sala = new Sala("Auditorium", 430);
        sala.setCompany(company);
        sala.save();

        sala = new Sala("Odeon", 220);
        sala.setCompany(company);
        sala.save();

    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    private static void creaProgetti(Company company) {
        Progetto p;

        p = new Progetto("La scienza della vita");
        p.setCompany(company);
        p.save();

        p = new Progetto("Storia e memoria");
        p.setCompany(company);
        p.save();

        p = new Progetto("Popoli nel tempo");
        p.setCompany(company);
        p.save();

    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaPagamenti(Company company) {
        ModoPagamento m;

        m = new ModoPagamento("BB", "Bonifico bancario");
        m.setCompany(company);
        m.save();

        m = new ModoPagamento("VP", "Vaglia postale");
        m.setCompany(company);
        m.save();

        m = new ModoPagamento("CONT", "Contanti");
        m.setCompany(company);
        m.save();

    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaTipiRicevuta(Company company) {
        save(new TipoRicevuta("RIC", "Ricevuta"), company);
        save(new TipoRicevuta("FATT", "Fattura"), company);
        save(new TipoRicevuta("FE", "Fattura Elettronica"), company);
    }// end of method


    /**
     * Create some demo data only if the table is empty
     */
    public static void creaOrdiniScuola(Company company) {
        save(new OrdineScuola("INF", "Scuola dell'Infanzia"), company);
        save(new OrdineScuola("PRI", "Primaria"), company);
        save(new OrdineScuola("MED", "Secondaria I grado (medie)"), company);
        save(new OrdineScuola("SUP", "Secondaria II grado (superiori)"), company);
        save(new OrdineScuola("UNI", "Università"), company);
    }// end of method


    /**
     * Create some demo data only if the table is empty
     */
    public static void creaInsegnanti(Company company) {
        Insegnante ins;

        ins = new Insegnante("Lovecchio", "Luigi", "Prof.",
                "lovecchio.luigi@gmail.com", "matematica, scienze");
        ins.setOrdineScuola(getOrdineScuolaRandom(company));
        ins.setTelefono("348-784565");
        ins.setIndirizzo1("Via dei Gelsomini, 8");
        ins.setIndirizzo2("20154 Ferrara");
        ins.setCompany(company);
        ins.save();

        ins = new Insegnante("Ferrari", "Sara", "Prof.ssa",
                "ferrari.sara@gmail.com", "lettere");
        ins.setOrdineScuola(getOrdineScuolaRandom(company));
        ins.setTelefono("885-4455778");
        ins.setIndirizzo1("Via Garibaldi, 26");
        ins.setIndirizzo2("50145 Rovigo");
        ins.setCompany(company);
        ins.save();

        ins = new Insegnante("Sarfatti", "Lucia", "Prof.ssa",
                "lsarfatti@ymail.com", "disegno");
        ins.setOrdineScuola(getOrdineScuolaRandom(company));
        ins.setTelefono("999-5462335");
        ins.setIndirizzo1("Piazza Po, 12");
        ins.setIndirizzo2("56445 Mantova");
        ins.setCompany(company);
        ins.save();

        ins = new Insegnante("Gasparotti", "Antonella", "Prof.ssa",
                "agasparotti@tin.it", "storia, filosofia");
        ins.setOrdineScuola(getOrdineScuolaRandom(company));
        ins.setTelefono("884-6589998");
        ins.setIndirizzo1("Largo Brasilia, 22");
        ins.setIndirizzo2("20100 Milano");
        ins.setCompany(company);
        ins.save();

        ins = new Insegnante("Marinelli", "Laura", "Prof.ssa",
                "lmarinelli@hotmail.it", "lettere, storia");
        ins.setOrdineScuola(getOrdineScuolaRandom(company));
        ins.setTelefono("556-4456658");
        ins.setIndirizzo1("Via Vasco de Gama, 22");
        ins.setIndirizzo2("25556 Castelnuovo Val Tidone (PC)");
        ins.setCompany(company);
        ins.save();
    }// end of method

    /**
     * Crea i comuni
     */
    public static void creaComuni(Company company) {
        ServletContext svlContext = EventoApp.getServletContext();
        String path = "/" + AlgosApp.DEMODATA_FOLDER_NAME + "comuni/comuni.xls";
        String fullPath = svlContext.getRealPath(path);
        if (fullPath != null) {
            ComuneImport.doImport(fullPath, company);
        }
    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaScuole(Company company) {
        Scuola scuola;


//		OrdineScuola ordine = (OrdineScuola)EQuery.queryFirst(OrdineScuola.class, OrdineScuola_.sigla, "SUP");

        // cerca l'ordine "SUP"
        Filter f1 = new Compare.Equal(Evento_.company.getName(), company);
        Filter f2 = new Compare.Equal(OrdineScuola_.sigla.getName(), "SUP");
        Filter f3 = new And(f1, f2);
        List<BaseEntity> entities = AQuery.getList(OrdineScuola.class, f3);
        OrdineScuola ordine = null;
        if (entities.size() > 0) {
            ordine = (OrdineScuola) entities.get(0);
        }

        scuola = new Scuola("Beccaria", "Liceo Classico Beccaria",
                getComuneRandom(company), ordine);
        scuola.setIndirizzo("Via Carlo Linneo,5");
        scuola.setCap("20145");
        scuola.setTelefono("025-365487");
        scuola.setEmail("liceobeccaria@yahoo.com");
        scuola.setCompany(company);
        scuola.save();

        scuola = new Scuola("Rampaldi", "Istituto Tecnico Rampaldi",
                getComuneRandom(company), ordine);
        scuola.setIndirizzo("Via Varzi N. 16");
        scuola.setCap("56554");
        scuola.setTelefono("125-2356487");
        scuola.setEmail("istrampaldi@yahoo.com");
        scuola.setCompany(company);
        scuola.save();

        scuola = new Scuola("Leonardo", "Liceo Scientifico Leonardo da Vinci",
                getComuneRandom(company), ordine);
        scuola.setIndirizzo("Via Stazione 1");
        scuola.setCap("36665");
        scuola.setTelefono("035-564789");
        scuola.setEmail("liceoleonardo@yahoo.com");
        scuola.setCompany(company);
        scuola.save();

        scuola = new Scuola("Falcone", "Istituto Magistrale Giovanni Falcone", ordine);
        scuola.setIndirizzo("Via Dunant, 1");
        scuola.setCap("24128");
        scuola.setTelefono("035-6598745");
        scuola.setEmail("istfalcone@yahoo.com");
        scuola.setCompany(company);
        scuola.save();

        scuola = new Scuola("Rota", "Istituto Superiore Lorenzo Rota",
                getComuneRandom(company), ordine);
        scuola.setIndirizzo("Via Lavello, 17");
        scuola.setCap("55664");
        scuola.setTelefono("023-564789");
        scuola.setEmail("istitutorota@yahoo.com");
        scuola.setCompany(company);
        scuola.save();

    }// end of method

    /**
     * Assegna la company e salva
     */
    private static void save(EventoEntity entity, Company company) {
        entity.setCompany(company);
        entity.save();
    }


    private static Comune getComuneRandom(Company company) {
        return (Comune) getEntityRandom(Comune.class, company);
    }

    private static OrdineScuola getOrdineScuolaRandom(Company company) {
        return (OrdineScuola) getEntityRandom(OrdineScuola.class, company);
    }


    private static Progetto getProgettoRandom(Company company) {
        return (Progetto) getEntityRandom(Progetto.class, company);
    }

    private static BaseEntity getEntityRandom(Class clazz, Company company) {
        Filter filter = new Compare.Equal(Evento_.company.getName(), company);
        List<BaseEntity> entities = AQuery.getList(clazz, filter);
        //List<EventoEntity> entities = (List<EventoEntity>) EQuery.getList(clazz);
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
    public static void creaStagioni(Company company) {

        // fino a fine maggio crea la stagione iniziata l'anno precedente
        // da giugno crea la stagione che inizia quest'anno
        DateTime dt = new DateTime();
        int year = dt.getYear();
        int month = dt.getMonthOfYear();  // where January is 1 and December is 12
        int yearStart;
        DateTime dStart, dEnd;
        if (month < 6) {
            yearStart = year - 1;
        } else {
            yearStart = year;
        }
        dStart = new DateTime(yearStart, 6, 1, 0, 0, 0);
        dEnd = dStart.plusYears(1).minusDays(1);
        String sigla = "" + dStart.getYear() + "-" + dEnd.getYear();

        Stagione stagione = new Stagione();
        stagione.setSigla(sigla);
        stagione.setDatainizio(dStart.toDate());
        stagione.setDatafine(dEnd.toDate());
        stagione.setCorrente(true);
        stagione.setCompany(company);
        stagione.save();

    }

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaEventi(Company company) {

        Evento evento;
        evento = new Evento("Vivarelli", "Un ricordo di Roberto Vivarelli", 16,
                9, 0, 5);
        saveEvento(evento, company);

        evento = new Evento("Accademia", "Accademia Bizantina", 16, 9, 0, 5);
        saveEvento(evento, company);

        evento = new Evento("Gentile", "L'assassinio di Giovanni Gentile", 12,
                7, 0, 5);
        saveEvento(evento, company);

        evento = new Evento("Chimica", "Le Frontiere della Chimica", 12, 7, 0, 5);
        saveEvento(evento, company);

        evento = new Evento("Big Bang",
                "Big Bang: l'inizio e la fine nelle stelle", 18, 10, 0, 8);
        saveEvento(evento, company);

        evento = new Evento("Auschwitz", "Auschwitz - parla un testimone", 15,
                8, 0, 5);
        saveEvento(evento, company);

        evento = new Evento("Mafia",
                "Cercando la verità nel labirinto della mafia", 10, 8, 0, 5);
        saveEvento(evento, company);

        evento = new Evento("Shackleton", "Sulle orme di Ernest Shackleton",
                16, 8, 0, 5);
        saveEvento(evento, company);

    }// end of method

    private static void saveEvento(Evento evento, Company company) {

        Filter f1 = new Compare.Equal(Evento_.company.getName(), company);
        Filter f2 = new Compare.Equal(Stagione_.corrente.getName(), true);
        Filter f3 = new And(f1, f2);
        ArrayList<BaseEntity> lista = AQuery.getList(Stagione.class, f3);
        Stagione stagione = null;
        if (lista.size() > 0) {
            stagione = (Stagione) lista.get(0);
        }
//		Stagione stagione = (Stagione)EQuery.getEntity(Stagione.class, filter);
        evento.setProgetto(getProgettoRandom(company));
        evento.setStagione(stagione);
        evento.setCompany(company);
        evento.save();
    }


    private static int getCount(Class<?> clazz, Company company) {
        long num = AQuery.getCount(clazz, Evento_.company, company);
        return (int) num;
    }

    private static void creaRappresentazioni(Company company) {
        Rappresentazione rapp;

        MutableDateTime dt = new MutableDateTime(2014, 11, 1, 0, 0, 0, 0);

        for (int i = 0; i < 40; i++) {

            dt.addDays(getRandom(5));
            if (getRandom(2) == 1) {
                dt.setTime(10, 30, 0, 0);
            } else {
                dt.setTime(15, 0, 0, 0);
            }
            Evento evento = (Evento) getEntityRandom(Evento.class, company);
            Sala sala = (Sala) getEntityRandom(Sala.class, company);

            rapp = new Rappresentazione();
            rapp.setEvento(evento);
            rapp.setDataRappresentazione(dt.toDate());
            rapp.setSala(sala);
            rapp.setCapienza(sala.getCapienza());
            rapp.setCompany(company);
            rapp.save();

        }
    }

    /**
     * Crea un certo numero di prenotazioni.
     * <p>
     *
     * @return una lista delle prenotazioni create
     */
    public static ArrayList<Prenotazione> creaPrenotazioni(Company company) {
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
                Rappresentazione rapp = getRappresentazionePost(dt.toDate(), company);

                if (rapp != null) {

                    int nInteri = getRandom(80);
                    int nRidotti = getRandom(3);
                    int nDisabili = getRandom(4);
                    int nAccomp = getRandom(4);


                    pren = new Prenotazione();
                    int numpren = CompanyPrefs.nextNumPren.getInt(company);
                    pren.setNumPrenotazione(numpren);
                    pren.setDataPrenotazione(dt.toDate());
                    pren.setRappresentazione(rapp);
                    pren.setScuola((Scuola) getEntityRandom(Scuola.class, company));
                    pren.setInsegnante((Insegnante) getEntityRandom(Insegnante.class, company));
                    pren.setEmailRiferimento(pren.getInsegnante().getEmail());
                    pren.setTelRiferimento(pren.getInsegnante().getTelefono());
                    pren.setClasse("4D");

                    pren.setNumInteri(nInteri);
                    pren.setNumRidotti(nRidotti);
                    pren.setNumDisabili(nDisabili);
                    pren.setNumAccomp(nAccomp);

                    pren.setModoPagamento((ModoPagamento) getEntityRandom(ModoPagamento.class, company));

                    DateTime scadConf = new DateTime(pren.getDataPrenotazione())
                            .plusDays(CompanyPrefs.ggScadConfermaPrenotazione.getInt(company));
                    pren.setScadenzaConferma(scadConf.toDate());

                    DateTime dataRapp = new DateTime(pren.getRappresentazione()
                            .getDataRappresentazione());
                    DateTime scadPaga = dataRapp
                            .minusDays(CompanyPrefs.ggScadConfermaPagamento.getInt(company));
                    pren.setScadenzaPagamento(scadPaga.toDate());

                    // importi
                    Evento e = pren.getRappresentazione().getEvento();
                    BigDecimal iIntero=e.getImportoIntero();
                    BigDecimal iRidotto=e.getImportoRidotto();
                    BigDecimal iDisabili=e.getImportoDisabili();
                    BigDecimal iAccomp=e.getImportoAccomp();
					pren.setImportoIntero(iIntero);
                    pren.setImportoRidotto(iRidotto);
                    pren.setImportoDisabili(iDisabili);
                    pren.setImportoAccomp(iAccomp);
                    BigDecimal totPren = Prenotazione.getTotImporto(nInteri, nRidotti, nDisabili, nAccomp, iIntero, iRidotto, iDisabili, iAccomp);
					pren.setImportoDaPagare(totPren);

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

                    pren.setCompany(company);

                    manager.persist(pren);

                    prenotazioni.add(pren);

                    System.out.println("create " + i + " -> " + pren);

                    int nextnum = CompanyPrefs.nextNumPren.getInt(company) + 1;
                    CompanyPrefs.nextNumPren.put(company, nextnum);

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
    private static Rappresentazione getRappresentazionePost(Date date, Company company) {
        Rappresentazione rapp = null;
        DateTime dt = new DateTime(date).plusDays(30);
        Filter f1 = new Compare.Equal(Evento_.company.getName(), company);
        Filter f2 = new Compare.Greater(Rappresentazione_.dataRappresentazione.getName(), dt.toDate());
        Filter filter = new And(f1, f2);

        EntityManager manager = EM.createEntityManager();
        JPAContainer container = new JPAContainer(Rappresentazione.class);
        LocalEntityProvider provider = new LocalEntityProvider(Rappresentazione.class, manager);
        container.setEntityProvider(provider);
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
    public static void creaAllegati(Company company) {
        ArrayList<Allegato> lista = AllegatoModulo.getDemoData();
        for (Allegato allegato : lista) {
            allegato.setCompany(company);
            allegato.save();
        }
    }// end of method


    /**
     * Crea le lettere mancanti
     */
    public static void creaLettere(Company company) {
        Lettera lettera;

        // controlla che esistano tutti i modelli previsti, se non esistono li crea
        for (ModelliLettere modello : ModelliLettere.values()) {
            String code = modello.getDbCode();
            Filter f1 = new Compare.Equal(Evento_.company.getName(), company);
            Filter f2 = new Compare.Equal(Lettera_.sigla.getName(), code);
            Filter f3 = new And(f1, f2);
            List<BaseEntity> entities = AQuery.getList(Lettera.class, f3);
            if (entities.size() == 0) {
                lettera = LetteraModulo.getLetteraDemo(modello);
                lettera.setCompany(company);
                lettera.save();
            }
        }

    }// end of method

    /**
     * Crea le preferenze
     */
    private static void creaPreferenze(Company company){
        // registra l'indirizzo della company come mittente delle email
        CompanyPrefs.senderEmailAddress.put(company, company.getEmail());
    }


}// end of class
