package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.services.EmailService;

import java.util.List;

import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class APIEmailController extends APIController {

    private final EmailService service;
    private final UserService userService;
    private final LoggerUtil loggerUtil;

    @GetMapping("emails")
    public List<Email> getEmails() {
        return (List<Email>) service.findAll();
    }

    @Operation(summary = "환자가 발송한 메일 목록을 조회합니다.")
    @GetMapping("/Outbox")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<Email> viewOutbox(){
        String currentUsername = loggerUtil.getCurrentUsername();
        return service.findBySender(currentUsername);
    }

    @Operation(summary = "환자가 수신한 메일 목록을 조회합니다.")
    @GetMapping("/Inbox")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<Email> viewInbox(){
        String currentUsername = loggerUtil.getCurrentUsername();
        User currentUser = userService.findByName(currentUsername);
        return service.findByReceiver(currentUser);
    }
}
