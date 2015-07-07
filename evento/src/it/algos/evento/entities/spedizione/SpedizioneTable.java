package it.algos.evento.entities.spedizione;

import it.algos.evento.multiazienda.ETable;
import it.algos.web.lib.LibDate;

import java.util.Date;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class SpedizioneTable extends ETable {

	public SpedizioneTable() {
		super(Spedizione.class);

		setColumnHeader(Spedizione_.dataSpedizione, "Data e ora");
		setColumnHeader(Spedizione_.destinatario, "Destinatario");
		setColumnHeader(Spedizione_.lettera, "Tipo lettera");
		setColumnHeader(Spedizione_.operatore, "Operatore");
		setColumnHeader(Spedizione_.spedita, "Sped");
		setColumnHeader(Spedizione_.errore, "Errore");

	}

	/**
	 * Initial sort order for the JPA container
	 * <p>
	 * 
	 * @param cont
	 *            the container to be sorted
	 */
	protected void sortJPAContainer(JPAContainer cont) {
		String sortField = Spedizione_.dataSpedizione.getName();
		cont.sort(new String[] { sortField }, new boolean[] { true });
	}// end of method

	
	protected Object[] getDisplayColumns() {
		return new Object[] { 
				Spedizione_.dataSpedizione, 
				Spedizione_.destinatario, 
				Spedizione_.lettera, 
				Spedizione_.operatore,
				Spedizione_.spedita, 
				Spedizione_.errore
				};
	}// end of method
	
	
	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {

		if (colId.equals(Spedizione_.dataSpedizione.getName())) {
			return LibDate.toStringDDMMYYYYHHMM((Date) property.getValue());
		}

		return super.formatPropertyValue(rowId, colId, property);

	}// end of method


}
