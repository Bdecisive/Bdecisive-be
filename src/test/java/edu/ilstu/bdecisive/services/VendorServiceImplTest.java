package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.models.Vendor;
import edu.ilstu.bdecisive.repositories.VendorRepository;
import edu.ilstu.bdecisive.services.impl.VendorServiceImpl;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class VendorServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private VendorServiceImpl vendorService;

    private User user;
    private Vendor vendor;
    private VendorRequestDTO vendorRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        user = new User("testuser", "test@example.com", "password123");
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        vendor = new Vendor();
        vendor.setId(1L);
        vendor.setCompanyName("Test Company");
        vendor.setAddress("123 Test St");
        vendor.setDescription("Test Description");
        vendor.setPhone("1234567890");
        vendor.setUser(user);
        vendor.setApproved(false);
        vendor.setCreatedAt(LocalDateTime.now());

        vendorRequestDTO = new VendorRequestDTO();
        vendorRequestDTO.setUsername("testuser");
        vendorRequestDTO.setEmail("test@example.com");
        vendorRequestDTO.setPassword("password123");
        vendorRequestDTO.setFirstName("John");
        vendorRequestDTO.setLastName("Doe");
        vendorRequestDTO.setCompanyName("Test Company");
        vendorRequestDTO.setAddress("123 Test St");
        vendorRequestDTO.setDescription("Test Description");
        vendorRequestDTO.setPhone("1234567890");
    }

    @Test
    void create_WhenValidRequest_ShouldCreateVendor() throws ServiceException {
        // Arrange
        when(userService.findByUsername(any())).thenReturn(Optional.empty());
        when(userService.findByEmail(any())).thenReturn(Optional.empty());
        when(userService.registerUser(any(User.class), eq(AppRole.ROLE_VENDOR), eq(false)))
                .thenReturn(user);
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        // Act
        vendorService.create(vendorRequestDTO);

        // Assert
        verify(userService, times(1)).registerUser(any(User.class), eq(AppRole.ROLE_VENDOR), eq(false));
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void create_WhenUsernameExists_ShouldThrowException() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> vendorService.create(vendorRequestDTO));
        assertEquals("Username is already taken!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void create_WhenEmailExists_ShouldThrowException() {
        // Arrange
        when(userService.findByUsername(any())).thenReturn(Optional.empty());
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> vendorService.create(vendorRequestDTO));
        assertEquals("Email is already in use!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void approveVendorAccount_WhenVendorExists_ShouldApproveAndSendEmail() throws ServiceException {
        // Arrange
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);
        doNothing().when(emailService).sendVendorStatusEmail(any(), any(), eq(true));

        // Act
        boolean result = vendorService.approveVendorAccount(1L);

        // Assert
        assertTrue(result);
        assertTrue(vendor.isApproved());
        assertNotNull(vendor.getApprovedDate());
        verify(vendorRepository, times(1)).save(vendor);
        verify(emailService, times(1))
                .sendVendorStatusEmail(eq(user), eq(vendor.getCompanyName()), eq(true));
    }

    @Test
    void approveVendorAccount_WhenVendorNotFound_ShouldThrowException() {
        // Arrange
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> vendorService.approveVendorAccount(1L));
        assertEquals("Vendor account doesn't exist for id: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void rejectVendorAccount_WhenVendorExists_ShouldRejectAndSendEmail() throws ServiceException {
        // Arrange
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);
        doNothing().when(emailService).sendVendorStatusEmail(any(), any(), eq(false));

        // Act
        boolean result = vendorService.rejectVendorAccount(1L);

        // Assert
        assertTrue(result);
        assertFalse(vendor.isApproved());
        assertNotNull(vendor.getApprovedDate());
        verify(vendorRepository, times(1)).save(vendor);
        verify(emailService, times(1))
                .sendVendorStatusEmail(eq(user), eq(vendor.getCompanyName()), eq(false));
    }

    @Test
    void getVendorByUserId_ShouldReturnVendorDTO() throws ServiceException {
        // Arrange
        when(userService.findUserById(1L)).thenReturn(user);
        when(vendorRepository.findByUser(user)).thenReturn(Optional.of(vendor));

        // Act
        VendorDTO result = vendorService.getVendorByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(vendor.getId(), result.getId());
        assertEquals(vendor.getCompanyName(), result.getCompanyName());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void isApproved_WhenVendorExists_ShouldReturnApprovalStatus() {
        // Arrange
        vendor.setApproved(true);
        when(vendorRepository.findByUser(user)).thenReturn(Optional.of(vendor));

        // Act
        boolean result = vendorService.isApproved(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void isApproved_WhenVendorDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(vendorRepository.findByUser(user)).thenReturn(Optional.empty());

        // Act
        boolean result = vendorService.isApproved(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void list_ShouldReturnAllVendors() {
        // Arrange
        List<Vendor> vendors = Arrays.asList(vendor);
        when(vendorRepository.findAll()).thenReturn(vendors);

        // Act
        List<VendorDTO> result = vendorService.list();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        VendorDTO dto = result.get(0);
        assertEquals(vendor.getId(), dto.getId());
        assertEquals(vendor.getCompanyName(), dto.getCompanyName());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getEmail(), dto.getEmail());
    }
}
