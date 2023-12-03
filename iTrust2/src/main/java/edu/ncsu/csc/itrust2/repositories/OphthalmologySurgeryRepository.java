package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface OphthalmologySurgeryRepository extends JpaRepository<OphthalmologySurgery, Long> {
    List<OphthalmologySurgery> findByPatient(@NotNull User patient);
    List<OphthalmologySurgery> findByOPH(@NotNull User patient);
}
