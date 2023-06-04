package pl.kurs.dao;

import org.springframework.stereotype.Repository;
import pl.kurs.models.Doctor;

import javax.persistence.*;

@Repository
public class DoctorDao implements IDoctorDao {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public void save(Doctor doctor) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(doctor);
        tx.commit();
        entityManager.close();
    }

    @Override
    public Doctor getWithVisits(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Doctor> tq = entityManager.createQuery("SELECT d FROM Doctor d LEFT JOIN FETCH d.visits WHERE d.id = :id", Doctor.class);
        tq.setParameter("id", id);
        Doctor doctor = tq.getSingleResult();
        entityManager.close();
        return doctor;

    }

    public Doctor getWithoutVisits(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        Doctor doctor = entityManager.find(Doctor.class, id);
        entityManager.close();
        return doctor;
    }

    @Override
    public void update(Doctor doctor) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.merge(doctor);
        tx.commit();
        entityManager.close();
    }

    @Override
    public void delete(Doctor doctor) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Doctor mergedDoctor = entityManager.merge(doctor);
        entityManager.remove(mergedDoctor);
        tx.commit();
        entityManager.close();
    }


}
