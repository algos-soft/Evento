package it.algos.evento.entities.prenotazione.eventi;


import com.vaadin.data.Container;
import it.algos.webbase.web.table.ATable;

import javax.persistence.EntityManager;

@SuppressWarnings("serial")
public class EventiInPrenTable extends ATable {

	
	public EventiInPrenTable(EntityManager em) {
		super(EventoPren.class, em);
	}

	@Override
	public Container createContainer() {
		return null;
	}

	protected Object[] getDisplayColumns() {
		return new Object[] { EventoPren_.timestamp, EventoPren_.tipo, EventoPren_.dettagli, EventoPren_.user , EventoPrenTable.colEmail, EventoPrenTable.colEsito};
	}// end of method

}
