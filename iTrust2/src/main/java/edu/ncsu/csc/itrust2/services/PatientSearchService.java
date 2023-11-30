package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientSearchService {

    private final PatientRepository patientRepository;

    private boolean isSubstringOf(String string, String substring) {
        string = string.toLowerCase();
        substring = substring.toLowerCase();

        int subStringIndex = 0;
        int stringIndexT = 0;

        while (subStringIndex < substring.length() && stringIndexT < string.length()) {
            if (substring.charAt(subStringIndex) == string.charAt(stringIndexT)) {
                subStringIndex++;
                stringIndexT++;
            } else {
                stringIndexT++;
            }
        }

        return subStringIndex == substring.length();
    }

    public List<Patient> listByPatientName(String nameQuery) {
        return patientRepository.findAll().stream()
                .filter(
                        patient ->
                                isSubstringOf(
                                                patient.getFirstName()
                                                        + " "
                                                        + patient.getLastName(),
                                                nameQuery)
                                        || isSubstringOf(patient.getUsername(), nameQuery))
                .toList();
    }
}
