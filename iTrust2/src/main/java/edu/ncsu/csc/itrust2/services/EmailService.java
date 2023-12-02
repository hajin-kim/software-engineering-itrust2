package edu.ncsu.csc.itrust2.services;

import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.EmailRepository;

import java.util.List;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailService extends Service {

    private final EmailRepository repository;

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

    public List<Email> findBySender(final String sender) {
        return repository.findBySender(sender);
    }
}
