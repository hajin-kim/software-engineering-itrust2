package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;

import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepresentationRepository
        extends JpaRepository<PersonalRepresentation, Long> {

    PersonalRepresentation findByPatientAndPersonalRepresentative(Patient patient, Patient representative);
}
