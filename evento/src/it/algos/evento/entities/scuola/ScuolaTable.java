package it.algos.evento.entities.scuola;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.multiazienda.ETable;
import it.algos.webbase.web.module.ModulePop;

@SuppressWarnings("serial")
public class ScuolaTable extends ETable {

	//private OrdineScuolaConverterOld ordineConverter = new OrdineScuolaConverterOld();

	public ScuolaTable(ModulePop modulo) {
		super(modulo);
		setColumnAlignment(Scuola_.ordine, Align.LEFT);

	}// end of constructor

	/**
	 * Initial sort order for the JPA container
	 * <p>
	 * 
	 * @param cont
	 *            the container to be sorted
	 */
	protected void sortJPAContainer(JPAContainer cont) {
		String sortField = Scuola_.sigla.getName();
		cont.sort(new String[] { sortField }, new boolean[] { true });
	}// end of method

	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		String string = null;

		if (colId.equals(Scuola_.ordine.getName())) {
			Object value = property.getValue();
			if (value!=null && value instanceof OrdineScuola) {
				OrdineScuola ordine = (OrdineScuola)value;
				string=ordine.getSigla();
			} else {
				string="";
			}
			return string;
		}

		return super.formatPropertyValue(rowId, colId, property);
	}// end of method



}// end of class
