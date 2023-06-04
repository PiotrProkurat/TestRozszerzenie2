package pl.kurs.dao;

import pl.kurs.models.Doctor;

public interface IDoctorDao {
    void save(Doctor doctor);

    Doctor getWithoutVisits(Long id);

    Doctor getWithVisits(Long id);

    void update(Doctor doctor);

    void delete(Doctor doctor);

}
