package edu.ncsu.csc.itrust2.repositories;
import java.util.List;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentatives;
import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface PersonalRepresentationRepository extends JpaRepository<PersonalRepresentatives, Long> {
    List<PersonalRepresentatives> findAllByPatient(User patient);
    List<PersonalRepresentatives> findALLByPr(User patient);
}
