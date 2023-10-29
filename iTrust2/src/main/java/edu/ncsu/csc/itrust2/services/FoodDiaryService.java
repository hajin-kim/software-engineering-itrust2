package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.repositories.FoodDiaryRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FoodDiaryService {
    private final FoodDiaryRepository foodDiaryRepository;
    private final PatientService patientService;

    public List<FoodDiary> listByPatient(String patient_name) {

        final Patient patient = (Patient) patientService.findByName(patient_name);
        return foodDiaryRepository.findAllByPatient(patient);
    }
}
