package it.algos.evento.entities.prenotazione;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import it.algos.evento.entities.company.Company;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.multiazienda.ELazyContainer;
import it.algos.evento.multiazienda.EventoEntity;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.module.ModulePop;

import java.util.ArrayList;

/**
 * Tabella prenotazioni usata dal modulo
 * Created by alex on 17/01/16.
 */
public class PrenotazioneTable extends PrenotazioneBaseTable {

    private final Action actRegistraPagamento = new Action(PrenotazioneTablePortal.CMD_REGISTRA_PAGAMENTO,
            PrenotazioneTablePortal.ICON_REGISTRA_PAGAMENTO);
    private final Action actIstruzioni = new Action(PrenotazioneTablePortal.CMD_RIEPILOGO_OPZIONE,
            PrenotazioneTablePortal.ICON_RIEPILOGO_OPZIONE);
    private final Action actMemoInvioSchedaPren = new Action(
            PrenotazioneTablePortal.CMD_MEMO_INVIO_SCHEDA_PREN,
            PrenotazioneTablePortal.ICON_MEMO_INVIO_SCHEDA_PREN);
    private final Action actMemoScadPag = new Action(PrenotazioneTablePortal.CMD_MEMO_SCAD_PAGA,
            PrenotazioneTablePortal.ICON_MEMO_SCAD_PAGA);
    private final Action actAttestatoPartecipazione = new Action(PrenotazioneTablePortal.CMD_ATTESTATO_PARTECIPAZIONE,
            PrenotazioneTablePortal.ICON_ATTESTATO_PARTECIPAZIONE);
    private final Action actAvvisoCongOpz = new Action(PrenotazioneTablePortal.CMD_CONGELA_OPZIONE,
            PrenotazioneTablePortal.ICON_CONGELA_OPZIONE);
    private final Action actSpostaAdAltraData = new Action(PrenotazioneTablePortal.CMD_SPOSTA_AD_ALTRA_DATA,
            PrenotazioneTablePortal.ICON_SPOSTA_AD_ALTRA_DATA);

    private ModulePop module;

    public PrenotazioneTable(ModulePop modulo) {
        super(modulo.getEntityManager());
        this.module = modulo;
        init();
    }


    @Override
    protected void init() {
        super.init();
    }


    /**
     * Return the Actions to display in contextual menu
     */
    protected Action[] getActions(Object target, Object sender) {
        Action[] actions = super.getActions(target, sender);
        ArrayList<Action> aActions=new ArrayList<>();
        for(Action a : actions){
            aActions.add(a);
        }


        aActions.add(actIstruzioni);
        aActions.add(actMemoInvioSchedaPren);
        aActions.add(actMemoScadPag);
        aActions.add(actAttestatoPartecipazione);
        aActions.add(actRegistraPagamento);
        aActions.add(actAvvisoCongOpz);
        aActions.add(actSpostaAdAltraData);

        return aActions.toArray(new Action[0]);
    }

    /**
     * Handle the contextual Actions
     */
    protected void handleAction(Action action, Object sender, Object target) {


        if (action.equals(actionDelete)) {
            getModule().delete();
        }


        Item rowItem = getTable().getItem(target);
        if (rowItem != null) {
            Object value = rowItem.getItemProperty("id").getValue();

            long id = Lib.getLong(value);
            if (id > 0) {

                if (action.equals(actionEdit)) {
                    getModule().edit();
                }

                if (action.equals(actRegistraPagamento)) {
                    registraPagamento();
                }

                if (action.equals(actIstruzioni)) {
                    inviaRiepilogoPrenotazione();
                }

                if (action.equals(actMemoInvioSchedaPren)) {
                    inviaMemoConfermaPren();
                }

                if (action.equals(actAvvisoCongOpz)) {
                    congelaPrenotazione();
                }

                if (action.equals(actSpostaAdAltraData)) {
                    spostaAdAltraData();
                }

                if (action.equals(actMemoScadPag)) {
                    inviaPromemoriaScadenzaPagamento();
                }

                if (action.equals(actAttestatoPartecipazione)) {
                    inviaAttestatoPartecipazione();
                }


            }
        }
    }


    /**
     * Creates the container
     * <p>
     *
     * @return un container RW filtrato sulla azienda corrente
     */
    @SuppressWarnings("unchecked")
    @Override
    public Container createContainer() {
        Class<EventoEntity> entityClass = (Class<EventoEntity>) getEntityClass();
        Company company = EventoSessionLib.getCompany();
        ELazyContainer entityContainer = new ELazyContainer(getEntityManager(), entityClass, getContainerPageSize(), company);
        return entityContainer;
    }// end of method



    public ModulePop getModule() {
        return module;
    }

}
