package it.algos.evento.entities.comune;

import com.vaadin.addon.jpacontainer.JPAContainer;

import it.algos.evento.multiazienda.ETable;
import it.algos.web.module.ModulePop;

public class ComuneTable extends ETable{
	
	public ComuneTable(ModulePop modulo) {
		super(modulo);
	}

	/**
	 * Initial sort order for the JPA container
	 * <p>
	 * 
	 * @param cont
	 *            the container to be sorted
	 */
	protected void sortJPAContainer(JPAContainer cont) {
		String sortField = Comune_.nome.getName();
		cont.sort(new String[] { sortField }, new boolean[] { true });
	}// end of method

}
