package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.services.PersonalRepresentationService;

import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiPersonalRepresentationController {
    private final PersonalRepresentationService personalRepresentationService;

    private LoggerUtil loggerUtil;
    @GetMapping("/personalRepresentatives")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<PersonalRepresentation> listPersonalRepresentativesByCurrentPatient() {
        final String patientName = loggerUtil.getCurrentUsername();
        return personalRepresentationService.listByPatient(patientName);
    }

    @GetMapping("/representingPatients")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<PersonalRepresentation> listPersonalRepresentingByCurrentPatient() {
        final String patientName = loggerUtil.getCurrentUsername();
        return personalRepresentationService.listByRepresenting(patientName);
    }

    @GetMapping("/patients/{patientUsername}/representingPatients")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<PersonalRepresentation> listPersonalRepresentingByPatientId(@PathVariable final String patientUsername) {
        return personalRepresentationService.listByRepresenting(patientUsername);
    }

    @GetMapping("/patients/{patientUsername}/personalRepresentatives")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<PersonalRepresentation> listPersonalRepresentativesByPatientId(@PathVariable final String patientUsername) {
        return personalRepresentationService.listByPatient(patientUsername);
    }

    @DeleteMapping("/representatives/{representativeUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void cancelRepresentative(@PathVariable String representativeUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.cancelPersonalRepresentation(currentUsername, representativeUsername);
    }

    @DeleteMapping("/representingPatients/{patientUsername}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void cancelRepresentingPatient(@PathVariable String patientUsername) {
        String currentUsername = loggerUtil.getCurrentUsername();
        personalRepresentationService.cancelPersonalRepresentation(patientUsername, currentUsername);
    }
}
