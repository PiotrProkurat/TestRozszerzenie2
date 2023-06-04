package pl.kurs.services;

import pl.kurs.models.Patient;

public interface IPatientService {
    void savePatient(Patient patient);

    void savePatientsFromFile();

    Patient getPatientWithVisits(Long id);

    Patient getPatientWithoutVisits(Long id);

    void updatePatient(Patient patient);

    void deletePatient(Patient patient);
}
