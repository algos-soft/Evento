package it.algos.evento.entities.evento;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.ETable;
import it.algos.webbase.web.converter.StringToBigDecimalConverter;
import it.algos.webbase.web.module.ModulePop;

import java.math.BigDecimal;
import java.util.Locale;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class EventoTable extends ETable {

	private static final StringToBigDecimalConverter conv = new StringToBigDecimalConverter(2);
	private static final Locale locale = Locale.getDefault();
	private static final String colPrezzi = "Prezzo";

	public EventoTable(ModulePop modulo) {
		super(modulo);
	}

	/**
	 * Creates the container
	 * <p>
	 *
	 * @return un container RW filtrato sulla azienda corrente
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Container createContainer() {
		// aggiunge un filtro sulla stagione corrente
		Container cont = super.createContainer();
		JPAContainer JPAcont = (JPAContainer)cont;
		Filter filter=new Compare.Equal(Evento_.stagione.getName(), Stagione.getStagioneCorrente());
		JPAcont.addContainerFilter(filter);
		return JPAcont;
	}// end of method


	@Override
	protected void createAdditionalColumns() {
		addGeneratedColumn(colPrezzi, new PrezziColumnGenerator());
	}

	@Override
	protected Object[] getDisplayColumns() {
		return new Object[] { Evento_.sigla, Evento_.titolo, colPrezzi, Evento_.progetto, Evento_.stagione};
	}
	
	/**
	 * Initial sort order for the JPA container
	 * <p>
	 * 
	 * @param cont
	 *            the container to be sorted
	 */
	protected void sortJPAContainer(JPAContainer cont) {
		String sortField = Evento_.sigla.getName();
		cont.sort(new String[] { sortField }, new boolean[] { true });
	}// end of method


	/** Formats the value in a column containing Double objects. */
	class PrezziColumnGenerator implements ColumnGenerator {

		/**
		 * Genera la cella dei prezzi.
		 */
		public Component generateCell(Table source, Object itemId, Object columnId) {
			Property prop;
			Item item = source.getItem(itemId);
			prop = item.getItemProperty(Evento_.importoIntero.getName());
			String intero = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
			if (intero==null)intero="0";
			prop = item.getItemProperty(Evento_.importoRidotto.getName());
			String ridotto = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
			if (ridotto==null)intero="0";
			prop = item.getItemProperty(Evento_.importoDisabili.getName());
			String disabile = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
			if (disabile==null)disabile="0";
			prop = item.getItemProperty(Evento_.importoAccomp.getName());
			String accomp = conv.convertToPresentation((BigDecimal) prop.getValue(), String.class, locale);
			if (accomp==null)accomp="0";

			String string = intero + " | " + ridotto+" | "+disabile+" | "+accomp;

			return new Label(string);
		}// end of method
	}// end of internal class

}// end of class
