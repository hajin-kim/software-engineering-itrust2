package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.PersonalRepresentation;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;
import edu.ncsu.csc.itrust2.repositories.PersonalRepresentationRepository;
import edu.ncsu.csc.itrust2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalRepresentationService {

    private final PersonalRepresentationRepository personalRepresentationRepository;
    private final UserRepository userRepository;

    public void cancelPersonalRepresentation(String patientName, String representativeName){
        Patient patient = (Patient) userRepository.findByUsername(patientName);
        User representative = userRepository.findByUsername(representativeName);

        PersonalRepresentation personalRepresentation
                = personalRepresentationRepository
                .findByPatientAndPersonalRepresentative(patient, representative);

        personalRepresentationRepository.deletePersonalRepresentation(personalRepresentation);
    }
}
