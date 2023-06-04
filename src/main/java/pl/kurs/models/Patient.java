package pl.kurs.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String firstName;
    @Column(unique = true, nullable = false)
    private String peselNumber;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", orphanRemoval = true)
    private Set<Visit> visits = new HashSet<>();

    public Patient() {
    }

    public Patient(Long id, String lastName, String firstName, String peselNumber, LocalDate dateOfBirth) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.peselNumber = peselNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPeselNumber() {
        return peselNumber;
    }

    public void setPeselNumber(String peselNumber) {
        this.peselNumber = peselNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit) {
        visit.setPatient(this);
        visits.add(visit);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", peselNumber='" + peselNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", visits=" + visits +
                '}';
    }
}
