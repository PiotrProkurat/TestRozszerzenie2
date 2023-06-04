package pl.kurs.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.kurs.dao.IPatientDao;
import pl.kurs.exception.WrongArgumentException;
import pl.kurs.exception.WrongEntityParameterException;
import pl.kurs.models.Patient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class PatientService implements IPatientService {
    private final IPatientDao patientDao;

    public PatientService(IPatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Value("${import.file.patients}")
    private String filePath;

    @Override
    public void savePatient(Patient patient) {
        checkPatientParameters(patient);
        patientDao.save(patient);
    }

    @Override
    public Patient getPatientWithVisits(Long id) {
        checkPatientIdNumber(id);
        return patientDao.getWithVisits(id);
    }

    @Override
    public Patient getPatientWithoutVisits(Long id) {
        checkPatientIdNumber(id);
        return patientDao.getWithoutVisits(id);
    }

    @Override
    public void updatePatient(Patient patient) {
        checkPatientParameters(patient);
        patientDao.update(patient);
    }

    @Override
    public void deletePatient(Patient patient) {
        checkPatientParameters(patient);
        patientDao.delete(patient);
    }

    @Override
    public void savePatientsFromFile() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String currentLine;
            bufferedReader.readLine();
            while ((currentLine = bufferedReader.readLine()) != null) {
                Patient newPatient = getPatientFromStringLine(dateFormatter, currentLine);
                checkPatientParameters(newPatient);
                patientDao.save(newPatient);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Patient getPatientFromStringLine(DateTimeFormatter dateFormatter, String currentLine) {
        String[] splittedArray = currentLine.split("\\s+|\t");
        Patient patient = new Patient(
                Long.parseLong(splittedArray[0]),
                splittedArray[1],
                splittedArray[2],
                splittedArray[3],
                LocalDate.parse(splittedArray[4], dateFormatter));
        return patient;
    }

    private void checkPatientParameters(Patient patient) {
        Optional.ofNullable(patient)
                .filter(x -> x.getId() != null)
                .filter(x -> x.getFirstName() != null)
                .filter(x -> x.getLastName() != null)
                .filter(x -> x.getDateOfBirth() != null)
                .filter(x -> x.getPeselNumber() != null)
                .orElseThrow(() -> new WrongEntityParameterException("Wrong Patient Parameters!"));
    }

    private void checkPatientIdNumber(Long id) {
        if (id == null || id < 0) {
            throw new WrongArgumentException("Wrong Patient Id number!");
        }
    }
}
