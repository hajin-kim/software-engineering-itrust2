package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.ICDCodeForm;
import edu.ncsu.csc.itrust2.models.ICDCode;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.ICDCodeService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class that provides the REST endpoints for handling ICD Codes. They can be retrieved individually
 * based on id, or all in a list. An Admin can add, remove, or edit them.
 *
 * @author Thomas
 * @author Kai Presler-Marshall
 */
@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class APIICDCodeController extends APIController {

    private final LoggerUtil loggerUtil;

    private final ICDCodeService service;

    /**
     * Returns a list of Codes in the system
     *
     * @return All the codes in the system
     */
    @GetMapping("/icdcodes")
    public List<ICDCode> getCodes() {
        loggerUtil.log(
                TransactionType.ICD_VIEW_ALL, loggerUtil.getCurrentUsername(), "Fetched icd codes");
        return (List<ICDCode>) service.findAll();
    }

    /**
     * Returns the code with the given ID
     *
     * @param id The ID of the code to retrieve
     * @return The requested Code
     */
    @GetMapping("/icdcode/{id}")
    public ResponseEntity getCode(@PathVariable("id") final Long id) {
        try {
            final ICDCode code = (ICDCode) service.findById(id);
            if (code == null) {
                return new ResponseEntity(
                        errorResponse("No code with id " + id), HttpStatus.NOT_FOUND);
            }
            loggerUtil.log(
                    TransactionType.ICD_VIEW,
                    loggerUtil.getCurrentUsername(),
                    "Fetched icd code with id " + id);
            return new ResponseEntity(code, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not retrieve ICD Code " + id + " because of " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates the code with the specified ID to the value supplied.
     *
     * @param id The ID of the code to edit
     * @param form The new values for the Code
     * @return The Response of the action
     */
    @PutMapping("/icdcode/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateCode(
            @PathVariable("id") final Long id, @RequestBody final ICDCodeForm form) {
        try {
            if (!service.existsById(id)) {
                return new ResponseEntity("No code with id " + id, HttpStatus.NOT_FOUND);
            }
            form.setId(id);
            final ICDCode code = new ICDCode(form);
            service.save(code);
            loggerUtil.log(
                    TransactionType.ICD_EDIT,
                    loggerUtil.getCurrentUsername(),
                    loggerUtil.getCurrentUsername() + " edited an ICD Code");

            return new ResponseEntity(code, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not update ICD Code " + id + " because of " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Adds a new code to the system
     *
     * @param form The data for the new Code
     * @return The result of the action
     */
    @PostMapping("/icdcodes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity addCode(@RequestBody final ICDCodeForm form) {
        try {
            final ICDCode code = new ICDCode(form);
            service.save(code);
            loggerUtil.log(
                    TransactionType.ICD_CREATE,
                    loggerUtil.getCurrentUsername(),
                    loggerUtil.getCurrentUsername() + " created an ICD Code");

            return new ResponseEntity(code, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not create ICD Code "
                                    + form.getCode()
                                    + " because of "
                                    + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a code from the system.
     *
     * @param id The ID of the code to delete
     * @return The result of the action.
     */
    @DeleteMapping("/icdcode/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCode(@PathVariable("id") final Long id) {
        try {
            final ICDCode code = (ICDCode) service.findById(id);
            service.delete(code);
            loggerUtil.log(
                    TransactionType.ICD_DELETE,
                    loggerUtil.getCurrentUsername(),
                    loggerUtil.getCurrentUsername() + " deleted an ICD Code");

            return new ResponseEntity(HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse(
                            "Could not delete ICD Code " + id + " because of " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
