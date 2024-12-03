package edu.ilstu.bdecisive.services;

import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.models.Category;
import edu.ilstu.bdecisive.models.Product;
import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.repositories.ProductRepository;
import edu.ilstu.bdecisive.repositories.UserRepository;
import edu.ilstu.bdecisive.services.impl.ProductServiceImpl;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() throws ServiceException, ServiceException {
        
        ProductRequestDTO requestDTO = new ProductRequestDTO(1L, "New Product", "Description", 100.0, 1L, 1L);
        User user = new User("john_doe", "john@example.com");
        user.setUserId(1L);
        Category category = new Category("Electronics", "Electronic devices");
        category.setId(1L);

        when(userService.findUserById(1L)).thenReturn(user);
        when(categoryService.findById(1L)).thenReturn(category);

        productService.create(requestDTO);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testProductUpdate() throws ServiceException {
        Product existingProduct = new Product(1L, "Old Product", "Old Description", 50.0, new Category(), new User());
        ProductRequestDTO requestDTO = new ProductRequestDTO(1L, "Updated Product", "Updated Description", 60.0, 1L, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        productService.productUpdate(1L, requestDTO);

        assertEquals("Updated Product", existingProduct.getName());
        assertEquals("Updated Description", existingProduct.getDescription());
        assertEquals(60.0, existingProduct.getPrice());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testDelete() throws ServiceException {
        
        Product product = new Product(1L, "Product", "Description", 50.0, new Category(), new User());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(reviewService, times(1)).deleteByProduct(product);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testGetProductsByVendor() throws ServiceException {
        User vendor = new User("john_doe", "john@example.com");
        vendor.setUserId(1L);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Smartphone", "Latest model", 699.99, new Category(), vendor));
        products.add(new Product(2L, "Laptop", "Gaming laptop", 1299.99, new Category(), vendor));

        when(userService.findUserById(1L)).thenReturn(vendor);
        when(productRepository.findByUser(vendor)).thenReturn(products);

        List<ProductDTO> result = productService.getProductsByVendor(1L);

        assertEquals(2, result.size());
        assertEquals("Smartphone", result.get(0).getName());
        assertEquals("Laptop", result.get(1).getName());
        verify(userService, times(1)).findUserById(1L);
        verify(productRepository, times(1)).findByUser(vendor);
    }

    @Test
    void testGetProductsByCategory() throws ServiceException {
        Category category = new Category("Electronics", "Electronic devices");
        category.setId(1L);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Phone", "Smartphone", 599.99, category, new User()));
        products.add(new Product(2L, "Laptop", "Gaming Laptop", 1299.99, category, new User()));

        when(categoryService.findById(1L)).thenReturn(category);
        when(productRepository.findByCategory(category)).thenReturn(products);

        List<ProductDTO> result = productService.getProductsByCategory(1L);

        assertEquals(2, result.size());
        assertEquals("Phone", result.get(0).getName());
        assertEquals("Electronics", result.get(0).getCategory().getName());
        verify(productRepository, times(1)).findByCategory(category);
    }

    @Test
    void testFindById_ProductFound() throws ServiceException {
        
        Product product = new Product(1L, "Product", "Description", 50.0, new Category(), new User());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertEquals("Product", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        
        ServiceException exception = assertThrows(ServiceException.class, () -> productService.findById(1L));
        assertEquals("Product not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
