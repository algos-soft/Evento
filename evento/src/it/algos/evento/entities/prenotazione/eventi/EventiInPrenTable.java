package it.algos.evento.entities.prenotazione.eventi;


@SuppressWarnings("serial")
public class EventiInPrenTable extends EventoPrenTable {

	
	public EventiInPrenTable() {
		super(EventoPren.class);
	}

	protected Object[] getDisplayColumns() {
		return new Object[] { EventoPren_.timestamp, EventoPren_.tipo, EventoPren_.dettagli, EventoPren_.user , EventoPrenTable.colEmail, EventoPrenTable.colEsito};
	}// end of method

}
