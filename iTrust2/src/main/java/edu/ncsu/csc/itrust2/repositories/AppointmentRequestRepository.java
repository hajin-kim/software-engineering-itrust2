package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.User;

import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByPatient(@NotNull User patient);

    List<AppointmentRequest> findByHcp(@NotNull User hcp);

    List<AppointmentRequest> findByHcpAndPatient(@NotNull User hcp, @NotNull User patient);

    List<AppointmentRequest> findByPatientAndDataAfter(
            @NotNull User patient, @NotNull ZonedDateTime now);

    List<AppointmentRequest> findByHcpAndDataAfter(@NotNull User hcp, @NotNull ZonedDateTime now);

    List<AppointmentRequest> findByHcpAndPatientAndDataAfter(
            @NotNull User hcp, @NotNull User patient, @NotNull ZonedDateTime now);
}
