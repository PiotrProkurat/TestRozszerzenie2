package pl.kurs.services;

import pl.kurs.models.Visit;

public interface IVisitService {
    void saveVisit(Visit visit);

    void saveVisitsFromFile();

    Visit getVisit(Long id);

    void updateVisit(Visit visit);

    void deleteVisit(Visit visit);
}
