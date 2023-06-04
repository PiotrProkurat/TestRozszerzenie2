package pl.kurs.app;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import pl.kurs.services.IDoctorService;
import pl.kurs.services.IPatientService;
import pl.kurs.services.IVisitService;

import java.time.LocalDate;

@ComponentScan(basePackages = "pl.kurs")
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

        IDoctorService doctorService = ctx.getBean(IDoctorService.class);
        doctorService.saveDoctorsFromFile();

        IPatientService patientService = ctx.getBean(IPatientService.class);
        patientService.savePatientsFromFile();

        IVisitService visitService = ctx.getBean(IVisitService.class);
        visitService.saveVisitsFromFile();

        //Metoda pozwalajaca znalezc najblizsz wolny termin lekarza w podanym dniu(lekarz moze miec jedna wizyte dziennie)
        System.out.println("=== Metoda pozwalajaca znalezc najblizsz wolny termin lekarza w podanym dniu ===");
        System.out.println("Available date of visit: " + doctorService.findAvailableDateOfVisit(23L, LocalDate.of(2006, 01, 07)));
        //Metoda pozwalająca pobrac pacjenta wraz z jego wszystkimi wizytami uzywajac jednego polecenia
        System.out.println("=== Metoda pozwalająca pobrac pacjenta wraz z jego wszystkimi wizytami ===");
        System.out.println("Patient with visits: " + patientService.getPatientWithVisits(100L));


    }
}


