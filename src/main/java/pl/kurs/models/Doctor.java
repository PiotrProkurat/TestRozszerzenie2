package pl.kurs.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "doctors")
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String specialty;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(unique = true, nullable = false)
    private String nipNumber;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor", orphanRemoval = true)
    private Set<Visit> visits = new HashSet<>();

    public Doctor() {
    }

    public Doctor(Long id, String lastName, String firstName, String specialty, LocalDate dateOfBirth, String nipNumber, String phoneNumber) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.specialty = specialty;
        this.dateOfBirth = dateOfBirth;
        this.nipNumber = nipNumber;
        this.phoneNumber = phoneNumber;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNipNumber() {
        return nipNumber;
    }

    public void setNipNumber(String nipNumber) {
        this.nipNumber = nipNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit){
        visit.setDoctor(this);
        visits.add(visit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id) && Objects.equals(lastName, doctor.lastName) && Objects.equals(firstName, doctor.firstName) && Objects.equals(specialty, doctor.specialty) && Objects.equals(dateOfBirth, doctor.dateOfBirth) && Objects.equals(nipNumber, doctor.nipNumber) && Objects.equals(phoneNumber, doctor.phoneNumber) && Objects.equals(visits, doctor.visits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, specialty, dateOfBirth, nipNumber, phoneNumber, visits);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nipNumber='" + nipNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", visits=" + visits +
                '}';
    }
}
