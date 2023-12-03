package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.OphthalmologySurgeryForm;
import edu.ncsu.csc.itrust2.models.FoodDiary;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.OphthalmologySurgery;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.repositories.OphthalmologySurgeryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OphthalmologySurgeryService {

    private final PatientService patientService;
    private final UserService userService;
    private final OphthalmologySurgeryRepository ophthalmologySurgeryRepository;

    public List<OphthalmologySurgery> findByOPH(final User OPH) {
        return ophthalmologySurgeryRepository.findByOPH(OPH);
    }

    public List<OphthalmologySurgery> findByPatient(final User patient) {
        return ophthalmologySurgeryRepository.findByPatient(patient);
    }

    public OphthalmologySurgery create(final OphthalmologySurgeryForm officeVisitForm) {
        final OphthalmologySurgery ophthalmologySurgery = new OphthalmologySurgery();

        ophthalmologySurgery.setPatient(userService.findByName(officeVisitForm.getPatient()));
        ophthalmologySurgery.setHcp(userService.findByName(officeVisitForm.getHcp()));

        ophthalmologySurgery.setLeftVisualAcuityResult(officeVisitForm.getLeftVisualAcuityResult());
        ophthalmologySurgery.setRightVisualAcuityResult(
                officeVisitForm.getRightVisualAcuityResult());

        ophthalmologySurgery.setRightSphere(officeVisitForm.getRightSphere());
        ophthalmologySurgery.setLeftSphere(officeVisitForm.getLeftSphere());

        ophthalmologySurgery.setRightAxis(officeVisitForm.getRightAxis());
        ophthalmologySurgery.setLeftAxis(officeVisitForm.getLeftAxis());

        ophthalmologySurgery.setRightCylinder(officeVisitForm.getRightCylinder());
        ophthalmologySurgery.setLeftCylinder(officeVisitForm.getLeftCylinder());

        ophthalmologySurgery.setSurgeryType(officeVisitForm.getSurgeryType());

        return ophthalmologySurgeryRepository.save(ophthalmologySurgery);
    }
}
