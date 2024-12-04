package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.mailing.EmailServiceImpl;
import edu.ilstu.bdecisive.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        user = new User();
        user.setEmail("test@example.com");
        user.setVerificationCode("123456");
        user.setUsername("testuser");
    }

    @Test
    void sendVerificationEmail_ShouldSendEmail() throws MessagingException {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        // Act
        emailService.sendVerificationEmail(to, subject, text);

        // Assert
        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendVerificationEmail_ToUser_ShouldSendFormattedEmail() throws MessagingException {
        // Act
        emailService.sendVerificationEmail(user);

        // Assert
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendCategoryConfirmationEmail_WhenApproved_ShouldSendApprovalEmail() throws MessagingException {
        // Act
        emailService.sendCategoryConfirmationEmail(user, "Test Category", true);

        // Assert
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendCategoryConfirmationEmail_WhenRejected_ShouldSendRejectionEmail() throws MessagingException {
        // Act
        emailService.sendCategoryConfirmationEmail(user, "Test Category", false);

        // Assert
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendVendorStatusEmail_WhenApproved_ShouldSendApprovalEmail() throws MessagingException {
        // Act
        emailService.sendVendorStatusEmail(user, "Test Company", true);

        // Assert
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void sendVendorStatusEmail_WhenRejected_ShouldSendRejectionEmail() throws MessagingException {
        // Act
        emailService.sendVendorStatusEmail(user, "Test Company", false);

        // Assert
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void generateVerificationCode_ShouldReturnSixDigitCode() {
        // Act
        String code = emailService.generateVerificationCode();

        // Assert
        assertNotNull(code);
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));
    }
}
