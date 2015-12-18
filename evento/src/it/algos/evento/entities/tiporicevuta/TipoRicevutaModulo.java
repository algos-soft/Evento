package it.algos.evento.entities.tiporicevuta;

import it.algos.evento.multiazienda.EModulePop;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Created by alex on 30-05-2015.
 */
public class TipoRicevutaModulo extends EModulePop {

    public TipoRicevutaModulo() {
        super(TipoRicevuta.class);
    }

    /**
     * Crea una sola istanza di un modulo per sessione.
     * Tutte le finestre e i tab di un browser sono nella stessa sessione.
     */
    public static TipoRicevutaModulo getInstance(){
        return (TipoRicevutaModulo) ModulePop.getInstance(TipoRicevutaModulo.class);
    }

    protected Attribute<?, ?>[] creaFieldsAll() {
        return new Attribute[] {TipoRicevuta_.sigla, TipoRicevuta_.descrizione };
    }

}
