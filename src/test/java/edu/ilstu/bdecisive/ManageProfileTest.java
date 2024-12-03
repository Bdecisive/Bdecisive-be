package edu.ilstu.bdecisive;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.ilstu.bdecisive.dtos.UserDTO;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.UserRepository;
import edu.ilstu.bdecisive.services.UserService;
import edu.ilstu.bdecisive.services.impl.UserServiceImpl;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class ManageProfileTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Sample user data
        existingUser = new User();
        existingUser.setUserId(101L);
        existingUser.setFirstName("John");
        existingUser.setEmail("johndoe@example.com");
        existingUser.setPhoneNumber("+1234567890");
        existingUser.setPassword("encrypted_password");
        existingUser.setProfilePictureUrl("https://example.com/johndoe.jpg");

        when(userRepository.findById(101L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(anyString())).thenReturn("new_encrypted_password");
    }

    @Test
    void testUpdateUserProfileWithValidData() {
        UserDTO userProfileDTO = new UserDTO();
        userProfileDTO.setUserName("John Doe Updated");
        userProfileDTO.setEmail("johndoe_updated@example.com");
        userProfileDTO.setPhoneNumber("+1987654321");
        userProfileDTO.setPassword("newpassword123");
        userProfileDTO.setProfilePictureUrl("https://example.com/johndoe_updated.jpg");

        assertDoesNotThrow(() -> userService.updateUserProfile(101L, userProfileDTO));

        // Verify user details were updated correctly
        verify(userRepository, times(1)).save(existingUser);
        assertEquals("John Doe Updated", existingUser.getFirstName());
        assertEquals("johndoe_updated@example.com", existingUser.getEmail());
        assertEquals("+1987654321", existingUser.getPhoneNumber());
        assertEquals("new_encrypted_password", existingUser.getPassword());
        assertEquals("https://example.com/johndoe_updated.jpg", existingUser.getProfilePictureUrl());
    }

    @Test
    void testUpdateUserProfileWithInvalidPhoneNumber() {
        UserDTO userProfileDTO = new UserDTO();
        userProfileDTO.setPhoneNumber("invalid_phone");

        ServiceException exception = assertThrows(ServiceException.class, () ->
                userService.updateUserProfile(101L, userProfileDTO)
        );

        assertEquals("Please enter a valid phone number.", exception.getMessage());
    }

    @Test
    void testUpdateUserProfileWithInvalidProfilePictureUrl() {
        UserDTO userProfileDTO = new UserDTO();
        userProfileDTO.setProfilePictureUrl("invalid_url");

        ServiceException exception = assertThrows(ServiceException.class, () ->
                userService.updateUserProfile(101L, userProfileDTO)
        );

        assertEquals("Please provide a valid URL.", exception.getMessage());
    }

    @Test
    void testUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserDTO userProfileDTO = new UserDTO();
        userProfileDTO.setUserName("NonExistent User");

        ServiceException exception = assertThrows(ServiceException.class, () ->
                userService.updateUserProfile(999L, userProfileDTO)
        );

        assertEquals("Cannot Find the user.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testNetworkIssueDuringUpdate() {
        // Simulate a save failure
        doThrow(new RuntimeException("Network error")).when(userRepository).save(existingUser);

        UserDTO userProfileDTO = new UserDTO();
        userProfileDTO.setUserName("John Doe Network Issue");

        ServiceException exception = assertThrows(ServiceException.class, () ->
                userService.updateUserProfile(101L, userProfileDTO)
        );

        assertEquals("Fail to update user profile.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
