package it.algos.evento;

import it.algos.evento.multiazienda.EventoEntity;
import it.algos.evento.multiazienda.EventoEntity_;
import it.algos.webbase.web.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 23-12-2015.
 */
public class CQuery {

    private EntityManager em;
    private Class<? extends BaseEntity> clazz;
    private CriteriaBuilder cb;
    Root<BaseEntity> root;
    private ArrayList<Predicate> predicates;

    public CQuery(EntityManager em, Class<? extends BaseEntity> clazz) {
        this.em = em;
        this.clazz = clazz;

        cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(clazz);
        root = (Root<BaseEntity>) cq.from(clazz);

    }

    public void addFilter(SingularAttribute attr, Object value){
        Predicate pred = cb.equal(root.get(attr), value);
        predicates.add(pred);
    }

    private void resolve(){

    }

    public List<BaseEntity> getRelsutList(){
        resolve();
        return null;
    }

}




