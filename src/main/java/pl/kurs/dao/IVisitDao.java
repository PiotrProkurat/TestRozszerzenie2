package pl.kurs.dao;

import pl.kurs.models.Visit;

public interface IVisitDao {
    void save(Visit visit);

    Visit get(Long id);

    void update(Visit visit);

    void delete(Visit visit);
}
