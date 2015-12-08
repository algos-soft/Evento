package it.algos.evento.ui.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.company.Company;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.lib.EventoSessionLib;
import it.algos.evento.multiazienda.EContainer;
import it.algos.evento.multiazienda.EROContainer;
import it.algos.webbase.web.entity.EM;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by alex on 08/12/15.
 */
public class DashboardData {

    static int getNumPrenotazioni(Stagione stagione){
        int num=0;
//        Container.Filter filtroRappresentazione = new Compare.Equal(Prenotazione_.rappresentazione.getName(), rapp);
//        Container.Filter filtroNonCongelata = new Compare.Equal(Prenotazione_.congelata.getName(), false);
//        EntityManager manager = EM.createEntityManager();
//        EROContainer cont = new EROContainer(Prenotazione.class, manager);
//        cont.addContainerFilter(filtroRappresentazione);
//        cont.addContainerFilter(filtroNonCongelata);

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

        EntityManager em = EM.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
        Root<Prenotazione> root = query.from(Prenotazione.class);
        query.select(builder.sum(root.get(Prenotazione_.numTotali)));

        Company company =  EventoSessionLib.getCompany();
        Predicate isAziendaCorrente = builder.equal(root.get(Prenotazione_.company),  company);
        //query.where(builder.and(isAziendaCorrente));
        num = em.createQuery(query).getSingleResult();

        return num;
    }
}
