package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.repositories.UserRepository;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.util.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static edu.ncsu.csc.itrust2.controllers.api.APIController.errorResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/emergencyPatient")
public class ApiEmergencyPatientController {

    private UserRepository userRepository;
    private DiagnosisRepository diagnosisRepository;
    private OfficeVisitRepository officeVisitRepository;

    @GetMapping("/getParam")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public ResponseEntity<PatientInfo> getPatientInfo(@RequestParam String username) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isPatient()) {
                Patient patient = (Patient) user;
                PatientInfo patientInfo =
                        new PatientInfo(
                                patient.getFirstName(), patient.getPreferredName(),
                                patient.getLastName(), patient.getDateOfBirth(),
                                patient.getGender(), patient.getBloodType());
                return ResponseEntity.ok(patientInfo);
            } else {
                return new ResponseEntity(
                        errorResponse("Could not find a personnel entry for you, " + username),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(
                    errorResponse("Could not find a personnel entry for you, " + username),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/recent/{username}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public ResponseEntity<List<Diagnosis>> getDiagnosesIn60Days(@PathVariable String username) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -60);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        List<OfficeVisit> officeVisits
                = officeVisitRepository
                .findByDateBetweenAndPatientIdOrderByDateDesc(startDate, endDate, username);


        List<Diagnosis> diagnoses = new ArrayList<>();
        for (OfficeVisit officeVisit : officeVisits) {
            List<Diagnosis> diagnosisList = diagnosisRepository.findByVisit(officeVisit);
            diagnoses.addAll(diagnosisList);
        }
        return ResponseEntity.ok(diagnoses);
    }

    @GetMapping("/recent/{username}")
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_ER')")
    public ResponseEntity<List<Prescription>> getPrescriptionsIn90Days(@PathVariable String username) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -90);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        List<OfficeVisit> officeVisits
                = officeVisitRepository
                .findByDateBetweenAndPatientIdOrderByDateDesc(startDate, endDate, username);

        List<Prescription> prescriptions = new ArrayList<>();
        for (OfficeVisit officeVisit : officeVisits) {
            List<Prescription> diagnosisList = officeVisit.getPrescriptions();
            prescriptions.addAll(diagnosisList);
        }
        return ResponseEntity.ok(prescriptions);
    }
}
