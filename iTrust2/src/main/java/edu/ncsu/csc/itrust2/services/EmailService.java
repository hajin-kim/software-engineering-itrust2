package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.forms.EmailForm;
import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.EmailRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailService extends Service {

    private final EmailRepository repository;
    private final UserService userService;

    @Autowired private JavaMailSender emailSender;

    public void sendEmail(@NotNull EmailForm emailForm) {
        final String senderEmail = "hajinkids3106@gmail.com";

        User Receiver = userService.findByName(emailForm.getReceiver());
        String ReceiverEmail = Receiver.getEmail();

        if (isEmail(ReceiverEmail)) {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(senderEmail);
            message.setTo(ReceiverEmail);
            message.setSubject("[iTrust2]" + emailForm.getSubject());
            message.setText(
                    emailForm.getSender() + "로부터 전송된 메세지입니다.\n" + emailForm.getMessageBody());

            emailSender.send(message);
        }
        Email email =
                new Email(
                        emailForm.getSender(),
                        Receiver,
                        emailForm.getSubject(),
                        emailForm.getMessageBody());

        repository.save(email);
    }

    @Override
    protected JpaRepository getRepository() {
        return repository;
    }

    public List<Email> findByReceiver(final User receiver) {
        return repository.findByReceiver(receiver);
    }

    public static boolean isEmail(String email) {
        boolean validation = false;

        if (email.isEmpty()) {
            return false;
        }

        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            validation = true;
        }

        return validation;
    }
}
