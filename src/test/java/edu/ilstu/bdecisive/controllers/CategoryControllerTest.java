package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.CategoryDTO;
import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.services.CategoryService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("Test Category");
        categoryRequestDTO.setDescription("Test Description");

        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);
        categoryResponseDTO.setName("Test Category");
        categoryResponseDTO.setDescription("Test Description");
        categoryResponseDTO.setApproved(true);

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Test Category");
    }

    @Test
    void list_ShouldReturnCategories() throws ServiceException {
        // Arrange
        List<CategoryResponseDTO> expectedCategories = Arrays.asList(categoryResponseDTO);
        when(categoryService.list(any(), any(), any())).thenReturn(expectedCategories);

        // Act
        ResponseEntity<List<CategoryResponseDTO>> response = categoryController.list(
                Optional.of("name"), Optional.of("description"));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCategories.size(), response.getBody().size());
        verify(categoryService).list(Optional.of("name"), Optional.of("description"), Optional.of(true));
    }

    @Test
    void listGlobalCategory_ShouldReturnCategories() {
        // Arrange
        List<CategoryDTO> expectedCategories = Arrays.asList(categoryDTO);
        when(categoryService.listGlobalCategory()).thenReturn(expectedCategories);

        // Act
        ResponseEntity<List<CategoryDTO>> response = categoryController.listGlobalCategory();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCategories.size(), response.getBody().size());
        verify(categoryService).listGlobalCategory();
    }

    @Test
    void getPendingApproval_ShouldReturnVendorCategories() throws ServiceException {
        // Arrange
        List<CategoryResponseDTO> expectedCategories = Arrays.asList(categoryResponseDTO);
        when(categoryService.vendorCategoryList()).thenReturn(expectedCategories);

        // Act
        ResponseEntity<List<CategoryResponseDTO>> response = categoryController.getPendingApproval();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCategories.size(), response.getBody().size());
        verify(categoryService).vendorCategoryList();
    }

    @Test
    void createCategoryRequest_ShouldCreateCategory() throws ServiceException {
        // Arrange
        doNothing().when(categoryService).create(any(CategoryRequestDTO.class));

        // Act
        ResponseEntity<?> response = categoryController.createCategoryRequest(categoryRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category request created successfully", response.getBody());
        verify(categoryService).create(categoryRequestDTO);
    }

    @Test
    void approveCategory_WhenSuccessful_ShouldReturnSuccess() throws ServiceException {
        // Arrange
        when(categoryService.approveOrRejectCategory(1L, true)).thenReturn(true);

        // Act
        ResponseEntity<String> response = categoryController.approveCategory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category approved successfully", response.getBody());
        verify(categoryService).approveOrRejectCategory(1L, true);
    }

    @Test
    void approveCategory_WhenFailed_ShouldReturnBadRequest() throws ServiceException {
        // Arrange
        when(categoryService.approveOrRejectCategory(1L, true)).thenReturn(false);

        // Act
        ResponseEntity<String> response = categoryController.approveCategory(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Category approval failed", response.getBody());
        verify(categoryService).approveOrRejectCategory(1L, true);
    }

    @Test
    void rejectCategory_WhenSuccessful_ShouldReturnSuccess() throws ServiceException {
        // Arrange
        when(categoryService.approveOrRejectCategory(1L, false)).thenReturn(true);

        // Act
        ResponseEntity<String> response = categoryController.rejectCategory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category rejected successfully", response.getBody());
        verify(categoryService).approveOrRejectCategory(1L, false);
    }

    @Test
    void rejectCategory_WhenFailed_ShouldReturnBadRequest() throws ServiceException {
        // Arrange
        when(categoryService.approveOrRejectCategory(1L, false)).thenReturn(false);

        // Act
        ResponseEntity<String> response = categoryController.rejectCategory(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Category rejection failed", response.getBody());
        verify(categoryService).approveOrRejectCategory(1L, false);
    }

    @Test
    void list_WhenServiceThrowsException_ShouldPropagateException() throws ServiceException {
        // Arrange
        when(categoryService.list(any(), any(), any()))
                .thenThrow(new ServiceException("Error listing categories", HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(ServiceException.class, () ->
                categoryController.list(Optional.empty(), Optional.empty()));
    }

    @Test
    void createCategoryRequest_WhenServiceThrowsException_ShouldPropagateException() throws ServiceException {
        // Arrange
        doThrow(new ServiceException("Error creating category", HttpStatus.BAD_REQUEST))
                .when(categoryService).create(any(CategoryRequestDTO.class));

        // Act & Assert
        assertThrows(ServiceException.class, () ->
                categoryController.createCategoryRequest(categoryRequestDTO));
    }
}
