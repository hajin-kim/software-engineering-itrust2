package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.PatientRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PatientSearchServiceTest {

    @Mock private PatientRepository patientRepository;

    @InjectMocks private PatientSearchService patientSearchService;

    @Test
    public void testListByPatientName() {
        // Mock patient
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, Month.JANUARY, 1));
        patient.setGender(Gender.parse("Male"));
        patient.setBloodType(BloodType.parse("A+"));
        patient.setRoles(Set.of(Role.ROLE_PATIENT));

        // Mock User
        String currentUserName = "TestUser";
        final Personnel currentUser = new Personnel();
        currentUser.setUsername(currentUserName);
        currentUser.setRoles(Set.of(Role.ROLE_HCP));

        // Mock patient list
        List<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(patient);

        // Mock interactions
        given(
                        patientRepository
                                .findAllByFirstNameContainsOrLastNameContainsOrUsernameContains(
                                        eq(patientName), eq(patientName), eq(patientName)))
                .willReturn(expectedPatients);

        // Test the method
        List<Patient> result = patientSearchService.listByPatientName(patientName);

        // Verify the result
        assertEquals(expectedPatients, result);
    }
}
