package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Patient;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByFirstNameContainsOrLastNameContainsOrUsernameContains(@Length(min = 1) String firstName, @Length(min = 1) String lastName, @Length(max = 20) String username);
}
