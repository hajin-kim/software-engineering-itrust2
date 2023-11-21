package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.records.EmergencyPatientInfo;
import edu.ncsu.csc.itrust2.repositories.DiagnosisRepository;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EmergencyPatientServiceTest {

    @Mock private DiagnosisRepository diagnosisRepository;
    @Mock private OfficeVisitRepository officeVisitRepository;
    @Mock private PatientService patientService;

    @Mock private LoggerUtil loggerUtil;

    @InjectMocks private EmergencyPatientService emergencyPatientService;

    @Test
    public void testGetPatientInformation() {
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

        // Mock interactions
        given(patientService.findByName(eq(patientName))).willReturn(patient);
        given(patientService.findByName(eq(currentUserName))).willReturn(currentUser);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUserName);

        // Test the method
        EmergencyPatientInfo result = emergencyPatientService.getPatientInformation(patientName);

        // Verify the result
        assertEquals(patient.getUsername(), result.username());
        assertEquals(patient.getFirstName(), result.firstName());
        assertEquals(patient.getPreferredName(), result.preferredName());
        assertEquals(patient.getLastName(), result.lastName());
        assertEquals(patient.getDateOfBirth(), result.dateOfBirth());
        assertEquals(patient.getGender(), result.gender());
        assertEquals(patient.getBloodType(), result.bloodType());
    }

    @Test
    public void testGetRecentOfficeVisits() {
        // Mock patient
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        // Mock office visits
        List<OfficeVisit> expectedOfficeVisits = new ArrayList<>();
        OfficeVisit officeVisit1 = new OfficeVisit();
        officeVisit1.setPatient(patient);
        officeVisit1.setDate(ZonedDateTime.now());
        officeVisit1.setType(AppointmentType.GENERAL_CHECKUP);
        officeVisit1.setNotes("Regular checkup");
        expectedOfficeVisits.add(officeVisit1);

        int dateAmount = 7;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -dateAmount);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        Instant startDateInstant = startDate.toInstant();
        Instant endDateInstant = endDate.toInstant();
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        ZonedDateTime zoneStartDate = ZonedDateTime.ofInstant(startDateInstant, zoneId);
        ZonedDateTime zoneEndDate = ZonedDateTime.ofInstant(endDateInstant, zoneId);

        // Mock interactions
        given(patientService.findByName(eq(patientName))).willReturn(patient);
        given(
                        officeVisitRepository.findByDateBetweenAndPatientOrderByDateDesc(
                                any(), any(), eq(patient)))
                .willReturn(expectedOfficeVisits);

        // Test the method
        List<OfficeVisit> result =
                emergencyPatientService.getRecentOfficeVisits(patientName, dateAmount);

        // Verify the result
        assertEquals(expectedOfficeVisits, result);
    }
}
