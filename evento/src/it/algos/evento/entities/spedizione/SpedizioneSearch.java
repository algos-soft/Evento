package it.algos.evento.entities.spedizione;

import it.algos.evento.multiazienda.ERelatedComboField;
import it.algos.evento.multiazienda.ESearchManager;
import it.algos.web.component.DateRangeComponent;
import it.algos.web.field.RelatedComboField;
import it.algos.web.field.TextField;
import it.algos.web.component.YesNoCheckboxComponent;
import it.algos.evento.entities.lettera.Lettera;

import java.util.ArrayList;

import com.vaadin.data.Container.Filter;
import com.vaadin.ui.FormLayout;

@SuppressWarnings("serial")
public class SpedizioneSearch extends ESearchManager {


	private DateRangeComponent fRangeDataSped;
	private TextField fDestinatario;
	private RelatedComboField fTipoLettera;
	private TextField fOperatore;
	private YesNoCheckboxComponent fSpedito;

	public SpedizioneSearch() {
		super();
		setCaption("Ricerca spedizioni");
	}// end of method

	// come default spazzola tutti i campi della Entity
	// aggiunge i campi al layout
	// può essere sovrascritto nelle sottoclassi specifiche
	protected void createFields(FormLayout layout) {

		fRangeDataSped = new DateRangeComponent("Data spedizione");
		layout.addComponent(fRangeDataSped);
		
		fDestinatario = new TextField("Destinatario");
		layout.addComponent(fDestinatario);
		
		fTipoLettera = new ERelatedComboField(Lettera.class, "Tipo lettera");
		layout.addComponent(fTipoLettera);

		fOperatore = new TextField("Operatore");
		layout.addComponent(fOperatore);

		fSpedito = new YesNoCheckboxComponent("Spedito");
		layout.addComponent(fSpedito);
	}// end of method

	
	@Override
	public ArrayList<Filter> createFilters() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		filters.add(fRangeDataSped.getFilter(Spedizione_.dataSpedizione.getName()));
		filters.add(createStringFilter(fDestinatario, Spedizione_.destinatario, SearchType.CONTAINS));
		filters.add(createBeanFilter(fTipoLettera, Spedizione_.lettera));
		filters.add(createStringFilter(fOperatore, Spedizione_.operatore, SearchType.STARTS_WITH));
		filters.add(fSpedito.getFilter(Spedizione_.spedita.getName()));
		return filters;
	}// end of method

}
