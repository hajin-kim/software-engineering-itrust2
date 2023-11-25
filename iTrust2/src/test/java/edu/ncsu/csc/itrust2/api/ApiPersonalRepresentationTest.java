package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import edu.ncsu.csc.itrust2.controllers.api.ApiPersonalRepresentationController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PersonalRepresentaionServiceTest {
    private MockMvc mockMvc;
    @Mock
    private PatientService patientService;
    @Mock
    private LoggerUtil loggerUtil;
    @Mock
    private PersonalRepresentationService personalRepresentationService;
    @Mock
    private BasicHealthMetricsService basicHealthMetricsService;
    @Mock
    private EmergencyPatientService emergencyPatientService;

    @Mock
    private AppointmentRequestService appointmentRequestService;

    @InjectMocks
    private ApiPersonalRepresentationController apiPersonalRepresentationController; // 테스트 대상 컨트롤러

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(apiPersonalRepresentationController).build();
    }

    @Test
    public void testListPatientLogs() throws Exception {
        String patientUsername = "patient";
        String currentUsername = "current_user";

        when(loggerUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(personalRepresentationService.isRepresentative(currentUsername, patientUsername)).thenReturn(true);
        when(loggerUtil.getAllForUser(patientUsername)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/representingPatients/{representingPatientUsername}/logs", patientUsername))
                .andExpect(status().isOk());
    }
    @Test
    public void testListPatientMedicalRecords() throws Exception {
        String patientUsername = "patient";
        String currentUsername = "current_user";

        Patient patient = new Patient();
        patient.setUsername(patientUsername);

        when(loggerUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(patientService.findByName(patientUsername)).thenReturn(patient);
        when(personalRepresentationService.isRepresentative(currentUsername, patientUsername)).thenReturn(true);
        when(basicHealthMetricsService.findByPatient(patient)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/representingPatients/{representingPatientUsername}/basic-medicalRecords", patientUsername))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPrescriptionsIn90Days() throws Exception {
        String patientUsername = "patient";
        String currentUsername = "current_user";

        when(loggerUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(personalRepresentationService.isRepresentative(currentUsername, patientUsername)).thenReturn(true);
        when(emergencyPatientService.getRecentPrescriptions(patientUsername)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/representingPatients/{representingPatientUsername}/prescription-medicalRecords", patientUsername))
                .andExpect(status().isOk());
    }
    @Test
    public void testListPatientDiagnosesIn60Days() throws Exception {
        String patientUsername = "patient";
        String currentUsername = "current_user";

        when(loggerUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(personalRepresentationService.isRepresentative(currentUsername, patientUsername)).thenReturn(true);
        when(emergencyPatientService.getRecentDiagnoses(patientUsername)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/representingPatients/{representingPatientUsername}/diagnoses", patientUsername))
                .andExpect(status().isOk());
    }

    @Test
    public void testListPatientAppointments() throws Exception {
        String patientUsername = "patient";
        String currentUsername = "current_user";

        Patient patient = new Patient();
        patient.setUsername(patientUsername);

        when(loggerUtil.getCurrentUsername()).thenReturn(currentUsername);
        when(patientService.findByName(patientUsername)).thenReturn(patient);
        when(personalRepresentationService.isRepresentative(currentUsername, patientUsername)).thenReturn(true);
        when(appointmentRequestService.findByPatient(patient)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/representingPatients/{representingPatientUsername}/appointments", patientUsername))
                .andExpect(status().isOk());
    }

}
