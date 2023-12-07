package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.DiagnosisForm;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.repositories.OfficeVisitRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OfficeVisitMutationServiceTest {
    @Mock private OfficeVisitRepository officeVisitRepository;

    @Mock private UserService userService;

    @Mock private AppointmentRequestService appointmentRequestService;

    @Mock private HospitalService hospitalService;

    @Mock private BasicHealthMetricsService bhmService;

    @Mock private PrescriptionService prescriptionService;

    @Mock private DiagnosisService diagnosisService;

    @Mock private OphthalmologySurgeryService ophthalmologySurgeryService;
    @InjectMocks private OfficeVisitMutationService officeVisitMutationService;

    private void mockAssertNotExistsById(String idString) {
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(false);
    }

    private void mockAssertExistsById(String idString) {
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(true);
    }

    private void mockGetAppointmentRequest(
            final OfficeVisit ov, AppointmentRequest appointmentRequest) {
        given(
                        appointmentRequestService.findByHcpAndPatientAndDate(
                                eq(ov.getHcp()), eq(ov.getPatient()), eq(ov.getDate())))
                .willReturn(Optional.of(appointmentRequest));
    }

    private void mockGetBasicHealthMetrics(
            final OfficeVisitForm ovf, BasicHealthMetrics basicHealthMetrics) {
        given(bhmService.build(eq(ovf))).willReturn(basicHealthMetrics);
    }

    private void mockGetDiagnoses(
            final List<DiagnosisForm> diagnosisForms, List<Diagnosis> diagnoses) { // 이건 또 어캐 짜냐
        given(diagnosisService.build(eq(diagnosisForms.get(0)))).willReturn(diagnoses.get(0));
        given(diagnosisService.build(eq(diagnosisForms.get(1)))).willReturn(diagnoses.get(1));
    }

    private void mockGetPrescriptions(
            final List<PrescriptionForm> prescriptionForms,
            List<Prescription> prescriptions) { // 이건 또 어캐 짜냐
        given(prescriptionService.build(eq(prescriptionForms.get(0))))
                .willReturn(prescriptions.get(0));
        given(prescriptionService.build(eq(prescriptionForms.get(1))))
                .willReturn(prescriptions.get(1));
    }

    @Test
    public void testCreate() {
        // Given
        String hospitalName = "TestHospital";
        String address = "TestAddress";
        String zip = "TestZip";
        String state = "TestState";
        final Hospital hospital = new Hospital(hospitalName, address, zip, state);

        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        DiagnosisForm diagnosisForm1 = new DiagnosisForm();
        DiagnosisForm diagnosisForm2 = new DiagnosisForm();
        List<DiagnosisForm> diagnosisForms = Arrays.asList(diagnosisForm1, diagnosisForm2);
        Diagnosis diagnosis1 = new Diagnosis();
        Diagnosis diagnosis2 = new Diagnosis();
        List<Diagnosis> diagnoses = Arrays.asList(diagnosis1, diagnosis2);

        PrescriptionForm prescriptionForm1 = new PrescriptionForm();
        PrescriptionForm prescriptionForm2 = new PrescriptionForm();
        List<PrescriptionForm> prescriptionForms =
                Arrays.asList(prescriptionForm1, prescriptionForm2);
        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();
        List<Prescription> prescriptions = Arrays.asList(prescription1, prescription2);

        OfficeVisitForm ovf = new OfficeVisitForm();
        ovf.setId("124516123");
        ovf.setPatient(patientName);
        ovf.setHcp(hcpName);
        ovf.setDate(String.valueOf(date));
        ovf.setPreScheduled("true");
        ovf.setHospital(hospitalName);
        ovf.setDiagnoses(diagnosisForms);
        ovf.setPrescriptions(prescriptionForms);

        OfficeVisit ov = new OfficeVisit();
        ov.setPatient(patient);
        ov.setHcp(hcp);
        ov.setDate(date);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        BasicHealthMetrics basicHealthMetrics = new BasicHealthMetrics();

        mockAssertExistsById(ovf.getId());
        given(userService.findByName(eq(patientName))).willReturn(patient);
        given(userService.findByName(eq(hcpName))).willReturn(hcp);
        mockGetAppointmentRequest(ov, appointmentRequest);
        given(hospitalService.findByName(eq(hospitalName))).willReturn(hospital);
        mockGetBasicHealthMetrics(ovf, basicHealthMetrics);
        mockGetDiagnoses(diagnosisForms, diagnoses);
        mockGetPrescriptions(prescriptionForms, prescriptions);

        OfficeVisit createdOfficeVisit = officeVisitMutationService.create(ovf);

        assertNotNull(createdOfficeVisit);

        // Add more assertions based on your data and logic
    }

    @Test
    public void testAssertNotExistsByIdWhenIdIsNull() {
        String idString = null;

        Long result = officeVisitMutationService.assertNotExistsById(idString);

        assertNull(result);
    }

    @Test
    public void testAssertNotExistsByIdWhenIdExists() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(true);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> officeVisitMutationService.assertNotExistsById(idString));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Office visit with the id 1 already exists", exception.getReason());
    }

    @Test
    public void testAssertNotExistsByIdWhenIdDoesNotExist() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(false);

        Long result = officeVisitMutationService.assertNotExistsById(idString);

        assertEquals(id, (long) result);
    }

    @Test
    public void testAssertExistsByIdWhenIdIsNull() {
        String idString = null;

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> officeVisitMutationService.assertExistsById(idString));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    public void testAssertExistsByIdWhenIdDoesNotExist() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(false);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> officeVisitMutationService.assertExistsById(idString));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Office visit with the id 1 doesn't exist", exception.getReason());
    }

    @Test
    public void testAssertExistsByIdWhenIdExists() {
        String idString = "1";
        final var id = Long.parseLong(idString);
        given(officeVisitRepository.existsById(eq(id))).willReturn(true);

        Long result = officeVisitMutationService.assertExistsById(idString);

        assertEquals(id, (long) result);
    }

    @Test
    public void testGetAppointmentRequestWhenPreScheduledIsNull() {
        OfficeVisit officeVisit = new OfficeVisit();
        String preScheduled = null;

        AppointmentRequest result =
                officeVisitMutationService.getAppointmentRequest(officeVisit, preScheduled);

        assertNull(result);
    }

    @Test
    public void testGetAppointmentRequestWhenNoMatchFound() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        OfficeVisit officeVisit = new OfficeVisit();
        officeVisit.setPatient(patient);
        officeVisit.setHcp(hcp);
        officeVisit.setDate(date);
        String preScheduled = "true";
        given(appointmentRequestService.findByHcpAndPatientAndDate(eq(hcp), eq(patient), eq(date)))
                .willReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () ->
                                officeVisitMutationService.getAppointmentRequest(
                                        officeVisit, preScheduled));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Marked as pre-schedule but no match can be found", exception.getReason());
    }

    @Test
    public void testGetAppointmentRequestWhenMatchFound() {
        String patientName = "TestPatient";
        final Patient patient = new Patient();
        patient.setUsername(patientName);

        String hcpName = "TestHcp";
        final Personnel hcp = new Personnel();
        hcp.setUsername(hcpName);

        final var date =
                ZonedDateTime.of(
                        ZonedDateTime.now().getYear(), 11, 30, 11, 30, 0, 0, ZoneId.of("UTC"));

        OfficeVisit officeVisit = new OfficeVisit();
        officeVisit.setPatient(patient);
        officeVisit.setHcp(hcp);
        officeVisit.setDate(date);
        String preScheduled = "true";
        AppointmentRequest appointmentRequest = new AppointmentRequest();

        given(appointmentRequestService.findByHcpAndPatientAndDate(eq(hcp), eq(patient), eq(date)))
                .willReturn(Optional.of(appointmentRequest));

        AppointmentRequest result =
                officeVisitMutationService.getAppointmentRequest(officeVisit, preScheduled);

        assertNotNull(result);
        // 이렇게 같다고 확인해도 되는가?
        assertEquals(appointmentRequest, result);
    }

    @Test
    public void testGetBasicHealthMetrics() {
        OfficeVisitForm ovf = new OfficeVisitForm();
        BasicHealthMetrics basicHealthMetrics = new BasicHealthMetrics();
        given(bhmService.build(eq(ovf))).willReturn(basicHealthMetrics);

        BasicHealthMetrics result = officeVisitMutationService.getBasicHealthMetrics(ovf);

        assertNotNull(result);
        assertEquals(basicHealthMetrics, result);
    }

    @Test
    public void testGetDiagnosesWhenDiagnosisFormsIsNull() {
        List<DiagnosisForm> diagnosisForms = null;

        List<Diagnosis> result = officeVisitMutationService.getDiagnoses(diagnosisForms);

        assertNull(result);
    }

    @Test
    public void testGetDiagnosesWhenDiagnosisFormsIsNotNull() {
        DiagnosisForm diagnosisForm1 = new DiagnosisForm();
        DiagnosisForm diagnosisForm2 = new DiagnosisForm();
        List<DiagnosisForm> diagnosisForms = Arrays.asList(diagnosisForm1, diagnosisForm2);
        Diagnosis diagnosis1 = new Diagnosis();
        Diagnosis diagnosis2 = new Diagnosis();
        given(diagnosisService.build(eq(diagnosisForm1))).willReturn(diagnosis1);
        given(diagnosisService.build(eq(diagnosisForm2))).willReturn(diagnosis2);

        List<Diagnosis> result = officeVisitMutationService.getDiagnoses(diagnosisForms);

        assertNotNull(result);
        assertEquals(diagnosis1, result.get(0));
        assertEquals(diagnosis2, result.get(1));
    }

    @Test
    public void testGetPrescriptionsWhenDiagnosisFormsIsNull() {
        List<PrescriptionForm> prescriptionForms = null;

        List<Prescription> result = officeVisitMutationService.getPrescriptions(prescriptionForms);

        assertNull(result);
    }

    @Test
    public void testGetPrescriptionsWhenDiagnosisFormsIsNotNull() {
        PrescriptionForm prescriptionForm1 = new PrescriptionForm();
        PrescriptionForm prescriptionForm2 = new PrescriptionForm();
        List<PrescriptionForm> prescriptionForms =
                Arrays.asList(prescriptionForm1, prescriptionForm2);
        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();
        given(prescriptionService.build(eq(prescriptionForm1))).willReturn(prescription1);
        given(prescriptionService.build(eq(prescriptionForm2))).willReturn(prescription2);

        List<Prescription> result = officeVisitMutationService.getPrescriptions(prescriptionForms);

        assertNotNull(result);
        assertEquals(prescription1, result.get(0));
        assertEquals(prescription2, result.get(1));
    }

    @Test
    public void testGetOphthalmologySurgery() {
        OphthalmologySurgeryForm ovf = new OphthalmologySurgeryForm();
        OphthalmologySurgery ophthalmologySurgery = new OphthalmologySurgery();
        given(ophthalmologySurgeryService.create(eq(ovf))).willReturn(ophthalmologySurgery);

        OphthalmologySurgery result = officeVisitMutationService.getOphthalmologySurgery(ovf);

        assertNotNull(result);
        assertEquals(ophthalmologySurgery, result);
    }
}
