package pl.kurs.services;

import pl.kurs.models.Doctor;

import java.time.LocalDate;

public interface IDoctorService {
    void saveDoctor(Doctor doctor);
    void saveDoctorsFromFile();
    Doctor getDoctorWithVisits(Long id);
    Doctor getDoctorWithoutVisits(Long id);
    void updateDoctor(Doctor doctor);
    void deleteDoctor(Doctor doctor);
    LocalDate findAvailableDateOfVisit(Long id, LocalDate dateOfVisit);
}
