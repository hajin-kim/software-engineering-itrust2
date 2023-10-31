package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class PatientSearchService {

    private final PatientRepository patientRepository;

    public List<Patient> listByPatientName(String nameQuery) {
        if(nameQuery.length() > 30){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientName is too long");
        }
        return patientRepository.findAllByFirstNameContainsOrLastNameContainsOrUsernameContains(
                nameQuery, nameQuery, nameQuery);
    }
}
