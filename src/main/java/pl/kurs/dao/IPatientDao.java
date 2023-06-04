package pl.kurs.dao;

import pl.kurs.models.Patient;

public interface IPatientDao {
    void save(Patient patient);

    Patient getWithoutVisits(Long id);

    Patient getWithVisits(Long id);

    void update(Patient patient);

    void delete(Patient patient);
}
