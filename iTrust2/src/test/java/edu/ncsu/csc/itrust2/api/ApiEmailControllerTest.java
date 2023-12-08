package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.controllers.api.APIEmailController;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.repositories.EmailRepository;
import edu.ncsu.csc.itrust2.repositories.UserRepository;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiEmailControllerTest {
    @Mock private EmailService emailService;
    @Mock private LoggerUtil loggerUtil;
    @Mock private UserService userService;
    @InjectMocks private APIEmailController apiEmailController;

    @Test
    public void testViewInbox() {
        final String currentUsername = "currentUsername";
        final UserForm currentUserForm =
                new UserForm(currentUsername, "123456", Role.ROLE_PATIENT, 1);
        final User sender1 = new Patient(currentUserForm);
        sender1.setEmail("hajinkids3106@gmail.com");
        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);
        given(userService.findByName(currentUsername)).willReturn(sender1);

        final String receiverUsername = "receiverUsername";
        final UserForm receiverUserForm =
                new UserForm(receiverUsername, "123456", Role.ROLE_PATIENT, 1);
        final User receiver1 = new Patient(receiverUserForm);
        receiver1.setEmail("12345@itrust.com");
        given(userService.findByName(receiverUsername)).willReturn(receiver1);

        final String receiver2Username = "receiver2Username";
        final UserForm receiver2UserForm =
                new UserForm(receiver2Username, "123456", Role.ROLE_HCP, 1);
        final User receiver2 = new Personnel(receiver2UserForm);
        receiver1.setEmail("abcde@itrust.com");
        given(userService.findByName(receiver2Username)).willReturn(receiver2);

        Email fakeEmail1 = new Email("currentUsername", receiver1, "Subject1", "Body1");
        Email fakeEmail2 = new Email("currentUsername", receiver2, "Subject2", "Body2");
        List<Email> expectedEmails = Arrays.asList(fakeEmail1, fakeEmail2);
        //emailService.save(fakeEmail1);
        //emailService.save(fakeEmail2);

        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);

        List<Email> result = apiEmailController.viewInbox();
        assertNotNull(result);
        assertEquals(expectedEmails, result);
    }

    @Test
    public void testViewOutbox() {
        final String currentUsername = "currentUsername";
        final UserForm currentUserForm =
                new UserForm(currentUsername, "123456", Role.ROLE_PATIENT, 1);
        final User sender1 = new Patient(currentUserForm);
        sender1.setEmail("hajinkids3106@gmail.com");
        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);
        given(userService.findByName(currentUsername)).willReturn(sender1);

        final String receiverUsername = "receiverUsername";
        final UserForm receiverUserForm =
                new UserForm(receiverUsername, "123456", Role.ROLE_PATIENT, 1);
        final User receiver1 = new Patient(receiverUserForm);
        receiver1.setEmail("12345@itrust.com");
        given(userService.findByName(receiverUsername)).willReturn(receiver1);

        final String receiver2Username = "receiver2Username";
        final UserForm receiver2UserForm =
                new UserForm(receiver2Username, "123456", Role.ROLE_HCP, 1);
        final User receiver2 = new Personnel(receiver2UserForm);
        receiver1.setEmail("abcde@itrust.com");
        given(userService.findByName(receiver2Username)).willReturn(receiver2);

        Email fakeEmail1 = new Email("currentUsername", receiver1, "Subject1", "Body1");
        Email fakeEmail2 = new Email("currentUsername", receiver2, "Subject2", "Body2");
        emailService.save(fakeEmail1);
        emailService.save(fakeEmail2);

        given(loggerUtil.getCurrentUsername()).willReturn(receiverUsername);

        List<Email> result = apiEmailController.viewOutbox();
        assertNotNull(result);
    }
}
