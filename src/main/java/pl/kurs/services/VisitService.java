package pl.kurs.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pl.kurs.dao.IVisitDao;
import pl.kurs.exception.WrongArgumentException;
import pl.kurs.exception.WrongEntityParameterException;
import pl.kurs.models.Doctor;
import pl.kurs.models.Patient;
import pl.kurs.models.Visit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class VisitService implements IVisitService {

    private final IVisitDao visitDao;

    private final IDoctorService doctorService;

    private final IPatientService patientService;

    public VisitService(IVisitDao visitDao, IDoctorService doctorService, IPatientService patientService) {
        this.visitDao = visitDao;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Value("${import.file.visits}")
    private String filePath;

    @Override
    public void saveVisit(Visit visit) {
        checkVisitParametersToSave(visit);
        visitDao.save(visit);
    }

    @Override
    public Visit getVisit(Long id) {
        checkVisitIdNumber(id);
        return visitDao.get(id);
    }

    @Override
    public void updateVisit(Visit visit) {
        checkVisitParametersToUpdateAndDelete(visit);
        visitDao.update(visit);
    }

    @Override
    public void deleteVisit(Visit visit) {
        checkVisitParametersToUpdateAndDelete(visit);
        visitDao.delete(visit);
    }

    public void saveVisitsFromFile() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String currentLine;
            bufferedReader.readLine();
            while ((currentLine = bufferedReader.readLine()) != null) {
                Visit visit = getVisit(dateFormatter, currentLine);
                checkVisitParametersToSave(visit);
                visitDao.save(visit);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Visit getVisit(DateTimeFormatter dateFormatter, String currentLine) {
        String[] splittedArray = currentLine.split("\\s+|\t");
        Doctor loadedDoctor = doctorService.getDoctorWithVisits(Long.parseLong(splittedArray[0]));
        Patient loadedPatient = patientService.getPatientWithVisits(Long.parseLong(splittedArray[1]));
        Visit visit = new Visit(LocalDate.parse(splittedArray[2], dateFormatter));
        loadedDoctor.addVisit(visit);
        loadedPatient.addVisit(visit);
        return visit;
    }

    private void checkVisitParametersToSave(Visit visit) {
        Optional.ofNullable(visit)
                .filter(x -> x.getId() == null)
                .filter(x -> x.getDateOfVisit() != null)
                .filter(x -> x.getDoctor() != null)
                .filter(x -> x.getPatient() != null)
                .orElseThrow(() -> new WrongEntityParameterException("Wrong Visit Parameters!"));
    }

    private void checkVisitParametersToUpdateAndDelete(Visit visit) {
        Optional.ofNullable(visit)
                .filter(x -> x.getId() != null)
                .filter(x -> x.getDateOfVisit() != null)
                .filter(x -> x.getDoctor() != null)
                .filter(x -> x.getPatient() != null)
                .orElseThrow(() -> new WrongEntityParameterException("Wrong Visit Parameters!"));
    }

    private void checkVisitIdNumber(Long id) {
        if (id == null || id < 0) {
            throw new WrongArgumentException("Wrong Visit Id number!");
        }
    }
}
