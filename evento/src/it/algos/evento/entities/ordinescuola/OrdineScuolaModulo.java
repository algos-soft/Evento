package it.algos.evento.entities.ordinescuola;

import it.algos.evento.multiazienda.EModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 30-05-2015.
 */
public class OrdineScuolaModulo extends EModulePop {


    public OrdineScuolaModulo() {
        super(OrdineScuola.class);
    }

    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[] {OrdineScuola_.sigla, OrdineScuola_.descrizione };
    }


}
