package edu.ilstu.bdecisive.controllers;

import edu.ilstu.bdecisive.dtos.ProductRequestDTO;
import edu.ilstu.bdecisive.dtos.ProductReviewDTO;
import edu.ilstu.bdecisive.services.ProductService;
import edu.ilstu.bdecisive.utils.ServiceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("create")
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

    @GetMapping("updateproduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequestDTO requestDTO) throws ServiceException {
        try {
            productService.productUpdate(requestDTO);
            return ResponseEntity.ok("Product updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
