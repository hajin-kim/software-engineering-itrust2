package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.records.EmergencyPatientInfo;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EmergencyPatientServiceTest {

    @Mock private DiagnosisRepository diagnosisRepository;
    @Mock private OfficeVisitRepository officeVisitRepository;
    @Mock private PatientService patientService;
    @Mock private UserService userService;
    @Mock private LoggerUtil loggerUtil;

    @InjectMocks private EmergencyPatientService emergencyPatientService;

    @Test
    public void testGetPatientInformation() {
        String patientName = "TestPatient";
        final var patient = new Patient();
        patient.setUsername(patientName);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, Month.JANUARY, 1));
        patient.setGender(Gender.parse("Male"));
        patient.setBloodType(BloodType.parse("A+"));
        patient.setRoles(Set.of(Role.ROLE_PATIENT));

        String currentUserName = "TestUser";
        currentUser.setUsername(currentUserName);
        currentUser.setRoles(Set.of(Role.ROLE_HCP));

        given(patientService.findByName(anyString())).willReturn(patient);
        //
        //        given(loggerUtil.currentUser()).willReturn(currentUserName);
        given(userService.findByName(anyString())).willReturn(currentUser);

        EmergencyPatientInfo result = emergencyPatientService.getPatientInformation(patientName);

        assertEquals(patient.getUsername(), result.username());
        assertEquals(patient.getFirstName(), result.firstName());
        assertEquals(patient.getPreferredName(), result.preferredName());
        assertEquals(patient.getLastName(), result.lastName());
        assertEquals(patient.getDateOfBirth(), result.dateOfBirth());
        assertEquals(patient.getGender(), result.gender());
        assertEquals(patient.getBloodType(), result.bloodType());
    }

    //    @Test
    //    public void testAddFoodDiaryFailure() {
    //        given(patientService.findByName(any(String.class))).willThrow(RuntimeException.class);
    //
    //        final var foodDiaryForm = new FoodDiaryForm();
    //
    //        assertThrows(
    //                ResponseStatusException.class,
    //                () -> foodDiaryService.addFoodDiary(foodDiaryForm, "patient"));
    //    }
    //
    //    @Test
    //    public void testListByPatient() {
    //        final var patient = new Patient();
    //        List<FoodDiary> foodDiaryList = new ArrayList<FoodDiary>();
    //
    //        given(patientService.findByName(any(String.class))).willReturn(patient);
    //
    //        given(foodDiaryRepository.findAllByPatient(patient)).willReturn(foodDiaryList);
    //
    //        final var result = foodDiaryService.listByPatient("patient");
    //
    //        assertEquals(result, foodDiaryList);
    //    }
}
