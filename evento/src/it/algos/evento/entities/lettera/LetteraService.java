package it.algos.evento.entities.lettera;

import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.pref.EventoPrefs;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibSecurity;
import it.algos.webbase.web.lib.LibSession;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;

import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.HashMap;

public class LetteraService {

    private static final String ESCAPE_INI = "${";
    private static final String ESCAPE_END = "}";

    public static String getTesto(String testoIn, HashMap<String, String> mappaEscape) {
        String testoOut = testoIn;
        String chiave = "";
        String valore = "";
        int posIni;
        int posEnd;
        String prima;
        String dopo;

        if (mappaEscape != null) {
            for (Object esc : mappaEscape.keySet()) {
                chiave = ESCAPE_INI + esc + ESCAPE_END;
                valore = mappaEscape.get(esc);

                if (testoOut.contains(chiave)) {
                    do {
                        posIni = testoOut.indexOf(chiave);
                        posEnd = posIni + chiave.length();
                        prima = testoOut.substring(0, posIni);
                        dopo = testoOut.substring(posEnd);
                        testoOut = prima + valore + dopo;
                    } while (testoOut.contains(chiave));
                }// end of if cycle

            }// end of for cycle
        }// end of if cycle

        return testoOut;
    }// end of method

    public static Spedizione spedisci(Lettera lettera, LetteraMap mappaEscape, HashMap<String, Object> mappaMail) {
        Spedizione spedizione = new Spedizione();
        boolean spedita = false;
        String testoMail = null;
        HashMap<String, String> mappaEsc = mappaEscape.getEscapeMap();
        String errore = null;
        String allegati = "";
        Object obj;

        testoMail = lettera.getTestOut(mappaEsc);

        String from = null;
        obj = mappaMail.get(MailKeys.from.getKey());
        if (obj != null) {
            from = (String) obj;
        }

        String destinatario = null;
        obj = mappaMail.get(MailKeys.destinatario.getKey());
        if (obj != null) {
            destinatario = (String) obj;
        }

        String oggetto = "";
        obj = mappaMail.get(MailKeys.oggetto.getKey());
        if (obj != null) {
            oggetto = (String) obj;
        } else {
            oggetto = lettera.getOggetto();
        }

        allegati = lettera.getAllegati();

        try {
            spedita = LetteraService.sendMail(from, destinatario, oggetto,
                    testoMail, lettera.isHtml(), allegati);
        } catch (EmailException e) {
            errore = e.getMessage();
            spedizione.setErrore(errore);
        }
        spedizione.setLettera(lettera);
        spedizione.setDestinatario(destinatario);
        spedizione.setSpedita(spedita);

        spedizione.save();

        return spedizione;
    }// end of method

    /**
     * Invia una email.
     *
     * @param from    il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param dest    il destinatario
     * @param oggetto l'oggetto della mail
     * @param testo   il corpo della mail
     * @return true se spedita correttamente
     */
    public static boolean sendMail(String from, String dest, String oggetto, String testo) throws EmailException {
        return sendMail(from, dest, oggetto, testo, true);
    }// end of method

    /**
     * Invia una email.
     *
     * @param from    il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param dest    il destinatario
     * @param oggetto l'oggetto della mail
     * @param testo   il corpo della mail
     * @param html   true se è una mail html
     * @return true se spedita correttamente
     */
    public static boolean sendMail(String from, String dest, String oggetto, String testo, boolean html) throws EmailException {
        return sendMail(from, dest, oggetto, testo, html, "");
    }// end of method


    /**
     * Invia una email.
     *
     * @param from    il mittente, se null o vuoto usa l'indirizzo della company corrente
     * @param dest    il destinatario
     * @param oggetto l'oggetto della mail
     * @param testo   il corpo della mail
     * @param html   true se è una mail html
     * @param allegati elenco dei nomi degli allegati (comma-separated string)
     * @return true se spedita correttamente
     */
    public static boolean sendMail(String from, String dest, String oggetto, String testo, boolean html, String allegati) throws EmailException {
        boolean spedita = false;
        String hostName = "";
        String username = "";
        String password = "";
        boolean useAuth = false;
        int smtpPort;


        // --legge dalle preferenze
        hostName = EventoPrefs.smtpServer.getString();
        username = EventoPrefs.smtpUserName.getString();
        password = EventoPrefs.smtpPassword.getString();
        useAuth = EventoPrefs.smtpUseAuth.getBool();
        smtpPort = EventoPrefs.smtpPort.getInt();

        // se from non è specificato usa quello della company corrente
        if ((from == null) || (from.equals(""))){
            from = CompanyPrefs.senderEmailAddress.getString();
        }

        // spedisce
        spedita = sendMail(hostName, smtpPort, useAuth, username, password, from, dest, oggetto, testo, html, allegati);

        return spedita;
    }// end of method


    public static boolean sendMail(String hostName, int smtpPort, boolean useAuth, String nickName,
                                   String password, String from, String dest, String oggetto,
                                   String testo, boolean html, String allegati) throws EmailException {
        boolean spedita = false;
        ImageHtmlEmail email;


        email = new ImageHtmlEmail();

        //adds attachments
        if (allegati != null && !allegati.equals("")) {
            ArrayList<String> listaAllegati = Lib.getArrayDaTesto(allegati);
            for (String name : listaAllegati) {
                DataSource ds = AllegatoModulo.getDataSource(name);
                String disposition = EmailAttachment.ATTACHMENT;
                email.attach(ds, name, name, disposition);
            }
        }

        // Create the email message
        if (hostName != null && !hostName.equals("")) {
            email.setHostName(hostName);
        }

        email.setSmtpPort(smtpPort);

        if (useAuth) {
            email.setAuthenticator(new DefaultAuthenticator(nickName, password));
            //email.setSSLOnConnect(false);
            email.setStartTLSEnabled(true);
        }

        if (from != null && !from.equals("")) {
            email.setFrom(from);
        }

        if (oggetto != null && !oggetto.equals("")) {
            email.setSubject(oggetto);
        }

        // aggiunge email di backup se configurato
        if (CompanyPrefs.backupEmail.getBool()) {
            String backupAddress = CompanyPrefs.backupEmailAddress.getString();
            if (!(backupAddress.equals(""))) {
                email.addBcc(backupAddress);
            }
        }


        if (html) {
            email.setHtmlMsg(testo);
        } else {
            email.setMsg(testo);
        }

        if (dest != null && !dest.equals("")) {
            String[] list = dest.split(",");
            for (String addr : list) {
                addr = addr.trim();
                email.addTo(addr);
            }
        }

        // set a data source resolver to resolve embedded images
        if (html) {
            ImageResolver resolver = new ImageResolver();
            email.setDataSourceResolver(resolver);
        }


        // send the email
        email.send();
        spedita = true;

        return spedita;
    }// end of method


}// end of class