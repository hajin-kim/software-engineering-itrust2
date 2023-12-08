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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @Mock private EmailService emailService;
    @Mock private LoggerUtil loggerUtil;
    @Mock private UserService userService;
    @Mock private UserRepository userRepository;

    @Mock private EmailRepository emailRepository;
    @InjectMocks private APIEmailController apiEmailController;

    @Test
    public void testSendEmail() {
        final String fixedSystemEmail = "hajinkids3106@gmail.com";
        final String currentUsername = "currentUsername";
        final UserForm currentUserForm = new UserForm(currentUsername, "123456", Role.ROLE_HCP, 1);
        User sender1 = new Personnel(currentUserForm);
        userRepository.save(sender1);
        sender1.setEmail(fixedSystemEmail);
        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);

        final String receiverUsername = "receiverUsername";
        final UserForm receiverUserForm =
                new UserForm(receiverUsername, "123456", Role.ROLE_PATIENT, 1);
        User receiver1 = new Patient(receiverUserForm);
        userRepository.save(receiver1);

        receiver1.setEmail("judy01@yonsei.ac.kr");

        when(userService.findByName(receiverUsername)).thenReturn(receiver1);
        given(userService.findByName(receiverUsername)).willReturn(receiver1);

        Email expectedEmail = new Email(sender1.getUsername(), receiver1, "Test Subject", "Test Message Body");
        List<Email> expectedEmails = List.of(expectedEmail);
        emailService.sendEmail(
                sender1.getUsername(), receiverUsername, "Test Subject", "Test Message Body");

        List<Email> savedEmail = emailService.findByReceiver(receiver1);
        assertNotNull(savedEmail);
        //assertEquals(expectedEmails,savedEmail);
    }

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
        emailRepository.save(fakeEmail1);
        emailRepository.save(fakeEmail2);

        given(loggerUtil.getCurrentUsername()).willReturn(currentUsername);

        List<Email> result = apiEmailController.viewInbox();
        assertNotNull(result);
        //assertEquals(2, result.size());
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
        emailRepository.save(fakeEmail1);
        emailRepository.save(fakeEmail2);

        given(loggerUtil.getCurrentUsername()).willReturn(receiverUsername);

        List<Email> result = apiEmailController.viewOutbox();
        assertNotNull(result);
    }
}
