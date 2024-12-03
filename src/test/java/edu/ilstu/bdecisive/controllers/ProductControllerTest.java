package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    public ProductControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductsByVendor() throws ServiceException {
        long userId = 1L;
        List<ProductDTO> mockProducts = Arrays.asList(
                new ProductDTO(1L, "Product 1", "Description 1", 10.0, null, "2023-01-01", "2023-01-02"),
                new ProductDTO(2L, "Product 2", "Description 2", 20.0, null, "2023-01-01", "2023-01-02")
        );
        when(productService.getProductsByVendor(userId)).thenReturn(mockProducts);

        List<ProductDTO> result = productController.getProductsByVendor(userId);

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        verify(productService, times(1)).getProductsByVendor(userId);
    }

    @Test
    void testGetProductsByCategory() throws ServiceException {
        long categoryId = 1L;
        List<ProductDTO> mockProducts = Arrays.asList(
                new ProductDTO(3L, "Product A", "Description A", 15.0, null, "2023-01-01", "2023-01-02"),
                new ProductDTO(4L, "Product B", "Description B", 25.0, null, "2023-01-01", "2023-01-02")
        );
        when(productService.getProductsByCategory(categoryId)).thenReturn(mockProducts);

        List<ProductDTO> result = productController.getProductsByCategory(categoryId);

        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getName());
        verify(productService, times(1)).getProductsByCategory(categoryId);
    }

    @Test
    void testCreateProduct() throws ServiceException {
        ProductRequestDTO requestDTO = new ProductRequestDTO(1L, "Product Name", "Description", 50.0, 1L, 1L);
        doNothing().when(productService).create(requestDTO);

        ResponseEntity<?> response = productController.createProduct(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product created successfully", response.getBody());
        verify(productService, times(1)).create(requestDTO);
    }

    @Test
    void testUpdateProduct() throws ServiceException {
        long productId = 1L;
        ProductRequestDTO requestDTO = new ProductRequestDTO(1L, "Updated Name", "Updated Description", 60.0, 2L, 1L);
        doNothing().when(productService).productUpdate(productId, requestDTO);

        ResponseEntity<?> response = productController.updateProduct(productId, requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product updated successfully", response.getBody());
        verify(productService, times(1)).productUpdate(productId, requestDTO);
    }

    @Test
    void testDeleteProduct() throws ServiceException {
        long productId = 1L;
        doNothing().when(productService).delete(productId);

        ResponseEntity<?> response = productController.deleteProduct(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product deleted successfully", response.getBody());
        verify(productService, times(1)).delete(productId);
    }
}
