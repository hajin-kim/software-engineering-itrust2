package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientSearchService {

    private final PatientRepository patientRepository;

    public List<Patient> listByPatientName(String patientName){
        return patientRepository.findAllByFirstNameContainsOrLastNameContainsOrUsernameContains(patientName, patientName, patientName);
    }

}
