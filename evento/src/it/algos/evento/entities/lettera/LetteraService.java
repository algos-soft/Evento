package it.algos.evento.entities.lettera;

import it.algos.evento.pref.CompanyPrefs;
import it.algos.evento.pref.EventoPrefs;
import it.algos.web.lib.Lib;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import it.algos.evento.entities.spedizione.Spedizione;

import java.util.ArrayList;
import java.util.HashMap;

import javax.activation.DataSource;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;

public class LetteraService {

	private static final String ESCAPE_INI = "${";
	private static final String ESCAPE_END = "}";

	public static String getTesto(String testoIn,
			HashMap<String, String> mappaEscape) {
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

	public static Spedizione spedisci(LetteraMap mappaEscape,
			HashMap<String, Object> mappaMail)  {
		Spedizione spedizione = new Spedizione();
		boolean spedita = false;
		String testoMail = null;
		Object modLettera = null;
		ModelliLettere modelloLettera = null;
		Lettera lettera = null;
		String oggetto = "";
		String destinatario = null;
		HashMap<String, String> mappaEsc = mappaEscape.getEscapeMap();
		String errore = null;
		String allegati = "";

		if (mappaMail != null) {
			modLettera = mappaMail.get(MailKeys.modello.getKey());
			if (modLettera != null) {
				if (modLettera instanceof ModelliLettere) {
					modelloLettera = (ModelliLettere) modLettera;
				}// end of if cycle
			}// end of if cycle
		}// end of if cycle

		if (modelloLettera != null) {
			lettera = modelloLettera.getLettera();
		}// end of if cycle

		if (lettera != null) {
			testoMail = lettera.getTestOut(mappaEsc);

			if (mappaMail.get(MailKeys.destinatario.getKey()) != null) {
				destinatario = (String) mappaMail.get(MailKeys.destinatario
						.getKey());
			}// end of if cycle

			if (mappaMail.get(MailKeys.oggetto.getKey()) != null) {
				oggetto = (String) mappaMail.get(MailKeys.oggetto.getKey());
			} else {
				if (lettera != null) {
					oggetto = lettera.getOggetto();
				}// end of if cycle
			}// end of if/else cycle

			allegati = lettera.getAllegati();

			try {
				spedita = LetteraService.sendMail(destinatario, oggetto,
						testoMail, lettera.isHtml(), allegati);
			} catch (EmailException e) {
				errore = e.getMessage();
				spedizione.setErrore(errore);
			}
			spedizione.setLettera(lettera);
			spedizione.setDestinatario(destinatario);
			spedizione.setSpedita(spedita);
		}// end of if cycle

		spedizione.save();

		return spedizione;
	}// end of method

	public static boolean sendMail(String dest, String oggetto, String testo, boolean html)
			throws EmailException {
		return sendMail(dest, oggetto, testo, html, "");
	}// end of method

	public static boolean sendMail(String dest, String oggetto, String testo, boolean html,
			String allegati) throws EmailException {
		boolean spedita = false;
		String hostName = "";
		String username = "";
		String password = "";
		String from = "";
		boolean useAuth=false;
		int smtpPort;
		

		// --legge dalle preferenze
		hostName = EventoPrefs.smtpServer.getString();
		username = EventoPrefs.smtpUserName.getString();
		password = EventoPrefs.smtpPassword.getString();
		useAuth = EventoPrefs.smtpUseAuth.getBool();
		smtpPort = EventoPrefs.smtpPort.getInt();
		from = CompanyPrefs.senderEmailAddress.getString();

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
		if (allegati!=null && !allegati.equals("")) {
			ArrayList<String> listaAllegati = Lib.getArrayDaTesto(allegati);
			for (String name : listaAllegati) {
				DataSource ds = AllegatoModulo.getDataSource(name);
				String disposition = EmailAttachment.ATTACHMENT;
				email.attach(ds, name, name, disposition);
			}
		}

		// Create the email message
		if(hostName!=null&&!hostName.equals("")){
			email.setHostName(hostName);
		}

		email.setSmtpPort(smtpPort);
		
		if (useAuth) {
			email.setAuthenticator(new DefaultAuthenticator(nickName, password));
			//email.setSSLOnConnect(false);
			email.setStartTLSEnabled(true);
		}

		if(from!=null&&!from.equals("")){
			email.setFrom(from);
		}

		if(oggetto!=null&&!oggetto.equals("")){
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

		if(dest!=null&&!dest.equals("")){
			String[] list=dest.split(",");
			for(String addr : list){
				addr=addr.trim();
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