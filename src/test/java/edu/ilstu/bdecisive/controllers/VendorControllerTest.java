package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.dtos.VendorRequestDTO;
import edu.ilstu.bdecisive.services.VendorService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendorControllerTest {

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;

    private VendorDTO vendorDTO;
    private VendorRequestDTO vendorRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        vendorDTO = new VendorDTO();
        vendorDTO.setId(1L);
        vendorDTO.setCompanyName("Test Company");
        vendorDTO.setFirstName("John");
        vendorDTO.setLastName("Doe");
        vendorDTO.setAddress("123 Test St");
        vendorDTO.setDescription("Test Description");
        vendorDTO.setPhone("1234567890");
        vendorDTO.setEmail("test@test.com");
        vendorDTO.setUsername("testuser");
        vendorDTO.setApproved(false);

        vendorRequestDTO = new VendorRequestDTO();
        vendorRequestDTO.setId(1L);
        vendorRequestDTO.setCompanyName("Test Company");
        vendorRequestDTO.setFirstName("John");
        vendorRequestDTO.setLastName("Doe");
        vendorRequestDTO.setAddress("123 Test St");
        vendorRequestDTO.setDescription("Test Description");
        vendorRequestDTO.setPhone("1234567890");
        vendorRequestDTO.setEmail("test@test.com");
        vendorRequestDTO.setUsername("testuser");
        vendorRequestDTO.setPassword("password123");
    }

    @Test
    void list_ShouldReturnListOfVendors() {
        // Arrange
        List<VendorDTO> expectedVendors = Arrays.asList(vendorDTO);
        when(vendorService.list()).thenReturn(expectedVendors);

        // Act
        List<VendorDTO> actualVendors = vendorController.list();

        // Assert
        assertNotNull(actualVendors);
        assertEquals(expectedVendors.size(), actualVendors.size());
        assertEquals(expectedVendors.get(0).getId(), actualVendors.get(0).getId());
        verify(vendorService, times(1)).list();
    }

    @Test
    void createVendorRequest_ShouldReturnSuccessMessage() throws ServiceException {
        // Arrange
        doNothing().when(vendorService).create(any(VendorRequestDTO.class));

        // Act
        ResponseEntity<?> response = vendorController.createVendorRequest(vendorRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vendor request created successfully", response.getBody());
        verify(vendorService, times(1)).create(vendorRequestDTO);
    }

    @Test
    void createVendorRequest_WhenServiceThrowsException_ShouldPropagateException() throws ServiceException {
        // Arrange
        doThrow(new ServiceException("Creation failed", HttpStatus.BAD_REQUEST))
                .when(vendorService).create(any(VendorRequestDTO.class));

        // Act & Assert
        assertThrows(ServiceException.class, () ->
                vendorController.createVendorRequest(vendorRequestDTO));
        verify(vendorService, times(1)).create(vendorRequestDTO);
    }

    @Test
    void approveVendorAccount_WhenSuccessful_ShouldReturnSuccessMessage() throws ServiceException {
        // Arrange
        when(vendorService.approveVendorAccount(1L)).thenReturn(true);

        // Act
        ResponseEntity<String> response = vendorController.approveVendorAccount(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vendor account approved successfully", response.getBody());
        verify(vendorService, times(1)).approveVendorAccount(1L);
    }

    @Test
    void approveVendorAccount_WhenFailed_ShouldReturnBadRequest() throws ServiceException {
        // Arrange
        when(vendorService.approveVendorAccount(1L)).thenReturn(false);

        // Act
        ResponseEntity<String> response = vendorController.approveVendorAccount(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Vendor approval failed", response.getBody());
        verify(vendorService, times(1)).approveVendorAccount(1L);
    }

    @Test
    void approveVendorAccount_WhenServiceThrowsException_ShouldPropagateException() throws ServiceException {
        // Arrange
        when(vendorService.approveVendorAccount(1L))
                .thenThrow(new ServiceException("Approval failed", HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ServiceException.class, () ->
                vendorController.approveVendorAccount(1L));
        verify(vendorService, times(1)).approveVendorAccount(1L);
    }

    @Test
    void rejectVendorAccount_WhenSuccessful_ShouldReturnSuccessMessage() throws ServiceException {
        // Arrange
        when(vendorService.rejectVendorAccount(1L)).thenReturn(true);

        // Act
        ResponseEntity<String> response = vendorController.rejectVendorAccount(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vendor account approved successfully", response.getBody());
        verify(vendorService, times(1)).rejectVendorAccount(1L);
    }

    @Test
    void rejectVendorAccount_WhenFailed_ShouldReturnBadRequest() throws ServiceException {
        // Arrange
        when(vendorService.rejectVendorAccount(1L)).thenReturn(false);

        // Act
        ResponseEntity<String> response = vendorController.rejectVendorAccount(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Vendor approval failed", response.getBody());
        verify(vendorService, times(1)).rejectVendorAccount(1L);
    }

    @Test
    void rejectVendorAccount_WhenServiceThrowsException_ShouldPropagateException() throws ServiceException {
        // Arrange
        when(vendorService.rejectVendorAccount(1L))
                .thenThrow(new ServiceException("Rejection failed", HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ServiceException.class, () ->
                vendorController.rejectVendorAccount(1L));
        verify(vendorService, times(1)).rejectVendorAccount(1L);
    }
}
