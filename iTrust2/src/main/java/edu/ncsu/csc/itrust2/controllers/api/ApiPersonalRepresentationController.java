package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.HospitalRepository;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;
import edu.ncsu.csc.itrust2.repositories.UserRepository;
import edu.ncsu.csc.itrust2.services.PersonalRepresentationService;

import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiPersonalRepresentationController {
    private final PersonalRepresentationService personalRepresentationService;

    private LoggerUtil loggerUtil;
    private UserService userService;
    String currentUserName = loggerUtil.getCurrentUsername();
    User currentUser = userService.findByName(currentUserName);

    @DeleteMapping("/delete/{patientName}")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT')")
    public void cancelPersonalRepresentation(String patientName, String representativeName) {
        try {
            if (patientName.equals(currentUserName) || representativeName.equals(currentUserName)){
                personalRepresentationService.cancelPersonalRepresentation(patientName, representativeName);
            } else {throw new Exception();}
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
