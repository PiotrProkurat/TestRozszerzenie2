package pl.kurs.dao;

import org.springframework.stereotype.Repository;
import pl.kurs.models.Visit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

@Repository
public class VisitDao implements IVisitDao {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public void save(Visit visit) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(visit);
        tx.commit();
        entityManager.close();
    }

    @Override
    public Visit get(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        Visit visit = entityManager.find(Visit.class, id);
        entityManager.close();
        return visit;
    }

    @Override
    public void update(Visit visit) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.merge(visit);
        tx.commit();
        entityManager.close();
    }

    @Override
    public void delete(Visit visit) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Visit mergedVisit = entityManager.merge(visit);
        entityManager.remove(mergedVisit);
        tx.commit();
        entityManager.close();
    }
}
