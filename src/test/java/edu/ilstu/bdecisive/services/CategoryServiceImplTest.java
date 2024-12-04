package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.CategoryDTO;
import edu.ilstu.bdecisive.dtos.CategoryRequestDTO;
import edu.ilstu.bdecisive.dtos.CategoryResponseDTO;
import edu.ilstu.bdecisive.dtos.VendorDTO;
import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.mailing.EmailService;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.Role;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.CategoryRepository;
import edu.ilstu.bdecisive.services.impl.CategoryServiceImpl;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private EmailService emailService;
    @Mock
    private UserService userService;
    @Mock
    private VendorService vendorService;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private User user;
    private Role role;
    private VendorDTO vendorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize role
        role = new Role();
        role.setRoleName(AppRole.ROLE_VENDOR);

        // Initialize user
        user = new User("testuser", "test@example.com", "password");
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(role);

        // Initialize category
        category = new Category("Test Category", "Test Description");
        category.setId(1L);
        category.setApproved(false);
        category.setUser(user);
        category.setCreatedAt(LocalDateTime.now());

        // Initialize request DTO
        categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("Test Category");
        categoryRequestDTO.setDescription("Test Description");

        // Initialize vendor DTO
        vendorDTO = new VendorDTO();
        vendorDTO.setId(1L);
        vendorDTO.setCompanyName("Test Company");

        // Initialize response DTO
        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);
        categoryResponseDTO.setName("Test Category");
        categoryResponseDTO.setDescription("Test Description");
    }

    @Test
    void findByCategoryName_ShouldReturnCategory() {
        // Arrange
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.of(category));

        // Act
        Optional<Category> result = categoryService.findByCategoryName("Test Category");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Category", result.get().getName());
    }

    @Test
    void list_WhenUserIsVendor_ShouldReturnFilteredCategories() throws ServiceException {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll(any(Example.class))).thenReturn(categories);
        when(userService.getCurrentUser()).thenReturn(user);
        when(vendorService.getVendorByUserId(1L)).thenReturn(vendorDTO);

        // Act
        List<CategoryResponseDTO> result = categoryService.list(
                Optional.of("Test"), Optional.of("Description"), Optional.of(true));

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getName());
    }

    @Test
    void vendorCategoryList_ShouldReturnVendorCategories() throws ServiceException {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        when(userService.getCurrentUser()).thenReturn(user);
        when(categoryRepository.findByUser(user)).thenReturn(categories);
        when(vendorService.getVendorByUserId(1L)).thenReturn(vendorDTO);

        // Act
        List<CategoryResponseDTO> result = categoryService.vendorCategoryList();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getName());
    }

    @Test
    void listGlobalCategory_ShouldReturnApprovedCategories() {
        // Arrange
        category.setApproved(true);
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findByApproved(true)).thenReturn(categories);

        // Act
        List<CategoryDTO> result = categoryService.listGlobalCategory();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getName());
    }

    @Test
    void findById_WhenCategoryExists_ShouldReturnCategory() throws ServiceException {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
    }

    @Test
    void findById_WhenCategoryNotFound_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> categoryService.findById(1L));
        assertEquals("Category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void create_WhenCategoryNameExists_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.of(category));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> categoryService.create(categoryRequestDTO));
        assertEquals("Category name already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void create_WhenUserIsAdmin_ShouldCreateApprovedCategory() throws ServiceException {
        // Arrange
        role.setRoleName(AppRole.ROLE_ADMIN);
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(user);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        categoryService.create(categoryRequestDTO);

        // Assert
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void approveOrRejectCategory_WhenSuccessful_ShouldUpdateCategoryAndSendEmail() throws ServiceException {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(emailService).sendCategoryConfirmationEmail(any(), any(), anyBoolean());

        // Act
        boolean result = categoryService.approveOrRejectCategory(1L, true);

        // Assert
        assertTrue(result);
        assertTrue(category.isApproved());
        assertNotNull(category.getApprovedDate());
        verify(categoryRepository).save(category);
        verify(emailService).sendCategoryConfirmationEmail(user, category.getName(), true);
    }

    @Test
    void approveOrRejectCategory_WhenCategoryNotFound_ShouldThrowException() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> categoryService.approveOrRejectCategory(1L, true));
        assertEquals("Category doesn't exist for id: 1", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
