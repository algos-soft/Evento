package it.algos.evento;

import it.algos.web.AlgosApp;


/**
 * Contenitore di costanti della applicazione
 */
public abstract class EventoApp extends AlgosApp {

    /**
     * the application name
     */
    public static final String APP_NAME = "eVento";

    /**
     * Nome dell'utente bot
     */
    public static final String BOT_USER = "SYS_ROBOT";

//	/**
//	 * modo programmatore
//	 */
//	public static Boolean MODO_PROG = false;

//	/**
//	 * modo manager
//	 */
//	public static Boolean MANAGER = false;

//	/**
//	 * La azienda corrente
//	 */
//	public static Company COMPANY = null;

    /**
     * Nome della pagina di conferma prenotazione
     */
    public static final String CONF_PAGE = "conf";

    /**
     * Nome della pagina del manager
     */
    public static final String MANAGER_PAGE = "manager";

    /**
     * chiave per la property user id nella session
     */
    public static final String KEY_USERID = "userid";

    /**
     * chiave per il flag di utilizzo funzionalità Gestione Mailing Integrata
     */
    public static final Boolean USA_GESTIONE_MAILING = true;

    private static final String KEY_SPLASHIMAGE = "splashimage";
    private static final String KEY_MENUBAR_ICON = "menubaricon";

    /**
     * Name of the local folder for images.<br>
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/data/img/";


}// end of static abstract class
