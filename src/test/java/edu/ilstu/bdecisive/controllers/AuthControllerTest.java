package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.VerifyUserDTO;
import edu.ilstu.bdecisive.security.request.LoginRequest;
import edu.ilstu.bdecisive.security.response.LoginResponse;
import edu.ilstu.bdecisive.services.AuthService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LocalValidatorFactoryBean validator;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private VerifyUserDTO verifyUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        // Initialize test data
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        loginResponse = new LoginResponse("test.jwt.token");

        verifyUserDTO = new VerifyUserDTO();
        verifyUserDTO.setUsername("testuser");
        verifyUserDTO.setVerificationCode("123456");
    }

    @Test
    void authenticateUser_WhenValidCredentials_ShouldReturnToken() throws ServiceException {
        // Arrange
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        LoginResponse returnedResponse = (LoginResponse) response.getBody();
        assertEquals("test.jwt.token", returnedResponse.getToken());
        verify(authService, times(1)).authenticateUser(loginRequest);
    }

    @Test
    void authenticateUser_WhenServiceThrowsException_ShouldPropagateException() throws ServiceException {
        // Arrange
        when(authService.authenticateUser(any(LoginRequest.class)))
                .thenThrow(new ServiceException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> authController.authenticateUser(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void loginRequest_ValidationConstraints() {
        // Empty username
        LoginRequest request = new LoginRequest();
        request.setPassword("password123");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));

        // Username too long (> 70 characters)
        request.setUsername("a".repeat(71));
        violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));

        // Password too short (< 6 characters)
        request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("12345");
        violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));

        // Password too long (> 120 characters)
        request.setPassword("a".repeat(121));
        violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void verifyUser_WhenSuccessful_ShouldReturnSuccessMessage() throws ServiceException {
        // Arrange
        doNothing().when(authService).verifyUser(any(VerifyUserDTO.class));

        // Act
        ResponseEntity<?> response = authController.verifyUser(verifyUserDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account verified successfully", response.getBody());
        verify(authService, times(1)).verifyUser(verifyUserDTO);
    }

    @Test
    void verifyUser_WhenRuntimeException_ShouldReturnBadRequest() throws ServiceException {
        // Arrange
        doThrow(new RuntimeException("Invalid verification code"))
                .when(authService).verifyUser(any(VerifyUserDTO.class));

        // Act
        ResponseEntity<?> response = authController.verifyUser(verifyUserDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid verification code", response.getBody());
    }

    @Test
    void verifyUserDTO_ValidationConstraints() {
        // Empty username
        VerifyUserDTO dto = new VerifyUserDTO();
        dto.setVerificationCode("123456");
        Set<ConstraintViolation<VerifyUserDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));

        // Username too long (> 70 characters)
        dto.setUsername("a".repeat(71));
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));

        // Empty verification code
        dto = new VerifyUserDTO();
        dto.setUsername("testuser");
        violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("verificationCode")));
    }

    @Test
    void resendVerificationCode_WhenSuccessful_ShouldReturnSuccessMessage() throws ServiceException {
        // Arrange
        doNothing().when(authService).resendVerificationCode(anyString());

        // Act
        ResponseEntity<?> response = authController.resendVerificationCode("testuser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verification code sent", response.getBody());
        verify(authService, times(1)).resendVerificationCode("testuser");
    }

    @Test
    void resendVerificationCode_WhenRuntimeException_ShouldReturnBadRequest() throws ServiceException {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(authService).resendVerificationCode(anyString());

        // Act
        ResponseEntity<?> response = authController.resendVerificationCode("testuser");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }
}
