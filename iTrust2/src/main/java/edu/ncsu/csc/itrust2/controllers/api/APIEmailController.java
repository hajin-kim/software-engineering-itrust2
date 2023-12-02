package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.EmailForm;
import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.services.EmailService;

import java.util.List;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[UC23] 이메일 전송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class APIEmailController extends APIController {
    @Autowired private final EmailService service;

    @GetMapping("emails")
    public List<Email> getEmails() {
        return (List<Email>) service.findAll();
    }

    @Operation(summary = "Everyone: 이메일 실제 발송")
    @PostMapping("/emails/send")
    public void sendEmail(
            @Parameter(description = "발신자 이름, 수신자 이름, 제목, 내용입니다.") @RequestBody @NotNull EmailForm emailForm) {
        service.sendEmail(emailForm);
    }
}
