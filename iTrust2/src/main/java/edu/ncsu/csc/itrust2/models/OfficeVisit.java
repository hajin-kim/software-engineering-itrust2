package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAdapter;
import edu.ncsu.csc.itrust2.adapters.ZonedDateTimeAttributeConverter;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.annotations.JsonAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the validated database-persisted office visit representation
 *
 * @author Kai Presler-Marshall
 */
@Schema(description = "환자가 병원 방문 시 작성되는 문서입니다.")
@NoArgsConstructor
@Getter
@Entity
@Table(name = "office_visit")
public class OfficeVisit extends DomainObject {

    /** The patient of this office visit */
    @Schema(description = "방문한 환자입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The hcp of this office visit */
    @Schema(description = "환자를 진료한 의사입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    /** The basic health metric data associated with this office visit. */
    @Schema(description = "환자의 기본적 건강상태입니다. one-to-one 관계입니다.")
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basichealthmetrics_id")
    private BasicHealthMetrics basicHealthMetrics;

    @Schema(description = "환자가 안과 수술을 받은 경우 해당 문서입니다.")
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ophthalmology_surgery_id")
    private OphthalmologySurgery ophthalmologySurgery;

    /** The date of this office visit */
    @Schema(description = "환자가 진료를 본 날짜입니다.")
    @Setter
    @NotNull @Basic
    // Allows the field to show up nicely in the database
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @JsonAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime date;

    /** The id of this office visit */
    @Schema(description = "고유 아이디입니다.")
    @Setter // TODO: remove
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The type of this office visit */
    @Schema(description = "병원 방문의 이유입니다. General Checkup, General ophthalmology, Ophthalmology Surgery가 선택가능합니다.")
    @Setter
    @NotNull @Enumerated(EnumType.STRING)
    private AppointmentType type;

    /** The hospital of this office visit */
    @Schema(description = "방문한 병원입니다.")
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "hospital_id", columnDefinition = "varchar(100)")
    private Hospital hospital;

    /**
     * The set of diagnoses associated with this visits Marked transient so not serialized or saved
     * in DB If removed, serializer gets into an infinite loop
     */
    @Schema(description = "진단된 병입니다.")
    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Diagnosis> diagnoses;

    /** The notes of this office visit */
    @Schema(description = "의사가 작성한 노트입니다.")
    @Setter private String notes;

    /** The appointment of this office visit */
    @Schema(description = "사전 예약된 방문인 경우 해당 예약의 id입니다.")
    @Setter
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentRequest appointment;

    @Schema(description = "처방전입니다.")
    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Prescription> prescriptions;

    public void validateDiagnoses() {
        if (diagnoses == null) {
            return;
        }
        for (final Diagnosis d : diagnoses) {
            if (d.getNote().length() > 500) {
                throw new IllegalArgumentException(
                        "Dagnosis note too long (500 character max) : " + d.getNote());
            }
            if (d.getCode() == null) {
                throw new IllegalArgumentException("Diagnosis Code missing!");
            }
        }
    }

    public void validateOphthalmologySurgery() {
        final OphthalmologySurgery ophthalmologySurgery = getOphthalmologySurgery();
        if (ophthalmologySurgery.getLeftVisualAcuityResult() == null
                || ophthalmologySurgery.getRightVisualAcuityResult() == null
                || ophthalmologySurgery.getLeftSphere() == null
                || ophthalmologySurgery.getRightSphere() == null
                || (ophthalmologySurgery.getRightCylinder() != null
                        && ophthalmologySurgery.getRightAxis() == null)
                || (ophthalmologySurgery.getLeftCylinder() != null
                        && ophthalmologySurgery.getLeftAxis() == null)
                || ophthalmologySurgery.getSurgeryType() == null) {
            throw new IllegalArgumentException(
                    "Not all necessary fields for ophthalmology surgery metrics were submitted");
        }
    }

    /**
     * Validates an office visit form for containing correct fields for patients 12 and over.
     * Expects the basic health metrics to already be set by the OfficeVisit constructor.
     */
    public void validate12AndOver() {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if (bhm.getDiastolic() == null
                || bhm.getHdl() == null
                || bhm.getHeight() == null
                || bhm.getHouseSmokingStatus() == null
                || bhm.getLdl() == null
                || bhm.getPatientSmokingStatus() == null
                || bhm.getSystolic() == null
                || bhm.getTri() == null
                || bhm.getWeight() == null) {
            throw new IllegalArgumentException(
                    "Not all necessary fields for basic health metrics were submitted.");
        }
    }

    /** Validates an office visit form for containing correct fields for patients 12 and under. */
    public void validateUnder12() {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if (bhm.getDiastolic() == null
                || bhm.getHeight() == null
                || bhm.getHouseSmokingStatus() == null
                || bhm.getSystolic() == null
                || bhm.getWeight() == null) {
            throw new IllegalArgumentException(
                    "Not all necessary fields for basic health metrics were submitted.");
        }
    }

    /** Validates an office visit form for containing correct fields for patients 3 and under. */
    public void validateUnder3() {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if (bhm.getHeight() == null
                || bhm.getHeadCircumference() == null
                || bhm.getHouseSmokingStatus() == null
                || bhm.getWeight() == null) {
            throw new IllegalArgumentException(
                    "Not all necessary fields for basic health metrics were submitted.");
        }
    }
}
