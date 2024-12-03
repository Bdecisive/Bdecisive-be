package edu.ilstu.bdecisive.mailing;

import edu.ilstu.bdecisive.models.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationEmail(String to, String subject, String text) throws MessagingException;
    void sendVerificationEmail(User user);
    void sendCategoryConfirmationEmail(User user, String categoryName, boolean isApproved);
    String generateVerificationCode();
    void sendVendorStatusEmail(User user, String companyName, boolean isApproved);
}
