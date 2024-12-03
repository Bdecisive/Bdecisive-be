package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.Role;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.security.jwt.JwtUtils;
import edu.ilstu.bdecisive.security.request.LoginRequest;
import edu.ilstu.bdecisive.security.services.UserDetailsImpl;
import edu.ilstu.bdecisive.services.impl.AuthServiceImpl;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private VendorService vendorService;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private User user;
    private Role role;
    private UserDetailsImpl userDetails;
    private VerifyUserDTO verifyUserDTO;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();

        // Initialize login request
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // Initialize role
        role = new Role();
        role.setRoleName(AppRole.ROLE_VENDOR);

        // Initialize user
        user = new User("testuser", "test@example.com", "password123");
        user.setUserId(1L);
        user.setEnabled(true);
        user.setRole(role);
        user.setFirstName("John");
        user.setLastName("Doe");

        // Initialize UserDetailsImpl
        GrantedAuthority authority = new SimpleGrantedAuthority(AppRole.ROLE_VENDOR.name());
        userDetails = new UserDetailsImpl(
                1L,
                "testuser",
                "test@example.com",
                "John",
                "Doe",
                "password123",
                true,
                List.of(authority)
        );

        // Initialize verification DTO
        verifyUserDTO = new VerifyUserDTO();
        verifyUserDTO.setUsername("testuser");
        verifyUserDTO.setVerificationCode("123456");

        jwtToken = "test.jwt.token";
    }

    @Test
    void authenticateUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> authService.authenticateUser(loginRequest));
        assertEquals("User account doesn't exists.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void authenticateUser_WhenUserNotVerified_ShouldThrowException() {
        // Arrange
        user.setEnabled(false);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> authService.authenticateUser(loginRequest));
        assertEquals("Account not verified. Please verify your account.", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void authenticateUser_WhenVendorNotApproved_ShouldThrowException() {
        // Arrange
        role.setRoleName(AppRole.ROLE_VENDOR);
        user.setRole(role);
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(vendorService.isApproved(user)).thenReturn(false);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> authService.authenticateUser(loginRequest));
        assertEquals("Vendor has not approved yet! Please wait for the admin to review and approve your account.",
                exception.getMessage());
        verify(vendorService).isApproved(user);
    }

    @Test
    void verifyUser_WhenValidCode_ShouldVerifyAndUpdateUser() throws ServiceException {
        // Arrange
        user.setEnabled(false);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(30));
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));
        doNothing().when(userService).save(any(User.class));

        // Act
        authService.verifyUser(verifyUserDTO);

        // Assert
        assertTrue(user.isEnabled());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiresAt());
        verify(userService).save(user);
    }

    @Test
    void resendVerificationCode_WhenValidUser_ShouldGenerateAndSendNewCode() throws ServiceException {
        // Arrange
        user.setEnabled(false);
        String newVerificationCode = "654321";
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(emailService.generateVerificationCode()).thenReturn(newVerificationCode);
        doNothing().when(userService).save(any(User.class));

        // Act
        authService.resendVerificationCode("testuser");

        // Assert
        assertEquals(newVerificationCode, user.getVerificationCode());
        assertNotNull(user.getVerificationCodeExpiresAt());
        verify(emailService).sendVerificationEmail(user);
        verify(userService).save(user);
    }

    @Test
    void forgotPassword_ShouldDelegateToUserService() {
        // Arrange
        String email = "test@example.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            authService.forgotPassword(email);
            verify(userService).generatePasswordResetToken(email);
        });
    }

    @Test
    void resetPassword_ShouldDelegateToUserService() {
        // Arrange
        String token = "reset_token";
        String newPassword = "newPassword123";

        // Act & Assert
        assertDoesNotThrow(() -> {
            authService.resetPassword(token, newPassword);
            verify(userService).resetPassword(token, newPassword);
        });
    }
}
