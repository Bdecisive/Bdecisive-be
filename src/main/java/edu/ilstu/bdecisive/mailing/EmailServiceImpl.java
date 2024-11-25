package edu.ilstu.bdecisive.mailing;

import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    public void sendCategoryConfirmationEmail(User user, String categoryName, boolean isApproved) {
        String subject = isApproved ? "Your Category Submission Has Been Approved!"
                : "Update on Your Category Submission";

        String approvedMsg = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<p style=\"font-size: 16px;\">We are pleased to inform you that your category submission, \""+ categoryName +"\" has been successfully approved!</p>"
                + "<p style=\"font-size: 16px;\">Thank you for contributing to our platform. Your category is now live and visible to our community, and we look forward to the value it will bring.</p>"
                + "<p style=\"font-size: 16px;\">If you have any questions or need further assistance, feel free to reach out to us.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        String rejctionMsg = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<p style=\"font-size: 16px;\">Thank you for submitting \""+ categoryName +"\" to our platform. After careful review, we regret to inform you that your category submission did not meet the approval criteria at this time.</p>"
                + "<p style=\"font-size: 16px;\">If you would like feedback or assistance to help with a future submission, please feel free to reach out to us.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for your understanding, and we hope to see your contributions again.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        String msg = isApproved ? approvedMsg : rejctionMsg;
        try {
            sendVerificationEmail(user.getEmail(), subject, msg);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    public void sendVendorStatusEmail(User user, String companyName, boolean isApproved) {
        String subject = isApproved ? "Your Vendor Application Has Been Approved!"
                : "Update on Your Vendor Application";

        String approvedMsg = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #4CAF50; margin-bottom: 20px;\">Congratulations!</h2>"
                + "<p style=\"font-size: 16px;\">We are pleased to inform you that your vendor application for <strong>"+ companyName +"</strong> has been approved!</p>"
                + "<p style=\"font-size: 16px;\">You now have full access to our vendor portal where you can:</p>"
                + "<ul style=\"font-size: 16px;\">"
                + "<li>Manage your company profile</li>"
                + "<li>List your products and services</li>"
                + "<li>Interact with potential customers</li>"
                + "<li>Access vendor-specific features</li>"
                + "</ul>"
                + "<p style=\"font-size: 16px;\">To get started, simply log in to your account using your credentials.</p>"
                + "<p style=\"font-size: 16px; margin-top: 20px;\">If you have any questions or need assistance, our support team is here to help.</p>"
                + "<p style=\"font-size: 14px; color: #666; margin-top: 30px;\">Best regards,<br>The BDecisive Team</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        String rejectionMsg = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #666; margin-bottom: 20px;\">Application Status Update</h2>"
                + "<p style=\"font-size: 16px;\">Thank you for your interest in becoming a vendor on our platform. We have carefully reviewed your application for <strong>"+ companyName +"</strong>.</p>"
                + "<p style=\"font-size: 16px;\">After thorough consideration, we regret to inform you that your application does not meet our current vendor requirements.</p>"
                + "<p style=\"font-size: 16px;\">Common reasons for application rejection include:</p>"
                + "<ul style=\"font-size: 16px;\">"
                + "<li>Incomplete company information</li>"
                + "<li>Insufficient business documentation</li>"
                + "<li>Misalignment with platform requirements</li>"
                + "</ul>"
                + "<p style=\"font-size: 16px;\">You are welcome to submit a new application after addressing any potential issues. If you would like specific feedback about your application, please contact our support team.</p>"
                + "<p style=\"font-size: 16px; margin-top: 20px;\">We appreciate your interest in our platform and wish you the best in your business endeavors.</p>"
                + "<p style=\"font-size: 14px; color: #666; margin-top: 30px;\">Best regards,<br>The BDecisive Team</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        String msg = isApproved ? approvedMsg : rejectionMsg;
        try {
            sendVerificationEmail(user.getEmail(), subject, msg);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
