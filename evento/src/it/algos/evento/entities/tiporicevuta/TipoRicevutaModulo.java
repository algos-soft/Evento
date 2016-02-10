package it.algos.evento.entities.tiporicevuta;

import it.algos.evento.multiazienda.EModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 30-05-2015.
 * .
 */
public class TipoRicevutaModulo extends EModulePop {

    /**
     * Costruttore senza parametri
     */
    public TipoRicevutaModulo() {
        super(TipoRicevuta.class);
    }// end of constructor


    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[] {TipoRicevuta_.sigla, TipoRicevuta_.descrizione };
    }

}// end of class
