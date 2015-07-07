package it.algos.evento.entities.rappresentazione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.multiazienda.ETable;
import it.algos.web.entity.BaseEntity;
import it.algos.web.lib.Lib;
import it.algos.web.module.ModulePop;
import it.algos.evento.entities.prenotazione.Prenotazione;

import java.util.Date;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class RappresentazioneTable extends ETable {

	private static final String PROP_EVENTO_STAGIONE =Evento.class.getSimpleName().toLowerCase()+"."+ Evento_.stagione.getName();

	private static final String colPostiPren = "pren";
	private static final String colPostiDisp = "disp";


	public RappresentazioneTable(ModulePop modulo) {
		super(modulo);

		setColumnHeader(Rappresentazione_.dataRappresentazione, "data e ora");
		
		// sort by date
		Object[] properties = { Rappresentazione_.dataRappresentazione.getName() };
		boolean[] ordering = { true };
		sort(properties, ordering);

		// aggiungi un listener alla creazione di prenotazioni
		// per rinfrescare il container che mostra il n. di posti disponibili
		Prenotazione.addPostPersistListener(new BaseEntity.PostPersistListener() {

			@Override
			public void postPersist(Class<?> entityClass, long id) {
				if (entityClass.equals(Prenotazione.class)) {
					refreshRowCache();
				}
			}
		});

		// aggiungi un listener alla modifica di prenotazioni
		// per rinfrescare il container che mostra il n. di posti disponibili
		Prenotazione.addPostUpdateListener(new BaseEntity.PostUpdateListener() {

			@Override
			public void postUpdate(Class<?> entityClass, long id) {
				if (entityClass.equals(Prenotazione.class)) {
					refreshRowCache();
				}
			}
		});

		setColumnAlignment(Rappresentazione_.capienza.getName(), Align.RIGHT);
		setColumnAlignment(colPostiPren, Align.RIGHT);
		setColumnAlignment(colPostiDisp, Align.RIGHT);
		
//		setColumnUseTotals(Rappresentazione_.capienza, true);
//		setColumnUseTotals(colPostiPren, true);
//		setColumnUseTotals(colPostiDisp, true);

		
		// comandi contestuali aggiuntivi
		addActionHandler(new Action.Handler() {

			private final Action actRiepilogo = new Action(RappresentazioneTablePortal.CMD_MEMO_EXPORT,
					RappresentazioneTablePortal.ICON_MEMO_EXPORT);

			public Action[] getActions(Object target, Object sender) {
				Action[] actions = null;
				actions = new Action[1];
				actions[0] = actRiepilogo;
				return actions;
			}

			public void handleAction(Action action, Object sender, Object target) {
				Item rowItem = getTable().getItem(target);
				if (rowItem != null) {
					Object value = rowItem.getItemProperty("id").getValue();
					long id = Lib.getLong(value);
					if (id > 0) {

						if (action.equals(actRiepilogo)) {
							RappresentazioneModulo.esportaRappresentazione(id, getUI());
						}

					}
				}

			}
		});
	}// end of constructor


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
		Filter filter=new Compare.Equal(PROP_EVENTO_STAGIONE, Stagione.getStagioneCorrente());
		JPAcont.addContainerFilter(filter);
		return JPAcont;
	}// end of method

	/**
	 * Initial sort order for the JPA container
	 * <p>
	 * 
	 * @param cont
	 *            the container to be sorted
	 */
	protected void sortJPAContainer(JPAContainer cont) {
		String sortField = Rappresentazione_.dataRappresentazione.getName();
		cont.sort(new String[] { sortField }, new boolean[] { true });
	}// end of method

	
	private RappresentazioneTable getTable() {
		return this;
	}

	@Override
	protected void createAdditionalColumns() {
		addGeneratedColumn(colPostiPren, new PostiPrenColumnGenerator());
		addGeneratedColumn(colPostiDisp, new PostiDispColumnGenerator());
	}

	@Override
	protected Object[] getDisplayColumns() {
		return new Object[] { Rappresentazione_.dataRappresentazione, Rappresentazione_.evento, Rappresentazione_.sala,
				Rappresentazione_.capienza, colPostiPren, colPostiDisp };
	}
	
	
	

	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {

		if (colId.equals(Rappresentazione_.dataRappresentazione.getName())) {
			return Rappresentazione.getDateAsString((Date) property.getValue());
		}

		return super.formatPropertyValue(rowId, colId, property);
	}// end of method

	/**
	 * Genera la colonna dei posti prenotati.
	 */
	class PostiPrenColumnGenerator implements ColumnGenerator {

		public Component generateCell(Table source, Object itemId, Object columnId) {

//			JPAContainerItem<Rappresentazione> item = (JPAContainerItem<Rappresentazione>) source.getItem(itemId);
//			Rappresentazione rapp = item.getEntity();
//			int disp = RappresentazioneModulo.getPostiDisponibili(rapp);
//			Label label = new Label("" + disp);
//			label.setSizeUndefined(); // se non metto questo, non allinea a destra la label
//			return label;
			return generateCellPosti(source, itemId, true);
		}
	}

	/**
	 * Genera la colonna dei posti disponibili.
	 */
	class PostiDispColumnGenerator implements ColumnGenerator {

		public Component generateCell(Table source, Object itemId, Object columnId) {

//			JPAContainerItem<Rappresentazione> item = (JPAContainerItem<Rappresentazione>) source.getItem(itemId);
//			Rappresentazione rapp = item.getEntity();
//			int disp = RappresentazioneModulo.getPostiDisponibili(rapp);
//			Label label = new Label("" + disp);
//			label.setSizeUndefined(); // se non metto questo, non allinea a destra la label
//			return label;
			
			return generateCellPosti(source, itemId, false);

		}
	}
	
	/**
	 * Genera la cella dei posti prenotati o disponibili.
	 * <p>
	 * @param source la table
	 * @param itemId l'item id
	 * @param pren true per prenotati, false per disponibili
	 */
	@SuppressWarnings("unchecked")
	private Component generateCellPosti(Table source, Object itemId, boolean pren){
		int posti=0;
		JPAContainerItem<Rappresentazione> item = (JPAContainerItem<Rappresentazione>) source.getItem(itemId);
		Rappresentazione rapp = item.getEntity();
		if (pren) {
			posti = RappresentazioneModulo.getPostiPrenotati(rapp);
		} else {
			posti = RappresentazioneModulo.getPostiDisponibili(rapp);
		}
		Label label = new Label("" + posti);
		label.setSizeUndefined(); // se non metto questo, non allinea a destra la label
		return label;
	}

}// end of class

