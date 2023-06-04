package pl.kurs.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.kurs.dao.IDoctorDao;
import pl.kurs.exception.NotFoundException;
import pl.kurs.exception.WrongArgumentException;
import pl.kurs.exception.WrongEntityParameterException;
import pl.kurs.models.Doctor;
import pl.kurs.models.Visit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class DoctorService implements IDoctorService {

    private final IDoctorDao doctorDao;

    public DoctorService(IDoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Value("${import.file.doctors}")
    private String filePath;

    @Override
    public void saveDoctor(Doctor doctor) {
        checkDoctorParameters(doctor);
        doctorDao.save(doctor);
    }

    @Override
    public Doctor getDoctorWithVisits(Long id) {
        checkDoctorIdNumber(id);
        return doctorDao.getWithVisits(id);
    }

    @Override
    public Doctor getDoctorWithoutVisits(Long id) {
        checkDoctorIdNumber(id);
        return doctorDao.getWithoutVisits(id);
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        checkDoctorParameters(doctor);
        doctorDao.update(doctor);
    }

    @Override
    public void deleteDoctor(Doctor doctor) {
        checkDoctorParameters(doctor);
        doctorDao.delete(doctor);
    }

    @Override
    public void saveDoctorsFromFile() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String currentLine;
            bufferedReader.readLine();
            while ((currentLine = bufferedReader.readLine()) != null) {
                Doctor newDoctor = getDoctorFromStringLine(dateFormatter, currentLine);
                checkDoctorParameters(newDoctor);
                doctorDao.save(newDoctor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Doctor getDoctorFromStringLine(DateTimeFormatter dateFormatter, String currentLine) {
        String[] splittedArray = currentLine.split("\\s+|\t");
        Doctor doctor = new Doctor(
                Long.parseLong(splittedArray[0]),
                splittedArray[1],
                splittedArray[2],
                splittedArray[3],
                LocalDate.parse(splittedArray[4], dateFormatter),
                splittedArray[5],
                splittedArray[6]);
        return doctor;
    }

    @Override
    public LocalDate findAvailableDateOfVisit(Long id, LocalDate dateOfVisit) {
        LocalDate availableDateOfVisit = dateOfVisit;
        LocalDate maxDate = dateOfVisit.plusMonths(3);
        List<Visit> doctorVisits = getVisitsAfterDate(id, dateOfVisit);

        while (availableDateOfVisit.isBefore(maxDate)) {
            final LocalDate checkingDate = availableDateOfVisit;
            boolean dateIsNotAvailable = doctorVisits.stream()
                    .anyMatch(x -> x.getDateOfVisit().isEqual(checkingDate));
            if (!dateIsNotAvailable && availableDateOfVisit.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    availableDateOfVisit.getDayOfWeek() != DayOfWeek.SUNDAY) {
                return availableDateOfVisit;
            }
            availableDateOfVisit = availableDateOfVisit.plusDays(1);
        }
        throw new NotFoundException("Not found available date");
    }

    private List<Visit> getVisitsAfterDate(Long id, LocalDate date) {
        List<Visit> doctorVisits = getDoctorWithVisits(id).getVisits()
                .stream()
                .filter(x -> (x.getDateOfVisit().isAfter(date) || x.getDateOfVisit().isEqual(date)))
                .sorted(Comparator.comparing(Visit::getDateOfVisit))
                .collect(Collectors.toList());
        return doctorVisits;
    }

    private void checkDoctorParameters(Doctor doctor) {
        Optional.ofNullable(doctor)
                .filter(x -> x.getId() != null)
                .filter(x -> x.getFirstName() != null)
                .filter(x -> x.getLastName() != null)
                .filter(x -> x.getDateOfBirth() != null)
                .filter(x -> x.getNipNumber() != null)
                .filter(x -> x.getPhoneNumber() != null)
                .filter(x -> x.getSpecialty() != null)
                .orElseThrow(() -> new WrongEntityParameterException("Wrong Doctor Parameters!"));
    }

    private void checkDoctorIdNumber(Long id) {
        if (id == null || id < 0) {
            throw new WrongArgumentException("Wrong Doctor Id number!");
        }
    }
}


