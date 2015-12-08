package it.algos.evento.ui.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.multiazienda.EContainer;
import it.algos.evento.multiazienda.EROContainer;
import it.algos.evento.multiazienda.EventoEntity_;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 08/12/15.
 */
public class DashboardData {

    static int getNumPrenotazioni(Stagione stagione){
        //int num=0;

//        EntityManager em = EM.createEntityManager();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Number> q = cb.createQuery(Number.class);
//        Root<Prenotazione> t = q.from(Prenotazione.class);
//
//// build SUM(A * (100.0 - B) / 100.0) expression
//        Expression<Double> diff = cb.diff(100.0, t.<Double>get("B"));
//        Expression<Double> prod = cb.prod(t.<Double>get("A"), diff);
//        Expression<Number> quot = cb.quot(prod, 100.0);
//        Expression<Number> sum = cb.sum(quot);
//        q.select(sum.alias("D"));
//
//        System.out.println(em.createQuery(q).getSingleResult());

//        EntityManager em = EM.createEntityManager();
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
//        Root<Prenotazione> root = query.from(Prenotazione.class);
//        query.select(builder.sum(root.get(Prenotazione_.numTotali)));
//
//        Company company =  EventoSessionLib.getCompany();
//        Predicate isAziendaCorrente = builder.equal(root.get(Prenotazione_.company),  company);
//        //query.where(builder.and(isAziendaCorrente));
//        num = em.createQuery(query).getSingleResult();

//        EntityManager em = EM.createEntityManager();
//        CriteriaBuilder qb = em.getCriteriaBuilder();
//        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
//        cq.select(qb.count(cq.from(Prenotazione.class)));
//
//        TypedQuery<Long> query=em.createQuery(cq);
//
//        Root<Prenotazione> pren = cq.from(Prenotazione.class);
//
//        Company company =  EventoSessionLib.getCompany();
//
//        cq.where(pren.get(Prenotazione_.company).in(company));
        //cq.where(/*your stuff*/);
        //Company company =  EventoSessionLib.getCompany();
        //Predicate isAziendaCorrente = qb.equal(root.get(Prenotazione_.company),  company);
        //query.where(builder.and(isAziendaCorrente));


        //num= query.getSingleResult().intValue();

        Company company =  EventoSessionLib.getCompany();
        Long aLong = AQuery.getCount(Prenotazione.class, Prenotazione_.company, company);

        return aLong.intValue();
    }


    /**
     * Ritorna il numero di prenotazioni per l'azienda corrente in una stagione.
     * @param stagione la stagione
     * @return  il numero totale di prenotazioni
     */
    public static int countPrenotazioni(Stagione stagione){
        int num=0;

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //CriteriaQuery<Prenotazione> cq = cb.createQuery(Prenotazione.class);
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);


        Root<Prenotazione> rootPren = cq.from(Prenotazione.class);
        Join<Prenotazione, Rappresentazione> jRapp = rootPren.join(Prenotazione_.rappresentazione);
        Join<Rappresentazione, Evento> jEve = jRapp.join(Rappresentazione_.evento);

        addFiltroCompany(rootPren, cb, cq);
        cq.where(cb.and(cb.equal(jEve.get(Evento_.stagione), stagione)));

        cq.select(cb.count(rootPren));

        //em.createQuery(cq).getSingleResult();

        TypedQuery<Long> q = em.createQuery(cq);
        num=q.getSingleResult().intValue();
        //List<Prenotazione> results = q.getResultList();
        //num=results.size();
        em.close();

        return num;
    }

    /**
     * Aggiunge un filtro sulla company corrente a una query.
     */
    private static void addFiltroCompany(Root root, CriteriaBuilder cb, CriteriaQuery cq){
        Company company =  EventoSessionLib.getCompany();
        cq.where(cb.and(cb.equal(root.get(EventoEntity_.company), company)));
    }

}
