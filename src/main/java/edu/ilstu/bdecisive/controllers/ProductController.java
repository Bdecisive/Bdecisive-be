package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.ProductDTO;
import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("{userId}/vendor")
    public List<ProductDTO> getProductsByVendor(@PathVariable long userId) {
        return productService.getProductsByVendor(userId);
    }

    @PostMapping("create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) throws ServiceException {
        productService.create(requestDTO);
        return ResponseEntity.ok("Product created successfully");
    }

    @GetMapping("post_product_review")
    public ResponseEntity<?> postProductReview(@RequestBody ProductReviewDTO requestDTO) throws ServiceException {
        try {
            productService.productReview(requestDTO);
            return ResponseEntity.ok("Review Posted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("{productId}/update")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductRequestDTO requestDTO) throws ServiceException {
        try {
            productService.productUpdate(productId, requestDTO);
            return ResponseEntity.ok("Product updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("{productId}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) throws ServiceException {
        try {
            productService.delete(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
