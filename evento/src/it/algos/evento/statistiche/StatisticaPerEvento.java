package it.algos.evento.statistiche;

import it.algos.evento.multiazienda.EQuery;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.scuola.Scuola;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

@SuppressWarnings("serial")
public class StatisticaPerEvento extends StatisticaBase {

	public StatisticaPerEvento() {
		super("Statistica per evento");
	}// end of constructor

	/**
	 * crea il container con le colonne
	 */
	@Override
	protected void creaContainer() {
		container = new IndexedContainer();
		addContainerProperty(Colonne.siglaEvento);
		addContainerProperty(Colonne.titoloEvento);
		addContainerProperty(Colonne.numscuole);
		addContainerProperty(Colonne.numrappresentazioni);
		addContainerProperty(Colonne.interi);
		addContainerProperty(Colonne.ridotti);
		addContainerProperty(Colonne.disabili);
		addContainerProperty(Colonne.accomp);
		addContainerProperty(Colonne.totSpettatori);
		addContainerProperty(Colonne.capienza);
		addContainerProperty(Colonne.totPagare);
		addContainerProperty(Colonne.totPagato);
	}// end of method

	
	/**
	 * popola il container con i dati
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void popola(Date data1, Date data2) {

		HashMap<Evento, WrapEvento> mappa = new HashMap<Evento, WrapEvento>();
		
		// lista delle rappresentazioni nel periodo
		ArrayList<Rappresentazione> lista = super.getRappresentazioni(data1, data2);
		for (Rappresentazione rapp : lista) {
			Evento evento = rapp.getEvento();
			WrapEvento wrapper = mappa.get(evento);
			if (wrapper==null) {
				wrapper = new WrapEvento();
				mappa.put(evento, wrapper);
				wrapper.setSiglaEvento(evento.getSigla());
				wrapper.setTitoloEvento(evento.getTitolo());
			}

			wrapper.addCapienza(rapp.getCapienza());
			wrapper.addNumRappresentazioni(1);
			
			// spazzola le prenotazioni e aggiunge i valori
			List<Prenotazione> listaPren = (List<Prenotazione>)EQuery.queryList(Prenotazione.class, Prenotazione_.rappresentazione, rapp);
			List<Scuola> scuole=new ArrayList<Scuola>();
			int numScuole=0;
			for (Prenotazione pren : listaPren) {
				wrapper.addInteri(pren.getNumInteri());
				wrapper.addRidotti(pren.getNumRidotti());
				wrapper.addOmaggi(pren.getNumDisabili());
				wrapper.addTotPagare(pren.getImportoDaPagare());
				wrapper.addTotPagato(pren.getImportoPagato());
				
				// rileva il cambio scuola per calcolare il numero di scuole
				if (!scuole.contains(pren.getScuola())) {
					scuole.add(pren.getScuola());
					numScuole++;
				}
			}
			wrapper.addNumscuole(numScuole);
			
		}
		
		
		// ordina le righe
		ArrayList<WrapEvento> wrappers = new ArrayList<WrapEvento>();
		for (WrapEvento wrapper : mappa.values()) {
			wrappers.add(wrapper);
		}
		Collections.sort(wrappers);
		
		// aggiunge le righe al container
		for (WrapEvento wrapper : wrappers) {
			addRiga(wrapper);
		}
		
	}// end of method
	
	
	@SuppressWarnings("unchecked")
	private void addRiga(WrapEvento wrapEvento) {
		
		int numInteri = wrapEvento.getInteri();
		int numRidotti = wrapEvento.getRidotti();
		int numOmaggi = wrapEvento.getOmaggi();
		int numScuole = wrapEvento.getNumscuole();
		int numRapp = wrapEvento.getNumRappresentazioni();
		int capienza = wrapEvento.getCapienza();
		BigDecimal pagare = wrapEvento.getTotPagare();
		BigDecimal pagato = wrapEvento.getTotPagato();
		
		Item item = container.getItem(container.addItem());
		item.getItemProperty(Colonne.siglaEvento.getTitolo()).setValue(wrapEvento.getSiglaEvento());
		item.getItemProperty(Colonne.titoloEvento.getTitolo()).setValue(wrapEvento.getTitoloEvento());
		item.getItemProperty(Colonne.numscuole.getTitolo()).setValue(numScuole);
		item.getItemProperty(Colonne.numrappresentazioni.getTitolo()).setValue(numRapp);
		item.getItemProperty(Colonne.capienza.getTitolo()).setValue(capienza);
		item.getItemProperty(Colonne.interi.getTitolo()).setValue(numInteri);
		item.getItemProperty(Colonne.ridotti.getTitolo()).setValue(numRidotti);
		item.getItemProperty(Colonne.disabili.getTitolo()).setValue(numOmaggi);
		item.getItemProperty(Colonne.totSpettatori.getTitolo()).setValue(wrapEvento.getTotSpettatori());
		item.getItemProperty(Colonne.totPagare.getTitolo()).setValue(pagare.doubleValue());
		item.getItemProperty(Colonne.totPagato.getTitolo()).setValue(pagato.doubleValue());

		// incrementa i totali
		WrapTotali wt = new WrapTotali();
		wt.setTotInteri(numInteri);
		wt.setTotRidotti(numRidotti);
		wt.setTotOmaggi(numOmaggi);
		wt.setTotScuole(numScuole);
		wt.setTotRappresentazioni(numRapp);
		wt.setTotCapienza(capienza);
		wt.setTotPagare(pagare);
		wt.setTotPagato(pagato);
		super.addTotali(wt);
		
	}// end of method
	
	
	
	private class WrapEvento implements Comparable {
		String siglaEvento;
		String titoloEvento;
		int numscuole;
		int numRappresentazioni;
		int capienza;
		int interi;
		int ridotti;
		int omaggi;
		BigDecimal totPagare=new BigDecimal(0);
		BigDecimal totPagato=new BigDecimal(0);
		public WrapEvento() {
			super();
		}
		public String getSiglaEvento() {
			return siglaEvento;
		}
		public void setSiglaEvento(String siglaEvento) {
			this.siglaEvento = siglaEvento;
		}
		public String getTitoloEvento() {
			return titoloEvento;
		}
		public void setTitoloEvento(String titoloEvento) {
			this.titoloEvento = titoloEvento;
		}
		public int getNumscuole() {
			return numscuole;
		}
		public void setNumscuole(int numscuole) {
			this.numscuole = numscuole;
		}
		public int getNumRappresentazioni() {
			return numRappresentazioni;
		}
		public int getCapienza() {
			return capienza;
		}
		public void setCapienza(int capienza) {
			this.capienza = capienza;
		}
		public int getInteri() {
			return interi;
		}
		public void setInteri(int num) {
			this.interi = num;
		}
		public int getRidotti() {
			return ridotti;
		}
		public void setRidotti(int num) {
			this.ridotti = num;
		}
		public int getOmaggi() {
			return omaggi;
		}
		public void setOmaggi(int num) {
			this.omaggi = num;
		}
		public BigDecimal getTotPagare() {
			return totPagare;
		}
		public void setTotPagare(BigDecimal totPagare) {
			this.totPagare = totPagare;
		}
		public BigDecimal getTotPagato() {
			return totPagato;
		}
		public void setTotPagato(BigDecimal totPagato) {
			this.totPagato = totPagato;
		}
		public int getTotSpettatori(){
			return getInteri()+getRidotti()+getOmaggi();
		}
		
		// metodi per aggiungere alle variabili
		public void addNumscuole(int numscuole) {
			this.numscuole += numscuole;
		}
		public void addNumRappresentazioni(int num) {
			this.numRappresentazioni += num;
		}
		public void addCapienza(int capienza) {
			this.capienza += capienza;
		}
		public void addInteri(int num) {
			this.interi += num;
		}
		public void addRidotti(int num) {
			this.ridotti += num;
		}
		public void addOmaggi(int num) {
			this.omaggi += num;
		}
		public void addTotPagare(BigDecimal totPagare) {
			if (totPagare!=null) {
				this.totPagare = this.totPagare.add(totPagare);				
			}
		}
		public void addTotPagato(BigDecimal totPagato) {
			if (totPagato!=null) {
				this.totPagato = this.totPagato.add(totPagato);				
			}
		}
		@Override
		public int compareTo(Object o) {
			int value = 0;
			if (o instanceof WrapEvento) {
				WrapEvento other = (WrapEvento)o;
				value = siglaEvento.compareTo(other.getSiglaEvento());
			}
			return value;
		}
		
	}

}
