package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.services.PatientSearchService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/patientSearch")
public class ApiPatientSearchController {
    private final PatientSearchService patientService;

    @GetMapping("/")
    public List<Patient> listPatientsByName(String patientName){
        if(patientName == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientName is required");
        }

        return patientService.listByPatientName(patientName);
    }
}
