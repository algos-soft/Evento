package it.algos.evento;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraKeys;
import it.algos.evento.entities.lettera.Lettera_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.multiazienda.AsteriaMigration;
import it.algos.evento.pref.EventoPrefs;
import it.algos.web.BootStrap;
import it.algos.web.entity.BaseEntity;
import it.algos.web.query.AQuery;
import it.algos.evento.daemons.DaemonPrenScadute;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.List;

public class EventoBootStrap extends BootStrap {

	/**
	 * Executed on container startup
	 * <p>
	 * Setup non-UI logic here
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
		ServletContext svltCtx = super.getServletContext();

		// registra il servlet context non appena è disponibile
		EventoApp.setServletContext(svltCtx);

		// Creo l'azienda Asteria se non esiste.
		AsteriaMigration.ensureCompanyAsteria();
		
		// Creo l'azienda Demo se non esiste
		DemoDataGenerator.ensureCompanyDemo();

		// avvia lo scheduler controllo solleciti che esegue ogni ora
		if (EventoPrefs.startDaemonAtStartup.getBool()) {
			DaemonPrenScadute.getInstance().start();
		}

		//esegue qualcosa per ogni Company
		List<Company> comps = Company.query.getList();
		for(Company company : comps){
			doForCompany(company);
		}

	}// end of method


	/**
	 * Esegue per ogni company allo startup del server
	 */
	private void doForCompany(Company company) {

		// Controlla che esista la lettera demo con elencate
		// le sostituzioni della Enumeration LetteraKeys.
		// Se manca la crea ora.
		Container.Filter fComp = new Compare.Equal(Lettera_.company.getName(), company);
		Container.Filter fType = new Compare.Equal(Lettera_.sigla.getName(), ModelliLettere.demo.getDbCode());
		Container.Filter filter = new And(fComp,fType);
		BaseEntity entity = AQuery.getEntity(Lettera.class, filter);
		if (entity==null){
			String siglaDemo = ModelliLettere.demo.getDbCode();
			Lettera lettera = new Lettera(siglaDemo, ModelliLettere.demo.getOggettoDefault(), LetteraKeys.getTestoDemo());
			lettera.setCompany(company);
			lettera.save();
		}
	}


	/**
	 * Tear down logic here<br>
	 * Sovrascritto
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		// arresta gli schedulers
		DaemonPrenScadute.getInstance().stop();

		super.contextDestroyed(sce);

	}// end of method

	/**
	 * @retturn the name of the current user
	 */
	public static String getUsername() {
		return ""; // not implemented
	}

}// end of class
