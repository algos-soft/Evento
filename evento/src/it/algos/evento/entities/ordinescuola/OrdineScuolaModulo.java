package it.algos.evento.entities.ordinescuola;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 30-05-2015.
 */
public class OrdineScuolaModulo extends EModulePop {


    public OrdineScuolaModulo() {
        super(OrdineScuola.class);
    }

    /**
     * Crea una sola istanza di un modulo per sessione.
     * Tutte le finestre e i tab di un browser sono nella stessa sessione.
     */
    public static OrdineScuolaModulo getInstance(){
        return (OrdineScuolaModulo) ModulePop.getInstance(OrdineScuolaModulo.class);
    }

    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[] {OrdineScuola_.sigla, OrdineScuola_.descrizione };
    }


}
