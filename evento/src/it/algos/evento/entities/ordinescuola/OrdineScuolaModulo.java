package it.algos.evento.entities.ordinescuola;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 30-05-2015.
 *
 */
public class OrdineScuolaModulo extends EModulePop {


    /**
     * Costruttore senza parametri
     */
    public OrdineScuolaModulo() {
        super(OrdineScuola.class);
    }// end of constructor


    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[] {OrdineScuola_.sigla, OrdineScuola_.descrizione };
    }


}// end of class
