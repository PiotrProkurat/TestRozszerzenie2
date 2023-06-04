package pl.kurs.dao;

import org.springframework.stereotype.Repository;
import pl.kurs.models.Patient;

import javax.persistence.*;

@Repository
public class PatientDao implements IPatientDao {
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public void save(Patient patient) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(patient);
        tx.commit();
        entityManager.close();
    }

    @Override
    public Patient getWithoutVisits(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        Patient patient = entityManager.find(Patient.class, id);
        entityManager.close();
        return patient;
    }

    @Override
    public Patient getWithVisits(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Patient> tq = entityManager.createQuery("SELECT p FROM Patient p LEFT JOIN FETCH p.visits WHERE p.id = :id", Patient.class);
        tq.setParameter("id", id);
        Patient patient = tq.getSingleResult();
        entityManager.close();
        return patient;
    }

    @Override
    public void update(Patient patient) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.merge(patient);
        tx.commit();
        entityManager.close();
    }

    @Override
    public void delete(Patient patient) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Patient mergedPatient = entityManager.merge(patient);
        entityManager.remove(mergedPatient);
        tx.commit();
        entityManager.close();
    }
}
