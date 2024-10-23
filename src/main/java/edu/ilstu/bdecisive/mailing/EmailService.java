package edu.ilstu.bdecisive.mailing;

import edu.ilstu.bdecisive.models.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(String to, String subject, String text) throws MessagingException;
    void sendVerificationEmail(User user);
    String generateVerificationCode();
}
